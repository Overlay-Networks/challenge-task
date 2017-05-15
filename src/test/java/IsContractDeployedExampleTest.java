import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.web3j.abi.datatypes.Bool;

import com.uzh.csg.overlaynetworks.web3j.AsyncHelper;
import com.uzh.csg.overlaynetworks.web3j.IsContractDeployedExample;
import com.uzh.csg.overlaynetworks.web3j.MessageRegistryHelper;
import com.uzh.csg.overlaynetworks.wrappers.MessageRegistry;

public class IsContractDeployedExampleTest extends AbstractLocalhostTest{
	
	private static final Logger LOGGER = Logger.getLogger(IsContractDeployedExampleTest.class.getName() );

    @Test
    public void shouldReturnTrueIfContractIsDeployed() throws InterruptedException, ExecutionException {
    	
    	final String contractAddress = run(new IsContractDeployedExample());
        final MessageRegistry messageRegistry = MessageRegistryHelper.getMessageRegistry(getWeb3j(), contractAddress); 
        final Bool result = messageRegistry.isDeployed().get();
        
		LOGGER.log(Level.INFO, this.getClass().toString()+"[messageRegistry.isDeployed="+result.getValue()+"]");
        assertThat(result.getValue()).isTrue();
    
    }
}