import scala.collection.mutable.ArrayBuffer
//import crypto._

class Transaction (transactionVersion : Long, inputCount : Long, inputs : ArrayBuffer[TransactionInput], 
		outputCount : Long, outputs : ArrayBuffer[TransactionOutput] , transactionLockTime : Long) {
	
	var _inputs = inputs
	var _outputs = outputs
	var _inputCount = inputCount
	var _outputCount = outputCount
	
	def getInputs : ArrayBuffer[TransactionInput] = return _inputs
	def getOutputs : ArrayBuffer[TransactionOutput] = return _outputs
//	def getTransactionHash : String = 
//	  return new String(Base58.byteEncode(SHA256.doubleSha256(arrayBufferToArray(transactionBytes))))
	
	
	def arrayBufferToArray(a : ArrayBuffer[Byte]) : Array[Byte] = {
		var xs = new Array[Byte](a.length)
		a.copyToArray(xs)
		return xs
	}
	
	def printTransaction = {
	  println("Version: " + transactionVersion)
	  println("InputCount: " + _inputCount)
	  for( in <- _inputs){
	    in.printInput
	  }
	  println("OutputCount: " + _outputCount)
	  for( out <- _outputs){
	    out.printOutput
	  }
	  println("LockTime: " + transactionLockTime)
	}
	
	def getTransactionVersion = transactionVersion
	def getTransactionLockTime = transactionLockTime
	
	def addOutput(out : TransactionOutput) = {_outputs += out}
	def addInput(in : TransactionInput) = {_inputs += in}
	def addInputCount(toAdd : Int) = {_inputCount += toAdd}
	def addOutputCount(toAdd : Int) = {_outputCount += toAdd}
}