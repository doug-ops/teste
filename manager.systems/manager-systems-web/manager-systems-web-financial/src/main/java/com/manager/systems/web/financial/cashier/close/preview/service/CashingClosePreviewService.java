/*
 * Date create 18/02/2024.
 */
package com.manager.systems.web.financial.cashier.close.preview.service;

import java.util.List;

import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserPreviewHeaderDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserWeekMovementCompanyDTO;

public interface CashingClosePreviewService {

	byte[] generatePDFCashierClosingAnalitic(CashingCloseUserPreviewHeaderDTO header, List<CashingCloseUserWeekMovementCompanyDTO> items) throws Exception;
	byte[] generatePDFCashierClosingSintetic(CashingCloseUserPreviewHeaderDTO header, List<CashingCloseUserWeekMovementCompanyDTO> items) throws Exception;
	
}