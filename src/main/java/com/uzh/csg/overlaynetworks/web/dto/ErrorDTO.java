package com.uzh.csg.overlaynetworks.web.dto;

public class ErrorDTO {

	private String type;
	private String message;

	public ErrorDTO(Exception ex) {
		this.setType(ex.getClass().getSimpleName());
		this.setMessage(ex.getMessage());
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}