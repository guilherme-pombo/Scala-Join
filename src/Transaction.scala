import scala.collection.mutable.ArrayBuffer
//import crypto._

class Transaction (transactionVersion : Long, inputs : ArrayBuffer[TransactionInput], 
		outputs : ArrayBuffer[TransactionOutput] , transactionLockTime : Long,
		transactionBytes : ArrayBuffer[Byte]) {
	
	var _inputs = inputs
	var _outputs = outputs
	
	def getInputs : ArrayBuffer[TransactionInput] = return _inputs
	def getOutputs : ArrayBuffer[TransactionOutput] = return _outputs
//	def getTransactionHash : String = 
//	  return new String(Base58.byteEncode(SHA256.doubleSha256(arrayBufferToArray(transactionBytes))))
	
	
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
	
	def getTransactionVersion = transactionVersion
	def getTransactionLockTime = transactionLockTime
	
	def addOutput(out : TransactionOutput) = {_outputs += out}
	def addInput(in : TransactionInput) = {_inputs += in}
}