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

## Spring application & Web Front-End
- There is a spring application for every client. This application provides a web front-end on port `:8080`.
- The data of a user is stored in the local storage of the client in the browser (messages, contacts, username, etc.)
- When the user authenticates (gives a username) a new session in the spring application is created. This session keeps the user data (contacts, username)
- When the communication abrupts (websocket connection loss), the server deletes this session and logs out.
- When a user revisits the website, all the stored data is loaded and the user is automatically logged in again (contacts, messages etc. are loaded).

### Architecture
- The spring application works as an intermediate between the web front-end and the smart contract and p2p.
- The Web Front-End is running on Vue.js and Vue-resource for the REST communication. Stomp and Sock.js are used for the websocket communication of the Spring application. Sock.js is used for the communication and Stomp is a messaging protocol which allows to send JSON to the Web Front-End (directly from Spring Websocket).
- The front-end dependencies were added as a Maven dependency for simplicity reasons: A second build process (e.g. with Webpack or Browserify) seemed like an unnecessary complicating factor. `mvn install` now injects all the dependencies accordingly.

### Communication (Interfaces)
**REST**
  - The communication from the Web Front-End to the Spring application runs over REST. This was decided because REST has a connection between a request and response (compared to WebSockets). E.g. a message sent to the Spring application can receive a message ID from the Spring application.
  - You can find the Swagger REST documentation on http://localhost:8080/swagger-ui.html

**WebSockets**
  - WebSockets are used to communicate from the Spring application to the Web Front-End. They are used, because they allow to immediately send data from the Spring application to the Web Front-End (without request)
  - If the communication abrupts, a disconnect listener logs the user out.
  - There are 3 interfaces with WebSockets:
    - Send a message to the Web Front-End (message from another peer)
    - Send the notary confirmation to the Web Front-End (notary via Ethereum)
    - Update the contact online status in the Web Front-End.

## P2P Communication

### General structure
Communication of Spring controller with P2P network is realized via P2PService class. P2PService class stores a reference to single instance of P2PClient object and implements P2PClientDelegate interface. P2PClient, in turn, performs all P2P-related work and outputs results to P2PService class via P2PClientDelegate. In this setting, P2PService class acts as an interface to P2P subsystem and the actual P2P interaction is done inside P2PClient.
P2PService and P2PClient are communicating in a following way:
- P2PService gets request from REST controller and, in turn, calls appropriate method in P2PClient
- P2PClient performs request asynchronously and delivers result back to P2PService via delegation pattern
- P2PService sends a direct websockets response to front-end or responds returns value to REST controller based on request type

For example, message sending process would look like:
- P2PService class is requested to send message, so P2PService would call sendMessage() method of P2PClient
- P2PClient would asynchronously perform message sending, abstracting away all underlying details, and upon completion would notify P2PService class via P2PClientDelegate method didSendMessage() indicating whether message was successfully sent or there was an error during send process.
- P2PService, once received response, would send websocket response to the front-end.

### P2P Implementation
As mentioned earlier, all P2P communication and interaction is done inside P2PClient class. Each instance of P2PClient class is associated with a single PeerDHT object. Below we will be discussing which functionality P2PClient is capable of providing and how does it achieve that.

#### start
Once instance of P2P client has been created, start() method is called which performs following tasks:
- Finds a free port and creates a socket
- Performs a bootstrapping procedure to the dedicated bootstrapping server
- Upon successful bootstrapping, stores client credentials such as username, IP address, port into the DHT
- Declares and set's up routine how to handle to direct messages

#### sendMessage
To send messages to other peers in P2P system, we use 'sendDirect' method offered by TomP2P framework.
Once we receive username of message receiver from the controller, we look up a DHT entry containing a key corresponding to the receiver's username which contains necessary information to reach receiver such as peer address, IP address and port.
Each message is Data object which contains not only the actual message text, but also metadata that is mandatory on receiving side such as indicator whether message should be signed or not, message ID and sender username. One may argue that sender's username can be reverse-looked up in DHT using peer address, however, each DHT lookup is costly, so we have chosen to transmit username of the sender directly in the message.

Once P2PClient receives message from another client, it responds with an ACK message to the sender and informs P2PService of new incoming message. 

#### updateContactOnlineStatus
At periodic time intervals, user's contact list is sent to the back-end to check if they are online and offline and update their status correspondingly. To find out whether contact is online (i.e. his entry is present in DHT) or offline, we firstly check using FutureGet whether his credentials are in DHT. If the contact shutted down appropriately, he has cleared his entry into DHT, so we can easily determine the contact's status. To handle the case of inappropriate termination, contact's entry in DHT has a TTL. When contact is pushing his entry into the DHT, it sets a TTL on the Data object and contact, if online, keeps re-publising his data into DHT using JobScheduler. So even if contact exited system inapproriately, his entry would remain in DHT for longest of TTL, which is currently set to 60 seconds.

#### shutdown
Once user closes the browser or clicks 'log out' button, P2PService calls 'shutdown()' method of P2PClient which removes peer credentials from DHT and announces shutdown in P2P system using announceShutdown().

