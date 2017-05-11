package com.uzh.csg.overlaynetworks.p2p.error;

public enum P2PReceiveMessageError implements P2PError {
	INVALID_MESSAGE_FORMAT("Invalid message format has been received");
	
	private final String message;
	
	P2PReceiveMessageError(String message) {
		this.message = message;
	}

	@Override
	public String getErrorMessage() {
		return message;
	}
	
}
