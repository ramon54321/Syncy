package syncy

import akka.actor._
import com.typesafe.config._
import scala.io._

object ServerMain {
    // -- Main method when server is instanciated
    def apply(port : String) = {
        println(" --> Initialized as Server instance")

        // -- Load config
        val portOverride = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
        val config = portOverride.withFallback(ConfigFactory.load("config_server.conf"))

        // -- Create actor system for server
        val actorSystem = ActorSystem("server", config)
        val mainActor = actorSystem.actorOf(Props[ServerActor], "main")
        
    }
}