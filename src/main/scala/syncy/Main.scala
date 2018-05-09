package syncy

object Main {
    def main(args : Array[String]) : Unit = {
        if (args.length == 1 && args(0).toLowerCase() == "cli") {
            CliMain()
        } else if (args.length == 2 && args(0).toLowerCase() == "server") {
            ServerMain(args(1)) // -- Start server at port
        } else {
            print(
            """Usage:
              |syncy server <port>
              |syncy cli
              |""".stripMargin
            )
        }
    }
}

