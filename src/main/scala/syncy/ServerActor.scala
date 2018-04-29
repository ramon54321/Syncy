package syncy

import akka.actor._
import scala.collection.mutable._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

class ServerActor extends Actor {

    var data : HashMap[String, String] = new HashMap()

    override def preStart(): Unit = {

    }

    override def receive: Receive = {
        case string : String => println("Received message: " + string)
        case command : Command => handleCommand(command)
        case _ => 
    }

    def handleCommand(command : Command) {
        command match {
            // -- Add value to data
            case Command("add", value : String) => {
                println("Adding value")
                val key = value.split("|")(0)
                val valu = value.split("|")(1)
                data.put(key, valu)
            }
            // -- Remove value from data
            case Command("remove", value : String) => {
                println("Removing value")
                val key = value
                data.remove(key)
            }
            // -- Return a list of each key value pair in this server's data
            case Command("listdata", value : String) => {
                println("Returning data")
                sender ! "Data is blablabla"
            }
        }
    }
}