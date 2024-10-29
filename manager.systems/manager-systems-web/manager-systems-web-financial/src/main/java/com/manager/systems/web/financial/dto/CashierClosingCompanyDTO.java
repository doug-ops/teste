/**
 *Create Date 21/02/2022
 */
package com.manager.systems.web.financial.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CashierClosingCompanyDTO implements Serializable 
{
	private static final long serialVersionUID = -5234403116121957942L;
	
	private String dataTo;
	private String dataFrom;
	private double total;
	
	private Map<Long, CashierClosingCompanyItemDTO> itens = new TreeMap<Long, CashierClosingCompanyItemDTO>();

	public CashierClosingCompanyDTO() 
	{
		super();
	}
	
	public final String getDataTo() {
		return this.dataTo;
	}

	public final void setDataTo(final String dataTo) {
		this.dataTo = dataTo;
	}

	public final String getDataFrom() {
		return this.dataFrom;
	}

	public final void setDataFrom(final String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public final double getTotal() {
		return this.total;
	}

	public final void setTotal(final double total) {
		this.total = total;
	}

	public final Map<Long, CashierClosingCompanyItemDTO> getItens() {
		return  this.itens;
	}
	
	public final void sumTotal(final double total)
	{
		this.total += total;
	}
	
	public final void addItem (final CashierClosingItemDTO item) throws Exception{
		final long companyId = item.getCompanyId();
		final String companyDescription = item.getCompany();
		final double documentValue = item.getDocumentValue();
		
		CashierClosingCompanyItemDTO itemCompany = this.itens.get(companyId);
		if(itemCompany == null) {
			itemCompany = new CashierClosingCompanyItemDTO();			
			itemCompany.setCompanyId(companyId);
			itemCompany.setCompanyDescription(companyDescription);
		}
			
		itemCompany.sumTotal(item.isCredit() ? documentValue : (documentValue < 0 ? documentValue : (documentValue * -1)));
		this.itens.put(companyId, itemCompany);		
	}
	
	public final void addItemCashierClosing (final CashierClosingItemDTO item) throws Exception{
		final long companyId = item.getCompanyId();
		final String companyDescription = item.getCompany();
		final double documentValue = item.getDocumentValue();
		
		CashierClosingCompanyItemDTO itemCompany = this.itens.get(companyId);
		if(itemCompany == null) {
			itemCompany = new CashierClosingCompanyItemDTO();			
			itemCompany.setCompanyId(companyId);
			itemCompany.setCompanyDescription(companyDescription);
		}
			
		itemCompany.sumTotal(item.isCredit() ? documentValue : (documentValue < 0 ? documentValue : (documentValue * -1)));
		this.itens.put(companyId, itemCompany);		
	}
	
	public final List<CashierClosingCompanyItemDTO> getOrdernadItens(){
		
		final List<CashierClosingCompanyItemDTO> companys = new ArrayList<CashierClosingCompanyItemDTO>(this.itens.values());
		Collections.sort(companys, new Comparator<CashierClosingCompanyItemDTO>()
		{
			@Override
			public int compare(final CashierClosingCompanyItemDTO o1, final CashierClosingCompanyItemDTO o2)
			{
				return o1.getCompanyDescription().compareTo(o2.getCompanyDescription());
			}
		});
		
		return companys;
	}
}