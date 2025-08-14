package com.infinite.jsf.provider.controller;

public enum ConstMessage {
	OLD_PASSWORD_REQUIRED("Old password is required."), 
	NEW_PASSWORD_REQUIRED("New password is required."),
	CONFIRM_PASSWORD_REQUIRED("Confirm password is required."),
	PASSWORD_MISMATCH("New password and confirm password do not match."),
	PASSWORD_REUSE("New password must be different from old password.");

	private final String message;

	ConstMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
