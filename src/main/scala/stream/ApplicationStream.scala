package stream

import java.util.UUID

import batch.RakeGlobal
import engine.Rake
import messaging.producers.ApplicationProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.spark.SparkConf
import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import schema.{AnalysedRecord, AnalysedRecordList}

/**
  * date 23/08/2017
  */
object ApplicationStream {
  // TODO: via env vars
  System.setProperty("twitter4j.oauth.consumerKey", "consumerKey")
  System.setProperty("twitter4j.oauth.consumerSecret", "consumerSecret")
  System.setProperty("twitter4j.oauth.accessToken", "accessToken")
  System.setProperty("twitter4j.oauth.accessTokenSecret", "accessTokenSecret")

  val sparkConf = new SparkConf().setAppName("LFTA").setMaster("local[2]") // local
  val ssc = new StreamingContext(sparkConf, Seconds(5))
  val stream = TwitterUtils.createStream(ssc, None, Array("USA", "Trump"))
  val spark: SparkSession = SparkSession.builder().getOrCreate()

  lazy val model = PipelineModel.load("src/main/resources/cold_start_model")
  val scores = RakeGlobal.coldStart.take(1000).toMap

  import com.datastax.spark.connector._
  import spark.implicits._

  stream.foreachRDD{ rdd =>
    val in = rdd.map(tweet => (1, tweet.getText)).toDF("tmp", "SentimentText")
    val df = model.transform(in)
    val ars = df.select("SentimentText", "prediction").map { row =>
      val text = row(0).asInstanceOf[String].trim.replaceAll("""[^\p{L}\p{Nd}]+""", " ")
      val reason = Rake.run(text).map {
        case (word, score) =>
          val newScore = if (scores.keys.toList.contains(word)) scores(word) else score
          (word, newScore)
      }.sortBy(-_._2)

      AnalysedRecord(UUID.randomUUID().toString, row(0).asInstanceOf[String], row(1).asInstanceOf[Double].toInt, reason.head._1)
    }

    ars.rdd.saveToCassandra("test", "lfta", SomeColumns("tid", "text", "sentiment", "reason"))

    val records = ars.collect().toList
    val toMQ = AnalysedRecordList(records).toString
    val record = new ProducerRecord[String, String]("tweets", "3", toMQ)
    ApplicationProducer.produce(record)
  }

  def start(): Unit = {
    ssc.start()
    ssc.awaitTermination()
  }
}
