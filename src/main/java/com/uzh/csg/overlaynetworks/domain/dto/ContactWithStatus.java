package com.uzh.csg.overlaynetworks.domain.dto;

public class ContactWithStatus {

	private Contact contact;
	private boolean isOnline;

	public ContactWithStatus(Contact contact, boolean isOnline) {
		this.contact = contact;
		this.isOnline = isOnline;
	}
	public ContactWithStatus() { }

	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public boolean getStatus() {
		return isOnline;
	}
	public void setStatus(boolean isOnline) {
		this.isOnline = isOnline;
	}

	@Override
	public String toString() {
		return "ContactWithStatusDTO{contact:"+contact+",status:"+isOnline+"}";
	}

}
