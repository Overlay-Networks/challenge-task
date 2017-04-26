import org.junit.Test;
import org.web3j.abi.datatypes.Utf8String;

import com.uzh.csg.overlaynetworks.web3j.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ExecutionException;

public class GetSmartContractTest extends AbstractLocalhostTest {
	
	@Test
	public void shouldLoadSmartContract() throws InterruptedException, ExecutionException {
		Utf8String myGreeting = new Utf8String("hallo");
		
		final Hashing contract = SmartContractExample.loadContract();
		System.out.println(contract.getGasPrice().toString());
		System.out.println(contract.getTransactionReceipt());
		System.out.println(contract.hash(myGreeting));
	}
}
