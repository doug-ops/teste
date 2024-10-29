package com.manager.systems.common.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import com.manager.systems.common.dao.CommonDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;

public class CommonDaoImpl implements CommonDao 
{
	private Connection connection;
	
	public CommonDaoImpl(final Connection connection)
	{
		super();
		this.connection = connection;
	}
	
	@Override
	public long getNextCode(final String tableName, final long initial) throws Exception
	{
		long nextId = initial;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_manager_next_free_code");
		query.append("(");
		query.append("?, "); //01 - table_search varchar(50)
		query.append("?  "); //02 - initial bigint
		query.append(")");
		query.append("}");
		
				
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setString(1, tableName);
			statement.setLong(2, initial);
			
	        resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	nextId = resultSet.getLong(DatabaseConstants.COLUMN_NEXT_ID); 
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
		
		if(nextId==0)
		{
			nextId = 1;
		}
		
		return nextId;
	}
}