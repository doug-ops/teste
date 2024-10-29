package com.manager.systems.common.dao.impl.movement.company;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;

import com.manager.systems.common.dao.movement.company.CompanyMovementExecutionDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.movement.company.CompanyMovementExecutionDTO;
import com.manager.systems.common.dto.movement.company.CompanyMovementExecutionItemDTO;
import com.manager.systems.common.vo.OperationType;

public class CompanyMovementExecutionDaoImpl implements CompanyMovementExecutionDao 
{
	private Connection connection;
	
	public CompanyMovementExecutionDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}
	
	@Override
	public void get(final CompanyMovementExecutionDTO companyMovementExecution) throws Exception 
	{	
		CallableStatement statement = null;
		ResultSet resultSet = null;
		
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_company_movement_execution");
		query.append("(");
		query.append("?, "); //01 - _operation INT,
		query.append("?, "); //02 - _date_from DATETIME,
		query.append("?, "); //03 - IN _date_to DATETIME,
		query.append("?, "); //04 - IN _company_ids VARCHAR(500),
		query.append("?, "); //05 - IN _field_id BIGINT(20),
		query.append("?, "); //06 - IN _field_name VARCHAR(20),
		query.append("?, "); //07 - IN _field_value VARCHAR(500)
		query.append("?, "); //08 - IN _company_id BIGINT(20),
		query.append("?, "); //09 - IN _document_parent_id BIGINT(20),
		query.append("?, "); //10 - IN _execution_date DATETIME,
		query.append("?, "); //11 - IN _expirationDate DATETIME,
		query.append("?, "); //12 - IN _bank_account_origin_id BIGINT
		query.append("?, "); //13 - IN _bank_account_destiny_id BIGINT,
		query.append("?, "); //14 - IN _week_year INT(11),
		query.append("?, "); //15 - IN _company_description VARCHAR(500),
		query.append("?, "); //16 - IN _user_id BIGINT,
		query.append("?, "); //17 - IN _is_execution BI,
		query.append("?, "); //18 - IN _document_note varchar(1000),
		query.append("?, "); //19 - IN _motive INT, 
		query.append("?");  //20 - IN _inactive BIT
		query.append(")"); 
		query.append("}");

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_COMPANY.getType()); //01 - _operation INT,
			statement.setNull(2,Types.DATE); //02 - IN _date_from DATETIME,
			statement.setNull(3, Types.DATE); //03 - IN _date_to DATETIME,
			statement.setNull(4, Types.BIGINT); //04 - IN _company_ids VARCHAR(500),
			statement.setNull(5, Types.INTEGER); //05 - IN _field_id BIGINT(20),
			statement.setNull(6, Types.VARCHAR); //06 - IN _field_name VARCHAR(20),
			statement.setNull(7, Types.VARCHAR); //07 - IN _field_value VARCHAR(500),
			statement.setString(8, companyMovementExecution.getCompanyId()); //08 - IN _company_id BIGINT(20),
			statement.setNull(9, Types.BIGINT); //09 - IN _document_parent_id BIGINT(20),
			statement.setNull(10,Types.DATE); //10 - IN _execution_date DATETIME,
			if(companyMovementExecution.getExpirationDate() == null) {
				statement.setNull(11, Types.DATE);  // 11 - IN _expirationDate DATETIME
			}else {
				statement.setDate(11, new java.sql.Date(companyMovementExecution.getExpirationDate().getTime())); // 11 - IN _expirationDate DATETIME
			}
			statement.setLong(12, companyMovementExecution.getBankAccountOriginId()); //12 - IN _bank_account_origin_id BIGINT
            statement.setLong(13, companyMovementExecution.getBankAccountDestinyId()); //13 - IN _bank_account_destiny_id BIGINT,
            statement.setLong(14, companyMovementExecution.getWeekYear()); //14 - IN _week_year INT(11),
            statement.setNull(15, Types.VARCHAR); //15 - IN _company_description VARCHAR(500),
            statement.setLong(16, companyMovementExecution.getUserId());//16 - IN _user_id BIGINT,
            statement.setNull(17, Types.BIT); //17 - IN _is_execution BIT,
            statement.setNull(18, Types.VARCHAR); //18 - IN _document_note varchar(1000),
            statement.setNull(19, Types.INTEGER); //19 - IN _motive INT,
            statement.setNull(20, Types.BIT); //20 - IN _inactive BIT
            		
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				final CompanyMovementExecutionItemDTO item = new CompanyMovementExecutionItemDTO();
				item.setId(resultSet.getLong(DatabaseConstants.COLUMN_ID));
				item.setCompany(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION));
				item.setCompanyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID));
				item.setMotive(resultSet.getString(DatabaseConstants.COLUMN_MOTIVE));
				item.setDocumentNote(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE));
				companyMovementExecution.addItem(item);
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
	public void getAll(final CompanyMovementExecutionDTO companyMovementExecution) throws Exception 
	{	
		CallableStatement statement = null;
		ResultSet resultSet = null;
		
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_company_movement_execution");
		query.append("(");
		query.append("?, "); //01 - _operation INT,
		query.append("?, "); //02 - _date_from DATETIME,
		query.append("?, "); //03 - IN _date_to DATETIME,
		query.append("?, "); //04 - IN _company_ids VARCHAR(500),
		query.append("?, "); //05 - IN _field_id BIGINT(20),
		query.append("?, "); //06 - IN _field_name VARCHAR(20),
		query.append("?, "); //07 - IN _field_value VARCHAR(500)
		query.append("?, "); //08 - IN _company_id BIGINT(20),
		query.append("?, "); //09 - IN _document_parent_id BIGINT(20),
		query.append("?, "); //10 - IN _execution_date DATETIME,
		query.append("?, "); //11 - IN _expirationDate DATETIME,
		query.append("?, "); //12 - IN _bank_account_origin_id BIGINT
		query.append("?, "); //13 - IN _bank_account_destiny_id BIGINT,
		query.append("?, "); //14 - IN _week_year INT(11),
		query.append("?, "); //15 - IN _company_description VARCHAR(500),
		query.append("?, "); //16 - IN _user_id BIGINT,
		query.append("?, "); //17 - IN _is_execution BIT,
		query.append("?, "); //18 - IN _document_note varchar(1000),
		query.append("?, "); //19 - IN _motive INT, 
		query.append("?  ");   //20 - IN _inactive BIT 
		query.append(")");
		query.append("}");

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - _operation INT,
			statement.setTimestamp(2,Timestamp.valueOf(companyMovementExecution.getDateFrom())); //02 - IN _date_from DATETIME,
			statement.setTimestamp(3, Timestamp.valueOf(companyMovementExecution.getDateTo())); //03 - IN _date_to DATETIME,
			statement.setString(4, companyMovementExecution.getCompanyId()); //04 - IN _company_ids VARCHAR(500),
			statement.setNull(5, Types.BIGINT); //05 - IN _field_id BIGINT(20),
			statement.setNull(6, Types.VARCHAR); //06 - IN _field_name VARCHAR(20),
			statement.setNull(7, Types.VARCHAR); //07 - IN _field_value VARCHAR(500),
			statement.setNull(8, Types.BIGINT); //08 - IN _company_id BIGINT(20),
			statement.setNull(9, Types.BIGINT); //09 - IN _document_parent_id BIGINT(20),
			statement.setNull(10, Types.DATE); //10 - IN _execution_date DATETIME,
			statement.setNull(11, Types.DATE); // 11 - IN _expirationDate DATETIME
			statement.setNull(12, Types.BIGINT); //12 - IN _bank_account_origin_id BIGINT
            statement.setNull(13, Types.BIGINT); //13 - IN _bank_account_destiny_id BIGINT,
            statement.setNull(14, Types.INTEGER); //14 - IN _week_year INT(11),
            statement.setNull(15, Types.VARCHAR); //15 - IN _company_description VARCHAR(500),
            statement.setLong(16, companyMovementExecution.getUserId());//16 - IN _user_id BIGINT,
            statement.setNull(17, Types.BIT); //17 - IN _is_execution BIT,
            statement.setNull(18, Types.VARCHAR); //18 - IN _document_note varchar(1000),
            statement.setNull(19, Types.INTEGER); //19 - IN _motive INT, 
            statement.setNull(20, Types.BIT); //20 - IN _inactive BIT
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				final CompanyMovementExecutionItemDTO item = new CompanyMovementExecutionItemDTO();
				item.setId(resultSet.getLong(DatabaseConstants.COLUMN_ID));
				item.setCompany(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION));
				item.setCompanyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID));
				item.setExpiryData(resultSet.getString(DatabaseConstants.COLUMN_EXPIRY_DATA));
				item.setExecutedData(resultSet.getString(DatabaseConstants.COLUMN_EXECUTION_DATE));
				item.setInactive((resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE) ? true: false));
				item.setMotive(resultSet.getString(DatabaseConstants.COLUMN_MOTIVE));
				item.setDocumentNote(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE));
				item.setDocumentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID));
				companyMovementExecution.addItem(item);
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
	
	public boolean updateCompanyMovementExecution(final CompanyMovementExecutionItemDTO companyMovementExecutionItem) throws Exception 
	{
		CallableStatement statement = null;
		
		final StringBuilder query = new StringBuilder();	
		query.append("{");
		query.append("call pr_company_movement_execution");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _date_from DATETIME,
		query.append("?, "); //03 - IN _date_to DATETIME,
		query.append("?, "); //04 - IN _company_ids VARCHAR(500),
		query.append("?, "); //05 - IN _field_id BIGINT(20),
		query.append("?, "); //06 - IN _field_name VARCHAR(20),
		query.append("?, "); //07 - IN _field_value VARCHAR(500)
		query.append("?, "); //08 - IN _company_id BIGINT(20),
		query.append("?, "); //09 - IN _document_parent_id BIGINT(20),
		query.append("?, "); //10 - IN _execution_date DATETIME,
		query.append("?, "); //11 - IN _expirationDate DATETIME,
		query.append("?, "); //12 - IN _bank_account_origin_id BIGINT
		query.append("?, "); //13 - IN _bank_account_destiny_id BIGINT,
		query.append("?, "); //14 - IN _week_year INT(11),
		query.append("?, "); //15 - IN _company_description VARCHAR(500),
		query.append("?, "); //16 - IN _user_id BIGINT,
		query.append("?, "); //17 - IN _is_execution BIT,
		query.append("?, "); //18 - IN _document_note varchar(1000),
		query.append("?, "); //19 - IN _motive INT, 
		query.append("?  ");   //20 - IN _inactive BIT
		query.append(")");
		query.append("}");
		
		boolean isSaved = false;
		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - _operation INT,
			statement.setNull(2,Types.DATE); //02 - IN _date_from DATETIME,
			statement.setNull(3,Types.DATE); //03 - IN _date_to DATETIME,
			statement.setNull(4, Types.VARCHAR); //04 - IN _company_ids VARCHAR(500),
			statement.setLong(5, companyMovementExecutionItem.getFildId()); //05 - IN _field_id BIGINT(20),
			statement.setString(6, companyMovementExecutionItem.getFildName()); //06 - IN _field_name VARCHAR(20),
			statement.setString(7, companyMovementExecutionItem.getFildValue()); //07 - IN _field_value VARCHAR(500),
			statement.setNull(8, Types.BIGINT); //08 - IN _company_id BIGINT(20),
			statement.setNull(9, Types.BIGINT); //09 - IN _document_parent_id BIGINT(20),
			statement.setNull(10, Types.DATE); //10 - IN _execution_date DATETIME,
			statement.setNull(11, Types.DATE); // 11 - IN _expirationDate DATETIME
			statement.setNull(12, Types.BIGINT); //12 - IN _bank_account_origin_id BIGINT
            statement.setNull(13, Types.BIGINT); //13 - IN _bank_account_destiny_id BIGINT,
            statement.setNull(14, Types.INTEGER); //14 - IN _week_year INT(11),
            statement.setNull(15, Types.VARCHAR); //15 - IN _company_description VARCHAR(500),
            statement.setNull(16, Types.BIGINT); //16 - IN _user_id BIGINT,
            statement.setNull(17, Types.BIT); //17 - IN _is_execution BIT,
            statement.setNull(18, Types.VARCHAR); //18 - IN _document_note varchar(1000),
            statement.setNull(19, Types.INTEGER); //19 - IN _motive INT, 
            statement.setNull(20, Types.BIT); //20 - IN _inactive BIT
            
			
			isSaved = (statement.executeUpdate()>0);
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
		
		return isSaved;
	}
	
	public boolean updateMovementMotive(final long companyId, final boolean isExecution) throws Exception 
	{
		CallableStatement statement = null;
		
		final StringBuilder query = new StringBuilder();	
		query.append("{");
		query.append("call pr_company_movement_execution");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _date_from DATETIME,
		query.append("?, "); //03 - IN _date_to DATETIME,
		query.append("?, "); //04 - IN _company_ids VARCHAR(500),
		query.append("?, "); //05 - IN _field_id BIGINT(20),
		query.append("?, "); //06 - IN _field_name VARCHAR(20),
		query.append("?, "); //07 - IN _field_value VARCHAR(500)
		query.append("?, "); //08 - IN _company_id BIGINT(20),
		query.append("?, "); //09 - IN _document_parent_id BIGINT(20),
		query.append("?, "); //10 - IN _execution_date DATETIME,
		query.append("?, "); //11 - IN _expirationDate DATETIME,
		query.append("?, "); //12 - IN _bank_account_origin_id BIGINT
		query.append("?, "); //13 - IN _bank_account_destiny_id BIGINT,
		query.append("?, "); //14 - IN _week_year INT(11),
		query.append("?, "); //15 - IN _company_description VARCHAR(500),
		query.append("?, "); //16 - IN _user_id BIGINT,
		query.append("?, "); //17 - IN _is_execution BIT,
		query.append("?, "); //18 - IN _document_note varchar(1000),
		query.append("?, "); //19 - IN _motive INT, 
		query.append("?  ");   //20 - IN _inactive BIT
		query.append(")");
		query.append("}");
		
		boolean isSaved = false;
		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.UPDATE_MOTIVE_NEGATIVE.getType()); //01 - _operation INT,
			statement.setNull(2,Types.DATE); //02 - IN _date_from DATETIME,
			statement.setNull(3,Types.DATE); //03 - IN _date_to DATETIME,
			statement.setNull(4, Types.VARCHAR); //04 - IN _company_ids VARCHAR(500),
			statement.setNull(5, Types.VARCHAR); //05 - IN _field_id BIGINT(20),
			statement.setNull(6, Types.VARCHAR); //06 - IN _field_name VARCHAR(20),
			statement.setNull(7, Types.VARCHAR); //07 - IN _field_value VARCHAR(500),
			statement.setLong(8, companyId); //08 - IN _company_id BIGINT(20),
			statement.setNull(9, Types.BIGINT); //09 - IN _document_parent_id BIGINT(20),
			statement.setNull(10, Types.DATE); //10 - IN _execution_date DATETIME,
			statement.setNull(11, Types.DATE); // 11 - IN _expirationDate DATETIME
			statement.setNull(12, Types.INTEGER); //12 - IN _bank_account_origin_id BIGINT
            statement.setNull(13, Types.BIGINT); //13 - IN _bank_account_destiny_id BIGINT,
            statement.setNull(14, Types.INTEGER); //14 - IN _week_year INT(11),
            statement.setNull(15, Types.VARCHAR); //15 - IN _company_description VARCHAR(500),
            statement.setNull(16, Types.BIGINT); //16 - IN _user_id BIGINT,
            statement.setBoolean(17, isExecution); //17 - IN _is_execution BIT,
            statement.setNull(18, Types.VARCHAR); //18 - IN _document_note varchar(1000),
            statement.setNull(19, Types.INTEGER); //19 - IN _motive INT, 
            statement.setNull(20, Types.BIT); //20 - IN _inactive BIT
            
			
			isSaved = (statement.executeUpdate()>0);
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
		
		return isSaved;
	}
}