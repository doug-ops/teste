package com.manager.systems.common.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.ComboboxDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.ComboboxFilterDTO;
import com.manager.systems.common.vo.Combobox;

public class ComboboxDaoImpl implements ComboboxDao {

	private Connection connection;

	public ComboboxDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public List<Combobox> getCombobox(final ComboboxFilterDTO filter) throws Exception {
		final List<Combobox> itens = new ArrayList<>();

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();

		query.append("{");
		query.append("call pr_combobox");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT
		query.append("?  "); // 02 - IN _inactive BIT
		query.append(")");
		query.append("}");

		ResultSet resultSet = null;

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, filter.getOperation());
			statement.setBoolean(2, filter.isInactive());

			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				itens.add(new Combobox(resultSet.getString(DatabaseConstants.COLUMN_ID), resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION)));
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
