/*
 * Creation date 22/05/2024.
 */
package com.manager.systems.model;

public enum UserComboboxEnum {
	CASHING_CLOSING_FINISH(1),
	CASHING_CLOSING_PREVIEW(2);
	
	private int code;
	
	private UserComboboxEnum(final int code) 
	{
		this.code = code;
	}
	
	public final int getCode() {
		return this.code;
	}
}
