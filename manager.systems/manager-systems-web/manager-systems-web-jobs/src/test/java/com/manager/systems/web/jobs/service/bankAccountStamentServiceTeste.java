package com.manager.systems.web.jobs.service;

import java.sql.Connection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dto.bankAccount.BankAccountStatemenReportDTO;
import com.manager.systems.common.service.bankAccount.BankAccountStatementService;
import com.manager.systems.common.service.bankAccount.impl.BankAccountStatementServiceImpl;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.web.jobs.utils.ConnectionUtils;

public class bankAccountStamentServiceTeste 
{
	private Connection connection = null;
	private BankAccountStatementService bankAccountStatementService = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.bankAccountStatementService = new BankAccountStatementServiceImpl(this.connection);
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.bankAccountStatementService);
		
		//Get
		final BankAccountStatemenReportDTO bankAccount = new BankAccountStatemenReportDTO();

		bankAccount.setOperation(2);
		bankAccount.setBankAccounts("363,240,234");
		bankAccount.setDateFrom(StringUtils.convertStringDateHoraToLocalDateTime("2021-12-20 00:00:00"));
		bankAccount.setDateTo(StringUtils.convertStringDateHoraToLocalDateTime("2021-12-20 23:59:59"));
		this.bankAccountStatementService.sendEmailBankAccountStatement();
		Assert.assertTrue(bankAccount.getItens().size()>0);
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