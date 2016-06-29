package traceable.syntax

import cats.std.list._
import cats.syntax.functor._
import cats.syntax.semigroup._
import traceable.core.Traceable

import scala.Predef.{any2stringadd => _, _}
import scala.math.Fractional
import scala.math.Fractional.Implicits._

object TracebleSyntaxTest extends App {

  import traceable.instances.TestTraceable._

  def graphToConsole: Unit = {
    println(printTransitions)
    clearGraph
  }
  // testing algebra

  println("SOME SIMPLE TESTS")

  val one = Traceable(1.0)
  recordNode(one)
  println(s"one = $one")
  graphToConsole

  val two = one + one
  recordNode(two)
  recordNode(one)

  val three = one + two
  recordNode(three)
  graphToConsole
  println(s"three = $three")

  val number = -Traceable(1.0) * (Traceable(5.0) - Traceable(3.0)) / Traceable(2.0)
  println(s"A failed test: $number")

  val oneMore = number + one
  println(s"But I can continue after failing!: $oneMore")

  println("\n\n")
//
//
  // TESTING MONOIDS
  println("TESTING MONOIDS")
  val list1x = List(1, 2 ,3)
  val list2x = List(1, 2 ,3)
  val listJoinx = list1x |+| list2x
  println(listJoinx)

  val list1 = Traceable(List(1, 2 ,3))
  val list2 = Traceable(List(1, 2 ,3))
  val listJoin = list1 |+| list2

  println(s"listJoin = $listJoin")
//
//
  // TESTING FUNCTIONS
  println("TESTING FUNCTIONS")

  val foo = Traceable("foo")  // "foo"+"bar"
  val function: String => String = _ + "bar"
  val bar = foo map function
  println(s"foobarred instantly: $bar" )


  val list = Traceable(List(1, 2, 3, 4))
  // this is triky becaue it is functor[functor]
  val x = list map (_ map (_ + 1))
  //val oneAdded2 = Functor.apply(list).map(_ + 1)
  println(s"Doublemapping: $x")

}
