package com.manager.systems.web.jobs.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;

import com.manager.systems.web.jobs.dao.MovementDataProductSincDao;
import com.manager.systems.web.jobs.dto.MovementDataProductSincDTO;
import com.manager.systems.web.jobs.dto.MovementDataProductSincItemDTO;
import com.manager.systems.web.jobs.utils.DatabaseConstants;
import com.manager.systems.web.jobs.vo.OperationType;

public class MovementDataProductSincDaoImpl implements MovementDataProductSincDao 
{
	@Override
	public void getProductMovementsPendingSinc(final MovementDataProductSincDTO movement, final Connection connection) throws Exception
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_sinc_data_product_movement_database");
		query.append("(");
		query.append("?,"); //01 - @operation INT
		query.append("?,"); //02 - @movement_legacy_id BIGINT
		query.append("?,"); //03 - @sinc
		query.append("?");  //04 - @register_version timestamp
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, movement.getOperationType().getType());
			statement.setNull(2, Types.BIGINT);
			statement.setNull(3, Types.VARCHAR);
			statement.setNull(4, Types.BINARY);			
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final MovementDataProductSincItemDTO  item = new MovementDataProductSincItemDTO();
	          	item.setMovementLegacyId(resultSet.getLong(DatabaseConstants.COLUMN_MOVEMENT_ID));
	          	item.setProductId(resultSet.getLong(DatabaseConstants.COLUMN_PRODUCT_ID));
	          	item.setCompanyDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION));
	          	item.setCreditInFinal(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_IN));
	          	item.setCreditOutFinal(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_OUT));
	          	item.setReadingDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_READING_DATE).toLocalDateTime());
	          	item.setInitialDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_INITIAL_DATE).toLocalDateTime());
	          	item.setMovementType(resultSet.getString(DatabaseConstants.COLUMN_MOVEMENT_TYPE));
	          	item.setProcessing(resultSet.getBoolean(DatabaseConstants.COLUMN_PROCESSING));
	          	item.setVersionRecord(resultSet.getBytes(DatabaseConstants.COLUMN_VERSION_REGISTER));
	          	item.setTerminalType(resultSet.getString(DatabaseConstants.COLUMN_TERMINAL_TYPE));
	          	item.setCompanyId(resultSet.getInt(DatabaseConstants.COLUMN_COMPANY_ID));
	          	item.setCompanyName(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_NAME));
	          	item.setRegisterEnabled(resultSet.getBoolean(DatabaseConstants.COLUMN_REGISTER_ENABLED));
	          	movement.addItem(item);
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
	public boolean changeSincStateProductMovementsPendingSinc(final MovementDataProductSincItemDTO item, final Connection connection) throws Exception {
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_sinc_data_product_movement_database");
		query.append("(");
		query.append("?,"); //01 - @operation INT
		query.append("?,"); //02 - @movement_legacy_id BIGINT
		query.append("?,"); //03 - @sinc
		query.append("?");  //04 - @register_version timestamp
		query.append(")");
		query.append("}");
		
		boolean result = false;
		
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.CHANGE_STATUS.getType());
			statement.setLong(2, item.getMovementLegacyId());
			statement.setBoolean(3, true);
			statement.setBytes(4, item.getVersionRecord());			
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
	public boolean saveProductMovementPendingSinc(final MovementDataProductSincItemDTO item, Connection connection) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_process_product_movement");
		query.append("(");
		query.append("?,"); //01 - @operation INT
		query.append("?,"); //02 - IN _movement_legacy_id BIGINT
		query.append("?,"); //03 - IN _legacy_system_id INT
		query.append("?,"); //04 - IN _product_id BIGINT
		query.append("?,"); //05 - IN _company_description VARCHAR(10)
		query.append("?,"); //06 - IN _credit_in_final BIGINT
		query.append("?,"); //07 - IN _credit_out_final BIGINT
		query.append("?,"); //08 - IN _reading_date DATETIME
		query.append("?,"); //09 - IN _movement_type VARCHAR(1)
		query.append("?,"); //10 - IN _inactive BIT
		query.append("?,"); //11 - IN _change_user BIGINT
		query.append("?,"); //12 - IN _credit_in_initial BIGINT, 
		query.append("?,"); //13 - IN _credit_out_initial BIGINT,
		query.append("?,"); //14 - IN _credit_clock_initial BIGINT, 
		query.append("?,"); //15 - IN _credit_clock_final BIGINT,
		query.append("?,"); //16 - IN _company_id BIGINT,
		query.append("?,"); //17 - IN _is_offline BIT,
		query.append("?,"); //18 - IN _initial_date DATETIME,
		query.append("?,"); //19 - IN _product_description VARCHAR(100),
		query.append("? "); //20 - IN _user_id BIGINT
		query.append(")");
		query.append("}");
		
		boolean result = false;
		
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - @operation INT
			statement.setLong(2, item.getMovementLegacyId()); //02 - IN _movement_legacy_id BIGINT			
			statement.setInt(3, item.getLegacySystemId()); //03 - IN _legacy_system_id INT
			statement.setLong(4, item.getProductId()); //04 - IN _product_id BIGINT 
			statement.setString(5, item.getCompanyDescription()); //05 - IN _company_description VARCHAR(10) 
			statement.setLong(6, item.getCreditInFinal()); //06 - IN _credit_in_final BIGINT 
			statement.setLong(7, item.getCreditOutFinal()); //07 - IN _credit_out_final BIGINT 
			statement.setTimestamp(8, Timestamp.valueOf(item.getReadingDate())); //08 - IN _reading_date DATETIME 
			statement.setString(9, item.getMovementType()); //09 - IN _movement_type VARCHAR(1)
			statement.setBoolean(10, item.isInactive()); //10 - IN _inactive BIT 
			statement.setLong(11, item.getChangeUser()); //11 - IN _change_user BIGINT
			statement.setNull(12, Types.BIGINT); //12 - IN _credit_in_initial BIGINT
			statement.setNull(13, Types.BIGINT); //13 - IN _credit_out_initial BIGINT
			statement.setNull(14, Types.BIGINT); //14 - IN _credit_clock_initial BIGINT
			statement.setNull(15, Types.BIGINT); //15 - IN _credit_clock_final BIGINT
			statement.setInt(16, item.getCompanyId()); //16 - IN _company_id BIGINT
			statement.setNull(17, Types.BIGINT); //17 - IN _is_offline BIT
			statement.setTimestamp(18, Timestamp.valueOf(item.getInitialDate())); //18 - IN _initial_date DATETIME
			statement.setString(19,  item.getProductDescription()); //19 - IN _product_description VARCHAR(100),
			statement.setNull(20, Types.BIGINT); //20 - IN _user_id BIGINT 
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
}