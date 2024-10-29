package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.Calendar;

public class IntegrationSystemsDTO implements Serializable 
{
	private static final long serialVersionUID = 6534269077684857876L;

	private String objectId;
	private String objectType;
	private int integrationSystemId;
	private String description;
	private String legacyId;
	private boolean inactive;
	private long userChange;
	private Calendar changeDate;
	
	public IntegrationSystemsDTO() 
	{
		super();
	}

	public final String getObjectId()
	{
		return this.objectId;
	}

	public final void setObjectId(final String objectId) 
	{
		this.objectId = objectId;
	}

	public final String getObjectType()
	{
		return this.objectType;
	}

	public final void setObjectType(final String objectType)
	{
		this.objectType = objectType;
	}

	public final int getIntegrationSystemId() 
	{
		return this.integrationSystemId;
	}

	public final void setIntegrationSystemId(final int integrationSystemId)
	{
		this.integrationSystemId = integrationSystemId;
	}

	public final String getLegacyId() 
	{
		return this.legacyId;
	}

	public final void setLegacyId(final String legacyId)
	{
		this.legacyId = legacyId;
	}
	
	public final boolean isInactive() 
	{
		return this.inactive;
	}
	
	public final int getInactive() 
	{
		return this.inactive ? 1 : 0;
	}

	public final void setInactive(final boolean inactive)
	{
		this.inactive = inactive;
	}

	public final long getUserChange() 
	{
		return this.userChange;
	}

	public final void setUserChange(final long userChange)
	{
		this.userChange = userChange;
	}

	public final Calendar getChangeDate() 
	{
		return this.changeDate;
	}

	public final void setChangeDate(final Calendar changeDate) 
	{
		this.changeDate = changeDate;
	}

	public final String getDescription() 
	{
		return this.description;
	}

	public final void setDescription(final String description) 
	{
		this.description = description;
	}
}