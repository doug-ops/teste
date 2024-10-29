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
public class CashFlowBankAccountTranferDTO implements Serializable {
	private static final long serialVersionUID = -1446947957187356736L;

	private List<CashFlowReportItemDTO> items;
	private boolean isAnalitic;
	
	private CashFlowBankAccountTranferCreditDTO bankAccountTranferCredit;
	private CashFlowBankAccountTranferDebitDTO bankAccountTranferDebit;

	public double getTotal() {
		return this.bankAccountTranferCredit.getTotal() - this.bankAccountTranferDebit.getTotal();
	}
	
	public void addItem(final CashFlowReportItemDTO item) {
		if(item.getDocumentValue() < 0) {
			this.bankAccountTranferDebit.addItem(item);
		} else {
			this.bankAccountTranferCredit.addItem(item);
		}
		if(this.items == null) {
			this.items = new ArrayList<>();
		}
		this.items.add(item);
	}
	
	public void initializeData() {
		this.bankAccountTranferCredit = CashFlowBankAccountTranferCreditDTO.builder().analitic(this.isAnalitic).build();
		this.bankAccountTranferDebit = CashFlowBankAccountTranferDebitDTO.builder().analitic(this.isAnalitic).build();
	}
}