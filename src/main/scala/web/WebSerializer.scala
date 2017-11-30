package web

import schema.{AnalysedRecord, AnalysedRecordList}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

/**
  * date 24/08/2017
  */
trait WebSerializer extends DefaultJsonProtocol {
  implicit val analysedRecordFormat: RootJsonFormat[AnalysedRecord] = jsonFormat4(AnalysedRecord)
  implicit val analysedRecordListFormat: RootJsonFormat[AnalysedRecordList] = jsonFormat1(AnalysedRecordList)
}
