package com.uzh.csg.overlaynetworks.domain.dto;

public class ContactWithStatus {

	private Contact contact;
	private boolean status;

	public ContactWithStatus(Contact contact, boolean status) {
		this.contact = contact;
		this.status = status;
	}
	public ContactWithStatus() { }

	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ContactWithStatusDTO{contact:"+contact+",status:"+status+"}";
	}

}
