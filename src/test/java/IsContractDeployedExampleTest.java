import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.web3j.abi.datatypes.Bool;

import com.uzh.csg.overlaynetworks.web3j.AsyncHelper;
import com.uzh.csg.overlaynetworks.web3j.IsContractDeployedExample;
import com.uzh.csg.overlaynetworks.web3j.MessageRegistryHelper;
import com.uzh.csg.overlaynetworks.wrappers.MessageRegistry;

public class IsContractDeployedExampleTest extends AbstractLocalhostTest{

    @Test
    public void shouldReturnTrueIfContractIsDeployed() throws InterruptedException, ExecutionException {
        final String contractAddress = run(new IsContractDeployedExample());
        final MessageRegistry messageRegistry = MessageRegistryHelper.getMessageRegistry(getWeb3j(), contractAddress); 
        final Bool asyncResult = messageRegistry.isDeployed().get();
        
        assert asyncResult.getValue();
    }
}