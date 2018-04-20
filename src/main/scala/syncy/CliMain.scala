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
        
        // -- Start input loop
        var takeInput = true;
        while (takeInput) {
            // -- Get input
            val input = StdIn.readLine()
            
            // -- Output buffer
            var output = ""

            // -- Process input
            input match {
                case "quit" | "q" => {
                        output += "Quiting CLI"
                        actorSystem.terminate();
                        takeInput = false;
                    }
                case "connect" => {
                        println("Connecting ...");
                        for (actor <- actorSystem.actorSelection(
                            "akka.tcp://server@127.0.0.1:5150/user/main"
                            ).resolveOne(5.seconds)) {
                            actor ! "Hello from cli"
                        }
                    }
                case _ =>
            }

            // -- Show output
            if (output != "") println(s" --> ${output}")
        }
    }
}