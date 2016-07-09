package qyburn.master

// TODO: Is there a better collection to use to maintain slots?
import scala.collection.mutable.ArrayBuffer
// import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{ Success, Failure }
import akka.util.Timeout
import akka.pattern.{ ask, pipe }


import akka.actor._
import akka.remote._

import qyburn.common._

// TODO: Planner that turns user code into execution plan
// TODO: Allocator that scales up and down resources
// TODO: Move policy out of constructor
class Master(policy: SchedulerPolicy) {

  implicit val actorSystem = ActorSystem("Master")
  val schedulerActor = actorSystem.actorOf(Props(classOf[SchedulerActor], policy), "SchedulerActor")

  // TODO: add timeout to submit, or user implicit value
  def submit(tasks: List[Task]): Future[List[TaskResult]] = {
    implicit val timeout = Timeout(6 seconds)
    val future: Future[List[TaskResult]] = ask(schedulerActor, new TaskListMessage(tasks))
      .mapTo[List[TaskResult]]
    future
  }
}

class SchedulerActor(policy: SchedulerPolicy) extends Actor {
  import context.dispatcher

  // TODO: figure out way to deal with timeouts
  implicit val timeout = Timeout(5 seconds)
  var slots = new ArrayBuffer[Slot]()

  def receive = {
    case TaskListMessage(tasks: List[Task]) => {
      val assignments = policy.schedule(tasks, slots)

      val future: Future[List[TaskResult]] = ask(self, new TaskAssignmentMessage(assignments))
        .mapTo[List[TaskResult]]

      future pipeTo sender
    }
    case TaskAssignmentMessage(assignments: List[TaskAssignment]) => {
      println("Task assignments complete. Sending tasks to slots")

      val taskFutures: List[Future[TaskResult]] = assignments.map { assignment =>
        val taskFuture: Future[TaskResult] = ask(assignment.slot.ref, new TaskStartMessage(assignment.task)).mapTo[TaskResult]

        taskFuture
      }

      Future.sequence(taskFutures) pipeTo sender
    }

    case SlotRegisterMessage(slot: Slot) => {
      println("Slot registered")

      // TODO: check for existing slot; whatever for now
      // TODO: May want to create Slots container class
      slots += slot
      sender() ! SlotRegisteredMessage(self)
    }

    case msg: String => {
      println(msg)
    }
  }
}

trait SchedulerPolicy {
  def schedule(tasks: List[Task], slots: ArrayBuffer[Slot]): List[TaskAssignment]
}
