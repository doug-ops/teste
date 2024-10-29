package com.manager.systems.web.jobs.service;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.web.jobs.dto.SendEmailErrorDTO;
import com.manager.systems.web.jobs.service.impl.SendEmailErrorServiceImpl;
import com.manager.systems.web.jobs.utils.ConnectionUtils;

public class SendEmailErrorServiceTest 
{
	private Connection connection = null;
	private SendEmailErrorService sendEmailErrosService = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.sendEmailErrosService = new SendEmailErrorServiceImpl(connection);
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.sendEmailErrosService);


		final List<SendEmailErrorDTO> sendEmailErros = this.sendEmailErrosService.getErrorSendEmail();
		Assert.assertTrue(sendEmailErros.size()>0);
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