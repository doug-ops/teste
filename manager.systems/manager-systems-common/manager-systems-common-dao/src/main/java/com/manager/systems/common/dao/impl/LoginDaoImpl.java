package com.manager.systems.common.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import com.manager.systems.common.dao.LoginDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.AccessProfileDTO;
import com.manager.systems.common.vo.ContactData;
import com.manager.systems.model.admin.User;

public class LoginDaoImpl implements LoginDao 
{
	private Connection connection;
	
	public LoginDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}

	@Override
	public void validate(final User user) throws Exception 
	{	
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_manager_login");
		query.append("(");
		query.append("?, "); //01 - access_data_user varchar(100)
		query.append("?  "); //02 - access_data_password varchar(100)
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setString(1, user.getAccessData().getUsername());
			statement.setString(2, user.getAccessData().getPassword());
			
	        resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	user.setId(resultSet.getLong("id"));
	        	user.setName(resultSet.getString("name"));
	        	user.setClientId(resultSet.getInt(DatabaseConstants.COLUMN_CLIENT_ID));
	        	final ContactData contactData = new ContactData();
	        	contactData.setEmail(resultSet.getString("email"));
	        	user.setContactData(contactData);
	        	final AccessProfileDTO accessProfile = new AccessProfileDTO();
	        	accessProfile.setId(resultSet.getInt("access_profile_id"));
	        	accessProfile.setDescription(resultSet.getString("description_role"));
	        	accessProfile.setRole(resultSet.getString("role"));
	        	accessProfile.setCashClosingMaxDiscount(resultSet.getDouble(DatabaseConstants.COLUMN_CASH_CLOSING_MAX_DISCOUNT));
	        	user.setAccessProfile(accessProfile);
	        	user.getAccessData().setExpirationDate(resultSet.getTimestamp("expiration_date"));
	        	user.getAccessData().setLastAcessDate(resultSet.getTimestamp("last_access_date"));
	        	user.setMainCompanyId(resultSet.getLong("main_company_id"));
	        	user.setMainCompanyDescription(resultSet.getString("main_company_description"));
	        	user.setMainCompanyBankAccountId(resultSet.getInt("main_company_bank_account_id"));
	        	user.setMainCompanyBankAccountDescription(resultSet.getString("main_company_bank_account_description"));
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
}