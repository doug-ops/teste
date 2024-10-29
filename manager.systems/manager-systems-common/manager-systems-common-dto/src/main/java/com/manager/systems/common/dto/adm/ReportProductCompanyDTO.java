package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.vo.ChangeData;

public class ReportProductCompanyDTO implements Serializable
{
	private static final long serialVersionUID = 175106517687473343L;
	
	private long productId;
	private long companyId;
	private int bankAccountId;
	private boolean inactive;
	private ChangeData changeData;
	private List<ProductCompanyDTO> productsCompanys = new ArrayList<ProductCompanyDTO>();
	
	public ReportProductCompanyDTO() 
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
	
	public final List<ProductCompanyDTO> getProductsCompanys() 
	{
		return this.productsCompanys;
	}

	public final void addItem(final ProductCompanyDTO item)
	{
		this.productsCompanys.add(item);
	}
}