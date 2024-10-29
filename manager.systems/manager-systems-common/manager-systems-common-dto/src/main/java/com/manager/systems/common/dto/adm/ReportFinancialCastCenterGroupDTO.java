package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.utils.StringUtils;

public class ReportFinancialCastCenterGroupDTO implements Serializable
{
	private static final long serialVersionUID = -8113874241302377355L;

	private String financialCastCenterGroupIdFrom;
	private String financialCastCenterGroupIdTo;
	private String inactive;
	private String description;
	private int operation;
	private List<FinancialCastCenterGroupDTO> financialCastCenterGroups = new ArrayList<FinancialCastCenterGroupDTO>();
	
	public ReportFinancialCastCenterGroupDTO() 
	{
		super();
	}
	
	public final int getFinancialCastCenterGroupIdFrom()
	{
		return StringUtils.isLong(this.financialCastCenterGroupIdFrom) ? Integer.valueOf(this.financialCastCenterGroupIdFrom) : 0;
	}
	
	public final void setFinancialCastCenterGroupIdFrom(final String financialCastCenterGroupIdFrom) 
	{
		this.financialCastCenterGroupIdFrom = financialCastCenterGroupIdFrom;
	}

	public final int getFinancialCastCenterGroupIdTo()
	{
		return StringUtils.isLong(this.financialCastCenterGroupIdTo) ? Integer.valueOf(this.financialCastCenterGroupIdTo) : 0;
	}

	public final void setFinancialCastCenterGroupIdTo(final String financialCastCenterGroupIdTo) 
	{
		this.financialCastCenterGroupIdTo = financialCastCenterGroupIdTo;
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

	public final List<FinancialCastCenterGroupDTO> gefinancialCastCenterGroups() 
	{
		return this.financialCastCenterGroups;
	}
	
	public final void addItem(final FinancialCastCenterGroupDTO financialCastCenterGroup)
	{
		this.financialCastCenterGroups.add(financialCastCenterGroup);
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