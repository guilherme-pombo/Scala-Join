package misc

import scala.sys.process._

object TransactionGenerator {
				
	//IGNORE THIS CLASS FOR NOW
	//IGNORE
   def main(args: Array[String]) {
     generateTransactions
   }
   
   def p(s : String) = scala.sys.process.stringToProcess("cmd /C " + s)
   def v(s: String) = scala.sys.process.stringToProcess("cmd /C " + s).!!
   
   def generateTransactions =  {
	   println("Start of script")
	   //Create private blockchain
	   //p("bitcoin-cli -regtest setgenerate true 101")
	   //CREATE PUBLIC KEY
	   var address = v("bitcoin-cli -regtest getnewaddress")
	   println(address)
	   //SEND COINS TO THAT ADDRESS FROM BLOCKCHAIN
	   p("bitcoin-cli -regtest sendtoaddress " + address + " 10.00")
	   //CONFIRM RECENT TRANSACTION
	   p("bitcoin-cli -regtest setgenerate true 1")
	   //DISPLAY CONFIRMED TRANSACTIONS
	   var list = v("bitcoin-cli -regtest listunspent")
	   var txid = listParser(list)
	   var vout = "0"
	   //CREATE PUBLIC KEY
	   var receiver = v("bitcoin-cli -regtest getnewaddress")
	   var cmd = "bitcoin-cli -regtest createrawtransaction "
	   var arg1 = "\"[{\"txid\":\""+ txid + "\",\"vout\":" + vout + "}]\" "
	   var arg2 = "\"{\""+ receiver+ "\":49.99}\""
	   var hex = v(cmd + arg1 + arg2)
	   println(hex)
	   println("End of Script")
   }
   
   def listParser(s : String) = {
	   var array = s.split("[\\{\\}]") //need escape characters
	   var ret = ""
	   for(i <- array){
	     var array2 = i.split(",")
	     var txid = ""
	     var vout = ""
	     for(j <- array2){
	       if(j.contains("txid")){
	         var txidstr = j.split(":")(1).replaceAll("\\s+","")
	         txid = txidstr.substring(1, txidstr.length-1)
	       }
	       if(j.contains("vout")){
	         vout = j.split(":")(1).replaceAll("\\s+","")
	       }
	     }
	     if(!txid.equals("")){
		    ret = txid
	     }
	   }
	   ret
   }
   
}