package traceable.core

import java.util.concurrent.ThreadLocalRandom

import Recorders.ITransitionRecorder

sealed abstract class Trace[T] extends {

  def id: Long
  def value: T

  def flatMapTrace[TT](tag: String)(f:  T => Traceable[TT])(implicit recorder: ITransitionRecorder): Traceable[TT] = {

    recorder.flatMapTrace(this)(tag)(f)

  }

  def mapTrace[TT](tag: String)(f:  T => TT)(implicit transitionRecorder: ITransitionRecorder): Traceable[TT] = {

    val id = getId()
    val function: T => Traceable[TT] = in => Traceable(f(in), id)
    flatMapTrace(tag)(function)
  }

  protected def getId(): Long = ThreadLocalRandom.current().nextLong

}

/** A value that is traced, also a node in the graph */
final case class Traceable[T](value: T, id: Long) extends Trace[T] {

  def |*|[TT](that: Traceable[TT]) = TraceBuilder2(this.value, that.value, getId())

}

object Traceable {

  def apply[T](value: T): Traceable[T] = Traceable(value, ThreadLocalRandom.current().nextLong)

}


// The classes below are the traced equivalent ot a cartesian builder, currently they are built only for 2, 3 and 4 arguments,
// more should be added in needed unless we use shapeless

final case class TraceBuilder2[T1, T2](v1: T1, v2: T2, id: Long) extends Trace[(T1, T2)] {

  def |*|[TT](that: Traceable[TT]) = TraceBuilder3(this.value, that.value, this.id)
  def value: (T1, T2) = (v1, v2)

}

final case class TraceBuilder3[T1, T2, T3](v1: (T1, T2), v2: T3, id: Long) extends Trace[(T1, T2, T3)] {

  def |*|[TT](that: Traceable[TT]) = TraceBuilder4(this.value, that.value, this.id)
  def value: (T1, T2, T3) = (v1._1, v1._2, v2)
}

final case class TraceBuilder4[T1, T2, T3, T4](v1: (T1, T2, T3), v2: T4, id: Long) extends Trace[(T1, T2, T3, T4)] {
  override def value: (T1, T2, T3, T4) = (v1._1, v1._2, v1._3, v2)
}

/** A special encoding for a graph node that is a function (not sure we need this, a bit of a dirty thing... but if you want to flatmap...) */

