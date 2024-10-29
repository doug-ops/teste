/*
 * Date create 24/08/2023.
 */
package com.manager.systems.web.financial.incomeExpenses.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;

import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.web.financial.incomeExpenses.dao.FinancialIncomeExpensesDao;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesFilterDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesItemDetailDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesReportDTO;

public class FinancialIncomeExpensesDaoImpl implements FinancialIncomeExpensesDao {
	
	private Connection connection;

	public FinancialIncomeExpensesDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}
	
	@Override
	public FinancialIncomeExpensesReportDTO getFinancialMovements(final FinancialIncomeExpensesFilterDTO filter) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;

		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_financial_incomes_expenses_report");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT
		query.append("?, "); // 02 - IN _company_id BIGINT
		query.append("?, "); // 03 - IN _date_from DATETIME
		query.append("?, "); // 04 - IN _date_to DATETIME
		query.append("?, "); // 05 - IN _users_children_parent VARCHAR(5000)
		query.append("?  "); // 06 - IN _financial_cost_center_id INTEGER
		query.append(")");
		query.append("}");
		
		final FinancialIncomeExpensesReportDTO report = FinancialIncomeExpensesReportDTO.builder().build();
		report.setAnalitc(filter.isAnalitc());
		report.setGroupBy(filter.getGroupBy());
		report.setExpenseHaab(filter.isExpenseHaab());
		report.setTransfCashingClose(filter.isTransfCashingClose());
		report.setIncomeExpense(filter.getIncomeExpense());
		if(filter.getBankAccounts() != null) {
			report.setBankAccounts(filter.getBankAccounts());			
		}

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, filter.getOperation()); // 01 - IN _operation INT
			statement.setString(2, filter.getCompanysId()); // 02 - IN _company_id BIGINT
			statement.setTimestamp(3, Timestamp.valueOf(filter.getDateFrom())); // 04 - IN _date_from DATETIME
			statement.setTimestamp(4, Timestamp.valueOf(filter.getDateTo())); // 05 - IN _date_to DATETIME
			statement.setString(5, filter.getUsersChildrenParent()); // 05 - IN _users_children_parent VARCHAR(5000)
			statement.setInt(6, filter.getFinancialCostCenter()); // 06 - IN _financial_cost_center_id INTEGER
			
			final long dateFromLong = filter.getDateFromLong();
			final long dateToLong = filter.getDateToLong();
			
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
		        			final FinancialIncomeExpensesItemDetailDTO detail = FinancialIncomeExpensesItemDetailDTO.builder()
								.weekYear(resultSet.getInt(DatabaseConstants.COLUMN_WEEK_YEAR))
								.dateFrom(resultSet.getString(DatabaseConstants.COLUMN_DATE_FROM))
								.dateTo(resultSet.getString(DatabaseConstants.COLUMN_DATE_TO))
								.movementTypeId(resultSet.getInt(DatabaseConstants.COLUMN_MOVEMENT_TYPE_ID))
								.movementDescription(resultSet.getString(DatabaseConstants.COLUMN_MOVEMENT_DESCRIPTION))
								.companyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID))
								.companyDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION))
								.documentValue(resultSet.getDouble(DatabaseConstants.COLUMN_TOTAL))
								.build();
		        			report.addCompany(detail);
		        		}
		        		else
		        		{
		        			final FinancialIncomeExpensesItemDetailDTO detail = FinancialIncomeExpensesItemDetailDTO.builder()
									.weekYear(resultSet.getInt(DatabaseConstants.COLUMN_WEEK_YEAR))
									.dateFrom(resultSet.getString(DatabaseConstants.COLUMN_DATE_FROM))
									.dateTo(resultSet.getString(DatabaseConstants.COLUMN_DATE_TO))
									.movementTypeId(resultSet.getInt(DatabaseConstants.COLUMN_MOVEMENT_TYPE_ID))
									.paymentDate(resultSet.getString(DatabaseConstants.COLUMN_PAYMENT_DATA))
									.paymentDateLong(resultSet.getLong(DatabaseConstants.COLUMN_PAYMENT_DATA_LONG))
									.documentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID))
									.documentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_ID))
									.documentValue(resultSet.getDouble(DatabaseConstants.COLUMN_TOTAL))
									.documentType(resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_TYPE))
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
									.profitDistribution(resultSet.getBoolean(DatabaseConstants.COLUMN_PROFIT_DISTRIBUTION))
									.documentNote(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE))
									.documentDescription(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_DESCRIPTION))
							.build();
		        			
        					if((detail.getPaymentDateLong() < dateFromLong) || (detail.getPaymentDateLong() > dateToLong)) {
        						continue;
        					}
        					
	        				if(report.getGroupBy() == 11) {
	        					
	        					//final List<Long> providers = new ArrayList<Long>(Arrays.asList(0L, 3722L, 284L, 3433L, 287L, 3543L, 3404L, 3837L, 3838L, 363L));
	        					
	        					//final List<Long> accounts = new ArrayList<Long>(Arrays.asList(199L, 605L, 441L, 825L, 1697L, 1211L, 445L, 446L));

	        					if(!(detail.getCompanyId() == 182 || detail.getCompanyId() == 1)) {
	        						continue;
	        					}      					
	        					
	        					//if(!accounts.contains(detail.getBankAccountId())){
	        					//	continue;
	        					//}
	        					
	        					//if(!accounts.contains(detail.getBankAccountOriginId())){
	        					//	continue;
	        					//}
	        					
	        					//if(!providers.contains(detail.getProviderId())){
	        					//	continue;
	        					//}
	        				}
	        				
	        				if(report.getBankAccounts() != null) {
	        					if(detail.getProviderId() != 2379 && !report.getBankAccounts().contains(detail.getBankAccountId())){
	        						continue;
	        					}
	        				}
	        			
		        			if(!report.isExpenseHaab() && detail.getProviderId() == 2379 && detail.getDocumentType() == 4) {
	        					continue;
	        				}
	        			
	        				if(!report.isTransfCashingClose() && (detail.getDocumentType() == 2 || detail.getDocumentType() == 3) && detail.getMovementTypeId() == 2 && 
	        						(detail.getDocumentNote().indexOf(ConstantDataManager.LABEL_TRANSF_FEC_CAIXA_DESTINY) > -1 ||
	        						 detail.getDocumentNote().indexOf(ConstantDataManager.LABEL_RESIDUAL_TRANSF_FEC_CAIXA_DESTINY) > -1)) {
	        					continue;
	        				}
					
	        				report.addItemDetail(detail);
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
