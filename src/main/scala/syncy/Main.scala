package syncy

object Main {
	def main(args : Array[String]) : Unit = {
		if (args.length < 1) {
			return
		} else if (args(0).toLowerCase() == "cli") {
			CliMain()
		} else if (args(0).toLowerCase() == "server") {
			ServerMain()
		}
	}
}
