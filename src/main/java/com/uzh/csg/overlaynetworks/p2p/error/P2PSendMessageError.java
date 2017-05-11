package com.uzh.csg.overlaynetworks.p2p.error;

public enum P2PSendMessageError implements P2PError {
	USER_NOT_FOUND("Failed to find user in DHT!"),
	INVALID_MESSAGE_FORMAT("Input message has invalid format!"),
	SEND_FAILURE("Failed to send message!");
	
	private final String message;
	
	P2PSendMessageError(String message) {
		this.message = message;
	}

	@Override
	public String getErrorMessage() {
		return message;
	}
	
}
