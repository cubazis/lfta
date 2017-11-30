name := "lightning_fast_tweet_analysis"

version := "1.0"

scalaVersion := "2.11.11"

resolvers += "Apache Staging" at "https://repository.apache.org/content/groups/staging/"
resolvers += "Spark Packages Repo" at "https://dl.bintray.com/spark-packages/maven"
resolvers ++= Seq(
  Resolver.typesafeRepo("releases"),
  Resolver.sonatypeRepo("releases")
)

// spark
libraryDependencies ++= {
  val sparkVersion = "2.2.0"
  val cassandraConnectorVersion = "2.0.1-s_2.11"
  Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "org.apache.spark" %% "spark-sql" % sparkVersion,
    "org.apache.spark" % "spark-mllib_2.11" % sparkVersion,
    "org.apache.spark" % "spark-streaming_2.11" % sparkVersion,
    "org.apache.bahir" %% "spark-streaming-twitter" % sparkVersion,
    "datastax" % "spark-cassandra-connector" % cassandraConnectorVersion
  )
}


// akka
libraryDependencies ++= {
  val akkaV       = "2.5.3"
  val akkaHttpV   = "10.0.9"
  val scalaTestV  = "3.0.1"
  val akkaKafkaV  = "0.16"
  val akkaSseV    = "3.0.0"
  val corsV       = "0.1.10"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
    "org.scalatest"     %% "scalatest" % scalaTestV % "test",
    "de.heikoseeberger" %% "akka-sse" % akkaSseV,
    "ch.megard" %% "akka-http-cors" % corsV,
    "com.typesafe.akka" %% "akka-stream-kafka" % akkaKafkaV
  )
}

// kafka
libraryDependencies ++= {
  val kafkaVersion = "0.10.2.1"
  Seq(
    "org.apache.kafka" % "kafka_2.10" % kafkaVersion exclude("log4j", "log4j") exclude("org.slf4j","slf4j-log4j12")
  )
}


