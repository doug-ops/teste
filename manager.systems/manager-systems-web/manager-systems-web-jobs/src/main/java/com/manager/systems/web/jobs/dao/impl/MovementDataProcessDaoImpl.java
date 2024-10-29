/**
 * Date create 03/04/2020.
 */
package com.manager.systems.web.jobs.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import com.manager.systems.web.jobs.dao.MovementDataProcessDao;
import com.manager.systems.web.jobs.utils.DatabaseConstants;

public class MovementDataProcessDaoImpl implements MovementDataProcessDao 
{
	private Connection connection;
	
	/**
	 * Consctructor.
	 * @param connection the java.sql.Connection.
	 */
	public MovementDataProcessDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}

	@Override
	public void getNextMovementProcess(final MovementDataProccessDTO movementData) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_process_document_movement_filter");
		query.append("(");
		query.append("?,"); //01 - IN _operation INT
		query.append("?,"); //02 - IN _company_id BIGINT
		query.append("?");  //03 - IN _is_offline BIT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, movementData.getOperationType().getType());
			statement.setLong(2, movementData.getCompanyId());
			statement.setBoolean(3, movementData.isOffline());
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	          	movementData.addMovementsId(resultSet.getLong(DatabaseConstants.COLUMN_MOVEMENT_ID));
	          	movementData.setCompanyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID));
	        }
		}
		catch(final Exception ex)
		{
			throw ex;
		}
		finally
		{
			if(resultSet!=null)
			{
				resultSet.close();
			}
			if(statement!=null)
			{
				statement.close();
			}
		}
	}

	@Override
	public boolean processMovementData(final MovementDataProccessDTO movementData) throws Exception 
	{
		CallableStatement statement = null;
		ResultSet resultSet = null;
		
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_process_document_movement");
		query.append("(");
		query.append("?,"); //01 - IN _user BIGINT, 
		query.append("?,"); //02 - IN company_id BIGINT,
		query.append("?,"); //03 - IN _is_offline BIT,
		query.append("?,"); //04 - IN _movement_ids VARCHAR(500)
		query.append("?");  //05 - IN _is_preview BIT
		query.append(")");
		query.append("}");
		
		boolean result = false;
		
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setLong(1, movementData.getUserId());
			statement.setLong(2, movementData.getCompanyId());
			statement.setBoolean(3, movementData.isOffline());
			statement.setString(4, movementData.getMovementsIds());	
			statement.setBoolean(5, movementData.isPreview());
			
			final boolean isProcessed  = statement.execute();
			
			long documentParentId = 0;
			if(isProcessed) {
				resultSet = statement.getResultSet();
				while (resultSet.next()) {
					documentParentId = resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID);
				}
			}
			if(documentParentId>0) {
				result = true;
			}
		}
		catch(final Exception ex)
		{
			throw ex;
		}
		finally
		{
			if(statement!=null)
			{
				statement.close();
			}
		}
		return result;
	}
}