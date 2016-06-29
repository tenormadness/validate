package Recorders

import traceable.core.{Transition, Traceable}

trait ITransitionRecorder {

  def recordNode[A](in: Traceable[A]): Unit

  def recordTransition(in: Transition): Unit

  /** a little helper function that build the transition for you starting from two nodes */
  def recordTransition[T, TT](from: Traceable[T], to: Traceable[TT], tag: String = ""): Unit = {

    recordTransition(Transition(from.id, to.id, tag))

  }

  //TODO: record metadata for node

}
