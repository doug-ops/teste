/*
 * Date create 02/01/2024
 */
package com.manager.systems.web.financial.cash.flow.grouping.dao;

import com.manager.systems.web.financial.cash.flow.grouping.dto.CashFlowFinancialGroupingFilterDTO;
import com.manager.systems.web.financial.cash.flow.grouping.dto.CashFlowFinancialGroupingReportDTO;

public interface CashFlowGroupingDao {
	
	CashFlowFinancialGroupingReportDTO getCashFlowFinancialGroupingReport(CashFlowFinancialGroupingFilterDTO filter) throws Exception;
}