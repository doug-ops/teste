package com.manager.systems.common.service;

import java.util.List;

import com.manager.systems.common.dto.ComboboxFilterDTO;
import com.manager.systems.common.vo.Combobox;

public interface ComboboxService {
	List<Combobox> getCombobox(ComboboxFilterDTO filter) throws Exception;
}
