package com.manager.systems.web.jobs.service;

import java.sql.Connection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.web.jobs.dao.impl.MovementDataProccessDTO;
import com.manager.systems.web.jobs.service.impl.MovementDataProcessServiceImpl;
import com.manager.systems.web.jobs.utils.ConnectionUtils;
import com.manager.systems.web.jobs.vo.OperationType;

public class MovementDataProcessServiceTest 
{
	private Connection connection = null;
	private MovementDataProcessService movementDataProcessService = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.movementDataProcessService = new MovementDataProcessServiceImpl(connection);
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.movementDataProcessService);
		
		//Get
		final MovementDataProccessDTO movementData = new MovementDataProccessDTO();
		movementData.setOffline(false);
		movementData.setOperation(OperationType.GET);
		this.movementDataProcessService.getNextMovementProcess(movementData);
		Assert.assertTrue(movementData.getMovementsIds().length()>0);
		
		movementData.setPreview(false);
		movementData.setUserId(1L);
		final boolean result = this.movementDataProcessService.processMovementData(movementData);
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