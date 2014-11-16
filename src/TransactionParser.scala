import java.math._
import java.lang.Long
import java.lang.Short
import scala.collection.mutable.ArrayBuffer

class TransactionParser(transactionString : String){

  var shortArray = hexToShortArray(transactionString)
  var counter = 0
  
  //need this so it doens't mess up
  implicit def javaToScalaShort(d: java.lang.Short) = d.shortValue
  
  //parse a string of hex to an array of Shorts
  def hexToShortArray(hex : String) : Array[scala.Short]= {
    var array = new Array[scala.Short](hex.length /2)
    var count = 0
    var i = 0
    while(i < hex.length){
      array(count) = javaToScalaShort(Short.parseShort(hex.substring(i, i+2), 16))
      count = count + 1
      i = i + 2
    }
    array
  }
	
  def parseTransaction() : Transaction = {
	  //initialize vars needed to compute transaction hash
		var transactionVersion = readUnsigned32
		//INPUTS
		var inputCount = readVariableLengthInt
		var inputs =  new ArrayBuffer[TransactionInput]()
		for(i <- 0 to inputCount.toInt -1){
			inputs += parseInput
		} 
		//OUTPUTS
		var outputCount = readVariableLengthInt
		var outputs =  new ArrayBuffer[TransactionOutput]()
		for(i <- 0 to outputCount.toInt -1){
			outputs += parseOutput
		} 
		var transactionLockTime = readUnsigned32
		return new Transaction(transactionVersion, inputCount, inputs, outputCount, 
		    outputs, transactionLockTime)
	}
	
	def parseInput() : TransactionInput = {
		var transactionHash = readXShorts(32)
		var transactionIndex = readUnsigned32
		var scriptLength = readVariableLengthInt
		var scriptData = readXShorts(scriptLength)
		var sequenceNumber = readUnsigned32
		
		return new TransactionInput(transactionHash,transactionIndex,scriptLength,
				scriptData, sequenceNumber)
	}
	
	def parseOutput() : TransactionOutput = {
		var value = readUnsigned64
		var scriptLength = readVariableLengthInt
		var script = readXShorts(scriptLength)
		
		return new TransactionOutput(value,scriptLength,script)
	}
	
	def readVariableLengthInt() : Long = {
		var firstByte = readUnsigned8
		if(firstByte < 253){firstByte}
		else if(firstByte == 253){readUnsigned16}
		else if(firstByte == 254){readUnsigned32}
		else if(firstByte == 255){
		  return (readUnsigned32 | readUnsigned32<< 32)
		  } 
		else{firstByte}
	}
	
	//slightly different method because JVM is terrible with big numbers
	def readUnsigned64() : Long = {
	    (readUnsigned32 | readUnsigned32<< 32)
	}
	
	def readUnsigned32() : Long = {
		var cc : Long = 0
		for(i <- 0 to 3){
		  cc += shortArray(counter) << 8 * i
		  counter = counter + 1
		}
		cc
	}
	
	def readUnsigned16() : Long = {
		var cc : Long = 0
		for(i <- 0 to 1){
		  cc += shortArray(counter) << 8 * i
		  counter = counter + 1
		}
		cc
	}
	
	def readUnsigned8() : Long = {
		var tmp = shortArray(counter)
		counter = counter + 1
		tmp.toLong
	}
	
	def readXShorts(numToRead : Long) : Array[scala.Short] = {
	  var tmp = new Array[scala.Short](numToRead.toInt)
	  for(i <- 0 to tmp.length - 1){
	    tmp(i) = shortArray(counter)
	    counter = counter + 1
	  }
	  tmp
	}
}