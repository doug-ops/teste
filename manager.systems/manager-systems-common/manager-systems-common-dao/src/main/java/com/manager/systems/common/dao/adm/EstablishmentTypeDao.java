package com.manager.systems.common.dao.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.EstablishmentTypeDTO;
import com.manager.systems.common.vo.Combobox;

public interface EstablishmentTypeDao 
{
	List<Combobox> getAllCombobox() throws Exception;
	Integer save(EstablishmentTypeDTO establishmentType) throws Exception;
}