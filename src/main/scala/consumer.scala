/**
  * date 31/08/2017
  *
  * @author IceKhan
  */
import java.util.concurrent._
import java.util.{Collections, Properties}

import kafka.consumer.KafkaStream
import kafka.utils.Logging
import messaging.serialization.AnalysedRecordDeserializer
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.codehaus.jackson.map.deser.std.StringDeserializer
import schema.AnalysedRecord

import scala.collection.JavaConversions._

class ScalaConsumerExample(val brokers: String,
                           val groupId: String,
                           val topic: String) extends Logging {

  val props = createConsumerConfig(brokers, groupId)
  val consumer = new KafkaConsumer[String, String](props)
  var executor: ExecutorService = null

  def shutdown() = {
    if (consumer != null)
      consumer.close();
    if (executor != null)
      executor.shutdown();
  }

  def createConsumerConfig(brokers: String, groupId: String): Properties = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")
    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props
  }

  def run() = {
    consumer.subscribe(Collections.singletonList(this.topic))

    Executors.newSingleThreadExecutor.execute(    new Runnable {
      override def run(): Unit = {
        while (true) {
          val records = consumer.poll(1000)

          for (record <- records) {
            System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset())
          }
        }
      }
    })
  }
}

object consumer {
  def main(args: Array[String]): Unit = {
    val example = new ScalaConsumerExample("0.0.0.0:9092", "lfta", "records")
    example.run()
    println("asdasd")
  }
}
