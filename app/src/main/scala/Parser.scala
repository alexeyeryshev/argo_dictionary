import java.net.URL

import org.htmlcleaner.HtmlCleaner
import play.api.libs.json._

import scala.util.Try

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
          val translation = getElementsByClassValue("art2")
          val example = Try(getElementsByClassValue("cit")).getOrElse("isNotExisted")
          val exampleTranslated = Try(getElementsByClassValue("ctr")).getOrElse("isNotExisted")
          val exampleSource = Try(getElementsByClassValue("cits")).getOrElse("isNotExisted")
          wordFrRu(word, sex, translate, translation, example, exampleTranslated, exampleSource)
      }
    wordsElements.toList
  }

  def toJson(wordsList: List[wordFrRu]): JsValue = Json.toJson(wordsList)

  implicit val wordListWrites = new Writes[List[wordFrRu]] {
    def writes(words: List[wordFrRu]): JsValue =
      words.foldRight(JsObject(Seq())) {
        case(word, acc) => acc ++ JsObject(Seq(
          word.word -> JsObject(Seq(
            "sex" -> JsString(word.sex),
            "transcription" -> JsString(word.translate),
            "translation" -> JsString(word.translation.substring(0, {
              val endOfLine = word.translation.indexWhere(_ == '.')
              if (endOfLine > 0) endOfLine else word.translation.length
            })),
            "example" -> JsString(word.example),
            "exampleTranslated" -> JsString(word.exampleTranslated),
            "exampleSource" -> JsString(word.exampleSource)
          )))
        )
      }
  }

  def getDictionary: JsValue = {
    val letters = "ABCDEFGHIJKLMNOŒPQRSTUVWXYZ"
    val listByLetter = letters.map(l => JsObject(Seq(s"$l" -> toJson(getWordsFromUrl(s"http://www.russki-mat.net/page.php?l=FrRu&a=$l"))))).toSeq
    JsObject(Seq("FrRu" -> JsArray(listByLetter)))
  }
}

case class wordFrRu(word: String, sex: String, translate: String, translation: String, example: String, exampleTranslated: String, exampleSource: String)