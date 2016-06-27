package traceable.Core

import java.util.concurrent.ThreadLocalRandom

/**
  * Created by artemkazakov on 6/14/16.
  */
case class Traceable[A](id:Long, value:A)

object Traceable {
  def apply[A](value:A) = new Traceable(ThreadLocalRandom.current().nextLong, value)
}