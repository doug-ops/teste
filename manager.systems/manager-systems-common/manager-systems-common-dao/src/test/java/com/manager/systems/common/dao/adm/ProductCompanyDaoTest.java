package com.manager.systems.common.dao.adm;

import java.sql.Connection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.impl.adm.ProductCompanyDaoImpl;
import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.ProductCompanyDTO;
import com.manager.systems.common.dto.adm.ReportProductCompanyDTO;
import com.manager.systems.common.vo.ChangeData;

public class ProductCompanyDaoTest 
{
	private Connection connection = null;
	private ProductCompanyDao productCompanyDao = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.productCompanyDao = new ProductCompanyDaoImpl(this.connection);
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.productCompanyDao);
		
		//Save insert
		ProductCompanyDTO productCompany = new ProductCompanyDTO();
		productCompany.setProductId(1L);
		productCompany.setCompanyId(114);
		productCompany.setBankAccountId(130);
		productCompany.setInactive(false);
		productCompany.setChangeData(new ChangeData(1L));		
		final boolean isSaved = this.productCompanyDao.save(productCompany);
		Assert.assertTrue(isSaved);
		
		//Get
		productCompany = new ProductCompanyDTO();
		productCompany.setProductId(1L);
		productCompany.setCompanyId(114);
		productCompany.setChangeData(new ChangeData(1L));		
		this.productCompanyDao.get(productCompany);
		Assert.assertEquals(1, productCompany.getProductId());

		//Inactive
		productCompany = new ProductCompanyDTO();
		productCompany.setProductId(1L);
		productCompany.setCompanyId(114);
		productCompany.setInactive(true);
		productCompany.setChangeData(new ChangeData(1L));
		final boolean isInactive = this.productCompanyDao.inactive(productCompany);
		Assert.assertTrue(isInactive);
		
		//Get
		productCompany = new ProductCompanyDTO();
		productCompany.setProductId(1L);
		productCompany.setCompanyId(114);
		productCompany.setChangeData(new ChangeData(1L));		
		this.productCompanyDao.get(productCompany);
		Assert.assertTrue(productCompany.isInactive());
		
		//Save update
		productCompany.setProductId(1L);
		productCompany.setCompanyId(114);
		productCompany.setBankAccountId(130);
		productCompany.setInactive(false);
		productCompany.setChangeData(new ChangeData(1L));		
		final boolean isUpdated = this.productCompanyDao.save(productCompany);
		Assert.assertTrue(isUpdated);
		
		//Get
		productCompany = new ProductCompanyDTO();
		productCompany.setProductId(1L);
		productCompany.setCompanyId(114);
		productCompany.setChangeData(new ChangeData(1L));		
		this.productCompanyDao.get(productCompany);
		Assert.assertFalse(productCompany.isInactive());
		
		final ReportProductCompanyDTO reportProductCompany = new ReportProductCompanyDTO();
		reportProductCompany.setProductId(1L);
		reportProductCompany.setCompanyId(114);
		reportProductCompany.setBankAccountId(130);
		reportProductCompany.setInactive(false);
		reportProductCompany.setChangeData(new ChangeData(1L));		
		this.productCompanyDao.getAll(reportProductCompany);
		Assert.assertTrue(reportProductCompany.getProductsCompanys().size()>0);
		
		//Delete
		productCompany = new ProductCompanyDTO();
		productCompany.setProductId(1L);
		productCompany.setCompanyId(114);
		productCompany.setInactive(true);
		productCompany.setChangeData(new ChangeData(1L));
		final boolean isDeleted = this.productCompanyDao.delete(productCompany);
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