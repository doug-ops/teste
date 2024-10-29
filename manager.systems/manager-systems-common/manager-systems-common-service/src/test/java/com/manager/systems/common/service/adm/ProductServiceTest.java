package com.manager.systems.common.service.adm;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.ProductDTO;
import com.manager.systems.common.dto.adm.ReportProductDTO;
import com.manager.systems.common.service.impl.adm.ProductServiceImpl;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;

public class ProductServiceTest 
{
	private Connection connection = null;
	private ProductService productService = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.productService = new ProductServiceImpl(this.connection);
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.productService);
		
		//Save insert
		ProductDTO product = new ProductDTO();
		product.setId("1");
		product.setDescription("Teste");
		product.setSalePrice("10");
		product.setCostPrice("10");
		product.setConversionFactor("10");
		product.setCompanyId("1");
		product.setInputMovement("1000");
		product.setOutputMovement("5000");
		product.setGroupId("1");
		product.setSubGroupId("1");
		product.setInactive(false);
		product.setChangeData(new ChangeData(1L));		
		final boolean isSaved = this.productService.save(product);
		Assert.assertTrue(isSaved);
		
		//Get
		product = new ProductDTO();
		product.setId("1");
		this.productService.get(product);
		Assert.assertEquals("Teste", product.getDescription());

		//Inactive
		product = new ProductDTO();
		product.setId("1");
		product.setInactive(true);
		product.setChangeData(new ChangeData(1L));
		final boolean isInactive = this.productService.inactive(product);
		Assert.assertTrue(isInactive);
		
		//Get
		product = new ProductDTO();
		product.setId("1");
		this.productService.get(product);
		Assert.assertTrue(product.isInactive());
		
		//Save update
		product.setId("1");
		product.setDescription("Teste2");
		product.setSalePrice("10");
		product.setCostPrice("10");
		product.setConversionFactor("10");
		product.setCompanyId("1");
		product.setInputMovement("1000");
		product.setOutputMovement("5000");
		product.setGroupId("1");
		product.setSubGroupId("1");
		product.setInactive(false);
		product.setChangeData(new ChangeData(1L));
		final boolean isUpdated = this.productService.save(product);
		Assert.assertTrue(isUpdated);		
		
		//Get
		product = new ProductDTO();
		product.setId("1");
		this.productService.get(product);
		Assert.assertEquals("Teste2", product.getDescription());
		Assert.assertFalse(product.isInactive());		
		
		ReportProductDTO reportProduct = new ReportProductDTO();
		reportProduct.setProductIdFrom("1");
		reportProduct.setProductIdTo("1");
		reportProduct.setProductGroupIds("1");	
		reportProduct.setInactive("0");
		reportProduct.setDescription("Teste2");
		this.productService.getAll(reportProduct);
		Assert.assertTrue(reportProduct.geProducts().size()>0);
		
		reportProduct = new ReportProductDTO();
		reportProduct.setProductIdFrom("1");
		reportProduct.setProductIdTo("1");
		reportProduct.setInactive("0");
		reportProduct.setDescription("Teste2");
		final List<Combobox> itens = this.productService.getAllAutocomplete(reportProduct);
		Assert.assertTrue(itens.size()>0);	
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