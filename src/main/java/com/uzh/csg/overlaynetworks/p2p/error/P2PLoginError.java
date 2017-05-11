package com.uzh.csg.overlaynetworks.p2p.error;

public enum P2PLoginError implements P2PError {
	SOCKET_OPEN_ERROR("Failed to open socket for client"),
	DHT_STORE_ERROR("Failed to store client credentials in DHT"),
	BOOTSTRAP_ERROR("Failed to bootstrap!");
	
	private final String message;
	
	P2PLoginError(String message) {
		this.message = message;
	}

	@Override
	public String getErrorMessage() {
		return message;
	}
	
}
