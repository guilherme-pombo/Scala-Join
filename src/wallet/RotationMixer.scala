package wallet
import scala.collection.mutable.ArrayBuffer

//This object file helps implement Rotational Mixing
//It assumes the existence of N CoinJoin servers
object RotationMixer {

  //Given an address AD to send X BTC to and assuming the existance
  //of N CoinJoin servers, this method creates N raw transactions
  //each sending X/N BTC to AD
  def splitTransaction(address : String, btc : Double,
      n : Int, txId : String) : ArrayBuffer[String] = {
    var newBtc = btc/n;
    var txs = new ArrayBuffer[String]
    for(i <- 1 to n){
      txs += Wallet.createRawTransaction(txId, 0, address, newBtc)
    }
    txs
  }
  
}