package com.uzh.csg.overlaynetworks.domain.dto;

public class Contact {

	private String name;

	public Contact(String name) {
		this.name = name;
	}
	public Contact() { }

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
