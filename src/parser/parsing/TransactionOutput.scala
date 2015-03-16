package parser.parsing
import java.math._
import misc.Base58

class TransactionOutput(value : Long, scriptLength :Long, script : Array[Byte]) {
	
	def getScript : Array[Byte] = script
	
	def getString : String = {
	  var ret = ""
	  ret += "\t\tValue: " + value + "\n"
	  ret += "\t\tScript Length: " + scriptLength + "\n"
	  ret += "\t\tScript Data: " + new String(Base58.byteEncode(script)) + "\n"
	  ret
	}
}