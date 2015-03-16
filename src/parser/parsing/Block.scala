package parser.parsing
import scala.collection.mutable.ArrayBuffer
import misc.Base58
import parser.crypto.SHA256

class Block(magicID : Long, blockLength : Long, version : Long, prevHash : Array[Byte],
			merkleRoot : Array[Byte], timeStamp : Long, targetDifficulty : Long, 
			nonce : Long, transactions : ArrayBuffer[Transaction], blockBytes : Array[Byte]) {
	
  
	def getTransactions : ArrayBuffer[Transaction] = return transactions
  	
	def getBlockHash : String = return new String(Base58.byteEncode(SHA256.doubleSha256(blockBytes)))
	    
	def getPrevBlockHash : String = return new String(Base58.byteEncode(prevHash))
	
	def getBytes : Array[Byte] = return version.toBinaryString.getBytes() ++ prevHash ++
	merkleRoot ++ timeStamp.toBinaryString.getBytes() ++ targetDifficulty.toBinaryString.getBytes() ++
	nonce.toBinaryString.getBytes
}