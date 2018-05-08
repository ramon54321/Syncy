package syncy

import scala.collection.immutable._

object State {
    var nextId : Int = 0;

    def getNextId() : Int = {
        nextId = nextId + 1
        return nextId
    }

    val initialState = new State(new ListSet(), new ListSet())

    def getInitialState() : State = {
        return initialState
    }
}

class State (val changes : ListSet[String], val parentState : ListSet[State]) {
    val id = State.getNextId()
}