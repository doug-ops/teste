package com.manager.systems.common.service;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.IntegrationSystemsDTO;
import com.manager.systems.common.service.impl.IntegrationSystemsServiceImpl;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.ObjectType;

public class IntegrationSystemsServiceTest 
{
	private Connection connection = null;
	private IntegrationSystemsService integrationSystemsService = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.integrationSystemsService = new IntegrationSystemsServiceImpl(this.connection);
	}
	
	@Test
	public void getAllCombobox() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.integrationSystemsService);
		
		//Get all Combobox
		final List<Combobox> items = this.integrationSystemsService.getAllCombobox();
		Assert.assertTrue(items.size()>0);	
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.integrationSystemsService);
		
		//Save insert
		final IntegrationSystemsDTO integrationSystems = new IntegrationSystemsDTO();
		integrationSystems.setObjectId("3");
		integrationSystems.setObjectType(ObjectType.COMPANY.getType());
		integrationSystems.setIntegrationSystemId(1);
		integrationSystems.setLegacyId("1");
		integrationSystems.setInactive(false);
		integrationSystems.setUserChange(1L);
		integrationSystems.setChangeDate(Calendar.getInstance());
		final boolean isSaved = this.integrationSystemsService.save(integrationSystems);
		Assert.assertTrue(isSaved);
		
		//Get All
		final List<IntegrationSystemsDTO> itens = this.integrationSystemsService.getAll(integrationSystems);
		Assert.assertTrue(itens.size()>0);
		
		//Delete
		integrationSystems.setInactive(true);
		final boolean isDeleted = this.integrationSystemsService.delete(integrationSystems);
		Assert.assertTrue(isDeleted);
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
