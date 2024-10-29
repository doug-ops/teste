/*
 * Create Date 13/12/2021
 */
package com.manager.systems.common.dao.bankAccount;

import com.manager.systems.common.dto.bankAccount.BankAccountStatemenReportDTO;

public interface BankAccountStatementDao {
	void getBankAccountStatement(BankAccountStatemenReportDTO report) throws Exception;
	void getBankAccountStatementFilter(BankAccountStatemenReportDTO report) throws Exception;
}