package messaging.producers

import org.apache.kafka.clients.producer.ProducerRecord
import schema.AnalysedRecord

/**
  * date 30/08/2017
  */
object DemoTest {
  while (true) {
    val ar = AnalysedRecord("123123", "asdas", 1, "")
    val record = new ProducerRecord[String, String]("records", "1", "asdasd")
    ApplicationProducer.produce(record)

    println(s"Produced ${ar.toString}...")

    Thread.sleep(1000)
  }

}