### Fault Tolerance
To handle unstable nature of P2P communication, we use direct replication mechanism to replicate peer data in DHT. Each P2PClient uses JobScheduler to push an entry containing his information in DHT. JobScheduler pushes the contact's entry into the DHT every TTL/2 seconds (currently, it is 60/2 = 30 seconds). Not only it keeps data about the contact fresh, but also creates multiple replicas of the data, so even if node is goes down, the data can still be accessed.

### Bootstrapping Server
We have separated a bootstrapping server into separate project since it has to be run separately and independently from the main application. In essence, bootstrapping server is an instance of PeerDHT object which automatically finds a free port and opens a sockets and starts.
For P2PClient objects to be able to bootstrap successfully, bootstrapping server port and IP are hardcoded inside the class, so everytime bootstrapping server is restarted, constants inside P2PClient have to modified as well.

## Notary Service
The notary service persists chosen messages using a smart contract into the Ethereum block chain.

### Smart Contract
- The application accesses a smart contract on the Ethereum Test Network Ropsten(Revival). The contract written in solidity is located in the `Ethereum` folder as `MessageRegistry.sol`. Both the contract as a binary, as well as its interface are to be found in the same folder saved as `bin` and `abi` respectively. 
- The MessageRegistry can be found under the following address: `0xCDC2c9b31A414F8b7cd719C250ea6c650f18eb22`
- The contracts method ´save´, saves the sender's address, the receipients address (optional), the message to be saved and the timestamp of the block where the message is saved. The identifier cannot be empty or used multiple times. Using this method spends Gas.
  - Alternatively, a hash of the message could be saved into the blockchain instead of the message. In a second step this could be implemented or even in a separate application that handles a separate business case.
  - All of these informations are saved into a map separately. The identifier of the message is used to receive all information out of the maps. As solidity uses UInts, any normal integers need to be converted first. The same applies for the sender and receiver addreses, which are saved as type "address". This is done automatically in our application, but has to be considered if the contract should be accessed by another application.
  - It is only possible to save one message per identifier, such that nobody can overwrite a message that has been saved. This assures that the message will be saved as long as the blockchain exists.
- The method ´getMessage´ with an identifier as parameter will return the previously stored information. Using this method does not spend Gas.
- The MessageRegistry contains a function isDeployed() that is used to test whether the contract is reachable by an application. It simply returns true if it can be accessed. Using this method does not spend Gas.
- The smart contract was wrapped with the Web3j wrapper. The wrapped contract can be found under `com.uzh.csg.overlaynetworks.wrappers`. The `MessageRegistry.java` is used to access the contract on the blockchain and provides access to its functions. Do not change the automatically generated code.
  - The Web3j wrapper is still included for the time being, such that a new contract could easily be deployed and wrapped.
- The contract is deployed such that no one, not even the owner, can delete it from the blockchain. The same applies to any information saved in it. All information is public. ***Do not send*** any information to the blockchain that is private or could become problematic in the future.


### Web3j integration
[Web3j](https://github.com/web3j/web3j) is integrated as a Maven dependency and as integration of everything related to Ethereum.

In order to sucessfully use web3j and its JSON-RPC capabilties, you need to run geth in the background.

Either start geth with the following parameters: `geth --rpcapi personal,db,eth,net,web3 --rpc --testnet --light`. Or run this bash script `challenge-task/Ethereum/start_geth_light.sh` after giving it run permissions `chmod +X start_geth_light.sh`.

Now you can test your connection using our JUnit tests, or simply visit `localhost:8545` in your web browser and see if you get a JSON response back.

#### Examples
The sub-package `challenge-task/src/main/java/com/uzh/csg/overlaynetworks/web3j/examples` contains various code Examples.
These examples were used in combination with JUnit tests in `challenge-task/src/test/java/` in order to test various Web3j functionalities, *e.g.* the `GetGethVersionExampleTest` tests the JSON-RPC connection to the geth node running on port `localhost:8545`.

### Web3j
In `challenge-task/src/main/java/com/uzh/csg/overlaynetworks/web3j/` are the implementations of the following classes.

#### AsyncHelper
This helper class is an abstract implementation of AsyncResults for usage with JSON-RPC. Reusing this class made it easier to write tests.

#### CredentialsHelper
This helper class handles everything related to credentials and wallets. It basically loads the wallets located in `challenge-task/wallet/`. It could be rewritten to load any users wallet, such that his own public key will be stored as sender in the blockchain.

Wallets located within `wallet` already have funds so there's no need to provide testnet funds by your own for testing purposes (Unless someone already stole all those precious testnet coins). 

#### MessageRegistryHelper
The main smart contract called *MessageRegistry* can be instantiated using the *MessageRegistryHelper*.

#### MessageService
The MessageService is the main class used by the application. As the method names imply, it can either be checked if a message `isInBlockchain` or to it's possible to`writeToBlockchain`. Only if the notary is ticked, the message will be sent to the blockchain via "writeToBlockchain'. The receiving party will get the information that this message should be stored in the blockchain, and check via `isInBlockchain`.

# Presentation
Have a look at [the presentation](http://slides.com/sebschrepfer/overlay-networks).
