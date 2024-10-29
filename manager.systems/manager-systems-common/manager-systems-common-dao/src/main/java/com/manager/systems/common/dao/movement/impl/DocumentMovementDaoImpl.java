package com.manager.systems.common.dao.movement.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;

import com.manager.systems.common.dao.movement.DocumentMovementDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.ReportDocumentMovementDTO;
import com.manager.systems.common.dto.adm.ReportDocumentMovementItemDTO;

public class DocumentMovementDaoImpl implements DocumentMovementDao 
{
	@Override
	public void getAllMovement(final ReportDocumentMovementDTO report, final Connection connection) throws Exception 
	{	
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_generate_script_data");
		query.append("(");
		query.append("?, "); //01 - @table_name varchar(50),
		query.append("?  "); //02 - @register_version timestamp
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = connection.prepareCall(query.toString());
			//statement.setString(1, job.getItemName());
			//statement.setBytes(2, job.getVersionFinalRecord());			
	        resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final ReportDocumentMovementItemDTO item = new ReportDocumentMovementItemDTO();
	        	item.setId(resultSet.getLong(DatabaseConstants.COLUMN_ID));
	        	item.setParentId(resultSet.getLong(DatabaseConstants.COLUMN_PARENT_ID));
	        	item.setCredit(resultSet.getBoolean(DatabaseConstants.COLUMN_CREDIT));
	        	item.setCompanyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID));
	        	item.setProviderId(resultSet.getLong(DatabaseConstants.COLUMN_PROVIDER_ID));
	        	item.setBankAccountId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID));
	        	item.setDocumentType(resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_TYPE));
	        	item.setDocumentNumber(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NUMBER));
	        	item.setDocumentValue(resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
	        	item.setDocumentNote(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE));
	        	item.setDocumentStatus(resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_STATUS));
	        	item.setPaymentValue(resultSet.getDouble(DatabaseConstants.COLUMN_PAYMENT_VALUE));
	        	item.setPaymentDiscount(resultSet.getDouble(DatabaseConstants.COLUMN_PAYMENT_DISCOUNT));
	        	item.setPaymentExtra(resultSet.getDouble(DatabaseConstants.COLUMN_PAYMENT_EXTRA));
	        	item.setPaymentResidue(resultSet.getBoolean(DatabaseConstants.COLUMN_PAYMENT_RESIDUE));
	        	final Timestamp paymentData = resultSet.getTimestamp(DatabaseConstants.COLUMN_PAYMENT_DATA);
	        	if(paymentData!=null)
	        	{
		        	item.setPaymentData(paymentData.toLocalDateTime());	        		
	        	}
	        	final Timestamp paymentExpireData = resultSet.getTimestamp(DatabaseConstants.COLUMN_PAYMENT_EXPIRY_DATA);
	        	if(paymentExpireData!=null)
	        	{
		        	item.setPaymentExpiryData(paymentExpireData.toLocalDateTime());	        		
	        	}
	        	item.setPaymentUser(resultSet.getLong(DatabaseConstants.COLUMN_PAYMENT_USER));
	           	item.setNfeNumber(resultSet.getLong(DatabaseConstants.COLUMN_NFE_NUMBER));
	          	item.setNfeSerieNumber(resultSet.getInt(DatabaseConstants.COLUMN_NFE_SERIE_NUMBER));
	          	item.setGenerateBillet(resultSet.getBoolean(DatabaseConstants.COLUMN_GENERATE_BILLET));
	          	item.setGenerateBillet(resultSet.getBoolean(DatabaseConstants.COLUMN_GENERATE_BILLET));
	          	item.setFinancialGroupId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_GROUP_ID));
	          	item.setFinancialSubGroupId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_ID));
	          	item.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	          	item.setVersionRegister(resultSet.getBytes(DatabaseConstants.COLUMN_VERSION_REGISTER));
	          	//itens.add(item);
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
		//return itens;
	}
}