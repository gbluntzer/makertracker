package org.tenbitworks.model;

public enum MemberStatus {
	VISITOR("Visitor"),
	MEMBER("Member"),
	OFFICER("Officer"),
	ADMIN("Admin");
	
	private final String text;
	
	private MemberStatus(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
}
