import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;

import com.uzh.csg.overlaynetworks.web3j.AsyncHelper;
import com.uzh.csg.overlaynetworks.web3j.IsContractDeployedExample;
import com.uzh.csg.overlaynetworks.web3j.MessageRegistryHelper;
import com.uzh.csg.overlaynetworks.wrappers.MessageRegistry;

public class CanGetValuesTest extends AbstractLocalhostTest{

    @Test
    public void shouldReturnTrueIfContractIsDeployed() throws InterruptedException, ExecutionException {
        final String contractAddress = run(new IsContractDeployedExample());
        final MessageRegistry messageRegistry = MessageRegistryHelper.getMessageRegistry(getWeb3j(), contractAddress); 
        final BigInteger myBigInt = new BigInteger("1234");
        final Uint256 myIdentifier = new Uint256(myBigInt);
        final List<Type> message = messageRegistry.getMessage(myIdentifier).get();
        message.
        
        assert asyncResult.getValue();
    }
}