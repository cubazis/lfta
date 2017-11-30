package messaging.producers

import java.util.Properties
import java.util.concurrent.Future

import messaging.serialization.AnalysedRecordSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}
import org.apache.kafka.common.serialization.StringSerializer
import schema.AnalysedRecord

/**
  * date 30/08/2017
  */
object ApplicationProducer extends Serializable {
  val properties = new Properties()

  properties.setProperty("bootstrap.servers", "0.0.0.0:9092")
  properties.setProperty("key.serializer", classOf[StringSerializer].getName)
  properties.setProperty("value.serializer", classOf[StringSerializer].getName)
  properties.setProperty("acks", "1")
  properties.setProperty("linger.ms", "1")

  val producer = new KafkaProducer[String, String](properties)

  def produce(rec: ProducerRecord[String, String]): Future[RecordMetadata] = {
    producer.send(rec)
  }
}
