package com.manager.systems.common.service.impl;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.ComboboxDao;
import com.manager.systems.common.dao.impl.ComboboxDaoImpl;
import com.manager.systems.common.dto.ComboboxFilterDTO;
import com.manager.systems.common.service.ComboboxService;
import com.manager.systems.common.vo.Combobox;

public class ComboboxServiceImpl implements ComboboxService {

	private ComboboxDao comboboxDao;

	public ComboboxServiceImpl(final Connection connection) {
		super();
		this.comboboxDao = new ComboboxDaoImpl(connection);
	}

	@Override
	public List<Combobox> getCombobox(final ComboboxFilterDTO filter) throws Exception {
		return this.comboboxDao.getCombobox(filter);
	}
}