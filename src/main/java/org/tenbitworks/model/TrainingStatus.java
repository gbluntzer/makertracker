package org.tenbitworks.model;

public enum TrainingStatus {
	COMPLETE("Completed"),
	PARTIAL("Partially Complete"),
    NOT_NEEDED("Not Needed");
	
	private final String text;
	
	private TrainingStatus(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
}
