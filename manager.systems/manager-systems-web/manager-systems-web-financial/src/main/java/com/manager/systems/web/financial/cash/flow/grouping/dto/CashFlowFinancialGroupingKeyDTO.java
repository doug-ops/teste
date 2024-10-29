package com.manager.systems.web.financial.cash.flow.grouping.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CashFlowFinancialGroupingKeyDTO implements Serializable {

	private static final long serialVersionUID = 7410608772254794639L;
	
	private long key; 
	private String keyDescription;
	
	private double incomeExpected;
	private double incomeFulfilled;
	private double paymentExpected;
	private double paymentFulfilled;
	private double transferCredit;
	private double transferDebit;
	
	public void addItem(final CashFlowFinancialGroupingReportItemDTO item) {
		if(item.getMovementTypeId() == 1) {
			this.incomeExpected += item.getDocumentValue();		
		} else if(item.getMovementTypeId() == 2) {
			this.incomeFulfilled += item.getDocumentValue();
		} else if(item.getMovementTypeId() == 3) {
			this.paymentFulfilled += item.getDocumentValue();
		} else if(item.getMovementTypeId() == 4) {
			this.paymentExpected += item.getDocumentValue();
		} else if(item.getMovementTypeId() == 5) {
			if(item.getDocumentValue() < 0) {
				this.transferDebit += item.getDocumentValue();				
			} else {
				this.transferCredit += item.getDocumentValue();
			}					
		}
	}
}