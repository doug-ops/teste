/**
 * Creation Date 04/02/2023.
 */
package com.manager.systems.common.dao.movement.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import com.manager.systems.common.dao.movement.DocumentMovementIdDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.movement.document.DocumentMovementIdDTO;

public class DocumentMovementIdDaoImpl implements DocumentMovementIdDao {

	private Connection connection;

	public DocumentMovementIdDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}
	
	@Override
	public DocumentMovementIdDTO generateDocumentMovementId(final boolean generateDocumentParentId, final boolean generateDocumentId) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;

		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_generate_document_movement_id");
		query.append("(");
		query.append("?, "); // 01 - IN _generateDocumentParentId BIT,
		query.append("?  "); // 02 - IN _generateDocumentId BIT,
		query.append(")");
		query.append("}");
		
		final DocumentMovementIdDTO item = new DocumentMovementIdDTO();

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setBoolean(1, generateDocumentParentId); // 01 - IN _generateDocumentParentId BIT,
			statement.setBoolean(2, generateDocumentId); // 02 - IN _generateDocumentId BIT,
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				item.setDocumentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID));
				item.setDocumentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_ID));
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
		
		return item;
	}
	
	@Override
	public long generateDocumentTransactionId() throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;

		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_generate_document_transaction_id");
		query.append("}");
		
		long result = 0L;

		try {
			statement = this.connection.prepareCall(query.toString());
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				result = resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_TRANSACTION_ID);
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
		
		return result;
	}
}
