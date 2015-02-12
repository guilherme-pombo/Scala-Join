package clientserver

//use this class to store the client's information

class ClientInfo( _btcTotal : Double, _change : Double, _hexTx: String, _unsigned : Boolean,
    _mergedUnsigned: String, _signed: Boolean, _mergedSigned: String) {
	
  val btcTotal = _btcTotal //amount of Bitcoin that the user is willing to mix
  val change = _change //amount of change user is willing to give away
  val hexTx= _hexTx
  val unsigned = _unsigned
  val mergedUnsigned = _mergedUnsigned
  val signed = _signed
  val mergedSigned = _mergedSigned
  
}