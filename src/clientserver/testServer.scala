package clientserver

object testServer {

  def main(args: Array[String]) {
	  var serverThread = new Thread(new Runnable() {
	  @Override
	  def run() = {
	    var serv = new Server
	    serv.start
	  }
	})
	var clientThread = new Thread(new Runnable() {
	  @Override
	  def run() =  {
	    var btc = 0.35
	    var change = 0.01
	    var client = new Client(0.35, 0.01, "adsada")
	  }
	})
	serverThread.start
	clientThread.start
  }
}