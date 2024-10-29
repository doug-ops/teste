/*
 * Create Date 14/03/2022
 */
package com.manager.systems.common.dao.dashboard;

import java.util.Map;

import com.manager.systems.common.dto.dashboard.DashboardCompanyExecutionWeekDTO;

public interface DashboardCompanyExecutionWeekDao {
	void getAll(DashboardCompanyExecutionWeekDTO dashboardCompanyExecutionWeek) throws Exception;
	Map<String, Double> getChartMovements(DashboardCompanyExecutionWeekDTO dashboardCompanyExecutionWeek) throws Exception;
}