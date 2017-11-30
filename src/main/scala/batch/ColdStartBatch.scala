package batch

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.{HashingTF, Tokenizer}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._


/**
  * date 14/09/2017
  *
  * sentiment analysis model for cold start
  *
  * @author IceKhan
  */
object ColdStartBatch {
  val spark = SparkSession.builder.master("local")
    .appName("lfta cold start model")
    .getOrCreate()

  val df = spark.read.option("header", "true")
    .csv("src/main/resources/sad.csv")

  val train = df.withColumn("Sentiment", df("Sentiment").cast(DoubleType))

  val tokenizer = new Tokenizer()
    .setInputCol("SentimentText")
    .setOutputCol("words")
  val hashingTF = new HashingTF()
    .setNumFeatures(1000)
    .setInputCol(tokenizer.getOutputCol)
    .setOutputCol("features")
  val lr = new LogisticRegression()
    .setLabelCol("Sentiment")
    .setMaxIter(10)
    .setRegParam(0.001)

  val pipeline = new Pipeline()
    .setStages(Array(tokenizer, hashingTF, lr))

  val model = pipeline.fit(train)

  model.write.overwrite().save("src/main/resources/cold_start_model")
}
