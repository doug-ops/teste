/*
 * Date create 30/03/2021.
 */
package com.manager.systems.web.jobs.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.web.jobs.dao.SendEmailDocumentDao;
import com.manager.systems.web.jobs.dto.SendEmailDocumentDTO;
import com.manager.systems.web.jobs.utils.DatabaseConstants;

public class SendEmailDocumentDaoImpl implements SendEmailDocumentDao {
	
	private Connection connection;
	
	public SendEmailDocumentDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public List<SendEmailDocumentDTO> getDocumentsToSendEmail() throws Exception{
		
		final List<SendEmailDocumentDTO> items = new ArrayList<>();
		
		CallableStatement statement = null;
		
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_movement_report_email");
		query.append("(");
		query.append("?,"); //01 - IN _operation INT
		query.append("?,"); //02 - IN _document_parent_id BIGINT
		query.append("?");  //03 - IN _company_id BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 2);
			statement.setLong(2, 0L);
			statement.setLong(3, 0L);
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final SendEmailDocumentDTO item = new SendEmailDocumentDTO();
	        	item.setId(resultSet.getLong(DatabaseConstants.COLUMN_ID));
	        	item.setEmail(resultSet.getString(DatabaseConstants.COLUMN_EMAIL));
	        	item.setDocumentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID));
	        	item.setCompanyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID));
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
	public boolean updateEmailStatusSend(final long documentParentId, final long companyId) throws Exception {
		
		CallableStatement statement = null;
		
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_movement_report_email");
		query.append("(");
		query.append("?,"); //01 - IN _operation INT
		query.append("?,"); //02 - IN _document_parent_id BIGINT
		query.append("?");  //03 - IN _company_id BIGINT
		query.append(")");
		query.append("}");
		
		boolean result = false;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 3);
			statement.setLong(2, documentParentId);
			statement.setLong(3, companyId);
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