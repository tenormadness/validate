package traceable.core

import java.util.concurrent.ThreadLocalRandom

import Recorders.ITransitionRecorder
import cats.{Monad, Applicative}

sealed abstract class CanBeTraced[T] {
  def id: Long
  def value: T
}

case class Traceable[T](value: T, id: Long) extends CanBeTraced[T]
//case class TraceableFunction[T](id: Long, value: T, descriptor: String) extends CanBeTraced[T]

object Traceable {
  def apply[T](value: T) = new Traceable(value, ThreadLocalRandom.current().nextLong)
}
//object TraceableFunction {  TODO:
//  def apply[T](value: T) = new Traceable(ThreadLocalRandom.current().nextLong, value)
//}

trait TraceableOps extends ITransitionRecorder {

  implicit object TraceableIsMonad extends Monad[Traceable] {

    override def pure[A](x: A): Traceable[A] = {

      val result = Traceable(x)
      //recordNode(result)
      result

    }

    override def flatMap[A, B](fa: Traceable[A])(f: (A) => Traceable[B]): Traceable[B] = {

      val result = f(fa.value)
      recordTransition(Transition(fa.id, result.id, ""))
      result
    }
  }

//  implicit class TracedApply[T](in: Traceable[T]) = {
//
//    def flatMapTrace(f: (A) => Traceable[B]): Traceable[B] = {
//
//
//  }

}


//    override def ap[A, B](ff: Traceable[(A) => B])(fa: Traceable[A]): Traceable[B] = {
//
//      val result = Traceable(ff.value(fa.value))
//      recordTransition(Transition(fa.id, result.id, "data"))
//      recordTransition(Transition(ff.id, result.id, "function"))
//      result
//
//    }
//
//    override def product[A, B](fa: Traceable[A], fb: Traceable[B]): Traceable[(A, B)] = {
//
//      val result = Traceable((fa.value, fb.value))
//      result
//
//    }

//override def map[A, B](fa: Traceable[A])(f: (A) => B): Traceable[B] = ???


