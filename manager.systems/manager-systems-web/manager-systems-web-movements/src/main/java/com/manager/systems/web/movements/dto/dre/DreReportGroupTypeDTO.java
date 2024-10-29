package com.manager.systems.web.movements.dto.dre;

import java.io.Serializable;
import java.util.TreeMap;

public class DreReportGroupTypeDTO implements Serializable
{
	private static final long serialVersionUID = -6234795068319514896L;

	private int groupTypeId;
	private String descriptionGroupType;
	private double total;
	private double percentSales;
	private int order;
	private double totalCredit;
	private double totalDebit;
	private double totalBalance;
	
	private TreeMap<String, DreReportGroupDTO> mapGroup = new TreeMap<String, DreReportGroupDTO>();
	
	public DreReportGroupTypeDTO() {
		super();
	}
	
	public final void addGroup(final DreReportItemDTO item) 
	{		
		DreReportGroupDTO value = null;
		if(this.mapGroup.containsKey(item.getFinancialGroupId()))
		{
			value = this.mapGroup.get(item.getFinancialGroupId());
		}
		if(value == null) {
			value = new DreReportGroupDTO();
			value.setIdGroup(item.getFinancialGroupId());
			value.setDescriptionGroup(item.getDescriptionFinancialGroup());
			value.setOrder(item.getOrder());			
		}
		//value.sumTotal(item.getTotal());
		if(item.isCredit()) {
			value.sumCredit(item.getTotal());			
		} else {
			value.sumDebit(item.getTotal());			
		}
		value.sumBalance();
		
		value.addSubGroup(item);
		
		this.mapGroup.put(item.getFinancialGroupId(), value);
	}

	public final int getGroupTypeId() {
		return groupTypeId;
	}

	public final void setGroupTypeId(int groupTypeId) {
		this.groupTypeId = groupTypeId;
	}

	public final String getDescriptionGroupType() {
		return descriptionGroupType;
	}

	public final void setDescriptionGroupType(String descriptionGroupType) {
		this.descriptionGroupType = descriptionGroupType;
	}

	public final double getTotal() {
		return total;
	}

	public final void sumTotal(double total) {
		this.total += total;		
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

	public final double getTotalBalance() {
		return this.totalBalance;
	}
}