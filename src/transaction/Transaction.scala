package transaction

import scala.collection.mutable.ArrayBuffer

class Transaction (transactionVersion : Long, inputCount : Long, inputs : ArrayBuffer[TransactionInput], 
		outputCount : Long, outputs : ArrayBuffer[TransactionOutput] , transactionLockTime : Long) {
	
	var _inputs = inputs
	var _outputs = outputs
	var _inputCount = inputCount
	var _outputCount = outputCount
	
	def getInputs : ArrayBuffer[TransactionInput] = return _inputs
	def getOutputs : ArrayBuffer[TransactionOutput] = return _outputs
	def getTransactionVersion = transactionVersion
	def getTransactionLockTime = transactionLockTime
	def addOutput(out : TransactionOutput) = {_outputs += out}
	def addInput(in : TransactionInput) = {_inputs += in}
	def addInputCount(toAdd : Int) = {_inputCount += toAdd}
	def addOutputCount(toAdd : Int) = {_outputCount += toAdd}
	
	//Display the transaction in a nice string
	def printTransaction = {
	  println("Version: " + transactionVersion)
	  println("InputCount: " + _inputCount)
	  var i = 0
	  for( in <- _inputs){
	    println("\t Input " + i + ":")
	    in.printInput
	    i= i +1
	  }
	  i= 0
	  println("OutputCount: " + _outputCount)
	  for( out <- _outputs){
	    println("\t Output " + i + ":")
	    out.printOutput
	    i = i + 1
	  }
	  println("LockTime: " + transactionLockTime)
	}
	
}