/*
 * Date create 28/10/2023.
 */
package com.manager.systems.web.financial.cash.flow.dao;

import com.manager.systems.web.financial.cash.flow.dto.CashFlowFilterDTO;
import com.manager.systems.web.financial.cash.flow.dto.CashFlowReportDTO;

public interface CashFlowDao {
	CashFlowReportDTO getCashFlowReport(CashFlowFilterDTO filter) throws Exception;
}