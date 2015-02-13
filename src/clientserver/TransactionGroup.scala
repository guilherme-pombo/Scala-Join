package clientserver
import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer

//class used to store ClientInfo for client's participating in the same Mix
class TransactionGroup(um: String, sm: String) {
	
  private val userInfo : HashMap[String, ClientInfo] = new HashMap
  private var signedTxs = new ArrayBuffer[String]
  var unsignedMergedTx = um
  var signedMergedTx = sm
    
  def putInfo(IP :String, info : ClientInfo) = userInfo.put(IP, info)
  def containsIP(IP: String) : Boolean =  userInfo.contains(IP)
  
  def addSignedTx(tx : String) = signedTxs += tx
  //if there is one signedTx per client, then return true
  def enoughSignedTxs: Boolean = signedTxs.size == userInfo.size 
  
  def arrayToString : String = {
    var ret = ""
    for(s <- signedTxs){
      ret += s + "\n"
    }
    ret
  }
}