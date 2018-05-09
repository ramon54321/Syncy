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
            var input = ""
            blocking {
                input = StdIn.readLine()
            }

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
                    if (input.startsWith("connect")) {

                        // 5150 or 5151
                        val port = input.split(" ")(1)

                        println("Connecting to " + port);
                        for (actor <- context.actorSelection(
                            "akka.tcp://server@127.0.0.1:" + port + "/user/main"
                            ).resolveOne(5.seconds)) {
                            serverActor = actor
                            serverActor ! "Hello from cli"
                            inputState = InputState.Base
                            println("Connected")
                        }
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
                    } else if (input.startsWith("disconnect")) {
                        serverActor = null
                        inputState = InputState.Disconnected
                    } else if (input.startsWith("merge")) {
                        serverActor ! Command("merge", "5151")
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
