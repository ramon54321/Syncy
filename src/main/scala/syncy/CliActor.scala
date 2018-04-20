package syncy

import akka.actor._

class CliActor extends Actor {
    override def receive: Receive = {
        case s : String => println(s)
        case _ => 
    }
}