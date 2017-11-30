package messaging.serialization

import java.util

import org.apache.kafka.common.serialization.Deserializer
import schema.AnalysedRecord

import scala.util.parsing.json.JSON

/**
  * date 30/08/2017
  */
class AnalysedRecordDeserializer extends Deserializer[List[AnalysedRecord]] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def close(): Unit = {}

  override def deserialize(topic: String, data: Array[Byte]): List[AnalysedRecord] = {
    JSON.parseFull(new String(data)) match {
      case Some(d: List[AnalysedRecord]) => d
      case None => List.empty
    }
  }
}
