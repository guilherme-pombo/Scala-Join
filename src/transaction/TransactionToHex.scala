package transaction

import java.nio.ByteBuffer
import misc.Tools

//USE THIS CLASS TO CONVERT SCALA TRANSACTION OBJECTS
//TO HEXADECIMAL STRINGS REPRESENTING TRANSACTIONS SO THE BITCOIN
//CLIENT CAN USE IT
object TransactionToHex {

  def convertToHex(tx : Transaction) : String = {
      var ver = longToHex(tx.getTransactionVersion)
	  var inCount = varIntToHex(tx._inputCount)
	  var ret = ""
	  for(in <- tx.getInputs){
	    ret += Tools.shortArrayToHexString(in.getPrevHash)
	    ret += longToHex(in.getIndex)
	    ret += varIntToHex(in.getScriptLen)
	    ret += Tools.shortArrayToHexString(in.getScript)
	    ret += longToHex(in.getSeqNum)
	  }
	  var outCount = varIntToHex(tx._outputCount)
	  var ret2 = ""
	  for(out <- tx.getOutputs){
	    ret2 += Tools.shortArrayToHexString(out.getValueAsShort)
	    ret2 += varIntToHex(out.getScriptLength)
	    ret2 += Tools.shortArrayToHexString(out.getScript)
	  }
	  var lockTime = longToHex(tx.getTransactionLockTime)
	  var ret3 =  ver + inCount + ret + outCount + ret2 + lockTime
	  ret3
  }
  
  //need special method, because values are 64 bit unsigned, rather than 32 bit
  private def valueToHex(lo : Long) : String = {
    Tools.reverseTwoByTwo(java.lang.Long.toHexString(lo).reverse)
  }
  
  private def longToHex(lo : Long) : String = {
    //convert the long to a byte array
    var bytes = ByteBuffer.allocate(8).putLong(lo).array()
    //convert byte array to Hex
    var tmp = bytes2Hex(bytes)
    var tmp2 = tmp.substring(10, tmp.length)
    var hex = Tools.reverseTwoByTwo(tmp2.reverse)
    hex.toLowerCase //just for formatting purposes
  }
  
  private def varIntToHex(lo : Long) : String = {
     //convert the long to a byte array
    var bytes = ByteBuffer.allocate(8).putLong(lo).array()
    //convert byte array to Hex
    var tmp = bytes2Hex(bytes)
    var check = tmp.substring(tmp.length -3, tmp.length)
    var hex = ""
    if(check == "FD"){
      var tmp2 = tmp.substring(tmp.length -5, tmp.length)
      hex = Tools.reverseTwoByTwo(tmp2.reverse).toLowerCase()
    }
    if(check == "FE"){
      var tmp2 = tmp.substring(tmp.length -7, tmp.length)
      hex = Tools.reverseTwoByTwo(tmp2.reverse).toLowerCase()
    }
    if(check == "FF"){
      var tmp2 = tmp.substring(tmp.length -9, tmp.length)
      hex = Tools.reverseTwoByTwo(tmp2.reverse).toLowerCase()
    }
    // <FD
    else{
      var tmp2 = check
      hex = Tools.reverseTwoByTwo(tmp2.reverse).toLowerCase()
    }
    hex
  }
  
  //Credit
  //From: http://www.dzone.com/snippets/scala-code-convert-tofrom-hex
  private def bytes2Hex( bytes: Array[Byte] ): String = {
    def cvtByte( b: Byte ): String = {
        (if ((b & 0xff) < 0x10 ) "0" else "" ) + java.lang.Long.toString( b & 0xff, 16 )
    }

    "0x" + bytes.map( cvtByte( _ )).mkString.toUpperCase
  }
  
  //convert a short to a hexadecimal string
  def shortToHex(sho : Short) = {
    Integer.toHexString(sho.intValue()).substring(4)
  }
  
}