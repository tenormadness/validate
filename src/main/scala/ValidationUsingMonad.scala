/** Broken, should go back and try to make this work */

/*

package Validation

import cats.Monad
import cats.syntax.cartesian._
import cats.syntax.flatMap._
import cats.syntax.functor._



sealed abstract class Validation[T] {

  def test(tester: T => Boolean, errorMessage: String): Validation[T] = this match {

    case v: Validating[T, tt] =>
      if (tester(v.value)) { v }
      else {Failure( errorMessage, v) }

    case f: Failure[T] => f

  }
}
object Descriptor {
  def apply(str: String): Validation[String] = Validating(str, Nil)
}
object Validate {
  def apply[T](in: T) = Validating(in, Nil)
  def apply[T](in: T, description: String) = Validating(in, List(description))
}

final case class Validating[T, TT](value: T, history: List[TT]) extends Validation[T]
final case class Failure[T](message: String, value: Validation[T]) extends Validation[T]

object Validation {

  implicit object ValidationIsMonad extends Monad[Validation] {

    override def pure[A](x: A): Validation[A] = Validating(x, Nil)

    override def flatMap[A, B](currentValue: Validation[A])(function: (A) => Validation[B]): Validation[B] = {
      currentValue match {

        case val1@Validating(value, hist1) =>
          function(value) match {
            case result@Validating(finalValue, fcnHist) =>
              Validating(finalValue, List(fcnHist, value :: hist1))
            case failed@Failure(msg, finalValue) =>
              Failure(msg, unpack(finalValue, value :: hist1))
          }


        case val1@Failure(msg1, value1) => Failure(msg1, flatMap(value1)(function))

      }
    }

    def unpack[T <: Any, TT <: Any, O >: T](validation: Validation[T], hist: List[TT]): Validation[T] = {
      validation match {
        case val1 @ Failure(msg, value) => Failure(msg, unpack(value, hist))
        case val1 @ Validating(value, histFcn) => Validating(value, List(histFcn, hist))
      }
    }
  }
}

object Tester extends App {

  import Validation._

  val one: Validation[Int] = Monad[Validation].pure(1)
  println(s"one = $one")

  //val two = one.validateMap(_ + 1)("+ 1")
  val two = one.map(_ + 1)
  println(s"two = $two")

  val three = one.flatMap(x => two.map(y=> x + y))
//  val three = (one |@| two).tupled.validateMap(x => x._1 + x._2)("_ + _")
  //val three = one.flatMap(x => two.validateMap(y=> x + y)(" + "))
  println(s"three = $three")
  val threeV2 = (one |@| two |@| Validate("_ + _")).map((x, y, z) =>  x + y)
  //val threeV2 = (one |@| two).tupled.validateMap(x =>  x._1 + x._2)("_ + _")
  println(s"threev2 = $threeV2")
// val threeLine = two.validateMap(_ + 1)("+ 1")
//  println(s"threeLine = $threeLine")

  println(s"three > 0 ==> ${three.test(_ > 0, "you screwed up")}")

  val failThree = three.test(_ < 0, "less than zero test failed")
  println(s"failed test $failThree")

  val twoFail = failThree.flatMap(x => Validate(x - 1))
  val oneFail = (three |@| twoFail).map(_ - _)  //TODO: Fix this
  val minusOneFail = (twoFail |@| three).map(_ - _)  //TODO: Fix this
  println(s"one after failure = $oneFail")
  println(s"minus one after failure = $minusOneFail")

  val array1 = Monad[Validation].pure(Seq(1, 2, 3, 4))
  val array2 = Monad[Validation].pure(Seq(5, 6))
  val merge = (array1 |@| array2).map(_ ++ _)
  val norm = merge.map(_.sum)
  println(s"norm = $norm")


  val a = Monad[Validation].pure("a")
  val b = Monad[Validation].pure("b")
  val abStraight = (a |@| b).map(_ + " " + _)
//  println(s"ab = $abStraight")
//  val abContext = a.flatMap(x => b.flatMap(y=> Validating(x + " " + y, Validating("strSum", Nil))))
//  val abContext =
//    for {
//      aVal <- a
//      bVal <- b
//    } yield {
//      aVal + " " + bVal
//    }
//  println(s"ab = $abContext")
}*/
