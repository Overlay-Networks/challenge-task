# Challenge Task: Group 1
The challenge task in the lecture Overlay Networks at the University of Zurich.

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
