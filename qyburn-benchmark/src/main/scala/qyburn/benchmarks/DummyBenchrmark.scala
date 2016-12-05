package qyburn.benchmark

import qyburn.common._
import qyburn.master._

object DummyBenchmark extends Benchmark {
  def run(): Unit = {
    val master = new Master(new DummySchedulerPolicy())
    Thread.sleep(5000)
    val i = 1
  }

  def schedulerPolicy = new DummySchedulerPolicy()
}

class DummyTask1 extends Task {
  val N = 10000

  def descriptor = new TaskDescriptor()
  def jarPath = "../demo/target/demo-1.0-SNAPSHOT.jar"
  def classNames = Array("qyburn.benchmark.DummyTask1", "qyburn.benchmark.DummyTaskResult1")

  def isPrime(n: Int): Boolean = {
    if (n <= 1) return false

    (2 to n - 1).foreach(i => {
      if (n % i == 0) return false
    })

    return true
  }

  // Very inefficient implementation to find Nth prime to test CPU
  def run(): DummyTaskResult1 = {
    var numPrimesFound = 0
    var curr = 0
    var currPrime = 0

    while (numPrimesFound < N) {
      if (isPrime(curr)) {
        currPrime = curr
        numPrimesFound += 1
      }
      curr += 1
    }

    val result = new DummyTaskResult1(currPrime)
    result
  }
}

class DummyTaskResult1(val result: Int) extends TaskResult

class DummySchedulerPolicy extends SchedulerPolicy {
  def schedule(tasks: List[Task], slots: List[Slot]): List[TaskAssignment] = {
    List(new TaskAssignment(tasks(0), slots(0)))
  }
}
