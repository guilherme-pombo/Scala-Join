package parsing
object Tesst {
	def main(args: Array[String]) {
      var p = new MemoryParser("C:/Users/Pombo/Desktop/FYP/BlockChain Parser/blk00093.dat");
      var blocks = p.parseFile
      println("Done")
    }
}