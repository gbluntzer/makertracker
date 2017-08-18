package org.tenbitworks.model;

public enum MemberStatus {
	MEMBER("Member"),
	INACTIVE_MEMBER("Inactive Member"),
	OFFICER("Officer");

	private final String text;

	private MemberStatus(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
