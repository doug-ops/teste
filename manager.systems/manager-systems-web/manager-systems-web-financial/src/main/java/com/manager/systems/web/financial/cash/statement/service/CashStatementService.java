/*
 * Date create 28/06/2023.
 */
package com.manager.systems.web.financial.cash.statement.service;

import com.manager.systems.web.financial.cash.statement.dto.CashStatemenReportDTO;
import com.manager.systems.web.financial.cash.statement.dto.CashStatemenReportFilterDTO;

public interface CashStatementService {
	CashStatemenReportDTO getCashStatement(CashStatemenReportFilterDTO filter) throws Exception;
	byte[] processPdfReport(CashStatemenReportDTO report) throws Exception;
}