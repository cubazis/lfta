package web

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.http.scaladsl.server.Directives._
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.Materializer
import schema.{AnalysedRecord, AnalysedRecordList}
import akka.kafka.scaladsl.Consumer

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.concurrent.duration._
import de.heikoseeberger.akkasse.scaladsl.model.ServerSentEvent
import ch.megard.akka.http.cors.CorsDirectives._
import ch.megard.akka.http.cors.CorsSettings

import scala.util.parsing.json.{JSONArray, JSONObject}

/**
  * date 24/08/2017
  */
trait Endpoints extends WebSerializer {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer
  implicit val jsonStreamingSupport: JsonEntityStreamingSupport = EntityStreamingSupport.json(1000 * 1024)
  val consumerSettings: ConsumerSettings[String, String]

  val logger: LoggingAdapter

  import de.heikoseeberger.akkasse.scaladsl.marshalling.EventStreamMarshalling._

  val corsSettings: CorsSettings.Default = CorsSettings.defaultSettings

  val routes = cors(corsSettings) {
    path("test") {
      get {
        complete("OK")
      }
    } ~ path("streaming" / Remaining) { topicName =>
      get {
        val source = Consumer.plainSource(consumerSettings, Subscriptions.topics(topicName))
          .map(consumerRecord => consumerRecord.value())
          .map(s =>ServerSentEvent(s.toString))
          .keepAlive(1 second, () => ServerSentEvent.heartbeat)

        complete(source)
      }
    }
  }
}
