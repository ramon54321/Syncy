package syncy

import akka.actor._
import com.typesafe.config._
import scala.io._

object ServerMain {
    // -- Main method when server is instanciated
    def apply(port : String) = {
        println(" --> Initialized as Server instance")

        // -- Load config
        // val portOverride = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
        // val config = portOverride.withFallback(ConfigFactory.load("config_server.conf"))

        val config = ConfigFactory.parseString(
            s"""
            akka {
                actor {
                    provider = "akka.remote.RemoteActorRefProvider"
                }
                remote {
                    enabled-transports = ["akka.remote.netty.tcp"]
                    netty.tcp {
                        hostname = "127.0.0.1"
                        port = $port
                    }
                }
                loglevel = "OFF"
            }
            """
        )

        // -- Create actor system for server
        val actorSystem = ActorSystem("server", config)
        val mainActor = actorSystem.actorOf(Props[ServerActor], "main")
        
    }
}
