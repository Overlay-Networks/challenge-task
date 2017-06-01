package com.uzh.csg.overlaynetworks.web3j;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.uzh.csg.overlaynetworks.domain.dto.Message;
import com.uzh.csg.overlaynetworks.domain.dto.ReceiveNotary;
import com.uzh.csg.overlaynetworks.wrappers.MessageRegistry;

public class MessageService extends Web3jService{

	final String contractAddress = "0xCDC2c9b31A414F8b7cd719C250ea6c650f18eb22";
    final MessageRegistry messageRegistry = MessageRegistryHelper.getMessageRegistry(this.getWeb3j(), contractAddress);

    /**
     * Checks whether a message with a certain identifier is saved in the blockchain
     *
     * @param The messageID, of which the user wants to know if it is saved in the blockchain
     * @return True, if the message exists in the blockchain, false if not
     */

    public boolean isInBlockchain(long messageId) throws InterruptedException, ExecutionException{
    	try {
	    	final String myMessageId = String.valueOf(messageId);
	    	final BigInteger myBigInt = new BigInteger(myMessageId);
	        final Uint256 myIdentifier = new Uint256(myBigInt);

	 		final List<Type> message = messageRegistry.getMessage(myIdentifier).get();

	 		if (message.isEmpty()) {
	 			return false; }
	 		else {
	 			return true;
	 		}

		}
    	catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    }
    /**
     * Saves a message into the blockchain. Converts the long to the needed Uint256 and the String to an Utf8String.
     *
     * @param The message to be saved
     * @param The messageID of the message that should be saved to the blockchain
     */

    public void writeToBlockchain(Message message, long messageId, SimpMessagingTemplate websocket) throws InterruptedException, ExecutionException {
		System.out.println("[ETH]Writing message to blockchain.");

		try {
			final String idString = String.valueOf(messageId);
			final BigInteger myBigInt = new BigInteger(idString);
			final Uint256 identifier = new Uint256(myBigInt);
			final Utf8String MyMessage = new Utf8String(message.getMessage());
			final Address receiver = new Address("0x5BbB245A661C4C112AA0e848A2BB007Ba3e9B628");
			
			
			System.out.println("[ETH]Initiating transaction.");
			System.out.println("[ETH] Message ID: " + idString);
			
			final Future <TransactionReceipt> saveToBlockchain = messageRegistry.save(MyMessage, receiver, identifier);
			ReceiveNotary notary = new ReceiveNotary();
			final TransactionReceipt transactionReceipt = saveToBlockchain.get();
			
			if (!transactionReceipt.getTransactionHash().isEmpty()) {
				notary.setMessageId(messageId);
				websocket.convertAndSend("/topic/receive-notary", notary);
				}
			System.out.println("[ETH-TX:]"+saveToBlockchain.toString());

			// this doesn't work -- blocks since not async
			//TransactionReceipt transactionReceipt = messageRegistry.save(MyMessage, receiver, identifier).get();
			//System.out.println("[ETH-TX:]"+transactionReceipt.getTransactionHash());

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("[ETH]Couldn't write to blockchain! Is geth running?");
		}




    }
}
