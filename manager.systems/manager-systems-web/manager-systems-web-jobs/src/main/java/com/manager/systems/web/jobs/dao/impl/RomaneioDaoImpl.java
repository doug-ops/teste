/*
 * Date create 09/02/2023.
 */
package com.manager.systems.web.jobs.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;

import com.manager.systems.web.jobs.dao.RomaneioDao;
import com.manager.systems.web.jobs.dto.RomaneioFilterDTO;

public class RomaneioDaoImpl implements RomaneioDao {
	
	private Connection connection;
	
	/*
	 * Constructor
	 */
	public RomaneioDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public void generateNextWeekMovement(final RomaneioFilterDTO filter) throws Exception {
		
		CallableStatement statement = null;
		
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_process_romaneio");
		query.append("(");
		query.append("?,"); //01 - IN _operation INT
		query.append("?,"); //02 - IN _company_id BIGINT
		query.append("?,"); //03 - IN _document_parent_id BIGINT
		query.append("?,"); //04 - IN _user_id BIGINT
		query.append("?,"); //05 - IN _document_note varchar(1000)
		query.append("? "); //06 - IN _count_week_process TINYINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, filter.getOperation());
			statement.setLong(2, filter.getCompanyId());
			statement.setLong(3, filter.getDocumentParentId());
			statement.setLong(4, filter.getUserId());
			statement.setString(5, filter.getDocumentNote());
			statement.setInt(6, filter.getCountWeekProcess());
			statement.execute();	        
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
}
