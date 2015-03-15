package parser.crypto


object RIPEMD160 {
	
	def ripemd160(bytestring : Array[Byte]) : Array[Byte] = {
		var d = new RIPEMD160Digest();
        d.update (bytestring, 0, bytestring.length);
        var digest = new Array[Byte](d.getDigestSize);
        d.doFinal (digest, 0);
        var ret = bytesToHex(digest);
        return ret.getBytes
	}
	
	//http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java
	var hexArray = "0123456789ABCDEF".toCharArray();
	def bytesToHex(bytes : Array[Byte]) : String = {
	    var hexChars : Array[Char] = new Array[Char](bytes.length * 2)
	    for ( j <- 0 to bytes.length - 1) {
	        var v = bytes(j) & 0xFF
	        hexChars(j * 2) = hexArray(v >>> 4)
	        hexChars(j * 2 + 1) = hexArray(v & 0x0F)
	    }
	    return new String(hexChars);
	}
}