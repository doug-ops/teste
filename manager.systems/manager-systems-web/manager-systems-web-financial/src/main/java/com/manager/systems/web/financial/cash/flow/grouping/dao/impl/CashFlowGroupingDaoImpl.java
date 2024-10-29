/*
 * Date create 02/01/2024
 */
package com.manager.systems.web.financial.cash.flow.grouping.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;

import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.web.financial.cash.flow.grouping.dao.CashFlowGroupingDao;
import com.manager.systems.web.financial.cash.flow.grouping.dto.CashFlowFinancialGroupingCompanyDTO;
import com.manager.systems.web.financial.cash.flow.grouping.dto.CashFlowFinancialGroupingFilterDTO;
import com.manager.systems.web.financial.cash.flow.grouping.dto.CashFlowFinancialGroupingReportDTO;
import com.manager.systems.web.financial.cash.flow.grouping.dto.CashFlowFinancialGroupingReportItemDTO;
import com.manager.systems.web.financial.cash.flow.grouping.dto.CashFlowFinancialGroupingUserDTO;

public class CashFlowGroupingDaoImpl implements CashFlowGroupingDao {
	
	private Connection connection;
	
	public CashFlowGroupingDaoImpl(final Connection connection) {
		this.connection = connection;
	}

	@Override
	public CashFlowFinancialGroupingReportDTO getCashFlowFinancialGroupingReport(final CashFlowFinancialGroupingFilterDTO filter) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;

		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_financial_cash_flow_grouping");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT
		query.append("?, "); // 02 - IN _date_from DATETIME
		query.append("?, "); // 03 - IN _date_to DATETIME
		query.append("?, "); // 04 - IN _users_children_parent VARCHAR(5000)		
		query.append("?, "); // 05 - IN _company_id BIGINT
		query.append("?  "); // 06 - IN _bak_account_ids VARCHAR(5000)
		query.append(")");
		query.append("}");
		
		final CashFlowFinancialGroupingReportDTO report = CashFlowFinancialGroupingReportDTO.builder().build();
		report.initializaData(filter);
		
		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 1); // 01 - IN _operation INT
			statement.setTimestamp(2, Timestamp.valueOf(filter.getDateFrom())); // 02 - IN _date_from DATETIME
			statement.setTimestamp(3, Timestamp.valueOf(filter.getDateTo())); // 03 - IN _date_to DATETIME
			statement.setString(4, filter.getUsersChildrenParent()); // 04 - IN _users_children_parent VARCHAR(5000)
			statement.setLong(5, filter.getCompanyId()); // 05 - IN _company_id BIGINT
			statement.setString(6, filter.getBankAccountIds()); // 06 - IN _bak_account_ids VARCHAR(5000)
			
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
		        			report.addCompany(CashFlowFinancialGroupingCompanyDTO.builder()
				        			.companyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID))
									.companyDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION))
									.build());
		        		} else if(1==count)
		        		{
		        			report.addUser(CashFlowFinancialGroupingUserDTO.builder()
				        			.userId(resultSet.getLong(DatabaseConstants.COLUMN_USER_ID))
									.userName(resultSet.getString(DatabaseConstants.COLUMN_USER_NAME))
									.build());
		        		}
		        		else
		        		{
		        			final CashFlowFinancialGroupingReportItemDTO detail = CashFlowFinancialGroupingReportItemDTO.builder()
									//.weekYear(resultSet.getInt(DatabaseConstants.COLUMN_WEEK_YEAR))
									//.dateFrom(resultSet.getString(DatabaseConstants.COLUMN_DATE_FROM))
									//.dateTo(resultSet.getString(DatabaseConstants.COLUMN_DATE_TO))
									.movementTypeId(resultSet.getInt(DatabaseConstants.COLUMN_MOVEMENT_TYPE_ID))
									.paymentDate(resultSet.getString(DatabaseConstants.COLUMN_PAYMENT_DATA))
									.paymentDateLong(resultSet.getLong(DatabaseConstants.COLUMN_PAYMENT_DATA_LONG))
									.weekYear(resultSet.getInt(DatabaseConstants.COLUMN_WEEK_YEAR))
									.monthYear(resultSet.getInt(DatabaseConstants.COLUMN_MONTH_YEAR))
									.documentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID))
									//.documentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_ID))
									.documentValue(resultSet.getDouble(DatabaseConstants.COLUMN_TOTAL))
									//.documentType(resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_TYPE))
									.companyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID))
									.companyDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION))
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
									.documentNote(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE))
									.documentDescription(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_DESCRIPTION))
							.build();
				
		        			detail.populateKey(filter.getGroupingType());
		        			report.addItem(detail);
		        		}
			        }
	        	}	        	
	        	hasResults = statement.getMoreResults();
	        	count++;
	        } while (hasResults);
	        
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		
		return report;
	}
}