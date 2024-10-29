package com.manager.systems.common.service.impl.dashboard;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.dashboard.DashboardDao;
import com.manager.systems.common.dao.impl.dashboard.DashboardDaoImpl;
import com.manager.systems.common.dto.dashboard.MovementPendingDTO;
import com.manager.systems.common.service.dashboard.DashboardService;

public class DashboardServiceImpl implements DashboardService {

	private DashboardDao dashboardDao;

	public DashboardServiceImpl() {
		super();
		this.dashboardDao = new DashboardDaoImpl();
	}

	@Override
	public List<MovementPendingDTO> getMovementPendingOldSystem(final Connection connection) throws Exception {
		return this.dashboardDao.getMovementPendingOldSystem(connection);
	}

	@Override
	public List<MovementPendingDTO> getMovementPending(final Connection connection) throws Exception {
		return this.dashboardDao.getMovementPending(connection);
	}
}
