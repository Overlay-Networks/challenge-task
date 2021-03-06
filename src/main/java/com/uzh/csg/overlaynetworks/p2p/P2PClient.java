package com.uzh.csg.overlaynetworks.p2p;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Random;

import com.uzh.csg.overlaynetworks.domain.dto.Contact;
import com.uzh.csg.overlaynetworks.domain.dto.Message;
import com.uzh.csg.overlaynetworks.domain.dto.MessageResult;
import com.uzh.csg.overlaynetworks.domain.exception.LoginFailedException;
import com.uzh.csg.overlaynetworks.domain.exception.MessageSendFailureException;
import com.uzh.csg.overlaynetworks.p2p.error.P2PLoginError;
import com.uzh.csg.overlaynetworks.p2p.error.P2PReceiveMessageError;
import com.uzh.csg.overlaynetworks.p2p.error.P2PSendMessageError;
import com.uzh.csg.overlaynetworks.p2p.error.P2PShutdownError;

import net.tomp2p.connection.Bindings;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.FutureRemove;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.dht.PutBuilder;
import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.futures.FutureDone;
import net.tomp2p.p2p.AutomaticFuture;
import net.tomp2p.p2p.JobScheduler;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;

public class P2PClient {

	/* to receive events declared in P2PDelegate, implement P2PClientDelegate protocol and set yourself to this property */
	public P2PClientDelegate delegate;

	private PeerDHT peer;
	private PeerInfo peerInfo;

	private Random random;

	private JobScheduler userDataUploader;

	/* bootstrapping server IP and port are fixed constants */
	private static final String BOOTSTRAP_ADDRESS = "192.168.1.163";
	private static final int BOOTSTRAP_PORT = 37083;

	/* TTL for peer credentials */
	private static final int USER_DATA_TTL = 60;


	public P2PClient(String username) {
		this.peerInfo = new PeerInfo(username);
		random = new Random(username.hashCode());
	}

	public PeerInfo getPeerInfo() {
		return this.peerInfo;
	}

	/**
	 * Bootstraps peer to the server
	 * Stores peer credentials in DHT
	 * Setup routine how to handle incoming messages for peer
	 * @throws LoginFailedException
	 */
	public void start() throws LoginFailedException {
		try {
			ServerSocket socket = new ServerSocket(0, 0, InetAddress.getByName("192.168.1.122"));
			InetAddress address = socket.getInetAddress();
			int port = socket.getLocalPort();

			/* ServerSocket opened just to get free port */
			socket.close();

			Bindings bindings = new Bindings().addAddress(address);
			peer = new PeerBuilderDHT(new PeerBuilder(new Number160(random)).bindings(bindings).ports(port).start()).start();
			System.out.println("Created peer with ID: " + peer.peerID().toString());

			/* Specifies what to do when message is received */
			peer.peer().objectDataReply(new ObjectDataReply() {

				@Override
				public Object reply(PeerAddress sender, Object request) throws Exception {
					if (request instanceof String) {
						String payload = (String) request;
						int notaryAndIDSeparatorIndex = payload.indexOf("_");
						int idAndUsernameSeparatorIndex = payload.indexOf("_", notaryAndIDSeparatorIndex+1);
						int usernameAndMessageSeparatorIndex = payload.lastIndexOf("_");
						if (notaryAndIDSeparatorIndex > 0 && idAndUsernameSeparatorIndex > 0 && usernameAndMessageSeparatorIndex > 0) {
							try {
								String notary = payload.substring(0, notaryAndIDSeparatorIndex);
								boolean isSigned = false;
								if(notary.compareTo("0") == 0) {
									isSigned = false;
								} else if (notary.compareTo("1") == 0) {
									isSigned = true;
								} else {
									if(delegate != null) {
										delegate.didReceiveMessage(null, null, null, P2PReceiveMessageError.INVALID_MESSAGE_FORMAT);
									}
								}
								String messageIDStr = payload.substring(notaryAndIDSeparatorIndex + 1, idAndUsernameSeparatorIndex);
								long messageID = Long.parseLong(messageIDStr);
								String username = payload.substring(idAndUsernameSeparatorIndex+1, usernameAndMessageSeparatorIndex);
								String message = payload.substring(usernameAndMessageSeparatorIndex+1, payload.length());

								Message messageObj = new Message();
								messageObj.setMessage(message);
								messageObj.setNotary(isSigned);
								Contact contact = new Contact(username);
								MessageResult result = new MessageResult(messageID);

								FutureDirect ackMessage = peer.peer().sendDirect(sender).object("ack_" + messageID).start();
								ackMessage.addListener(new BaseFutureAdapter<FutureDirect>() {

									@Override
									public void operationComplete(FutureDirect future) throws Exception {
										if (future.isSuccess()) {
											System.out.println("Successfully acknowledged incoming message!");
										} else {
											System.err.println("Failed to acknowledge incoming message!");
											System.err.println("Reason is: " + future.failedReason());
										}
									}

								});

								if (delegate != null) {
									delegate.didReceiveMessage(messageObj, contact, result, null);
								}
							} catch (NumberFormatException nfe) {
								System.err.println("Failed casting message ID to long!");
								nfe.printStackTrace();

								if (delegate != null) {
									delegate.didReceiveMessage(null, null, null, P2PReceiveMessageError.INVALID_MESSAGE_FORMAT);
								}
							}
						} else if (payload.startsWith("ack") && payload.indexOf("_") > 0) {
							try {
								String messageIDStr = payload.substring(payload.indexOf("_")+1, payload.length());
								long messageID = Long.parseLong(messageIDStr);
								MessageResult result = new MessageResult(messageID);
								if (delegate != null) {
									delegate.didReceiveAck(result, null);
								}
							} catch (NumberFormatException nfe) {
								System.err.println("Failed casting message ID to long!");
								nfe.printStackTrace();

								if (delegate != null) {
									delegate.didReceiveAck(null, P2PReceiveMessageError.INVALID_MESSAGE_FORMAT);
								}
							}
						} else if(payload.compareTo("ping") == 0) {
							FutureDirect pingACKMessage = peer.peer().sendDirect(sender).object("pingACK_" + peerInfo.getUsername()).start();
							pingACKMessage.addListener(new BaseFutureAdapter<FutureDirect>() {

								@Override
								public void operationComplete(FutureDirect future) throws Exception {
									if (future.isFailed()) {
										System.err.println("Failed to send ping ACK!");
										System.err.println("Reason is: " + future.failedReason());
									} else {
										System.out.println("Successfully sent ping ACK message!");
									}
								}

							});
						} else if(payload.startsWith("pingACK_")) {
							String username = payload.substring(payload.indexOf("_") + 1, payload.length());
							Contact contact = new Contact(username);
							if (delegate != null) {
								delegate.didUpdateOnlineStatus(contact, true, null);
							}
						} else {
							if (delegate != null) {
								delegate.didReceiveMessage(null, null, null, P2PReceiveMessageError.INVALID_MESSAGE_FORMAT);
							}
						}
					} else {
						if (delegate != null) {
							  delegate.didReceiveMessage(null, null, null, P2PReceiveMessageError.INVALID_MESSAGE_FORMAT);
						}
					}
					return null;
				}
			});

			peerInfo.setInetAddress(address);
			peerInfo.setPort(port);
			peerInfo.setPeerAddress(peer.peerAddress());

			System.out.println("Client peer started on IP " + address + " on port " + port);
			System.out.println("Bootstrapping peer...");
			bootstrap();
		} catch (IOException ie) {
			ie.printStackTrace();

			if (delegate != null) {
				delegate.didLogin(null, P2PLoginError.SOCKET_OPEN_ERROR);
			}
		}
	}

