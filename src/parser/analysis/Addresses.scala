package analysis

import crypto.RIPEMD160
import crypto.SHA256

object Addresses {
  
	def publicKey65ToAddress(publicKey : Array[Byte]) : Array[Byte] = {
		var toReturn = null
		if(publicKey(0) == 0x04){
			var intermediate = SHA256.sha256(publicKey)
			var output = new Array[Byte](1)
			output(0) = 0
			var intermediate2 = RIPEMD160.ripemd160(intermediate)
			output = output ++ intermediate2
			intermediate = SHA256.sha256(output)
			intermediate = SHA256.sha256(intermediate)
			output = output :+ intermediate(0)
			output = output :+ intermediate(1)
			output = output :+ intermediate(2)
			output = output :+ intermediate(3)
			return output
		}
		return toReturn
	}
	
	def publicKey20ToAddress(publicKey : Array[Byte]) : Array[Byte] = {
		var output = new Array[Byte](1)
		output(0) = 0
		output = output ++ publicKey
		var intermediate = SHA256.sha256(output)
		intermediate = SHA256.sha256(intermediate)
		output = output :+ intermediate(0)
		output = output :+ intermediate(1)
		output = output :+ intermediate(2)
		output = output :+ intermediate(3)
		return output
	}
	
}