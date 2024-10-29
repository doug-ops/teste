package com.manager.systems.common.service.impl;

import java.sql.Connection;

import com.manager.systems.common.dao.LoginDao;
import com.manager.systems.common.dao.impl.LoginDaoImpl;
import com.manager.systems.common.service.LoginService;
import com.manager.systems.model.admin.User;

public class LoginServiceImpl implements LoginService 
{
	private LoginDao loginDao;
	
	public LoginServiceImpl(final Connection connection) 
	{
		super();
		this.loginDao = new LoginDaoImpl(connection);
	}
	
	@Override
	public void validate(final User user) throws Exception 
	{
		this.loginDao.validate(user);
	}
}