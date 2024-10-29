package com.manager.systems.common.service.adm;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.AccessProfileDTO;
import com.manager.systems.common.dto.adm.ReportAccessProfileDTO;
import com.manager.systems.common.service.impl.adm.AccessProfileServiceImpl;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;

public class AccessProfileServiceTest 
{
	private Connection connection = null;
	private AccessProfileService accessProfileService = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.accessProfileService = new AccessProfileServiceImpl(this.connection);
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.accessProfileService);
		
		//Save insert
		AccessProfileDTO accessProfile = new AccessProfileDTO();
		accessProfile.setId(1);
		accessProfile.setDescription("Teste");
		accessProfile.setRole("Teste");
		accessProfile.setInactive(false);
		accessProfile.setChangeData(new ChangeData(1L));		
		final boolean isSaved = this.accessProfileService.save(accessProfile);
		Assert.assertTrue(isSaved);
		
		//Get
		accessProfile = new AccessProfileDTO();
		accessProfile.setId(1);
		this.accessProfileService.get(accessProfile);
		Assert.assertEquals("Teste", accessProfile.getDescription());

		//Inactive
		accessProfile = new AccessProfileDTO();
		accessProfile.setId(1);
		accessProfile.setInactive(true);
		accessProfile.setChangeData(new ChangeData(1L));
		final boolean isInactive = this.accessProfileService.inactive(accessProfile);
		Assert.assertTrue(isInactive);
		
		//Get
		accessProfile = new AccessProfileDTO();
		accessProfile.setId(1);
		this.accessProfileService.get(accessProfile);
		Assert.assertTrue(accessProfile.isInactive());
		
		//Save update
		accessProfile = new AccessProfileDTO();
		accessProfile.setId(1);
		accessProfile.setDescription("Teste2");
		accessProfile.setInactive(false);
		accessProfile.setChangeData(new ChangeData(1L));		
		final boolean isUpdated = this.accessProfileService.save(accessProfile);
		Assert.assertTrue(isUpdated);		
		
		//Get
		accessProfile = new AccessProfileDTO();
		accessProfile.setId(1);
		this.accessProfileService.get(accessProfile);
		Assert.assertEquals("Teste2", accessProfile.getDescription());
		Assert.assertFalse(accessProfile.isInactive());		
		
		ReportAccessProfileDTO report = new ReportAccessProfileDTO();
		report.setIdFrom("1");
		report.setIdTo("1");
		report.setInactive("0");
		report.setDescription("Teste2");
		this.accessProfileService.getAll(report);
		Assert.assertTrue(report.getItens().size()>0);
		
		report = new ReportAccessProfileDTO();
		report.setIdFrom("1");
		report.setIdTo("1");
		report.setInactive("0");
		report.setDescription("Teste2");
		final List<Combobox> items = this.accessProfileService.getAllCombobox(report);
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