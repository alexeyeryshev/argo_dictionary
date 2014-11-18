/**
 * Created by eryshev-alexey on 11/5/14.
 */
import argoParser.ArgoParser

object main extends App {
  println("Hello World")
  var wordsList = ArgoParser.getWordsFromUrl("file:///home/eryshev-alexey/Desktop/test.html")
  println(ArgoParser.toJson(wordsList))
}
