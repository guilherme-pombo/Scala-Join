package parser.analysis

//CURRENTLY NOT IN USE
import parser.parsing._
import misc.Base58
import scala.collection.mutable.ArrayBuffer
import java.util.HashMap;
import java.io.File

class BlockChain {
	
	var blockChain = new HashMap[String,Block]()
	var transactionChain = new HashMap[String,Transaction]()
	var lastBlockHash : String = ""
	var MAX_REASONABLE_SCRIPT_LENGTH = (1024*8)
	
	def getTransactionHistory = {
		buildBlockChain
		var lastBlock = blockChain.get(lastBlockHash) 
		while(lastBlock != null){
			var transactions = lastBlock.getTransactions
			for(t <- transactions){
				var senders = "Senders: \n"
				var receivers = "Receivers: \n"
				var inputs = t.getInputs
				var outputs = t.getOutputs
				for(in <- inputs){
					var parentTransactionHash = in.getTransactionHash
					println(parentTransactionHash)
					var parent = transactionChain.get(parentTransactionHash)
					if(parent!= null){
						var prevout = parent.getOutputs
						for(o2 <- prevout){
							//println("Out1: " + o2.getScript.length)
							senders += "\t" + getReadableAddress(readOutputScript(o2.getScript)) + "\n"
							print(senders)
						}
					}
				}
				for(out <- outputs){
					//println("Out2: " + out.getScript.length)
					receivers += "\t" + getReadableAddress(readOutputScript(out.getScript)) + "\n"
					print(receivers)
				}
				println(senders+receivers)
			}
			//println(lastBlock.getPrevBlockHash)
			
			lastBlock = blockChain.get(lastBlock.getPrevBlockHash)
		}
	}
	
	def buildBlockChain = {
		//Need to allocate extra heap space, -Xmx1250m
		var p = new MemoryParser("C:/Users/Pombo/AppData/Roaming/Bitcoin/regtest/blocks/blk00000.dat")
		var blocks = p.parseFile
		for(b <- blocks){
		  //if it is not a core block transaction
		  blockChain.put(b.getBlockHash,b)
		  lastBlockHash = b.getBlockHash
		  var transactions = b.getTransactions
	      for(t <- transactions){
		    transactionChain.put(t.getTransactionHash,t)
		  }
		}
		//writeToFile("C:/Users/Pombo/Desktop/FYP/test3.txt", tmp)
	}
	
	def writeToFile(p: String, s: String): Unit = {
	    val pw = new java.io.PrintWriter(new File(p))
	    try pw.write(s) finally pw.close()
	}
	
	def getReadableAddress(k : PublicKey) : String = {
	  var key = k.key
	  var isRipe = k.isRipeMD160
	  //println(key.length)
	  if(key.length == 65 && !isRipe){
			return new String(Base58.encode(Addresses.publicKey65ToAddress(key)))
		}
		else if(key.length == 20 && isRipe){
			return new String(Base58.encode(Addresses.publicKey20ToAddress(key)))
		}
		else{
			return "Not a valid public key"
		}
	}
	
	def readOutputScript(script : Array[Byte]) : PublicKey  = {
		var isRipeMD160 = false
		var publicKey = new Array[Byte](1)
		println(new String(Base58.byteEncode(script)))
		if(script.length<MAX_REASONABLE_SCRIPT_LENGTH){
		  if(script.length == 67 && script(0) == 65 && script(66)== OPCodes.OP_CHECKSIG){
			  publicKey = new Array[Byte](65)
			  for(i <- 0 to 64){
				  publicKey(i) = script(i+1)
			  }
		  }
		  else if(script.length == 66 && script(65)== OPCodes.OP_CHECKSIG){
			  publicKey = new Array[Byte](65)
			  for(i <- 0 to 64){
				  publicKey(i) = script(i)
			  }
		  }
		  else if ( script.length >= 25 && script(0) == OPCodes.OP_DUP && script(1) == OPCodes.OP_HASH160 
				&& script(2) == 20){
			  isRipeMD160 = true
			  publicKey = new Array[Byte](20)
			  for(i <- 3 to 22){
				  publicKey(i-3) = script(i)
			  }
		  }
		  else if (script.length == 5){
				//let the publicKey still be 1 in length to serve as a flag
		  }
		  else{
			  if ( script.length > 25 )
			  {
				  var endIndex =script.length-25;
				  var i = 0
				  var break = false
				  while(i<endIndex && !break)
				  {
					  if ( script(i) == OPCodes.OP_DUP && script(i+1) == OPCodes.OP_HASH160 &&
							  script(i+2) == 20 && script(i+23) == OPCodes.OP_EQUALVERIFY &&
							  script(i+24) == OPCodes.OP_CHECKSIG ){
						  publicKey = new Array[Byte](20)
						  isRipeMD160 = true
						  for(j <- i+3 to i+22){
							  publicKey(j-(i+3)) = script(j)
						  }
						  break = true
					  }
					  i = i +1
				  }
			  }
		  }
		}
		return new PublicKey(publicKey,isRipeMD160)
	}
	
}