	/**
	 * Sends direct message to the contact specified in @message.getReceiver()
	 * @param message
	 * @param result
	 */
	public void sendMessage(Message message, MessageResult result) throws MessageSendFailureException {
		if (message.getReceiver() == null) {
			if (delegate != null) {
				delegate.didSendMessage(P2PSendMessageError.USER_NOT_FOUND);
			}
			return;
		}
		PeerInfo receiverInfo = new PeerInfo(message.getReceiver().getName());
		FutureGet getIPAddress = peer.get(receiverInfo.getUsernameKey()).start();
		String messageToSend = (message.getNotary() ? "1" : "0") + "_" + result.getMessageId() + "_" +  peerInfo.getUsername() + "_" + message.getMessage();
		getIPAddress.addListener(new BaseFutureAdapter<FutureGet>() {

			@Override
			public void operationComplete(FutureGet future) throws Exception {
				if (future.isSuccess() && future.data() != null) {
					try {
						PeerInfo receiverInfo = new PeerInfo(future.data().toBytes());
						FutureDirect directMessage = peer.peer().sendDirect(receiverInfo.getPeerAddress()).object(messageToSend).start();
						directMessage.addListener(new BaseFutureAdapter<FutureDirect>() {

							@Override
							public void operationComplete(FutureDirect future) throws Exception {
								if (future.isSuccess()) {
									System.out.println("Successfully sent direct message!");

									if (delegate != null) {
										delegate.didSendMessage(null);
									}
								} else {
									System.err.println("Failed to directly send value!");
									System.err.println("Reason is: " + future.failedReason());

									if (delegate != null) {
										delegate.didSendMessage(P2PSendMessageError.SEND_FAILURE);
									}
								}
							}

						});
					} catch (IllegalArgumentException iae) {
						iae.printStackTrace();

						if (delegate != null) {
							delegate.didSendMessage(P2PSendMessageError.INVALID_MESSAGE_FORMAT);
						}
					}
				} else {
					System.err.println("Failed to retrieve data from DHT for key " + receiverInfo.getUsername() + "!");
					System.err.println("Reason is " + future.failedReason());

					if (delegate != null) {
						delegate.didSendMessage(P2PSendMessageError.USER_NOT_FOUND);
					}
				}
			}

		});
	}

