/*
 * Date create 18/08/2023.
 */
package com.manager.systems.web.financial.cashier.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;

import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.web.financial.cashier.dao.CashierMovementDao;
import com.manager.systems.web.financial.cashier.dto.CashierMovementReportDTO;
import com.manager.systems.web.financial.cashier.dto.CashierMovementReportFilterDTO;
import com.manager.systems.web.financial.cashier.dto.CashierMovementReportItemDetailDTO;

public class CashierMovementDaoImpl implements CashierMovementDao {

	private Connection connection;

	public CashierMovementDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}
	
	@Override
	public CashierMovementReportDTO getMovements(final CashierMovementReportFilterDTO filter) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;

		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_cashier_movement_report");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT
		query.append("?, "); // 02 - IN _company_id BIGINT
		query.append("?, "); // 03 - IN _date_from DATETIME
		query.append("?  "); // 04 - IN _date_to DATETIME
		query.append(")");
		query.append("}");
		
		final CashierMovementReportDTO report = CashierMovementReportDTO.builder().build();

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, filter.getOperation()); // 01 - IN _operation INT
			statement.setLong(2, filter.getCompanyId()); // 02 - IN _company_id BIGINT
			statement.setTimestamp(3, Timestamp.valueOf(filter.getDateFrom())); // 04 - IN _date_from DATETIME
			statement.setTimestamp(4, Timestamp.valueOf(filter.getDateTo())); // 05 - IN _date_to DATETIME
			
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
		        			final CashierMovementReportItemDetailDTO detail = CashierMovementReportItemDetailDTO.builder()
								.weekYear(resultSet.getInt(DatabaseConstants.COLUMN_WEEK_YEAR))
								.dateFrom(resultSet.getString(DatabaseConstants.COLUMN_DATE_FROM))
								.dateTo(resultSet.getString(DatabaseConstants.COLUMN_DATE_TO))
								.movementTypeId(resultSet.getInt(DatabaseConstants.COLUMN_MOVEMENT_TYPE_ID))
								.movementDescription(resultSet.getString(DatabaseConstants.COLUMN_MOVEMENT_DESCRIPTION))
								.groupType(resultSet.getInt(DatabaseConstants.COLUMN_GROUP_TYPE_ID))
								.orderItem(resultSet.getInt(DatabaseConstants.COLUMN_ORDER_ITEM))
								.companyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID))
								.companyDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION))
								.documentValue(resultSet.getDouble(DatabaseConstants.COLUMN_TOTAL))
								.build();
		        			report.addItem(detail);
		        		}
		        		else
		        		{
		        			final CashierMovementReportItemDetailDTO detail = CashierMovementReportItemDetailDTO.builder()
									.weekYear(resultSet.getInt(DatabaseConstants.COLUMN_WEEK_YEAR))
									.dateFrom(resultSet.getString(DatabaseConstants.COLUMN_DATE_FROM))
									.dateTo(resultSet.getString(DatabaseConstants.COLUMN_DATE_TO))
									.movementTypeId(resultSet.getInt(DatabaseConstants.COLUMN_MOVEMENT_TYPE_ID))
										//.movementDescription(resultSet.getString(DatabaseConstants.COLUMN_MOVEMENT_DESCRIPTION))
									.groupType(resultSet.getInt(DatabaseConstants.COLUMN_GROUP_TYPE_ID))
										//.orderItem(resultSet.getInt(DatabaseConstants.COLUMN_ORDER_ITEM))
										//.creationDate(resultSet.getString(DatabaseConstants.COLUMN_CREATION_DATE))
									.paymentDate(resultSet.getString(DatabaseConstants.COLUMN_PAYMENT_DATA))
									.documentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID))
									.documentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_ID))
									.documentValue(resultSet.getDouble(DatabaseConstants.COLUMN_TOTAL))
									.documentType(resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_TYPE))
									.companyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID))
										//.companyDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION))
									.providerId(resultSet.getLong(DatabaseConstants.COLUMN_PROVIDER_ID))
										//.providerDescription(resultSet.getString(DatabaseConstants.COLUMN_PROVIDER_DESCRIPTION))
									.bankAccountId(resultSet.getLong(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID))
									.documentNote(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE))
										//.documentDescription(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_DESCRIPTION))
								.build();
						
						
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
