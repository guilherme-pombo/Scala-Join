	
package transaction

import misc.Tools

class TransactionInput(transactionHash :Array[Short], transactionIndex : Long, 
    _scriptLength : Long, scriptData : Array[Short], sequenceNumber : Long, data : Array[Short]) {

	var scriptD = scriptData
	var scriptLength = _scriptLength
	
	def getPrevHash = transactionHash
	def getIndex = transactionIndex
	def getScriptLen = scriptLength
	def getSeqNum = sequenceNumber
	def getDataArray = data
	def getScript = scriptD
	def setScript(script : Array[Short]){
	  scriptD = script.clone()
	  scriptLength = script.length
	}
	def readableScript = {
	  Tools.shortArrayToHexString(scriptData)
	}
	
	def printInput{
	  var b1 = Tools.reverseTwoByTwo(Tools.shortArrayToHexString(transactionHash).reverse)
	  println("\t\t Previous Transaction Hash: " + b1)
	  println("\t\t Transaction index: " + transactionIndex)
	  println("\t\t Script Length: " + scriptLength)
	  var b2 = Tools.shortArrayToHexString(scriptData)
	  println("\t\t Script: " + b2)
	  println("\t\t Sequence Number: " + sequenceNumber)
	}
	
	//string used to check if two different inputs are the same
	def getComparableString : String = {
	  var b1 = Tools.reverseTwoByTwo(Tools.shortArrayToHexString(transactionHash).reverse)
	  "" + b1 + transactionIndex + sequenceNumber
	}
}