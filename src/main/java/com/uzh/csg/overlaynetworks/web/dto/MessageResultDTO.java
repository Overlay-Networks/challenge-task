package com.uzh.csg.overlaynetworks.web.dto;

public class MessageResultDTO {

	private boolean success;
	private long messageId;

	public MessageResultDTO(boolean success, long messageId) {
		this.success = success;
		this.messageId = messageId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

}
