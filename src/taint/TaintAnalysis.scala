package taint

import wallet.Wallet
import scala.collection.mutable.ArrayBuffer

object TaintAnalysis {
	
  def main(args: Array[String]) {
    var r = getTransactionHistory(10)
    println(r.length)
    for(a <- r){
      println(a)
      println("-------------")
    }
  }
  
  //Get the Transaction History for the numTx most recent transactions
  def getTransactionHistory(numTx : Int) : Array[String] = {
    var raw = Wallet.listTransactions(numTx)
    raw.split("account")
  }
  
}