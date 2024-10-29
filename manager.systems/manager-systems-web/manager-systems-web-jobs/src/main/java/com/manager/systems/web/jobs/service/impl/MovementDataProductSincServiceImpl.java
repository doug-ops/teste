package com.manager.systems.web.jobs.service.impl;

import java.sql.Connection;

import com.manager.systems.web.jobs.dao.MovementDataProductSincDao;
import com.manager.systems.web.jobs.dao.impl.MovementDataProductSincDaoImpl;
import com.manager.systems.web.jobs.dto.MovementDataProductSincDTO;
import com.manager.systems.web.jobs.dto.MovementDataProductSincItemDTO;
import com.manager.systems.web.jobs.service.MovementDataProductSincService;

public class MovementDataProductSincServiceImpl implements MovementDataProductSincService 
{
	private MovementDataProductSincDao movementDataProductSincDao;
	
	public MovementDataProductSincServiceImpl() 
	{
		super();
		this.movementDataProductSincDao = new MovementDataProductSincDaoImpl();
	}
	
	@Override
	public void getProductMovementsPendingSinc(final MovementDataProductSincDTO movement, final Connection connection) throws Exception 
	{
		this.movementDataProductSincDao.getProductMovementsPendingSinc(movement, connection);
	}

	@Override
	public boolean saveProductMovementPendingSinc(final MovementDataProductSincItemDTO item, final Connection connection) throws Exception 
	{
		return this.movementDataProductSincDao.saveProductMovementPendingSinc(item, connection);
	}

	@Override
	public boolean changeSincStateProductMovementsPendingSinc(final MovementDataProductSincItemDTO item, final Connection connection) throws Exception
	{
		return this.movementDataProductSincDao.changeSincStateProductMovementsPendingSinc(item, connection);
	}
}