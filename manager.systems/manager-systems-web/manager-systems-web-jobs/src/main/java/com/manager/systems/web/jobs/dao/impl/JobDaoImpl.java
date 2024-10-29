package com.manager.systems.web.jobs.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.web.jobs.dao.JobDao;
import com.manager.systems.web.jobs.dto.JobDTO;
import com.manager.systems.web.jobs.utils.DatabaseConstants;

public class JobDaoImpl implements JobDao 
{
	@Override
	public boolean save(final JobDTO job, final Connection connection) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_job");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _id INT,
		query.append("?, "); //03 - IN _parent_id INT
		query.append("?, "); //04 - IN _description VARCHAR(20), 
		query.append("?, "); //05 - IN _sync_timer VARCHAR(20), 
		query.append("?, "); //06 - IN _records_processed INT, 
		query.append("?, "); //07 - IN _initial_record VARCHAR(20), 
		query.append("?, "); //08 - IN _final_record VARCHAR(20), 
		query.append("?, "); //09 - IN _initial_version_record TIMESTAMP, 
		query.append("?, "); //10 - IN _final_version_record TIMESTAMP, 
		query.append("?, "); //11 - IN _processing_status BIT, 
		query.append("?, "); //12 - IN _processing_message VARCHAR(500), 
		query.append("?, "); //13 - IN _processing_data DATETIME, 
		query.append("?, "); //14 - IN _inactive int, 
		query.append("?  "); //15 - IN _user_change BIGINT		
		query.append(")");
		query.append("}");
				
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, 1); //01 - IN _operation INT,	        
			statement.setInt(2, job.getId()); //02 - IN _id INT,
			statement.setInt(3, job.getParentId()); //03 - IN _parent_id INT
			statement.setString(4, job.getDescription()); //04 - IN _description VARCHAR(20), 
			statement.setString(5, job.getSyncTimer()); //05 - IN _sync_timer VARCHAR(20), 
			statement.setInt(6, job.getRecordsProcessed()); //06 - IN _records_processed INT, 
			statement.setString(7, job.getInitialRecord()); //07 - IN _initial_record VARCHAR(20), 
			statement.setString(8, job.getFinalRecord()); //08 - IN _final_record VARCHAR(20), 
			statement.setBytes(9, job.getVersionInitialRecord()); //09 - IN _initial_version_record BINARY(20), 
			statement.setBytes(10, job.getVersionFinalRecord()); //10 - IN _final_version_record BINARY(20), 
			statement.setBoolean(11, job.isProcessingStatus()); //11 - IN _processing_status BIT, 
			statement.setString(12, job.getProcessingMessage()); //12 - IN _processing_message VARCHAR(500), 
			if(job.getProcessingData()!=null)
			{
				statement.setTimestamp(13, Timestamp.valueOf(job.getProcessingData())); //13 - IN _processing_data DATETIME, 				
			}
			else
			{
				statement.setNull(13, Types.TIMESTAMP); //13 - IN _processing_data DATETIME, 
			}
			statement.setBoolean(14, job.isInactive());  //14 - IN _inactive int, 
			statement.setLong(15, job.getUserChange());  //15 - IN _user_change BIGINT	
			result = (statement.executeUpdate()>0);  
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

	@Override
	public boolean inactive(final JobDTO job, final Connection connection) throws Exception
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_job");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _id INT,
		query.append("?, "); //03 - IN _parent_id INT
		query.append("?, "); //04 - IN _description VARCHAR(20), 
		query.append("?, "); //05 - IN _sync_timer VARCHAR(20), 
		query.append("?, "); //06 - IN _records_processed INT, 
		query.append("?, "); //07 - IN _initial_record VARCHAR(20), 
		query.append("?, "); //08 - IN _final_record VARCHAR(20), 
		query.append("?, "); //09 - IN _initial_version_record TIMESTAMP, 
		query.append("?, "); //10 - IN _final_version_record TIMESTAMP, 
		query.append("?, "); //11 - IN _processing_status BIT, 
		query.append("?, "); //12 - IN _processing_message VARCHAR(500), 
		query.append("?, "); //13 - IN _processing_data DATETIME, 
		query.append("?, "); //14 - IN _inactive int, 
		query.append("?  "); //15 - IN _user_change BIGINT		
		query.append(")");
		query.append("}");
				
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, 2); //01 - IN _operation INT,	        
			statement.setInt(2, job.getId()); //02 - IN _id INT,
			statement.setInt(3, job.getParentId()); //03 - IN _parent_id INT
			statement.setNull(4, Types.VARCHAR); //04 - IN _description VARCHAR(20), 
			statement.setNull(5, Types.VARCHAR); //05 - IN _sync_timer VARCHAR(20), 
			statement.setNull(6, Types.INTEGER); //06 - IN _records_processed INT, 
			statement.setNull(7, Types.VARCHAR); //07 - IN _initial_record VARCHAR(20), 
			statement.setNull(8, Types.VARCHAR); //08 - IN _final_record VARCHAR(20), 
			statement.setNull(9, Types.BINARY); //09 - IN _initial_version_record BINARY(20), 
			statement.setNull(10, Types.BINARY); //10 - IN _final_version_record BINARY(20), 
			statement.setNull(11, Types.BIT); //11 - IN _processing_status BIT, 
			statement.setNull(12, Types.VARCHAR); //12 - IN _processing_message VARCHAR(500), 
			statement.setNull(13, Types.DATE); //13 - IN _processing_data DATETIME, 
			statement.setBoolean(14, job.isInactive());  //14 - IN _inactive int, 
			statement.setLong(15, job.getUserChange());  //15 - IN _user_change BIGINT	
			result = (statement.executeUpdate()>0);  
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

	@Override
	public void get(final JobDTO job, final Connection connection) throws Exception 
	{	
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_job");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _id INT,
		query.append("?, "); //03 - IN _parent_id INT
		query.append("?, "); //04 - IN _description VARCHAR(20), 
		query.append("?, "); //05 - IN _sync_timer VARCHAR(20), 
		query.append("?, "); //06 - IN _records_processed INT, 
		query.append("?, "); //07 - IN _initial_record VARCHAR(20), 
		query.append("?, "); //08 - IN _final_record VARCHAR(20), 
		query.append("?, "); //09 - IN _initial_version_record TIMESTAMP, 
		query.append("?, "); //10 - IN _final_version_record TIMESTAMP, 
		query.append("?, "); //11 - IN _processing_status BIT, 
		query.append("?, "); //12 - IN _processing_message VARCHAR(500), 
		query.append("?, "); //13 - IN _processing_data DATETIME, 
		query.append("?, "); //14 - IN _inactive int, 
		query.append("?  "); //15 - IN _user_change BIGINT		
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
				
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, 3); //01 - IN _operation INT,	        
			statement.setInt(2, job.getId()); //02 - IN _id INT,
			statement.setInt(3, job.getParentId()); //03 - IN _parent_id INT
			statement.setNull(4, Types.VARCHAR); //04 - IN _description VARCHAR(20), 
			statement.setNull(5, Types.VARCHAR); //05 - IN _sync_timer VARCHAR(20), 
			statement.setNull(6, Types.INTEGER); //06 - IN _records_processed INT, 
			statement.setNull(7, Types.VARCHAR); //07 - IN _initial_record VARCHAR(20), 
			statement.setNull(8, Types.VARCHAR); //08 - IN _final_record VARCHAR(20), 
			statement.setNull(9, Types.BINARY); //09 - IN _initial_version_record BINARY(20), 
			statement.setNull(10, Types.BINARY); //10 - IN _final_version_record BINARY(20), 
			statement.setNull(11, Types.BIT); //11 - IN _processing_status BIT, 
			statement.setNull(12, Types.VARCHAR); //12 - IN _processing_message VARCHAR(500), 
			statement.setNull(13, Types.DATE); //13 - IN _processing_data DATETIME, 
			statement.setBoolean(14, job.isInactive());  //14 - IN _inactive int, 
			statement.setLong(15, job.getUserChange());  //15 - IN _user_change BIGINT	
			
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	job.setId(resultSet.getInt(DatabaseConstants.COLUMN_ID));
	        	job.setParentId(resultSet.getInt(DatabaseConstants.COLUMN_PARENT_ID));
	        	job.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
	        	job.setSyncTimer(resultSet.getString(DatabaseConstants.COLUMN_SYNC_TIMER));
	        	job.setItemName(resultSet.getString(DatabaseConstants.COLUMN_ITEM_NAME));
	        	job.setRecordsProcessed(resultSet.getInt(DatabaseConstants.COLUMN_RECORDS_PROCESSED));
	        	if(resultSet.getString(DatabaseConstants.COLUMN_INITIAL_RECORD)!=null)
	        	{
		        	job.setInitialRecord(resultSet.getString(DatabaseConstants.COLUMN_INITIAL_RECORD));	        		
	        	}
	        	if(resultSet.getString(DatabaseConstants.COLUMN_FINAL_RECORD)!=null)
	        	{
		        	job.setFinalRecord(resultSet.getString(DatabaseConstants.COLUMN_FINAL_RECORD));	        		
	        	}
	        	if(resultSet.getBytes(DatabaseConstants.COLUMN_INITIAL_VERSION_RECORD)!=null)
	        	{
		        	job.setVersionInitialRecord(resultSet.getBytes(DatabaseConstants.COLUMN_INITIAL_VERSION_RECORD));	        		
	        	}
	        	if(resultSet.getBytes(DatabaseConstants.COLUMN_FINAL_VERSION_RECORD)!=null)
	        	{
		        	job.setVersionFinalRecord(resultSet.getBytes(DatabaseConstants.COLUMN_FINAL_VERSION_RECORD));	        		
	        	}
	        	job.setProcessingStatus(resultSet.getBoolean(DatabaseConstants.COLUMN_PROCESSING_STATUS));
	        	if(resultSet.getString(DatabaseConstants.COLUMN_PROCESSING_MESSAGE)!=null)
	        	{
		        	job.setProcessingMessage(resultSet.getString(DatabaseConstants.COLUMN_PROCESSING_MESSAGE));	        		
	        	}
	        	if(resultSet.getTimestamp(DatabaseConstants.COLUMN_PROCESSING_DATA)!=null)
	        	{
		        	job.setProcessingData(resultSet.getTimestamp(DatabaseConstants.COLUMN_PROCESSING_DATA).toLocalDateTime());	        		
	        	}
	        	job.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
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
	}

	@Override
	public List<JobDTO> getAll(final Connection connection) throws Exception 
	{
		final List<JobDTO> itens = new ArrayList<JobDTO>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_job");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _id INT,
		query.append("?, "); //03 - IN _parent_id INT
		query.append("?, "); //04 - IN _description VARCHAR(20), 
		query.append("?, "); //05 - IN _sync_timer VARCHAR(20), 
		query.append("?, "); //06 - IN _records_processed INT, 
		query.append("?, "); //07 - IN _initial_record VARCHAR(20), 
		query.append("?, "); //08 - IN _final_record VARCHAR(20), 
		query.append("?, "); //09 - IN _initial_version_record TIMESTAMP, 
		query.append("?, "); //10 - IN _final_version_record TIMESTAMP, 
		query.append("?, "); //11 - IN _processing_status BIT, 
		query.append("?, "); //12 - IN _processing_message VARCHAR(500), 
		query.append("?, "); //13 - IN _processing_data DATETIME, 
		query.append("?, "); //14 - IN _inactive int, 
		query.append("?  "); //15 - IN _user_change BIGINT		
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
				
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, 4); //01 - IN _operation INT,	        
			statement.setNull(2, Types.INTEGER); //02 - IN _id INT,
			statement.setNull(3, Types.INTEGER); //03 - IN _parent_id INT
			statement.setNull(4, Types.VARCHAR); //04 - IN _description VARCHAR(20), 
			statement.setNull(5, Types.VARCHAR); //05 - IN _sync_timer VARCHAR(20), 
			statement.setNull(6, Types.INTEGER); //06 - IN _records_processed INT, 
			statement.setNull(7, Types.VARCHAR); //07 - IN _initial_record VARCHAR(20), 
			statement.setNull(8, Types.VARCHAR); //08 - IN _final_record VARCHAR(20), 
			statement.setNull(9, Types.BINARY); //09 - IN _initial_version_record BINARY(20), 
			statement.setNull(10, Types.BINARY); //10 - IN _final_version_record BINARY(20), 
			statement.setNull(11, Types.BIT); //11 - IN _processing_status BIT, 
			statement.setNull(12, Types.VARCHAR); //12 - IN _processing_message VARCHAR(500), 
			statement.setNull(13, Types.DATE); //13 - IN _processing_data DATETIME, 
			statement.setBoolean(14, false);  //14 - IN _inactive int, 
			statement.setNull(15, Types.BIGINT);  //15 - IN _user_change BIGINT	
			
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final JobDTO job = new JobDTO();
	        	job.setId(resultSet.getInt(DatabaseConstants.COLUMN_ID));
	        	job.setParentId(resultSet.getInt(DatabaseConstants.COLUMN_PARENT_ID));
	        	job.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
	        	job.setItemName(resultSet.getString(DatabaseConstants.COLUMN_ITEM_NAME));
	        	job.setSyncTimer(resultSet.getString(DatabaseConstants.COLUMN_SYNC_TIMER));
	        	job.setRecordsProcessed(resultSet.getInt(DatabaseConstants.COLUMN_RECORDS_PROCESSED));
	        	if(resultSet.getString(DatabaseConstants.COLUMN_INITIAL_RECORD)!=null)
	        	{
		        	job.setInitialRecord(resultSet.getString(DatabaseConstants.COLUMN_INITIAL_RECORD));	        		
	        	}
	        	if(resultSet.getString(DatabaseConstants.COLUMN_FINAL_RECORD)!=null)
	        	{
		        	job.setFinalRecord(resultSet.getString(DatabaseConstants.COLUMN_FINAL_RECORD));	        		
	        	}
	        	if(resultSet.getBytes(DatabaseConstants.COLUMN_INITIAL_VERSION_RECORD)!=null)
	        	{
		        	job.setVersionInitialRecord(resultSet.getBytes(DatabaseConstants.COLUMN_INITIAL_VERSION_RECORD));	        		
	        	}
	        	if(resultSet.getBytes(DatabaseConstants.COLUMN_FINAL_VERSION_RECORD)!=null)
	        	{
		        	job.setVersionFinalRecord(resultSet.getBytes(DatabaseConstants.COLUMN_FINAL_VERSION_RECORD));	        		
	        	}
	        	job.setProcessingStatus(resultSet.getBoolean(DatabaseConstants.COLUMN_PROCESSING_STATUS));
	        	if(resultSet.getString(DatabaseConstants.COLUMN_PROCESSING_MESSAGE)!=null)
	        	{
		        	job.setProcessingMessage(resultSet.getString(DatabaseConstants.COLUMN_PROCESSING_MESSAGE));	        		
	        	}
	        	if(resultSet.getTimestamp(DatabaseConstants.COLUMN_PROCESSING_DATA)!=null)
	        	{
		        	job.setProcessingData(resultSet.getTimestamp(DatabaseConstants.COLUMN_PROCESSING_DATA).toLocalDateTime());	        		
	        	}
	        	job.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	itens.add(job);
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
		return itens;
	}
}