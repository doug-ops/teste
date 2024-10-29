package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.utils.StringUtils;

public class ReportAccessProfileDTO implements Serializable
{
	private static final long serialVersionUID = -3987913989528926680L;
	
	private String idFrom;
	private String idTo;
	private String inactive;
	private String description;
	private int operation;
	private List<AccessProfileDTO> itens = new ArrayList<AccessProfileDTO>();
	
	public ReportAccessProfileDTO() 
	{
		super();
	}
	
	public final int getIdFrom()
	{
		return StringUtils.isLong(this.idFrom) ? Integer.valueOf(this.idFrom) : 0;
	}
	
	public final void setIdFrom(final String idFrom) 
	{
		this.idFrom = idFrom;
	}

	public final int getIdTo()
	{
		return StringUtils.isLong(this.idTo) ? Integer.valueOf(this.idTo) : 0;
	}

	public final void setIdTo(final String idTo) 
	{
		this.idTo = idTo;
	}

	public final int getInactive() 
	{
		return StringUtils.isLong(this.inactive) ? Integer.valueOf(this.inactive) : 2;
	}

	public final void setInactive(final String inactive) 
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

	public final List<AccessProfileDTO> getItens() 
	{
		return this.itens;
	}
	
	public final void addItem(final AccessProfileDTO item)
	{
		this.itens.add(item);
	}

	public final int getOperation()
	{
		return this.operation;
	}

	public final void setOperation(final int operation)
	{
		this.operation = operation;
	}
}