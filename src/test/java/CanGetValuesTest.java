import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.Test;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import com.uzh.csg.overlaynetworks.web3j.examples.IsContractDeployedExample;
import com.uzh.csg.overlaynetworks.web3j.MessageRegistryHelper;
import com.uzh.csg.overlaynetworks.wrappers.MessageRegistry;
import java.util.logging.*;


public class CanGetValuesTest extends AbstractLocalhostTest{

private static final Logger LOGGER = Logger.getLogger(CanGetValuesTest.class.getName() );

    @Test
    public void shouldReturnTrueIfValueIsRetrieved() throws InterruptedException, ExecutionException {

    	try{

        final String contractAddress = run(new IsContractDeployedExample());
        final MessageRegistry messageRegistry = MessageRegistryHelper.getMessageRegistry(getWeb3j(), contractAddress);

		LOGGER.log(Level.INFO,this.getClass().toString()+"[messageRegistry.getContractAddress()="+messageRegistry.getContractAddress()+"]");

    	final BigInteger myBigInt = new BigInteger("1234");
        final Uint256 myIdentifier = new Uint256(myBigInt);

        final List<Type> message = messageRegistry.getMessage(myIdentifier).get();

		LOGGER.log(Level.INFO,this.getClass().toString()+"message.size()="+message.size());
		LOGGER.log(Level.INFO,this.getClass().toString()+"message.toString()="+message.toString());

        assertThat(message).isNotNull();
        assertThat(message.size()).isEqualTo(4);
    	assertThat(message.toString()).isEqualTo("test");

    	} catch(Exception e){
    		LOGGER.log(Level.INFO, e.toString()+"["+e.getClass().toString()+"]");
    	}

    }
}
