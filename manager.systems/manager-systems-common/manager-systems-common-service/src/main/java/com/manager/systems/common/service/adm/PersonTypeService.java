package com.manager.systems.common.service.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.PersonTypeDTO;
import com.manager.systems.common.vo.Combobox;

public interface PersonTypeService 
{
	List<Combobox> getAllCombobox(String objectType) throws Exception;
	Integer save(PersonTypeDTO personType) throws Exception;
}