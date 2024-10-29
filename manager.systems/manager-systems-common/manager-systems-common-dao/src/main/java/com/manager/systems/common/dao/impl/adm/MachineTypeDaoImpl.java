package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.adm.MachineTypeDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.MachineTypeDTO;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.OperationType;

public class MachineTypeDaoImpl implements MachineTypeDao 
{
	private Connection connection;
	
	public MachineTypeDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}

	@Override
	public List<Combobox> getAllCombobox() throws Exception 
	{
		final List<Combobox> items = new ArrayList<Combobox>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_machine_type");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _description VARCHAR(50),
		query.append("?, "); //03 - IN _inactive INT, 
		query.append("? "); //04 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		try
		{			
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL_COMBOBOX.getType()); //01 - IN _operation INT,
			statement.setNull(2, Types.INTEGER); //02 - IN _description VARCHAR(50),
			statement.setNull(3, Types.VARCHAR); //03 - IN _inactive INT, 
			statement.setNull(4, Types.BIGINT); //04 -  IN _user_change BIGINT
	        
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
	public Integer save(final MachineTypeDTO machineType) throws Exception 
	{
		ResultSet resultSet = null;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		Integer machineTypeId = 0;
		
		query.append("{");
		query.append("call pr_machine_type");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _description VARCHAR(50),
		query.append("?, "); //03 - IN _inactive INT, 
		query.append("? "); //04 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
			statement.setString(2, machineType.getDescription()); //02 - IN _description varchar(100), 
			statement.setInt(3, machineType.getInactiveInt()); //03 - IN _inactive int, 
			statement.setLong(4, machineType.getChangeData().getUserChange()); //04 - IN _user_change BIGINT,
			final boolean isProcessed  = statement.execute();
	        
	    	if(isProcessed) {
	    		resultSet = statement.getResultSet();
				while (resultSet.next()) {
					machineTypeId = resultSet.getInt(DatabaseConstants.COLUMN_MACHINE_TYPE_ID);
				}
			}
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
		return machineTypeId;
	}
}