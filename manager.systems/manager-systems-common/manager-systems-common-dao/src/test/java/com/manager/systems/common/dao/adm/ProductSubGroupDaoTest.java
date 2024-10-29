package com.manager.systems.common.dao.adm;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.impl.adm.ProductSubGroupDaoImpl;
import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.ProductSubGroupDTO;
import com.manager.systems.common.dto.adm.ReportProductSubGroupDTO;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;

public class ProductSubGroupDaoTest 
{
	private Connection connection = null;
	private ProductSubGroupDao productSubGroupDao = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.productSubGroupDao = new ProductSubGroupDaoImpl(this.connection);
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.productSubGroupDao);
		
		//Save insert
		ProductSubGroupDTO productSubGroup = new ProductSubGroupDTO();
		productSubGroup.setId(1);
		productSubGroup.setDescription("Teste");
		productSubGroup.setGroupId("1");
		productSubGroup.setInactive(false);
		productSubGroup.setChangeData(new ChangeData(1L));		
		final boolean isSaved = this.productSubGroupDao.save(productSubGroup);
		Assert.assertTrue(isSaved);
		
		//Get
		productSubGroup = new ProductSubGroupDTO();
		productSubGroup.setId(1);
		this.productSubGroupDao.get(productSubGroup);
		Assert.assertEquals("Teste", productSubGroup.getDescription());

		//Inactive
		productSubGroup = new ProductSubGroupDTO();
		productSubGroup.setId(1);
		productSubGroup.setInactive(true);
		productSubGroup.setChangeData(new ChangeData(1L));
		final boolean isInactive = this.productSubGroupDao.inactive(productSubGroup);
		Assert.assertTrue(isInactive);
		
		//Get
		productSubGroup = new ProductSubGroupDTO();
		productSubGroup.setId(1);
		this.productSubGroupDao.get(productSubGroup);
		Assert.assertTrue(productSubGroup.isInactive());
		
		//Save update
		productSubGroup = new ProductSubGroupDTO();
		productSubGroup.setId(1);
		productSubGroup.setDescription("Teste2");
		productSubGroup.setGroupId("1");
		productSubGroup.setInactive(false);
		productSubGroup.setChangeData(new ChangeData(1L));		
		final boolean isUpdated = this.productSubGroupDao.save(productSubGroup);
		Assert.assertTrue(isUpdated);		
		
		//Get
		productSubGroup = new ProductSubGroupDTO();
		productSubGroup.setId(1);
		this.productSubGroupDao.get(productSubGroup);
		Assert.assertEquals("Teste2", productSubGroup.getDescription());
		Assert.assertFalse(productSubGroup.isInactive());		
		
		ReportProductSubGroupDTO reportProductSubGroup = new ReportProductSubGroupDTO();
		reportProductSubGroup.setProductSubGroupIdFrom("1");
		reportProductSubGroup.setProductSubGroupIdTo("1");
		reportProductSubGroup.setProductGroupIds("1");	
		reportProductSubGroup.setInactive("0");
		reportProductSubGroup.setDescription("Teste2");
		this.productSubGroupDao.getAll(reportProductSubGroup);
		Assert.assertTrue(reportProductSubGroup.geProductSubGroups().size()>0);	
		
		reportProductSubGroup = new ReportProductSubGroupDTO();
		reportProductSubGroup.setProductSubGroupIdFrom("1");
		reportProductSubGroup.setProductSubGroupIdTo("1");
		reportProductSubGroup.setProductGroupIds("1");	
		reportProductSubGroup.setInactive("0");
		reportProductSubGroup.setDescription("Teste2");
		final List<Combobox> items = this.productSubGroupDao.getAllCombobox(reportProductSubGroup);
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