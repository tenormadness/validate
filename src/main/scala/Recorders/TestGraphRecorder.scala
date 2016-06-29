package Recorders

import traceable.core.{Traceable, Transition}

import scala.annotation.tailrec
import scala.collection.mutable

/**
 * Holds Graph nodes and transition in memory
 */
trait TestGraphRecorder extends ITransitionRecorder {

  var transitions: mutable.Buffer[Transition] = mutable.Buffer()
  var nodes: mutable.Buffer[Traceable[_]] = mutable.Buffer()

  override def recordNode[A](in: Traceable[A]): Unit =  nodes += in

  override def recordTransition(in: Transition): Unit = transitions += in

  def clearGraph: Unit = {
    transitions = mutable.Buffer()
    nodes = mutable.Buffer()
  }

  def printTransitions: String = {

    val nodesAsMap = nodes.map(node => node.id -> node.value).toMap

    transitions
      .map { case Transition(from, to, tag) =>

      val fromStr = nodesAsMap.getOrElse(from, s"???")
      val toStr   = nodesAsMap.getOrElse(to,   s"???")

      fromStr + s" --[$tag]--> " + toStr

      }

      .fold("")(_ + "\n" + _)

  }
  
  def isConnected(from: Traceable[_], to: Traceable[_]): Boolean = {

    val graphMap = transitions
      .groupBy(_.from)
      .mapValues(_ map(_.to))
      .mapValues(_.toSeq)

    //TODO: perhaps add graph search functionality

    graphMap.get(from.id).fold(false)(_.toSet contains to.id)

  }

}
