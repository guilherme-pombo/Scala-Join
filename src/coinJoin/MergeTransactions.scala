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
	    //Duplicate outputs are allowed --> Implies same Receiver in two different txs
	    //Duplicate inputs are not allowed --> Implies same money being spent twice
	    for(out1 <- t.getOutputs){
	      var alreadyExists = false
	      for(out2 <- finalTx.getOutputs){
	        if(areEqualOutputs(out1, out2)){
	          alreadyExists = true
	          //increase the value of the finalTx output, because no use in having
	          //same address twice, just send the sum of the values to the address
	          out2.addToValue(out1.getValue)
	        }
	      }
	      if(!alreadyExists){
	        finalTx.addOutput(new TransactionOutput(out1.value, out1.getScriptLength,
	            out1.getScript, out1.getDataArray))
	      }
	    }
	    //Collate inputs
	    for(in1 <- t.getInputs){
	      var alreadyExists = false
	      for(in2 <- finalTx.getInputs){
	        if(areEqualInputs(in1,in2)){alreadyExists = true}
	      }
	      if(!alreadyExists){
	        finalTx.addInput(new TransactionInput(in1.getPrevHash,in1.getIndex,
	    	      in1.getScriptLen, new Array[Short](0), in1.getSeqNum, in1.getDataArray))
	      }
	      //duplicate input
	      else{
	        println("Duplicate inputs found, invalid transaction to merge")
	        return null
	      }
	    }
	  }
	  //Shuffle inputs and outputs using Scala's shuffle
	  util.Random.shuffle(finalTx.getInputs)
	  util.Random.shuffle(finalTx.getOutputs)
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
	  var finalOuts = finalTx.getOutputs //checker transaction's outputs
	  var finalIns = finalTx.getInputs //checker transaction's inputs
	  var checkVersion = finalTx.getTransactionVersion
	  var checkTime = finalTx.getTransactionLockTime
	  for(i<- 0 to transactions.length-1){
	    var check = transactions(i)
	    if(check.getTransactionLockTime != checkTime){
	      println("Incompatible lock times in inputted transactions")
	      null //can't merge transactions
	    }
	    if(check.getTransactionVersion!=checkVersion){
	      println("Incompatible transaction version in inputted transactions")
	      null //can't merge transactions
	    }
	    //Check outputs
	    //if different number of outputs don't even bother checking
	    if(check.getOutputs.length != finalOuts.length){
	      println("Incompatible outputs on transactions " + i)
	      return null
	    }
	    var outs = check.getOutputs
	    //VERIFY IF ALL OUTPUTS MATCH AND ARE IN THE SAME ORDER
	    for(j <- 0 to outs.length -1){
	      var out1 = outs(j)
	      var out2 = finalOuts(j) //final Tx out
          if(!areEqualOutputs(out1, out2)){
	         println("Outputs number: " + j + " does not match")
	         return null //can't merge transactions
	      }
        }
        //Check inputs
	    //if different number of inputs don't even bother checking
	    if(check.getInputs.length != finalIns.length){
	      println("Incompatible input on transactions " + i + " and " +  (i+1))
	      return null
	    }
	    var ins = check.getInputs
	    //VERIFY IF ALL INPUTS MATCH AND ARE IN ORDER
	    for(j <- 0 to ins.length -1){
	      var in1 = ins(j)
	      var in2 = finalIns(j) //Final Tx input
          if(!areEqualInputs(in1, in2)){
	         println("Inputs number: " + j + " do not match")
	         return null //can't merge transactions
	      }
	      //COPY ALL SCRIPTSIGS
          else{
	    	if(in1.getScript.length>0){
	    	  finalTx.getInputs(j) = new TransactionInput(in2.getPrevHash,in2.getIndex,
	    	      in1.getScriptLen, in1.getScript, in2.getSeqNum, in2.getDataArray)
	    	}
	      }
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