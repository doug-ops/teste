/*
 * Crete Date 08/10/2022
 */
package com.manager.systems.web.financial.service;

import java.util.List;

import com.manager.systems.common.dto.movement.company.PreviewMovementCompanyDTO;
import com.manager.systems.web.financial.dto.CashierClosingCompanyDTO;
import com.manager.systems.web.financial.dto.CashierClosingDTO;
import com.manager.systems.web.financial.dto.CashierClosingItemDTO;
import com.manager.systems.web.financial.dto.CashierClosingRequestStoreDTO;
import com.manager.systems.web.financial.dto.CashierClosingWekendDTO;
import com.manager.systems.web.financial.dto.FinancialDocumentDTO;

public interface CashierClosingService 
{
	void getAllCashierClosing(CashierClosingDTO cashierClosing) throws Exception;
	void getCashierClosing(PreviewMovementCompanyDTO previewMovementCompany) throws Exception;
	void getCashierClosingItem(CashierClosingItemDTO item) throws Exception;
	byte[] genetarePdfReportFromPreview(PreviewMovementCompanyDTO preview) throws Exception;
	byte[] generatePDFCashierClosingAnalitic(String dateFilter, List<CashierClosingRequestStoreDTO> items) throws Exception;
	byte[] generatePDFCashierClosingSintetic(String dateFilter, List<CashierClosingRequestStoreDTO> items) throws Exception;
	byte[] generatePDFCashierClosingsWeek(CashierClosingWekendDTO cashierClosings) throws Exception;
	byte[] generatePDFMovementDocumentsGroupCompany(CashierClosingCompanyDTO cashierClosings) throws Exception;
	Long creditTrasnferValuesOriginToDestinty(FinancialDocumentDTO financialDocument) throws Exception;
	boolean debitTrasnferValuesOriginResidue(FinancialDocumentDTO financialDocument) throws Exception;
	boolean saveDocumentExpense(FinancialDocumentDTO financialDocument) throws Exception;
	boolean checkCashingCloseSuccess(FinancialDocumentDTO document) throws Exception;
}