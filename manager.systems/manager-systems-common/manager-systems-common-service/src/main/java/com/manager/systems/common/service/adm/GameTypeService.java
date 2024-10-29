package com.manager.systems.common.service.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.GameTypeDTO;
import com.manager.systems.common.vo.Combobox;

public interface GameTypeService 
{
	List<Combobox> getAllCombobox() throws Exception;
	Integer save(GameTypeDTO gameType) throws Exception;
}