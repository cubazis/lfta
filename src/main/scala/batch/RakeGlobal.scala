package batch

import engine.Rake
import org.apache.spark.sql.SparkSession

/**
  * date 16/09/2017
  *
  * @author IceKhan
  */
object RakeGlobal {
  private val spark = SparkSession.builder.master("local")
    .appName("rake cold start")
    .getOrCreate()

  def coldStart: List[(String, Double)] = {
    spark.read.option("header", "true")
      .csv("src/main/resources/sad.csv")
      .createOrReplaceTempView("twitter_sentiment")

    val hugeString = spark.sql(
      """
        |SELECT SentimentText
        |FROM twitter_sentiment
        |LIMIT 1000
      """.stripMargin).collect()
      .map { row =>
        row.getAs[String](0).trim().replaceAll("""[^\p{L}\p{Nd}]+""", " ")
      }.toList.mkString(".")

    Rake.run(hugeString).filter(_._2 > 2.0)
  }
}
