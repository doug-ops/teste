package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.utils.StringUtils;

public class ReportFinancialSubGroupDTO implements Serializable
{
	private static final long serialVersionUID = -4420564965968268798L;

	private String financialSubGroupIdFrom;
	private String financialSubGroupIdTo;
	private String financialGroupIds;
	private String inactive;
	private String description;
	private int operation;
	private List<FinancialSubGroupDTO> financialSubGroups = new ArrayList<FinancialSubGroupDTO>();
	
	public ReportFinancialSubGroupDTO() 
	{
		super();
	}
	
	public final String getFinancialSubGroupIdFrom()
	{
		return this.financialSubGroupIdFrom;
	}
	
	public final void setFinancialSubGroupIdFrom(final String financialSubGroupIdFrom) 
	{
		this.financialSubGroupIdFrom = financialSubGroupIdFrom;
	}

	public final String getFinancialSubGroupIdTo()
	{
		return this.financialSubGroupIdTo;
	}

	public final void setFinancialSubGroupIdTo(final String financialSubGroupIdTo) 
	{
		this.financialSubGroupIdTo = financialSubGroupIdTo;
	}
	
	public final String getFinancialGroupIds()
	{
		return this.financialGroupIds;
	}

	public final void setFinancialGroupIds(final String financialGroupIds)
	{
		this.financialGroupIds = financialGroupIds;
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

	public List<FinancialSubGroupDTO> geFinancialSubGroups() 
	{
		return this.financialSubGroups;
	}
	
	public final void addItem(final FinancialSubGroupDTO financialSubGroup)
	{
		this.financialSubGroups.add(financialSubGroup);
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