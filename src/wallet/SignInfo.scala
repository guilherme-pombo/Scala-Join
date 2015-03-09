package wallet

//This is a class to help with testing
//It simply stores the information required to sign a transaction
class SignInfo(_txId : String, _vout : Int, _scriptPubKey : String) {
  
  var txId = _txId
  var vout = _vout
  var spk = _scriptPubKey
}