package com.uzh.csg.overlaynetworks.p2p.error;

public enum P2PShutdownError implements P2PError {
	CLEAR_DHT_ERROR("Failed to find user in DHT!"),
	SHUTDOWN_ERROR("Input message has invalid format!");
	
	private final String message;
	
	P2PShutdownError(String message) {
		this.message = message;
	}

	@Override
	public String getErrorMessage() {
		return message;
	}
	
}
