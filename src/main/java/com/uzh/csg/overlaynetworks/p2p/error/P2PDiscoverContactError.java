package com.uzh.csg.overlaynetworks.p2p.error;

public enum P2PDiscoverContactError implements P2PError {
	CONTACT_NOT_PRESENT("This contact doesn't appear to exist!"),
	DISCOVERY_ERROR("Failed to discover contact! Please, try again later!");

	private final String message;

	P2PDiscoverContactError(String message) {
		this.message = message;
	}

	@Override
	public String getErrorMessage() {
		return message;
	}

}
