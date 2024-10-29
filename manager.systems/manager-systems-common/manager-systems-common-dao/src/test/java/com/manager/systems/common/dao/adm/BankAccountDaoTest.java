package com.manager.systems.common.dao.adm;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.impl.adm.BankAccountDaoImpl;
import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.BankAccountDTO;
import com.manager.systems.common.dto.adm.ReportBankAccountDTO;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;


public class BankAccountDaoTest 
{
	private Connection connection = null;
	private BankAccountDao bankAccountDao = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.bankAccountDao = new BankAccountDaoImpl(this.connection);
	}
	
	@Test
	public void crud() throws Exception
	{
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.bankAccountDao);
		
		//Save Insert
		BankAccountDTO bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setId("1");
		bankAccountDTO.setDescription("Teste01");
		bankAccountDTO.setInactive(false);
		//bankAccountDTO.setIntegrationSystems(new IntegrationSystems(1, "1"));
		bankAccountDTO.setBankBalanceAvailable(0.01);
		bankAccountDTO.setBankLimitAvailable(0.01);
		bankAccountDTO.setChangeData(new ChangeData(1L));
		final boolean isSaved = this.bankAccountDao.save(bankAccountDTO);
		Assert.assertTrue(isSaved);
		
		//Get
		bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setId("1");
		this.bankAccountDao.get(bankAccountDTO);
		Assert.assertEquals("Teste01", bankAccountDTO.getDescription());
		
		//Inactive
		bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setId("1");
		bankAccountDTO.setInactive(true);
		bankAccountDTO.setChangeData(new ChangeData(1L));
		final boolean isInactive = this.bankAccountDao.inactive(bankAccountDTO);
		Assert.assertTrue(isInactive);
		
		//Get
		bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setId("1");
		this.bankAccountDao.get(bankAccountDTO);
		Assert.assertEquals(true, bankAccountDTO.isInactive());
		
		//Update
		bankAccountDTO = new BankAccountDTO();		
		bankAccountDTO.setId("1");
		bankAccountDTO.setDescription("Teste02");
		bankAccountDTO.setInactive(false);
		bankAccountDTO.setChangeData(new ChangeData(1L));
		final boolean isUpdate = this.bankAccountDao.save(bankAccountDTO);
		Assert.assertTrue(isUpdate);
		
		//Get
		bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setId("1");
		this.bankAccountDao.get(bankAccountDTO);
		Assert.assertEquals("Teste02", bankAccountDTO.getDescription());
		
		//GetAll
		ReportBankAccountDTO reportBankAccountDTO = new ReportBankAccountDTO();
		reportBankAccountDTO.setBankAccountIdFrom("1");
		reportBankAccountDTO.setBankAccountIdTo("1");
		reportBankAccountDTO.setDescription("Teste02");
		reportBankAccountDTO.setInactive("0");
		this.bankAccountDao.getAll(reportBankAccountDTO);
		Assert.assertTrue(reportBankAccountDTO.getBankAccounts().size()>0);
		
		final ReportBankAccountDTO reportBankAccount = new ReportBankAccountDTO();
		reportBankAccount.setUserOperation(1L);
		List<Combobox> itens = this.bankAccountDao.getAllCombobox(reportBankAccount);
		Assert.assertTrue(itens.size()>0);
		
		reportBankAccountDTO = new ReportBankAccountDTO();
		reportBankAccountDTO.setDescription("anderson");
		reportBankAccountDTO.setInactive("0");
		itens = this.bankAccountDao.getAllAutocomplete(reportBankAccountDTO);
		Assert.assertTrue(itens.size()>0);
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