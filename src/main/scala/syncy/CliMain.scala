package syncy

import akka.actor._
import com.typesafe.config._

object CliMain {
    var actorSystem : ActorSystem = null

    // -- Main method when cli is instanciated
    def apply() = {
        println(" --> Initialized as CLI instance")

        // -- Load config
        val config = ConfigFactory.load("config_cli.conf")

        // -- Create actor system for cli
        actorSystem = ActorSystem("cli", config)
        val mainActor = actorSystem.actorOf(Props[CliActor], "main")
    }
}

object InputState extends Enumeration {
    type InputState = Value
    val Disconnected, Base = Value
}
