package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.adm.FinancialCastCenterGroupDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.FinancialCastCenterGroupDTO;
import com.manager.systems.common.dto.adm.ReportFinancialCastCenterGroupDTO;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.OperationType;

public class FinancialCastCenterGroupDaoImpl implements FinancialCastCenterGroupDao 
{
	private Connection connection;
	
	public FinancialCastCenterGroupDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}

	@Override
	public boolean save(final FinancialCastCenterGroupDTO FinancialCastCenterGroup) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_cost_center_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _financiancial_cast_center_group_id_to int,
		query.append("?, "); //03 - IN _financiancial_cast_center_group_id_to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?  "); //06 - IN _change_user BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
			statement.setInt(2, FinancialCastCenterGroup.getId()); //02 - IN _financiancial_cast_center_group_id_from int,
			statement.setInt(3, FinancialCastCenterGroup.getId()); //03 - IN _financiancial_cast_center_group_id_to int,
			statement.setString(4, FinancialCastCenterGroup.getDescription()); //04 - IN _description varchar(100), 
			statement.setInt(5, FinancialCastCenterGroup.getInactiveInt()); //05 - IN _inactive int, 
			statement.setLong(6, FinancialCastCenterGroup.getChangeData().getUserChange()); //06 - IN _change_user BIGINT	
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
	public boolean inactive(final FinancialCastCenterGroupDTO FinancialCastCenterGroup) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_cost_center_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _financiancial_cast_center_group_id_to int,
		query.append("?, "); //03 - IN _financiancial_cast_center_group_id_to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?  "); //06 - IN _change_user BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.INACTIVE.getType()); //01 - IN _operation INT,
			statement.setInt(2, FinancialCastCenterGroup.getId()); //02 - IN _financiancial_cast_center_group_id_from int,
			statement.setInt(3, FinancialCastCenterGroup.getId()); //03 - IN _financiancial_cast_center_group_id_to int,
			statement.setNull(4, Types.VARCHAR); //04 - IN _description varchar(100), 
			statement.setInt(5, FinancialCastCenterGroup.getInactiveInt()); //05 - IN _inactive int, 
			statement.setLong(6, FinancialCastCenterGroup.getChangeData().getUserChange()); //06 - IN _change_user BIGINT	
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
	public void get(final FinancialCastCenterGroupDTO FinancialCastCenterGroup) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_cost_center_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _financiancial_cast_center_group_id_to int,
		query.append("?, "); //03 - IN _financiancial_cast_center_group_id_to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?  "); //06 - IN _change_user BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); //01 - IN _operation INT,
			statement.setInt(2, FinancialCastCenterGroup.getId()); //02 - IN _financiancial_cast_center_group_id_from int,
			statement.setInt(3, FinancialCastCenterGroup.getId()); //03 - IN _financiancial_cast_center_group_id_to int,
			statement.setNull(4, Types.VARCHAR); //04 - IN _description varchar(100), 
			statement.setNull(5, Types.INTEGER); //05 - IN _inactive int, 
			statement.setNull(6, Types.BIGINT); //06 - IN _change_user BIGINT	
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	FinancialCastCenterGroup.setId(resultSet.getInt(DatabaseConstants.COLUMN_ID));
	        	FinancialCastCenterGroup.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
	        	FinancialCastCenterGroup.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserCreation(resultSet.getLong(DatabaseConstants.COLUMN_CREATION_USER));
	        	changeData.setCreationDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CREATION_DATE));
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_CHANGE_USER));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	FinancialCastCenterGroup.setChangeData(changeData);
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
	public void getAll(final ReportFinancialCastCenterGroupDTO reportFinancialCastCenterGroup) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_cost_center_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _financiancial_cast_center_group_id_to int,
		query.append("?, "); //03 - IN _financiancial_cast_center_group_id_to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?  "); //06 - IN _change_user BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - IN _operation INT,
			statement.setInt(2, reportFinancialCastCenterGroup.getFinancialCastCenterGroupIdFrom()); //02 - IN _financiancial_cast_center_group_id_from int,
			statement.setInt(3, reportFinancialCastCenterGroup.getFinancialCastCenterGroupIdTo()); //03 - IN _financiancial_cast_center_group_id_to int,
			statement.setString(4, reportFinancialCastCenterGroup.getDescription()); //04 - IN _description varchar(100), 
			statement.setInt(5, reportFinancialCastCenterGroup.getInactive()); //05 - IN _inactive int, 
			statement.setNull(6, Types.BIGINT); //06 - IN _change_user BIGINT	
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final FinancialCastCenterGroupDTO FinancialCastCenterGroup = new FinancialCastCenterGroupDTO();
	        	FinancialCastCenterGroup.setId(resultSet.getInt(DatabaseConstants.COLUMN_ID));
	        	FinancialCastCenterGroup.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
	        	FinancialCastCenterGroup.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_CHANGE_USER));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	FinancialCastCenterGroup.setChangeData(changeData);
	        	reportFinancialCastCenterGroup.addItem(FinancialCastCenterGroup);
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
	public List<Combobox> getAllCombobox(final ReportFinancialCastCenterGroupDTO reportFinancialCastCenterGroup) throws Exception 
	{
		final List<Combobox> items = new ArrayList<Combobox>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_cost_center_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_group_id_to int,
		query.append("?, "); //03 - IN _product_group_id_to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?  "); //06 - IN _change_user BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL_COMBOBOX.getType()); //01 - IN _operation INT,
			statement.setInt(2, reportFinancialCastCenterGroup.getFinancialCastCenterGroupIdFrom()); //02 - IN _financiancial_cast_center_id_from int,
			statement.setInt(3, reportFinancialCastCenterGroup.getFinancialCastCenterGroupIdTo()); //03 - IN _financiancial_cast_center_id_to int,
			statement.setString(4, reportFinancialCastCenterGroup.getDescription()); //04 - IN _description varchar(100), 
			statement.setInt(5, reportFinancialCastCenterGroup.getInactive()); //05 - IN _inactive int, 
			statement.setNull(6, Types.BIGINT); //06 - IN _change_user BIGINT	
	        
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