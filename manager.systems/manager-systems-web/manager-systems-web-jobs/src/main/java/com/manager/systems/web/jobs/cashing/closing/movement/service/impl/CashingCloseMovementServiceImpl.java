package com.manager.systems.web.jobs.cashing.closing.movement.service.impl;

import java.sql.Connection;

import com.manager.systems.web.jobs.cashing.closing.movement.dao.CashingCloseMovementDao;
import com.manager.systems.web.jobs.cashing.closing.movement.dao.impl.CashingCloseMovementDaoImpl;
import com.manager.systems.web.jobs.cashing.closing.movement.dto.CashingCloseMovementRequestDTO;
import com.manager.systems.web.jobs.cashing.closing.movement.service.CashingCloseMovementService;

public class CashingCloseMovementServiceImpl implements CashingCloseMovementService {

	private CashingCloseMovementDao cashingCloseMovementDao;
	
	public CashingCloseMovementServiceImpl(final Connection connection) {
		this.cashingCloseMovementDao = new CashingCloseMovementDaoImpl(connection);
	}
	
	@Override
	public void generateMovement(final CashingCloseMovementRequestDTO request) throws Exception {
		
		this.cashingCloseMovementDao.generateMovement(request);		
	
	}
}
