package org.tenbitworks.model;

public enum AssetStatus {
	OWNED("Owned"),
	LOANED("Loaned"),
	REMOVED("Removed"),
	BORROWED("Borrowed by Member");
	
	private final String text;
	
	private AssetStatus(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
}
