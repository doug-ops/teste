package com.manager.systems.web.movements.service;

import java.sql.Connection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.web.movements.dto.DocumentMovementDTO;
import com.manager.systems.web.movements.service.impl.DocumentMovementServiceImpl;

public class DocumentMovementServiceTest 
{
	private Connection connection = null;
	private DocumentMovementService documentMovementService = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.documentMovementService = new DocumentMovementServiceImpl(this.connection);
	}
	
	@Test
	public void getAll() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.documentMovementService);
		
		//Get all
		final DocumentMovementDTO documentMovement = new DocumentMovementDTO();
		documentMovement.setCompanyId(174L);
		documentMovement.setProviderId(2415L);
		documentMovement.setFinancialGroupId("24.000");
		documentMovement.setFinancialSubGroupId("24.001");
		documentMovement.setBankAccountId(192);
		documentMovement.setDocumentType("1,2,3");
		documentMovement.setDocumentNumber("2");
		documentMovement.setCredit(true);
		documentMovement.setDebit(true);
		documentMovement.setFinancial(false);
		documentMovement.setOpen(true);
		documentMovement.setClose(true);
		documentMovement.setRemoved(true);
		documentMovement.setDateFrom(StringUtils.convertStringDateHoraToLocalDateTime("2019-08-01 00:00:00"));
		documentMovement.setDateTo(StringUtils.convertStringDateHoraToLocalDateTime("2019-11-30 23:59:59"));
		documentMovement.setFilterBy(3);	
		this.documentMovementService.getAllDocumentMovement(documentMovement);
		Assert.assertTrue(documentMovement.getItens().size()>0);	
	}

	@After
	public void finaliza() throws Exception
	{
		if(this.connection!=null)
		{
			this.connection.close();
		}
		Assert.assertTrue(this.connection.isClosed());
		this.connection = null;		
	}
}
