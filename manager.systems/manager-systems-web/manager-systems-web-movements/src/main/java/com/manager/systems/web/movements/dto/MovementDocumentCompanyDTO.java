/**
 *Create Date 21/02/2022
 */
package com.manager.systems.web.movements.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MovementDocumentCompanyDTO implements Serializable 
{
	private static final long serialVersionUID = -5234403116121957942L;
	
	private String dataTo;
	private String dataFrom;
	private double total;
	
	private Map<Long, MovementDocumentCompanyItemDTO> itens = new TreeMap<Long, MovementDocumentCompanyItemDTO>();

	public MovementDocumentCompanyDTO() 
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

	public final Map<Long, MovementDocumentCompanyItemDTO> getItens() {
		return  this.itens;
	}
	
	public final void sumTotal(final double total)
	{
		this.total += total;
	}
	
	public final void addItem (final DocumentMovementItemDTO item) throws Exception{
		final long companyId = item.getCompanyId();
		final String companyDescription = item.getCompany();
		final double documentValue = item.getDocumentValue();
		
		MovementDocumentCompanyItemDTO itemCompany = this.itens.get(companyId);
		if(itemCompany == null) {
			itemCompany = new MovementDocumentCompanyItemDTO();			
			itemCompany.setCompanyId(companyId);
			itemCompany.setCompanyDescription(companyDescription);
		}
			
		itemCompany.sumTotal(item.isCredit() ? documentValue : (documentValue < 0 ? documentValue : (documentValue * -1)));
		this.itens.put(companyId, itemCompany);		
	}
	
	public final List<MovementDocumentCompanyItemDTO> getOrdernadItens(){
		
		final List<MovementDocumentCompanyItemDTO> companys = new ArrayList<MovementDocumentCompanyItemDTO>(this.itens.values());
		Collections.sort(companys, new Comparator<MovementDocumentCompanyItemDTO>()
		{
			@Override
			public int compare(final MovementDocumentCompanyItemDTO o1, final MovementDocumentCompanyItemDTO o2)
			{
				return o1.getCompanyDescription().compareTo(o2.getCompanyDescription());
			}
		});
		
		return companys;
	}
}