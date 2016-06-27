package traceable.Core

/**
  * Created by artemkazakov on 6/14/16.
  */
trait traceableCollection[A] {
  def map[B](f:Funktor[A,B]) : traceableCollection[B]
  def filter(f:Funktor[A, Boolean]) : traceableCollection[A]
  def reduce[B](f:Funktor2[A, B], initialValue:B) : traceableCollection[B]
  def zip(other:traceableCollection[A], f:Funktor2[A,A]):traceableCollection[A]
  def recordTransition(tr:Transition): Unit
  def recordNodes(box:Boolean = false):traceableCollection[A]
  def seq : Iterable[Traceable[A]]
}
