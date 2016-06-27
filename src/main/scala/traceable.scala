package traceableCompute

import java.util.concurrent.ThreadLocalRandom

import Recorders.SimpleRecorder
import traceable.Core.{Funktor, Funktor2, Traceable, TraceableSeq}

object Square extends Funktor[Int, Int] {
  val name = "Square"
  def apply(a:Int) = a * a
}

object Even extends Funktor[Int, Boolean] {
  val name = "Even"
  def apply(a:Int) = a % 2 == 0
}

object Sum extends Funktor2[Int, Int] {
  val name = "Sum"
  def apply(b: Int, a:Int) = a + b
}
/*
case class Person(name:String, age:Int)
case class Title()
*/

object Test extends App {
  println("digraph {")
  val col = TraceableSeq(Range(1,5))
  val col2 = TraceableSeq(Range(6,11)).recordNodes(true).filter(Even).recordNodes
  col.recordNodes(true).map(Square)
    .recordNodes.map(Square)
    .filter(Even).zip(col2, Sum).reduce(Sum, 0).recordNodes(true)
  println("}")
}
