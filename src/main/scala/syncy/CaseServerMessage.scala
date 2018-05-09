package syncy

case class ServerMessage(message : String, changes : List[String])

case class StateMessage(state : State)