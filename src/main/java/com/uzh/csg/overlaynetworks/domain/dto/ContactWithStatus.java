package com.uzh.csg.overlaynetworks.domain.dto;

public class ContactWithStatus {

	private Contact contact;
	private ContactStatus status;

	public ContactWithStatus(Contact contact, ContactStatus status) {
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

	public ContactStatus getStatus() {
		return status;
	}
	public void setStatus(ContactStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ContactWithStatusDTO{contact:"+contact+",status:"+status+"}";
	}

}
