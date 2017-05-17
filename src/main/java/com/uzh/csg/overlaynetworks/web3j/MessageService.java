package com.uzh.csg.overlaynetworks.web3j;
import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.Test;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.uzh.csg.overlaynetworks.domain.dto.Message;
import com.uzh.csg.overlaynetworks.web3j.IsContractDeployedExample;
import com.uzh.csg.overlaynetworks.web3j.MessageRegistryHelper;
import com.uzh.csg.overlaynetworks.wrappers.MessageRegistry;
import java.util.logging.*;


public class MessageService extends Web3jService{
	
	final String contractAddress = "0xCDC2c9b31A414F8b7cd719C250ea6c650f18eb22";
    final MessageRegistry messageRegistry = MessageRegistryHelper.getMessageRegistry(this.getWeb3j(), contractAddress);  
    
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
    		return false;
    	}
    }
    public void writeToBlockchain(Message message, long messageId) throws InterruptedException, ExecutionException {
        final String idString = String.valueOf(messageId);
        final BigInteger myBigInt = new BigInteger(idString); 
        final Uint256 identifier = new Uint256(myBigInt);
        final Utf8String MyMessage = new Utf8String(message.getMessage());
        final Address receiver = new Address("0x5BbB245A661C4C112AA0e848A2BB007Ba3e9B628");
  
        TransactionReceipt transactionReceipt = messageRegistry.save(MyMessage, receiver, identifier).get();
        
        
    }
}