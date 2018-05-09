package syncy

case class ServerMessage(message : String)

case class StateMessage(state : State)
case class ApplyMessage(initialState : State, commonState : State)