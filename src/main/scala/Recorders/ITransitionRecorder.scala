package Recorders

import traceable.core.{Transition, Traceable}

trait ITransitionRecorder {

  def recordNode[A](in: Traceable[A]): Unit

  def recordTransition(in: Transition): Unit

  def recordTransition[T, TT](from: Traceable[T], to: Traceable[TT], tag: String = ""): Unit = {

    recordTransition(Transition(from.id, to.id, tag))

  }

  //TODO: record metadata for node

}
