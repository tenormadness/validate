package Validation

import cats._
import cats.syntax.cartesian._
import cats.syntax.semigroup._
import scala.language.{higherKinds, implicitConversions}
import scala.math.Fractional
import scala.math.Fractional.Implicits._

object ValidationSyntax {

  implicit def validationCanBeFractional[T](implicit ev: Fractional[T]): Fractional[Validation[T]] = new Fractional[Validation[T]] {

    override def plus(x: Validation[T], y: Validation[T]): Validation[T] =
      (Descriptor("+") |@| x |@| y).map { case (_, a, b) => ev.plus(a, b) }

    override def toDouble(x: Validation[T]): Double = ev.toDouble(x.value)

    override def toFloat(x: Validation[T]): Float = ev.toFloat(x.value)

    override def toInt(x: Validation[T]): Int = ev.toInt(x.value)

    override def negate(x: Validation[T]): Validation[T] =
      (Descriptor("-") |@| x).map((d, a) => ev.negate(a))

    override def fromInt(x: Int): Validation[T] = Validate(ev.fromInt(x))

    override def toLong(x: Validation[T]): Long = ev.toLong(x.value)

    override def times(x: Validation[T], y: Validation[T]): Validation[T] =
      (Descriptor("*") |@| x |@| y).map { case (_, a, b) => ev.times(a, b) }

    override def minus(x: Validation[T], y: Validation[T]): Validation[T] =
      (Descriptor("-") |@| x |@| y).map { case (_, a, b) => ev.minus(a, b) }

    override def compare(x: Validation[T], y: Validation[T]): Int = ev.compare(x.value, y.value)

    override def div(x: Validation[T], y: Validation[T]): Validation[T] =
      (Descriptor("/") |@| x |@| y).map { case (_, a, b) => a / b }
  }

  implicit def validationCanBeSemigroup[T](implicit ev: Semigroup[T]) = new Semigroup[Validation[T]] {
    override def combine(x: Validation[T], y: Validation[T]): Validation[T] =
      (Descriptor("|+|") |@| x |@| y).map { case (_, a, b) => a |+| b }
  }

  implicit def functorCombine[F[_]](implicit ev: Functor[F]) = new Functor.Composite[Validation, F] {
    override def F: Functor[Validation] = Validation.ValidationApIsApplicative
    override def G: Functor[F] = ev
  }

//  TODO: WTF!  Noel's suggest that every validation wraps around a Monad by default, then I could pipe functors through Validation easily but I would need to provide an ID monad for any simple type
//  implicit def functorOps[F[_]](implicit ev: Functor[F]) = new Functor[Validation[F[_]]] {
//    override def map[A, B](fa: Validation[A])(f: (A) => B): Validation[B] = functorCombine(ev).map(fa)()
//  }
  // TODO: This is half baked, not very nice at all
  implicit class ValidationCombiner[F[_]: Functor, A](fa: Validation[F[A]]) {
    def mapValidate[B](f: A=> B): Validation[F[B]] = (Descriptor("function: " + f.toString()) |@| fa).map {case (_, f0) => implicitly[Functor[F]].map(f0)(f)}
  //functorCombine[F].map(fa)(f)
  }

}
