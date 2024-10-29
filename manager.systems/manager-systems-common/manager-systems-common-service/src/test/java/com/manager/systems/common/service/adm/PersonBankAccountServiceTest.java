package com.manager.systems.common.service.adm;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.PersonBankAccountDTO;
import com.manager.systems.common.service.impl.adm.PersonBankAccountServiceImpl;
import com.manager.systems.common.vo.ObjectType;

public class PersonBankAccountServiceTest {
	private Connection connection = null;
	private PersonBankAccountService personBankAccountService = null;

	@Before
	public void inicializa() throws Exception {
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.personBankAccountService = new PersonBankAccountServiceImpl(this.connection);
	}

	@Test
	public void crud() throws Exception {
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.personBankAccountService);

		// Save insert
		final PersonBankAccountDTO item = new PersonBankAccountDTO();
		item.setObjectId(4);
		item.setObjectType(ObjectType.COMPANY.getType());
		item.setBankAccountId(19);
		item.setInactive(false);
		item.setUserChange(1L);
		item.setChangeDate(Calendar.getInstance());
		final boolean isSaved = this.personBankAccountService.save(item);
		Assert.assertTrue(isSaved);

		// Get All
		final List<PersonBankAccountDTO> itens = this.personBankAccountService.getAll(item);
		Assert.assertTrue(itens.size() > 0);

		// Delete
		item.setInactive(true);
		final boolean isDeleted = this.personBankAccountService.delete(item);
		Assert.assertTrue(isDeleted);
	}

	@After
	public void finaliza() throws Exception {
		if (this.connection != null) {
			this.connection.rollback();
			this.connection.close();
		}
		Assert.assertTrue(this.connection.isClosed());
		this.connection = null;
	}
}
