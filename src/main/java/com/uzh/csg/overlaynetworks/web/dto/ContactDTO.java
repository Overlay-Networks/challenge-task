package com.uzh.csg.overlaynetworks.web.dto;

public class ContactDTO {

	private String name;

	public ContactDTO(String name) {
		this.name = name;
	}
	public ContactDTO() { }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ContactDTO{name:"+name+"}";
	}
}
