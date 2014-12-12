object RandomTransactions {

	// Random generator
	val random = new scala.util.Random
 
	// Generate a random string of length n from the given alphabet
	def randomString(alphabet: String)(n: Int): String = 
	Stream.continually(random.nextInt(alphabet.size)).map(alphabet).take(n).mkString
	
	// Generate a random alphabnumeric string of length n
	def randomHexString(n: Int) = 
	randomString("0123456789abcdef")(n)
	
	def generateNTransactions(n : Int) : String = {
	  var ret = ""
	  for(i <- 1 to n){
	    ret += randomTransaction + "\n"
	  }
	  ret
	}
	
	def randomTransaction = {
	  var ver = "01000000"
	  var in = "01"
	  var prevHash = randomHexString(64)
	  var prevIndex = randomHexString(8)
	  var tmplength = (random.nextInt(70) + 23) //scriptLength is at least 23
	  var scriptLength = tmplength.toHexString
	  var script = randomHexString(tmplength*2)
	  var sequenceNumber = "ffffffff"
	  var out = "01" //one output
	  var value = randomHexString(16)
	  var tmplength2 = generateScriptPubKeyLength
	  var scriptPubKeyLength = tmplength2.toHexString
	  var scriptPubKey = randomHexString(tmplength2*2)
	  var lockTime = "00000000" //if it is a closed transaction it is always 0
	  ver + in + prevHash + prevIndex + scriptLength + script + sequenceNumber+ out + value +
	  scriptPubKeyLength + scriptPubKey + lockTime
	}
	
	//generate the most common Lengths of a script Pub Key and a random one
	def generateScriptPubKeyLength : Int = {
	  var seed = random.nextInt(5)
	  if(seed == 0) 67
	  if(seed == 1) 66
	  if(seed == 2) 25
	  if(seed == 3) 20
	  else random.nextInt(100) + 17
	}
}