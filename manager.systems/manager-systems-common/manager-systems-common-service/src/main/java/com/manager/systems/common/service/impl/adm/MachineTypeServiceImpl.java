package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.adm.MachineTypeDao;
import com.manager.systems.common.dao.impl.adm.MachineTypeDaoImpl;
import com.manager.systems.common.dto.adm.MachineTypeDTO;
import com.manager.systems.common.service.adm.MachineTypeService;
import com.manager.systems.common.vo.Combobox;

public class MachineTypeServiceImpl implements MachineTypeService 
{
	private MachineTypeDao machineTypeDao;
	
	public MachineTypeServiceImpl(final Connection connection) 
	{
		super();
		this.machineTypeDao = new MachineTypeDaoImpl(connection);
	}

	@Override

	public List<Combobox> getAllCombobox() throws Exception 
	{
		return this.machineTypeDao.getAllCombobox();
	}
	
	@Override
	public Integer save(final MachineTypeDTO machineType) throws Exception 
	{
		return this.machineTypeDao.save(machineType);
	}
}