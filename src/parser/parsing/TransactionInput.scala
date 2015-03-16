package parser.parsing
import misc.Base58

class TransactionInput(transactionHash : Array[Byte], transactionIndex : Long, 
    scriptLength : Long, scriptData : Array[Byte], sequenceNumber : Long) {
	
	def getTransactionHash : String = return new String(Base58.byteEncode(transactionHash))
	
	def getBytes : Array[Byte] = {
	  return transactionHash ++ transactionIndex.toBinaryString.getBytes ++ 
	  scriptLength.toBinaryString.getBytes ++ scriptData ++ sequenceNumber.toBinaryString.getBytes
	}
	
	def getString : String = {
	  var ret = ""
	  ret += "\t\tTransactionHash: " + getTransactionHash + "\n"
	  ret += "\t\tTransaction Index: " + transactionIndex + "\n"
	  ret += "\t\tScript Length: " + scriptLength + "\n"
	  ret += "\t\tScript Data: " + new String(Base58.byteEncode(scriptData)) + "\n"
	  ret += "\t\tSequence Number: " + sequenceNumber + "\n"
	  ret
	}
}