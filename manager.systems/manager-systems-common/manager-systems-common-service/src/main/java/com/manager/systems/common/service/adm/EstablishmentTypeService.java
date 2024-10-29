package com.manager.systems.common.service.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.EstablishmentTypeDTO;
import com.manager.systems.common.vo.Combobox;

public interface EstablishmentTypeService 
{
	List<Combobox> getAllCombobox() throws Exception;
	Integer save(EstablishmentTypeDTO establishmentType) throws Exception;
}