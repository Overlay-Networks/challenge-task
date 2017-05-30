package com.uzh.csg.overlaynetworks.p2p;

import com.uzh.csg.overlaynetworks.domain.dto.Contact;
import com.uzh.csg.overlaynetworks.domain.dto.ContactStatus;
import com.uzh.csg.overlaynetworks.domain.dto.Message;
import com.uzh.csg.overlaynetworks.domain.dto.MessageResult;
import com.uzh.csg.overlaynetworks.domain.exception.LoginFailedException;
import com.uzh.csg.overlaynetworks.domain.exception.MessageSendFailureException;
import com.uzh.csg.overlaynetworks.p2p.error.P2PError;

public interface P2PClientDelegate {

	/**
	 * Called upon successful login or if there was an error when attempting to log in.
	 * If @error = null, login was successful and information about peer can be extracted from @peer
	 * If @peer = null, login was unsuccessful. Error information can be extracted by @error.getErrorMessage().
	 * Also throws LoginFailedException if login wasn't successful.
	 * @param peer - contains information about peer such as username, IP address and port
	 * @param error - contains information about the error such as error message
	 */
	public void didLogin(PeerInfo peer, P2PError error) throws LoginFailedException;

	/**
	 * Called when message has been successfully received by the peer or there was error an during receive.
	 * If @error = null, message has been successfully received, message content can be retrieved
	 * via message.getMessage() and sender can be retrieved using message.getSender().
	 * Use result.getMessageId() to extract message ID.
	 * If @message = null and @messageResult = null, there was an error during receive.
	 * Detailed information can be found by calling @error.getErrorMessage()
	 * @param message - Message object containing message text and receiver i.e. yourself
	 * @param result - MessageResult object containing ID of received message
	 * @param from - Contact object containing username of message sender (can be retrieved using from.getName())
	 * @param error - contains information about the error such as error message
	 */
	public void didReceiveMessage(Message message, Contact from, MessageResult result, P2PError error);

	/**
	 * Called when acknowledgement of message receive has been received from message-receiving party or there was an error during receive.
	 * If @error = null, ACK message has been successfully received and ID of ACK-ed message can be retrieved via result.getMessageId().
	 * @param result - MessageResult object containing ID of received message
	 * @param error - contains information about the error such as error message
	 */
	public void didReceiveAck(MessageResult result, P2PError error);

	/**
	 * Called once message has been successfully sent or if there was an error during the sending.
	 * If @error = null, message has been successfully sent.
	 * Otherwise, throw MessageSendFailureException and extract error information from P2PError object.
	 * @param error - contains information about the error such as error message
	 */
	public void didSendMessage(P2PError error) throws MessageSendFailureException;

	/**
	 * Called once requested contact has been successfully discovered
	 * returning PeerInfo object containing information about requested contact
	 * or
	 * there was an error and contact wasn't discovered due to not being present in DHT
	 * or due to internal P2P error
	 * @param peerInfo - contains information about discovered contact
	 * @param error - contains information about the error such as error message
	 */
	public void didDiscoverContact(PeerInfo peerInfo, P2PError error);

	/**
	 * Called when online status is updated for a @contact
	 * @param contact - contact for whom online status has updated
	 * @param status - enum containing information about contact status
	 * @param error - contains information about the error such as error message
	 */
	public void didUpdateOnlineStatus(Contact contact, ContactStatus status, P2PError error);

	/**
	 * Called when peer has been successfully shutted down or when error during shutdown occured.
	 * If @error = null, shutdown has been successful.
	 * If @error != null, there was error during shutdown. Detailed information can be received by calling @error.getErrorMessage()
	 * @param error - contains information about the error such as error message
	 */
	public void didShutdown(P2PError error);

}
