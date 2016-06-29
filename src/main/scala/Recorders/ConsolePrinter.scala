package Recorders

import traceable.core.{Traceable, Transition}

/**
 * Created by lucatosatto on 6/28/16.
 */
trait ConsolePrinter extends ITransitionRecorder {

  override def recordNode[A](in: Traceable[A]): Unit = {

    println(s"node: ${in.id}  => ${in.value}")
  }

  override def recordTransition(in: Transition): Unit = {

    println(s"transition: ${in.from}  -> ${in.to}   ::  ${in.tag}")

  }
}
