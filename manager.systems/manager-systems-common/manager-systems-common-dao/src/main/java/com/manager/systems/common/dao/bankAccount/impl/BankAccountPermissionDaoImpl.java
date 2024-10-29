/*
 * Date create 21/06/2024.
 */
package com.manager.systems.common.dao.bankAccount.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.bankAccount.BankAccountPermissionDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.vo.Combobox;

public class BankAccountPermissionDaoImpl implements BankAccountPermissionDao {
	
	private Connection connection;
	
	public BankAccountPermissionDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public List<Combobox> getBankAccountPermissionsByUser(final long userId) throws Exception {
		final List<Combobox> itens = new ArrayList<Combobox>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_person_bank_account_permission");
		query.append("(");
		query.append("?, "); 	//01 - IN _operation TINYINT
		query.append("?  "); 	//02 - IN _user_id BIGINT		
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;

		try 
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 4); //01 - IN _operation TINYINT
			statement.setLong(2, userId); //02 - IN _user_id BIGINT
			resultSet = statement.executeQuery();
			
			while (resultSet.next())
			{
				itens.add(new Combobox(resultSet.getString(DatabaseConstants.COLUMN_ID), resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION), resultSet.getString(DatabaseConstants.COLUMN_COMPANY_ID)));
			}
		}
		catch (final Exception e) 
		{
			throw e;
		}
		finally 
		{
			if(resultSet != null)
			{
				resultSet.close();
			}
			if(statement != null)
			{
				statement.close();
			}
		}
		
		return itens;
	}

}
