package taint

class TxInfo(address : String, category: String, txId: String, amount : Float, fee : Float,
    confirmations : Long, time: Long) {
  
  def getTxId = txId
  def getAddress = address
  def getCategory = category
  def getAmount = amount
  
  def printTx{
    println("Address: " + address )
    println("Category: " + category )
    println("TxId: " + txId )
    println("Amount: " + amount )
    println("Fee: " + fee )
    println("Confirmations: " + confirmations )
    println("Time: " + time )
  }

}