/*
 * Date create 07/02/2024.
 */
package com.manager.systems.web.financial.cashier.close.preview.service.impl;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.web.financial.cashier.close.preview.dao.CashingCloseUserMovementDao;
import com.manager.systems.web.financial.cashier.close.preview.dao.impl.CashingCloseUserMovementDaoImpl;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashierClosingPreviewFinishRequest;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserMovementDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserMovementFilterDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserWeekMovementCompanyDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserWeekMovementCompanyLaunchDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserWeekMovementDTO;
import com.manager.systems.web.financial.cashier.close.preview.service.CashingCloseUserMovementService;

public class CashingCloseUserMovementServiceImpl implements CashingCloseUserMovementService {

	CashingCloseUserMovementDao cashingCloseUserMovementDao;
	
	public CashingCloseUserMovementServiceImpl(final Connection connection) {
		this.cashingCloseUserMovementDao = new CashingCloseUserMovementDaoImpl(connection);
	}

	@Override
	public List<CashingCloseUserMovementDTO> getCashingCloseUserMovements(final CashingCloseUserMovementFilterDTO filter) throws Exception {
		return this.cashingCloseUserMovementDao.getCashingCloseUserMovements(filter);
	}

	@Override
	public void generateMovementUser(final CashingCloseUserMovementFilterDTO filter) throws Exception {
		this.cashingCloseUserMovementDao.generateMovementUser(filter);		
	}

	@Override
	public CashingCloseUserWeekMovementDTO getCashingCloseUserMovementsByWeek(final CashingCloseUserMovementFilterDTO filter) throws Exception {
		return this.cashingCloseUserMovementDao.getCashingCloseUserMovementsByWeek(filter);
	}
	
	@Override
	public void saveMovementHeader(final CashingCloseUserWeekMovementDTO movement) throws Exception {
		this.cashingCloseUserMovementDao.saveMovementHeader(movement);
	}

	@Override
	public void saveMovementCompanys(final List<CashingCloseUserWeekMovementCompanyDTO> movementCompanys) throws Exception {
		this.cashingCloseUserMovementDao.saveMovementCompanys(movementCompanys);		
	}

	@Override
	public void saveCashierClosingLaunch(final List<CashingCloseUserWeekMovementCompanyLaunchDTO> cashingClosingLaunchList) throws Exception {
		this.cashingCloseUserMovementDao.saveCashierClosingLaunch(cashingClosingLaunchList);
	}

	@Override
	public void processCashierClosingLaunch(final CashierClosingPreviewFinishRequest cashierClosingPreviewFinishRequest) throws Exception {
		this.cashingCloseUserMovementDao.processCashierClosingLaunch(cashierClosingPreviewFinishRequest);
	}

	@Override
	public List<CashingCloseUserWeekMovementCompanyLaunchDTO> getCashierClosingLaunchs(final long cashierClosingId, final long userOperator) throws Exception {
		return this.cashingCloseUserMovementDao.getCashierClosingLaunchs(cashierClosingId, userOperator);
	}

	@Override
	public void deleteCashierClosingLaunchs(final long cashierClosingId, final long userOperator) throws Exception {
		this.cashingCloseUserMovementDao.deleteCashierClosingLaunchs(cashierClosingId, userOperator);		
	}
}