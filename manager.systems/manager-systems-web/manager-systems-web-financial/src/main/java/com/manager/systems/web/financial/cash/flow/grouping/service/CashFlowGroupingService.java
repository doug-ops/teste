/*
 * Date create 02/01/2024
 */
package com.manager.systems.web.financial.cash.flow.grouping.service;

import com.manager.systems.web.financial.cash.flow.grouping.dto.CashFlowFinancialGroupingFilterDTO;
import com.manager.systems.web.financial.cash.flow.grouping.dto.CashFlowFinancialGroupingReportDTO;

public interface CashFlowGroupingService {
	CashFlowFinancialGroupingReportDTO getCashFlowGroupingReport(CashFlowFinancialGroupingFilterDTO filter) throws Exception;

	byte[] processPdfReport(CashFlowFinancialGroupingReportDTO  report) throws Exception;
}
