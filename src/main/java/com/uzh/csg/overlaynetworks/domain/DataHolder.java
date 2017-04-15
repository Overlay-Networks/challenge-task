package com.uzh.csg.overlaynetworks.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.uzh.csg.overlaynetworks.domain.dto.Contact;

@Service
public class DataHolder {

	private boolean authenticated;
	private String username;
	private Set<Contact> contacts = new HashSet<>();

	public boolean isAuthenticated() {
		return authenticated;
	}
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Contact> getContacts() {
		return contacts;
	}
	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}

	@Override
	public String toString() {
		return "DataHolder [authenticated="
				+ authenticated
				+ ", username="
				+ username
				+ ", contacts="
				+ contacts
				+ "]";
	}
}