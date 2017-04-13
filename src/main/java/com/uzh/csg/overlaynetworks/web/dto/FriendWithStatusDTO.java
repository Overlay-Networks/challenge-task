package com.uzh.csg.overlaynetworks.web.dto;

public class FriendWithStatusDTO {

	private FriendDTO friend;
	private boolean status;

	public FriendWithStatusDTO(FriendDTO friend, boolean status) {
		this.friend = friend;
		this.status = status;
	}
	public FriendWithStatusDTO() { }

	public FriendDTO getFriend() {
		return friend;
	}
	public void setFriend(FriendDTO friend) {
		this.friend = friend;
	}

	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "FriendWithStatusDTO{friend:"+friend+",status:"+status+"}";
	}

}
