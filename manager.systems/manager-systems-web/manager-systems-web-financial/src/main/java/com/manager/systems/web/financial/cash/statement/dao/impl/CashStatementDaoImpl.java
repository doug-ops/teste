/*
 * Date create 28/06/2023.
 */
package com.manager.systems.web.financial.cash.statement.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;

import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.web.financial.cash.statement.dao.CashStatementDao;
import com.manager.systems.web.financial.cash.statement.dto.CashStatemenReportDTO;
import com.manager.systems.web.financial.cash.statement.dto.CashStatemenReportFilterDTO;
import com.manager.systems.web.financial.cash.statement.dto.CashStatementItemDTO;

public class CashStatementDaoImpl implements CashStatementDao {
	
	private Connection connection;
	
	public CashStatementDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public CashStatemenReportDTO getCashStatement(final CashStatemenReportFilterDTO filter) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;
		
		final CashStatemenReportDTO report = CashStatemenReportDTO.builder().build();
		
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_cash_statement_report");
		query.append("(");
		query.append("?, "); //01 - IN _operation TINYINT
		query.append("?, "); //02 - IN _company_id BIGINT
		query.append("?, "); //03 - IN _date_from DATETIME
		query.append("?, "); //04 - IN _date_to DATETIME
		query.append("?  "); //05 - IN _user_childrens_parent VARCHAR(5000)
		query.append(")");
		query.append("}");
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, 1); //01 - IN _operation TINYINT
			statement.setLong(2, filter.getCompanyId()); //02 - IN _company_id BIGINT
			statement.setTimestamp(3, Timestamp.valueOf(filter.getDateFrom())); //03 - IN _date_from DATETIME
			statement.setTimestamp(4, Timestamp.valueOf(filter.getDateTo())); //04 - IN _date_to DATETIME
			statement.setString(5, filter.getUsersChildrenParent()); //05 - IN _user_childrens_parent VARCHAR(5000)
			
			int count = 0;
	        boolean hasResults = statement.execute();  
	        do
	        {
	        	if(hasResults)
	        	{
		        	resultSet = statement.getResultSet();
		        	while (resultSet.next()) 
			        {
		        		if(0==count)
		        		{
		        			//report.addCompany(CashFlowFinancialGroupingCompanyDTO.builder()
				        	//		.companyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID))
							//		.companyDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION))
							//		.build());
		        		} else if(1==count)
		        		{
		        			//report.addUser(CashFlowFinancialGroupingUserDTO.builder()
				        	//		.userId(resultSet.getLong(DatabaseConstants.COLUMN_USER_ID))
							//		.userName(resultSet.getString(DatabaseConstants.COLUMN_USER_NAME))
							//		.build());
		        		}
		        		else
		        		{
		        			final CashStatementItemDTO item = CashStatementItemDTO.builder()
									.companyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID))
									.companyDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION))
									.paymentDate(resultSet.getString(DatabaseConstants.COLUMN_PAYMENT_DATA))
									.paymentDateLong(resultSet.getLong(DatabaseConstants.COLUMN_PAYMENT_DATA_LONG))
									.dateFrom(resultSet.getString(DatabaseConstants.COLUMN_DATE_FROM))
									.dateTo(resultSet.getString(DatabaseConstants.COLUMN_DATE_TO))
									.weekYear(resultSet.getInt(DatabaseConstants.COLUMN_WEEK_YEAR))
									.providerId(resultSet.getLong(DatabaseConstants.COLUMN_PROVIDER_ID))
									.providerDescription(resultSet.getString(DatabaseConstants.COLUMN_PROVIDER_DESCRIPTION))
									.financialGroupId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_ID))
									.financialGroupDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_DESCRIPTION_REPORT))
									.financialSubGroupId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_ID))
									.financialSubGroupDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_DESCRIPTION_REPORT))
									.bankAccountId(resultSet.getLong(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID))
									.bankAccountDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESCRIPTION))
									.bankAccountOriginId(resultSet.getLong(DatabaseConstants.COLUMN_BANK_ACCOUNT_ORIGIN_ID))
									.bankAccountOriginDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_ORIGIN_DESCRIPTION))
									.transactionSameCompany(resultSet.getBoolean(DatabaseConstants.COLUMN_TRANSACTION_SAME_COMPANY))
									.profitDistribution(resultSet.getBoolean(DatabaseConstants.COLUMN_PROFIT_DISTRIBUTION))
									.documentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID))								
									.movementTypeId(resultSet.getInt(DatabaseConstants.COLUMN_MOVEMENT_TYPE_ID))
									.groupTypeId(resultSet.getInt(DatabaseConstants.COLUMN_GROUP_TYPE_ID))
									.documentValue(resultSet.getDouble(DatabaseConstants.COLUMN_TOTAL))
									.documentType(resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_TYPE))
									.documentNote(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_DESCRIPTION))
		        					.build();	        			        		
		        			report.addItem(item);
		        		}
			        }
	        	}	        	
	        	hasResults = statement.getMoreResults();
	        	count++;
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
		
		return report;
	}
}
