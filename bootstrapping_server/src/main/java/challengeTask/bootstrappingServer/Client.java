package challengeTask.bootstrappingServer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;

import net.tomp2p.connection.Bindings;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;

public class Client {

	private int port;
	private Peer peer;
	
	private Random random;
	
	public Client(int port) {
		assert(port > 0 && port < 65536);
		this.port = port;
		random = new Random(85L);
	}
	
	public void start() {
		try {
			Bindings bindings = new Bindings().listenAny();
			peer = new PeerBuilder(new Number160(random)).bindings(bindings).ports(port).start();
			System.out.println("Client peer started on port " + port);
		} catch (IOException ie) {
			System.err.println("Failed to start bootstrapping peer!");
			System.err.println(ie.getMessage());
		}
	}
	
	public void boostrap(final InetAddress address, final int port) {
		FutureDiscover discover = peer.discover().inetAddress(address).ports(port).start();
		System.out.println("Checking discoverability settings...");
		discover.addListener(new BaseFutureAdapter<FutureDiscover>(){

			public void operationComplete(FutureDiscover future) throws Exception {
				if (future.isSuccess()) {
					System.out.println("Discoverable by other nodes!");
					
					FutureBootstrap bootstrap = peer.bootstrap().inetAddress(address).ports(port).start();
					bootstrap.addListener(new BaseFutureAdapter<FutureBootstrap>() {

						public void operationComplete(FutureBootstrap future) throws Exception {
							if (future.isSuccess()) {
								System.out.println("Successfully bootstrapped to server!");
							} else {
								System.err.println("Failed to bootstrap!");
								System.err.println("Reason is " + future.failedReason());
							}
						}
						
					});
				} else {
					System.err.println("Not discoverable by other nodes! Aborting bootstrap...");
					System.err.println("Reason is " + future.failedReason());
				}
			}
			
		});
	}
	
	public void shutdown() {
		peer.shutdown();
	}
	
}
