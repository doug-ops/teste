/*
 * Date create 28/06/2023.
 */
package com.manager.systems.common.dao.provider.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;

import com.manager.systems.common.dao.provider.ProviderStatementDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.provider.ProviderStatemenReportDTO;
import com.manager.systems.common.dto.provider.ProviderStatementItemDTO;

public class ProviderStatementDaoImpl implements ProviderStatementDao {
	
	private Connection connection;
	
	public ProviderStatementDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public void getProviderStatement(final ProviderStatemenReportDTO report) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;
		
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_provider_statement_report");
		query.append("(");
		query.append("?, "); //01 - IN _providers_ids VARCHAR(500)
		query.append("?, "); //02 - IN _date_from DATETIME
		query.append("?, "); //03 - IN _date_to DATETIME
		query.append("?, "); //04 - IN _document_type_ids VARCHAR(500)
		query.append("?, "); //05 - IN _group_by VARCHAR(500)
		query.append("?  "); //06 - IN _user_childrens_parent VARCHAR(5000)
		query.append(")");
		query.append("}");
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setString(1, report.getProviders()); //01 - IN _providers_ids VARCHAR(500)
			statement.setTimestamp(2, Timestamp.valueOf(report.getDateFrom())); //02 - IN _date_from DATETIME,
			statement.setTimestamp(3, Timestamp.valueOf(report.getDateTo())); //03 - IN _date_to DATETIME
			statement.setString(4, report.getTypeDocument()); //04 - IN _document_type_ids VARCHAR(500)
			statement.setString(5, report.getGroupBy()); //05 - IN _group_by VARCHAR(500)
			statement.setString(6, report.getUserChildrensParent()); //06 - IN _user_childrens_parent VARCHAR(5000)
			
	        boolean hasResults = statement.execute();  
	        do
	        {
	        	if(hasResults)
	        	{
		        	resultSet = statement.getResultSet();
		        	while (resultSet.next()) 
			        {
	        			final ProviderStatementItemDTO item = new ProviderStatementItemDTO();
	        			item.setDocumentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID));
	        			item.setDocumentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_ID));
	        			item.setDocumentDate(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_DATE));
	        			item.setDocumentValue(resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
	        			item.setCompanyId(resultSet.getInt(DatabaseConstants.COLUMN_COMPANY_ID));
	        			item.setCompanyDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION));
	        			item.setDocumentType(resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_TYPE));
	        			item.setProviderId(resultSet.getLong(DatabaseConstants.COLUMN_PROVIDER_ID));
	        			item.setProviderDescription(resultSet.getString(DatabaseConstants.COLUMN_PROVIDER_DESCRIPTION));
	        			item.setGroupMovement(resultSet.getBoolean(DatabaseConstants.COLUMN_IS_GROUP_MOVEMENT));
	        			item.setTypeMovementDescription(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_TYPE_DESCRIPTION));
	        			item.setMovementDescription(resultSet.getString(DatabaseConstants.COLUMN_MOVEMENT_DESCRIPTION));
	        			item.setFinancialGroupId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_ID));
	        			item.setFinancialGroupDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_DESCRIPTION_REPORT));
	        			item.setFinancialSubGroupId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_ID));
	        			item.setFinancialSubGroupDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_DESCRIPTION_REPORT));
	        			report.setProviderId(resultSet.getLong(DatabaseConstants.COLUMN_PROVIDER_ID));
	        			report.setProviderDescription(resultSet.getString(DatabaseConstants.COLUMN_PROVIDER_DESCRIPTION));	        			
	        			report.addItem(item);
			        }
	        	}	        	
	        	hasResults = statement.getMoreResults();
	        } while (hasResults);
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
