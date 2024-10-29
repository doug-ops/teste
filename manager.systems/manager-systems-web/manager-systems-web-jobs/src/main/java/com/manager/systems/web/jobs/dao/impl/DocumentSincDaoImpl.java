package com.manager.systems.web.jobs.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.web.jobs.dao.DocumentSincDao;
import com.manager.systems.web.jobs.dto.DocumentSincDTO;
import com.manager.systems.web.jobs.dto.JobDTO;
import com.manager.systems.web.jobs.utils.DatabaseConstants;
import com.manager.systems.web.jobs.vo.OperationType;

public class DocumentSincDaoImpl implements DocumentSincDao 
{
	@Override
	public List<DocumentSincDTO> getAllMajorVersionRecord(final JobDTO job, final Connection connection) throws Exception 
	{
		final List<DocumentSincDTO> itens = new ArrayList<DocumentSincDTO>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_generate_script_data");
		query.append("(");
		query.append("?,"); //01 - @table_name varchar(50),
		query.append("?"); //02 - @register_version timestamp
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setString(1, job.getItemName());
			statement.setBytes(2, job.getVersionFinalRecord());			
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final DocumentSincDTO item = new DocumentSincDTO();
	        	item.setCreationDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CREATION_DATE).toLocalDateTime());
	        	item.setId(resultSet.getLong(DatabaseConstants.COLUMN_ID));
	        	item.setParentId(resultSet.getLong(DatabaseConstants.COLUMN_PARENT_ID));
	        	item.setCredit(resultSet.getBoolean(DatabaseConstants.COLUMN_CREDIT));
	        	item.setCompanyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID));
	        	item.setProviderId(resultSet.getLong(DatabaseConstants.COLUMN_PROVIDER_ID));
	        	final String bankAccountId = resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID);
	        	if(StringUtils.isLong(bankAccountId))
	        	{
		        	item.setBankAccountId(Integer.valueOf(bankAccountId));	        		
	        	}
	        	else
	        	{
	        		item.setBankAccountId(0);
	        	}
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
	          	item.setNfeSerieNumber(StringUtils.isLong(resultSet.getString(DatabaseConstants.COLUMN_NFE_SERIE_NUMBER)) ? resultSet.getInt(DatabaseConstants.COLUMN_NFE_SERIE_NUMBER) : 0);
	          	item.setGenerateBillet(resultSet.getBoolean(DatabaseConstants.COLUMN_GENERATE_BILLET));
	          	item.setGenerateBillet(resultSet.getBoolean(DatabaseConstants.COLUMN_GENERATE_BILLET));
	          	item.setFinancialGroupId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_GROUP_ID));
	          	item.setFinancialSubGroupId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_ID));
	          	item.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	          	item.setVersionRegister(resultSet.getBytes(DatabaseConstants.COLUMN_VERSION_REGISTER));
	          	itens.add(item);
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
		return itens;
	}
	
	@Override
	public boolean save(final DocumentSincDTO documentSinc, final Connection connection) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_document_movement");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _id BIGINT,
		query.append("?, "); //03 - IN _parent_id BIGINT,
		query.append("?, "); //04 - IN _document_type INT,
		query.append("?, "); //05 - IN _credit BIT,
		query.append("?, "); //06 - IN _company_id BIGINT,
		query.append("?, "); //07 - IN _provider_id BIGINT,
		query.append("?, "); //08 - IN _bank_account_id INT,
		query.append("?, "); //09 - IN _document_number VARCHAR(20),
		query.append("?, "); //10 - IN _document_value DECIMAL(19,4),
		query.append("?, "); //11 - IN _document_note VARCHAR(1000),
		query.append("?, "); //12 - IN _document_status INT,
		query.append("?, "); //13 - IN _payment_value DECIMAL(19,4),
		query.append("?, "); //14 - IN _payment_discount DECIMAL(19,4),
		query.append("?, "); //15 - IN _payment_extra  DECIMAL(19,4),
		query.append("?, "); //16 - IN _payment_residue BIT,
		query.append("?, "); //17 - IN _payment_data DATETIME,
		query.append("?, "); //18 - IN _payment_expiry_data DATETIME,
		query.append("?, "); //19 - IN _payment_user BIGINT,
		query.append("?, "); //20 - IN _nfe_number BIGINT,
		query.append("?, "); //21 - IN _nfe_serie_number INT,
		query.append("?, "); //22 - IN _generate_billet BIT,
		query.append("?, "); //23 - IN _financial_group_id INT,
		query.append("?, "); //24 - IN _financial_sub_group_id INT,
		query.append("?, "); //25 - IN _inactive BIT,
		query.append("?, "); //26 - IN _user_change BIGINT,
		query.append("?, "); //27 - IN _creation_date DATETIME,
		query.append("?, "); //28 - IN _filter_data_type INT,
		query.append("?, "); //29 - IN _moviment_type_credit BIT,
		query.append("?, "); //30 - IN _moviment_type_debit BIT,
		query.append("?, "); //31 - IN _moviment_type_nfe BIT,
		query.append("?, "); //32 - IN _document_status_filter VARCHAR(50),
		query.append("?, "); //33 - IN bank_account_origin_id INT,
		query.append("?, "); //34 - IN financial_cost_center_id INT,
		query.append("?  "); //35 - IN _totalDebitOrigin DECIMAL(19,4)
		query.append(")");
		query.append("}");
		
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
			statement.setLong(2,documentSinc.getId()); //02 - IN _id BIGINT,
			statement.setLong(3,documentSinc.getParentId()); //03 - IN _parent_id BIGINT,
			statement.setInt(4,documentSinc.getDocumentType()); //04 - IN _document_type INT,
			statement.setBoolean(5,documentSinc.isCredit()); //05 - IN _credit BIT,
			statement.setLong(6,documentSinc.getCompanyId()); //06 - IN _company_id BIGINT,
			statement.setLong(7, documentSinc.getProviderId()); //07 - IN _provider_id BIGINT,
			statement.setInt(8, documentSinc.getBankAccountId()); //08 - IN _bank_account_id INT,
			statement.setString(9, documentSinc.getDocumentNumber()); //09 - IN _document_number VARCHAR(20),
			statement.setDouble(10, documentSinc.getDocumentValue()); //10 - IN _document_value DECIMAL(19,4),
			statement.setString(11, documentSinc.getDocumentNote()); //11 - IN _document_note VARCHAR(1000),
			statement.setInt(12, documentSinc.getDocumentStatus()); //12 - IN _document_status INT,
			statement.setDouble(13 ,documentSinc.getPaymentValue()); //13 - IN _payment_value DECIMAL(19,4),
			statement.setDouble(14 ,documentSinc.getPaymentDiscount()); //14 - IN _payment_discount DECIMAL(19,4),
			statement.setDouble(15, documentSinc.getPaymentExtra()); //15 - IN _payment_extra  DECIMAL(19,4),
			statement.setBoolean(16, documentSinc.isPaymentResidue()); //16 - IN _payment_residue BIT,
			statement.setTimestamp(17, Timestamp.valueOf(documentSinc.getPaymentData())); //17 - IN _payment_data DATETIME,
			statement.setTimestamp(18, Timestamp.valueOf(documentSinc.getPaymentExpiryData())); //18 - IN _payment_expiry_data DATETIME,
			statement.setLong(19 ,documentSinc.getPaymentUser()); //19 - IN _payment_user BIGINT,
			statement.setLong(20 ,documentSinc.getNfeNumber()); //20 - IN _nfe_number BIGINT,
			statement.setInt(21 ,documentSinc.getNfeSerieNumber()); //21 - IN _nfe_serie_number INT,
			statement.setBoolean(22 ,documentSinc.isGenerateBillet()); //22 - IN _generate_billet BIT,
			statement.setInt(23 ,documentSinc.getFinancialGroupId()); //23 - IN _financial_group_id INT,
			statement.setInt(24 ,documentSinc.getFinancialSubGroupId()); //24 - IN _financial_sub_group_id INT,
			statement.setBoolean(25, documentSinc.isInactive()); //25 - IN _inactive BIT,
			statement.setLong(26, documentSinc.getUserChange()); //26 - IN _user_change BIGINT,
			statement.setTimestamp(27, Timestamp.valueOf(documentSinc.getCreationDate())); //27 - IN _creation_date DATETIME,
			statement.setNull(28,Types.INTEGER); //28 - IN _filter_data_type INT,
			statement.setNull(29,Types.BIT); //29 - IN _moviment_type_credit BIT,
			statement.setNull(30,Types.BIT); //30 - IN _moviment_type_debit BIT,
			statement.setNull(31,Types.BIT); //31 - IN _moviment_type_nfe BIT,
			statement.setNull(32,Types.VARCHAR); //32 - IN _document_status_filter VARCHAR(50),
			statement.setNull(33, Types.INTEGER); //33 - IN bank_account_origin_id	INT, 
			statement.setNull(34, Types.INTEGER); //34 - IN financial_cost_center_id INT,
			statement.setNull(35, Types.DECIMAL); //35 - IN _totalDebitOrigin DECIMAL(19,4)
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