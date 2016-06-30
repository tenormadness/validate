package traceable.core

import java.util.concurrent.ThreadLocalRandom

import Recorders.ITransitionRecorder
import cats.{Monad, Applicative}


/** trait that encodes a node in the graph */
sealed abstract class CanBeTraced[T] {
  def id: Long
  def value: T
}

/** traced functions are graph edges */
sealed trait ITraceableFunction {

  type In
  type Out

  def tag: String
  def f: In => Out

  override def toString(): String = "Function: " + tag

}

/** A value that is traced, also a node in the graph */
final case class Traceable[T](value: T, id: Long) extends CanBeTraced[T]

/** Enriched functions that can tag graph edges when applied (I hate scala for not extending over arity!)*/
final case class GraphEdgeFunction[In, Out](f: In => Out, tag: String) extends (In => Out) {

  def apply(in: In): Out = f(in)

}
final case class GraphEdgeFunction2[In1, In2, Out](f: (In1, In2) => Out, tag: String) extends ((In1, In2) => Out) {

  def apply(in1: In1, in2: In2): Out = f(in1, in2)

}

/** A special encoding for a graph node that is a function (not sure we need this, a bit of a dirty thing... but if you want to flatmap...) */
//final case class FunctionNode(value: ITraceableFunction, id: Long) extends CanBeTraced[ITraceableFunction] /*with ITraceableFunction {
//  override type In = value.In
//
//  override def tag: String = value.tag
//
//  override def f: (In) => Out = value.f
//
//  override type Out = value.Out
//}
//*/
// Some constructors
object Traceable {
  def apply[T](value: T): Traceable[T] = Traceable(value, ThreadLocalRandom.current().nextLong)
}

//object FunctionNode {
////  def apply[A, B](f: A => B, tag: String): FunctionNode[A, B] = {
////    FunctionNode(GraphEdgeFunction(f, tag), ThreadLocalRandom.current().nextLong)
////  }
//
//  def apply(in: ITraceableFunction): FunctionNode = {
//    FunctionNode(in, ThreadLocalRandom.current().nextLong)
//  }
// }