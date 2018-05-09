package syncy

import akka.actor._
import scala.collection.mutable.HashMap
import scala.collection.immutable.ListSet
import akka.util.Timeout
import scala.util.Random
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class ServerActor extends Actor {

    var state : State = State.getInitialState()
    var changes : ListSet[String] = new ListSet()

    var data : HashMap[String, String] = new HashMap()

    override def preStart(): Unit = {

    }

    override def receive: Receive = {
        case string : String => println("Received message: " + string)
        case command : Command => handleCommand(command)
        case servermsg : ServerMessage => handleServerMessage(servermsg)
        case statemsg : StateMessage => {
                mergeServerState = statemsg.state
                mergeWith("0")
            }
        case _ => 
    }

    // -- Commit current changes into new state
    def commit() {
        if (changes.size == 0) {
            println("Nothing to commit")
            return
        }

        state = new State(changes, ListSet(this.state))
        changes = new ListSet()
        println("Committing")
    }

    // -- Apply states in recursive manner
    def applyState(state : State, commonState : State) {

    }

    def makeChange(key : String, value : String) {
        data.put(key, value)
        changes = changes + (key + "|" + value)
    }

    var mergeStage = 0
    var mergeServer : ActorRef = null
    var mergeServerState : State = null
    def mergeWith(port : String) {
        mergeStage match {
            case 0 => {
                println("Merging with " + port)

                for (actor <- context.actorSelection(
                    "akka.tcp://server@127.0.0.1:" + port + "/user/main"
                    ).resolveOne(5.seconds)) {
                    mergeServer = actor
                    
                    this.commit()
                    mergeServer ! ServerMessage("commit", null)

                    mergeStage = 1
                }
            }
            case 1 => {
                println("Continuing Merge -> Received state from other server")

                // -- Find common ancestor
                println(state.id)
                println(mergeServerState.id)

                val commonState : State = findCommonState(state, mergeServerState)

                if (commonState == null) {
                    println("No common ancestor found")
                    return
                }

                // -- Apply changes

                mergeStage = 2
            }
        }

        
    }

    def findCommonState(state1 : State, state2 : State) : State = {
        for (pState <- state1.parentState) {
            for (p2State <- state2.parentState) {
                if (p2State == pState) return p2State
            }
        }
        return null
    }

    def handleServerMessage(servermsg : ServerMessage) {
        servermsg match {
            // -- Commit
            case ServerMessage("commit", _) => {
                commit()
                println("Sending state message")
                sender ! StateMessage(state)
            }
        }
    }

    def handleCommand(command : Command) {
        command match {
            // -- Commit
            case Command("commit", value : String) => {
                commit()
            }
            // -- Add value to data
            case Command("add", value : String) => {
                println("Adding value")
                val key = (value.split("|"))(0)
                val valu = (value.split("|"))(1)
                makeChange(key, valu)
            }
            // -- Remove value from data
            case Command("remove", value : String) => {
                println("Removing value")
                val key = value
                data.remove(key)
            }
            // -- Return a list of each key value pair in this server's data
            case Command("status", value : String) => {
                println("Returning status")
                sender ! "State: " + state.id + "\nChanges: " +
                    changes.toString() + "\nData: " + data.toString()
            }
            // -- Merge
            case Command("merge", value : String) => {
                println("Merge")
                mergeWith(value)
            }
        }
    }
}