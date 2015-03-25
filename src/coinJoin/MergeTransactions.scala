package coinJoin

import scala.collection.mutable.ArrayBuffer
import java.util.Scanner
import transaction.TransactionInput
import transaction.Transaction
import transaction.TransactionOutput
import transaction.TransactionToHex
import transaction.TransactionParser

object MergeTransactions {
	
	//Takes as input a string with various unsigned transactions
    //returns the transactions merged in an escrow transaction
    //returns hexadecimal string
	def mergeUnsigned(input : String) : String = {
	  var transactions = stringToTransactions(input)
	  var unsigned = mergeUnsignedTransactions(transactions)
	  if(unsigned == null){
	    println("Merging failed") //reasons why it failed are printed out by mergeUnsignedTransactions
	    ""
	  }
	  else{
	    TransactionToHex.convertToHex(unsigned)
	  }
	}
	
	//Takes as input a string with various Signed transactions
    //returns the transactions merged in an escrow transaction
    //returns hexadecimal string
	def mergeSigned(input : String) : String= {
	  var transactions = stringToTransactions(input)
	  var signed = mergeSignedTransactions(transactions)
	  if(signed == null){
	    println("Merging failed") //reasons why it failed are printed out by mergeUnsignedTransactions
	    ""
	  }
	  else{
	    TransactionToHex.convertToHex(signed)
	  }
	}
	
	
	//Check if two inputs are the same
	def areEqualInputs(i1 : TransactionInput, i2 : TransactionInput) = 
	  i1.getComparableString.equals(i2.getComparableString)
	
	//Check if two outputs are the same
	def areEqualOutputs(o1 : TransactionOutput, o2 : TransactionOutput) = 
	  o1.getComparableString.equals(o2.getComparableString)
	  
	//merge raw transactions sent in from the users
	//send back for signing and approval
	def mergeUnsignedTransactions(transactions : ArrayBuffer[Transaction]) : Transaction = {
	  //empty list return null
	  if(transactions.length==0){
	    println("No transactions inputted")
	    return null
	    }
	  
	  //Check if the number of inputs and outputs does not exceed the limit
	  var outputCount = 0
	  var inputCount = 0
	  for(t<- transactions){
	    outputCount += t.getOutputs.length
	    inputCount += t.getInputs.length
	  }
	  
	  //use these to check for correctness of other transactions in the array
	  var checkVersion = transactions(0).getTransactionVersion
	  var checkTime = transactions(0).getTransactionLockTime
	  //this transaction will have the collated inputs and outputs
	  var finalTx = new Transaction(checkVersion, 0, new ArrayBuffer[TransactionInput](),
	     0, new ArrayBuffer[TransactionOutput](), checkTime)
	  for(t <- transactions){
	    if(t.getTransactionLockTime != checkTime){
	      println("Incompatible lock times in inputted transactions")
	      null //can't merge transactions
	    }
	    if(t.getTransactionVersion!=checkVersion){
	      println("Incompatible transaction version in inputted transactions")
	      null //can't merge transactions
	    }
	    //Collate outputs
	    //Duplicate outputs are allowed
	    //Duplicate inputs are not allowed
	    for(out <- t.getOutputs){
	      var alreadyExists = false
	      for(o <- finalTx.getOutputs){
	    	//perform deep array comparison
	        if(out.getScript.deep ==  o.getScript.deep){alreadyExists = true}
	      }
	      if(!alreadyExists){
	        finalTx.addOutput(out)
	        finalTx.addOutputCount(1)
	      }
	    }
	    //Collate inputs
	    for(in <- t.getInputs){
	      var alreadyExists = false
	      for(i <- finalTx.getInputs){
	        if(areEqualInputs(in,i)){alreadyExists = true}
	      }
	      if(!alreadyExists){
	        finalTx.addInput(in)
	        finalTx.addInputCount(1)}
	      //duplicate input
	      else{
	        println("Duplicate inputs found, invalid transaction to merge")
	        return null
	      }
	    }
	  }
	  //RIGHT NOW I AM NOT RANDOMIZING TO MAKE DEBUGGING EASIER
	  //NEED TO:
	  //RANDOMIZE COLLATED INPUTS AND OUTPUTS
	  finalTx
	}
	
	//verifies if the modulo of the signatures is the same for all transactions,
	//meaning that all users signed the same transaction, each with his own
	//private key.
	def mergeSignedTransactions (transactions : ArrayBuffer[Transaction]) : Transaction = {
	  //empty list return null
	  if(transactions.length==0){
	    println("No transactions inputted")
	    return null
	  }
	  //this transaction will have the collated scriptSigs
	  var finalTx = transactions(0) //first transaction serves as checker
	  var checkVersion = finalTx.getTransactionVersion
	  var checkTime = finalTx.getTransactionLockTime
	  for(i<- 0 to transactions.length-2){
	    var tx1 = transactions(i)
	    var tx2 = transactions(i+1)
	    if(tx1.getTransactionLockTime != checkTime){
	      println("Incompatible lock times in inputted transactions")
	      null //can't merge transactions
	    }
	    if(tx1.getTransactionVersion!=checkVersion){
	      println("Incompatible transaction version in inputted transactions")
	      null //can't merge transactions
	    }
	    //Check outputs
	    //if different number of outputs don't even bother checking
	    if(tx1.getOutputs.length != tx2.getOutputs.length){
	      println("Incompatible outputs on transactions " + i + " and " +  (i+1))
	      return null
	    }
	    var outs1 = tx1.getOutputs
	    var outs2 = tx2.getOutputs
	    //VERIFY IF ALL OUTPUTS MATCH
	    for(j <- 0 to outs1.length -1){
	      var out1 = outs1(j)
	      var out2 = outs2(j)
          if(!areEqualOutputs(out1, out2)){
	         println("Outputs number: " + j + " do not match")
	         return null //can't merge transactions
	      }
        }
        //Check inputs
	    //if different number of inputs don't even bother checking
	    if(tx1.getInputs.length != tx2.getInputs.length){
	      println("Incompatible input on transactions " + i + " and " +  (i+1))
	      return null
	    }
	    var ins1 = tx1.getInputs
	    var ins2 = tx2.getInputs
	    //VERIFY IF ALL INPUTS MATCH
	    for(j <- 0 to ins1.length -1){
	      var in1 = ins1(j)
	      var in2 = ins2(j)
          if(!areEqualInputs(in1, in2)){
	         println("Inputs number: " + j + " do not match")
	         return null //can't merge transactions
	      }
	      //COLLATE ALL SCRIPTSIGS
//          else{
//            //if there is an input script copy it over to the next input
//	    	if(in1.getScript.length>0){
//	    	  finalTx.getInputs(j).addScript(in1.getScript)
//	    	}
//	        //in the last iteration add the second input's scriptSig as well
//	        if(i == transactions.length-2){
//	          if(in2.getScript.length>0){
//	    	  finalTx.getInputs(j).addScript(in2.getScript)
//	          }
//	        }
//	      }
        }
	  }
	  finalTx
	}
	
	//take in as an input several transactions, one per line, inputed as hex strings
	//returns an array of the parsed transactions
	def stringToTransactions(input : String) : ArrayBuffer[Transaction] = {
		var read = new Scanner(input)
		var transactions =  new ArrayBuffer[Transaction](0)
		while (read.hasNextLine) {
			var line = read.nextLine();
			var parser = new TransactionParser(line)
			transactions += parser.parseTransaction
		}
		read.close
		transactions
	}
	
}