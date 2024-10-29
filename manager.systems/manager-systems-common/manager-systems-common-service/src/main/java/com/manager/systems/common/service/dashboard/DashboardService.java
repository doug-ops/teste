package com.manager.systems.common.service.dashboard;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dto.dashboard.MovementPendingDTO;

public interface DashboardService {
	List<MovementPendingDTO> getMovementPendingOldSystem(Connection connection) throws Exception;

	List<MovementPendingDTO> getMovementPending(Connection connection) throws Exception;
}