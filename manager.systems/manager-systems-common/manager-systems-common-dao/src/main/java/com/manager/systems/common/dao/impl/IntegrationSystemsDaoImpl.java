package com.manager.systems.common.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.IntegrationSystemsDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.IntegrationSystemsDTO;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.OperationType;

public class IntegrationSystemsDaoImpl implements IntegrationSystemsDao 
{
	private Connection connection;
	
	public IntegrationSystemsDaoImpl(final Connection connection) 
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
		query.append("call pr_maintenance_integration_systems");
		query.append("(");
		query.append("?, "); //01 - operation
		query.append("?, "); //02 - IN _object_id BIGINT
		query.append("?, "); //03 - IN _object_type VARCHAR(1), 
		query.append("?, "); //04 - IN integration_system_id INT, 
		query.append("?, "); //05 - IN legacy_id VARCHAR(10)
		query.append("?, "); //06 - IN _inactive INT, 
		query.append("?, "); //07 - IN _user_change BIGINT, 
		query.append("?  "); //08 - IN _change_date DATETIME		
		query.append(")");
		query.append("}");
		
				
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL_COMBOBOX.getType()); //01 - operation
			statement.setNull(2, Types.BIGINT); //02 - IN _object_id BIGINT
			statement.setNull(3, Types.VARCHAR); //03 - IN _object_type VARCHAR(1), 
			statement.setNull(4, Types.INTEGER); //04 - IN integration_system_id INT, 
			statement.setNull(5, Types.VARCHAR); //05 - IN legacy_id VARCHAR(10)
			statement.setNull(6,  Types.INTEGER); //06 - _inactive INT,
			statement.setNull(7,  Types.BIGINT); //07 - _user_change BIGINT,
			statement.setNull(8, Types.TIMESTAMP); //08 - _change_date DATETIME,

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

	@Override
	public boolean save(final IntegrationSystemsDTO integrationSystems) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_integration_systems");
		query.append("(");
		query.append("?, "); //01 - operation
		query.append("?, "); //02 - IN _object_id BIGINT
		query.append("?, "); //03 - IN _object_type VARCHAR(1), 
		query.append("?, "); //04 - IN integration_system_id INT, 
		query.append("?, "); //05 - IN legacy_id VARCHAR(10)
		query.append("?, "); //06 - IN _inactive INT, 
		query.append("?, "); //07 - IN _user_change BIGINT, 
		query.append("?  "); //08 - IN _change_date DATETIME
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - operation
			statement.setLong(2, Long.valueOf(integrationSystems.getObjectId())); //02 - IN _object_id BIGINT
			statement.setString(3, integrationSystems.getObjectType()); //03 - IN _object_type VARCHAR(10), 
			statement.setInt(4, integrationSystems.getIntegrationSystemId()); //04 - IN integration_system_id INT, 
			statement.setString(5, integrationSystems.getLegacyId()); //05 - IN legacy_id VARCHAR(10)
			statement.setInt(6,  integrationSystems.getInactive()); //06 - _inactive INT,
			statement.setLong(7,  integrationSystems.getUserChange()); //07 - _user_change BIGINT,
			statement.setTimestamp(8, new java.sql.Timestamp(integrationSystems.getChangeDate().getTimeInMillis())); //08 - _change_date DATETIME,
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
	public boolean delete(final IntegrationSystemsDTO integrationSystems) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_integration_systems");
		query.append("(");
		query.append("?, "); //01 - operation
		query.append("?, "); //02 - IN _object_id BIGINT
		query.append("?, "); //03 - IN _object_type VARCHAR(1), 
		query.append("?, "); //04 - IN integration_system_id INT, 
		query.append("?, "); //05 - IN legacy_id VARCHAR(10)
		query.append("?, "); //06 - IN _inactive INT, 
		query.append("?, "); //07 - IN _user_change BIGINT, 
		query.append("?  "); //08 - IN _change_date DATETIME
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.DELETE.getType()); //01 - operation
			statement.setLong(2, Long.valueOf(integrationSystems.getObjectId())); //02 - IN _object_id BIGINT
			statement.setString(3, integrationSystems.getObjectType()); //03 - IN _object_type VARCHAR(1), 
			statement.setInt(4, integrationSystems.getIntegrationSystemId()); //04 - IN integration_system_id INT, 
			statement.setNull(5, Types.VARCHAR); //05 - IN legacy_id VARCHAR(10)
			statement.setInt(6,  integrationSystems.getInactive()); //06 - _inactive INT,
			statement.setLong(7,  integrationSystems.getUserChange()); //07 - _user_change BIGINT,
			statement.setTimestamp(8, new java.sql.Timestamp(integrationSystems.getChangeDate().getTime().getTime())); //08 - _change_date DATETIME,
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
	public List<IntegrationSystemsDTO> getAll(final IntegrationSystemsDTO integrationSystems) throws Exception 
	{
		final List<IntegrationSystemsDTO> itens = new ArrayList<IntegrationSystemsDTO>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_integration_systems");
		query.append("(");
		query.append("?, "); //01 - operation
		query.append("?, "); //02 - IN _object_id BIGINT
		query.append("?, "); //03 - IN _object_type VARCHAR(1), 
		query.append("?, "); //04 - IN integration_system_id INT, 
		query.append("?, "); //05 - IN legacy_id VARCHAR(10)
		query.append("?, "); //06 - IN _inactive INT, 
		query.append("?, "); //07 - IN _user_change BIGINT, 
		query.append("?  "); //08 - IN _change_date DATETIME
		query.append(")");
		query.append("}");
		
				
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - operation
			statement.setLong(2, Long.valueOf(integrationSystems.getObjectId())); //02 - IN _object_id BIGINT
			statement.setString(3, integrationSystems.getObjectType()); //03 - IN _object_type VARCHAR(1), 
			statement.setNull(4, Types.INTEGER); //04 - IN integration_system_id INT, 
			statement.setNull(5, Types.VARCHAR); //05 - IN legacy_id VARCHAR(10)
			statement.setInt(6,  integrationSystems.getInactive()); //06 - _inactive INT,
			statement.setNull(7,  Types.BIGINT); //07 - _user_change BIGINT,
			statement.setNull(8, Types.TIMESTAMP); //08 - _change_date DATETIME,
	        resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final IntegrationSystemsDTO item = new IntegrationSystemsDTO();
	        	item.setLegacyId(resultSet.getString(DatabaseConstants.COLUMN_LEGACY_ID));
	        	item.setIntegrationSystemId(resultSet.getInt(DatabaseConstants.COLUMN_INTEGRATION_SYSTEM_ID));
	        	item.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
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