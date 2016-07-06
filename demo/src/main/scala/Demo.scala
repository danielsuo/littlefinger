package com.danielsuo

import scala.collection.mutable.ArrayBuffer

import qyburn.common._
import qyburn.scheduler._

object Demo extends App {
  val blah: Task = new DemoTask()

  val scheduler = new Scheduler(new DemoSchedulerPolicy())

  // TODO: Handle not enough resources better than this garbage
  Thread.sleep(5000)

  scheduler.submit(Array(blah))
}

class DemoTask extends Task {
  val input = 100
  type ResultType = DemoTaskResult

  def descriptor = new TaskDescriptor()
  def jarPath = "../demo/target/demo-1.0-SNAPSHOT.jar"
  def classNames = Array("com.danielsuo.DemoTask", "com.danielsuo.DemoTaskResult")

  def run(): ResultType = {
    val result = new DemoTaskResult(input * input)
    println(result.result)
    result
  }
}

class DemoTaskResult(val result: Int) extends TaskResult

class DemoSchedulerPolicy extends SchedulerPolicy {
  def schedule(tasks: Array[Task], slots: ArrayBuffer[Slot]): Array[TaskAssignment] = {
    Array(new TaskAssignment(tasks(0), slots(0)))
  }
}
