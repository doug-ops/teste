package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.utils.StringUtils;

public class ReportProductSubGroupDTO implements Serializable
{
	private static final long serialVersionUID = -4420564965968268798L;

	private String productSubGroupIdFrom;
	private String productSubGroupIdTo;
	private String productGroupIds;
	private String inactive;
	private String description;
	private int operation;
	private List<ProductSubGroupDTO> productSubGroups = new ArrayList<ProductSubGroupDTO>();
	
	public ReportProductSubGroupDTO() 
	{
		super();
	}
	
	public final int getProductSubGroupIdFrom()
	{
		return StringUtils.isLong(this.productSubGroupIdFrom) ? Integer.valueOf(this.productSubGroupIdFrom) : 0;
	}
	
	public final void setProductSubGroupIdFrom(final String productSubGroupIdFrom) 
	{
		this.productSubGroupIdFrom = productSubGroupIdFrom;
	}

	public final int getProductSubGroupIdTo()
	{
		return StringUtils.isLong(this.productSubGroupIdTo) ? Integer.valueOf(this.productSubGroupIdTo) : 0;
	}

	public final void setProductSubGroupIdTo(final String productSubGroupIdTo) 
	{
		this.productSubGroupIdTo = productSubGroupIdTo;
	}
	
	public final String getProductGroupIds()
	{
		return this.productGroupIds;
	}

	public final void setProductGroupIds(final String productGroupIds)
	{
		this.productGroupIds = productGroupIds;
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

	public List<ProductSubGroupDTO> geProductSubGroups() 
	{
		return this.productSubGroups;
	}
	
	public final void addItem(final ProductSubGroupDTO productSubGroup)
	{
		this.productSubGroups.add(productSubGroup);
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