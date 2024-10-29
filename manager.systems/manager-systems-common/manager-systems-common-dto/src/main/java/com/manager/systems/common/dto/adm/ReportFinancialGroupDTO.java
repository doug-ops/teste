package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import com.manager.systems.common.utils.StringUtils;

public class ReportFinancialGroupDTO implements Serializable
{
	private static final long serialVersionUID = 6675144930683802010L;

	private String financialGroupIdFrom;
	private String financialGroupIdTo;
	private String inactive;
	private String description;
	private int operation;
	private Map<String, FinancialGroupDTO> financialGroups = new TreeMap<String, FinancialGroupDTO>();
	
	public ReportFinancialGroupDTO() 
	{
		super();
	}
	
	public final String getFinancialGroupIdFrom()
	{
		return this.financialGroupIdFrom;
	}
	
	public final void setFinancialGroupIdFrom(final String financialGroupIdFrom) 
	{
		this.financialGroupIdFrom = financialGroupIdFrom;
	}

	public final String getFinancialGroupIdTo()
	{
		return this.financialGroupIdTo;
	}

	public final void setFinancialGroupIdTo(final String financialGroupIdTo) 
	{
		this.financialGroupIdTo = financialGroupIdTo;
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

	public final Map<String, FinancialGroupDTO> geFinancialGroups() 
	{
		return this.financialGroups;
	}
	
	public final void addItem(final FinancialGroupDTO item)
	{
		this.financialGroups.put(item.getId(), item);
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