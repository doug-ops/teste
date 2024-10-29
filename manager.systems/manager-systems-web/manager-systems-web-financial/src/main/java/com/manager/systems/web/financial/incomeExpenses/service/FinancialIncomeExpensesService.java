/*
 * Date create 24/08/2023.
 */
package com.manager.systems.web.financial.incomeExpenses.service;

import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesFilterDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesReportDTO;

public interface FinancialIncomeExpensesService {

	FinancialIncomeExpensesReportDTO getFinancialMovements(FinancialIncomeExpensesFilterDTO filter) throws Exception;
	byte[] processPdfReport(FinancialIncomeExpensesReportDTO report) throws Exception;
}
