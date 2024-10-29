package com.manager.systems.web.movements.dto.dre;

import java.io.Serializable;
import java.util.TreeMap;

public class DreReportGroupDTO implements Serializable
{
	private static final long serialVersionUID = -6234795068319514896L;

	private String groupId;
	private String descriptionGroup;
	private double total;
	private double percentSales;
	private int order;
	private double totalCredit;
	private double totalDebit;
	private double totalBalance;

	private TreeMap<String, DreReportSubGroupDTO> mapSubGroup = new TreeMap<String, DreReportSubGroupDTO>();
	
	public DreReportGroupDTO() {
		super();
	}

	public final void addSubGroup(final DreReportItemDTO item) 
	{		
		DreReportSubGroupDTO value = null;
		if(this.mapSubGroup.containsKey(item.getFinancialSubGroupId()))
		{
			value = this.mapSubGroup.get(item.getFinancialSubGroupId());
		}
		if(value == null) {
			value = new DreReportSubGroupDTO();
			value.setIdSubGroup(item.getFinancialSubGroupId());
			value.setDescriptionSubGroup(item.getDescriptionFinancialSubGroup());
			value.setOrder(item.getOrder());			
		}
		//value.sumTotal(item.getTotal());
		if(item.isCredit()) {
			value.sumCredit(item.getTotal());			
		} else {
			value.sumDebit(item.getTotal());			
		}
		value.sumBalance();
		
		value.addItem(item);
		
		this.mapSubGroup.put(item.getFinancialSubGroupId(), value);
	}

	public final String getIdGroup() {
		return groupId;
	}
	
	public final void setIdGroup(String groupId) {
		this.groupId = groupId;
	}

	public final String getDescriptionGroup() {
		return descriptionGroup;
	}
	public final void setDescriptionGroup(String descriptionGroup) {
		this.descriptionGroup = descriptionGroup;
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
	
	public final double getTotalBalance() {
		return this.totalBalance;
	}
}