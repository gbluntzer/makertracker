package org.tenbitworks.model;

public enum Roles { //TODO Creates roles drop down on users page
	USER("User"),
	ADMIN("Admin"),
	API("Api");
	
	private final String text;

	private Roles(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
