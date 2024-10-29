package com.manager.systems.web.financial.cash.flow.dto;

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
public class CashFlowIncomeExpectedDTO implements Serializable{
	private static final long serialVersionUID = -306014133666081956L;
	
	private double total;
	private List<CashFlowReportItemDTO> items;
	private boolean isAnalitic;
	
	public void addItem(final CashFlowReportItemDTO item) {
		if(this.items == null) {
			this.items = new ArrayList<>();
		}
		
		this.total += item.getDocumentValue();
		this.items.add(item);
	}

}
