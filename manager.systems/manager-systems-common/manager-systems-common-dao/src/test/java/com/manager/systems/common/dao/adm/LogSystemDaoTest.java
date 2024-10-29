package com.manager.systems.common.dao.adm;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.impl.adm.LogSystemDaoImpl;
import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.ChangeObjectDTO;
import com.manager.systems.common.dto.adm.ReportLogSystemDTO;

public class LogSystemDaoTest 
{
	private Connection connection = null;
	private LogSystemDao logSystemDao = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.logSystemDao = new LogSystemDaoImpl(this.connection);
	}
		
	@Test
	public void crudLogSystem() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.logSystemDao);
		
		
		//Get
		final ReportLogSystemDTO reportLog = new ReportLogSystemDTO();
		reportLog.setSystemId(1);
		reportLog.setCountLogSystem(10);
		
		final List<ChangeObjectDTO>  items = this.logSystemDao.getAll(reportLog);
		
		Assert.assertTrue(items.size()>0);		
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
