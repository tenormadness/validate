package traceable.Core

/**
  * Created by artemkazakov on 6/14/16.
  */
trait Funktor2[A, B] {
  def name:String
  def apply(b:B, a:A):B
}
