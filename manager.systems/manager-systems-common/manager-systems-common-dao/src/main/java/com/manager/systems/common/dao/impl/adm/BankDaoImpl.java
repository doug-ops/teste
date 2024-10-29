package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.adm.BankDao;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.OperationType;

public class BankDaoImpl implements BankDao 
{
	private Connection connection;
	
	public BankDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}

	@Override
	public List<Combobox> getAllCombobox() throws Exception 
	{
		final List<Combobox> itens = new ArrayList<Combobox>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_bank");
		query.append("(");
		query.append("?, "); //01 - operation
		query.append("?, "); //02 - IN _id VARCHAR(10)
		query.append("?, "); //03 - IN _description VARCHAR(10), 
		query.append("?, "); //04 - IN _inactive INT, 
		query.append("?, "); //05 - IN _user_change BIGINT, 
		query.append("?  "); //06 - IN _change_date DATETIME		
		query.append(")");
		query.append("}");
		
				
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL_COMBOBOX.getType()); //01 - operation
			statement.setNull(2, Types.VARCHAR); ///02 - IN _id VARCHAR(10)
			statement.setNull(3, Types.VARCHAR); //03 - IN _description VARCHAR(10), 
			statement.setNull(4,  Types.INTEGER); //04 - _inactive INT,
			statement.setNull(5,  Types.BIGINT); //05 - _user_change BIGINT,
			statement.setNull(6, Types.TIMESTAMP); //06 - _change_date DATETIME,

	        resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final String key = resultSet.getString("id");
	        	final String value = resultSet.getString("description");
	        	itens.add(new Combobox(key, value));
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