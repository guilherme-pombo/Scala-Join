import java.nio.ByteBuffer
import java.security.MessageDigest
import java.nio.ByteOrder
import scala.collection.mutable.ArrayBuffer
object Tools {
	
	//convert an array of shorts to an array of BIG ENDIAN bytes
	def shortArrayToByteArray(array : Array[Short]) = {
	  var bytes = new Array[Byte](array.length * 2)
	  ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).asShortBuffer().put(array)
	  bytes
	}
	
	def shortArrayToHexString(array: Array[Short]) = {
	  var hexNums = "0123456789abcdef"
	  var hex = hexNums.toCharArray
	  var chars = new Array[Char](array.length*2)
	  var count = 0
	  for(i <- array){
	    chars(count) = hex(i >> 4)
	    count = count + 1
	    chars(count)  = hex(i % 16)
	    count = count + 1
	  }
	  new String(chars)
	}
	
	def reverseTwoByTwo(str : String) = {
	  var res = ""
	  var i = 0
	  while(i<str.length-1){
	    res += str.substring(i,i+2).reverse
	    i= i+2
	  }
	  res
	}
//	pub fn u8_to_hex_string(data: &[u8]) -> ~str {
//  let hex_chars = "0123456789abcdef";
//  let mut rv = ~"";
//
//  for c in data.iter() {
//    rv.push_char (hex_chars[c >> 4] as char);
//    rv.push_char (hex_chars[c % 16] as char);
//  }
//  rv
//}
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