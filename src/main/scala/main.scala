/**
 * Created by eryshev-alexey on 11/5/14.
 */
import argoParser.Parser

object main extends App {
  println("Hello World")
  new Parser {getHeadlinesFromUrl("file:///home/eryshev-alexey/Desktop/test.html")}
}
