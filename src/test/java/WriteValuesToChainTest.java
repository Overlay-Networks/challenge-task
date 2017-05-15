import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.uzh.csg.overlaynetworks.web3j.AsyncHelper;
import com.uzh.csg.overlaynetworks.web3j.IsContractDeployedExample;
import com.uzh.csg.overlaynetworks.web3j.MessageRegistryHelper;
import com.uzh.csg.overlaynetworks.wrappers.MessageRegistry;

public class WriteValuesToChainTest extends AbstractLocalhostTest{

	private static final Logger LOGGER = Logger.getLogger(WriteValuesToChainTest.class.getName() );

    @Test
    public void shouldReturnTrueIfWrittenToBlockchain() throws InterruptedException, ExecutionException {
        final String contractAddress = run(new IsContractDeployedExample());
        
		LOGGER.log(Level.INFO,this.getClass().toString()+"contractAddress="+contractAddress+"]");
        
        final MessageRegistry messageRegistry = MessageRegistryHelper.getMessageRegistry(getWeb3j(), contractAddress); 
        
        // Write to Blockchain
        final BigInteger myBigInt = new BigInteger("1250"); 
        final Uint256 identifier = new Uint256(myBigInt);
        final Utf8String message = new Utf8String("This is the Java Test");
        final Address receiver = new Address("0x5BbB245A661C4C112AA0e848A2BB007Ba3e9B628");
  
        TransactionReceipt transactionReceipt = messageRegistry.save(message, receiver, identifier).get();
        
		LOGGER.log(Level.INFO,this.getClass().toString()+"transactionReceipt.getTransactionHash="+transactionReceipt.getTransactionHash());
        
    	

		
    }
}