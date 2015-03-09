package parsing

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import scala.collection.mutable.ArrayBuffer
import crypto._
import java.math._
import java.nio.file.Files
import java.nio.file.Paths

//For this to run need to allocate more heap space
//Usually passing in -Xmx1250m is enough
class MemoryParser(filename : String) {
  
    //file of type block000XX.dat containing the data necessary to parse the blockchain
	var stream = Files.readAllBytes(Paths.get(filename))
	var counter = 0
	//fields needed to compute both Block hash and Transaction hash
	//Block
	var readingBlockHeader = false
	var blockHeader : Array[Byte] = null//always size 80
	var blockHeaderIndex = 0
	//Transaction
	var transactionBytes : ArrayBuffer[Byte] = null
	var readingTransaction = false
	var transactionIndex : Int = 0
	def parseFile() :ArrayBuffer[Block] = {
		var toReturn = new ArrayBuffer[Block]()
		while(counter < stream.length){
		  toReturn += parseBlock
		}
		return toReturn
	}
	
	def parseBlock() : Block = {
		var magicID = findMagicID()
		//println(magicID == 0xD9B4BEF9)
		//println(counter)
		var blockLength = readUnsigned32
		//initialize vars needed to compute blockHash
		blockHeader = new Array[Byte](80)
		readingBlockHeader = true
		blockHeaderIndex = 0
		var version = readUnsigned32
		var prevHash = readXBytes(32)
		var merkleRoot = readXBytes(32)
		var timeStamp = readUnsigned32
		var targetDifficulty = readUnsigned32
		var nonce = readUnsigned32
		readingBlockHeader = false
		//End of block header
		var transactionCount = readVariableLengthInt()
		var transactions =  new ArrayBuffer[Transaction]()
		for( i <- 0 to transactionCount.toInt -1){
			transactions += parseTransaction
		}
		
		//End of parsing
		return new Block(magicID, blockLength, version, prevHash, merkleRoot, timeStamp, 
		    targetDifficulty, nonce, transactions, blockHeader)
	}
	
	def findMagicID() : Long = {
		var ID : Long = 0
		while(ID != 0xD9B4BEF9){
			ID = readUnsigned32
			if(ID != 0xD9B4BEF9){
				counter = counter - 3
			}
		}
		return ID
	}
	
	def parseTransaction : Transaction = {
	  //initialize vars needed to compute transaction hash
		readingTransaction = true
		transactionBytes = new ArrayBuffer[Byte]()
		transactionIndex = 0
		var transactionVersion = readUnsigned32
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
		readingTransaction = false
		return new Transaction(transactionVersion, inputs, outputs, transactionLockTime,
		    transactionBytes);
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
			if(readingTransaction){
				transactionBytes += u_int64(i)
				transactionIndex = transactionIndex + 1
			}
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
			if(readingBlockHeader){
				blockHeader(blockHeaderIndex) = tmp(i)
				blockHeaderIndex = blockHeaderIndex + 1
			}
			if(readingTransaction){
				transactionBytes += tmp(i)
				transactionIndex = transactionIndex + 1
			}
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
			if(readingBlockHeader){
				blockHeader(blockHeaderIndex) = tmp(i)
				blockHeaderIndex = blockHeaderIndex + 1
			}
			if(readingTransaction){
				transactionBytes += tmp(i)
				transactionIndex = transactionIndex + 1
			}
		}
		var toReturn = (0xFF & tmp(0) | (0xFF & tmp(1)) << 8)
		return toReturn
	}
	
	def readUnsigned8() : Int = {
		var u_int8 = stream(counter)
		counter = counter + 1
		if(readingBlockHeader){
			blockHeader(blockHeaderIndex) = u_int8
			blockHeaderIndex = blockHeaderIndex + 1
		}
		if(readingTransaction){
			transactionBytes += u_int8.toByte
			transactionIndex = transactionIndex + 1
		}
		return u_int8 & 0xFF;
	}
	
	def readXBytes(bytesToRead : Long) : Array[Byte] = {
		var read = new Array[Byte](bytesToRead.toInt) //it is always a safe conversion
		for(i <- 0 to bytesToRead.toInt - 1){
			var tmp = stream(counter)
			counter = counter + 1
			read(i) = tmp
			if(readingBlockHeader){
				blockHeader(blockHeaderIndex) = tmp
				blockHeaderIndex = blockHeaderIndex + 1
			}
			if(readingTransaction){
				transactionBytes += tmp
				transactionIndex = transactionIndex + 1
			}
		}
		return read;
	}
	
}