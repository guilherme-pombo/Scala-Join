package taint

import wallet.Wallet
import scala.collection.mutable.ArrayBuffer

object TaintAnalysis {
	
  def main(args: Array[String]) {
    //
  }
  
  def getTaint(address1 : String, address2 : String, txs: Array[TxInfo]) : Float = {
    //get all transactions in which address1 participated
    var txs1 = getAddressHistory(address1, txs) 
    //get all transactions where address1 received money
    //category = receive
    var totalMoneyReceived : Float = 0
    var received = new ArrayBuffer[String]
    for(t<-txs1){
      if(t.getCategory.equals("received")){
        received += t.getTxId
        totalMoneyReceived += t.getAmount
      }
    }
    //Check if address2 possibly sent any money to address1
    //Can't tell for sure due to CoinJoin merging
    var hist = getAddressHistory(address2, txs)
    var moneySentToAddress1 : Float = 0
    for(t <- hist){
      for(txId <- received){
        if(t.getTxId.equals(txId)){
          moneySentToAddress1 += t.getAmount
        }
      }
    }
    //The taint of address 1 to address 2 will be the total amount of money
    //received by address 1 from to address 2, divided by the total amount
    //of money received
    moneySentToAddress1/totalMoneyReceived //TAINT
  }
  
  //get all transactions in which a specific address was involved
  def getAddressHistory(address : String, txs : Array[TxInfo]) : ArrayBuffer[TxInfo] ={
    var ret = new ArrayBuffer[TxInfo]
    for(t <- txs){
      if(t.getAddress.equals(address)){
        ret += t
      }
    }
    ret
  }
  
  //get information for addresses involved in a specific transaction
  def getParticipants(txId : String, txs : Array[TxInfo]) : ArrayBuffer[TxInfo] = {
    var ret = new ArrayBuffer[TxInfo]
    for(t <- txs){
      if(t.getTxId.equals(txId)){
        ret += t
      }
    }
    ret
  }
  
  //Get the Transaction History for the numTx most recent transactions
  //ArrayBuffers are not used to speed up things
  def getTransactionHistory(numTx : Int) : Array[TxInfo] = {
    var raw = Wallet.listTransactions(numTx)
    //split for all types of account ""
    var info = raw.split("account")
    //Use the following splitting parameters
    //Address
    //Category
    //Amount
    //Fee
    //Confirmations
    //TxId
    //Time
    var txsInfo = new Array[TxInfo](info.length-1)
    var txsInfoPointer = 0
    var splitParams = Array("address", "category", "amount", "fee", "confirmations", "txid",
        "time")
    var strings = new Array[String](3)
    var longs = new Array[Long](2)
    var floats = new Array[Float](2)
    for(i <- 1 to info.length -1){
        var stringsPointer = 0
        var longsPointer = 0
        var floatsPointer = 0
	    //Split info for each param
	    for(param <- splitParams){
	      //if it's a string
	      if(param.equals("category") || param.equals("address") || param.equals("txid")){
	        var split = info(i).split(param)
		    split = split(1).split(",")
		    strings(stringsPointer) = split(0).substring(5,split(0).length - 1)
		    stringsPointer = 1+ stringsPointer
	      }
	      //if it's a Long
	      else if(param.equals("confirmations") || param.equals("time")){
	        var split = info(i).split(param)
			split = split(1).split(",")
			longs(longsPointer) = split(0).substring(4,split(0).length).toLong
			longsPointer = longsPointer + 1
	      }
	      //if it's a float
	      else{
	        var split = info(i).split(param)
			split = split(1).split(",")
			floats(floatsPointer) = split(0).substring(4,split(0).length).toFloat
			floatsPointer = floatsPointer + 1
	      }
	    }
	    txsInfo(txsInfoPointer) = new TxInfo(strings(0), strings(1), strings(2), floats(0),
	        floats(1),longs(0),longs(1))
	    txsInfoPointer = txsInfoPointer + 1
    }
    txsInfo
  }
  
}