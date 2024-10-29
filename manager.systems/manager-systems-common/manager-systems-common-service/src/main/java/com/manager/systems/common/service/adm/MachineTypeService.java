package com.manager.systems.common.service.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.MachineTypeDTO;
import com.manager.systems.common.vo.Combobox;

public interface MachineTypeService 
{
	List<Combobox> getAllCombobox() throws Exception;
	Integer save(MachineTypeDTO machineType) throws Exception;
}