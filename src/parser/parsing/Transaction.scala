package parser.parsing
import scala.collection.mutable.ArrayBuffer
import misc.Base58
import parser.crypto.SHA256

class Transaction (transactionVersion : Long, inputs : ArrayBuffer[TransactionInput], 
		outputs : ArrayBuffer[TransactionOutput] , transactionLockTime : Long,
		transactionBytes : ArrayBuffer[Byte]) {
	
	def getInputs : ArrayBuffer[TransactionInput] = return inputs
	def getOutputs : ArrayBuffer[TransactionOutput] = return outputs
	def getTransactionHash : String = 
	  return new String(Base58.byteEncode(SHA256.doubleSha256(arrayBufferToArray(transactionBytes))))
	
	def arrayBufferToArray(a : ArrayBuffer[Byte]) : Array[Byte] = {
		var xs = new Array[Byte](a.length)
		a.copyToArray(xs)
		return xs
	}
	
	def getBytesInputs(in : ArrayBuffer[TransactionInput]) : Array[Byte] = {
	  var ret = new Array[Byte](0)
	  for(i <- in){
	    ret = ret ++ i.getBytes
	  }
	  return ret
	}
	
	//returns a readable string representing the transaction
	def getString : String = {
	  var ret = ""
	  ret += "Transaction Version: " + transactionVersion + "\n"
	  ret += "Input Count: " + inputs.length + "\n"
	  var i = 0
	  for( in <- inputs){
	    ret += "\t Input " + i + ":" + "\n"
	    ret += in.getString
	    i= i +1
	  }
	  i= 0
	  ret += "Output Count: " + outputs.length + "\n"
	  for( out <- outputs){
	    ret += "\t Output " + i + ":" + "\n"
	    ret += out.getString
	    i= i +1
	  }
	  ret += "LockTime: " + transactionLockTime + "\n"
	  ret
	}

}