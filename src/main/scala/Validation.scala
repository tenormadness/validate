package Validation

import cats.{Eval, Applicative, Traverse}

sealed abstract class Validation[T] {

  def value: T

  def test(tester: T => Boolean, errorMessage: String): Validation[T] = this match {

    case v: Validating[T, tt] =>
      if (tester(v.value)) { v }
      else {Failure( errorMessage, v) }

    case f: Failure[T] => f
  }
}
object Validate {
  def apply[T](in: T): Validation[T] = Validating(in, Nil)
  //def apply[T](in: T, description: String): Validation[T] = Validating(in, Leaf(description))
}
object Descriptor {
  def apply(str: String): Validation[String] = Validating(str, Nil)
}

final case class Validating[T, TT](value: T, history: HistoryTree) extends Validation[T]
final case class Failure[T](message: String, validation: Validation[T]) extends Validation[T] {
  def value = validation.value
}

object Validation {

  implicit object ValidationApIsApplicative extends Applicative[Validation] {

    override def pure[A](x: A): Validation[A] = Validating(x, Nil)
    
    override def ap[A, B](ff: Validation[(A) => B])(fa: Validation[A]): Validation[B] = (ff, fa) match {
      case (func @ Validating(f, histF), validating @ Validating(value, histV)) =>
        Validating(f(value), Node(histF, histV))
      case (func @ Validating(f, histF), validating @ Failure(msg, value)) =>
        Failure(msg, ap(func)(value))
      case (func @ Failure(msg, f), validating @ Validating(value, histV)) =>
        Failure(msg, ap(f)(validating))
      case (func @ Failure(msg1, f), validating @ Failure(msg2, value)) =>
        Failure(msg1 + " && " + msg2, ap(f)(value))
    }

    override def product[A, B](fa: Validation[A], fb: Validation[B]): Validation[(A, B)] = (fa, fb) match {

      case (val1 @ Validating(value1, hist1), val2 @ Validating(value2, hist2)) =>
        Validating((value1, value2), Node(hist1, hist2))
      case (val1 @ Validating(value1, hist1), val2 @ Failure(msg2, value2)) =>
        Failure(msg2, product(val1, value2))
      case (val1 @ Failure(msg1, value1),     val2 @ Validating(value2, hist2)) =>
        Failure(msg1, product(value1, val2))
      case (val1 @ Failure(msg1, value1),     val2 @ Failure(msg2, value2)) =>
        Failure(msg1 + " && " + msg2, product(value1, value2))
    }

    override def map[A, B](fa: Validation[A])(f: (A) => B): Validation[B] = fa match {

      case validating @ Validating(value, hist) => Validating(f(value), Node(Leaf(value), hist))
      case Failure(msg, value) => Failure(msg, map(value)(f))
    }
  }

}


