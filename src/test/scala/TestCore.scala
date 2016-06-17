package Validation

object TestCore extends App {
  import cats.syntax.all._
  //import cats.all
  import cats.std.list._

  // one plus one is ... ?

  val one = Validate(1)

  println(s"1 + 1 ===> ${one map (_ + 1)}")

  // what about one plus one and they are both validated???

  (one |@| one) map (_ + _) // the syntax is not even ugly... I have to squish in a builder


  // I have list of Val and val of list
  val foo = List(Validate(1), Validate(2),  Validate(3))//.sequence
  val bar = Validate(List(1, 2, 3))

  // How do I sum one to these? bar is easy

  val bazz = (bar |@| one) map { (list, n) => list.map(_ + n)}
  println(s"(1, 2, 3) + 1 ==> $bazz")

  // what about foo??? (this is a bit uglier)

  val fooo = foo.map(_ |@| one)
                .map(_ map (_ + _))
  println(s"(v(1), v(2), v(3)) + 1 ==> $fooo")

  // there are also a quantity of extra goodies... to change between foo and bar, even though they are less intuitive

  val bazzV2 = implicitly[cats.Applicative[Validation]].sequence(fooo)
  println(s"here it comes again! \n $bazzV2")


  val filtered = bar map (_.filter(_ < 3))
  println(s"filtered  = $filtered")

}
