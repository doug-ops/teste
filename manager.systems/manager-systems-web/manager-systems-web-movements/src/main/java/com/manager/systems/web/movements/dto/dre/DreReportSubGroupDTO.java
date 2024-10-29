package com.manager.systems.web.movements.dto.dre;

import java.io.Serializable;
import java.util.TreeMap;

public class DreReportSubGroupDTO implements Serializable
{
	private static final long serialVersionUID = -6234795068319514896L;

	private String idSubGroup;
	private String descriptionSubGroup;
	private double total;
	private double percentSales;
	private int order;	
	private double totalCredit;
	private double totalDebit;
	private double totalBalance;

	private TreeMap<Long, DreReportItemDTO> mapItems = new TreeMap<Long, DreReportItemDTO>();
	
	public DreReportSubGroupDTO() {
		super();
	}

	public final void addItem(final DreReportItemDTO item) 
	{				
		this.mapItems.put(item.getDocumentId(), item);
	}
	
	public final String getIdSubGroup() {
		return idSubGroup;
	}
	
	public final void setIdSubGroup(String idSubGroup) {
		this.idSubGroup = idSubGroup;
	}

	public final String getDescriptionSubGroup() {
		return descriptionSubGroup;
	}
	
	public final void setDescriptionSubGroup(String descriptionSubGroup) {
		this.descriptionSubGroup = descriptionSubGroup;
	}

	public final double getTotal() {
		return total;
	}

	public final void sumTotal(double total) {
		this.total += total;
	}

	public final double getPercentSales() {
		return percentSales;
	}

	public final void setPercentSales(double percentSales) {
		this.percentSales = percentSales;
	}

	public final int getOrder() {
		return order;
	}

	public final void setOrder(int order) {
		this.order = order;
	}

	public void calculatePercentage(final double totalSales) {
		this.percentSales = (this.total / totalSales) * 100;
	}
	
	public final void sumCredit(double credit) {
		this.totalCredit += credit;		
	}
	
	public final void sumDebit(double debit) {
		this.totalDebit += debit;		
	}
	
	public final void sumBalance() {
		this.totalBalance = this.totalCredit - this.totalDebit;		
	}
}