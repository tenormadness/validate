package traceable.Core

/**
  * Created by artemkazakov on 6/14/16.
  */
trait Funktor[A, B] {
  def name:String
  def apply(a:A):B
}
