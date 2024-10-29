/*
 * Crete Date 08/10/2022
 */
package com.manager.systems.web.financial.dao;

import com.manager.systems.common.dto.movement.company.PreviewMovementCompanyDTO;
import com.manager.systems.web.financial.dto.CashierClosingDTO;
import com.manager.systems.web.financial.dto.CashierClosingItemDTO;
import com.manager.systems.web.financial.dto.FinancialDocumentDTO;

public interface CashierClosingDao 
{
	void getAllCashierClosing(CashierClosingDTO cashierClosing) throws Exception;
	void getCashierClosing(PreviewMovementCompanyDTO previewMovementCompany) throws Exception;
	void getCashierClosingItem(CashierClosingItemDTO item) throws Exception;
	Long creditTrasnferValuesOriginToDestinty(FinancialDocumentDTO financialDocument) throws Exception;
	boolean debitTrasnferValuesOriginResidue(FinancialDocumentDTO financialDocument) throws Exception;
	boolean saveDocumentExpense(FinancialDocumentDTO financialDocument) throws Exception;
	boolean checkCashingCloseSuccess(FinancialDocumentDTO document) throws Exception;
}