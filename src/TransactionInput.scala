//import crypto._
class TransactionInput(transactionHash : Array[Byte], transactionIndex : Long, 
    scriptLength : Long, scriptData : Array[Byte], sequenceNumber : Long) {
	
	//def getTransactionHash : String = return new String(Base58.byteEncode(transactionHash))
	var scriptD = scriptData
	def getBytes : Array[Byte] = {
	  return transactionHash ++ transactionIndex.toBinaryString.getBytes ++ 
	  scriptLength.toBinaryString.getBytes ++ scriptData ++ sequenceNumber.toBinaryString.getBytes
	}
	
	def getPrevHash = transactionHash
	def getIndex = transactionIndex
	def getSeqNum = sequenceNumber
	
	def getScript = scriptD
	def setScript(script : Array[Byte]) = {
	  scriptD = script
	}
}