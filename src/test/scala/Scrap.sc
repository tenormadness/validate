

case class MyFun(f: Int => String, tag: String) extends (Int => String)