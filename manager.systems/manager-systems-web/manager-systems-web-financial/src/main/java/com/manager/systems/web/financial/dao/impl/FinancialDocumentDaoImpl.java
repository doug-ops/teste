package com.manager.systems.web.financial.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;

import com.manager.systems.common.vo.OperationType;
import com.manager.systems.web.financial.dao.FinancialDocumentDao;
import com.manager.systems.web.financial.dto.FinancialDocumentDTO;

public class FinancialDocumentDaoImpl implements FinancialDocumentDao 
{
	private Connection connection;
	
	public FinancialDocumentDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}
	
	@Override
	public void get(final FinancialDocumentDTO document) throws Exception 
	{		
		CallableStatement statement = null;
		ResultSet resultSet = null;
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
		query.append("?, "); //23 - IN _financial_group_id VARCHAR,
		query.append("?, "); //24 - IN _financial_sub_group_id VARCHAR,
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
		query.append("?, "); //35 - IN _totalDebitOrigin DECIMAL(19,4)
		query.append("?, "); //36 - IN _document_parent_id_origin BIGINT
		query.append("?  "); //37 - IN _document_id_origin BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); //01 - IN _operation INT,
			statement.setLong(2,document.getId()); //02 - IN _id BIGINT,
			statement.setLong(3,document.getDocumentParentId()); //03 - IN _parent_id BIGINT,
			statement.setInt(4,document.getDocumentType()); //04 - IN _document_type INT,
			statement.setNull(5,Types.INTEGER); //05 - IN _credit BIT,
			statement.setLong(6,document.getCompanyId()); //06 - IN _company_id BIGINT,
			statement.setLong(7, document.getProviderId()); //07 - IN _provider_id BIGINT,
			statement.setInt(8, document.getBankAccountId()); //08 - IN _bank_account_id INT,
			statement.setString(9, document.getDocumentNumber()); //09 - IN _document_number VARCHAR(20),
			statement.setNull(10, Types.DECIMAL); //10 - IN _document_value DECIMAL(19,4),
			statement.setNull(11, Types.VARCHAR); //11 - IN _document_note VARCHAR(1000),
			statement.setNull(12, Types.INTEGER); //12 - IN _document_status INT,
			statement.setNull(13 ,Types.DECIMAL); //13 - IN _payment_value DECIMAL(19,4),
			statement.setNull(14 ,Types.DECIMAL); //14 - IN _payment_discount DECIMAL(19,4),
			statement.setNull(15, Types.DECIMAL); //15 - IN _payment_extra  DECIMAL(19,4),
			statement.setNull(16, Types.BOOLEAN); //16 - IN _payment_residue BIT,
			statement.setNull(17, Types.DATE); //17 - IN _payment_data DATETIME,
			statement.setNull(18, Types.DATE); //18 - IN _payment_expiry_data DATETIME,
			statement.setLong(19 ,document.getUserChange()); //19 - IN _payment_user BIGINT,
			statement.setNull(20 ,Types.BIGINT); //20 - IN _nfe_number BIGINT,
			statement.setNull(21 ,Types.INTEGER); //21 - IN _nfe_serie_number INT,
			statement.setNull(22 ,Types.BIT); //22 - IN _generate_billet BIT,
			statement.setString(23 ,document.getFinancialGroupId()); //23 - IN _financial_group_id VARCHAR,
			statement.setString(24 ,document.getFinancialSubGroupId()); //24 - IN _financial_sub_group_id VARCHAR,
			statement.setBoolean(25, document.isInactive()); //25 - IN _inactive BIT,
			statement.setLong(26, document.getUserChange()); //26 - IN _user_change BIGINT,
			statement.setNull(27, Types.DATE); //27 - IN _creation_date DATETIME,
			statement.setNull(28,Types.INTEGER); //28 - IN _filter_data_type INT,
			statement.setNull(29,Types.BIT); //29 - IN _moviment_type_credit BIT,
			statement.setNull(30,Types.BIT); //30 - IN _moviment_type_debit BIT,
			statement.setNull(31,Types.BIT); //31 - IN _moviment_type_nfe BIT,
			statement.setNull(32,Types.VARCHAR); //32 - IN _document_status_filter VARCHAR(50),
			statement.setNull(33, Types.INTEGER); //33 - IN bank_account_origin_id	INT, 
			statement.setNull(34, Types.INTEGER); //34 - IN financial_cost_center_id INT,
			statement.setNull(35, Types.DECIMAL); //35 - IN _totalDebitOrigin DECIMAL(19,4)
			statement.setLong(36, document.getDocumentParentIdOrigin()); //36 - IN _document_parent_id_origin BIGINT
			statement.setLong(37, document.getDocumentIdOrigin()); //37 - IN _document_id_origin BIGINT
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	
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
	}

	@Override
	public boolean saveTransfer(final FinancialDocumentDTO document) throws Exception 
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
		query.append("?, "); //23 - IN _financial_group_id VARCHAR,
		query.append("?, "); //24 - IN _financial_sub_group_id VARCHAR,
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
		query.append("?, "); //35 - IN _totalDebitOrigin DECIMAL(19,4)
		query.append("?, "); //36 - IN _document_parent_id_origin BIGINT
		query.append("?, "); //37 - IN _document_id_origin BIGINT
		query.append("?  "); //38 - IN _document_transaction_id BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
			statement.setLong(2,document.getDocumentId()); //02 - IN _id BIGINT,
			statement.setLong(3,document.getDocumentParentId()); //03 - IN _parent_id BIGINT,
			statement.setInt(4, document.getDocumentType()); //04 - IN _document_type INT,
			statement.setBoolean(5, document.isCredit()); //05 - IN _credit BIT,
			statement.setLong(6, document.getCompanyId()); //06 - IN _company_id BIGINT,
			statement.setLong(7, document.getProviderId()); //07 - IN _provider_id BIGINT,
			statement.setInt(8, document.getBankAccountId()); //08 - IN _bank_account_id INT,
			statement.setString(9, document.getDocumentNumber()); //09 - IN _document_number VARCHAR(20),
			statement.setDouble(10, document.getDocumentValue()); //10 - IN _document_value DECIMAL(19,4),
			statement.setString(11, document.getDocumentNote()); //11 - IN _document_note VARCHAR(1000),
			statement.setInt(12, document.getDocumentStatus()); //12 - IN _document_status INT,
			statement.setDouble(13 ,document.getPaymentValue()); //13 - IN _payment_value DECIMAL(19,4),
			statement.setDouble(14 ,document.getDocumentDiscount()); //14 - IN _payment_discount DECIMAL(19,4),
			statement.setDouble(15, document.getDocumentExtra()); //15 - IN _payment_extra  DECIMAL(19,4),
			statement.setBoolean(16, document.isPaymentResidue()); //16 - IN _payment_residue BIT,
			statement.setTimestamp(17, Timestamp.valueOf(document.getPaymentDate())); //17 - IN _payment_data DATETIME,
			statement.setTimestamp(18, Timestamp.valueOf(document.getPaymentExpiryDate())); //18 - IN _payment_expiry_data DATETIME,
			statement.setLong(19, document.getUserChange()); //19 - IN _payment_user BIGINT,
			statement.setNull(20, Types.BIGINT); //20 - IN _nfe_number BIGINT,
			statement.setInt(21, document.getPaymentStatus()); //21 - IN _payment_status INT,
			statement.setBoolean(22, document.isGenerateBillet()); //22 - IN _generate_billet BIT,
			statement.setString(23, document.getFinancialGroupId()); //23 - IN _financial_group_id VARCHAR,
			statement.setString(24 , document.getFinancialSubGroupId()); //24 - IN _financial_sub_group_id VARCHAR,
			statement.setBoolean(25, document.isInactive()); //25 - IN _inactive BIT,
			statement.setLong(26, document.getUserChange()); //26 - IN _user_change BIGINT,
			statement.setNull(27, Types.TIMESTAMP); //27 - IN _creation_date DATETIME,
			statement.setNull(28, Types.INTEGER); //28 - IN _filter_data_type INT,
			statement.setNull(29, Types.BIT); //29 - IN _moviment_type_credit BIT,
			statement.setNull(30, Types.BIT); //30 - IN _moviment_type_debit BIT,
			statement.setNull(31, Types.BIT); //31 - IN _moviment_type_nfe BIT,
			statement.setNull(32, Types.VARCHAR); //32 - IN _document_status_filter VARCHAR(50),
			statement.setInt(33, document.getBankAccountOriginId()); //33 - IN bank_account_origin_id INT,
			statement.setInt(34, document.getFinancialCostCenterId()); //34 - IN financial_cost_center_id INT,
			statement.setDouble(35, document.getTotalDebitOrigin()); //35 - IN _totalDebitOrigin DECIMAL(19,4)
			statement.setLong(36, document.getDocumentParentIdOrigin()); //36 - IN _document_parent_id_origin BIGINT
			statement.setLong(37, document.getDocumentIdOrigin()); //37 - IN _document_id_origin BIGINT
			statement.setLong(38, document.getDocumentTransactionId()); //38 - IN _document_transaction_id BIGINT
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
	
	@Override
	public boolean inactive(final FinancialDocumentDTO document) throws Exception 
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
		query.append("?, "); //23 - IN _financial_group_id VARCHAR,
		query.append("?, "); //24 - IN _financial_sub_group_id VARCHAR,
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
		query.append("?, "); //35 - IN _totalDebitOrigin DECIMAL(19,4)
		query.append("?, "); //36 - IN _document_parent_id_origin BIGINT
		query.append("?, "); //37 - IN _document_id_origin BIGINT
		query.append("?  "); //38 - IN _document_transaction_id BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.INACTIVE.getType()); //1 - IN _operation INT
			statement.setLong(2,document.getId()); //02 - IN _id BIGINT,
			statement.setLong(3,document.getDocumentParentId()); //03 - IN _parent_id BIGINT,
			statement.setInt(4,document.getDocumentType()); //04 - IN _document_type INT,
			statement.setNull(5,Types.BIT); //05 - IN _credit BIT,
			statement.setNull(6,Types.BIGINT); //06 - IN _company_id BIGINT,
			statement.setNull(7, Types.BIGINT); //07 - IN _provider_id BIGINT,
			statement.setNull(8, Types.INTEGER); //08 - IN _bank_account_id INT,
			statement.setNull(9, Types.VARCHAR); //09 - IN _document_number VARCHAR(20),
			statement.setNull(10, Types.DECIMAL); //10 - IN _document_value DECIMAL(19,4),
			statement.setNull(11, Types.VARCHAR); //11 - IN _document_note VARCHAR(1000),
			statement.setNull(12, Types.INTEGER); //12 - IN _document_status INT,
			statement.setNull(13 ,Types.DECIMAL); //13 - IN _payment_value DECIMAL(19,4),
			statement.setNull(14 ,Types.DECIMAL); //14 - IN _payment_discount DECIMAL(19,4),
			statement.setNull(15, Types.DECIMAL); //15 - IN _payment_extra  DECIMAL(19,4),
			statement.setNull(16, Types.BIT); //16 - IN _payment_residue BIT,
			statement.setNull(17, Types.TIMESTAMP); //17 - IN _payment_data DATETIME,
			statement.setNull(18, Types.TIMESTAMP); //18 - IN _payment_expiry_data DATETIME,
			statement.setNull(19 ,Types.BIGINT); //19 - IN _payment_user BIGINT,
			statement.setNull(20 ,Types.BIGINT); //20 - IN _nfe_number BIGINT,
			statement.setNull(21 ,Types.INTEGER); //21 - IN _nfe_serie_number INT,
			statement.setNull(22 ,Types.BIT); //22 - IN _generate_billet BIT,
			statement.setNull(23 ,Types.VARCHAR); //23 - IN _financial_group_id VARCHAR,
			statement.setNull(24 ,Types.VARCHAR); //24 - IN _financial_sub_group_id VARCHAR,
			statement.setBoolean(25, document.isInactive()); //25 - IN _inactive BIT,
			statement.setLong(26, document.getUserChange()); //26 - IN _user_change BIGINT,
			statement.setNull(27, Types.TIMESTAMP); //27 - IN _creation_date DATETIME,
			statement.setNull(28,Types.INTEGER); //28 - IN _filter_data_type INT,
			statement.setNull(29,Types.BIT); //29 - IN _moviment_type_credit BIT,
			statement.setNull(30,Types.BIT); //30 - IN _moviment_type_debit BIT,
			statement.setNull(31,Types.BIT); //31 - IN _moviment_type_nfe BIT,
			statement.setNull(32,Types.VARCHAR); //32 - IN _document_status_filter VARCHAR(50),
			statement.setNull(33, Types.INTEGER); //33 - IN bank_account_origin_id	INT, 
			statement.setNull(34, Types.INTEGER); //34 - IN financial_cost_center_id INT,
			statement.setNull(35, Types.DECIMAL); //35 - IN _totalDebitOrigin DECIMAL(19,4)
			statement.setLong(36, document.getDocumentParentIdOrigin()); //36 - IN _document_parent_id_origin BIGINT
			statement.setLong(37, document.getDocumentIdOrigin()); //37 - IN _document_id_origin BIGINT
			statement.setLong(38, document.getDocumentTransactionId()); //38 - IN _document_transaction_id BIGINT
	        statement.executeUpdate();  
	        result=true;
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
	
	@Override
	public boolean ungroup(final FinancialDocumentDTO document) throws Exception 
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
		query.append("?, "); //23 - IN _financial_group_id VARCHAR,
		query.append("?, "); //24 - IN _financial_sub_group_id VARCHAR,
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
		query.append("?, "); //35 - IN _totalDebitOrigin DECIMAL(19,4)
		query.append("?, "); //36 - IN _document_parent_id_origin BIGINT
		query.append("?, "); //37 - IN _document_id_origin BIGINT
		query.append("?  "); //38 - IN _document_transaction_id BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.UNGROUP.getType()); //1 - IN _operation INT
			statement.setLong(2,document.getId()); //02 - IN _id BIGINT,
			statement.setLong(3,document.getDocumentParentId()); //03 - IN _parent_id BIGINT,
			statement.setInt(4,document.getDocumentType()); //04 - IN _document_type INT,
			statement.setNull(5,Types.BIT); //05 - IN _credit BIT,
			statement.setNull(6,Types.BIGINT); //06 - IN _company_id BIGINT,
			statement.setNull(7, Types.BIGINT); //07 - IN _provider_id BIGINT,
			statement.setNull(8, Types.INTEGER); //08 - IN _bank_account_id INT,
			statement.setNull(9, Types.VARCHAR); //09 - IN _document_number VARCHAR(20),
			statement.setNull(10, Types.DECIMAL); //10 - IN _document_value DECIMAL(19,4),
			statement.setNull(11, Types.VARCHAR); //11 - IN _document_note VARCHAR(1000),
			statement.setNull(12, Types.INTEGER); //12 - IN _document_status INT,
			statement.setNull(13 ,Types.DECIMAL); //13 - IN _payment_value DECIMAL(19,4),
			statement.setNull(14 ,Types.DECIMAL); //14 - IN _payment_discount DECIMAL(19,4),
			statement.setNull(15, Types.DECIMAL); //15 - IN _payment_extra  DECIMAL(19,4),
			statement.setNull(16, Types.BIT); //16 - IN _payment_residue BIT,
			statement.setNull(17, Types.TIMESTAMP); //17 - IN _payment_data DATETIME,
			statement.setNull(18, Types.TIMESTAMP); //18 - IN _payment_expiry_data DATETIME,
			statement.setNull(19 ,Types.BIGINT); //19 - IN _payment_user BIGINT,
			statement.setNull(20 ,Types.BIGINT); //20 - IN _nfe_number BIGINT,
			statement.setNull(21 ,Types.INTEGER); //21 - IN _nfe_serie_number INT,
			statement.setNull(22 ,Types.BIT); //22 - IN _generate_billet BIT,
			statement.setNull(23 ,Types.VARCHAR); //23 - IN _financial_group_id VARCHAR,
			statement.setNull(24 ,Types.VARCHAR); //24 - IN _financial_sub_group_id VARCHAR,
			statement.setNull(25, Types.BIT); //25 - IN _inactive BIT,
			statement.setLong(26, document.getUserChange()); //26 - IN _user_change BIGINT,
			statement.setNull(27, Types.TIMESTAMP); //27 - IN _creation_date DATETIME,
			statement.setNull(28,Types.INTEGER); //28 - IN _filter_data_type INT,
			statement.setNull(29,Types.BIT); //29 - IN _moviment_type_credit BIT,
			statement.setNull(30,Types.BIT); //30 - IN _moviment_type_debit BIT,
			statement.setNull(31,Types.BIT); //31 - IN _moviment_type_nfe BIT,
			statement.setNull(32,Types.VARCHAR); //32 - IN _document_status_filter VARCHAR(50),
			statement.setNull(33, Types.INTEGER); //33 - IN bank_account_origin_id	INT, 
			statement.setNull(34, Types.INTEGER); //34 - IN financial_cost_center_id INT,
			statement.setNull(35, Types.DECIMAL); //35 - IN _totalDebitOrigin DECIMAL(19,4)
			statement.setLong(36, document.getDocumentParentIdOrigin()); //36 - IN _document_parent_id_origin BIGINT
			statement.setLong(37, document.getDocumentIdOrigin()); //37 - IN _document_id_origin BIGINT
			statement.setLong(38, document.getDocumentTransactionId()); //38 - IN _document_transaction_id BIGINT
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
	
	@Override
	public boolean changeStatus(final FinancialDocumentDTO document) throws Exception 
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
		query.append("?, "); //23 - IN _financial_group_id VARCHAR,
		query.append("?, "); //24 - IN _financial_sub_group_id VARCHAR,
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
		query.append("?, "); //35 - IN _totalDebitOrigin DECIMAL(19,4)
		query.append("?, "); //36 - IN _document_parent_id_origin BIGINT
		query.append("?, "); //37 - IN _document_id_origin BIGINT
		query.append("?  "); //38 - IN _document_transaction_id BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.CHANGE_STATUS.getType()); //1 - IN _operation INT
			statement.setLong(2,document.getId()); //02 - IN _id BIGINT,
			statement.setLong(3,document.getDocumentParentId()); //03 - IN _parent_id BIGINT,
			statement.setInt(4,document.getDocumentType()); //04 - IN _document_type INT,
			statement.setNull(5,Types.BIT); //05 - IN _credit BIT,
			statement.setNull(6,Types.BIGINT); //06 - IN _company_id BIGINT,
			statement.setNull(7, Types.BIGINT); //07 - IN _provider_id BIGINT,
			statement.setNull(8, Types.INTEGER); //08 - IN _bank_account_id INT,
			statement.setNull(9, Types.VARCHAR); //09 - IN _document_number VARCHAR(20),
			statement.setNull(10, Types.DECIMAL); //10 - IN _document_value DECIMAL(19,4),
			statement.setNull(11, Types.VARCHAR); //11 - IN _document_note VARCHAR(1000),
			statement.setInt(12, document.getDocumentStatus()); //12 - IN _document_status INT,
			statement.setNull(13 ,Types.DECIMAL); //13 - IN _payment_value DECIMAL(19,4),
			statement.setNull(14 ,Types.DECIMAL); //14 - IN _payment_discount DECIMAL(19,4),
			statement.setNull(15, Types.DECIMAL); //15 - IN _payment_extra  DECIMAL(19,4),
			statement.setNull(16, Types.BIT); //16 - IN _payment_residue BIT,
			statement.setNull(17, Types.TIMESTAMP); //17 - IN _payment_data DATETIME,
			statement.setNull(18, Types.TIMESTAMP); //18 - IN _payment_expiry_data DATETIME,
			statement.setNull(19 ,Types.BIGINT); //19 - IN _payment_user BIGINT,
			statement.setNull(20 ,Types.BIGINT); //20 - IN _nfe_number BIGINT,
			statement.setNull(21 ,Types.INTEGER); //21 - IN _nfe_serie_number INT,
			statement.setNull(22 ,Types.BIT); //22 - IN _generate_billet BIT,
			statement.setNull(23 ,Types.VARCHAR); //23 - IN _financial_group_id VARCHAR,
			statement.setNull(24 ,Types.VARCHAR); //24 - IN _financial_sub_group_id VARCHAR,
			statement.setNull(25, Types.BIT); //25 - IN _inactive BIT,
			statement.setLong(26, document.getUserChange()); //26 - IN _user_change BIGINT,
			statement.setNull(27, Types.TIMESTAMP); //27 - IN _creation_date DATETIME,
			statement.setNull(28,Types.INTEGER); //28 - IN _filter_data_type INT,
			statement.setNull(29,Types.BIT); //29 - IN _moviment_type_credit BIT,
			statement.setNull(30,Types.BIT); //30 - IN _moviment_type_debit BIT,
			statement.setNull(31,Types.BIT); //31 - IN _moviment_type_nfe BIT,
			statement.setNull(32,Types.VARCHAR); //32 - IN _document_status_filter VARCHAR(50),
			statement.setNull(33, Types.INTEGER); //33 - IN bank_account_origin_id	INT, 
			statement.setNull(34, Types.INTEGER); //34 - IN financial_cost_center_id INT,
			statement.setNull(35, Types.DECIMAL); //35 - IN _totalDebitOrigin DECIMAL(19,4)
			statement.setLong(36, document.getDocumentParentIdOrigin()); //36 - IN _document_parent_id_origin BIGINT
			statement.setLong(37, document.getDocumentIdOrigin()); //37 - IN _document_id_origin BIGINT
			statement.setLong(38, document.getDocumentTransactionId()); //38 - IN _document_transaction_id BIGINT
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

	@Override
	public boolean change(final FinancialDocumentDTO document) throws Exception
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
		query.append("?, "); //23 - IN _financial_group_id VARCHAR,
		query.append("?, "); //24 - IN _financial_sub_group_id VARCHAR,
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
		query.append("?, "); //35 - IN _totalDebitOrigin DECIMAL(19,4)
		query.append("?, "); //36 - IN _document_parent_id_origin BIGINT
		query.append("?, "); //37 - IN _document_id_origin BIGINT
		query.append("?  "); //38 - IN _document_transaction_id BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.CHANGE.getType()); //01 - IN _operation INT,
			statement.setLong(2,document.getDocumentId()); //02 - IN _id BIGINT,
			statement.setLong(3,document.getDocumentParentId()); //03 - IN _parent_id BIGINT,
			statement.setInt(4,document.getDocumentType()); //04 - IN _document_type INT,
			statement.setBoolean(5, document.isCredit()); //05 - IN _credit BIT,
			statement.setLong(6,document.getCompanyId()); //06 - IN _company_id BIGINT,
			statement.setLong(7, document.getProviderId()); //07 - IN _provider_id BIGINT,
			statement.setInt(8, document.getBankAccountId()); //08 - IN _bank_account_id INT,
			statement.setString(9, document.getDocumentNumber()); //09 - IN _document_number VARCHAR(20),
			statement.setDouble(10, document.getDocumentValue()); //10 - IN _document_value DECIMAL(19,4),
			statement.setString(11, document.getDocumentNote()); //11 - IN _document_note VARCHAR(1000),
			statement.setInt(12, document.getDocumentStatus()); //12 - IN _document_status INT,
			statement.setDouble(13 ,document.getPaymentValue()); //13 - IN _payment_value DECIMAL(19,4),
			statement.setDouble(14 ,document.getDocumentDiscount()); //14 - IN _payment_discount DECIMAL(19,4),
			statement.setDouble(15, document.getDocumentExtra()); //15 - IN _payment_extra  DECIMAL(19,4),
			statement.setBoolean(16, document.isPaymentResidue()); //16 - IN _payment_residue BIT,
			statement.setTimestamp(17, Timestamp.valueOf(document.getPaymentDate())); //17 - IN _payment_data DATETIME,
			statement.setTimestamp(18, Timestamp.valueOf(document.getPaymentExpiryDate())); //18 - IN _payment_expiry_data DATETIME,
			statement.setLong(19 ,document.getUserChange()); //19 - IN _payment_user BIGINT,
			statement.setNull(20 ,Types.BIGINT); //20 - IN _nfe_number BIGINT,
			statement.setInt(21 , document.getPaymentStatus()); //21 - IN _payment_status INT,
			statement.setBoolean(22 ,document.isGenerateBillet()); //22 - IN _generate_billet BIT,
			statement.setString(23 ,document.getFinancialGroupId()); //23 - IN _financial_group_id VARCHAR,
			statement.setString(24 ,document.getFinancialSubGroupId()); //24 - IN _financial_sub_group_id VARCHAR,
			statement.setBoolean(25, document.isInactive()); //25 - IN _inactive BIT,
			statement.setLong(26, document.getUserChange()); //26 - IN _user_change BIGINT,
			statement.setNull(27, Types.TIMESTAMP); //27 - IN _creation_date DATETIME,
			statement.setNull(28,Types.INTEGER); //28 - IN _filter_data_type INT,
			statement.setNull(29,Types.BIT); //29 - IN _moviment_type_credit BIT,
			statement.setNull(30,Types.BIT); //30 - IN _moviment_type_debit BIT,
			statement.setNull(31,Types.BIT); //31 - IN _moviment_type_nfe BIT,
			statement.setNull(32,Types.VARCHAR); //32 - IN _document_status_filter VARCHAR(50),
			statement.setNull(33, Types.INTEGER); //33 - IN bank_account_origin_id	INT, 
			statement.setNull(34, Types.INTEGER); //34 - IN financial_cost_center_id INT,
			statement.setNull(35, Types.DECIMAL); //35 - IN _totalDebitOrigin DECIMAL(19,4)
			statement.setLong(36, document.getDocumentParentIdOrigin()); //36 - IN _document_parent_id_origin BIGINT
			statement.setLong(37, document.getDocumentIdOrigin()); //37 - IN _document_id_origin BIGINT
			statement.setLong(38, document.getDocumentTransactionId()); //38 - IN _document_transaction_id BIGINT
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
	
	@Override
	public boolean groupParent(final FinancialDocumentDTO document) throws Exception 
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
		query.append("?, "); //23 - IN _financial_group_id VARCHAR,
		query.append("?, "); //24 - IN _financial_sub_group_id VARCHAR,
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
		query.append("?, "); //35 - IN _totalDebitOrigin DECIMAL(19,4)
		query.append("?, "); //36 - IN _document_parent_id_origin BIGINT
		query.append("?, "); //37 - IN _document_id_origin BIGINT
		query.append("?  "); //38 - IN _document_transaction_id BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GROUP_PARENT.getType()); //1 - IN _operation INT
			statement.setNull(2, Types.BIGINT); //02 - IN _id BIGINT,
			statement.setLong(3, document.getDocumentParentId()); //03 - IN _parent_id BIGINT,
			statement.setNull(4, Types.INTEGER); //04 - IN _document_type INT,
			statement.setNull(5,Types.BIT); //05 - IN _credit BIT,
			statement.setNull(6,Types.BIGINT); //06 - IN _company_id BIGINT,
			statement.setNull(7, Types.BIGINT); //07 - IN _provider_id BIGINT,
			statement.setNull(8, Types.INTEGER); //08 - IN _bank_account_id INT,
			statement.setNull(9, Types.VARCHAR); //09 - IN _document_number VARCHAR(20),
			statement.setNull(10, Types.DECIMAL); //10 - IN _document_value DECIMAL(19,4),
			statement.setString(11, document.getDocumentNote()); //11 - IN _document_note VARCHAR(1000),
			statement.setNull(12, Types.INTEGER); //12 - IN _document_status INT,
			statement.setNull(13 ,Types.DECIMAL); //13 - IN _payment_value DECIMAL(19,4),
			statement.setNull(14 ,Types.DECIMAL); //14 - IN _payment_discount DECIMAL(19,4),
			statement.setNull(15, Types.DECIMAL); //15 - IN _payment_extra  DECIMAL(19,4),
			statement.setNull(16, Types.BIT); //16 - IN _payment_residue BIT,
			statement.setNull(17, Types.TIMESTAMP); //17 - IN _payment_data DATETIME,
			statement.setNull(18, Types.TIMESTAMP); //18 - IN _payment_expiry_data DATETIME,
			statement.setNull(19 ,Types.BIGINT); //19 - IN _payment_user BIGINT,
			statement.setNull(20 ,Types.BIGINT); //20 - IN _nfe_number BIGINT,
			statement.setNull(21 ,Types.INTEGER); //21 - IN _nfe_serie_number INT,
			statement.setNull(22 ,Types.BIT); //22 - IN _generate_billet BIT,
			statement.setNull(23 ,Types.VARCHAR); //23 - IN _financial_group_id VARCHAR,
			statement.setNull(24 ,Types.VARCHAR); //24 - IN _financial_sub_group_id VARCHAR,
			statement.setNull(25, Types.BIT); //25 - IN _inactive BIT,
			statement.setLong(26, document.getUserChange()); //26 - IN _user_change BIGINT,
			statement.setNull(27, Types.TIMESTAMP); //27 - IN _creation_date DATETIME,
			statement.setNull(28,Types.INTEGER); //28 - IN _filter_data_type INT,
			statement.setNull(29,Types.BIT); //29 - IN _moviment_type_credit BIT,
			statement.setNull(30,Types.BIT); //30 - IN _moviment_type_debit BIT,
			statement.setNull(31,Types.BIT); //31 - IN _moviment_type_nfe BIT,
			statement.setNull(32,Types.VARCHAR); //32 - IN _document_status_filter VARCHAR(50),
			statement.setNull(33, Types.INTEGER); //33 - IN bank_account_origin_id	INT, 
			statement.setNull(34, Types.INTEGER); //34 - IN financial_cost_center_id INT,
			statement.setNull(35, Types.DECIMAL); //35 - IN _totalDebitOrigin DECIMAL(19,4)
			statement.setLong(36, document.getDocumentParentIdOrigin()); //36 - IN _document_parent_id_origin BIGINT
			statement.setLong(37, document.getDocumentIdOrigin()); //37 - IN _document_id_origin BIGINT
			statement.setLong(38, document.getDocumentTransactionId()); //38 - IN _document_transaction_id BIGINT
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
	
	@Override
	public boolean saveNewDocument(final FinancialDocumentDTO document) throws Exception 
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
		query.append("?, "); //23 - IN _financial_group_id VARCHAR,
		query.append("?, "); //24 - IN _financial_sub_group_id VARCHAR,
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
		query.append("?, "); //35 - IN _totalDebitOrigin DECIMAL(19,4)
		query.append("?, "); //36 - IN _document_parent_id_origin BIGINT
		query.append("?, "); //37 - IN _document_id_origin BIGINT
		query.append("?  "); //38 - IN _document_transaction_id BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE_NEW_DOCUMENT.getType()); //01 - IN _operation INT,
			statement.setLong(2,document.getDocumentId()); //02 - IN _id BIGINT,
			statement.setLong(3,document.getDocumentParentId()); //03 - IN _parent_id BIGINT,
			statement.setInt(4, document.getDocumentType()); //04 - IN _document_type INT,
			statement.setBoolean(5, document.isCredit()); //05 - IN _credit BIT,
			statement.setLong(6, document.getCompanyId()); //06 - IN _company_id BIGINT,
			statement.setLong(7, document.getProviderId()); //07 - IN _provider_id BIGINT,
			statement.setInt(8, document.getBankAccountId()); //08 - IN _bank_account_id INT,
			statement.setString(9, document.getDocumentNumber()); //09 - IN _document_number VARCHAR(20),
			statement.setDouble(10, document.getDocumentValue()); //10 - IN _document_value DECIMAL(19,4),
			statement.setString(11, document.getDocumentNote()); //11 - IN _document_note VARCHAR(1000),
			statement.setInt(12, document.getDocumentStatus()); //12 - IN _document_status INT,
			statement.setDouble(13 ,document.getPaymentValue()); //13 - IN _payment_value DECIMAL(19,4),
			statement.setDouble(14 ,document.getDocumentDiscount()); //14 - IN _payment_discount DECIMAL(19,4),
			statement.setDouble(15, document.getDocumentExtra()); //15 - IN _payment_extra  DECIMAL(19,4),
			statement.setBoolean(16, document.isPaymentResidue()); //16 - IN _payment_residue BIT,
			statement.setTimestamp(17, Timestamp.valueOf(document.getPaymentDate())); //17 - IN _payment_data DATETIME,
			statement.setTimestamp(18, Timestamp.valueOf(document.getPaymentExpiryDate())); //18 - IN _payment_expiry_data DATETIME,
			statement.setLong(19, document.getUserChange()); //19 - IN _payment_user BIGINT,
			statement.setNull(20, Types.BIGINT); //20 - IN _nfe_number BIGINT,
			statement.setInt(21, document.getPaymentStatus()); //21 - IN _payment_status INT,
			statement.setBoolean(22, document.isGenerateBillet()); //22 - IN _generate_billet BIT,
			statement.setString(23, document.getFinancialGroupId()); //23 - IN _financial_group_id VARCHAR,
			statement.setString(24 , document.getFinancialSubGroupId()); //24 - IN _financial_sub_group_id VARCHAR,
			statement.setBoolean(25, document.isInactive()); //25 - IN _inactive BIT,
			statement.setLong(26, document.getUserChange()); //26 - IN _user_change BIGINT,
			statement.setNull(27, Types.TIMESTAMP); //27 - IN _creation_date DATETIME,
			statement.setNull(28, Types.INTEGER); //28 - IN _filter_data_type INT,
			statement.setNull(29, Types.BIT); //29 - IN _moviment_type_credit BIT,
			statement.setNull(30, Types.BIT); //30 - IN _moviment_type_debit BIT,
			statement.setNull(31, Types.BIT); //31 - IN _moviment_type_nfe BIT,
			statement.setNull(32, Types.VARCHAR); //32 - IN _document_status_filter VARCHAR(50),
			statement.setInt(33, document.getBankAccountOriginId()); //33 - IN bank_account_origin_id INT,
			statement.setInt(34, document.getFinancialCostCenterId()); //34 - IN financial_cost_center_id INT,
			statement.setNull(35, Types.DECIMAL); //35 - IN _totalDebitOrigin DECIMAL(19,4)
			statement.setLong(36, document.getDocumentParentIdOrigin()); //36 - IN _document_parent_id_origin BIGINT
			statement.setLong(37, document.getDocumentIdOrigin()); //37 - IN _document_id_origin BIGINT
			statement.setLong(38, document.getDocumentTransactionId()); //38 - IN _document_transaction_id BIGINT
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
	
	@Override
	public boolean saveDocumentExpense(final FinancialDocumentDTO document) throws Exception 
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
		query.append("?, "); //23 - IN _financial_group_id VARCHAR,
		query.append("?, "); //24 - IN _financial_sub_group_id VARCHAR,
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
		query.append("?, "); //35 - IN _totalDebitOrigin DECIMAL(19,4)
		query.append("?, "); //36 - IN _document_parent_id_origin BIGINT
		query.append("?, "); //37 - IN _document_id_origin BIGINT
		query.append("?  "); //38 - IN _document_transaction_id BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE_NEW_DOCUMENT_EXPENSE.getType()); //01 - IN _operation INT,
			statement.setLong(2,document.getDocumentId()); //02 - IN _id BIGINT,
			statement.setLong(3,document.getDocumentParentId()); //03 - IN _parent_id BIGINT,
			statement.setInt(4, document.getDocumentType()); //04 - IN _document_type INT,
			statement.setBoolean(5, document.isCredit()); //05 - IN _credit BIT,
			statement.setLong(6, document.getCompanyId()); //06 - IN _company_id BIGINT,
			statement.setLong(7, document.getProviderId()); //07 - IN _provider_id BIGINT,
			statement.setInt(8, document.getBankAccountId()); //08 - IN _bank_account_id INT,
			statement.setString(9, document.getDocumentNumber()); //09 - IN _document_number VARCHAR(20),
			statement.setDouble(10, document.getDocumentValue()); //10 - IN _document_value DECIMAL(19,4),
			statement.setString(11, document.getDocumentNote()); //11 - IN _document_note VARCHAR(1000),
			statement.setInt(12, document.getDocumentStatus()); //12 - IN _document_status INT,
			statement.setDouble(13 ,document.getPaymentValue()); //13 - IN _payment_value DECIMAL(19,4),
			statement.setDouble(14 ,document.getDocumentDiscount()); //14 - IN _payment_discount DECIMAL(19,4),
			statement.setDouble(15, document.getDocumentExtra()); //15 - IN _payment_extra  DECIMAL(19,4),
			statement.setBoolean(16, document.isPaymentResidue()); //16 - IN _payment_residue BIT,
			statement.setTimestamp(17, Timestamp.valueOf(document.getPaymentDate())); //17 - IN _payment_data DATETIME,
			statement.setTimestamp(18, Timestamp.valueOf(document.getPaymentExpiryDate())); //18 - IN _payment_expiry_data DATETIME,
			statement.setLong(19, document.getUserChange()); //19 - IN _payment_user BIGINT,
			statement.setNull(20, Types.BIGINT); //20 - IN _nfe_number BIGINT,
			statement.setInt(21, document.getPaymentStatus()); //21 - IN _payment_status INT,
			statement.setBoolean(22, document.isGenerateBillet()); //22 - IN _generate_billet BIT,
			statement.setString(23, document.getFinancialGroupId()); //23 - IN _financial_group_id VARCHAR,
			statement.setString(24 , document.getFinancialSubGroupId()); //24 - IN _financial_sub_group_id VARCHAR,
			statement.setBoolean(25, document.isInactive()); //25 - IN _inactive BIT,
			statement.setLong(26, document.getUserChange()); //26 - IN _user_change BIGINT,
			statement.setNull(27, Types.TIMESTAMP); //27 - IN _creation_date DATETIME,
			statement.setNull(28, Types.INTEGER); //28 - IN _filter_data_type INT,
			statement.setNull(29, Types.BIT); //29 - IN _moviment_type_credit BIT,
			statement.setNull(30, Types.BIT); //30 - IN _moviment_type_debit BIT,
			statement.setNull(31, Types.BIT); //31 - IN _moviment_type_nfe BIT,
			statement.setNull(32, Types.VARCHAR); //32 - IN _document_status_filter VARCHAR(50),
			statement.setInt(33, document.getBankAccountOriginId()); //33 - IN bank_account_origin_id INT,
			statement.setInt(34, document.getFinancialCostCenterId()); //34 - IN financial_cost_center_id INT
			statement.setNull(35, Types.DECIMAL); //35 - IN _totalDebitOrigin DECIMAL(19,4)
			statement.setLong(36, document.getDocumentParentIdOrigin()); //36 - IN _document_parent_id_origin BIGINT
			statement.setLong(37, document.getDocumentIdOrigin()); //37 - IN _document_id_origin BIGINT
			statement.setLong(38, document.getDocumentTransactionId()); //38 - IN _document_transaction_id BIGINT
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

	@Override
	public boolean rollbackMovement(final FinancialDocumentDTO financialDocument) throws Exception {
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_document_movement_rollback");
		query.append("(");
		query.append("?, "); //01- IN _document_parent_id BIGINT
		query.append("?, "); //02 - IN _document_id BIGINT
		query.append("?  "); //03 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setLong(1, financialDocument.getDocumentParentId()); //01 - IN _document_parent_id_origin BIGINT
			statement.setLong(2, financialDocument.getDocumentId()); //02 - IN _document_id_origin BIGINT
			statement.setLong(3, financialDocument.getUserChange()); //03 - IN _user_change BIGINT,
	        statement.executeUpdate();  
	        result=true;
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