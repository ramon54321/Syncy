package syncy

import akka.actor._
import scala.io._
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class CliActor extends Actor {

    // -- Connected server
    Future {
        var serverActor : ActorRef = null

        // -- Input state
        var inputState = InputState.Disconnected
        
        // -- Start input loop
        var takeInput = true;
        while (takeInput) {
            // -- Get input
            val input = StdIn.readLine()

            // -- Process input
            // -- Match regardless of state
            input match {
                case "quit" | "q" => {
                        CliMain.actorSystem.terminate();
                        takeInput = false;
                    }
                case _ =>
            }

            // -- Match depending on state
            inputState match {
                case InputState.Disconnected => {
                    input match {
                        case "connect" => {
                            println("Connecting ...");
                            for (actor <- context.actorSelection(
                                "akka.tcp://server@127.0.0.1:5150/user/main"
                                ).resolveOne(5.seconds)) {
                                serverActor = actor
                                serverActor ! "Hello from cli"
                                inputState = InputState.Base
                            }
                        }
                        case _ =>
                    }
                }
                case InputState.Base => {
                    if (input.startsWith("add")) {
                        serverActor ! Command("add", "bob|25")
                    } else if (input.startsWith("remove")) {
                        serverActor ! Command("remove", "bob")
                    } else if (input.startsWith("status")) {
                        serverActor ! Command("status", "")
                    } else if (input.startsWith("commit")) {
                        serverActor ! Command("commit", "")
                    }
                }
            }
        }
    }

    override def receive: Receive = {
        case s : String => println(s)
        case _ => 
    }
}