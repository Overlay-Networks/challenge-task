package com.uzh.csg.overlaynetworks.p2p;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.UUID;

import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;

/* Class that stores information about peer */
public class PeerInfo {

	private String username;

	private PeerAddress peerAddress;

	private InetAddress inetAddress;
	private int port;

	public PeerInfo(String username) {
		this.username = username;
	}

	public PeerInfo(String username, InetAddress address, int port) {
		this.username = username;
		this.inetAddress = address;
		this.port = port;
	}

	public PeerInfo(String username, InetAddress address, int port, PeerAddress peerAddress) {
		this.username = username;
		this.inetAddress = address;
		this.port = port;
		this.peerAddress = peerAddress;
	}

	public String getUsername() {
		return username;
	}

	/**
	 * Returns Number160 uniquely corresponding to username
	 * Same username results in same Number160
	 * @return
	 */
	public Number160 getUsernameKey() {
		String key = UUID.nameUUIDFromBytes(username.getBytes()).toString();
		key = key.replace("-","");
		key = "0x" + key;
		return new Number160(key);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public PeerAddress getPeerAddress() {
		return peerAddress;
	}

	public void setPeerAddress(PeerAddress peerAddress) {
		this.peerAddress = peerAddress;
	}

	public InetAddress getInetAddress() {
		return inetAddress;
	}

	public void setInetAddress(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public PeerInfo(byte[] data) throws IllegalArgumentException {
		int firstSeparatorIndex = -1;
		int secondSeparatorIndex = -1;
		for (int i=0; i < data.length; i++) {
			byte ch = data[i];
			if (ch == ':') {
				if (firstSeparatorIndex != -1) {
					secondSeparatorIndex = i;
				} else {
					firstSeparatorIndex = i;
				}
			}
		}
		if (firstSeparatorIndex == -1 || secondSeparatorIndex == -1) {
			throw new IllegalArgumentException("Input byte[] doesn't contain any separators!");
		} else {
			byte[] peerAddressBytes = new byte[firstSeparatorIndex];
			byte[] inetAdressBytes = new byte[secondSeparatorIndex - firstSeparatorIndex];
			byte[] portBytes = new byte[data.length - secondSeparatorIndex + 1];
			for (int i=0; i < data.length; i++) {
				if(i < firstSeparatorIndex) {
					peerAddressBytes[i] = data[i];
				} else if (i > firstSeparatorIndex && i < secondSeparatorIndex) {
					inetAdressBytes[i - firstSeparatorIndex - 1] = data[i];
				} else {
					portBytes[i - secondSeparatorIndex - 1] = data[i];
				}
			}

			this.peerAddress = new PeerAddress(peerAddressBytes);
			try {
				this.inetAddress = InetAddress.getByAddress(inetAdressBytes);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Cannot extract InetAddress from given byte[]!");
			}
			this.port = ByteBuffer.wrap(portBytes).getInt();
		}
	}

	public byte[] toByteArray() {
		byte[] peerAddressBytes = peerAddress.toByteArray();
		byte[] addressBytes = inetAddress.getAddress();
		byte[] portBytes = ByteBuffer.allocate(4).putInt(port).array();
		ByteBuffer dataBuffer = ByteBuffer.allocate(peerAddressBytes.length + addressBytes.length + portBytes.length + 2);
		byte separatorCharacter = ':';
		dataBuffer.put(peerAddressBytes);
		dataBuffer.put(separatorCharacter);
		dataBuffer.put(addressBytes);
		dataBuffer.put(separatorCharacter);
		dataBuffer.put(portBytes);
		return dataBuffer.array();
	}

}
