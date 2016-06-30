package traceable.core

import java.util.concurrent.ThreadLocalRandom

import Recorders.ITransitionRecorder
import cats.{Monad, Applicative}

sealed abstract class CanBeTraced[T] {
  def id: Long
  def value: T
}

sealed trait ITraceableFunction[A, B] extends (A => B) {

  def tag: String
  def f: A => B

  override def apply(in: A): B = f.apply(in)

  override def toString(): String = "Function: " + tag

}

/** A value that is traced, also a node in the graph */
final case class Traceable[T](value: T, id: Long) extends CanBeTraced[T]

/** An enriched function that can tag graph edges when applied */
final case class TraceableFunction[A, B](f: A => B, tag: String) extends ITraceableFunction[A, B]

/** A special enconding for a grpah node that is a function (not sure we need this) */
final case class FunctionNode[A, B](f: A => B, tag: String, id: Long) extends CanBeTraced[String] with ITraceableFunction[A, B] {
  override val value: String = tag
}

// Some constructors
object Traceable {
  def apply[T](value: T) = Traceable(value, ThreadLocalRandom.current().nextLong)
}

object FunctionNode {
  def apply[A, B](f: A => B, tag: String) = {
    FunctionNode(f, tag, ThreadLocalRandom.current().nextLong)
  }
}