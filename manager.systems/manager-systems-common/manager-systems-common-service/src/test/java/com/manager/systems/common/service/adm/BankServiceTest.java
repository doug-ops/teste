package com.manager.systems.common.service.adm;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.service.impl.BankServiceImpl;
import com.manager.systems.common.vo.Combobox;

public class BankServiceTest 
{
	private Connection connection = null;
	private BankService bankService = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.bankService = new BankServiceImpl(this.connection);
	}
	
	@Test
	public void getAllCombobox() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.bankService);
		
		//Get all Combobox
		final List<Combobox> items = this.bankService.getAllCombobox();
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
