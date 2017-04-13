package com.uzh.csg.overlaynetworks.web.dto;

public class FriendDTO {

	private String name;

	public FriendDTO(String name) {
		this.name = name;
	}
	public FriendDTO() { }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "FriendDTO{name:"+name+"}";
	}
}
