package qyburn.worker

import akka.actor._
import akka.remote._

import qyburn.common._

object Worker extends App {

  implicit val actorSystem = ActorSystem("Worker")
  val workerActor = actorSystem.actorOf(Props[WorkerActor], "WorkerActor")

  Task.getClassPath().foreach(println);

  workerActor ! "START"
}

class WorkerActor extends Actor {
  // TODO: Workers may have more than one slot
  // TODO: Workers in charge of keeping track of slots vs actual resources
  val slot = new Slot()
  slot.ref = self

  // TODO: Should read this from environment or config file
  // TODO: Get ActorRef instead of ActorSelection
  var schedulerSelection = context.actorSelection("akka.tcp://Scheduler@127.0.0.1:5150/user/SchedulerActor")
  var schedulerActor: ActorRef = _

  def receive = {
    case "START" => {
      println ("Attempting to register")
      schedulerSelection ! new SlotRegisterMessage(slot)
      // TODO: Probably can do better than this. See http://doc.akka.io/docs/akka/snapshot/java/howto.html
      Thread.sleep(1000)
      self ! "START"
    }
    case SlotRegisteredMessage(schedulerActorRef: ActorRef) => {
      println("Worker registered!")
      schedulerActor = schedulerActorRef
      context.become(receiveWhenRegistered)
    }
  }

  def receiveWhenRegistered: Receive = {
    case msg: String => {
      println(msg)
    }

    case TaskStartMessage(task: Task) => {
      schedulerActor ! task.run()
    }
  }
}
