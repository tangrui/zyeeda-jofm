package com.zyeeda.jofm.services;

public class InvalidScopeException extends Exception {

	private static final long serialVersionUID = -4268307784154144586L;
	
	private String scope;
	
	public InvalidScopeException(String scope) {
		this.scope = scope;
	}
	
	public String getScope() {
		return this.scope;
	}

}
