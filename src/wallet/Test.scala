package wallet
import coinJoin.MergeTransactions
import scala.collection.mutable.ArrayBuffer

object Test {

  def main(args: Array[String]) {
    test1Send
  }
  
  //A few sample tests bellow
  
  //In this case we do a simple regular raw transaction, to add noise to the blockChain
  //No CoinJoin
  //Just an example to illustrate how a simple non-joint transaction works
  def regularTransaction{
    //Generate an address
    var sender = Wallet.generateAddress
    println(sender)
    //Give it 0.01 Bitcoins
    var txId = Wallet.giveAddressBTC(sender, 0.01)
    //Confirm given bitcoins by generating a block
    Wallet.confirmTransaction
    //Get txId and ScriptPubKey
    var decoded = Wallet.getRawTransaction(txId)
    var spk = Wallet.getScriptPubKey(decoded)
    //Generate an address for the receiver
    var receiver = Wallet.generateAddress
    println(receiver)
    //Create a raw transaction
    var rawTx = Wallet.createRawTransaction(txId, 0, receiver, 0.01)
    //Sign raw transaction
    var signedTx = Wallet.getHexSigned(Wallet.signrawtransaction(rawTx, txId , 0, spk ))
    //Send signed raw transaction
    Wallet.sendTransaction(signedTx)
    //Confirm most recent transaction
    Wallet.confirmTransaction
  }
  
  //Test1: Simplest test. Merges two simple transactions, signs the merged
  //transaction and then merges the signed transactions.
  //Both transactions have the same value
  def test1 {
    var mergeUnsigned = ""
    var signInfo = new ArrayBuffer[SignInfo]
    for(i<- 0 to 1){
      var sender = Wallet.generateAddress
	  var txId = Wallet.giveAddressBTC(sender, 0.01)
	  Wallet.confirmTransaction
	  var decoded = Wallet.getRawTransaction(txId)
	  var spk = Wallet.getScriptPubKey(decoded)
	  var receiver = Wallet.generateAddress
	  signInfo += new SignInfo(txId, 0, spk)
	  mergeUnsigned += Wallet.createRawTransaction(txId, 0, receiver, 0.01)
    }
    var mergedTx = MergeTransactions.mergeUnsigned(mergeUnsigned)
    var mergedSigned = ""
    for(i <- 0 to 1){
      var info = signInfo(i)
      mergedSigned += Wallet.getHexSigned(Wallet.signrawtransaction(mergedTx, info.txId , 0, info.spk )) + "\n"
    }
    var finalTx = MergeTransactions.mergeSigned(mergedSigned)
    println("FinalTx: " + finalTx)
  }
  
  //Same as test1 but sends the finalTx
  def test1Send {
    var mergeUnsigned = ""
    var signInfo = new ArrayBuffer[SignInfo]
    for(i<- 0 to 1){
      var sender = Wallet.generateAddress
	  var txId = Wallet.giveAddressBTC(sender, 0.01)
	  println("Sender: " + sender)
	  Wallet.confirmTransaction
	  var decoded = Wallet.getRawTransaction(txId)
	  var spk = Wallet.getScriptPubKey(decoded)
	  var receiver = Wallet.generateAddress
	  println(receiver)
	  signInfo += new SignInfo(txId, 0, spk)
	  mergeUnsigned += Wallet.createRawTransaction(txId, 0, receiver, 0.01)
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
    //Confirm most recent transaction
    Wallet.confirmTransaction
  } 
  
  //Test2: 2nd simplest test. Merges N simple transactions, signs the merged
  //transaction and then merges the signed transactions.
  //All transactions have the same value
  def test2(transactionsToSign : Int) {
    var mergeUnsigned = ""
    var signInfo = new ArrayBuffer[SignInfo]
    for(i<- 0 to transactionsToSign - 1){
      var sender = Wallet.generateAddress
	  var txId = Wallet.giveAddressBTC(sender, 0.01)
	  Wallet.confirmTransaction
	  var decoded = Wallet.getRawTransaction(txId)
	  var spk = Wallet.getScriptPubKey(decoded)
	  var receiver = Wallet.generateAddress
	  signInfo += new SignInfo(txId, 0, spk)
	  mergeUnsigned += Wallet.createRawTransaction(txId, 0, receiver, 0.01)
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
      var sender = Wallet.generateAddress
	  var txId = Wallet.giveAddressBTC(sender, 0.01)
	  Wallet.confirmTransaction
	  var decoded = Wallet.getRawTransaction(txId)
	  var spk = Wallet.getScriptPubKey(decoded)
	  var receiver = Wallet.generateAddress
	  signInfo += new SignInfo(txId, 0, spk)
	  mergeUnsigned += Wallet.createRawTransaction(txId, 0, receiver, 0.01)
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
    //Confirm most recent transaction
    Wallet.confirmTransaction
  }
  
  def test3(value: Long) {
    var mergeUnsigned = ""
    var signInfo = new ArrayBuffer[SignInfo]
    for(i<- 0 to 1){
      var sender = Wallet.generateAddress
	  var txId = Wallet.giveAddressBTC(sender, value)
	  Wallet.confirmTransaction
	  var decoded = Wallet.getRawTransaction(txId)
	  var spk = Wallet.getScriptPubKey(decoded)
	  var receiver = Wallet.generateAddress
	  signInfo += new SignInfo(txId, 0, spk)
	  mergeUnsigned += Wallet.createRawTransaction(txId, 0, receiver, value)
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