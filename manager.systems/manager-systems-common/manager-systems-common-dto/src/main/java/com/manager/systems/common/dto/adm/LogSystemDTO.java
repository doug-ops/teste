package com.manager.systems.common.dto.adm;

import java.io.Serializable;

public class LogSystemDTO implements Serializable 
{
	private static final long serialVersionUID = 7927565459010117025L;
	
	private long userChange;
	private String userChangeName;
	private String changeDateString;
	private String descriptionSystem;
	
	public LogSystemDTO() 
	{
		super();
	}
	
	public final long getUserChange() {
		return this.userChange;
	}

	public final void setUserChange(final long userChange) {
		this.userChange = userChange;
	}

	public final String getUserChangeName() {
		return this.userChangeName;
	}

	public final void setUserChangeName(final String userChangeName) {
		this.userChangeName = userChangeName;
	}

	public final String getChangeDateString() {
		return this.changeDateString;
	}

	public final void setChangeDateString(final String changeDateString) {
		this.changeDateString = changeDateString;
	}
	
	public final String getDescriptionSystem() {
		return this.descriptionSystem;
	}

	public final void setDescriptionSystem(final String descriptionSystem) {
		this.descriptionSystem = descriptionSystem;
	}
}