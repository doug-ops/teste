package com.manager.systems.common.dao;

import java.util.List;

import com.manager.systems.common.dto.ComboboxFilterDTO;
import com.manager.systems.common.vo.Combobox;

public interface ComboboxDao {
	List<Combobox> getCombobox(ComboboxFilterDTO filter) throws Exception;
}