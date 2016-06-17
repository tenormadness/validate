package Validation

import cats.syntax.functor._
import cats.syntax.cartesian._
import cats.syntax.semigroup._
import cats.std.list._
import ValidationSyntax._
import scala.math.Fractional
import Fractional.Implicits._
import Predef.{any2stringadd => _, _}

object ValidationTest extends App {

  import ValidationSyntax._
  // testing algebra

  println("SOME SIMPLE TESTS")

  val one = Validate(1.0)
  println(s"one = $one")

  val two = one + one
  println(s"two = $two")

  val three = one + two
  println(s"three = $three")

  val number = (-Validate(1.0) * (Validate(5.0) - Validate(3.0)) / Validate(2.0)).test(_ > 0, "This should have been larger than zero")
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

  val list1 = Validate(List(1, 2 ,3))
  val list2 = Validate(List(1, 2 ,3))
  val listJoin = list1 |+| list2

  println(s"listJoin = $listJoin")
//
//
  // TESTING FUNCTIONS
  println("TESTING FUNCTIONS")

  val foo = Validate("foo")  // "foo"+"bar"
  val function: String => String = _ + "bar"
  val bar = foo map function
  println(s"foobarred instantly: $bar" )

  val barWithcomment = (Descriptor("+bar") |@| foo).tupled.map(x => function(x._1))
  println(s"what a mess: $bar" )


  val list = Validate(List(1, 2, 3, 4))
  // this is triky becaue it is functor[functor]
  val x = list map (_ map (_ + 1))
  val xx = functorCombine[List].map(list)(_ + 1)
  val xxx = list mapValidate (_ + 1)
  //val oneAdded2 = Functor.apply(list).map(_ + 1)
  println(s"Doublemapping: $x")
  println(s"Doublemapping: $xx")
  println(s"Doublemapping: $xxx")

  println(s"What a mess! We would need validated functions as a primitive :(")

  object PlusOne extends (Int => Int) {

    override def apply(v1: Int): Int = v1 + 1

    override def toString() = "_ + 1"
  }

  val xxxx = list mapValidate PlusOne
  println(s"Properly done: $xxxx")
}
