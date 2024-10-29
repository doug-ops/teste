package com.manager.systems.common.service.impl.address;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.manager.systems.common.dao.address.AddressDao;
import com.manager.systems.common.dao.impl.address.AddressDaoImpl;
import com.manager.systems.common.service.address.AddressService;
import com.manager.systems.common.vo.Combobox;

public class AddressServiceImpl implements AddressService 
{
	private AddressDao addressDao;
	
	public AddressServiceImpl(final Connection connection) 
	{
		super();
		this.addressDao = new AddressDaoImpl(connection);
	}

	@Override
	public List<Combobox> getAllStates() throws Exception 
	{
		return this.addressDao.getAllStates();
	}

	@Override
	public List<Combobox> getAllCountry() throws Exception 
	{
		return this.addressDao.getAllCountry();
	}

	@Override
	public Map<String, Combobox> getAllStatesMap() throws Exception 
	{
		return this.addressDao.getAllStatesMap();
	}
}