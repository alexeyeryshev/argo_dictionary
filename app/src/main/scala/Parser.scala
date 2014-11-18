package argoParser
/**
 * Created by eryshev-alexey on 11/17/14.
 */

import java.net.URL

import org.htmlcleaner.{TagNode, HtmlCleaner}

import scala.util.Try
import play.api.libs.json._

object ArgoParser {
  def getWordsFromUrl(url: String): List[wordFrRu] = {
    val cleaner = new HtmlCleaner

    val rootNode = cleaner.clean(new URL(url))
    val elements = rootNode.getElementsByName("div", true)
    val wordsElements =
      elements.filter(elem => Some(elem.getAttributes.get("itemtype")).getOrElse("") == "http://schema.org/ScholarlyArticle") map {
        elem =>
          def getElementsByClassValue(value: String) = elem.getElementsByAttValue("class", value, true, false)(0).getText.toString

          val word = getElementsByClassValue("lem")
          val sex = getElementsByClassValue("grm")
          val translate = getElementsByClassValue("trs")
          val example = Try(getElementsByClassValue("cit")).getOrElse("isNotExisted")
          val exampleTranslated = Try(getElementsByClassValue("ctr")).getOrElse("isNotExisted")
          val exampleSource = Try(getElementsByClassValue("cits")).getOrElse("isNotExisted")
          wordFrRu(word, sex, translate, example, exampleTranslated, exampleSource)
      }
    wordsElements.toList
  }

  def toJson(wordsList: List[wordFrRu]): JsValue = Json.toJson(wordsList)

  /*implicit val wordWrites = new Writes[wordFrRu] {
    def writes(word: wordFrRu): JsValue = JsObject(Seq(
      word.word -> JsObject(
        "sex" -> JsString(word.sex),
        "translate" -> JsString(word.translate),
        "example" -> JsString(word.example),
        "exampleTranslated" -> JsString(word.exampleTranslated),
        "exampleSource" -> JsString(word.exampleSource)
      ))
    )
  }*/

  implicit val wordListWrites = new Writes[List[wordFrRu]] {
    def writes(words: List[wordFrRu]): JsValue =
      words.foldRight(JsObject(Seq())) {
        case(word, acc) => acc ++ JsObject(Seq(
          word.word -> JsObject(Seq(
            "sex" -> JsString(word.sex),
            "translate" -> JsString(word.translate),
            "example" -> JsString(word.example),
            "exampleTranslated" -> JsString(word.exampleTranslated),
            "exampleSource" -> JsString(word.exampleSource)
          )))
        )
      }
  }
}

case class wordFrRu(word: String, sex: String, translate: String, example: String, exampleTranslated: String, exampleSource: String)