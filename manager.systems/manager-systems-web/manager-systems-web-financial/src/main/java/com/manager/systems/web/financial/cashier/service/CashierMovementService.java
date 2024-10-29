/*
 * Date create 18/08/2023.
 */
package com.manager.systems.web.financial.cashier.service;

import com.manager.systems.web.financial.cashier.dto.CashierMovementReportDTO;
import com.manager.systems.web.financial.cashier.dto.CashierMovementReportFilterDTO;

public interface CashierMovementService {

	CashierMovementReportDTO getMovements(CashierMovementReportFilterDTO filter) throws Exception;
	byte[] generatePDFSintetic(CashierMovementReportDTO report) throws Exception;
}
