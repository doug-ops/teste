package com.manager.systems.common.service.impl;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.adm.BankDao;
import com.manager.systems.common.dao.impl.adm.BankDaoImpl;
import com.manager.systems.common.service.adm.BankService;
import com.manager.systems.common.vo.Combobox;

public class BankServiceImpl implements BankService 
{
	private BankDao bankDao;
	
	public BankServiceImpl(final Connection connection) 
	{
		super();
		this.bankDao = new BankDaoImpl(connection);
	}

	@Override
	public List<Combobox> getAllCombobox() throws Exception 
	{
		return this.bankDao.getAllCombobox();
	}
}