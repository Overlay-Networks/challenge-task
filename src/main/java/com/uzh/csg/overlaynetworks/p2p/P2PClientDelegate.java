package com.uzh.csg.overlaynetworks.p2p;

import com.uzh.csg.overlaynetworks.domain.dto.Contact;
import com.uzh.csg.overlaynetworks.p2p.error.P2PError;

public interface P2PClientDelegate {

	/**
	 * Called upon successful login or if there was an error when attempting to log in.
	 * If @error = null, login was successful and information about peer can be extracted from @peer
	 * If @peer = null, login was unsuccessful. Error information can be extracted by @error.getErrorMessage() 
	 * @param peer - contains information about peer such as username, IP address and port
	 * @param error - contains information about the error such as error message
	 */
	public void didLogin(PeerInfo peer, P2PError error);
	
	/**
	 * Called when message has been successfully received by the peer or there was error during receival.
	 * If @error = null, message has been successfully received, @senderUsername and @message can be read.
	 * If @message = null and @senderUsername = null, there was an error during receival.
	 * Detailed information can be found by calling @error.getErrorMessage()
	 * @param senderUsername - contains username of message sender
	 * @param message - contains the actual message received
	 * @param error - contains information about the error such as error message
	 */
	public void didReceiveMessage(String senderUsername, String message, P2PError error);
	
	/**
	 * Called once message has been successfully sent or if there was an error during the sending.
	 * If @error = null, message has been successfully sent.
	 * Otherwise, extract error information from P2PError object
	 * @param error - contains information about the error such as error message
	 */
	public void didSendMessage(P2PError error);
	
	/**
	 * 
	 * @param contact
	 * @param error
	 */
	public void didDiscoverContact(Contact contact, P2PError error);
	
	/**
	 * 
	 * @param error
	 */
	public void didShutdown(P2PError error);
	
}
