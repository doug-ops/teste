package com.manager.systems.common.dao;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.impl.IntegrationSystemsDaoImpl;
import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.IntegrationSystemsDTO;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.ObjectType;

public class IntegrationSystemsDaoTest 
{
	private Connection connection = null;
	private IntegrationSystemsDao integrationSystemsDao = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.integrationSystemsDao = new IntegrationSystemsDaoImpl(this.connection);
	}
	
	@Test
	public void getAllCombobox() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.integrationSystemsDao);
		
		//Get all Combobox
		final List<Combobox> items = this.integrationSystemsDao.getAllCombobox();
		Assert.assertTrue(items.size()>0);	
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.integrationSystemsDao);
		
		//Save insert
		final IntegrationSystemsDTO integrationSystems = new IntegrationSystemsDTO();
		integrationSystems.setObjectId("3");
		integrationSystems.setObjectType(ObjectType.COMPANY.getType());
		integrationSystems.setIntegrationSystemId(1);
		integrationSystems.setLegacyId("1");
		integrationSystems.setInactive(false);
		integrationSystems.setUserChange(1L);
		integrationSystems.setChangeDate(Calendar.getInstance());
		final boolean isSaved = this.integrationSystemsDao.save(integrationSystems);
		Assert.assertTrue(isSaved);
		
		//Get All
		final List<IntegrationSystemsDTO> itens = this.integrationSystemsDao.getAll(integrationSystems);
		Assert.assertTrue(itens.size()>0);
		
		//Delete
		integrationSystems.setInactive(true);
		final boolean isDeleted = this.integrationSystemsDao.delete(integrationSystems);
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
