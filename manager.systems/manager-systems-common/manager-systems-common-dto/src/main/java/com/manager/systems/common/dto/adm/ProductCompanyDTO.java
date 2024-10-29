package com.manager.systems.common.dto.adm;

import java.io.Serializable;

import com.manager.systems.common.vo.ChangeData;

public class ProductCompanyDTO implements Serializable
{
	private static final long serialVersionUID = -1001880840337573958L;

	private long productId;
	private long companyId;
	private int bankAccountId;
	private boolean inactive;
	private ChangeData changeData;
	
	public ProductCompanyDTO() 
	{
		super();
	}

	public final long getProductId() 
	{
		return this.productId;
	}
	
	public final void setProductId(final long productId) 
	{
		this.productId = productId;
	}

	public final long getCompanyId() 
	{
		return this.companyId;
	}

	public final void setCompanyId(final long companyId) 
	{
		this.companyId = companyId;
	}

	public final int getBankAccountId() 
	{
		return this.bankAccountId;
	}

	public final void setBankAccountId(final int bankAccountId) 
	{
		this.bankAccountId = bankAccountId;
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
		return this.inactive ? 1 : 0;
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