package crypto

import java.security.MessageDigest

object SHA256 {
	
	def sha256(bytestring : Array[Byte]) : Array[Byte] = {
		var md = MessageDigest.getInstance("SHA-256")
		md.update(bytestring) // Change this to "UTF-16" if needed
		var digest = md.digest
		return digest
	}
	
	def doubleSha256(bytestring : Array[Byte]) : Array[Byte] = {
		return sha256(sha256(bytestring))
	}
}