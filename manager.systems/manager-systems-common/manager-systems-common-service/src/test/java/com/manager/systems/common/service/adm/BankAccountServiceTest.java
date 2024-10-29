package com.manager.systems.common.service.adm;

import java.sql.Connection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.BankAccountDTO;
import com.manager.systems.common.dto.adm.ReportBankAccountDTO;
import com.manager.systems.common.service.impl.adm.BankAccountServiceImpl;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;

public class BankAccountServiceTest 
{
	private Connection connection = null;
	private BankAccountService bankAccountService = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.bankAccountService = new BankAccountServiceImpl(this.connection);
	}
	
	@Test
	public void crud() throws Exception
	{
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.bankAccountService);
	
		//Save
		BankAccountDTO bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setId("1");
		bankAccountDTO.setDescription("Teste01");
		bankAccountDTO.setInactive(false);
		//bankAccountDTO.setIntegrationSystems(new IntegrationSystems(1, "1"));
		bankAccountDTO.setBankBalanceAvailable(0.01);
		bankAccountDTO.setBankLimitAvailable(0.01);
		bankAccountDTO.setChangeData(new ChangeData(1L));
		final boolean isSaved = this.bankAccountService.save(bankAccountDTO);
		Assert.assertTrue(isSaved);
			
		//Get
		bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setId("1");
		this.bankAccountService.get(bankAccountDTO);
		Assert.assertEquals("Teste01", bankAccountDTO.getDescription());
		
		//Inactive
		bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setId("1");
		bankAccountDTO.setInactive(true);
		bankAccountDTO.setChangeData(new ChangeData(1L));
		final boolean isInactive = bankAccountService.inactive(bankAccountDTO);
		Assert.assertTrue(isInactive);
		
		//Get
		bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setId("1");
		this.bankAccountService.get(bankAccountDTO);
		Assert.assertEquals(true, bankAccountDTO.isInactive());
		
		//Update
		bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setId("1");
		bankAccountDTO.setDescription("Teste02");
		bankAccountDTO.setInactive(false);
		bankAccountDTO.setChangeData(new ChangeData(1L));
		final boolean isUpdate = bankAccountService.save(bankAccountDTO);
		Assert.assertTrue(isUpdate);
		
		//Get
		bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setId("1");
		this.bankAccountService.get(bankAccountDTO);
		Assert.assertEquals("Teste02", bankAccountDTO.getDescription());
		
		//GetAll
		ReportBankAccountDTO reportBankAccountDTO = new ReportBankAccountDTO();
		reportBankAccountDTO.setBankAccountIdFrom("1");
		reportBankAccountDTO.setBankAccountIdTo("1");
		reportBankAccountDTO.setDescription("Teste02");
		reportBankAccountDTO.setInactive("0");
		this.bankAccountService.getAll(reportBankAccountDTO);
		Assert.assertTrue(reportBankAccountDTO.getBankAccounts().size()>0);
		
		final ReportBankAccountDTO reportBankAccount = new ReportBankAccountDTO();
		reportBankAccount.setUserOperation(1L);
		List<Combobox> itens = this.bankAccountService.getAllCombobox(reportBankAccount);
		Assert.assertTrue(itens.size()>0);
		
		reportBankAccountDTO = new ReportBankAccountDTO();
		reportBankAccountDTO.setDescription("anderson");
		reportBankAccountDTO.setInactive("0");
		itens = this.bankAccountService.getAllAutocomplete(reportBankAccountDTO);
		Assert.assertTrue(itens.size()>0);
	}	
}