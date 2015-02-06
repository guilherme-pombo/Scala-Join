# Scala-Join
Scala implementation of the Coinjoin and MixCoin protocols

To use Scala Join simply:
  1- Create any number of Bitcoin unsigned transactions from a number of participants.
  2- Use CoinJoin/MergeTransactions.scala's mergeUnsigned function to merge all the transactions
  3- Get the result of the merging as an unsigned hexadecimal escrow transaction
  4- Send the escrow transaction to all envolved participants, so they can sign it
  5- Get the signed transaction from each party
  6- Merge all signed transactions using CoinJoin/MergeTransactions.scala's mergeSigned function
  7- The output should be a signed escrow transaction that can be sent to the Bitcoin network
