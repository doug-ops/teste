/*
 * Date create 02/09/2023.
 */
package com.manager.systems.web.jobs.service.impl;

import java.sql.Connection;

import com.manager.systems.web.jobs.dao.RomaneioDao;
import com.manager.systems.web.jobs.dao.impl.RomaneioDaoImpl;
import com.manager.systems.web.jobs.dto.RomaneioFilterDTO;
import com.manager.systems.web.jobs.service.RomaneioService;

public class RomaneioServiceImpl implements RomaneioService {

	private RomaneioDao romaneioDao;
	
	public RomaneioServiceImpl(final Connection connection) {
		super();
		this.romaneioDao = new RomaneioDaoImpl(connection);
	}
	
	@Override
	public void generateNextWeekMovement(final RomaneioFilterDTO filter) throws Exception {
		this.romaneioDao.generateNextWeekMovement(filter);
	}
}