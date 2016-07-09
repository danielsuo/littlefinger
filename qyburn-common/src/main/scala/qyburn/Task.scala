package qyburn.common

object Task extends Serializable {
  def loadClass(jar: String, classNames: Array[String]): Unit = {
    val loader = new java.net.URLClassLoader(Array(new java.io.File(jar).toURI.toURL), this.getClass.getClassLoader)

    classNames.foreach(className => {
      loader.loadClass(className)
    })
  }

  def addClassPath(classpath: String): Unit = {
      val url = new java.io.File(classpath).toURI.toURL
      val classLoader = ClassLoader.getSystemClassLoader().asInstanceOf[java.net.URLClassLoader]
      // val urlClass = classOf[java.net.URLClassLoader]
      // val method = urlClass.getDeclaredMethod("addURL", classOf[java.net.URL])
      val method = classLoader.getClass.getMethod("addURL", url.getClass)
      method.setAccessible(true)
      method.invoke(classLoader, url)
  }

  def getClassPathHelper(cl: ClassLoader): Array[java.net.URL] = cl match {
    case null => Array()
    case url: java.net.URLClassLoader => url.getURLs() ++ getClassPathHelper(cl.getParent)
    case _ => getClassPathHelper(cl.getParent)
  }

  def getClassPath(): Array[java.net.URL] = {
    getClassPathHelper(getClass.getClassLoader) ++ getClassPathHelper(ClassLoader.getSystemClassLoader())
  }
}

// TODO: Consider access to these classes (e.g., private[qyburn])
// TODO: Consider whether we want to pass around Task, a pot. large object
trait Task extends Serializable {
  def descriptor: TaskDescriptor
  def jarPath: String
  def classNames: Array[String]
  def run(): TaskResult
}

class TaskDescriptor() extends Serializable {
  var test: Int = 100
}

class TaskAssignment(val task: Task, val slot: Slot)

class TaskStatus extends Serializable {

}

trait TaskResult extends Serializable {

}

// TODO: Remove. Used in DynamicCLassLoader.java example
class Dummy {

}
