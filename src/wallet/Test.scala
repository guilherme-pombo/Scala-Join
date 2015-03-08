package wallet

object Test {

  def main(args: Array[String]) {
	  var ad1 = Wallet.generateAddress
	  var txId = Wallet.giveAddressBTC(ad1, 50)
	  var decoded = Wallet.getRawTransaction(txId)
	  var spk = Wallet.getScriptPubKey(decoded)
	  var ad2 = Wallet.generateAddress
	  var hex = Wallet.createRawTransaction(txId, 0, ad2, 50)
	  var signed = Wallet.signrawtransaction(hex, txId, 0, spk)
	  println(signed)
  }
}