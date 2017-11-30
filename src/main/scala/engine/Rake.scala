package engine

import scala.io.Source
import scala.util.matching.Regex

/**
  * date 15/09/2017
  *
  * keywords extraction, based on https://github.com/aneesha/RAKE/blob/master/rake.py
  *
  * @author IceKhan
  */
object Rake {
  private val sentenceDelimiters = """[.!?,;:\t\\\\"\\(\\)\\\'\u2019\u2013]|\\s\\-\\s"""
  private val stopPath = "src/main/resources/stop_words.txt"

  private def splitSentences(text: String): List[String] = text.split(sentenceDelimiters).toList

  private def buildStopWordRegex(): String = Source.fromFile(stopPath).getLines.map(sw => s"""\\b$sw(?![\\w-])""").mkString("|")

  private def generateCandidateKeywords(sentenceList: List[String], swp: String): List[String] = {
    sentenceList.flatMap { sentence =>
      sentence.trim.replaceAll(swp, "|").split("\\|").toList
    }.map(_.toLowerCase().trim).filter(_.nonEmpty)
  }

  private def separateWords(text: String, minWordReturnSize: Int): List[String] = {
    text.split(" ")
      .map(word => word.toLowerCase().trim)
      .filter(word => word.length > minWordReturnSize && word.nonEmpty && !word.matches("""\d+"""))
      .toList
  }

  private def calculateWordScore(phraseList: List[String]): Map[String, Double] = {
    val tmp = phraseList.flatMap { phrase =>
      val wordList = separateWords(phrase, 0)
      val wlDegree = wordList.length - 1

      wordList.map { word =>
        ((word, 1), (word, wlDegree))
      }
    }

    val wordFrequency = tmp.map(_._1).groupBy(_._1).map(x => (x._1, x._2.length))
    val wordDegree = tmp.map(_._2).groupBy(_._1).map(x => (x._1, x._2.reduce((a, b) => (a._1, a._2 + a._2))._2))

    val wordDegreeUpdated = wordFrequency.map(wf => (wf._1, wordDegree(wf._1) + wordFrequency(wf._1)) )

    wordFrequency.map { wf => (wf._1, wordDegreeUpdated(wf._1) / (wordFrequency(wf._1) * 1.0))}
  }

  private def generateCandidateKeywordScores(phraseList: List[String], wordScores: Map[String, Double]): Map[String, Double] = {
    phraseList.map { phrase =>
      val score = separateWords(phrase, 0).map { word => wordScores(word) }.sum
      (phrase, score)
    }.toMap
  }

  def run(text: String): List[(String, Double)] = {
    val sentenceList = splitSentences(text)
    val phraseList = generateCandidateKeywords(sentenceList, buildStopWordRegex())
    val wordScore = calculateWordScore(phraseList)
    val candidates = generateCandidateKeywordScores(phraseList, wordScore)
    candidates.toSeq.sortBy(-_._2).toList
  }
}
