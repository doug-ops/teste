package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.adm.PersonTypeDao;
import com.manager.systems.common.dao.impl.adm.PersonTypeDaoImpl;
import com.manager.systems.common.dto.adm.PersonTypeDTO;
import com.manager.systems.common.service.adm.PersonTypeService;
import com.manager.systems.common.vo.Combobox;

public class PersonTypeServiceImpl implements PersonTypeService 
{
	private PersonTypeDao companyTypeDao;
	
	public PersonTypeServiceImpl(final Connection connection) 
	{
		super();
		this.companyTypeDao = new PersonTypeDaoImpl(connection);
	}

	@Override

	public List<Combobox> getAllCombobox(final String objectType) throws Exception 
	{
		return this.companyTypeDao.getAllCombobox(objectType);
	}
	
	@Override
	public Integer save(final PersonTypeDTO personType) throws Exception 
	{
		return this.companyTypeDao.save(personType);
	}
}