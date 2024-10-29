/*
 * Date create 18/09/2023.
 */
package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.adm.CompanyDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.CompanyIntegrationSystemDTO;

public class CompanyDaoImpl implements CompanyDao {

	private Connection connection;
	
	public CompanyDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}
	
	@Override
	public List<CompanyIntegrationSystemDTO> getCompanyIntegrationSystem(final long companyId) throws Exception {
		List<CompanyIntegrationSystemDTO> result = null;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_get_company_data");
		query.append("(");
		query.append("?, "); //01 - operation
		query.append("?  "); //02 - IN _company_id BIGINT	
		query.append(")");
		query.append("}");
		
				
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 1); //01 - operation
			statement.setLong(2, companyId); ///02 - IN _company_id BIGINT

	        resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	if(result == null) {
	        		result = new ArrayList<>();
	        	}
	        	result.add(CompanyIntegrationSystemDTO.builder()
	        			.companyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID))
	        			.companyDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION))
	        			.bravoRoomId(resultSet.getString(DatabaseConstants.COLUMN_LEGACY_ID))
	        			.build());
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
		return result;
	}

}
