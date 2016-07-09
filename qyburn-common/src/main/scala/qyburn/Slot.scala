package qyburn.common

import akka.actor._

// TODO: Set up different serializer
// TODO: Understand why Scala requires we initialize instance variables unless we have constructor
class Slot extends Serializable {
  var ref: ActorRef = _
}
