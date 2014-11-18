package argoParser
/**
 * Created by eryshev-alexey on 11/17/14.
 */

import java.net.URL

import org.htmlcleaner.HtmlCleaner

import scala.collection.mutable.ListBuffer

trait Parser {
  def getHeadlinesFromUrl(url: String): List[String] = {
    var stories = new ListBuffer[String]
    val cleaner = new HtmlCleaner

    val rootNode = cleaner.clean(new URL(url))
    val elements = rootNode.getElementsByName("div", true)
    val wordsElements =
      elements.filter(elem => Some(elem.getAttributes.get("itemtype")).getOrElse("") == "http://schema.org/ScholarlyArticle") map {
        elem => elem.getText.toString
      }
    wordsElements.foreach(println)

    /*val props = cleaner.getProperties
    val rootNode = cleaner.clean(new URL(url))
    val elements = rootNode.getElementsByName("a", true)
    for (elem <- elements) {
      val classType = elem.getAttributeByName("class")
      if (classType != null && classType.equalsIgnoreCase("articleTitle")) {
        // stories might be "dirty" with text like "'", clean it up
        val text = StringEscapeUtils.unescapeHtml4(elem.getText.toString)
        stories += text
      }
    }
    return stories.filter(storyContainsDesiredPhrase(_)).toList
  }*/
    List()
  }
}
