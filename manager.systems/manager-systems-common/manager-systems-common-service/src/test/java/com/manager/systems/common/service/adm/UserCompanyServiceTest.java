package com.manager.systems.common.service.adm;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.CompanyDTO;
import com.manager.systems.common.dto.adm.UserCompnayDTO;
import com.manager.systems.common.service.impl.adm.UserCompanyServiceImpl;

public class UserCompanyServiceTest 
{
	private Connection connection = null;
	private UserCompanyService userCompanyService = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.userCompanyService = new UserCompanyServiceImpl(this.connection);
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.userCompanyService);
		
		//Save insert
		UserCompnayDTO item = new UserCompnayDTO();
		item.setUserId(1);
		item.setCompanyId(1);
		item.setInactive(false);
		item.setUserChange(1);
		final boolean isSaved = this.userCompanyService.save(item);
		Assert.assertTrue(isSaved);
		
		//Get
		long userId =1;
		List<CompanyDTO> itens = this.userCompanyService.get(userId);
		Assert.assertTrue(itens.size()>0);

		//Inactive
		item = new UserCompnayDTO();
		item.setUserId(1);
		item.setCompanyId(1);
		item.setInactive(true);
		item.setUserChange(1);
		final boolean isInactive = this.userCompanyService.inactive(item);
		Assert.assertTrue(isInactive);
		
		//Get
		userId =1;
		itens = this.userCompanyService.get(userId);
		Assert.assertTrue(itens.size()>0);
		
		//Save update
		item = new UserCompnayDTO();
		item.setUserId(1);
		item.setCompanyId(1);
		item.setInactive(false);
		item.setUserChange(1);
		final boolean isUpdated = this.userCompanyService.save(item);
		Assert.assertTrue(isUpdated);						
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