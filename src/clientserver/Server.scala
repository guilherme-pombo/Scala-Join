package clientserver
import java.net._
import java.io._
import scala.io._
import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer
import coinJoin.MergeTransactions

class Server {
	
	//this will store a mapping from IP address to client info
	var userInfo : HashMap[String, ClientInfo] = new HashMap
	//this will store the various groups of clients involved in Mixing
	var transactionGroups = new ArrayBuffer[TransactionGroup]
	//this will be used to map IP to hexadecimal transactions
	var userTx : HashMap[String, String] = new HashMap
	final val MINTX = 10 //minimum amount of users need in a mix to guarantee anonymity 
	var currIP = ""
	
	def start = {
	  val server = new ServerSocket(9999)
	  while (true) {
	    //Objects needed for communication
	    val socket = server.accept
	    val in = new BufferedSource(socket.getInputStream).getLines
	    val out = new PrintStream(socket.getOutputStream)
	    
	    currIP = socket.getInetAddress.toString
	    var str = in.next
	    //If initial request
	    if(str.substring(0, 6).equals("First")){
	      //count the number of transactions that meet the client's requirements
	      var result = countTransactions(str.substring(6,str.length))
	      out.println(result)
	      out.flush
	      socket.close
	    }
	    
	    //Client requesting merged unsigned transaction
	    if(str.substring(0, 12).equals("ReqUnsigned")){
	      var ret = ""
	      if(userInfo(currIP).unsigned){
	        ret = "Done:" + userInfo(currIP).mergedUnsigned 
	      }
	      //wait 3 minutes and try again
	      else{ret = "Wait:3"}
	      out.println(ret)
	      out.flush
	      socket.close
	    }
	    
	    if(str.substring(0,9).equals("signedTx")){
	      var signedTx = str.split(":")(1)
	      var everySignedTx = ""
	      //iterate through the various transactionGroups
	      var inGroup = false
	      for(group <- transactionGroups){
	        if(group.containsIP(currIP)){
	          inGroup = true
	          //add the transaction
	          group.addSignedTx(signedTx)
	          //if all the necessary transactions for merging have been added
	          if(group.enoughSignedTxs){
	            everySignedTx = group.arrayToString
	          }
	        }
	      }
	      var ret = ""
	      //Client requested merging a signedTx but is not part of a transaction group
	      if(!inGroup){
	        ret = "error"
	      }
	      //Successfully add signedTx, but not enough txs yet to merge
	      if(everySignedTx.equals("")){
	        ret = "Success1"
	      }
	      //enough txs to merge
	      if(!everySignedTx.equals("")){
	        ret = "Success2"
	        var mergeSigned = MergeTransactions.mergeSigned(everySignedTx)
	        //NEED TO USE BITCOIN NETWORK TO USE MERGESIGNED AND MAKE THE PAYMENT
	      }
	      out.println(ret)
	      out.flush
	      socket.close
	    }
	    	    
	  }
	  
	}
	
	def countTransactions(str: String) : String= {
	  //split the string
      var spl = str.split("-")
      //get the variable values from the string
      var btc = spl(0).split(":")(1).toDouble
      var change = spl(1).split(":")(1).toDouble
      var hexTx = spl(2).split(":")(1)
	  val it = userInfo.iterator
	  var count = 0
	  var txs = "" //transactions to be merged
      while(it.hasNext){
        var tmp = it.next._2 
        if((tmp.btcTotal == btc || tmp.btcTotal == btc + change || tmp.btcTotal == btc - change)
            && !tmp.unsigned){
          count = count + 1
          txs += tmp.hexTx + "\n"
        }
      }
      var ret = ""
      if(count >= MINTX){
        //merge the unsigned transactions and send them back
        var merge = MergeTransactions.mergeUnsigned(txs)
        val it2 = userInfo.iterator
        //start new transaction group
        var tg = new TransactionGroup(merge, "")
        //update every client that had requested a merging with the specified value
        while(it.hasNext){
	        var index = it.next._1 
	        var tmp = it.next._2 
	        if((tmp.btcTotal == btc || tmp.btcTotal == btc + change || tmp.btcTotal == btc - change)
	            && !tmp.unsigned){
	          var info = new ClientInfo(tmp.btcTotal, tmp.change , tmp.hexTx , true,
	              merge, false, "")
	          userInfo(index) = info
	          tg.putInfo(index, info)
	        }
        }
        //add new transaction group to the existing groups
        transactionGroups += tg
        ret = "Done:" + merge
      }
      else{
        var rest = MINTX - count
        var time2Wait = rest //1 minute * rest
        ret = "Wait:" + time2Wait
      }
      //if the user's info hasn't been saved in the server yet, save it
      if(!userInfo.contains(currIP)){
        userInfo.put(currIP, new ClientInfo(btc,change, hexTx, false, "", false, ""))
      }
	  return ret
	}
}