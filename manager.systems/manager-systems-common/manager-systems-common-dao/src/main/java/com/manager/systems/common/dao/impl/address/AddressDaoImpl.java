package com.manager.systems.common.dao.impl.address;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.manager.systems.common.dao.address.AddressDao;
import com.manager.systems.common.vo.Combobox;

public class AddressDaoImpl implements AddressDao 
{
	private Connection connection;
	
	public AddressDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}

	@Override
	public List<Combobox> getAllStates() throws Exception 
	{
		final List<Combobox> itens = new ArrayList<Combobox>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_address");
		query.append("(");
		query.append("? "); //01 - operation
		query.append(")");
		query.append("}");
		
				
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 1);
			
	        resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final String key = resultSet.getString("address_state_ibge");
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
	public Map<String, Combobox> getAllStatesMap() throws Exception 
	{
		final Map<String, Combobox> itens = new TreeMap<String, Combobox>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_address");
		query.append("(");
		query.append("? "); //01 - operation
		query.append(")");
		query.append("}");
		
				
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 1);
			
	        resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final String key = resultSet.getString("state_acronym");
	        	final String key2 = resultSet.getString("address_state_ibge");
	        	final String value = resultSet.getString("description");
	        	itens.put(key, new Combobox(key2, value));
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
	public List<Combobox> getAllCountry() throws Exception 
	{
		final List<Combobox> itens = new ArrayList<Combobox>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_address");
		query.append("(");
		query.append("? "); //01 - operation
		query.append(")");
		query.append("}");
		
				
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 2);
			
	        resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final String key = resultSet.getString("address_country_ibge");
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