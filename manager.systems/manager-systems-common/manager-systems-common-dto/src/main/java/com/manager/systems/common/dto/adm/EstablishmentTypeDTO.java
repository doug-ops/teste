package com.manager.systems.common.dto.adm;

import java.io.Serializable;

import com.manager.systems.common.vo.ChangeData;

public class EstablishmentTypeDTO implements Serializable
{
	private static final long serialVersionUID = 4092182836229660106L;
	
	private int id;
	private boolean inactive;
	private String description;
	private ChangeData changeData;
	
	public EstablishmentTypeDTO() 
	{
		super();
	}
		
	public final boolean isInactive() 
	{
		return this.inactive;
	}
	
	public final int getInactiveInt()
	{
		return this.inactive ? 1 : 0;
	}

	public final void setInactive(final boolean inactive)
	{
		this.inactive = inactive;
	}

	public final String getDescription() 
	{
		return this.description;
	}

	public final void setDescription(final String description)
	{
		this.description = description;
	}
	
	public final ChangeData getChangeData()
	{
		return this.changeData;
	}

	public final void setChangeData(final ChangeData changeData)
	{
		this.changeData = changeData;
	}

	public final int getId() {
		return this.id;
	}

	public final void setId(final int id) {
		this.id = id;
	}
}