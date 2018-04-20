package syncy

import akka.actor._
import com.typesafe.config._
import scala.io._

object ServerMain {
    // -- Main method when server is instanciated
    def apply() = {
        println(" --> Initialized as Server instance")

        // -- Load config
        val config = ConfigFactory.load("config_server.conf")

        // -- Create actor system for server
        val actorSystem = ActorSystem("server", config)
        val mainActor = actorSystem.actorOf(Props[ServerActor], "main")
        
    }
}