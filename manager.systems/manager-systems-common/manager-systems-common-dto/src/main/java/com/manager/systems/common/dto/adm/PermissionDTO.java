package com.manager.systems.common.dto.adm;

import java.io.Serializable;

public class PermissionDTO implements Serializable 
{
	private static final long serialVersionUID = -3846869138775864568L;

	private int id;
	private int parentId;
	private String description;
	private String role;
	private boolean inactive;
	
	public PermissionDTO() 
	{
		super();
	}

	public final int getId()
	{
		return this.id;
	}

	public final void setId(final int id)
	{
		this.id = id;
	}

	public final int getParentId() 
	{
		return this.parentId;
	}

	public final void setParentId(final int parentId) 
	{
		this.parentId = parentId;
	}

	public final String getDescription()
	{
		return this.description;
	}

	public final void setDescription(final String description)
	{
		this.description = description;
	}

	public final String getRole() 
	{
		return this.role;
	}

	public final void setRole(final String role) 
	{
		this.role = role;
	}

	public final boolean isInactive()
	{
		return this.inactive;
	}

	public final void setInactive(final boolean inactive) 
	{
		this.inactive = inactive;
	}
}