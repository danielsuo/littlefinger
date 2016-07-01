package qyburn

import akka.actor._

/**
 * @author ${user.name}
 */
object Worker extends App {
  val system = ActorSystem("Worker")
  val remoteActor = system.actorOf(Props[RemoteActor], name = "RemoteActor")
  remoteActor ! "RemoteActor is alive!"


  def urlses(cl: ClassLoader): Array[java.net.URL] = cl match {
    case null => Array()
    case u: java.net.URLClassLoader => u.getURLs() ++ urlses(cl.getParent)
    case _ => urlses(cl.getParent)
  }

  val  urls = urlses(getClass.getClassLoader)
  println(urls.filterNot(_.toString.contains("ivy")).mkString("\n"))

  // TODO: Abstract this out https://github.com/earldouglas/akka-remote-class-loading
  def getActorFromJar(jar: String, actor: String): ActorRef = {
    import java.io._
    val loader = new java.net.URLClassLoader(Array(new File(jar).toURI.toURL), this.getClass.getClassLoader)
    val clazz = loader.loadClass(actor)
    system.actorOf(Props(clazz.asInstanceOf[Class[Actor]]))
  }
}

class RemoteActor extends Actor {
  def receive = {
    case msg: String =>
      println(s"RemoteActor received message '$msg'")
      val clegane = Worker.getActorFromJar("../qyburn-scheduler/target/qyburn-scheduler-1.0-SNAPSHOT.jar", "qyburn.CleganeActor")
      sender ! "Hello from the RemoteActor"
  }
}
