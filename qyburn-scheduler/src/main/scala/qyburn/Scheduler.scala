package qyburn

import akka.actor._
import akka.remote._

/**
 * @author ${user.name}
 */
object Scheduler extends App {
  implicit val system = ActorSystem("Scheduler")
  val localActor = system.actorOf(Props[LocalActor], name = "LocalActor")
  val blah = new Herro()
  localActor ! "START"
}

class LocalActor extends Actor {
  val remote = context.actorSelection("akka.tcp://Worker@127.0.0.1:5150/user/RemoteActor")
  var counter = 0

  val address = Address("akka.tcp", "Worker", "127.0.0.1", 5150)

  def receive = {
    case "START" =>
      remote ! "Hello from the LocalActor"
    case msg: String =>
      println(s"LocalActor received message: '$msg'")
      if (counter < 5) {
        sender ! "Hello back to you"
        counter += 1
      }
  }
}

class CleganeActor extends Actor {
  println("I was created!")

  def receive = {
    case msg: String =>
      println(s"CleganeActor received message: '$msg'")
  }
}
