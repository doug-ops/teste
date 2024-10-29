package com.manager.systems.common.dto.movement.company;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportMovementCompanyGroup implements Serializable{

	private static final long serialVersionUID = 588610798347466567L;
	
	private int groupId;
	private String groupDescription;
	private double totalCreditIn;
	private double totalCreditOut;
	private double totalCreditClock;
	private double balanceProductMovement;
	private double balanceTransferMovement;
	private int executionOrder;
	private int groupExecutionOrder;
	private int groupItemExecutionOrder;
	private String cssBalanceProduct;
	private String cssBalanceTransfer;
	private Map<Long, ReportMovementCompanyProduct> mapProductsMovements = new TreeMap<>();
	private List<ReportMovementCompanyProduct> productsMovements = new ArrayList<>();
	private List<ReportMovementCompanyFinancialTransfer> transferMovements = new ArrayList<>();
	private double initialBalance;
	private double finalBalance;
	
	public int getOrder() {
		return Integer.valueOf(this.getGroupExecutionOrder()+""+this.getGroupItemExecutionOrder()+""+this.getExecutionOrder());
	}
	
	public void addProductMovement(final ReportMovementCompanyProduct item) {		
		ReportMovementCompanyProduct itemMap = this.mapProductsMovements.get(item.getProductId());
		if(itemMap!=null) {
			if(item.isInput()) 
			{
				itemMap.setCreditInInitial(item.getCreditInInitial());	
				itemMap.setCreditInFinal(item.getCreditInFinal());
				itemMap.setTotalCreditIn(item.getTotalCreditIn());
			}
			else 
			{
				if(item.getCreditClockInitial() > 0 && item.getCreditClockFinal() > 0) {
					itemMap.calculateTotals();
					this.balanceProductMovement-=itemMap.getBalance();
					itemMap.setCreditClockInitial(item.getCreditClockInitial());	
					itemMap.setCreditClockFinal(item.getCreditClockFinal());
					itemMap.setTotalCreditClock(item.getTotalCreditClock());
				}
				else 
				{
					itemMap.setCreditOutInitial(item.getCreditOutInitial());	
					itemMap.setCreditOutFinal(item.getCreditOutFinal());
					itemMap.setTotalCreditOut(item.getTotalCreditOut());
				}
			}
		}		
		else {
			itemMap = item;
		}
		itemMap.calculateTotals();
		if(item.isInput()) {
			this.totalCreditIn+=itemMap.getTotalCreditIn();
		}
		else {
			this.totalCreditOut+=itemMap.getTotalCreditOut();	
			this.totalCreditClock+=itemMap.getTotalCreditClock();
			this.balanceProductMovement+=itemMap.getBalance();
		}
		this.mapProductsMovements.put(itemMap.getProductId(), itemMap);
		
		if(this.balanceProductMovement<0) {
			this.cssBalanceProduct = "color: red;";
		}
		else {
			this.cssBalanceProduct = "color: green;";
		}		
		this.finalBalance = (this.balanceProductMovement-this.balanceTransferMovement);
		this.initialBalance = this.balanceProductMovement;
	}
	
	public void addTransferMovement(final ReportMovementCompanyFinancialTransfer item) {
		
		this.balanceTransferMovement+=item.getTransferValue();	
		this.transferMovements.add(item);
		if(this.balanceTransferMovement<0) {
			this.cssBalanceTransfer = "color: red;";
		}
		else {
			this.cssBalanceTransfer = "color: green;";
		}
		if(this.balanceTransferMovement < 0) {
			this.finalBalance = (this.balanceProductMovement + (this.balanceTransferMovement * -1));			
		}
		else {
			this.finalBalance = (this.balanceProductMovement - this.balanceTransferMovement);
		}
	}
	
	public void sorListMovement()
	{		
		this.productsMovements.clear();
		for(final Map.Entry<Long, ReportMovementCompanyProduct> entry : this.mapProductsMovements.entrySet()) {
			this.productsMovements.add(entry.getValue());
		}
		Collections.sort(this.productsMovements, new Comparator<ReportMovementCompanyProduct>()
		{
			@Override
			public int compare(final ReportMovementCompanyProduct o1, final ReportMovementCompanyProduct o2)
			{
				return Integer.compare(o1.getOrder(), o2.getOrder());
			}
		});
		
		Collections.sort(this.transferMovements, new Comparator<ReportMovementCompanyFinancialTransfer>()
		{
			@Override
			public int compare(final ReportMovementCompanyFinancialTransfer o1, final ReportMovementCompanyFinancialTransfer o2)
			{
				return Integer.compare(o1.getOrder(), o2.getOrder());
			}
		});
		this.mapProductsMovements = null;
	}
	
	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		final String newLine = System.getProperty("line.separator");

		result.append(this.getClass().getName());
		result.append(" Object {");
		result.append(newLine);

		final Field[] fields = this.getClass().getDeclaredFields();

		for (final Field field : fields) {
			result.append("  ");
			try {
				result.append(field.getName());
				result.append(": ");
				// requires access to private field:
				result.append(field.get(this));
			} catch (final IllegalAccessException ex) {

			}
			result.append(newLine);
		}
		result.append("}");
		return result.toString();
	}
}