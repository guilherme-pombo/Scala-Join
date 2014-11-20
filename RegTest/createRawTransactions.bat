:: Go to the deamon folder
cd C:\Program Files\Bitcoin\daemon
:: Create private blockchain
bitcoin-cli -regtest setgenerate true 101
:: CREATE PUBLIC KEY
set ADDRESS=bitcoin-cli -regtest getnewaddress
:: SEND COINS TO THAT ADDRESS FROM BLOCKCHAIN
bitcoin-cli -regtest sendtoaddress "%ADDRESS%" 10.00
:: CONFIRM RECENT TRANSACTION
bitcoin-cli -regtest setgenerate true 1
:: DISPLAY CONFIRMED TRANSACTIONS
bitcoin-cli -regtest listunspent