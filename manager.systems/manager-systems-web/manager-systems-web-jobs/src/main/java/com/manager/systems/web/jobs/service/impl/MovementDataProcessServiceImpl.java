/**
 * Date create 03/04/2020.
 */
package com.manager.systems.web.jobs.service.impl;

import java.sql.Connection;

import com.manager.systems.web.jobs.dao.MovementDataProcessDao;
import com.manager.systems.web.jobs.dao.impl.MovementDataProccessDTO;
import com.manager.systems.web.jobs.dao.impl.MovementDataProcessDaoImpl;
import com.manager.systems.web.jobs.service.MovementDataProcessService;

public class MovementDataProcessServiceImpl implements MovementDataProcessService 
{
	private MovementDataProcessDao movementDataProcessDao;
	
	/**
	 * Constructor.
	 * @param connection the java.sql.Connection.
	 */
	public MovementDataProcessServiceImpl(final Connection connection) 
	{
		super();
		this.movementDataProcessDao = new MovementDataProcessDaoImpl(connection);
	}

	@Override
	public void getNextMovementProcess(final MovementDataProccessDTO movementData) throws Exception 
	{
		this.movementDataProcessDao.getNextMovementProcess(movementData);
	}

	@Override
	public boolean processMovementData(final MovementDataProccessDTO movementData) throws Exception 
	{
		return this.movementDataProcessDao.processMovementData(movementData);
	}
}
