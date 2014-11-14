import java.math._
import scala.collection.mutable.ArrayBuffer
class TransactionParser(transactionString : String){

  var stream = transactionString.getBytes()
  var counter = 0
  def parseTransaction() : Transaction = {
	  //initialize vars needed to compute transaction hash
		var transactionVersion = readUnsigned32
		println("T" + transactionVersion)
		//INPUTS
		var inputCount = readVariableLengthInt()
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
		var transactionLockTime = readUnsigned32()
		return new Transaction(transactionVersion, inputs, outputs, transactionLockTime,
		    null);
	}
	
	def parseInput() : TransactionInput = {
		var transactionHash = readXBytes(32)
		var transactionIndex = readUnsigned32
		var scriptLength = readVariableLengthInt
		//println("In: " + scriptLength)
		var scriptData = readXBytes(scriptLength)
		var sequenceNumber = readUnsigned32
		
		return new TransactionInput(transactionHash,transactionIndex,scriptLength,
				scriptData, sequenceNumber)
	}
	
	def parseOutput() : TransactionOutput = {
		var value = readUnsigned64
		var scriptLength = readVariableLengthInt
		//println("Output: " + scriptLength)
		var script = readXBytes(scriptLength)
		
		return new TransactionOutput(value,scriptLength,script)
	}
	
	def readVariableLengthInt() : Long = {
		var firstByte = readUnsigned8
		//println(firstByte)
		if(firstByte < 253){return firstByte.toInt}
		else if(firstByte == 253){return readUnsigned16}
		else if(firstByte == 254){return readUnsigned32}
		else if(firstByte == 255){
		  println("Reached")
		  return (readUnsigned32 | readUnsigned32<< 32)
		  } 
		else{return firstByte.toInt}
	}
	
	
	def readUnsigned64() : Long = {
		var u_int64 = new Array[Byte](8);
		for(i <- 0 to 7){ 
			u_int64(i) = stream(counter)
			counter = counter + 1
		}
		var tmp = new Array[Byte](8)
		for(i <- 0 to 7){
		  tmp(i) = u_int64(7-i)
		}
		return new BigInteger(tmp).longValue()
	}
	
	def readUnsigned32() : Long = {
		var tmp = new Array[Byte](4)
		for(i <- 0 to 3){
			tmp(i) = stream(counter)
			counter = counter + 1
		}
		var value = 
				((tmp(0) & 0xFF) <<  0) |
				((tmp(1) & 0xFF) <<  8) |
				((tmp(2) & 0xFF) << 16) |
				((tmp(3) & 0xFF) << 24)
		return value;
	}
	
	def readUnsigned16() : Int = {
		var tmp = new Array[Byte](4)
		for(i <- 0 to 1){
			tmp(i) = stream(counter)
			counter = counter +1
		}
		var toReturn = (0xFF & tmp(0) | (0xFF & tmp(1)) << 8)
		return toReturn
	}
	
	def readUnsigned8() : Int = {
		var u_int8 = stream(counter)
		counter = counter + 1
		return u_int8 & 0xFF;
	}
	
	def readXBytes(bytesToRead : Long) : Array[Byte] = {
		var read = new Array[Byte](bytesToRead.toInt) //it is always a safe conversion
		for(i <- 0 to bytesToRead.toInt - 1){
			var tmp = stream(counter)
			counter = counter + 1
			read(i) = tmp
		}
		return read;
	}
}