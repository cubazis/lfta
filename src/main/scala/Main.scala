import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.kafka.ConsumerSettings
import akka.stream.ActorMaterializer
import messaging.producers.ApplicationProducer
import messaging.serialization.AnalysedRecordDeserializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import schema.AnalysedRecord
import stream.ApplicationStream
import web.Endpoints

/**
  * date 19/08/2017
  */
object Main extends App with Endpoints {
  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorMaterializer()
  override val logger = Logging(system, getClass)
  val consumerSettings = ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
    .withBootstrapServers("127.0.0.1:9092")
    .withGroupId("lfta")
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")

  Http().bindAndHandle(routes, "0.0.0.0", 8000)
  println("Server is running on 8000...")

  ApplicationStream.start()
}
