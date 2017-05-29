#!/bin/bash
echo "Starting geth --rpcapi personal,db,eth,net,web3 --rpc --testnet --light";
geth --rpcapi personal,db,eth,net,web3 --rpc --testnet --light console 2>>geth.log
echo "Done"
