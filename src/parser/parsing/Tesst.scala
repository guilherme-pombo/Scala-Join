package parser.parsing
object Tesst {
	def main(args: Array[String]) {
      var p = new MemoryParser("C:/Users/Pombo/AppData/Roaming/Bitcoin/regtest/blocks/blk00000.dat");
      var blocks = p.parseFile
      for(b <- blocks){
        var txs = b.getTransactions
        for(t <- txs){
          print(t.getString)
        }
      }
    }
}