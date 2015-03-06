package transaction

import java.math._
import misc.Tools

class TransactionOutput(value : Long, scriptLength :Long, script : Array[Short],
    data : Array[Short]) {
	
	def getScript : Array[Short] = script
	def getValue = value
	def getScriptLength = scriptLength
	def getValueAsShort : Array[Short] = {
	  var ret = new Array[Short](8)
	  for (i<- 0 to 7){
	    ret(i) = data(i)
	  }
	  ret
	}
	
	def printOutput = {
	  println("\t\t Value: " + value)
	  println("\t\t ScriptLength: " + scriptLength)
	  var b1 = Tools.shortArrayToHexString(script)
	  println("\t\t Script: " + b1)
	}
	
	//returns a string which can be used to compare outputs
	def getComparableString : String = {
	  "" + value + Tools.shortArrayToHexString(script)
	}
}