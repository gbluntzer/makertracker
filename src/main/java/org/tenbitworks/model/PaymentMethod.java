package org.tenbitworks.model;

public enum PaymentMethod {
	NONE("None"),
	PAYPAL("Paypal"),
	DWOLLA("Dwolla"),
	CASH("Cash");

	private final String text;

	private PaymentMethod(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
