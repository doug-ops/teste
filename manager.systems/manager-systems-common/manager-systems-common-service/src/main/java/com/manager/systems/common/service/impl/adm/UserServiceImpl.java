/*
 * Date create 30/11/2021
 */
package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;

import com.manager.systems.common.dao.adm.UserDao;
import com.manager.systems.common.dao.impl.adm.UserDaoImpl;
import com.manager.systems.common.service.adm.UserService;
import com.manager.systems.model.admin.User;

public class UserServiceImpl implements UserService 
{
	private UserDao userDao;
	
	public UserServiceImpl(final Connection connection) 
	{
		super();
		this.userDao = new UserDaoImpl(connection);
	}

	@Override
	public boolean save(final User user) throws Exception {
		return this.userDao.save(user);		
	}
	
	@Override
	public boolean updatelastAcessUser(final User user) throws Exception {
		return this.userDao.updatelastAcessUser(user);		
	}
	
	@Override
	public boolean updateLastAccessFinishUser(final User user) throws Exception {
		return this.userDao.updateLastAccessFinishUser(user);		
	}

	@Override
	public boolean saveConfigUser(final User user) throws Exception {
		return this.userDao.saveConfigUser(user);
	}
}