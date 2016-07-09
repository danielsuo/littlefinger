package qyburn.benchmark

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Success, Failure }

import qyburn.common._
import qyburn.master._

object Main extends App {
  println("Hello, world!")
  // val blah: Task = new DemoTask()
  //
  // val master = new Master(new DemoSchedulerPolicy())
  //
  // // TODO: Handle not enough resources better than this garbage
  // Thread.sleep(5000)
  //
  // master.submit(List(blah))
  //   .mapTo[List[DemoTaskResult]]
  //   .onComplete {
  //     case Success(result) => {
  //       result.foreach(taskResult => {
  //         println(taskResult.result)
  //       })
  //     }
  //     case Failure(t) => {
  //       t.printStackTrace()
  //     }
  //   }
}

class DemoTask extends Task {
  val input = 100

  def descriptor = new TaskDescriptor()
  def jarPath = "../demo/target/demo-1.0-SNAPSHOT.jar"
  def classNames = Array("com.danielsuo.DemoTask", "com.danielsuo.DemoTaskResult")

  def run(): DemoTaskResult = {
    val result = new DemoTaskResult(input * input)
    println(result.result)
    println("Waiting...")
    Thread.sleep(1000)
    println("Done waiting!")
    result
  }
}

class DemoTaskResult(val result: Int) extends TaskResult

class DemoSchedulerPolicy extends SchedulerPolicy {
  def schedule(tasks: List[Task], slots: ArrayBuffer[Slot]): List[TaskAssignment] = {
    List(new TaskAssignment(tasks(0), slots(0)))
  }
}
