package com.manager.systems.common.dao.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.PersonTypeDTO;
import com.manager.systems.common.vo.Combobox;

public interface PersonTypeDao 
{
	List<Combobox> getAllCombobox(String objectType) throws Exception;
	Integer save(PersonTypeDTO personType) throws Exception;
}