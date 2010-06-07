package com.zyeeda.jofm.services;

public class InvalidOperationException extends Exception {

	private static final long serialVersionUID = 520289653399155684L;
	
	private String operation;
	
	public InvalidOperationException(String operation) {
		this.operation = operation;
	}
	
	public String getOperation() {
		return this.operation;
	}

}
