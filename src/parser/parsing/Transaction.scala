package parsing
import scala.collection.mutable.ArrayBuffer
import crypto._

class Transaction (transactionVersion : Long, inputs : ArrayBuffer[TransactionInput], 
		outputs : ArrayBuffer[TransactionOutput] , transactionLockTime : Long,
		transactionBytes : ArrayBuffer[Byte]) {
	
	def getInputs : ArrayBuffer[TransactionInput] = return inputs
	def getOutputs : ArrayBuffer[TransactionOutput] = return outputs
	def getTransactionHash : String = 
	  return new String(Base58.byteEncode(SHA256.doubleSha256(arrayBufferToArray(transactionBytes))))
	
//	def getTransactionHash : String = {
//	  var ret = transactionVersion.toBinaryString.getBytes ++ getBytesInputs(inputs) ++
//	  getBytesOutputs(outputs)
//	  return new String(Base58.byteEncode(SHA256.doubleSha256(ret)))
//	}
	
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
	
//	def getBytesOutputs(out : ArrayBuffer[TransactionOutput]) : Array[Byte] = {
//	  var ret = new Array[Byte](0)
//	  for(o <- out){
//	    ret = ret ++ o.getBytes
//	  }
//	  return ret
//	}
}