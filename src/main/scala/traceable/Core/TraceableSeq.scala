package traceable.Core

import Recorders.SimpleRecorder

/**
  * Created by artemkazakov on 6/14/16.
  */
object TraceableSeq {
  def apply[A](seq:Seq[A]) = {
    new TraceableSeq(seq.map(Traceable(_)))
  }
}

class TraceableSeq[A](val seq:Seq[Traceable[A]]) extends traceableCollection[A] with SimpleRecorder {
  def map[B](f: Funktor[A,B]) = {
    new TraceableSeq[B](seq.map{ a =>
      val b = Traceable(f(a.value))
      recordTransition(Transition(a.id, b.id, f.name))
      b
    })
  }

  def zip(other:traceableCollection[A], f:Funktor2[A,A]):traceableCollection[A] = {
    new TraceableSeq[A](
      seq.zip(other.seq).map{x =>
        val b = Traceable(f(x._1.value, x._2.value))
        recordTransition(Transition(x._1.id, b.id, f.name))
        recordTransition(Transition(x._2.id, b.id, f.name))
        b })
  }

  def filter(f: Funktor[A,Boolean]) = {
    val filteredOut = new TraceableSeq[A](seq.filterNot(x => f(x.value)))
    filteredOut.seq.foreach(x => recordTransition(Transition(x.id, x.id, s"Not:${f.name}")))
    val filtered = new TraceableSeq[A](seq.filter(x => f(x.value)))
    filtered.seq.foreach(x => recordTransition(Transition(x.id, x.id, f.name)))
    filtered
  }

  def reduce[B](f:Funktor2[A, B], initialValue:B):TraceableSeq[B] = {
    val res = Traceable(initialValue)
    val b = seq.foldLeft[B](initialValue)((sum, x) =>
      {
        recordTransition(Transition(x.id, res.id, f.name))
        f(sum, x.value)
      })
    new TraceableSeq[B](Seq(Traceable(res.id, b)))
  }

  def recordNodes:TraceableSeq[A] = {
    record(seq, false)
    this
  }

  def recordNodes(box:Boolean = false):TraceableSeq[A] = {
    record(seq,box)
    this
  }
}