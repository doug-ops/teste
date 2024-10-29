package com.manager.systems.common.dao.adm;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.impl.adm.AccessProfileDaoImpl;
import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.AccessProfileDTO;
import com.manager.systems.common.dto.adm.ReportAccessProfileDTO;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;

public class AccessProfileDaoTest 
{
	private Connection connection = null;
	private AccessProfileDao accessProfileDao = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.accessProfileDao = new AccessProfileDaoImpl(this.connection);
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.accessProfileDao);
		
		//Save insert
		AccessProfileDTO accessProfile = new AccessProfileDTO();
		accessProfile.setId(1);
		accessProfile.setDescription("Teste");
		accessProfile.setRole("Teste");
		accessProfile.setInactive(false);
		accessProfile.setChangeData(new ChangeData(1L));		
		final boolean isSaved = this.accessProfileDao.save(accessProfile);
		Assert.assertTrue(isSaved);
		
		//Get
		accessProfile = new AccessProfileDTO();
		accessProfile.setId(1);
		this.accessProfileDao.get(accessProfile);
		Assert.assertEquals("Teste", accessProfile.getDescription());

		//Inactive
		accessProfile = new AccessProfileDTO();
		accessProfile.setId(1);
		accessProfile.setInactive(true);
		accessProfile.setChangeData(new ChangeData(1L));
		final boolean isInactive = this.accessProfileDao.inactive(accessProfile);
		Assert.assertTrue(isInactive);
		
		//Get
		accessProfile = new AccessProfileDTO();
		accessProfile.setId(1);
		this.accessProfileDao.get(accessProfile);
		Assert.assertTrue(accessProfile.isInactive());
		
		//Save update
		accessProfile = new AccessProfileDTO();
		accessProfile.setId(1);
		accessProfile.setDescription("Teste2");
		accessProfile.setInactive(false);
		accessProfile.setChangeData(new ChangeData(1L));		
		final boolean isUpdated = this.accessProfileDao.save(accessProfile);
		Assert.assertTrue(isUpdated);		
		
		//Get
		accessProfile = new AccessProfileDTO();
		accessProfile.setId(1);
		this.accessProfileDao.get(accessProfile);
		Assert.assertEquals("Teste2", accessProfile.getDescription());
		Assert.assertFalse(accessProfile.isInactive());		
		
		ReportAccessProfileDTO report = new ReportAccessProfileDTO();
		report.setIdFrom("1");
		report.setIdTo("1");
		report.setInactive("0");
		report.setDescription("Teste2");
		this.accessProfileDao.getAll(report);
		Assert.assertTrue(report.getItens().size()>0);
		
		report = new ReportAccessProfileDTO();
		report.setIdFrom("1");
		report.setIdTo("1");
		report.setInactive("0");
		report.setDescription("Teste2");
		final List<Combobox> items = this.accessProfileDao.getAllCombobox(report);
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