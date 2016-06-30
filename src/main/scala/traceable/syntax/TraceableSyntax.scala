package traceable.syntax

import Recorders.ITransitionRecorder
import cats._
import traceable.core.{GraphEdgeFunction, GraphEdgeFunction2, TraceableCoreOps, Traceable}
import cats.syntax.all._

import scala.language.{higherKinds, implicitConversions}
import scala.math.Fractional

trait TraceableSyntax extends ITransitionRecorder with TraceableCoreOps {

  import traceable.core.TraceableCoreOps
  import cats.std.list._
  import cats.syntax.cartesian._

  implicit def TraceableCanBeFractional[T](implicit ev: Fractional[T]): Fractional[Traceable[T]] = new Fractional[Traceable[T]] {

    import Fractional.Implicits._

    override def plus(x: Traceable[T], y: Traceable[T]): Traceable[T] = {

      val result = Traceable(x.value + y.value)
      recordTransition(x, result, "+")
      recordTransition(y, result, "+")
      result
    }

    override def toDouble(x: Traceable[T]): Double = ev.toDouble(x.value)

    override def toFloat(x: Traceable[T]): Float = ev.toFloat(x.value)

    override def toInt(x: Traceable[T]): Int = ev.toInt(x.value)

    override def negate(x: Traceable[T]): Traceable[T] = x.map(ev.negate)

    override def fromInt(x: Int): Traceable[T] = Traceable(ev.fromInt(x))

    override def toLong(x: Traceable[T]): Long = ev.toLong(x.value)

    override def times(x: Traceable[T], y: Traceable[T]): Traceable[T] =
      (cartesianSyntax(x) |@| y).map { case (a, b) => ev.times(a, b) }

    override def minus(x: Traceable[T], y: Traceable[T]): Traceable[T] =
      (x |@| y).map { case (a, b) => ev.minus(a, b) }

    override def compare(x: Traceable[T], y: Traceable[T]): Int = ev.compare(x.value, y.value)

    override def div(x: Traceable[T], y: Traceable[T]): Traceable[T] = for {a <- x; b <- y} yield { a / b }
  }

  implicit def TraceableCanBeSemigroup[T](implicit ev: Semigroup[T]): Semigroup[Traceable[T]] = new Semigroup[Traceable[T]] {
    override def combine(x: Traceable[T], y: Traceable[T]): Traceable[T] = {


      val curryCombine: T => T => T = (z) => ev.combine(z, _)
      //val graphFun: T => T => T = (x) => GraphEdgeFunction(ev.combine.curried(x)(_), "|+|")

      (x |@| y).map( GraphEdgeFunction2(ev.combine, "|+|") )

    }
  }

}
