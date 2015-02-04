import java.io._
import java.security._
import java.nio.file.{Paths, Files}
import java.nio.charset.StandardCharsets

object Warranty {

  //Takes
  
  var pair = generateKeyPair
  var priv = pair.getPrivate
  
  //Take a hexadecimal raw transaction and sign it with the server's private key
  def getWarranty(tx : String) : Array[Byte] = {
    var dsa = Signature.getInstance("SHA1withDSA", "SUN");
    dsa.initSign(priv);
    var bytes = tx.toByte
    Files.write(Paths.get("transaction.txt"), tx.getBytes(StandardCharsets.UTF_8)) //replace existing file, every time
    var fis = new FileInputStream("transaction.txt")//need to change this
    var bufin = new BufferedInputStream(fis)
    var buffer = new Array[Byte](1024)
    var len = bufin.read(buffer)
    while (len >= 0) {
      dsa.update(buffer, 0, len)
      len = bufin.read(buffer)
    }
    bufin.close()
    var signature = dsa.sign() //get the signature
    //write the signature to a file in the server
    var writer = new FileOutputStream("signature"); //need to create different names for different users
	writer.write(signature)
	writer.close()
	signature //return the signature
  }
  
  
  private def generateKeyPair() : KeyPair = {
    var keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
    var random = SecureRandom.getInstance("SHA1PRNG", "SUN");
    keyGen.initialize(1024, random);
    var pair = keyGen.generateKeyPair();
    var key = pair.getPublic().getEncoded();
	var writer = new FileOutputStream("ServerPublicKey"); //write the server's public key to a file
	writer.write(key);
	writer.close();
    pair //return public, private key pair
  }
}