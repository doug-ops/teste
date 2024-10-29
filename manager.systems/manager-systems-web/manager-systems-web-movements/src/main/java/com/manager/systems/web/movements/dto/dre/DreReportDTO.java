package com.manager.systems.web.movements.dto.dre;

import java.util.Map;
import java.util.TreeMap;

public class DreReportDTO {
	
	private boolean analitic;
	private double totalCredit;
	private double totalDebit;
	private double totalBalance;
	
	private Map<Integer, DreReportGroupTypeDTO> mapGroupType = new TreeMap<Integer, DreReportGroupTypeDTO>();
	
	
	public final void addGroupType(final DreReportItemDTO item) 
	{		
		if(item.isCredit()) {
			sumCredit(item.getTotal());			
		} else {
			sumDebit(item.getTotal());			
		}
		sumBalance();
		
		DreReportGroupTypeDTO value = null;
		if(this.mapGroupType.containsKey(item.getGroupTypeId()))
		{
			value = this.mapGroupType.get(item.getGroupTypeId());
		}
		if(value == null) {
			value = new DreReportGroupTypeDTO();			
			value.setGroupTypeId(item.getGroupTypeId());
			value.setDescriptionGroupType(item.getDescriptionGroupType());
			value.setOrder(item.getOrder());
		}
		//value.sumTotal(item.getTotal());
		if(item.isCredit()) {
			value.sumCredit(item.getTotal());			
		} else {
			value.sumDebit(item.getTotal());			
		}
		value.sumBalance();
		
		value.addGroup(item);
		
		this.mapGroupType.put(item.getGroupTypeId(), value);
	}
	
	public final Map<Integer, DreReportGroupTypeDTO> getMapGroupType() {
		return this.mapGroupType;
	}
	
	public final boolean isAnalitic() {
		return analitic;
	}

	public final void setAnalitic(final boolean analitic) {
		this.analitic = analitic;
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