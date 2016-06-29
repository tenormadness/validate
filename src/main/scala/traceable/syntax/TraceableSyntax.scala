package traceable.syntax

import cats._
import traceable.Core.Traceable

import scala.language.{higherKinds, implicitConversions}
import scala.math.Fractional

object TraceableSyntax {

  implicit def TraceableCanBeFractional[T](implicit ev: Fractional[T]): Fractional[Traceable[T]] = new Fractional[Traceable[T]] {

    override def plus(x: Traceable[T], y: Traceable[T]): Traceable[T] =
      (Descriptor("+") |@| x |@| y).map { case (_, a, b) => ev.plus(a, b) }

    override def toDouble(x: Traceable[T]): Double = ev.toDouble(x.value)

    override def toFloat(x: Traceable[T]): Float = ev.toFloat(x.value)

    override def toInt(x: Traceable[T]): Int = ev.toInt(x.value)

    override def negate(x: Traceable[T]): Traceable[T] =
      (Descriptor("-") |@| x).map((d, a) => ev.negate(a))

    override def fromInt(x: Int): Traceable[T] = Validate(ev.fromInt(x))

    override def toLong(x: Traceable[T]): Long = ev.toLong(x.value)

    override def times(x: Traceable[T], y: Traceable[T]): Traceable[T] =
      (Descriptor("*") |@| x |@| y).map { case (_, a, b) => ev.times(a, b) }

    override def minus(x: Traceable[T], y: Traceable[T]): Traceable[T] =
      (Descriptor("-") |@| x |@| y).map { case (_, a, b) => ev.minus(a, b) }

    override def compare(x: Traceable[T], y: Traceable[T]): Int = ev.compare(x.value, y.value)

    override def div(x: Traceable[T], y: Traceable[T]): Traceable[T] =
      (Descriptor("/") |@| x |@| y).map { case (_, a, b) => a / b }
  }

  implicit def TraceableCanBeSemigroup[T](implicit ev: Semigroup[T]) = new Semigroup[Traceable[T]] {
    override def combine(x: Traceable[T], y: Traceable[T]): Traceable[T] =
      (Descriptor("|+|") |@| x |@| y).map { case (_, a, b) => a |+| b }
  }

  implicit def functorCombine[F[_]](implicit ev: Functor[F]) = new Functor.Composite[Traceable, F] {
    override def F: Functor[Traceable] = Traceable.TraceableApIsApplicative
    override def G: Functor[F] = ev
  }

//  TODO: WTF!  Noel's suggest that every Traceable wraps around a Monad by default, then I could pipe functors through Traceable easily but I would need to provide an ID monad for any simple type
//  implicit def functorOps[F[_]](implicit ev: Functor[F]) = new Functor[Traceable[F[_]]] {
//    override def map[A, B](fa: Traceable[A])(f: (A) => B): Traceable[B] = functorCombine(ev).map(fa)()
//  }
  // TODO: This is half baked, not very nice at all
  implicit class TraceableCombiner[F[_]: Functor, A](fa: Traceable[F[A]]) {
    def mapValidate[B](f: A=> B): Traceable[F[B]] = (Descriptor("function: " + f.toString()) |@| fa).map {case (_, f0) => implicitly[Functor[F]].map(f0)(f)}
  //functorCombine[F].map(fa)(f)
  }

}
