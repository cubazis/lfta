package schema

import scala.util.parsing.json.{JSONArray, JSONObject}

/**
  * date 24/08/2017
  */
case class AnalysedRecord(tid: String, text: String, sentiment: Int, reason: String) {
  def toJSON = {
    JSONObject(Map(
      "tid" -> tid,
      "sentiment" -> sentiment,
      "text" -> text,
      "reason" -> reason
    ))
  }
}

case class AnalysedRecordList(records: List[AnalysedRecord]) {
  override def toString: String = {
    JSONArray(records map (_.toJSON))
      .toString(scala.util.parsing.json.JSONFormat.defaultFormatter)
  }
}