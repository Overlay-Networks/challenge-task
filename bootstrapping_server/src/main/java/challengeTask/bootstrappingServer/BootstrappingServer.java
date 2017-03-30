package challengeTask.bootstrappingServer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;

import net.tomp2p.connection.Bindings;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;

public class BootstrappingServer {
	
	private InetAddress address;
	private int port;
	
	private Peer server;
	
	private Random random;

	public BootstrappingServer(InetAddress address, int port) {
		assert(port > 0 && port < 65536);
		this.address = address;
		this.port = port;
		
		this.random = new Random(43L);
	}
	
	public void start() {
		Bindings binding = new Bindings().addAddress(address);
		try {
			server = new PeerBuilder(new Number160(random)).bindings(binding).ports(port).start();
			System.out.println("Bootstrapping peer started on port " + port);
		} catch (IOException ie) {
			System.err.println("Failed to start bootstrapping peer!");
			System.err.println(ie.getMessage());
		}
	}
	
	public PeerAddress getAddress() {
		return server.peerAddress();
	}
	
	public void shutdown() {
		server.shutdown();
	}
	
}