	/**
	 * Checks whether given contact is online or not
	 * First, checks whether peer info is present in DHT. If it is present, peer isn't necessarily online.
	 * Hence, it tries to ping it with direct message.
	 * Upon completion, corresponding didUpdateOnlineStatus(...) delegate method is called
	 * @param contact
	 */
	public void updateOnlineStatus(Contact contact) {
		PeerInfo contactInfo = new PeerInfo(contact.getName());
		FutureGet retrieveUser = peer.get(contactInfo.getUsernameKey()).start();
		retrieveUser.addListener(new BaseFutureAdapter<FutureGet>() {

			@Override
			public void operationComplete(FutureGet future) throws Exception {
				if(future.isSuccess() && future.data() != null) {
					PeerInfo contactInfo = new PeerInfo(future.data().toBytes());
					if(contactInfo != null) {
						FutureDirect pingMessage = peer.peer().sendDirect(contactInfo.getPeerAddress()).object("ping").start();
						pingMessage.addListener(new BaseFutureAdapter<FutureDirect>() {

							@Override
							public void operationComplete(FutureDirect future) throws Exception {
								if(future.isCompleted() && future.isFailed()) {
									System.err.println("Failed to send direct ping message: " + future.isFailed());
									if(delegate != null) {
										delegate.didUpdateOnlineStatus(contact, false, null);
									}
								} else if (future.isSuccess()) {
									System.out.println("Successfully sent ping message!");
								}
							}

						});
					} else {
						if (delegate != null) {
							delegate.didUpdateOnlineStatus(contact, false, null);
						}
					}
				} else {
					if (delegate != null) {
						delegate.didUpdateOnlineStatus(contact, false, null);
					}
				}
			}

		});
	}

	/**
	 * Shutdowns peer 'nicely' announcing that in P2P system and removing credentials from DHT
	 */
	public void shutdown() {
		FutureDone<Void> uploaderShutdown = userDataUploader.shutdown();
		uploaderShutdown.addListener(new BaseFutureAdapter<FutureDone<Void>>() {

			@Override
			public void operationComplete(FutureDone<Void> future) throws Exception {
				if(future.isSuccess()) {
					System.out.println("Successfully shutted down recurrent user data uploader!");
				} else {
					System.err.println("Failed to shut down recurrent user data uploader! Error is " + future.failedReason());
				}
			}

		});
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
								System.err.println("Error shutting down peer!");
								System.err.println("Reason is " + future.failedReason());

								if (delegate != null) {
									delegate.didShutdown(P2PShutdownError.SHUTDOWN_ERROR);
								}
							}
						}

					});
				} else {
					System.err.println("Failed to remove peer credentials from DHT!");
					System.err.println("Reasom is " + future.failedReason());

					if (delegate != null) {
						delegate.didShutdown(P2PShutdownError.CLEAR_DHT_ERROR);
					}
				}
			}

		});
	}

	/**
	 * Helper function which bootstraps peer to the bootstrapping server
	 * Bootstrapping server credentials are specified on top of the file using:
	 * - BOOTSTRAP_ADDRESS
	 * - BOOTSTRAP_PORT
	 * @throws LoginFailedException
	 */
	private void bootstrap() throws LoginFailedException {
		try {
			InetAddress bootstrapAddress = InetAddress.getByName(BOOTSTRAP_ADDRESS);
			FutureBootstrap bootstrap = peer.peer().bootstrap().inetAddress(bootstrapAddress).ports(BOOTSTRAP_PORT).start();
			bootstrap.addListener(new BaseFutureAdapter<FutureBootstrap>() {

				@Override
				public void operationComplete(FutureBootstrap future) throws Exception {
					if (future.isSuccess()) {
						System.out.println("Successfully bootstrapped to server!");

						byte[] peerData = peerInfo.toByteArray();
						Data dataToStore = new Data(peerData);
						dataToStore.ttlSeconds(USER_DATA_TTL);
						PutBuilder userDataPut = peer.put(peerInfo.getUsernameKey()).data(dataToStore);
						userDataUploader = new JobScheduler(peer.peer());
						userDataUploader.start(userDataPut, (USER_DATA_TTL/6) * 1000, -1, new AutomaticFuture() {

							@Override
							public void futureCreated(BaseFuture future) {
								future.addListener(new BaseFutureAdapter<FuturePut>() {

									@Override
									public void operationComplete(FuturePut future) throws Exception {
										if(future.isSuccess()) {
											System.out.println("Added data to DHT for peer " + peerInfo.getUsername());
										} else {
											System.err.println("Failed adding data to DHT for peer " + peerInfo.getUsername() + "!");
											System.err.println(future.failedReason());
										}
									}

								});
							}

						});

						if (delegate != null) {
							delegate.didLogin(peerInfo, null);
						}
					} else {
						System.err.println("Failed to bootstrap!");
						System.err.println("Reason is " + future.failedReason());
						if (delegate != null) {
							delegate.didLogin(null, P2PLoginError.BOOTSTRAP_ERROR);
						}
						return;
					}
				}

			});
		} catch (UnknownHostException uhe) {
			System.err.println("Invalid host specified: " + BOOTSTRAP_ADDRESS + "!");
			if (delegate != null) {
				delegate.didLogin(null, P2PLoginError.BOOTSTRAP_ERROR);
			}
			return;
		}
	}

}
