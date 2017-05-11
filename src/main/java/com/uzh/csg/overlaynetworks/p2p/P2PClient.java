package com.uzh.csg.overlaynetworks.p2p;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Random;

import net.tomp2p.connection.Bindings;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.FutureRemove;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.futures.FutureDone;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class P2PClient {

	private PeerDHT peer;
	private PeerInfo peerInfo;
	
	private Random random;
	
	/* bootstrapping server IP and port are fixed constants */
	private static final String BOOTSTRAP_ADDRESS = "127.0.0.1";
	private static final int BOOTSTRAP_PORT = 50704;
	
	public P2PClient(String username) throws IOException {
		this.peerInfo = new PeerInfo(username);
		random = new Random(85L);
	}
	
	public void start() throws IOException {
		ServerSocket socket = new ServerSocket(0, 0, InetAddress.getByName(null));
		InetAddress address = socket.getInetAddress();
		int port = socket.getLocalPort();
		
		/* ServerSocket opened just to get free port */
		try {
			socket.close();
		} catch(IOException ie) {
			System.err.println("Failed to close socket!");
			ie.printStackTrace();
		}
		Bindings bindings = new Bindings().addAddress(address);
		peer = new PeerBuilderDHT(new PeerBuilder(new Number160(random)).bindings(bindings).ports(port).start()).start();
		
		peerInfo.setInetAddress(address);
		peerInfo.setPort(port);
		peerInfo.setPeerAddress(peer.peerAddress());
		
		System.out.println("Client peer started on IP " + address + " on port " + port);
		System.out.println("Bootstrapping peer...");
		bootstrap();
	}
	
	public void sendMessage(String username, String message) {
		Number160 storeKey = new Number160(username.hashCode());
		FutureGet getIPAddress = peer.get(storeKey).start();
		getIPAddress.addListener(new BaseFutureAdapter<FutureGet>() {
			
			public void operationComplete(FutureGet future) throws Exception {
				if (future.isSuccess() && future.data() != null) {
					try {
						PeerInfo receiverInfo = new PeerInfo(future.data().toBytes());
						FutureDirect directMessage = peer.peer().sendDirect(receiverInfo.getPeerAddress()).object(message).start();
						directMessage.addListener(new BaseFutureAdapter<FutureDirect>() {

							@Override
							public void operationComplete(FutureDirect future) throws Exception {
								if (future.isSuccess()) {
									System.out.println("Successfuly sent direct message!");
								} else {
									System.out.println("Failed to directly send value! Reason is: " + future.failedReason());
								}
							}
							
						});
					} catch (IllegalArgumentException iae) {
						
					}
				} else {
					System.err.println("Failed to retrieve data from DHT for key " + username + "! Reason is " + future.failedReason());
				}
			}
			
		});
	}
	
	public void isOnline(String username) {
		Number160 userKey = new Number160(username.hashCode());
		FutureGet retrieveUser = peer.get(userKey).start();
		retrieveUser.addListener(new BaseFutureAdapter<FutureGet>() {

			@Override
			public void operationComplete(FutureGet future) throws Exception {
				if(future.isSuccess() && future.data() != null) {
					PeerInfo peerInfo = new PeerInfo(future.data().toBytes());
					FutureDiscover discover = peer.peer().discover().peerAddress(peerInfo.getPeerAddress()).start();
					discover.addListener(new BaseFutureAdapter<FutureDiscover>(){

						@Override
						public void operationComplete(FutureDiscover future) throws Exception {
							if(future.isDiscoveredTCP()) {
								
							}
							
						}
						
					});
				} else {
					/* return not online */
				}
			}
			
		});
	}
	
	private void bootstrap() {
		try {
			InetAddress bootstrapAddress = InetAddress.getByName(BOOTSTRAP_ADDRESS);
			FutureBootstrap bootstrap = peer.peer().bootstrap().inetAddress(bootstrapAddress).ports(BOOTSTRAP_PORT).start();
			bootstrap.addListener(new BaseFutureAdapter<FutureBootstrap>() {

				public void operationComplete(FutureBootstrap future) throws Exception {
					if (future.isSuccess()) {
						System.out.println("Successfully bootstrapped to server!");
						storeUserInformationInDHT();
					} else {
						System.err.println("Failed to bootstrap!");
						System.err.println("Reason is " + future.failedReason());
					}
				}
				
			});
		} catch (UnknownHostException uhe) {
			System.err.println("Invalid host specified: " + BOOTSTRAP_ADDRESS + "!");
			return;
		}
	}
	
	/* upon successful bootstrapping, peer stores it's username, peer address, IP address and port in DHT */
	private void storeUserInformationInDHT() {
		byte[] peerData = peerInfo.toByteArray();
		Data dataToStore = new Data(peerData);
		FuturePut put = peer.put(peerInfo.getUsernameKey()).data(dataToStore).start();
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
	
	public void shutdown() {
		FutureRemove removeData = peer.remove(peerInfo.getUsernameKey()).start();
		removeData.addListener(new BaseFutureAdapter<FutureRemove>() {

			@Override
			public void operationComplete(FutureRemove future) throws Exception {
				if(future.isSuccess()) {
					System.out.println("Successfully removed user credentials from DHT!");
					System.out.println("Shutting down...");
					FutureDone<Void> shutdownFuture = peer.peer().announceShutdown().start();
					shutdownFuture.addListener(new BaseFutureAdapter<FutureDone<Void>>() {

						@Override
						public void operationComplete(FutureDone<Void> future) throws Exception {
							if(future.isSuccess()) {
								System.out.println("Successfully shutted down peer!");
							} else {
								System.err.println("Error shutting down peer! Reason is " + future.failedReason());
							}
						}
						
					});
				} else {
					System.err.println("Failed to remove peer credentials from DHT!");
				}
			}
			
		});
	}
	
}
