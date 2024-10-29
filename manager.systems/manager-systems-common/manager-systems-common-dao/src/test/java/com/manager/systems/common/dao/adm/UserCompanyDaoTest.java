package com.manager.systems.common.dao.adm;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.impl.adm.UserCompanyDaoImpl;
import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.CompanyDTO;
import com.manager.systems.common.dto.adm.UserCompnayDTO;

public class UserCompanyDaoTest 
{
	private Connection connection = null;
	private UserCompanyDao userCompanyDao = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.userCompanyDao = new UserCompanyDaoImpl(this.connection);
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.userCompanyDao);
		
		//Save insert
		UserCompnayDTO item = new UserCompnayDTO();
		item.setUserId(1);
		item.setCompanyId(1);
		item.setInactive(false);
		item.setUserChange(1);
		final boolean isSaved = this.userCompanyDao.save(item);
		Assert.assertTrue(isSaved);
		
		//Get
		String userId = "1";
		List<CompanyDTO> itens = this.userCompanyDao.getDataMovementExecutionCompany(userId);
		Assert.assertTrue(itens.size()>0);

		//Inactive
		item = new UserCompnayDTO();
		item.setUserId(1);
		item.setCompanyId(1);
		item.setInactive(true);
		item.setUserChange(1);
		final boolean isInactive = this.userCompanyDao.inactive(item);
		Assert.assertTrue(isInactive);
		
		//Get
		userId = "1";
		itens = this.userCompanyDao.getDataMovementExecutionCompany(userId);
		Assert.assertTrue(itens.size()>0);
		
		//Save update
		item = new UserCompnayDTO();
		item.setUserId(1);
		item.setCompanyId(1);
		item.setInactive(false);
		item.setUserChange(1);
		final boolean isUpdated = this.userCompanyDao.save(item);
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