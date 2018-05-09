package syncy

import scala.collection.immutable._
import scala.util.Random

object State {
    //var nextId : Int = 0;
    val random = new Random()

    def getNextId() : Int = {
        //nextId = nextId + 1
        return random.nextInt(1000000)
    }

    val initialState = new State(new ListSet(), new ListSet(), 0)

    def getInitialState() : State = {
        return initialState
    }
}

case class State (val changes : ListSet[String],
    val parentState : ListSet[State], val id : Int = State.getNextId())