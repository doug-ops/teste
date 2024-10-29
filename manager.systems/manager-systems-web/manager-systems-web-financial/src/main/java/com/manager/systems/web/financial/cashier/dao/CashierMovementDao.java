/*
 * Date create 18/08/2023.
 */
package com.manager.systems.web.financial.cashier.dao;

import com.manager.systems.web.financial.cashier.dto.CashierMovementReportDTO;
import com.manager.systems.web.financial.cashier.dto.CashierMovementReportFilterDTO;

public interface CashierMovementDao {

	CashierMovementReportDTO getMovements(CashierMovementReportFilterDTO filter) throws Exception;
}