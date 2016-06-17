package Validation

case object Nil extends HistoryTree
final case class Leaf[T](item: T) extends HistoryTree
final case class Node(left: HistoryTree, right: HistoryTree) extends HistoryTree
//final case class Node3(left: HistoryTree, mid: HistoryTree, right: HistoryTree) extends HistoryTree

sealed abstract class HistoryTree extends Product with Serializable {

  override def toString: String = this match {
    case Nil => " * "
    case Leaf(item) => item.toString
    case Node(l, r) => s"($l, $r)"
//    case Node3(desc, l, m, r) => s"($l, $m, $r)"
  }
}

