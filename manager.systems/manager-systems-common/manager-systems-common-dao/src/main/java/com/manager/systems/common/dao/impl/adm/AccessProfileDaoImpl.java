package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.adm.AccessProfileDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.AccessProfileDTO;
import com.manager.systems.common.dto.adm.ReportAccessProfileDTO;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.OperationType;

public class AccessProfileDaoImpl implements AccessProfileDao 
{
	private Connection connection;
	
	public AccessProfileDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}

	@Override
	public boolean save(final AccessProfileDTO accessProfile) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_access_profile");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _id_to int,
		query.append("?, "); //03 - IN __to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _role varchar(50), 
		query.append("?, "); //06 - IN _inactive int, 
		query.append("?  "); //07 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
			statement.setInt(2, accessProfile.getId()); //02 - IN _id_from int,
			statement.setInt(3, accessProfile.getId()); //03 - IN _id_to int,
			statement.setString(4, accessProfile.getDescription()); //04 - IN _description varchar(100),
			statement.setString(5, accessProfile.getRole()); //05 - IN _role varchar(50),
			statement.setInt(6, accessProfile.getInactiveInt()); //05 - IN _inactive int, 
			statement.setLong(7, accessProfile.getChangeData().getUserChange()); //06 - IN _user_change BIGINT			
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
	public boolean inactive(final AccessProfileDTO accessProfile) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_access_profile");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _id_to int,
		query.append("?, "); //03 - IN __to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _role varchar(50), 
		query.append("?, "); //06 - IN _inactive int, 
		query.append("?  "); //07 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.INACTIVE.getType()); //01 - IN _operation INT,
			statement.setInt(2, accessProfile.getId()); //02 - IN _id_from int,
			statement.setInt(3, accessProfile.getId()); //03 - IN _id_to int,
			statement.setNull(4, Types.VARCHAR); //04 - IN _description varchar(100), 
			statement.setNull(5, Types.VARCHAR); //05 - IN _role varchar(50),
			statement.setInt(6, accessProfile.getInactiveInt()); //05 - IN _inactive int, 
			statement.setLong(7, accessProfile.getChangeData().getUserChange()); //06 - IN _user_change BIGINT			
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
	public void get(final AccessProfileDTO accessProfile) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_access_profile");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _id_to int,
		query.append("?, "); //03 - IN __to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _role varchar(50), 
		query.append("?, "); //06 - IN _inactive int, 
		query.append("?  "); //07 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); //01 - IN _operation INT,
			statement.setInt(2, accessProfile.getId()); //02 - IN _id_from int,
			statement.setInt(3, accessProfile.getId()); //03 - IN _id_to int,
			statement.setNull(4, Types.VARCHAR); //04 - IN _description varchar(100), 
			statement.setNull(5, Types.VARCHAR); //05 - IN _role varchar(50),
			statement.setNull(6, Types.INTEGER); //05 - IN _inactive int, 
			statement.setNull(7, Types.BIGINT); //06 - IN _user_change BIGINT			
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	accessProfile.setId(resultSet.getInt(DatabaseConstants.COLUMN_ID));
	        	accessProfile.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
	        	accessProfile.setRole(resultSet.getString(DatabaseConstants.COLUMN_ROLE));
	        	accessProfile.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	accessProfile.setCashClosingMaxDiscount(resultSet.getDouble(DatabaseConstants.COLUMN_CASH_CLOSING_MAX_DISCOUNT));
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserCreation(resultSet.getLong(DatabaseConstants.COLUMN_USER_CREATION));
	        	changeData.setCreationDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CREATION_DATE));
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	accessProfile.setChangeData(changeData);
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
	public void getAll(final ReportAccessProfileDTO reportAccessProfile) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_access_profile");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _id_to int,
		query.append("?, "); //03 - IN __to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _role varchar(50), 
		query.append("?, "); //06 - IN _inactive int, 
		query.append("?  "); //07 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - IN _operation INT,
			statement.setInt(2, reportAccessProfile.getIdFrom()); //02 - IN _product_group_id_from int,
			statement.setInt(3, reportAccessProfile.getIdTo()); //03 - IN _product_group_id_to int,
			statement.setString(4, reportAccessProfile.getDescription()); //04 - IN _description varchar(100),
			statement.setNull(5, Types.VARCHAR); //05 - IN _role varchar(50),
			statement.setInt(6, reportAccessProfile.getInactive()); //05 - IN _inactive int, 
			statement.setNull(7, Types.BIGINT); //06 - IN _user_change BIGINT			
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final AccessProfileDTO item = new AccessProfileDTO();
	        	item.setId(resultSet.getInt(DatabaseConstants.COLUMN_ID));
	        	item.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
	        	item.setRole(resultSet.getString(DatabaseConstants.COLUMN_ROLE));
	        	item.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	item.setChangeData(changeData);
	        	reportAccessProfile.addItem(item);
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
	public List<Combobox> getAllCombobox(final ReportAccessProfileDTO reportAccessProfile) throws Exception 
	{
		final List<Combobox> items = new ArrayList<Combobox>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_access_profile");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _id_to int,
		query.append("?, "); //03 - IN __to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _role varchar(50), 
		query.append("?, "); //06 - IN _inactive int, 
		query.append("?  "); //07 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL_COMBOBOX.getType()); //01 - IN _operation INT,
			statement.setInt(2, reportAccessProfile.getIdFrom()); //02 - IN _id_from int,
			statement.setInt(3, reportAccessProfile.getIdTo()); //03 - IN _id_to int,
			statement.setString(4, reportAccessProfile.getDescription()); //04 - IN _description varchar(100),
			statement.setNull(5, Types.VARCHAR); //05 - IN _role varchar(50),
			statement.setString(5, reportAccessProfile.getDescription()); //04 - IN _description varchar(100), 	
			statement.setInt(6, reportAccessProfile.getInactive()); //05 - IN _inactive int, 
			statement.setNull(7, Types.BIGINT); //06 - IN _user_change BIGINT			
	        
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
	public boolean saveConfig(final AccessProfileDTO accessProfile) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_access_profile_config");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _access_profile_id int,
		query.append("?, "); //03 - IN _cash_closing_max_discount decimal(19,2), 
		query.append("?, "); //04 - IN _inactive int, 
		query.append("?  "); //05 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
			statement.setInt(2, accessProfile.getId()); //02 - IN _access_profile_id int,
			statement.setDouble(3, accessProfile.getCashClosingMaxDiscount()); //03 - IN _cash_closing_max_discount decimal(19,2), 
			statement.setInt(4, accessProfile.getInactiveInt()); //05 - IN _inactive int, 
			statement.setLong(5, accessProfile.getChangeData().getUserChange()); //06 - IN _user_change BIGINT			
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
}