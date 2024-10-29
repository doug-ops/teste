package com.manager.systems.common.dto.adm;

import java.io.Serializable;

import com.manager.systems.common.vo.ChangeData;

public class FinancialTransferProductDTO implements Serializable
{
	private static final long serialVersionUID = -7963083415429774918L;

	private int groupItem;
	private long bankAccountOriginId;
    private long bankAccountDestinyId;
    private long productId;
    private String productDescription;
	private boolean inactive;
	private ChangeData changeData;
	
	public FinancialTransferProductDTO() 
	{
		super();
	}

	public final int getGroupItem() 
	{
		return this.groupItem;
	}

	public final void setGroupItem(final int groupItem) 
	{
		this.groupItem = groupItem;
	}

	public final long getBankAccountOriginId() 
	{
		return this.bankAccountOriginId;
	}

	public final void setBankAccountOriginId(final long bankAccountOriginId) 
	{
		this.bankAccountOriginId = bankAccountOriginId;
	}

	public final long getBankAccountDestinyId() 
	{
		return this.bankAccountDestinyId;
	}

	public final void setBankAccountDestinyId(final long bankAccountDestinyId) 
	{
		this.bankAccountDestinyId = bankAccountDestinyId;
	}

	public final long getProductId() 
	{
		return this.productId;
	}

	public final void setProductId(final long productId) 
	{
		this.productId = productId;
	}

	public final boolean isInactive() 
	{
		return this.inactive;
	}

	public final void setInactive(final boolean inactive) 
	{
		this.inactive = inactive;
	}
	
	public final int getInactiveInt()
	{
		return (this.inactive ? 1 : 0);
	}

	public final ChangeData getChangeData()
	{
		return this.changeData;
	}

	public final void setChangeData(final ChangeData changeData) 
	{
		this.changeData = changeData;
	}
	
	public String getProductDescription() 
	{
		return this.productDescription;
	}
	
	public void setProductDescription(final String productDescription) 
	{
		this.productDescription = productDescription;
	}
}