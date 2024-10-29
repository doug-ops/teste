/*
 * Date create 13/12/2021.
 */
package com.manager.systems.common.service.bankAccount;

import com.manager.systems.common.dto.bankAccount.BankAccountStatemenReportDTO;

public interface BankAccountStatementService {
	void getBankAccountStatement(BankAccountStatemenReportDTO report) throws Exception;
	void sendEmailBankAccountStatement() throws Exception;
	byte[] processPdfReport(BankAccountStatemenReportDTO report) throws Exception;
}