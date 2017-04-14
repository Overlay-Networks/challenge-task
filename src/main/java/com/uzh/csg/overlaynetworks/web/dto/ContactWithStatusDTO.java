package com.uzh.csg.overlaynetworks.web.dto;

public class ContactWithStatusDTO {

	private ContactDTO contact;
	private boolean status;

	public ContactWithStatusDTO(ContactDTO contact, boolean status) {
		this.contact = contact;
		this.status = status;
	}
	public ContactWithStatusDTO() { }

	public ContactDTO getContact() {
		return contact;
	}
	public void setContact(ContactDTO contact) {
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
