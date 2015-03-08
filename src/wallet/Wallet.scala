package wallet

import scala.sys.process._
import scala.collection.mutable.ArrayBuffer

//This object provides a series of Bitcoin Wallet operations that can be used
//by any other class. It's main use is to Testing the CoinJoin protocol and
//carrying out Server operations, such as sending out BTC.

object Wallet {
	
   def p(s : String) = scala.sys.process.stringToProcess("cmd /C " + s)
   def v(s: String) = scala.sys.process.stringToProcess("cmd /C " + s).!!
   
   //generate a bitcoin address --- NOT A PUBLIC KEY
   //creates a private key in the wallet for the address
   def generateAddress : String = {
     //println("bitcoin-cli -regtest getnewaddress")
     v("bitcoin-cli -regtest getnewaddress")
   }
   
   //generate a list of bitcoin addresses
   def generateAddressList(n : Int) : ArrayBuffer[String] = {
     var ret = new ArrayBuffer[String](n)
     for (i <- 0 to n-1){
       ret(i) = generateAddress
     }
     ret
   }
   
   //Because we are running tests in RegTest server we can give any address
   //as much BTC as we want. Give address, X btc.
   //Return the txId
   def giveAddressBTC(address : String, btc : Double): String = {
     //println("bitcoin-cli -regtest sendtoaddress " + address.stripLineEnd + " " + btc)
     v("bitcoin-cli -regtest sendtoaddress " + address.stripLineEnd + " " + btc)
   }
   
   //get Raw transaction info for given txId
   def getRawTransaction(txId : String) : String = {
     //println("bitcoin-cli -regtest getrawtransaction " + txId.stripLineEnd + " 1")
     v("bitcoin-cli -regtest getrawtransaction " + txId.stripLineEnd + " 1")
   }
   
   //create a raw transaction and return it's hexadecimal representation
   def createRawTransaction(txId : String, vout : Int, 
       receiver :String, amount : Double) : String = {
	 val arg1 = "\"[{\\\"txid\\\":\\\""+ txId.stripLineEnd +"\\\",\\\"vout\\\":" + vout + "}]\""
	 val arg2 = "\"{\\\""+ receiver.stripLineEnd+ "\\\":" + amount + "}\""
	 //println("bitcoin-cli -regtest createrawtransaction " + arg1 + " " + arg2)
	 v("bitcoin-cli -regtest createrawtransaction " + arg1 + " " + arg2)
   }
   
   //decode a hexadecimal raw transaction and present it as a JSON object
   def decodeRawTransaction(hex : String) : String = {
     v("bitcoin-cli -regtest decoderawtransaction " + hex)
   }
   
   //takes in a decoded raw transaction and returns the scriptPubKey
   def getScriptPubKey(decodedTx : String) : String = {
     var split = decodedTx.split("scriptPubKey")
	 split = split(1).split("hex")
	 split = split(1).split(",")
	 split(0).substring(5,split(0).length - 1)
   }
   
   //We need to provide the txId, vout and scriptpubkey of the inputs we're signing 
   //so our offline wallet knows which of its keys to use for the signature
   def signrawtransaction(hex : String, txId : String, vout: Int, spk : String) : String = {
     var arg1 = "\"[{\\\"txid\\\":\\\""+ txId.stripLineEnd + "\\\",\\\"vout\\\":" + 
     vout + ",\\\"scriptPubKey\\\":\\\"" + spk.stripLineEnd + "\\\"}]\""
     //println("bitcoin-cli -regtest signrawtransaction  " + hex.stripLineEnd + " " + arg1)
     v("bitcoin-cli -regtest signrawtransaction  " + hex.stripLineEnd + " " + arg1)
   }
   
   //Sends a signed transaction to the network
   //returns transaction id
   def sendTransaction(hex : String) : String = {
     v("bitcoin-cli -regtest sendrawtransaction " + hex)
   }
   
   //check if wallet has been paid
   def listTransactions() : String = {
      v("bitcoind listtransactions \"\" 1")
   }
   
}