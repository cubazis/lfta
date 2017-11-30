package messaging.serialization

import java.util

import org.apache.kafka.common.serialization.Serializer
import schema.AnalysedRecord

import scala.util.parsing.json.{JSONArray, JSONObject}

/**
  * date 30/08/2017
  */
class AnalysedRecordSerializer extends Serializer[List[AnalysedRecord]]  {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  private def toJSON(analysedRecord: AnalysedRecord) = {
    JSONObject(Map(
      "tid" -> analysedRecord.tid,
      "text" -> analysedRecord.text,
      "sentiment" -> analysedRecord.sentiment,
      "reason" -> analysedRecord.reason
    ))
  }

  override def serialize(topic: String, data: List[AnalysedRecord]): Array[Byte] = {
    JSONArray(data map (x => toJSON(x)))
      .toString(scala.util.parsing.json.JSONFormat.defaultFormatter).getBytes()
  }

  override def close(): Unit = {}
}
