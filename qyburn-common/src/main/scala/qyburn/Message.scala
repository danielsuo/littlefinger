package qyburn.common

import akka.actor._

// TODO: consider access to these messages (e.g., private[qyburn])
sealed trait TaskMessage

// TODO: Eventually, use Akka Typed to get static checking for what messages an
// actor will process
// TODO: We can get rid of a bunch of these
case class TaskListMessage(tasks: List[Task]) extends TaskMessage
case class TaskStartMessage(task: Task) extends TaskMessage
// case class TaskDescriptorMessage(descriptor: TaskDescriptor) extends TaskMessage
case class TaskAssignmentMessage(assignments: List[TaskAssignment]) extends TaskMessage
// case class TaskStatusMessage(status: TaskStatus) extends TaskMessage
// case class TaskResultMessage(result: TaskResult) extends TaskMessage
case class TaskCompleteMessage(result: TaskResult) extends TaskMessage

sealed trait SlotMessage

case class SlotRegisterMessage(slot: Slot) extends SlotMessage
case class SlotRegisteredMessage(schedulerActorRef: ActorRef) extends SlotMessage
case class SlotUnregisterMessage() extends SlotMessage
case class SlotHeartbeatMessage() extends SlotMessage
