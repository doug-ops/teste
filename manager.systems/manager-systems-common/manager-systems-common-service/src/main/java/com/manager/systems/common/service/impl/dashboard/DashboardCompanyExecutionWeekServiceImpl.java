package com.manager.systems.common.service.impl.dashboard;

import java.sql.Connection;
import java.util.Map;

import com.manager.systems.common.dao.dashboard.DashboardCompanyExecutionWeekDao;
import com.manager.systems.common.dao.impl.dashboard.DashboardCompanyExecutionWeekDaoImpl;
import com.manager.systems.common.dto.dashboard.DashboardCompanyExecutionWeekDTO;
import com.manager.systems.common.service.dashboard.DashboardCompanyExecutionWeekService;

public class DashboardCompanyExecutionWeekServiceImpl implements DashboardCompanyExecutionWeekService {

	private DashboardCompanyExecutionWeekDao dashboardCompanyExecutionWeekDao;

	public DashboardCompanyExecutionWeekServiceImpl(final Connection connection) {
		super();
		this.dashboardCompanyExecutionWeekDao = new DashboardCompanyExecutionWeekDaoImpl(connection);
	}

	@Override
	public void getAll(final DashboardCompanyExecutionWeekDTO dashboardCompanyExecutionWeek) throws Exception {
		 this.dashboardCompanyExecutionWeekDao.getAll(dashboardCompanyExecutionWeek);
	}

	@Override
	public Map<String, Double> getChartMovements(final DashboardCompanyExecutionWeekDTO dashboardCompanyExecutionWeek) throws Exception {
		return this.dashboardCompanyExecutionWeekDao.getChartMovements(dashboardCompanyExecutionWeek);
	}
}
