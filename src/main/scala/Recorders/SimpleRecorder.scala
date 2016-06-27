package Recorders

import traceable.Core.{Traceable, Transition}

/**
  * Created by artemkazakov on 6/13/16.
  */
trait SimpleRecorder {
  def printNode[A](tr:Traceable[A], isBox:Boolean = false) = {
    val shape = if (isBox) "box" else "ellipse"
    println(s"${tr.id}[shape=$shape,label=${tr.value}];")
  }

  def printEdge(tr:Transition) = {
    println(s"""${tr.to}[label="..."];""")
    println(s"""${tr.from} -> ${tr.to}[label="${tr.tag}"]""")
  }

  def recordTransition(tr:Transition): Unit = {
    printEdge(tr)
  }

  def record[A](seq:Seq[Traceable[A]], isBox:Boolean = false): Unit = {
    seq.foreach{ x=> printNode(x,isBox) }
  }
}
