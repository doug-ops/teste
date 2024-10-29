package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.utils.StringUtils;

public class ReportProductGroupDTO implements Serializable
{
	private static final long serialVersionUID = -8113874241302377355L;

	private String productGroupIdFrom;
	private String productGroupIdTo;
	private String inactive;
	private String description;
	private int operation;
	private List<ProductGroupDTO> productGroups = new ArrayList<ProductGroupDTO>();
	
	public ReportProductGroupDTO() 
	{
		super();
	}
	
	public final int getProductGroupIdFrom()
	{
		return StringUtils.isLong(this.productGroupIdFrom) ? Integer.valueOf(this.productGroupIdFrom) : 0;
	}
	
	public final void setProductGroupIdFrom(final String productGroupIdFrom) 
	{
		this.productGroupIdFrom = productGroupIdFrom;
	}

	public final int getProductGroupIdTo()
	{
		return StringUtils.isLong(this.productGroupIdTo) ? Integer.valueOf(this.productGroupIdTo) : 0;
	}

	public final void setProductGroupIdTo(final String productGroupIdTo) 
	{
		this.productGroupIdTo = productGroupIdTo;
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

	public final List<ProductGroupDTO> geProductGroups() 
	{
		return this.productGroups;
	}
	
	public final void addItem(final ProductGroupDTO productGroup)
	{
		this.productGroups.add(productGroup);
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