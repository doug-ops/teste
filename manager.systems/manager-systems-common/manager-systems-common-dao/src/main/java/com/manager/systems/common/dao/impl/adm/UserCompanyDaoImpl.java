package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.adm.UserCompanyDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.CompanyDTO;
import com.manager.systems.common.dto.adm.UserCompnayDTO;
import com.manager.systems.common.vo.OperationType;

public class UserCompanyDaoImpl implements UserCompanyDao 
{
	private Connection connection;
	
	public UserCompanyDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}

	@Override
	public boolean save(final UserCompnayDTO userCompnay) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_user_company");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _user_id BIGINT,
		query.append("?, "); //03 - IN _company_id BIGINT,
		query.append("?, "); //04 - IN _inactive int, 
		query.append("?  "); //05 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
			statement.setLong(2, userCompnay.getUserId()); //02 - IN _user_id BIGINT,
			statement.setLong(3, userCompnay.getCompanyId()); //03 - IN _company_id BIGINT,
			statement.setInt(4, userCompnay.getInactiveInt()); //04 - IN _inactive int, 
			statement.setLong(5, userCompnay.getUserChange()); //05 - IN _user_change BIGINT			
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
	public boolean inactive(final UserCompnayDTO userCompnay) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_user_company");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _user_id BIGINT,
		query.append("?, "); //03 - IN _company_id BIGINT,
		query.append("?, "); //04 - IN _inactive int, 
		query.append("?  "); //05 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			if(userCompnay.getUserId() > 0) {
				statement.setInt(1, OperationType.INACTIVE.getType()); //01 - IN _operation INT,				
			} else {
				statement.setInt(1, 6); //01 - IN _operation INT,
			}
			statement.setLong(2, userCompnay.getUserId()); //02 - IN _user_id BIGINT,
			statement.setLong(3, userCompnay.getCompanyId()); //03 - IN _company_id BIGINT,
			statement.setInt(4, userCompnay.getInactiveInt()); //04 - IN _inactive int, 
			statement.setLong(5, userCompnay.getUserChange()); //05 - IN _user_change BIGINT			
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
	public List<CompanyDTO> get(final long userId) throws Exception 
	{
		final List<CompanyDTO> itens = new ArrayList<CompanyDTO>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_user_company");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _user_id BIGINT,
		query.append("?, "); //03 - IN _company_id BIGINT,
		query.append("?, "); //04 - IN _inactive int, 
		query.append("?  "); //05 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); //01 - IN _operation INT,
			statement.setLong(2, userId); //02 - IN _user_id BIGINT,        
			statement.setNull(3, Types.BIGINT); //03 - IN _company_id BIGINT,
			statement.setNull(4, Types.BIGINT); //04 - IN _inactive int, 
			statement.setNull(5, Types.BIGINT); //05 - IN _user_change BIGINT		
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final CompanyDTO item = new CompanyDTO();
	        	item.setId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID));
	        	item.setDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION));
	        	item.setExpiryDataString(resultSet.getString(DatabaseConstants.COLUMN_EXPIRY_DATA_STRING));
	        	item.setPersonTypeId(resultSet.getInt(DatabaseConstants.COLUMN_PERSON_TYPE_ID));
	        	item.setPersonTypeDescription(resultSet.getString(DatabaseConstants.COLUMN_PERSON_TYPE_DESCRIPTION));
	        	item.setProcessMovementAutomatic(resultSet.getBoolean(DatabaseConstants.COLUMN_COMPANY_PROCESS_MOVEMENT_AUTOMATIC));
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
	public List<UserCompnayDTO> getUsersCompany(final long companyId) throws Exception 
	{
		final List<UserCompnayDTO> itens = new ArrayList<UserCompnayDTO>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_user_company");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _user_id BIGINT,
		query.append("?, "); //03 - IN _company_id BIGINT,
		query.append("?, "); //04 - IN _inactive int, 
		query.append("?  "); //05 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL_USERS_COMPANY.getType()); //01 - IN _operation INT,
			statement.setNull(2, Types.BIGINT); //02 - IN _user_id BIGINT,        
			statement.setLong(3, companyId); //03 - IN _company_id BIGINT,
			statement.setNull(4, Types.BIGINT); //04 - IN _inactive int, 
			statement.setNull(5, Types.BIGINT); //05 - IN _user_change BIGINT		
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final UserCompnayDTO item = new UserCompnayDTO();
	        	item.setUserId(resultSet.getLong(DatabaseConstants.COLUMN_USER_ID));
	        	item.setCompanyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID));
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
	public List<CompanyDTO> getDataMovementExecutionCompany(final String usersId) throws Exception 
	{
		final List<CompanyDTO> itens = new ArrayList<CompanyDTO>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_user_company");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _user_id BIGINT,
		query.append("?, "); //03 - IN _company_id BIGINT,
		query.append("?, "); //04 - IN _inactive int, 
		query.append("?  "); //05 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - IN _operation INT,
			statement.setString(2, usersId); //02 - IN _user_id VARCHAR(500),
			statement.setNull(3, Types.BIGINT); //03 - IN _company_id BIGINT,
			statement.setNull(4, Types.BIGINT); //04 - IN _inactive int, 
			statement.setNull(5, Types.BIGINT); //05 - IN _user_change BIGINT			
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final CompanyDTO item = new CompanyDTO();
	        	item.setId(resultSet.getInt(DatabaseConstants.COLUMN_COMPANY_ID));
	        	item.setDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION));
	        	item.setBankAccountOriginId(resultSet.getLong(DatabaseConstants.COLUMN_BANK_ACCOUNT_ORIGIN_ID));
	        	item.setBankAccountDestinyId(resultSet.getLong(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESTINY_ID));
	        	item.setWeekYear(resultSet.getInt(DatabaseConstants.COLUMN_COUNT_WEEK_YEAR));
	        	item.setExpiryDataString(resultSet.getString(DatabaseConstants.COLUMN_EXPIRY_DATA_STRING));
	        	item.setExecutionDate(resultSet.getString(DatabaseConstants.COLUMN_EXECUTION_DATE));
	        	item.setBackgroundColor(resultSet.getString(DatabaseConstants.COLUMN_BACKGROUND_COLOR));
	        	item.setPersonTypeId(resultSet.getInt(DatabaseConstants.COLUMN_PERSON_TYPE_ID));
	        	item.setPersonTypeDescription(resultSet.getString(DatabaseConstants.COLUMN_PERSON_TYPE_DESCRIPTION));
	        	item.setProcessMovementAutomatic(resultSet.getBoolean(DatabaseConstants.COLUMN_COMPANY_PROCESS_MOVEMENT_AUTOMATIC));
	        	item.setDocumentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID));
	        	item.setPaymentDate(resultSet.getString(DatabaseConstants.COLUMN_PAYMENT_DATA));
	        	item.setRegisterOrder(resultSet.getInt(DatabaseConstants.COLUMN_REGISTER_ORDER));
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
}