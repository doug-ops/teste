package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.adm.EstablishmentTypeDao;
import com.manager.systems.common.dao.impl.adm.EstablishmentTypeDaoImpl;
import com.manager.systems.common.dto.adm.EstablishmentTypeDTO;
import com.manager.systems.common.service.adm.EstablishmentTypeService;
import com.manager.systems.common.vo.Combobox;

public class EstablishmentTypeServiceImpl implements EstablishmentTypeService 
{
	private EstablishmentTypeDao establishmentType;
	
	public EstablishmentTypeServiceImpl(final Connection connection) 
	{
		super();
		this.establishmentType = new EstablishmentTypeDaoImpl(connection);
	}

	@Override

	public List<Combobox> getAllCombobox() throws Exception 
	{
		return this.establishmentType.getAllCombobox();
	}
	
	@Override
	public Integer save(final EstablishmentTypeDTO establishmentType) throws Exception 
	{
		return this.establishmentType.save(establishmentType);
	}
}