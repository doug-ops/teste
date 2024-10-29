package com.manager.systems.common.service.adm;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.ProductGroupDTO;
import com.manager.systems.common.dto.adm.ReportProductGroupDTO;
import com.manager.systems.common.service.impl.adm.ProductGroupServiceImpl;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;

public class ProductGroupServiceTest 
{
	private Connection connection = null;
	private ProductGroupService productGroupService = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.productGroupService = new ProductGroupServiceImpl(this.connection);
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.productGroupService);
		
		//Save insert
		ProductGroupDTO productGroup = new ProductGroupDTO();
		productGroup.setId(1);
		productGroup.setDescription("Teste");
		productGroup.setInactive(false);
		productGroup.setChangeData(new ChangeData(1L));		
		final boolean isSaved = this.productGroupService.save(productGroup);
		Assert.assertTrue(isSaved);
		
		//Get
		productGroup = new ProductGroupDTO();
		productGroup.setId(1);
		this.productGroupService.get(productGroup);
		Assert.assertEquals("Teste", productGroup.getDescription());

		//Inactive
		productGroup = new ProductGroupDTO();
		productGroup.setId(1);
		productGroup.setInactive(true);
		productGroup.setChangeData(new ChangeData(1L));
		final boolean isInactive = this.productGroupService.inactive(productGroup);
		Assert.assertTrue(isInactive);
		
		//Get
		productGroup = new ProductGroupDTO();
		productGroup.setId(1);
		this.productGroupService.get(productGroup);
		Assert.assertTrue(productGroup.isInactive());
		
		//Save update
		productGroup = new ProductGroupDTO();
		productGroup.setId(1);
		productGroup.setDescription("Teste2");
		productGroup.setInactive(false);
		productGroup.setChangeData(new ChangeData(1L));		
		final boolean isUpdated = this.productGroupService.save(productGroup);
		Assert.assertTrue(isUpdated);		
		
		//Get
		productGroup = new ProductGroupDTO();
		productGroup.setId(1);
		this.productGroupService.get(productGroup);
		Assert.assertEquals("Teste2", productGroup.getDescription());
		Assert.assertFalse(productGroup.isInactive());		
		
		ReportProductGroupDTO reportProductGroup = new ReportProductGroupDTO();
		reportProductGroup.setProductGroupIdFrom("1");
		reportProductGroup.setProductGroupIdTo("1");
		reportProductGroup.setInactive("0");
		reportProductGroup.setDescription("Teste2");
		this.productGroupService.getAll(reportProductGroup);
		Assert.assertTrue(reportProductGroup.geProductGroups().size()>0);	
		
		reportProductGroup = new ReportProductGroupDTO();
		reportProductGroup.setProductGroupIdFrom("1");
		reportProductGroup.setProductGroupIdTo("1");
		reportProductGroup.setInactive("0");
		reportProductGroup.setDescription("Teste2");
		final List<Combobox> items = this.productGroupService.getAllCombobox(reportProductGroup);
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