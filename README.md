# Documentation
*Challenge Task: Group 1*

This repository contains one part of the challenge task of the lecture *Overlay Networks* at the University of Zurich.
Details can be found on the [official course website](http://www.csg.uzh.ch/csg/en/teaching/fs17/p2p/challenge.html).
The second part of this project is in a separate repository: [the bootstrapping server](https://github.com/Overlay-Networks/bootstrapping-server).

## Prerequisites
- Java v1.8 JDK installed
- Geth >= v1.5.9 installed
- Run geth in the background with the following parameters `geth --rpcapi personal,db,eth,net,web3 --rpc --testnet --light`
 - Blockchain needs to be up to date for the Application to work
 - If you want to use your own Ethereum wallet, have some test ether to pay for gas prices

## Running the application
- You need to install the `maven` dependencies
  - Run `./mvnw install` in the project directory
- Run the application by running `src/main/java/com/uzh/csg/overlaynetworks/Application.java` as a Java application.
- Open `http://localhost:8080`

## Interfaces between Web Front-End and Spring application
- **REST**
  - The communication from the Web Front-End to the Spring application runs over REST. This was decided because REST has a connection between a request and response (compared to WebSockets). E.g. a message sent to the Spring application can receive a message ID from the Spring application.
  - You can find the Swagger REST documentation on http://localhost:8080/swagger-ui.html
- **WebSockets**
  - WebSockets are used to communicate from the Spring application to the Web Front-End. They are used, because they allow to immediately send data from the Spring application to the Web Front-End (without request)
  - There are 3 interfaces with WebSockets:
    - Send a message to the Web Front-End (message from another peer)
    - Send the notary confirmation to the Web Front-End (notary via Ethereum)
    - Update the contact online status in the Web Front-End.

## Smart Contract
- The application accesses a smart contract on the Ethereum Test Network Ropsten(Revival). The contract written in solidity is located in the `Ethereum` folder.
  - The contract saves the sender's address, the receipients address (optional), the message to be saved and the timestamp of the block where the message is saved.
  - It is only possible to save one message per identifier, such that nobody can overwrite a message that has been saved. -
  - The MessageRegistry can be found under the following address: `0xCDC2c9b31A414F8b7cd719C250ea6c650f18eb22`
  - The smart contract was wrapped with the Web3j wrapper. The wrapped contract can be found under `com.uzh.csg.overlaynetworks.wrappers`. The `MessageRegistry.java` is used to access the contract on the blockchain and provides access to its functions. Do not change the automatically generated code.

## Web3j integration
[Web3j](https://github.com/web3j/web3j) is integrated as a Maven dependency and as integration of everything related to Ethereum.

In order to sucessfully use web3j and its JSON-RPC capabilties, you need to run geth in the background.

Either start geth with the following parameters: `geth --rpcapi personal,db,eth,net,web3 --rpc --testnet --light`. Or run this bash script `challenge-task/Ethereum/start_geth_light.sh` after giving it run permissions `chmod +X start_geth_light.sh`.

Now you can test your connection using our JUnit tests, or simply visit `localhost:8545` in your web browser and see if you get a JSON response back.

### Examples
The sub-package `challenge-task/src/main/java/com/uzh/csg/overlaynetworks/web3j/examples` contains various code Examples.
These examples were used in combination with JUnit tests in `challenge-task/src/test/java/` in order to test various Web3j functionalities, *e.g.* the `GetGethVersionExampleTest` tests the JSON-RPC connection to the geth node running on port `localhost:8545`.

## Web3j
In `challenge-task/src/main/java/com/uzh/csg/overlaynetworks/web3j/` are the implementations of the following classes.

#### AsyncHelper
This helper class is an abstract implementation of AsyncResults for usage with JSON-RPC. Reusing this class made it easier to write tests.

#### CredentialsHelper
This helper class handles everything related to credentials and wallets. It basically loads the wallets located in `challenge-task/wallet/`. It could be rewritten to load.

Wallets located within `wallet` already have funds so there's no need to provide testnet funds by your own for testing purposes (Unless someone already stole all those precious testnet coins).

#### MessageRegistryHelper
The main smart contract called *MessageRegistry* can be instantiated using the *MessageRegistryHelper*.

#### MessageService
The MessageService is the main class used by the application. As the method names imply, it can either be checked if a message `isInBlockchain` or to it's possible to`writeToBlockchain`.  
