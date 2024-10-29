package com.manager.systems.common.dto.adm;

import java.io.Serializable;

public class AccessProfilePermissionFilterDTO implements Serializable 
{
	private static final long serialVersionUID = 8329184142817019434L;

	private int permissionId;
	private int permissionParentId;
	private int accessProfileId;
	private boolean inactive;
	
	public AccessProfilePermissionFilterDTO() 
	{
		super();
	}

	public final int getPermissionId() 
	{
		return this.permissionId;
	}

	public final void setPermissionId(final int permissionId) 
	{
		this.permissionId = permissionId;
	}

	public final int getPermissionParentId()
	{
		return this.permissionParentId;
	}

	public final void setPermissionParentId(final int permissionParentId)
	{
		this.permissionParentId = permissionParentId;
	}

	public final int getAccessProfileId() 
	{
		return this.accessProfileId;
	}

	public final void setAccessProfileId(final int accessProfileId) 
	{
		this.accessProfileId = accessProfileId;
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