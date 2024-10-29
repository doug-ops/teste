package com.manager.systems.common.dao.dashboard;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dto.dashboard.MovementPendingDTO;

public interface DashboardDao {
	List<MovementPendingDTO> getMovementPendingOldSystem(Connection connection) throws Exception;

	List<MovementPendingDTO> getMovementPending(Connection connection) throws Exception;
}