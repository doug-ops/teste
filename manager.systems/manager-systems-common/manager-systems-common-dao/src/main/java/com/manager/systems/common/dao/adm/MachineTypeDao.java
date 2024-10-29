package com.manager.systems.common.dao.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.MachineTypeDTO;
import com.manager.systems.common.vo.Combobox;

public interface MachineTypeDao 
{
	List<Combobox> getAllCombobox() throws Exception;
	Integer save(MachineTypeDTO machineType) throws Exception;
}