package parsing
import java.math._

class TransactionOutput(value : Long, scriptLength :Long, script : Array[Byte]) {
	
	def getScript : Array[Byte] = script
	
//	def getBytes : Array[Byte] = {
//	  	return value.toBinaryString.getBytes ++ scriptLength.toBinaryString.getBytes ++ script
//	}
}