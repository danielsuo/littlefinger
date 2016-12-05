package qyburn.benchmark

import qyburn.common._
import qyburn.master._

abstract class Benchmark {
  def run(): Unit

  def schedulerPolicy: SchedulerPolicy
}
