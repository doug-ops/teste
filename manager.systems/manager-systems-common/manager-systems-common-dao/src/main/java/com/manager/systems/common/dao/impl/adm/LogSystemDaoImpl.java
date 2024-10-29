/**Create 01/08/2022*/
package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.adm.LogSystemDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.ChangeObjectDTO;
import com.manager.systems.common.dto.adm.LogSystemDTO;
import com.manager.systems.common.dto.adm.ReportLogSystemDTO;
import com.manager.systems.common.vo.OperationType;

public class LogSystemDaoImpl implements LogSystemDao 
{
	private Connection connection;
	
	public LogSystemDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}
	
	@Override
	public List<LogSystemDTO> get(final ReportLogSystemDTO reportLog) throws Exception 
	{
		final List<LogSystemDTO> items = new ArrayList<>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_log_system");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _date_from DATETIME,
		query.append("?, "); //03 - IN _date_to DATETIME,
		query.append("?, "); //04 - IN _system_id INT,
		query.append("?, "); //05 - IN _object_id BIGINT,
		query.append("?, "); //06 -  IN _count INT
		query.append("?, "); //07 -  IN _id INT,
		query.append("? "); //08 -  IN _changeDate VARCHAR(50)
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		try
		{			
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); //01 - IN _operation INT,
			statement.setNull(2, Types.TIMESTAMP); //02 - IN _date_from DATETIME,
			statement.setNull(3, Types.TIMESTAMP); //03 - IN _date_to DATETIME,
			statement.setInt(4, reportLog.getSystemId()); //04 - IN _system_id INT,
			statement.setLong(5, reportLog.getObjectId()); //05 - IN _object_id BIGINT,
			statement.setInt(6, reportLog.getCountLogSystem());//06 - IN _count INT
			statement.setNull(7, Types.INTEGER); //07 -  IN _id INT,
			statement.setNull(8, Types.VARCHAR); //08 -  IN _changeDate VARCHAR(50)
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
				final LogSystemDTO logSystem = new LogSystemDTO();
				logSystem.setUserChange(resultSet.getInt(DatabaseConstants.COLUMN_USER_CHANGE));
				logSystem.setUserChangeName(resultSet.getString(DatabaseConstants.COLUMN_USER_CHANGE_NAME));
				logSystem.setChangeDateString(resultSet.getString(DatabaseConstants.COLUMN_CHANGE_DATE));
				logSystem.setDescriptionSystem(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
				items.add(logSystem);				
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
		return items;
	}	
	
	@Override
	public List<ChangeObjectDTO> getAll(final ReportLogSystemDTO reportLog) throws Exception 
	{
		final List<ChangeObjectDTO> items = new ArrayList<ChangeObjectDTO>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_log_system");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _date_from DATETIME,
		query.append("?, "); //03 - IN _date_to DATETIME,
		query.append("?, "); //04 - IN _system_id INT,
		query.append("?, "); //05 - IN _object_id BIGINT,
		query.append("?, "); //06 -  IN _count INT
		query.append("?, "); //07 -  IN _id INT,
		query.append("? "); //08 -  IN _changeDate VARCHAR(50)
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		try
		{			
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - IN _operation INT,
			if(reportLog.getDateFrom() != null) {
				statement.setTimestamp(2, Timestamp.valueOf(reportLog.getDateFrom())); //02 - IN _date_from DATETIME,
			}else {
				statement.setNull(2, Types.TIMESTAMP);//02 - IN _date_from DATETIME,
			}
			if(reportLog.getDateTo() != null) {
				statement.setTimestamp(3, Timestamp.valueOf(reportLog.getDateTo())); //03 - IN _date_to DATETIME,
			}else {
				statement.setNull(3, Types.TIMESTAMP);//03 - IN _date_to DATETIME,
			}
			statement.setInt(4, reportLog.getSystemId()); //04 - IN _system_id INT,
			statement.setLong(5, reportLog.getObjectId()); //05 - IN _object_id BIGINT,
			statement.setNull(6, Types.INTEGER);//06 - IN _count INT,
			if(reportLog.getId() !=0) {
				statement.setInt(7, reportLog.getId()); //07 - IN _id INT
			}else {
				statement.setNull(7, Types.INTEGER);//07 - IN _id INT
			}
			if(reportLog.getChangeDate() != null) {
				statement.setString(8, reportLog.getChangeDate()); //08 - IN _changeDate VARCHAR(50)
			}else {
				statement.setNull(8, Types.VARCHAR);//08 - IN _changeDate VARCHAR(50)
			}
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
				final ChangeObjectDTO changeObject = new ChangeObjectDTO();
				changeObject.setId(resultSet.getInt(DatabaseConstants.COLUMN_ID));
				changeObject.setChangeUser(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
				changeObject.setChangeUserName(resultSet.getString(DatabaseConstants.COLUMN_USER_CHANGE_NAME));
				changeObject.setChangeDate(resultSet.getString(DatabaseConstants.COLUMN_CHANGE_DATE_STRING));
				changeObject.setChangeObjectName(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
				
				String jsonOld = resultSet.getString(DatabaseConstants.COLUMN_JSON_DATA_BEFORE);
				String jsonNew = resultSet.getString(DatabaseConstants.COLUMN_JSON_DATA_AFTER);
				changeObject.convertJsontOdMap(jsonOld, jsonNew);
				items.add(changeObject);
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
		return items;
	}	
}