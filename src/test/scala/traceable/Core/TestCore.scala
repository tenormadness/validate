package traceable.core


object TestCore extends App {
  import cats.syntax.all._
  //import cats.all
  import cats.std.list._

  import traceable.instances.ConsoleTraceable._

  // one plus one is ... ?

  val one = Traceable(1, 1111111)
  recordNode(one)

  val twoRes = one map (_ + 1); //recordNode(twoRes)

  println(s"1 + 1 ===> $twoRes")
  println("\n\n")

  // what about one plus one and they are both Traceable???

  recordNode((one |@| one) map (_ + _)) // the syntax is not even ugly... I have to squish in a builder
  println("\n\n")

  // I have list of Val and val of list
  val foo = List(Traceable(1), Traceable(2),  Traceable(3))//.sequence
  foo.foreach(recordNode)
  val fooo = foo.map(_ |@| one)
      .map(_ map (_ + _))
  println(s"(v(1), v(2), v(3)) + 1 ==> $fooo")
  println("\n\n")


  val bar = Traceable(List(1, 2, 3))
  val bazz = (bar |@| one) map { (list, n) => list.map(_ + n)}
  println(s"(1, 2, 3) + 1 ==> $bazz")
  println("\n\n")

  // what about foo??? (this is a bit uglier)


  // there are also a quantity of extra goodies... to change between foo and bar, even though they are less intuitive

  val bazzV2 = implicitly[cats.Monad[Traceable]].sequence(foo)
  println(s"sequenced List(T(1), T(2), T(2)) \n $bazzV2") // this is a bit of messy graph because I build many tuples and Lists
  println("\n\n")

  val filtered = bar map (_.filter(_ < 3))
  println(s"filtered  = $filtered")

}
