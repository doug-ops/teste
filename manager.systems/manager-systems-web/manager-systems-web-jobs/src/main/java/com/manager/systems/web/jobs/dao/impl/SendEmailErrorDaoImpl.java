/*
 * Date create 23/08/2022.
 */
package com.manager.systems.web.jobs.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.vo.OperationType;
import com.manager.systems.web.jobs.dao.SendEmailErrorDao;
import com.manager.systems.web.jobs.dto.SendEmailErrorDTO;
import com.manager.systems.web.jobs.utils.DatabaseConstants;

public class SendEmailErrorDaoImpl implements SendEmailErrorDao {
	
	private Connection connection;
	
	public SendEmailErrorDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}

	public List<SendEmailErrorDTO> getErrorSendEmail() throws Exception{
		
		final List<SendEmailErrorDTO> items = new ArrayList<>();
		
		CallableStatement statement = null;
		
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_send_email_erros");
		query.append("(");
		query.append("?, ");  //01 - IN _operation INT
		query.append("?, ");  //02 - IN _id BIGINT,
		query.append("?, ");  //03 - IN _process_description VARCHAR(500),
		query.append("?, ");  //04 - IN _error_note VARCHAR(500),
		query.append("?, ");  //05 - IN _email VARCHAR(100),
		query.append("?, ");  //06 - IN _creation_date DATETIME,
		query.append("?, ");  //07 - IN _has_send BIT,
		query.append("?, ");  //08 - IN _send_date DATETIME,
		query.append("? ");  //09 - IN _inactive BIT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType());
			statement.setNull(2, Types.BIGINT);
			statement.setNull(3, Types.VARCHAR);
			statement.setNull(4, Types.VARCHAR);
			statement.setNull(5, Types.VARCHAR);
			statement.setNull(6, Types.DATE);
			statement.setBoolean(7, false);
			statement.setNull(8, Types.DATE);
			statement.setBoolean(9, false);
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final SendEmailErrorDTO item = new SendEmailErrorDTO();
	        	item.setId(resultSet.getLong(DatabaseConstants.COLUMN_ID));
	        	item.setProcessDescription(resultSet.getString(DatabaseConstants.COLUMN_PROCESS_DESCRIPTION));
	        	item.setErrorNote(resultSet.getString(DatabaseConstants.COLUMN_ERROR_NOTE));
	        	item.setEmail(resultSet.getString(DatabaseConstants.COLUMN_EMAIL));
	        	item.setHasSend(resultSet.getBoolean(DatabaseConstants.COLUMN_HAS_SEND));
	        	final Timestamp sendData = resultSet.getTimestamp(DatabaseConstants.COLUMN_SEND_DATE);
	        	if(sendData!=null)
	        	{
		        	item.setSendDate(sendData.toLocalDateTime());	        		
	        	}
	        	items.add(item);
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
	public boolean updateEmailStatusSend(long id, LocalDateTime date, boolean hasSend) throws Exception {
		
		CallableStatement statement = null;
		
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_send_email_erros");
		query.append("(");
		query.append("?, ");  //01 - IN _operation INT
		query.append("?, ");  //02 - IN _id BIGINT,
		query.append("?, ");  //03 - IN _process_description VARCHAR(500),
		query.append("?, ");  //04 - IN _error_note VARCHAR(500),
		query.append("?, ");  //05 - IN _email VARCHAR(100),
		query.append("?, ");  //06 - IN _creation_date DATETIME,
		query.append("?, ");  //07 - IN _has_send BIT,
		query.append("?, ");  //08 - IN _send_date DATETIME,
		query.append("? ");  //09 - IN _inactive BIT
		query.append(")");
		query.append("}");
		
		boolean result = false;
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType());
			statement.setLong(2, id);
			statement.setNull(3, Types.VARCHAR);
			statement.setNull(4, Types.VARCHAR);
			statement.setNull(5, Types.VARCHAR);
			statement.setNull(6, Types.DATE);
			statement.setBoolean(7, hasSend);
			statement.setTimestamp(8, Timestamp.valueOf(date));
			statement.setBoolean(9, false);
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