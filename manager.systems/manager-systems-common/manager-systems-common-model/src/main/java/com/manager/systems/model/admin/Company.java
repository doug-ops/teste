package com.manager.systems.model.admin;

import java.io.Serializable;

import com.manager.systems.model.Person;

public class Company extends Person implements Serializable 
{
	private static final long serialVersionUID = 1466339689404243398L;
	
	private String socialName;
	private String fantasyName;
	
	public Company() 
	{
		super();
	}
	
	public final String getSocialName() 
	{
		return this.socialName;
	}

	public final void setSocialName(final String socialName)
	{
		this.socialName = socialName;
	}

	public final String getFantasyName()
	{
		return this.fantasyName;
	}

	public final void setFantasyName(final String fantasyName) 
	{
		this.fantasyName = fantasyName;
	}
}