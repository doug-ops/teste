package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.manager.systems.common.utils.StringUtils;

public class ReportProductDTO implements Serializable
{
	private static final long serialVersionUID = -740976885962990668L;
	
	private int id;
	private String productIdFrom;
	private String productIdTo;
	private String productGroupIds;
	private String productSubGroupIds;
	private String inactive;
	private String description;
	private String companyId;
	private String bankAccountId;
	private String bankAccountDestinyId;
	private long user;
	private List<ProductDTO> products = new ArrayList<ProductDTO>();
	private Map<Integer, List<ProductDTO>> addProducts = new TreeMap<Integer, List<ProductDTO>>();

	public ReportProductDTO() 
	{
		super();
	}
	
	public int getId() 
	{
		return this.id;
	}
	
	public void setId(final int id) 
	{
		this.id = id;
	}
	
	public final long getProductIdFrom()
	{
		return StringUtils.isLong(this.productIdFrom) ? Long.valueOf(this.productIdFrom) : 0L;
	}
	
	public final void setProductIdFrom(final String productIdFrom) 
	{
		this.productIdFrom = productIdFrom;
	}

	public final long getProductIdTo()
	{
		return StringUtils.isLong(this.productIdTo) ? Integer.valueOf(this.productIdTo) : 0;
	}

	public final void setProductIdTo(final String productIdTo) 
	{
		this.productIdTo = productIdTo;
	}
	
	public final String getProductGroupIds()
	{
		return this.productGroupIds;
	}

	public final void setProductGroupIds(final String productGroupIds)
	{
		this.productGroupIds = productGroupIds;
	}
	
	public final String getProductSubGroupIds()
	{
		return this.productSubGroupIds;
	}

	public final void setProductSubGroupIds(final String productSubGroupIds)
	{
		this.productSubGroupIds = productSubGroupIds;
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

	public List<ProductDTO> geProducts() 
	{
		return this.products;
	}
	
	public final void addItem(final ProductDTO product)
	{
		this.products.add(product);
	}

	public final String getCompanyId() 
	{
		return this.companyId;
	}

	public final void setCompanyId(final String companyId) 
	{
		this.companyId = companyId;
	}

	public final String getBankAccountId() 
	{
		return this.bankAccountId;
	}

	public final void setBankAccountId(final String bankAccountId) 
	{
		this.bankAccountId = bankAccountId;
	}
	
	public final String getBankAccountDestinyId() {
		return this.bankAccountDestinyId;
	}

	public final void setBankAccountDestinyId(final String bankAccountDestinyId) {
		this.bankAccountDestinyId = bankAccountDestinyId;
	}

	public final long getUser() {
		return this.user;
	}

	public final void setUser(final long user) {
		this.user = user;
	}
	
	public final Map<Integer, List<ProductDTO>> getAddProducts() 
	{
		return this.addProducts;
	}
	
	public final void addProduct(final int key, final ProductDTO item)
	{
		List<ProductDTO> itens = this.addProducts.get(key);
		if(itens==null)
		{
			itens = new ArrayList<ProductDTO>();
		}
		itens.add(item);
		this.addProducts.put(key, itens);
	}
}