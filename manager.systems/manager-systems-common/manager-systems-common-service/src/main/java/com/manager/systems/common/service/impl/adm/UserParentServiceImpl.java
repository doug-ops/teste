package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.adm.UserParentDao;
import com.manager.systems.common.dao.impl.adm.UserParentDaoImpl;
import com.manager.systems.common.dto.adm.UserDTO;
import com.manager.systems.common.dto.adm.UserParentDTO;
import com.manager.systems.common.service.adm.UserParentService;
import com.manager.systems.common.vo.Combobox;

public class UserParentServiceImpl implements UserParentService 
{
	private UserParentDao userParentDao;
	
	public UserParentServiceImpl(final Connection connection) 
	{
		super();
		this.userParentDao = new UserParentDaoImpl(connection);
	}

	@Override
	public boolean save(final UserParentDTO userParent) throws Exception 
	{
		return this.userParentDao.save(userParent);
	}
	
	@Override
	public List<UserDTO> get(final long userId) throws Exception 
	{
		return this.userParentDao.get(userId);
	}
	
	@Override
	public List<Combobox> getAllParentCombobox(long userId) throws Exception 
	{
		return this.userParentDao.getAllParentCombobox(userId);
	}
	
	@Override
	public List<Combobox> getUserParentCombobox(long userId) throws Exception 
	{
		return this.userParentDao.getUserParentCombobox(userId);
	}

	@Override
	public boolean inactive(final UserParentDTO userParent) throws Exception {
		return this.userParentDao.inactive(userParent);
	}

	@Override
	public List<Combobox> getUserComboboxByClient(final long userId) throws Exception {
		return this.userParentDao.getUserComboboxByClient(userId);
	}

	@Override
	public List<Combobox> getAllUserParentComboboxByOperation(final long userId, final int operation) throws Exception {
		return this.userParentDao.getAllUserParentComboboxByOperation(userId, operation);
	}
}