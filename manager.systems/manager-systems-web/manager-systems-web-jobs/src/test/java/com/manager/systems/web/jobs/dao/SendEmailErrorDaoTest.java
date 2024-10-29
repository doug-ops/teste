package com.manager.systems.web.jobs.dao;

import java.sql.Connection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.web.jobs.dao.impl.SendEmailErrorDaoImpl;
import com.manager.systems.web.jobs.dto.SendEmailErrorDTO;
import com.manager.systems.web.jobs.utils.ConnectionUtils;

public class SendEmailErrorDaoTest 
{
	private Connection connection = null;
	private SendEmailErrorDao sendEmailErrosDao = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.sendEmailErrosDao = new SendEmailErrorDaoImpl(connection);
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.sendEmailErrosDao);
		
		//Get
		SendEmailErrorDTO sendEmailErros = new SendEmailErrorDTO();	
		this.sendEmailErrosDao.getErrorSendEmail();
		Assert.assertEquals("Sincronia de Documentos", sendEmailErros);
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