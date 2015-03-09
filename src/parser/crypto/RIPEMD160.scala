package crypto

import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.util.encoders.Hex;

object RIPEMD160 {
	
	def ripemd160(bytestring : Array[Byte]) : Array[Byte] = {
		var d = new RIPEMD160Digest();
        d.update (bytestring, 0, bytestring.length);
        var digest = new Array[Byte](d.getDigestSize);
        d.doFinal (digest, 0);
        Hex.encode (digest);
        return digest;
	}
}