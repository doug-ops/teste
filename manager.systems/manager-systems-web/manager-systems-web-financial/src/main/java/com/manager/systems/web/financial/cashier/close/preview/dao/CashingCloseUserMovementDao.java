/*
 * Date create 07/02/2024
 */
package com.manager.systems.web.financial.cashier.close.preview.dao;

import java.util.List;

import com.manager.systems.web.financial.cashier.close.preview.dto.CashierClosingPreviewFinishRequest;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserMovementDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserMovementFilterDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserWeekMovementCompanyDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserWeekMovementCompanyLaunchDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserWeekMovementDTO;

public interface CashingCloseUserMovementDao {
	
	List<CashingCloseUserMovementDTO> getCashingCloseUserMovements(CashingCloseUserMovementFilterDTO filter) throws Exception;
	void generateMovementUser(CashingCloseUserMovementFilterDTO filter) throws Exception;
	CashingCloseUserWeekMovementDTO getCashingCloseUserMovementsByWeek(CashingCloseUserMovementFilterDTO filter) throws Exception;
	void saveMovementHeader(CashingCloseUserWeekMovementDTO movement) throws Exception;
	void saveMovementCompanys(List<CashingCloseUserWeekMovementCompanyDTO> movementCompanys) throws Exception;
	void saveCashierClosingLaunch(List<CashingCloseUserWeekMovementCompanyLaunchDTO> cashingClosingLaunchList) throws Exception;
	void processCashierClosingLaunch(CashierClosingPreviewFinishRequest cashierClosingPreviewFinishRequest) throws Exception;
	List<CashingCloseUserWeekMovementCompanyLaunchDTO> getCashierClosingLaunchs(long cashierClosingId, long userOperator) throws Exception;
	void deleteCashierClosingLaunchs(long cashierClosingId, long userOperator) throws Exception;
	
}