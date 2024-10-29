package com.manager.systems.common.dao.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.GameTypeDTO;
import com.manager.systems.common.vo.Combobox;

public interface GameTypeDao 
{
	List<Combobox> getAllCombobox() throws Exception;
	Integer save(GameTypeDTO gameType) throws Exception;
}