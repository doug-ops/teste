package com.manager.systems.common.dao.adm;

import java.sql.Connection;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.impl.adm.AccessProfilePermissionDaoImpl;
import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.AccessProfilePermissionDTO;
import com.manager.systems.common.dto.adm.AccessProfilePermissionFilterDTO;

public class AccessProfilePermissionDaoTest 
{
	private Connection connection = null;
	private AccessProfilePermissionDao accessProfilePermissionDao = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.accessProfilePermissionDao = new AccessProfilePermissionDaoImpl(this.connection);
	}
	
	@Test
	public void crud() throws Exception
	{
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.accessProfilePermissionDao);
		
		//Get 
		final AccessProfilePermissionFilterDTO filter = new AccessProfilePermissionFilterDTO();
		filter.setAccessProfileId(1);
		filter.setInactive(false);
		final Map<Integer, AccessProfilePermissionDTO> itens = this.accessProfilePermissionDao.getByAccessProfile(filter);
		Assert.assertTrue(itens.size()>0);
		
		//Save
		for (final Map.Entry<Integer, AccessProfilePermissionDTO> entry : itens.entrySet()) 
		{
			final boolean isSaved = this.accessProfilePermissionDao.save(entry.getValue());
			Assert.assertTrue(isSaved);	
		}
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