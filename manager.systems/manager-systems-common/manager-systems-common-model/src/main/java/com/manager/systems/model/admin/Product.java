package com.manager.systems.model.admin;

import java.io.Serializable;

import com.manager.systems.common.vo.ChangeData;

public class Product implements Serializable
{
	private static final long serialVersionUID = -1104079792823117504L;

	private long id;
	private String description;
	private boolean inactive;
	private long companyId;
	private int groupId;
	private int subGroupId;
	private double salePrice;
	private double costPrice;
	private double conversionFactor;
	private long inputMovement;
	private long outputMovement;
	private ChangeData changeData;
	
	public Product() 
	{
		super();
	}

	public final long getId()
	{
		return this.id;
	}

	public final void setId(final long id)
	{
		this.id = id;
	}

	public final String getDescription() 
	{
		return this.description;
	}

	public final void setDescription(final String description) 
	{
		this.description = description;
	}

	public final boolean isInactive() 
	{
		return this.inactive;
	}

	public final void setInactive(final boolean inactive) 
	{
		this.inactive = inactive;
	}

	public final long getCompanyId() 
	{
		return this.companyId;
	}

	public final void setCompanyId(final long companyId)
	{
		this.companyId = companyId;
	}

	public final double getSalePrice()
	{
		return this.salePrice;
	}

	public final void setSalePrice(final double salePrice) 
	{
		this.salePrice = salePrice;
	}

	public final double getCostPrice()
	{
		return this.costPrice;
	}

	public final void setCostPrice(final double costPrice) 
	{
		this.costPrice = costPrice;
	}

	public final double getConversionFactor()
	{
		return this.conversionFactor;
	}

	public final void setConversionFactor(final double conversionFactor)
	{
		this.conversionFactor = conversionFactor;
	}

	public final int getGroupId()
	{
		return this.groupId;
	}

	public final void setGroupId(final int groupId)
	{
		this.groupId = groupId;
	}

	public final int getSubGroupId() 
	{
		return this.subGroupId;
	}

	public final void setSubGroupId(final int subGroupId) 
	{
		this.subGroupId = subGroupId;
	}

	public final long getInputMovement() 
	{
		return this.inputMovement;
	}

	public final void setInputMovement(final long inputMovement)
	{
		this.inputMovement = inputMovement;
	}

	public final long getOutputMovement() 
	{
		return this.outputMovement;
	}

	public final void setOutputMovement(final long outputMovement) 
	{
		this.outputMovement = outputMovement;
	}

	public final ChangeData getChangeData() 
	{
		return this.changeData;
	}

	public final void setChangeData(final ChangeData changeData) 
	{
		this.changeData = changeData;
	}
}