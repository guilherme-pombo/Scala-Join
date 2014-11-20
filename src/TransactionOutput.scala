import java.math._

class TransactionOutput(value : Long, scriptLength :Long, script : Array[Short]) {
	
	def getScript : Array[Short] = script
	
	def getValue = value
//	def getBytes : Array[Byte] = {
//	  	return value.toBinaryString.getBytes ++ scriptLength.toBinaryString.getBytes ++ script
//	}
	
	def printOutput = {
	  println("\t\t Value: " + value)
	  println("\t\t ScriptLength: " + scriptLength)
	  var b1 = Tools.shortArrayToHexString(script)
	  println("\t\t Script: " + b1)
	}
}