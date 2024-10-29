package com.manager.systems.web.jobs.dao;

import java.sql.Connection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.web.jobs.dao.impl.MovementDataProccessDTO;
import com.manager.systems.web.jobs.dao.impl.MovementDataProcessDaoImpl;
import com.manager.systems.web.jobs.utils.ConnectionUtils;
import com.manager.systems.web.jobs.vo.OperationType;

public class MovementDataProcessDaoTest 
{
	private Connection connection = null;
	private MovementDataProcessDao movementDataProcessDao = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.movementDataProcessDao = new MovementDataProcessDaoImpl(connection);
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.movementDataProcessDao);
		
		//Get
		final MovementDataProccessDTO movementData = new MovementDataProccessDTO();
		movementData.setOffline(false);
		movementData.setOperation(OperationType.GET);
		this.movementDataProcessDao.getNextMovementProcess(movementData);
		Assert.assertTrue(movementData.getMovementsIds().length()>0);
		
		movementData.setPreview(false);
		movementData.setUserId(1);
		final boolean result = this.movementDataProcessDao.processMovementData(movementData);
		Assert.assertTrue(result);
	}

	@After
	public void finaliza() throws Exception
	{
		if(this.connection!=null)
		{
			this.connection.rollback();
			this.connection.close();
		}
		Assert.assertTrue(this.connection.isClosed());
		this.connection = null;		
	}
}