package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.adm.FinancialGroupDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.FinancialGroupDTO;
import com.manager.systems.common.dto.adm.FinancialSubGroupDTO;
import com.manager.systems.common.dto.adm.ReportFinancialGroupDTO;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.OperationType;

public class FinancialGroupDaoImpl implements FinancialGroupDao 
{
	private Connection connection;
	
	public FinancialGroupDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}

	@Override
	public boolean save(final FinancialGroupDTO financialGroup) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _financial_group_id_to VARCHAR(7),
		query.append("?, "); //03 - IN _financial_group_id_to VARCHAR(7),
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?  "); //06 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
			statement.setString(2, financialGroup.getId()); //02 - IN _financial_group_id_from VARCHAR(7),
			statement.setString(3, financialGroup.getId()); //03 - IN _financial_group_id_to VARCHAR(7),
			statement.setString(4, financialGroup.getDescription()); //04 - IN _description varchar(100), 
			statement.setInt(5, financialGroup.getInactiveInt()); //05 - IN _inactive int, 
			statement.setLong(6, financialGroup.getChangeData().getUserChange()); //06 - IN _user_change BIGINT			
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
	public boolean inactive(final FinancialGroupDTO financialGroup) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _financial_group_id_to int,
		query.append("?, "); //03 - IN _financial_group_id_to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?  "); //06 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.INACTIVE.getType()); //01 - IN _operation INT,
			statement.setString(2, financialGroup.getId()); //02 - IN _financial_group_id_from int,
			statement.setString(3, financialGroup.getId()); //03 - IN _financial_group_id_to int,
			statement.setNull(4, Types.VARCHAR); //04 - IN _description varchar(100), 
			statement.setInt(5, financialGroup.getInactiveInt()); //05 - IN _inactive int, 
			statement.setLong(6, financialGroup.getChangeData().getUserChange()); //06 - IN _user_change BIGINT			
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
	public void get(final FinancialGroupDTO financialGroup) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _financial_group_id_to int,
		query.append("?, "); //03 - IN _financial_group_id_to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?  "); //06 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); //01 - IN _operation INT,
			statement.setString(2, financialGroup.getId()); //02 - IN _financial_group_id_from int,
			statement.setString(3, financialGroup.getId()); //03 - IN _financial_group_id_to int,
			statement.setNull(4, Types.VARCHAR); //04 - IN _description varchar(100), 
			statement.setNull(5, Types.INTEGER); //05 - IN _inactive int, 
			statement.setNull(6, Types.BIGINT); //06 - IN _user_change BIGINT			
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	financialGroup.setId(resultSet.getString(DatabaseConstants.COLUMN_ID));
	        	financialGroup.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
	        	financialGroup.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserCreation(resultSet.getLong(DatabaseConstants.COLUMN_USER_CREATION));
	        	changeData.setCreationDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CREATION_DATE));
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	financialGroup.setChangeData(changeData);
	        	final String subGroupId = resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_ID);
	        	if(!StringUtils.isNull(subGroupId)) {
		        	final FinancialSubGroupDTO financialSubGroup = new FinancialSubGroupDTO();
		        	financialSubGroup.setId(subGroupId);
		        	financialSubGroup.setDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_DESCRIPTION));
		        	financialSubGroup.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_INACTIVE_SUB_GROUP_DESCRIPTION));
		        	financialSubGroup.setRevenueSource(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_REVENUE_SOURCE_ID));
		        	financialSubGroup.setRevenueSourceDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_REVENUE_SOURCE));
		        	financialSubGroup.setRevenueType(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_REVENUE_TYPE_ID));
		        	financialSubGroup.setRevenueTypeDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_REVENUE_TYPE));	
		        	financialGroup.addSubGroup(financialSubGroup);
	        	}	        	
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
	public void getAll(final ReportFinancialGroupDTO reportFinancialGroup) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _financial_group_id_to int,
		query.append("?, "); //03 - IN _financial_group_id_to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?  "); //06 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - IN _operation INT,
			statement.setString(2, reportFinancialGroup.getFinancialGroupIdFrom()); //02 - IN _financial_group_id_from int,
			statement.setString(3, reportFinancialGroup.getFinancialGroupIdTo()); //03 - IN _financial_group_id_to int,
			statement.setString(4, reportFinancialGroup.getDescription()); //04 - IN _description varchar(100), 
			statement.setInt(5, reportFinancialGroup.getInactive()); //05 - IN _inactive int, 
			statement.setNull(6, Types.BIGINT); //06 - IN _user_change BIGINT			
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final String key = resultSet.getString(DatabaseConstants.COLUMN_ID);
	        	FinancialGroupDTO financialGroup = reportFinancialGroup.geFinancialGroups().get(key);
	        	if(financialGroup==null) {
	        		financialGroup = new FinancialGroupDTO();
		        	financialGroup.setId(key);
		        	financialGroup.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
		        	financialGroup.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
		        	final ChangeData changeData = new ChangeData();
		        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
		        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
		        	financialGroup.setChangeData(changeData);
	        	}
	        	final FinancialSubGroupDTO financialSubGroup = new FinancialSubGroupDTO();
	        	financialSubGroup.setId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_ID));
	        	financialSubGroup.setDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_DESCRIPTION));
	        	financialSubGroup.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_INACTIVE_SUB_GROUP_DESCRIPTION));
	        	financialSubGroup.setRevenueSource(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_REVENUE_SOURCE_ID));
	        	financialSubGroup.setRevenueSourceDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_REVENUE_SOURCE));
	        	financialSubGroup.setRevenueType(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_REVENUE_TYPE_ID));
	        	financialSubGroup.setRevenueTypeDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_REVENUE_TYPE));
	        	financialGroup.addSubGroup(financialSubGroup);
	        	reportFinancialGroup.addItem(financialGroup);
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
	public List<Combobox> getAllCombobox() throws Exception 
	{
		final List<Combobox> items = new ArrayList<Combobox>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _financial_group_id_to int,
		query.append("?, "); //03 - IN _financial_group_id_to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?  "); //06 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL_COMBOBOX.getType()); //01 - IN _operation INT,
			statement.setNull(2, Types.INTEGER); //02 - IN _financial_group_id_from int,
			statement.setNull(3, Types.INTEGER); //03 - IN _financial_group_id_to int,
			statement.setNull(4, Types.VARCHAR); //04 - IN _description varchar(100), 
			statement.setNull(5, Types.INTEGER); //05 - IN _inactive int, 
			statement.setNull(6, Types.BIGINT); //06 - IN _user_change BIGINT			
	        
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
	public String getMaxId() throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _financial_group_id_to int,
		query.append("?, "); //03 - IN _financial_group_id_to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?  "); //06 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		String result = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_MAX.getType()); //01 - IN _operation INT,
			statement.setNull(2, Types.INTEGER); //02 - IN _financial_group_id_from int,
			statement.setNull(3, Types.INTEGER); //03 - IN _financial_group_id_to int,
			statement.setNull(4, Types.VARCHAR); //04 - IN _description varchar(100), 
			statement.setNull(5, Types.INTEGER); //05 - IN _inactive int, 
			statement.setNull(6, Types.BIGINT); //06 - IN _user_change BIGINT			
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	result = resultSet.getString(DatabaseConstants.COLUMN_ID);	
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
		
		return result;
	}
}