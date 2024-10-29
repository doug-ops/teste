package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;
import java.util.Map;

import com.manager.systems.common.dao.adm.AccessProfilePermissionDao;
import com.manager.systems.common.dao.impl.adm.AccessProfilePermissionDaoImpl;
import com.manager.systems.common.dto.adm.AccessProfilePermissionDTO;
import com.manager.systems.common.dto.adm.AccessProfilePermissionFilterDTO;
import com.manager.systems.common.service.adm.AccessProfilePermissionService;

public class AccessProfilePermissionServiceImpl implements AccessProfilePermissionService 
{
	private AccessProfilePermissionDao accessProfilePermissionDao;
	
	public AccessProfilePermissionServiceImpl(final Connection connection) 
	{
		super();
		this.accessProfilePermissionDao = new AccessProfilePermissionDaoImpl(connection);
	}

	@Override
	public boolean save(final AccessProfilePermissionDTO accessProfilePermission) throws Exception
	{
		return this.accessProfilePermissionDao.save(accessProfilePermission);
	}

	@Override
	public boolean inactiveAll(final AccessProfilePermissionDTO accessProfilePermission) throws Exception
	{
		return this.accessProfilePermissionDao.inactiveAll(accessProfilePermission);
	}

	@Override
	public Map<Integer, AccessProfilePermissionDTO> getByAccessProfile(final AccessProfilePermissionFilterDTO filter) throws Exception 
	{
		return this.accessProfilePermissionDao.getByAccessProfile(filter);
	}
}