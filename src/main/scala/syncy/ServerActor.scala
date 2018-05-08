package syncy

import akka.actor._
import scala.collection.mutable.HashMap
import scala.collection.immutable.ListSet
import scala.concurrent._
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

    def handleServerMessage(servermsg : ServerMessage) {
        
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
        }
    }
}