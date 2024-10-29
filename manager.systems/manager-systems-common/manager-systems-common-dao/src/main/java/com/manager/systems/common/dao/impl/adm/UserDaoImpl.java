/*
 * Date create 30/11/2021
 */
package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

import com.manager.systems.common.dao.adm.UserDao;
import com.manager.systems.common.vo.OperationType;
import com.manager.systems.model.admin.User;

public class UserDaoImpl implements UserDao 
{
	private Connection connection;
	
	public UserDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}

	@Override
	public boolean save(final User user) throws Exception 
	{	
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_user");
		query.append("(");
		query.append("?, "); //01 - _operation INT,
		query.append("?, "); //02 - _id BIGINT,
		query.append("?, "); //03 - _person_id BIGINT,
		query.append("?, "); //04 - _name VARCHAR(100),
		query.append("?, "); //05 - _alias_name VARCHAR(100),
		query.append("?, "); //06 - _rg VARCHAR(20),
		query.append("?, "); //07 - _access_data_user VARCHAR(100),
		query.append("?, "); //08 - _access_data_password VARCHAR(100),
		query.append("?, "); //09 - _expiration_date DATETIME,
		query.append("?, "); //10 - _last_access_date DATETIME,
		query.append("?, "); //11 - _exit_access_date DATETIME,
		query.append("?  "); //12 - _inactive INT
		query.append(")");
		query.append("}");
		
		boolean saveResul = false;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - _operation INT,
			statement.setLong(2, Long.valueOf(user.getId())); //02 - _id BIGINT,
			statement.setNull(3, Types.BIGINT); //03 - _person_id BIGINT,
			statement.setString(4, user.getName()); //04 - _name VARCHAR(100),
			statement.setNull(5, Types.VARCHAR); //05 - _alias_name VARCHAR(100),
			statement.setNull(6, Types.VARCHAR); //06 - _rg VARCHAR(20),
			statement.setNull(7, Types.VARCHAR); //07 - _access_data_user VARCHAR(100),
			statement.setNull(8, Types.VARCHAR); //08 - _access_data_password VARCHAR(100),
			statement.setNull(9, Types.TIMESTAMP); //09 - _expiration_date DATETIME,
			statement.setTimestamp(10, new java.sql.Timestamp(user.getUserLastAcessDate().getTime().getTime())); //10 - _last_access_date DATETIME
			statement.setNull(11, Types.TIMESTAMP); //11 - _exit_access_date DATETIME,
			statement.setNull(12, Types.INTEGER); //12 - _inactive INT
			saveResul = (statement.executeUpdate()>0);
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
		return saveResul;
	}
	
	@Override
	public boolean updatelastAcessUser(final User user) throws Exception 
	{	
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_user");
		query.append("(");
		query.append("?, "); //01 - _operation INT,
		query.append("?, "); //02 - _id BIGINT,
		query.append("?, "); //03 - _person_id BIGINT,
		query.append("?, "); //04 - _name VARCHAR(100),
		query.append("?, "); //05 - _alias_name VARCHAR(100),
		query.append("?, "); //06 - _rg VARCHAR(20),
		query.append("?, "); //07 - _access_data_user VARCHAR(100),
		query.append("?, "); //08 - _access_data_password VARCHAR(100),
		query.append("?, "); //09 - _expiration_date DATETIME,
		query.append("?, "); //10 - _last_access_date DATETIME,
		query.append("?, "); //11 - _exit_access_date DATETIME,
		query.append("?  "); //12 - _inactive INT
		query.append(")");
		query.append("}");
		
		boolean saveResul = false;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.UPDATE_LAST_ACCESS_USER.getType()); //01 - _operation INT,
			statement.setLong(2, Long.valueOf(user.getId())); //02 - _id BIGINT,
			statement.setNull(3, Types.BIGINT); //03 - _person_id BIGINT,
			statement.setString(4, user.getName()); //04 - _name VARCHAR(100),
			statement.setNull(5, Types.VARCHAR); //05 - _alias_name VARCHAR(100),
			statement.setNull(6, Types.VARCHAR); //06 - _rg VARCHAR(20),
			statement.setNull(7, Types.VARCHAR); //07 - _access_data_user VARCHAR(100),
			statement.setNull(8, Types.VARCHAR); //08 - _access_data_password VARCHAR(100),
			statement.setNull(9, Types.TIMESTAMP); //09 - _expiration_date DATETIME,
			statement.setTimestamp(10, new java.sql.Timestamp(user.getUserLastAcessDate().getTime().getTime())); //10 - _last_access_date DATETIME,
			statement.setNull(11, Types.TIMESTAMP); //11 - _exit_access_date DATETIME
			statement.setNull(12, Types.INTEGER); //12 - _inactive INT
			saveResul = (statement.executeUpdate()>0);
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
		return saveResul;
	}	
		@Override
		public boolean updateLastAccessFinishUser(final User user) throws Exception 
		{	
			CallableStatement statement = null;
			final StringBuilder query = new StringBuilder();
			
			query.append("{");
			query.append("call pr_maintenance_user");
			query.append("(");
			query.append("?, "); //01 - _operation INT,
			query.append("?, "); //02 - _id BIGINT,
			query.append("?, "); //03 - _person_id BIGINT,
			query.append("?, "); //04 - _name VARCHAR(100),
			query.append("?, "); //05 - _alias_name VARCHAR(100),
			query.append("?, "); //06 - _rg VARCHAR(20),
			query.append("?, "); //07 - _access_data_user VARCHAR(100),
			query.append("?, "); //08 - _access_data_password VARCHAR(100),
			query.append("?, "); //09 - _expiration_date DATETIME,
			query.append("?, "); //10 - _last_access_date DATETIME,
			query.append("?, "); //11 - _exit_access_date DATETIME,
			query.append("?  "); //12 - _inactive INT
			query.append(")");
			query.append("}");
			
			boolean saveResul = false;
			try
			{
				statement = this.connection.prepareCall(query.toString());
				statement.setInt(1, OperationType.UPDATE_LAST_ACCESS_FINISH_USER.getType()); //01 - _operation INT,
				statement.setLong(2, Long.valueOf(user.getId())); //02 - _id BIGINT,
				statement.setNull(3, Types.BIGINT); //03 - _person_id BIGINT,
				statement.setString(4, user.getName()); //04 - _name VARCHAR(100),
				statement.setNull(5, Types.VARCHAR); //05 - _alias_name VARCHAR(100),
				statement.setNull(6, Types.VARCHAR); //06 - _rg VARCHAR(20),
				statement.setNull(7, Types.VARCHAR); //07 - _access_data_user VARCHAR(100),
				statement.setNull(8, Types.VARCHAR); //08 - _access_data_password VARCHAR(100),
				statement.setNull(9, Types.TIMESTAMP); //09 - _expiration_date DATETIME,
				statement.setNull(10, Types.TIMESTAMP); //10 - _last_access_date DATETIME,
				statement.setTimestamp(11, new java.sql.Timestamp(user.getUserExitAcessDate().getTime().getTime())); //11 - IN _exit_access_date DATETIME
				statement.setNull(12, Types.INTEGER); //12 - _inactive INT
				saveResul = (statement.executeUpdate()>0);
				
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
			return saveResul;
		}

		@Override
		public boolean saveConfigUser(final User user) throws Exception {
			CallableStatement statement = null;
			final StringBuilder query = new StringBuilder();
				
			query.append("{? = call func_save_config_user_data(?, ?)}");
			
			boolean result = false;
			
			try
			{
				statement = this.connection.prepareCall(query.toString());
				statement.registerOutParameter(1, Types.BIT);
				statement.setLong(2, user.getId()); //01 - IN _user_id BIGINT,
				statement.setDouble(3, user.getCashClosingMaxDiscount()); //02 - IN _cash_closing_max_discount decimal(19,2)			
				
				statement.execute();
				result = statement.getBoolean(1);
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