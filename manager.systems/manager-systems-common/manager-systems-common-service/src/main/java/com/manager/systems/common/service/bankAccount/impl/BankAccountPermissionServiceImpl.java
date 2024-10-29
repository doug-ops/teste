/*
 * Date create 21/06/2024.
 */
package com.manager.systems.common.service.bankAccount.impl;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.bankAccount.BankAccountPermissionDao;
import com.manager.systems.common.dao.bankAccount.impl.BankAccountPermissionDaoImpl;
import com.manager.systems.common.service.bankAccount.BankAccountPermissionService;
import com.manager.systems.common.vo.Combobox;

public class BankAccountPermissionServiceImpl implements BankAccountPermissionService {

	private BankAccountPermissionDao bankAccountPermissionDao;
	
	public BankAccountPermissionServiceImpl(final Connection connection) {
		super();
		this.bankAccountPermissionDao = new BankAccountPermissionDaoImpl(connection);
	}
	
	@Override
	public List<Combobox> getBankAccountPermissionsByUser(final long userId) throws Exception {
		return this.bankAccountPermissionDao.getBankAccountPermissionsByUser(userId);
	}
}