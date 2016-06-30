package traceable.core

import Recorders.ITransitionRecorder
import cats.Monad


trait TraceableCoreOps extends ITransitionRecorder {

  implicit object TraceableIsMonad extends Monad[Traceable] {

    override def pure[Z](x: Z): Traceable[Z] = {

      val result = Traceable(x)
      //recordNode(result)
      result

    }

    override def flatMap[A, B](fa: Traceable[A])(f: (A) => Traceable[B]): Traceable[B] = {

      val result = f(fa.value)

      f match {
        case traceFun @GraphEdgeFunction(_, tag) =>
          println(tag)
          recordTransition(Transition(fa.id, result.id, tag))
        case _ => recordTransition(Transition(fa.id, result.id, ""))
      }

      result
    }


    override def map[A, B](fa: Traceable[A])(f: (A) => B): Traceable[B] = {

      val result = pure(f(fa.value))

      f match {
        case GraphEdgeFunction(_, tag) => recordTransition(Transition(fa.id, result.id, tag))
        case _ => recordTransition(Transition(fa.id, result.id, ""))
      }

      result
    }
//
  }
}
