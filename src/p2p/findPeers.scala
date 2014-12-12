package p2p
import scala.sys.process._
import java.io.BufferedReader
import java.io.StringReader
import scala.collection.mutable.ArrayBuffer
import java.io._
import java.net._

object findPeers {
	
    def p(s : String) = scala.sys.process.stringToProcess("cmd /C " + s)
    def v(s: String) = scala.sys.process.stringToProcess("cmd /C " + s).!!
    
	def findPeers() = {
      //Find list of peers
	  var peers = v("nslookup bitseed.xf2.org")
	  var adr = peers.split("Addresses:  ")(1)
	  var reader = new BufferedReader(new StringReader(adr))
	  var line = reader.readLine
	  var addresses = new ArrayBuffer[String] //stores all peer IPs as a String
	  while (line != null) {
        addresses += line.replaceAll("\\s+","") //take away all whitespace
        line = reader.readLine
	  }
	  
	}
    
    //Inspired by one of Oracle's tutorials
    //https://docs.oracle.com/javase/tutorial/
    def connectToPeer(address : String, msg : String){
    	try{
    		var socket = new Socket(address, 8333)
    		var out = new PrintWriter(socket.getOutputStream(), true)
    		var in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
    		var stdIn = new BufferedReader(new InputStreamReader(System.in))
    		while (true) {
    			out.println(msg);
    			System.out.println("echo: " + in.readLine());
    		}
    	} catch{
    	case e1: UnknownHostException => println("Couldn't find Host")
    	case e2: IOException => println("IOexception")
    	}
    }
    
    def createPayload(){
      var magic = "0xDAB5BFFA" //0xD9B4BEF9 for main net
      
    }
}