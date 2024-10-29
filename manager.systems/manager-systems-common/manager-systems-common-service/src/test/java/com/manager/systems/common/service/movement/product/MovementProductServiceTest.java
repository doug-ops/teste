package com.manager.systems.common.service.movement.product;

import java.sql.Connection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.movement.product.MovementProductDTO;
import com.manager.systems.common.service.impl.movement.product.MovementProductServiceImpl;

public class MovementProductServiceTest 
{
	private Connection connection = null;
	private MovementProductService movementProductService = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.movementProductService = new MovementProductServiceImpl(this.connection);
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.movementProductService);
				
		//Get Data Product
		final MovementProductDTO movementProduct = new MovementProductDTO();
		movementProduct.setProductId(8100L);
		movementProduct.setCompanyId(187L);
		this.movementProductService.get(movementProduct);
		Assert.assertEquals("BAR ABIERTO ORIG", movementProduct.getProductDescription());
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