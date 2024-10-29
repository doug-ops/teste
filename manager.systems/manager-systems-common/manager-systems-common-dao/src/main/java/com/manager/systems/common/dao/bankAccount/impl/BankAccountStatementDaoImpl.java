package com.manager.systems.common.dao.bankAccount.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;

import com.manager.systems.common.dao.bankAccount.BankAccountStatementDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.bankAccount.BankAccountStatemenReportDTO;
import com.manager.systems.common.dto.bankAccount.BankAccountStatementItemDTO;

public class BankAccountStatementDaoImpl implements BankAccountStatementDao {
	
	private Connection connection;
	
	public BankAccountStatementDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public void getBankAccountStatement(final BankAccountStatemenReportDTO report) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;
		
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_bank_account_statement_report");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _bank_account_ids VARCHAR(500),
		query.append("?, "); //03 - IN person_ids  VARCHAR(500)
		query.append("?, "); //04 - IN _date_from DATETIME,
		query.append("?, "); //05 - IN _date_to DATETIME
		query.append("?  "); //06 - IN _financial_cost_center_id INTEGER
		query.append(")");
		query.append("}");
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, report.getOperation()); //01 - IN _operation INT,
			statement.setString(2, report.getBankAccounts()); //02 - IN _bank_account_ids VARCHAR(500),
			statement.setString(3, report.getPersons()); //02 - IN person_ids VARCHAR(500),
			statement.setTimestamp(4, Timestamp.valueOf(report.getDateFrom())); //04 - IN _date_from DATETIME,
			statement.setTimestamp(5, Timestamp.valueOf(report.getDateTo())); //05 - IN _date_to DATETIME
			statement.setInt(6, report.getFinancialCostCenter()); // 06 - IN _financial_cost_center_id INTEGER
			
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
		        			report.setInitialBankAccountAvaliable(resultSet.getDouble(DatabaseConstants.COLUMN_BANK_ACCOUNT_AVAILABLE_INITIAL));
		        			report.setFinalBankAccountAvaliable(resultSet.getDouble(DatabaseConstants.COLUMN_BANK_ACCOUNT_AVAILABLE_INITIAL));
		        		}
		        		else
		        		{
		        			if(report.getOperation()==1 || report.getOperation()==2) {
			        			final BankAccountStatementItemDTO item = new BankAccountStatementItemDTO();
			        			item.setDocumentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID));
			        			item.setDocumentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_ID));
			        			item.setDocumentDate(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_DATE));
			        			item.setBankAccountId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID));
			        			item.setBanAccountDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESCRIPTION));
			        			item.setDocumentNote(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE));			        			
			        			item.setDocumentValue(resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
			        			item.setCompanyId(resultSet.getInt(DatabaseConstants.COLUMN_COMPANY_ID));
			        			item.setCompanyDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION));
			        			item.setBankAccountOriginId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ORIGIN_ID));
			        			item.setBankAccountOriginDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_ORIGIN_DESCRIPTION));
			        			item.setDocumentType(resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_TYPE));
			        			item.setProviderId(resultSet.getLong(DatabaseConstants.COLUMN_PROVIDER_ID));
			        			item.setProviderDescription(resultSet.getString(DatabaseConstants.COLUMN_PROVIDER_DESCRIPTION));
			        			item.setGroupMovement(resultSet.getBoolean(DatabaseConstants.COLUMN_IS_GROUP_MOVEMENT));
			        			item.setTypeMovementDescription(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_TYPE_DESCRIPTION));
			        			item.setMovementDescription(resultSet.getString(DatabaseConstants.COLUMN_MOVEMENT_DESCRIPTION));
			        			report.setCompanyId(resultSet.getInt(DatabaseConstants.COLUMN_COMPANY_ID));
			        			report.setCompanyDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION));
			        			item.populateDataDocumentNote();
			        			
			        			report.addItem(item);
		        			}
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
	}
	
	@Override
	public void getBankAccountStatementFilter(final BankAccountStatemenReportDTO report) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;
		
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_bank_account_statement_report");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _bank_account_ids VARCHAR(500),
		query.append("?, "); //03 - IN person_ids  VARCHAR(500)
		query.append("?, "); //04 - IN _date_from DATETIME,
		query.append("?, "); //05 - IN _date_to DATETIME
		query.append("?  "); //06 - IN _financial_cost_center_id INTEGER
		query.append(")");
		query.append("}");
		
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, report.getOperation()); //01 - IN _operation INT,
			statement.setNull(2, Types.VARCHAR); //02 - IN _bank_account_ids VARCHAR(500),
			statement.setNull(3, Types.VARCHAR); //02 - IN person_ids VARCHAR(500),
			statement.setTimestamp(4, Timestamp.valueOf(report.getDateFrom())); //03 - IN _date_from DATETIME,
			statement.setTimestamp(5, Timestamp.valueOf(report.getDateTo())); //04 - IN _date_to DATETIME
			statement.setInt(6, report.getFinancialCostCenter()); // 06 - IN _financial_cost_center_id INTEGER
			
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				final long userId = resultSet.getLong(DatabaseConstants.COLUMN_USER_ID);
        		final long bankAccountId = resultSet.getLong(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID);
        		final long companyId = resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID);
    			final String email = resultSet.getString(DatabaseConstants.COLUMN_SEND_EMAIL);
    			final long personId = resultSet.getLong(DatabaseConstants.COLUMN_PERSON_ID);
        		report.addFilter(userId, bankAccountId, personId, companyId, email);
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
}
