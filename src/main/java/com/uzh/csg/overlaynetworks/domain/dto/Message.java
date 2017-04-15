package com.uzh.csg.overlaynetworks.web.dto;

public class MessageDTO {

	private ContactDTO receiver;
	private String message;

	public MessageDTO(ContactDTO receiver, String message) {
		this.receiver = receiver;
		this.message = message;
	}
	public MessageDTO() { }

	public ContactDTO getReceiver() {
		return receiver;
	}

	public void setReceiver(ContactDTO receiver) {
		this.receiver = receiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MessageDTO{receiver:"+receiver+",message:"+message+",}";
	}

}
