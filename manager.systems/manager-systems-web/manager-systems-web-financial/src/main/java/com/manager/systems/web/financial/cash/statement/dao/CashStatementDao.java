/*
 * Create Date 04/07/2023
 */
package com.manager.systems.web.financial.cash.statement.dao;

import com.manager.systems.web.financial.cash.statement.dto.CashStatemenReportDTO;
import com.manager.systems.web.financial.cash.statement.dto.CashStatemenReportFilterDTO;

public interface CashStatementDao {
	CashStatemenReportDTO getCashStatement(CashStatemenReportFilterDTO filter) throws Exception;
}