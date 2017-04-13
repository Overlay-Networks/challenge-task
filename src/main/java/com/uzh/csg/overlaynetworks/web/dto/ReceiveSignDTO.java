package com.uzh.csg.overlaynetworks.web.dto;

public class ReceiveSignDTO {

	private long messageId;

	public ReceiveSignDTO(long messageId) {
		this.messageId = messageId;
	}
	public ReceiveSignDTO() { }

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	@Override
	public String toString() {
		return "ReceiveSignDTO{messageId:"+messageId+"}";
	}

}
