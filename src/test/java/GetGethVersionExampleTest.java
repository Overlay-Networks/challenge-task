import org.junit.Test;

import com.uzh.csg.overlaynetworks.web3j.examples.GetGethVersionExample;

import static org.assertj.core.api.Assertions.assertThat;

public class GetGethVersionExampleTest extends AbstractLocalhostTest {

    @Test
    public void shouldRetrieveTheCorrectBlockchainVersion() {
        final String version = run(new GetGethVersionExample());
        // Depends on your individual installation
        assertThat(version).isEqualTo("Geth/v1.5.9-stable-a07539fb/darwin/go1.8");
    }

}
