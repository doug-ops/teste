/*
 * Date create 28/10/2023.
 */
package com.manager.systems.web.financial.cash.flow.service;

import com.manager.systems.web.financial.cash.flow.dto.CashFlowFilterDTO;
import com.manager.systems.web.financial.cash.flow.dto.CashFlowReportDTO;

public interface CashFlowService {
	CashFlowReportDTO getCashFlowReport(CashFlowFilterDTO filter) throws Exception;

	byte[] processPdfReport(CashFlowReportDTO report) throws Exception;
}
