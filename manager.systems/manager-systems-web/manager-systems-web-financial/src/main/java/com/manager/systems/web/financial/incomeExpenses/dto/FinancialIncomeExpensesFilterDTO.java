/*
 * Date create 24/08/2023.
 */
package com.manager.systems.web.financial.incomeExpenses.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.manager.systems.common.utils.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class FinancialIncomeExpensesFilterDTO implements Serializable {

	private static final long serialVersionUID = -6331736225736905697L;
	
	private int operation;
	private String companysId;
	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;
	private String usersChildrenParent;
	private int groupBy;
	private boolean isAnalitc;
	private int incomeExpense;
	private boolean isExpenseHaab;
	private boolean isTransfCashingClose;
	private List<Long> bankAccounts;	
	private int financialCostCenter;
	
	public long getDateFromLong() {
		return Long.valueOf(StringUtils.formatDate(this.dateFrom, StringUtils.DATE_PATTERN_YYMMDD));
	}
	
	public long getDateToLong() {
		return Long.valueOf(StringUtils.formatDate(this.dateTo, StringUtils.DATE_PATTERN_YYMMDD));
	}
}
