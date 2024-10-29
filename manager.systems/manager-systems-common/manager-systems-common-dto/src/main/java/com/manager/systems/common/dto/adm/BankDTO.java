package com.manager.systems.common.dto.adm;

import java.io.Serializable;

public class BankDTO implements Serializable 
{
	private static final long serialVersionUID = -8048495126664078985L;

	private String id;
	private String description;
	
	public BankDTO() 
	{
		super();
	}
	
	public String getId() 
	{
		return id;
	}
	
	public void setId(final String id)
	{
		this.id = id;
	}
	
	public String getDescription() 
	{
		return this.description;
	}
	
	public void setDescription(final String description) 
	{
		this.description = description;
	}
}