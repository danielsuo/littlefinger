package qyburn.scheduler

// TODO: Is there a better collection to use to maintain slots?
import scala.collection.mutable.ArrayBuffer

import akka.actor._
import akka.remote._

import qyburn.common._

// TODO: Planner that turns user code into execution plan
// TODO: Allocator that scales up and down resources
class Scheduler(policy: SchedulerPolicy) {

  implicit val actorSystem = ActorSystem("Scheduler")
  val schedulerActor = actorSystem.actorOf(Props(classOf[SchedulerActor], policy), "SchedulerActor")

  def submit(tasks: Array[Task]): TaskResult = {
    val future = schedulerActor ? new TaskArrayMessage(tasks)
    val result = Await.result(future, Timeout(5 seconds).duration)
  }
}

class SchedulerActor(policy: SchedulerPolicy) extends Actor {
  var slots = new ArrayBuffer[Slot]()

  def receive = {
    case TaskArrayMessage(tasks: Array[Task]) => {
      val assignments = policy.schedule(tasks, slots)

      self ! new TaskAssignmentMessage(assignments)
    }
    case TaskAssignmentMessage(assignments: Array[TaskAssignment]) => {
      println("Task assignments complete. Sending tasks to slots")
      assignments.foreach(assignment => {
        assignment.slot.ref ! new TaskStartMessage(assignment.task)
      })
    }

    case SlotRegisterMessage(slot: Slot) => {
      println("Slot registered")

      // TODO: check for existing slot; whatever for now
      // TODO: May want to create Slots container class
      slots += slot
      sender() ! SlotRegisteredMessage(self)
    }

    // TODO: How to preserve sub type
    case result: TaskResult => {
      println("Received result")
      println(result)
    }
  }
}

trait SchedulerPolicy {
  def schedule(tasks: Array[Task], slots: ArrayBuffer[Slot]): Array[TaskAssignment]
}
