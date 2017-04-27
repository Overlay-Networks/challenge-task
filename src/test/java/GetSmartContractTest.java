import org.junit.Test;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import com.uzh.csg.overlaynetworks.web3j.*;

import com.uzh.csg.overlaynetworks.web3j.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.web3j.tx.Contract.GAS_LIMIT;
import static org.web3j.tx.ManagedTransaction.GAS_PRICE;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class GetSmartContractTest extends AbstractLocalhostTest {
	
	private static final BigInteger MAX_GAS_PER_TX = BigInteger.valueOf(3141592L);
	final Credentials credentials = CredentialsHelper.getDefaultCredentials();
	
	// every test has its own web3j instance
    private final Web3j web3j = Web3j.build(new HttpService("http://localhost:8545"));
	
	@Test
	
	public void shouldLoadSmartContract() throws InterruptedException, ExecutionException {
		BigInteger bigInt = new BigInteger("1");
		Uint256 identifier = new Uint256(bigInt);
		
		
		GetMessageExample gm1 = new GetMessageExample();
		System.out.println(gm1.run(web3j, credentials, GAS_PRICE, GAS_LIMIT, MAX_GAS_PER_TX));

	}
}
