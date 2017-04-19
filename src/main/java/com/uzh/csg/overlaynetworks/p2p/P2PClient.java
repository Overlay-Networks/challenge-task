package com.uzh.csg.overlaynetworks.p2p;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.util.Random;

import net.tomp2p.connection.Bindings;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class P2PClient {

	private ServerSocket socket;
	private PeerDHT peer;
	private String username;
	
	private Random random;
	
	public P2PClient(String username) throws IOException {
		socket = new ServerSocket(0);
		this.username = username;
		random = new Random(85L);
	}
	
	public void start() throws IOException {
		Bindings bindings = new Bindings().addAddress(socket.getInetAddress());
		peer = new PeerBuilderDHT(new PeerBuilder(new Number160(random)).bindings(bindings).ports(socket.getLocalPort()).start()).start();
		System.out.println("Client peer started on IP " + socket.getInetAddress() + " on port " + socket.getLocalPort());
	}
	
	public void boostrap(final InetAddress address, final int port) {
		FutureDiscover discover = peer.peer().discover().inetAddress(address).ports(port).start();
		System.out.println("Checking discoverability settings...");
		discover.addListener(new BaseFutureAdapter<FutureDiscover>(){

			public void operationComplete(FutureDiscover future) throws Exception {
				if (future.isSuccess()) {
					System.out.println("Discoverable by other nodes!");
					
					FutureBootstrap bootstrap = peer.peer().bootstrap().inetAddress(address).ports(port).start();
					bootstrap.addListener(new BaseFutureAdapter<FutureBootstrap>() {

						public void operationComplete(FutureBootstrap future) throws Exception {
							if (future.isSuccess()) {
								System.out.println("Successfully bootstrapped to server!");
								storeUserInformationInDHT(username, address, port);
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
	
	/* upon successful bootstrapping, peer stores it's username, IP address and port in DHT */
	private void storeUserInformationInDHT(String username, InetAddress address, int port) {
		final Number160 storeKey = new Number160(username);
		if (storeKey != null) {
			byte[] addressBytes = address.getAddress();
			byte[] portBytes = ByteBuffer.allocate(4).putInt(port).array();
			ByteBuffer dataBuffer = ByteBuffer.allocate(addressBytes.length + 1 + portBytes.length);
			byte separatorCharacter = 'S';
			dataBuffer.put(addressBytes);
			dataBuffer.put(separatorCharacter);
			dataBuffer.put(portBytes);
			Data dataToStore = new Data(dataBuffer.array());
			FuturePut put = peer.put(storeKey).data(dataToStore).start();
			put.addListener(new BaseFutureAdapter<FuturePut>() {

				@Override
				public void operationComplete(FuturePut future) throws Exception {
					if (future.isSuccess()) {
						System.out.println("Successfuly stored user credentials in DHT!");
					} else {
						System.err.println("Failed to store user credentials in DHT! Reason is " + future.failedReason());
					}
				}
				
			});
		}
	}
	
	public void shutdown() {
		peer.shutdown();
	}
	
}
