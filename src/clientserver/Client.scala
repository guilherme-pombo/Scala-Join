package clientserver
import java.net._
import java.io._
import scala.io._
import java.util.concurrent.TimeUnit

class Client (_btcTotal : Double, _change : Double, _hexTx : String){
  
  val btcTotal = _btcTotal //amount of Bitcoin that the user is willing to mix
  val change = _change //amount of change user is willing to give away
  val hexTx = _hexTx //the hexadecimal representation of the transaction to be mixed
  var mergedUnsigned = "" //store the client's merged unsigned transaction
  
  //Initialize variables needed for communication
  val s = new Socket(InetAddress.getByName("localhost"), 9999)
  lazy val in = new BufferedSource(s.getInputStream()).getLines()
  val out = new PrintStream(s.getOutputStream())
	

  def sendMainInfo{
  	out.println("FirstbtcTotal:" + btcTotal + "-change:" + change + "-Tx:" + hexTx )
  	out.flush
  	var str = in.next
  	//HAVE TO WAIT BEFORE MERGING IS DONE
  	if(str.substring(0, 5).equals("Wait")){
  	  var time2Wait = in.next.split(":")(1).toInt
  	  println("Received: " + time2Wait)
  	  try {
  		  TimeUnit.MINUTES.sleep(time2Wait);
  	  } catch{
		  case e: InterruptedException  => println ("Huh?")
	  }
      //After waiting, check if merged unsigned transaction has been done again
      requestUnsigned
  	}
  	//MERGING HAS BEEN DONE
  	else{
  	  mergedUnsigned = in.next.split(":")(1)
  	  //Move on to Next step
  	}
  }
  
  def requestUnsigned{
    out.println("ReqUnsigned" )
  	out.flush
  	var str = in.next
  	//HAVE TO WAIT BEFORE MERGING IS DONE
  	if(str.substring(0, 5).equals("Wait")){
  	  var time2Wait = in.next.split(":")(1).toInt
  	  println("Received: " + time2Wait)
  	  try {
  		  TimeUnit.MINUTES.sleep(100);
  	  } catch{
		  case e: InterruptedException  => println ("Huh?")
	  }
      //After waiting, check if merged unsigned transaction has been done again
      requestUnsigned
  	}
  	else{
  	  mergedUnsigned = in.next.split(":")(1)
  	  //Move on to Next step
  	}
  }
  
  def sendSignedTx(signedTx: String){
    out.println("signedTx:" + signedTx)
  	out.flush
  	var str = in.next
  	if(str.equals("error")){
  	  println("You can't be doing that yet. You haven't requested Mixing yet")
  	}
    if(str.equals("Success1")){
      println("Signed Transactions successfully added for mixing. Please wait...")
    }
    if(str.equals("Success2")){
      println("Transaction successfully mixed. Bitcoin payment should have been made.")
    }
  }
    
}