/*
 * Date create 24/08/2023.
 */
package com.manager.systems.web.financial.incomeExpenses.dao;

import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesFilterDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesReportDTO;

public interface FinancialIncomeExpensesDao {
	
	FinancialIncomeExpensesReportDTO getFinancialMovements(FinancialIncomeExpensesFilterDTO filter) throws Exception;
}