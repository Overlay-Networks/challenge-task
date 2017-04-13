package com.uzh.csg.overlaynetworks.web.dto;

public class ReceiveMessageDTO {

	private FriendDTO sender;
	private String message;

	public ReceiveMessageDTO(FriendDTO sender, String message) {
		this.sender = sender;
		this.message = message;
	}
	public ReceiveMessageDTO() { }

	public FriendDTO getSender() {
		return sender;
	}

	public void setSender(FriendDTO sender) {
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
