package com.manager.systems.common.service.adm;

import java.sql.Connection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.FinancialSubGroupDTO;
import com.manager.systems.common.dto.adm.ReportFinancialSubGroupDTO;
import com.manager.systems.common.service.impl.adm.FinancialSubGroupServiceImpl;
import com.manager.systems.common.vo.ChangeData;

public class FinancialSubGroupServiceTest 
{
	private Connection connection = null;
	private FinancialSubGroupService financialSubGroupService = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.financialSubGroupService = new FinancialSubGroupServiceImpl(this.connection);
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.financialSubGroupService);
		
		//Save insert
		FinancialSubGroupDTO financialSubGroup = new FinancialSubGroupDTO();
		financialSubGroup.setId("1");
		financialSubGroup.setDescription("Teste");
		financialSubGroup.setGroupId("1");
		financialSubGroup.setInactive(false);
		financialSubGroup.setChangeData(new ChangeData(1L));		
		final boolean isSaved = this.financialSubGroupService.save(financialSubGroup);
		Assert.assertTrue(isSaved);
		
		//Get
		financialSubGroup = new FinancialSubGroupDTO();
		financialSubGroup.setId("1");
		this.financialSubGroupService.get(financialSubGroup);
		Assert.assertEquals("Teste", financialSubGroup.getDescription());

		//Inactive
		financialSubGroup = new FinancialSubGroupDTO();
		financialSubGroup.setId("1");
		financialSubGroup.setInactive(true);
		financialSubGroup.setChangeData(new ChangeData(1L));
		final boolean isInactive = this.financialSubGroupService.inactive(financialSubGroup);
		Assert.assertTrue(isInactive);
		
		//Get
		financialSubGroup = new FinancialSubGroupDTO();
		financialSubGroup.setId("1");
		this.financialSubGroupService.get(financialSubGroup);
		Assert.assertTrue(financialSubGroup.isInactive());
		
		//Save update
		financialSubGroup = new FinancialSubGroupDTO();
		financialSubGroup.setId("1");
		financialSubGroup.setDescription("Teste2");
		financialSubGroup.setGroupId("1");
		financialSubGroup.setInactive(false);
		financialSubGroup.setChangeData(new ChangeData(1L));		
		final boolean isUpdated = this.financialSubGroupService.save(financialSubGroup);
		Assert.assertTrue(isUpdated);		
		
		//Get
		financialSubGroup = new FinancialSubGroupDTO();
		financialSubGroup.setId("1");
		this.financialSubGroupService.get(financialSubGroup);
		Assert.assertEquals("Teste2", financialSubGroup.getDescription());
		Assert.assertFalse(financialSubGroup.isInactive());		
		
		final ReportFinancialSubGroupDTO reportFinancialSubGroup = new ReportFinancialSubGroupDTO();
		reportFinancialSubGroup.setFinancialSubGroupIdFrom("1");
		reportFinancialSubGroup.setFinancialSubGroupIdTo("1");
		reportFinancialSubGroup.setFinancialGroupIds("1");	
		reportFinancialSubGroup.setInactive("0");
		reportFinancialSubGroup.setDescription("Teste2");
		this.financialSubGroupService.getAll(reportFinancialSubGroup);
		Assert.assertTrue(reportFinancialSubGroup.geFinancialSubGroups().size()>0);	
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
