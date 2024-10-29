package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.adm.UserParentDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.UserDTO;
import com.manager.systems.common.dto.adm.UserParentDTO;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.OperationType;

public class UserParentDaoImpl implements UserParentDao 
{
	private Connection connection;
	
	public UserParentDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}

	@Override
	public boolean save(final UserParentDTO userParent) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_user_parent");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _user_id BIGINT,
		query.append("?, "); //03 - IN _user_parent_id BIGINT,
		query.append("?, "); //04 - IN _inactive INT, 
		query.append("?  "); //05 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
			statement.setLong(2, userParent.getUserId()); //02 - IN _user_id BIGINT,
			statement.setLong(3, userParent.getUserParentId()); //03 - IN _user_parent_id BIGINT,
			statement.setInt(4, userParent.getInactiveInt()); //04 - IN _inactive INT, 
			statement.setLong(5, userParent.getUserChange()); //05 - IN _user_change BIGINT			
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
	public boolean inactive(final UserParentDTO userParent) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_user_parent");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _user_id BIGINT,
		query.append("?, "); //03 - IN _user_parent_id BIGINT,
		query.append("?, "); //04 - IN _inactive INT, 
		query.append("?  "); //05 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.INACTIVE.getType()); //01 - IN _operation INT,
			statement.setLong(2, userParent.getUserId()); //02 - IN _user_id BIGINT,
			statement.setLong(3, userParent.getUserParentId()); //03 - IN _user_parent_id BIGINT,
			statement.setInt(4, userParent.getInactiveInt()); //04 - IN _inactive INT, 
			statement.setLong(5, userParent.getUserChange()); //05 - IN _user_change BIGINT			
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
	public List<UserDTO> get(final long userId) throws Exception 
	{
		final List<UserDTO> itens = new ArrayList<UserDTO>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_user_parent");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _user_id BIGINT,
		query.append("?, "); //03 - IN _user_parent_id BIGINT,
		query.append("?, "); //04 - IN _inactive INT, 
		query.append("?  "); //05 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); //01 - IN _operation INT,
			statement.setNull(2, Types.BIGINT); //02 - IN _user_id BIGINT,        
			statement.setLong(3, userId); //03 - IN _user_parent_id BIGINT,
			statement.setNull(4, Types.BIGINT); //04 - IN _inactive INT, 
			statement.setNull(5, Types.BIGINT); //05 - IN _user_change BIGINT		
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final UserDTO item = new UserDTO();
	        	item.setId(resultSet.getInt(DatabaseConstants.COLUMN_USER_ID));
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
	
	public List<Combobox> getAllParentCombobox(long userId) throws Exception 
	{
		final List<Combobox> items = new ArrayList<Combobox>();
		int inactive=0;
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
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - IN _operation INT,
			statement.setLong(2, Long.valueOf(userId)); //02 - _id BIGINT,
			statement.setNull(3, Types.BIGINT); //03 - _person_id BIGINT,
			statement.setNull(4, Types.VARCHAR); //04 - _name VARCHAR(100),
			statement.setNull(5, Types.VARCHAR); //05 - _alias_name VARCHAR(100),
			statement.setNull(6, Types.VARCHAR); //06 - _rg VARCHAR(20),
			statement.setNull(7, Types.VARCHAR); //07 - _access_data_user VARCHAR(100),
			statement.setNull(8, Types.VARCHAR); //08 - _access_data_password VARCHAR(100),
			statement.setNull(9, Types.TIMESTAMP); //09 - _expiration_date DATETIME,
			statement.setNull(10, Types.TIMESTAMP); //10 - _last_access_date DATETIME,
			statement.setNull(11, Types.TIMESTAMP); //11 - _exit_access_date DATETIME,
			statement.setInt(12, inactive); //12 - _inactive INT
			

			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	items.add(new Combobox(resultSet.getString(DatabaseConstants.COLUMN_ID), resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION)));
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
		return items;
	}
	
	@Override
	public List<Combobox> getUserParentCombobox(long userId) throws Exception 
	{
		final List<Combobox> items = new ArrayList<Combobox>();
		
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
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); //01 - IN _operation INT,
			statement.setLong(2, Long.valueOf(userId)); //02 - _id BIGINT,
			statement.setNull(3, Types.BIGINT); //03 - _person_id BIGINT,
			statement.setNull(4, Types.VARCHAR); //04 - _name VARCHAR(100),
			statement.setNull(5, Types.VARCHAR); //05 - _alias_name VARCHAR(100),
			statement.setNull(6, Types.VARCHAR); //06 - _rg VARCHAR(20),
			statement.setNull(7, Types.VARCHAR); //07 - _access_data_user VARCHAR(100),
			statement.setNull(8, Types.VARCHAR); //08 - _access_data_password VARCHAR(100),
			statement.setNull(9, Types.TIMESTAMP); //09 - _expiration_date DATETIME,
			statement.setNull(10, Types.TIMESTAMP); //10 - _last_access_date DATETIME,
			statement.setNull(11, Types.TIMESTAMP); //11 - _exit_access_date DATETIME,
			statement.setNull(12, Types.INTEGER); //12 - _inactive INT
			

			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	items.add(new Combobox(resultSet.getString(DatabaseConstants.COLUMN_ID), resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION)));
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
		return items;
	}
	
	@Override
	public List<Combobox> getUserComboboxByClient(long userId) throws Exception 
	{
		final List<Combobox> items = new ArrayList<Combobox>();
		
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
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 5); //01 - IN _operation INT,
			statement.setLong(2, Long.valueOf(userId)); //02 - _id BIGINT,
			statement.setNull(3, Types.BIGINT); //03 - _person_id BIGINT,
			statement.setNull(4, Types.VARCHAR); //04 - _name VARCHAR(100),
			statement.setNull(5, Types.VARCHAR); //05 - _alias_name VARCHAR(100),
			statement.setNull(6, Types.VARCHAR); //06 - _rg VARCHAR(20),
			statement.setNull(7, Types.VARCHAR); //07 - _access_data_user VARCHAR(100),
			statement.setNull(8, Types.VARCHAR); //08 - _access_data_password VARCHAR(100),
			statement.setNull(9, Types.TIMESTAMP); //09 - _expiration_date DATETIME,
			statement.setNull(10, Types.TIMESTAMP); //10 - _last_access_date DATETIME,
			statement.setNull(11, Types.TIMESTAMP); //11 - _exit_access_date DATETIME,
			statement.setNull(12, Types.INTEGER); //12 - _inactive INT
			

			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	items.add(new Combobox(resultSet.getString(DatabaseConstants.COLUMN_ID), resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION)));
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
		return items;
	}

	@Override
	public List<Combobox> getAllUserParentComboboxByOperation(final long userId, final int operation) throws Exception {
		
		final List<Combobox> items = new ArrayList<Combobox>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_user_get_combobox");
		query.append("(");
		query.append("?, "); //01 - _operation INT
		query.append("?  "); //02 - _user_id BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, operation); //01 - IN _operation INT,
			statement.setLong(2, userId); //02 - _user_id BIGINT		

			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	items.add(new Combobox(resultSet.getString(DatabaseConstants.COLUMN_ID), resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION)));
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
		return items;
	}
}