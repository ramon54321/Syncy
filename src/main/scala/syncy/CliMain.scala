package syncy

import akka.actor._
import com.typesafe.config._
import scala.io._
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object CliMain {
    // -- Main method when cli is instanciated
    def apply() = {
        println(" --> Initialized as CLI instance")

        // -- Load config
        val config = ConfigFactory.load("config_cli.conf")

        // -- Create actor system for cli
        val actorSystem = ActorSystem("cli", config)
        val mainActor = actorSystem.actorOf(Props[CliActor], "main")

        // -- Connected server
        var serverActor : ActorRef = null

        // -- Input state
        var inputState = InputState.Disconnected
        
        // -- Start input loop
        var takeInput = true;
        while (takeInput) {
            // -- Get input
            val input = StdIn.readLine()
            
            // -- Output buffer
            var output = ""

            // -- Process input
            // -- Match regardless of state
            input match {
                case "quit" | "q" => {
                        output += "Quiting CLI"
                        actorSystem.terminate();
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
                            for (actor <- actorSystem.actorSelection(
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
                    }
                }
            }

            // -- Show output
            if (output != "") println(s" --> ${output}")
        }
    }
}

object InputState extends Enumeration {
    type InputState = Value
    val Disconnected, Base = Value
}