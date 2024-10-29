package com.manager.systems.common.service.impl;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.IntegrationSystemsDao;
import com.manager.systems.common.dao.impl.IntegrationSystemsDaoImpl;
import com.manager.systems.common.dto.adm.IntegrationSystemsDTO;
import com.manager.systems.common.service.IntegrationSystemsService;
import com.manager.systems.common.vo.Combobox;

public class IntegrationSystemsServiceImpl implements IntegrationSystemsService 
{
	private IntegrationSystemsDao integrationSystemsDao;
	
	public IntegrationSystemsServiceImpl(final Connection connection) 
	{
		super();
		this.integrationSystemsDao = new IntegrationSystemsDaoImpl(connection);
	}

	@Override
	public List<Combobox> getAllCombobox() throws Exception 
	{
		return this.integrationSystemsDao.getAllCombobox();
	}

	@Override
	public boolean save(final IntegrationSystemsDTO integrationSystems) throws Exception
	{
		return this.integrationSystemsDao.save(integrationSystems);
	}

	@Override
	public boolean delete(final IntegrationSystemsDTO integrationSystems) throws Exception 
	{
		return this.integrationSystemsDao.delete(integrationSystems);
	}

	@Override
	public List<IntegrationSystemsDTO> getAll(final IntegrationSystemsDTO integrationSystems) throws Exception 
	{
		return this.integrationSystemsDao.getAll(integrationSystems);
	}
}