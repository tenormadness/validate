package traceable.core

import Recorders.ITransitionRecorder
import cats.Monad


trait TraceableCoreOps extends ITransitionRecorder {

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
}
