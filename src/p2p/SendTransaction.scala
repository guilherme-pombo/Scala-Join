package p2p
import scala.sys.process._

object SendTransaction {
	
  def p(s : String) = scala.sys.process.stringToProcess("cmd /C " + s)
  def v(s: String) = scala.sys.process.stringToProcess("cmd /C " + s).!!
  
  def sendSignedJointTransaction(hexTx : String) = {
    p("bitcoin-cli -regtest sendrawtransaction " + hexTx)
    //for RegTest, since we are not actually connecting to the p2p network
    p("bitcoin-cli -regtest setgenerate true 1")
  }
  
}