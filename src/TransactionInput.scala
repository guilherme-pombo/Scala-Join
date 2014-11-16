//import crypto._
class TransactionInput(transactionHash :Array[Short], transactionIndex : Long, 
    scriptLength : Long, scriptData : Array[Short], sequenceNumber : Long) {
	
	//def getTransactionHash : String = return new String(Base58.byteEncode(transactionHash))
	var scriptD = scriptData
	
	def getPrevHash = transactionHash
	def getIndex = transactionIndex
	def getSeqNum = sequenceNumber
	
	def getScript = scriptD
	def setScript(script : Array[Short]) = {
	  scriptD = script
	}
	
	def printInput = {
	  //Don't know why I have to do this
	  var b1 = Tools.reverseTwoByTwo(Tools.shortArrayToHexString(transactionHash).reverse)
	  println("\t Previous Transaction Hash: " + b1)
	  println("\t Transaction index: " + transactionIndex)
	  println("\t Script Length: " + scriptLength)
	  var b2 = Tools.shortArrayToHexString(scriptData)
	  println("\t Script: " + b2)
	  println("\t Sequence Number: " + sequenceNumber)
	}
}