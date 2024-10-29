package com.manager.systems.common.dao.adm;

import java.sql.Connection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.impl.adm.FinancialTransferGroupDaoImpl;
import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.FinancialTransferGroupDTO;
import com.manager.systems.common.dto.adm.FinancialTransferGroupItemDTO;
import com.manager.systems.common.dto.adm.FinancialTransferProductDTO;
import com.manager.systems.common.vo.ChangeData;

public class FinancialTransferGroupDaoTest 
{
	private Connection connection = null;
	private FinancialTransferGroupDao financialTransferGroupDao = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.financialTransferGroupDao = new FinancialTransferGroupDaoImpl(this.connection);
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.financialTransferGroupDao);
		
		//Save insert
		FinancialTransferGroupDTO financialTransferGroup = new FinancialTransferGroupDTO();
		FinancialTransferGroupItemDTO financialTransferGroupItem = new FinancialTransferGroupItemDTO();
		FinancialTransferProductDTO financialTransferProduct = new FinancialTransferProductDTO();
		
		financialTransferGroup.setId(1);
		financialTransferGroup.setBankAccountOriginId(130);
		financialTransferGroup.setBankAccountDestinyId(0);
		financialTransferGroup.setOrder(1);
		financialTransferGroup.setDescription("Grupo Principal");
		financialTransferGroup.setInactive(false);
		financialTransferGroup.setChangeData(new ChangeData(1L));
		
		financialTransferGroupItem.setBankAccountId(130);
		financialTransferGroupItem.setId(1);
		financialTransferGroupItem.setGroupId(1);
		financialTransferGroupItem.setProviderId(2314L);
		financialTransferGroupItem.setCreditDebit(false);
		financialTransferGroupItem.setDescription("Bebidas");
		financialTransferGroupItem.setOrder(1);
		financialTransferGroupItem.setExpense(0);
		financialTransferGroupItem.setOverTotal(false);
		financialTransferGroupItem.setTransferState(2);
		financialTransferGroupItem.setTransferType(1);
		financialTransferGroupItem.setValueTransfer(244.44);
		financialTransferGroupItem.setInactive(false);
		financialTransferGroupItem.setUseRemainingBalance(false);
		financialTransferGroupItem.setChangeData(new ChangeData(1L));
		financialTransferGroup.addItem(financialTransferGroupItem);
		
		financialTransferProduct.setBankAccountOriginId(130);
		financialTransferProduct.setBankAccountDestinyId(0);
		financialTransferProduct.setGroupItem(1);
		financialTransferProduct.setProductId(100);
		financialTransferProduct.setInactive(false);
		financialTransferProduct.setChangeData(new ChangeData(1L));
		financialTransferGroup.addProduct(financialTransferGroup.getId(), financialTransferProduct);
		
		financialTransferProduct.setBankAccountOriginId(130);
		financialTransferProduct.setBankAccountDestinyId(0);
		financialTransferProduct.setGroupItem(1);
		financialTransferProduct.setProductId(101);
		financialTransferProduct.setInactive(false);
		financialTransferProduct.setChangeData(new ChangeData(1L));
		financialTransferGroup.addProduct(financialTransferGroup.getId(), financialTransferProduct);
		
		financialTransferProduct.setBankAccountOriginId(130);
		financialTransferProduct.setBankAccountDestinyId(0);
		financialTransferProduct.setGroupItem(1);
		financialTransferProduct.setProductId(102);
		financialTransferProduct.setInactive(false);
		financialTransferProduct.setChangeData(new ChangeData(1L));
		financialTransferGroup.addProduct(financialTransferGroup.getId(), financialTransferProduct);
		
		final boolean isSaved = this.financialTransferGroupDao.save(financialTransferGroup);
		Assert.assertTrue(isSaved);
		
		financialTransferGroup.setId(1);
		financialTransferGroup = new FinancialTransferGroupDTO();
		financialTransferGroup.setBankAccountOriginId(130);
		financialTransferGroup.setBankAccountDestinyId(0);
		financialTransferGroup.setInactive(false);
		financialTransferGroup.setChangeData(new ChangeData(1L));
		financialTransferGroupItem = new FinancialTransferGroupItemDTO();
		financialTransferGroupItem.setInactive(false);
		financialTransferGroup.addItem(financialTransferGroupItem);
		this.financialTransferGroupDao.get(financialTransferGroup);
		Assert.assertTrue("Grupo Principal".equalsIgnoreCase(financialTransferGroup.getDescription()));
		
		/**
		//Get
		financialGroup = new FinancialGroupDTO();
		financialGroup.setId("1");
		this.financialGroupDao.get(financialGroup);
		Assert.assertEquals("Teste", financialGroup.getDescription());

		//Inactive
		financialGroup = new FinancialGroupDTO();
		financialGroup.setId("1");
		financialGroup.setInactive(true);
		financialGroup.setChangeData(new ChangeData(1L));
		final boolean isInactive = this.financialGroupDao.inactive(financialGroup);
		Assert.assertTrue(isInactive);
		
		//Get
		financialGroup = new FinancialGroupDTO();
		financialGroup.setId("1");
		this.financialGroupDao.get(financialGroup);
		Assert.assertTrue(financialGroup.isInactive());
		
		//Save update
		financialGroup = new FinancialGroupDTO();
		financialGroup.setId("1");
		financialGroup.setDescription("Teste2");
		financialGroup.setInactive(false);
		financialGroup.setChangeData(new ChangeData(1L));		
		final boolean isUpdated = this.financialGroupDao.save(financialGroup);
		Assert.assertTrue(isUpdated);		
		
		//Get
		financialGroup = new FinancialGroupDTO();
		financialGroup.setId("1");
		this.financialGroupDao.get(financialGroup);
		Assert.assertEquals("Teste2", financialGroup.getDescription());
		Assert.assertFalse(financialGroup.isInactive());		
		
		final ReportFinancialGroupDTO reportFinancialGroup = new ReportFinancialGroupDTO();
		reportFinancialGroup.setFinancialGroupIdFrom("1");
		reportFinancialGroup.setFinancialGroupIdTo("1");
		reportFinancialGroup.setInactive("0");
		reportFinancialGroup.setDescription("Teste2");
		this.financialGroupDao.getAll(reportFinancialGroup);
		Assert.assertTrue(reportFinancialGroup.geFinancialGroups().size()>0);
		
		final List<Combobox> items = this.financialGroupDao.getAllCombobox();
		Assert.assertTrue(items.size()>0);
		*/
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
