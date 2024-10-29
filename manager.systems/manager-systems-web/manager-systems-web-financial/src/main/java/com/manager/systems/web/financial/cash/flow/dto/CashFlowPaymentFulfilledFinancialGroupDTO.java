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
public class CashFlowPaymentFulfilledFinancialGroupDTO implements Serializable {
	private static final long serialVersionUID = 628088123487796707L;
	
	private String id;
	private String description;
	
	private double total;
	private Map<String, CashFlowPaymentFulfilledFinancialSubGroupDTO> items;
	
	public void addItem(final CashFlowReportItemDTO item) {
		if(this.items == null) {
			this.items = new TreeMap<>();
		}
		
		this.total += item.getDocumentValue();
		
		CashFlowPaymentFulfilledFinancialSubGroupDTO subGroup = this.items.get(item.getFinancialSubGroupId());
		if(subGroup == null) {
			subGroup = CashFlowPaymentFulfilledFinancialSubGroupDTO.builder().id(item.getFinancialSubGroupId()).description(item.getFinancialSubGroupDescription()).build();
		}
		subGroup.addItem(item);
		this.items.put(item.getFinancialSubGroupId(), subGroup);
	}
}
