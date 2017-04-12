package com.uzh.csg.overlaynetworks.web.dto;

public class ResultDTO {

	private boolean success;

	public ResultDTO(boolean success) {
		this.success = success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean getSuccess() {
		return success;
	}

}
