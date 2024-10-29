package com.manager.systems.common.service.impl;

import java.sql.Connection;

import com.manager.systems.common.dao.CommonDao;
import com.manager.systems.common.dao.impl.CommonDaoImpl;
import com.manager.systems.common.service.CommonService;

public class CommonServiceImpl implements CommonService 
{
	private CommonDao commonDao;
	
	public CommonServiceImpl(final Connection connection) 
	{
		super();
		this.commonDao = new CommonDaoImpl(connection);
	}

	@Override
	public long getNextCode(final String tableName, final long initial) throws Exception 
	{
		return this.commonDao.getNextCode(tableName, initial);
	}
}