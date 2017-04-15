package com.uzh.csg.overlaynetworks.domain.dto;

public class Login {

	private String name;

	public Login(String name) {
		this.name = name;
	}
	public Login() { }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "LoginDTO{name:"+name+"}";
	}
}
