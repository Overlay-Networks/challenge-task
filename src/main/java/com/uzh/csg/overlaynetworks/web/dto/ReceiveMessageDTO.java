package com.uzh.csg.overlaynetworks.web.dto;

public class ReceiveMessageDTO {

	private ContactDTO sender;
	private String message;

	public ReceiveMessageDTO(ContactDTO sender, String message) {
		this.sender = sender;
		this.message = message;
	}
	public ReceiveMessageDTO() { }

	public ContactDTO getSender() {
		return sender;
	}

	public void setSender(ContactDTO sender) {
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ReceiveMessageDTO{sender:"+sender+",message:"+message+",}";
	}

}
