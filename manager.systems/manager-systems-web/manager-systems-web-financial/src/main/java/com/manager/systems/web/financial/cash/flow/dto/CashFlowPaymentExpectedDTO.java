/**
 * Creation at 23/10/2023.
 */
package com.manager.systems.web.financial.cash.flow.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CashFlowPaymentExpectedDTO implements Serializable {
	private static final long serialVersionUID = 3795369231737328649L;
	
	private double total;
	private Map<String, CashFlowPaymentFulfilledFinancialGroupDTO> items;
	private boolean isAnalitic;
	
	public void addItem(final CashFlowReportItemDTO item) {
		if(this.items == null) {
			this.items = new TreeMap<>();
		}
		
		this.total += item.getDocumentValue();
		
		CashFlowPaymentFulfilledFinancialGroupDTO group = this.items.get(item.getFinancialGroupId());
		if(group == null) {
			group = CashFlowPaymentFulfilledFinancialGroupDTO.builder().id(item.getFinancialGroupId()).description(item.getFinancialGroupDescription()).build();
		}
		
		group.addItem(item);
		this.items.put(item.getFinancialGroupId(), group);
	}
}