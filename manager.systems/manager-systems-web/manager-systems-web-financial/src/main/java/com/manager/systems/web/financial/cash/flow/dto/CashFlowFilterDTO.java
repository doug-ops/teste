package com.manager.systems.web.financial.cash.flow.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CashFlowFilterDTO implements Serializable {
	private static final long serialVersionUID = 5465047237882144591L;
	
	private int operation;
	private long companyId;
	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;
	private String usersChildrenParent;
	private boolean isAnalitc;
	private int incomeExpense;
	private boolean isExpenseHaab;
	private boolean isTransfCashingClose;
	private int groupBy;
	private List<Long> bankAccounts;
}
