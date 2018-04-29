package syncy

import akka.actor._
import scala.collection.mutable._

class ServerActor extends Actor {

    var data : HashMap[String, String] = new HashMap()

    override def preStart(): Unit = {

    }

    override def receive: Receive = {
        case s : String => println("Received message: " + s)
        case Command(comType, comValue) => println("Received command: " + comType + " - " + comValue)
        case _ => 
    }
}