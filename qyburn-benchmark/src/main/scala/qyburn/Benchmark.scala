package qyburn.benchmark

abstract class Benchmark {
  // TODO: profiling level
  def runTasks()
  def collateData()
  def analyzeData()
}
