package qyburn.master

import akka.actor._
import akka.remote._

import qyburn.common._

/**
 * @author ${user.name}
 */

// TODO: If we're using as a library, we shouldn't need a main, but using to
// test for now
object Main extends App {
  implicit val system = ActorSystem("Master")
  val localActor = system.actorOf(Props[LocalActor], name = "LocalActor")
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
