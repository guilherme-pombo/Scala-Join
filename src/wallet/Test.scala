package wallet
import coinJoin.MergeTransactions
import scala.collection.mutable.ArrayBuffer

object Test {

  def main(args: Array[String]) {
    test2Send(10)
  }
  
  //A few sample tests bellow
  //Note, these tests will simply output the result of using CoinJoin
  //They do not send the CoinJoin transaction to the bitcoin network
  //This, however could easily be done by using SendTransaction on the output of the test
  
  //Test1: Simplest test. Merges two simple transactions, signs the merged
  //transaction and then merges the signed transactions.
  //Both transactions have the same value
  def test1 {
    var mergeUnsigned = ""
    var signInfo = new ArrayBuffer[SignInfo]
    for(i<- 0 to 1){
      var ad1 = Wallet.generateAddress
	  var txId = Wallet.giveAddressBTC(ad1, 50)
	  var decoded = Wallet.getRawTransaction(txId)
	  var spk = Wallet.getScriptPubKey(decoded)
	  var ad2 = Wallet.generateAddress
	  signInfo += new SignInfo(txId, 0, spk)
	  mergeUnsigned += Wallet.createRawTransaction(txId, 0, ad2, 50)
    }
    var mergedTx = MergeTransactions.mergeUnsigned(mergeUnsigned)
    var mergedSigned = ""
    for(i <- 0 to 1){
      var info = signInfo(i)
      mergedSigned += Wallet.getHexSigned(Wallet.signrawtransaction(mergedTx, info.txId , 0, info.spk )) + "\n"
    }
    print(mergedSigned)
    var finalTx = MergeTransactions.mergeSigned(mergedSigned)
    println(finalTx)
  }
  
  //Same as test1 but sends the finalTx
  def test1Send {
    var mergeUnsigned = ""
    var signInfo = new ArrayBuffer[SignInfo]
    for(i<- 0 to 1){
      var ad1 = Wallet.generateAddress
	  var txId = Wallet.giveAddressBTC(ad1, 50)
	  var decoded = Wallet.getRawTransaction(txId)
	  var spk = Wallet.getScriptPubKey(decoded)
	  var ad2 = Wallet.generateAddress
	  signInfo += new SignInfo(txId, 0, spk)
	  mergeUnsigned += Wallet.createRawTransaction(txId, 0, ad2, 50)
    }
    var mergedTx = MergeTransactions.mergeUnsigned(mergeUnsigned)
    var mergedSigned = ""
    for(i <- 0 to 1){
      var info = signInfo(i)
      mergedSigned += Wallet.getHexSigned(Wallet.signrawtransaction(mergedTx, info.txId , 0, info.spk )) + "\n"
    }
    var finalTx = MergeTransactions.mergeSigned(mergedSigned)
    println(finalTx)
    Wallet.sendTransaction(finalTx)
  } 
  
  //Test2: 2nd simplest test. Merges N simple transactions, signs the merged
  //transaction and then merges the signed transactions.
  //All transactions have the same value
  def test2(transactionsToSign : Int) {
    var mergeUnsigned = ""
    var signInfo = new ArrayBuffer[SignInfo]
    for(i<- 0 to transactionsToSign - 1){
      var ad1 = Wallet.generateAddress
	  var txId = Wallet.giveAddressBTC(ad1, 50)
	  var decoded = Wallet.getRawTransaction(txId)
	  var spk = Wallet.getScriptPubKey(decoded)
	  var ad2 = Wallet.generateAddress
	  signInfo += new SignInfo(txId, 0, spk)
	  mergeUnsigned += Wallet.createRawTransaction(txId, 0, ad2, 50)
    }
    var mergedTx = MergeTransactions.mergeUnsigned(mergeUnsigned)
    var mergedSigned = ""
    for(i <- 0 to transactionsToSign - 1){
      var info = signInfo(i)
      mergedSigned += Wallet.getHexSigned(Wallet.signrawtransaction(mergedTx, info.txId , 0, info.spk )) + "\n"
    }
    var finalTx = MergeTransactions.mergeSigned(mergedSigned)
    println(finalTx)
  }
  
  //Same as test2 but sends the finalTx
  def test2Send(transactionsToSign : Int) {
    var mergeUnsigned = ""
    var signInfo = new ArrayBuffer[SignInfo]
    for(i<- 0 to transactionsToSign - 1){
      var ad1 = Wallet.generateAddress
	  var txId = Wallet.giveAddressBTC(ad1, 50)
	  var decoded = Wallet.getRawTransaction(txId)
	  var spk = Wallet.getScriptPubKey(decoded)
	  var ad2 = Wallet.generateAddress
	  signInfo += new SignInfo(txId, 0, spk)
	  mergeUnsigned += Wallet.createRawTransaction(txId, 0, ad2, 50)
    }
    var mergedTx = MergeTransactions.mergeUnsigned(mergeUnsigned)
    var mergedSigned = ""
    for(i <- 0 to transactionsToSign - 1){
      var info = signInfo(i)
      mergedSigned += Wallet.getHexSigned(Wallet.signrawtransaction(mergedTx, info.txId , 0, info.spk )) + "\n"
    }
    var finalTx = MergeTransactions.mergeSigned(mergedSigned)
    println(finalTx)
    Wallet.sendTransaction(finalTx)
  }
  
  def test3(value: Long) {
    var mergeUnsigned = ""
    var signInfo = new ArrayBuffer[SignInfo]
    for(i<- 0 to 1){
      var ad1 = Wallet.generateAddress
	  var txId = Wallet.giveAddressBTC(ad1, value)
	  var decoded = Wallet.getRawTransaction(txId)
	  var spk = Wallet.getScriptPubKey(decoded)
	  var ad2 = Wallet.generateAddress
	  signInfo += new SignInfo(txId, 0, spk)
	  mergeUnsigned += Wallet.createRawTransaction(txId, 0, ad2, value)
    }
    var mergedTx = MergeTransactions.mergeUnsigned(mergeUnsigned)
    var mergedSigned = ""
    for(i <- 0 to 1){
      var info = signInfo(i)
      mergedSigned += Wallet.getHexSigned(Wallet.signrawtransaction(mergedTx, info.txId , 0, info.spk )) + "\n"
    }
    var finalTx = MergeTransactions.mergeSigned(mergedSigned)
    println(finalTx)
  } 
}