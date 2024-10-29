/*
 * Date create 24/08/2023.
 */
package com.manager.systems.web.financial.incomeExpenses.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class FinancialIncomeExpensesProviderDTO implements Serializable {

	private static final long serialVersionUID = 365989986771723048L;
	
	private double income;
	private double expenses;
	private double balance;
	private double transfInput;
	private double transfOutput;
	private double profitDistribution;
	private boolean hasProfitDistribution;
	private long providerId;
	private String providerDescription;
	private List<FinancialIncomeExpensesItemDetailDTO> items;
	

	public void addItem(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(detail.getMovementTypeId() == 1) {
			if(detail.isProfitDistribution()) {
				this.hasProfitDistribution = true;
				this.profitDistribution += detail.getDocumentValue();								
			} else if(!detail.isTransactionSameCompany()) {
				this.income += detail.getDocumentValue();								
			} else {
				this.transfInput += detail.getDocumentValue();
			}
		} else {
			if(detail.isProfitDistribution()) {
				this.hasProfitDistribution = true;
				this.profitDistribution -= detail.getDocumentValue();								
			} else if(!detail.isTransactionSameCompany()) {
				this.expenses += detail.getDocumentValue();
			} else {
				this.transfOutput += detail.getDocumentValue();	
			}
		}
		
		if(this.items == null) {
			this.items = new ArrayList<>();
		}		
		this.items.add(detail);
	}
	
	public void calculateTotais() {
		this.balance = (this.income + this.transfInput - this.expenses - this.transfOutput + this.profitDistribution);
	}
	
	public boolean hasTransfeSameCompany() {
		return (this.transfInput != 0 || this.transfOutput != 0);
	}
}