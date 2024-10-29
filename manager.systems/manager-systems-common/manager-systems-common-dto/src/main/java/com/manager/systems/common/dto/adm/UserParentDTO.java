package com.manager.systems.common.dto.adm;

import java.io.Serializable;

public class UserParentDTO implements Serializable 
{
	private static final long serialVersionUID = -9101021886035686924L;

	private long userId;
	private long userParentId;
	private long userChange;
	private boolean inactive;
	
	public UserParentDTO() 
	{
		super();
	}

	public final long getUserId() 
	{
		return this.userId;
	}

	public final void setUserId(final long userId) 
	{
		this.userId = userId;
	}

	public final long getUserParentId()
	{
		return this.userParentId;
	}

	public final void setUserParentId(final long userParentId) 
	{
		this.userParentId = userParentId;
	}

	public final long getUserChange() 
	{
		return this.userChange;
	}

	public final void setUserChange(final long userChange) 
	{
		this.userChange = userChange;
	}

	public final boolean isInactive()
	{
		return this.inactive;
	}

	public final void setInactive(final boolean inactive) 
	{
		this.inactive = inactive;
	}

	public int getInactiveInt() 
	{
		int result = 0;
		if(this.inactive)
		{
			result = 1;
		}
		return result;
	}
}