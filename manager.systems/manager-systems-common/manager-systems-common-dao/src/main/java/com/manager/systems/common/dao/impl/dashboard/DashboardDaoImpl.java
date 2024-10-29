package com.manager.systems.common.dao.impl.dashboard;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.dashboard.DashboardDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.dashboard.MovementPendingDTO;

public class DashboardDaoImpl implements DashboardDao {

	public DashboardDaoImpl() {
		super();
	}

	@Override
	public List<MovementPendingDTO> getMovementPendingOldSystem(final Connection connection) throws Exception {

		final List<MovementPendingDTO> itens = new ArrayList<MovementPendingDTO>();

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_rel_mov_pending_old_system");
		query.append("}");

		ResultSet resultSet = null;

		try {
			statement = connection.prepareCall(query.toString());
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				final MovementPendingDTO item = new MovementPendingDTO();
				item.setCustomer(resultSet.getString(DatabaseConstants.COLUMN_CUSTOMER));
				item.setMovementDate(resultSet.getString(DatabaseConstants.COLUMN_REGISTER_DATE));
				item.setOnline(resultSet.getBoolean(DatabaseConstants.COLUMN_REGISTER_ONLINE));
				item.setCountProducts(resultSet.getInt(DatabaseConstants.COLUMN_COUNT_PRODUCT));
				itens.add(item);
			}
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
		}
		return itens;
	}

	@Override
	public List<MovementPendingDTO> getMovementPending(final Connection connection) throws Exception {
		final List<MovementPendingDTO> itens = new ArrayList<MovementPendingDTO>();

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_dashboard");
		query.append("(?)");
		query.append("}");

		ResultSet resultSet = null;

		try {
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, 1);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				final MovementPendingDTO item = new MovementPendingDTO();
				item.setCustomer(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION));
				item.setMovementDate(resultSet.getString(DatabaseConstants.COLUMN_READING_DATE));
				item.setOnline((resultSet.getBoolean(DatabaseConstants.COLUMN_IS_OFFLINE) ? false : true));
				item.setCountProducts(resultSet.getInt(DatabaseConstants.COLUMN_COUNT_PRODUCT));
				itens.add(item);
			}
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
		}
		return itens;
	}
}