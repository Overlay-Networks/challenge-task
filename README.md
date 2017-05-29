# Documentation 
*Challenge Task: Group 1*

This repository contains one part of the challenge task of the lecture *Overlay Networks* at the University of Zurich.
Details can be found on the [official course website](http://www.csg.uzh.ch/csg/en/teaching/fs17/p2p/challenge.html). 
The second part of this project is in a separate repository: [the bootstrapping server](https://github.com/Overlay-Networks/bootstrapping-server).


## Prerequisites
- Java 1.8 JDK installed
- Geth >= 1.59 installed
- Run geth in the background with the following parameters `geth --rpcapi personal,db,eth,net,web3 --rpc --testnet --light`
 - Blockchain needs to be up to date for the Application to work
 - If you want to use your own Ethereum wallet, have some test ether to pay for gas prices 

 
## Running the application
- You need to install the `maven` dependencies
  - Run `./mvnw install` in the project directory

- Run the application by running `src/main/java/com/uzh/csg/overlaynetworks/Application.java` as a Java application.
- Open `http://localhost:8080`

## Smart Contract
- The application accesses a smart contract on the ethereum Test Network Ropsten(Revival). The contract written in solidity is to be found in the `Ethereum` folder. 
  - The contract saves the sender's address, the receipients address (optional), the message to be saved and the timestamp of the block where the message is saved. 
  - It is only possible to save one message per identifier, such that nobody can overwrite a message that has been saved. -
  - The MessageRegistry can be found under the following address: `0xCDC2c9b31A414F8b7cd719C250ea6c650f18eb22`
  - The smart contract was wrapped with the Web3j wrapper. The wrapped contract can be found under `com.uzh.csg.overlaynetworks.wrappers`. The `MessageRegistry.java` is used to access the contract on the blockchain and provides access to its functions. Do not change the automatically generated code. 
 
## API Documentation
- You can find the Swagger REST documentation on http://localhost:8080/swagger-ui.html

## Web3j integration
[Web3j](https://github.com/web3j/web3j) is integrated as a Maven dependency and as integration of everything related to Ethereum.

In order to sucessfully use web3j and its JSON-RPC capabilties, you need to run geth in the background.

Either start geth with the following parameters: `geth --rpcapi personal,db,eth,net,web3 --rpc --testnet --light`. Or run this bash script `challenge-task/Ethereum/start_geth_light.sh` after giving it run permissions `chmod +X start_geth_light.sh`.

Now you can test your connection using our JUnit tests, or simply visit `localhost:8545` in your web browser and see if you get a JSON response back.

### Examples
The sub-package `challenge-task/src/main/java/com/uzh/csg/overlaynetworks/web3j/examples` contains various code Examples.
These examples were used in combination with JUnit tests in `challenge-task/src/test/java/` in order to test various Web3j functionalities, *e.g.* the `GetGethVersionExampleTest` tests the JSON-RPC connection to the geth node running on port `localhost:8545`.

## Web3j
In `challenge-task/src/main/java/com/uzh/csg/overlaynetworks/web3j/` are the implementations of **TODO: complete this**.

### AsyncHelper &
This helper class is an abstract implementation of Async JSON-RPC waitForResults methods

### CredentialsHelper
This helper method loads the wallets located in `challenge-task/wallet/`.
**TODO: describe multiple wallet functionality**

### MessageRegistryHelper
The main smart contract *MessageRegistry*
**TODO: complete this**

### MessageService
**TODO: complete this**

### Web3jService
**TODO: complete this**

