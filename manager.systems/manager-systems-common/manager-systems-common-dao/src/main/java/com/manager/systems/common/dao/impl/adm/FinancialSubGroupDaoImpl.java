package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.adm.FinancialSubGroupDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.FinancialSubGroupDTO;
import com.manager.systems.common.dto.adm.ReportFinancialSubGroupDTO;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.OperationType;

public class FinancialSubGroupDaoImpl implements FinancialSubGroupDao 
{
	private Connection connection;
	
	public FinancialSubGroupDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}

	@Override
	public boolean save(final FinancialSubGroupDTO financialSubGroup) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_sub_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _financial_sub_group_id_from VARCHAR(7),
		query.append("?, "); //03 - IN _financial_sub_group_id_to VARCHAR(7),
		query.append("?, "); //04 - IN _financial_group_id varchar(100), 		
		query.append("?, "); //05 - IN _description varchar(100), 
		query.append("?, "); //06 - IN _inactive int, 
		query.append("?, "); //07 - IN _user_change BIGINT,
		query.append("?, "); //08 - IN _revenue_source INT,
		query.append("?  "); //09 - IN _revenue_type INT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
			statement.setString(2, financialSubGroup.getId()); //02 - IN _financial_sub_group_id_from int,
			statement.setString(3, financialSubGroup.getId()); //03 - IN _financial_sub_group_id_to int,
			statement.setString(4, String.valueOf(financialSubGroup.getGroupId())); //04 - IN _financial_group_id varchar(100), 
			statement.setString(5, financialSubGroup.getDescription()); //05 - IN _description varchar(100), 
			statement.setInt(6, financialSubGroup.getInactiveInt()); //06 - IN _inactive int, 
			statement.setLong(7, financialSubGroup.getChangeData().getUserChange()); //07 - IN _user_change BIGINT		
			statement.setString(8, financialSubGroup.getRevenueSource()); //08 - IN _revenue_source INT,
			statement.setString(9, financialSubGroup.getRevenueType()); //09 - IN _revenue_type INT			
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
	public boolean inactive(final FinancialSubGroupDTO financialSubGroup) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_sub_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _financial_sub_group_id_from VARCHAR(7),
		query.append("?, "); //03 - IN _financial_sub_group_id_to VARCHAR(7),
		query.append("?, "); //04 - IN _financial_group_id varchar(100), 		
		query.append("?, "); //05 - IN _description varchar(100), 
		query.append("?, "); //06 - IN _inactive int, 
		query.append("?, "); //07 - IN _user_change BIGINT,
		query.append("?, "); //08 - IN _revenue_source INT,
		query.append("?  "); //09 - IN _revenue_type INT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.INACTIVE.getType()); //01 - IN _operation INT,
			statement.setString(2, financialSubGroup.getId()); //02 - IN _financial_group_id_from int,
			statement.setString(3, financialSubGroup.getId()); //03 - IN _financial_group_id_to int,
			statement.setString(4, financialSubGroup.getGroupId()); //04 - IN _financial_group_id varchar(100), 
			statement.setNull(5, Types.VARCHAR); //05 - IN _description varchar(100), 
			statement.setInt(6, financialSubGroup.getInactiveInt()); //06 - IN _inactive int, 
			statement.setLong(7, financialSubGroup.getChangeData().getUserChange()); //07 - IN _user_change BIGINT		
			statement.setString(8, financialSubGroup.getRevenueSource()); //08 - IN _revenue_source INT,
			statement.setString(9, financialSubGroup.getRevenueType()); //09 - IN _revenue_type INT			
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
	public boolean inactiveAllByGroup(final FinancialSubGroupDTO financialSubGroup) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_sub_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _financial_sub_group_id_from VARCHAR(7),
		query.append("?, "); //03 - IN _financial_sub_group_id_to VARCHAR(7),
		query.append("?, "); //04 - IN _financial_group_id varchar(100), 		
		query.append("?, "); //05 - IN _description varchar(100), 
		query.append("?, "); //06 - IN _inactive int, 
		query.append("?, "); //07 - IN _user_change BIGINT,
		query.append("?, "); //08 - IN _revenue_source INT,
		query.append("?  "); //09 - IN _revenue_type INT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.INACTIVE_ALL.getType()); //01 - IN _operation INT,
			statement.setNull(2, Types.VARCHAR); //02 - IN _financial_group_id_from int,
			statement.setNull(3, Types.VARCHAR); //03 - IN _financial_group_id_to int,
			statement.setString(4, financialSubGroup.getGroupId()); //04 - IN _financial_group_id varchar(100), 
			statement.setNull(5, Types.VARCHAR); //05 - IN _description varchar(100), 
			statement.setInt(6, financialSubGroup.getInactiveInt()); //06 - IN _inactive int, 
			statement.setLong(7, financialSubGroup.getChangeData().getUserChange()); //07 - IN _user_change BIGINT		
			statement.setString(8, financialSubGroup.getRevenueSource()); //08 - IN _revenue_source INT,
			statement.setString(9, financialSubGroup.getRevenueType()); //09 - IN _revenue_type INT			
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
	public void get(final FinancialSubGroupDTO financialSubGroup) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_sub_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _financial_sub_group_id_from VARCHAR(7),
		query.append("?, "); //03 - IN _financial_sub_group_id_to VARCHAR(7),
		query.append("?, "); //04 - IN _financial_group_id varchar(100), 		
		query.append("?, "); //05 - IN _description varchar(100), 
		query.append("?, "); //06 - IN _inactive int, 
		query.append("?, "); //07 - IN _user_change BIGINT,
		query.append("?, "); //08 - IN _revenue_source INT,
		query.append("?  "); //09 - IN _revenue_type INT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); //01 - IN _operation INT,
			statement.setString(2, financialSubGroup.getId()); //02 - IN _financial_group_id_from int,
			statement.setString(3, financialSubGroup.getId()); //03 - IN _financial_group_id_to int,
			statement.setNull(4, Types.VARCHAR); //04 - IN _financial_group_id varchar(100), 
			statement.setNull(5, Types.VARCHAR); //05 - IN _description varchar(100), 
			statement.setNull(6, Types.INTEGER); //06 - IN _inactive int, 
			statement.setNull(7, Types.BIGINT); //07 - IN _user_change BIGINT			
			statement.setString(8, financialSubGroup.getRevenueSource()); //08 - IN _revenue_source INT,
			statement.setString(9, financialSubGroup.getRevenueType()); //09 - IN _revenue_type INT				        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	financialSubGroup.setId(resultSet.getString(DatabaseConstants.COLUMN_ID));
	        	financialSubGroup.setGroupId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_ID));
	        	financialSubGroup.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
	        	financialSubGroup.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	financialSubGroup.setRevenueSource(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_REVENUE_SOURCE));
	        	financialSubGroup.setRevenueType(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_REVENUE_TYPE));
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	changeData.setUsernameChange(resultSet.getString(DatabaseConstants.COLUMN_USER_CHANGE_NAME));
	        	financialSubGroup.setChangeData(changeData);
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
	public void getAll(final ReportFinancialSubGroupDTO reportFinancialSubGroup) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_sub_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _financial_sub_group_id_from VARCHAR(7),
		query.append("?, "); //03 - IN _financial_sub_group_id_to VARCHAR(7),
		query.append("?, "); //04 - IN _financial_group_id varchar(100), 		
		query.append("?, "); //05 - IN _description varchar(100), 
		query.append("?, "); //06 - IN _inactive int, 
		query.append("?, "); //07 - IN _user_change BIGINT,
		query.append("?, "); //08 - IN _revenue_source INT,
		query.append("?  "); //09 - IN _revenue_type INT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - IN _operation INT,
			statement.setString(2, reportFinancialSubGroup.getFinancialSubGroupIdFrom()); //02 - IN _financial_sub_group_id_from int,
			statement.setString(3, reportFinancialSubGroup.getFinancialSubGroupIdTo()); //03 - IN _financial_sub_group_id_to int,
			statement.setString(4, reportFinancialSubGroup.getFinancialGroupIds()); //04 - IN _financial_group_id varchar(100)
			statement.setString(5, reportFinancialSubGroup.getDescription()); //05 - IN _description varchar(100), 
			statement.setInt(6, reportFinancialSubGroup.getInactive()); //06 - IN _inactive int, 
			statement.setNull(7, Types.BIGINT); //07 - IN _user_change BIGINT			
			statement.setNull(8, Types.VARCHAR); //08 - IN _revenue_source INT,
			statement.setNull(9, Types.VARCHAR); //09 - IN _revenue_type INT			
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final FinancialSubGroupDTO financialSubGroup = new FinancialSubGroupDTO();
	        	financialSubGroup.setId(resultSet.getString(DatabaseConstants.COLUMN_ID));
	        	financialSubGroup.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
	        	financialSubGroup.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	financialSubGroup.setGroupId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_ID));
	        	financialSubGroup.setDescriptionGroup(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_DESCRIPTION));
	        	financialSubGroup.setRevenueSource(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_REVENUE_SOURCE));
	        	financialSubGroup.setRevenueType(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_REVENUE_TYPE));
	        	
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	financialSubGroup.setChangeData(changeData);
	        	reportFinancialSubGroup.addItem(financialSubGroup);
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
	public List<Combobox> getAllCombobox(final ReportFinancialSubGroupDTO reportFinancialSubGroup) throws Exception
	{
		final List<Combobox> items = new ArrayList<Combobox>();

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_sub_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _financial_sub_group_id_from VARCHAR(7),
		query.append("?, "); //03 - IN _financial_sub_group_id_to VARCHAR(7),
		query.append("?, "); //04 - IN _financial_group_id varchar(100), 		
		query.append("?, "); //05 - IN _description varchar(100), 
		query.append("?, "); //06 - IN _inactive int, 
		query.append("?, "); //07 - IN _user_change BIGINT,
		query.append("?, "); //08 - IN _revenue_source INT,
		query.append("?  "); //09 - IN _revenue_type INT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL_COMBOBOX.getType()); //01 - IN _operation INT,
			statement.setString(2, reportFinancialSubGroup.getFinancialSubGroupIdFrom()); //02 - IN _financial_sub_group_id_from int,
			statement.setString(3, reportFinancialSubGroup.getFinancialSubGroupIdTo()); //03 - IN _financial_sub_group_id_to int,
			statement.setString(4, reportFinancialSubGroup.getFinancialGroupIds()); //04 - IN _financial_group_id varchar(100)
			statement.setString(5, reportFinancialSubGroup.getDescription()); //05 - IN _description varchar(100), 
			statement.setInt(6, reportFinancialSubGroup.getInactive()); //06 - IN _inactive int, 
			statement.setNull(7, Types.BIGINT); //07 - IN _user_change BIGINT			
			statement.setNull(8, Types.VARCHAR); //08 - IN _revenue_source INT,
			statement.setNull(9, Types.VARCHAR); //09 - IN _revenue_type INT			
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	items.add(new Combobox
	        	(
	    			resultSet.getString(DatabaseConstants.COLUMN_ID), 
	    			resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION)
	        	));
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
	public void getMaxId(final FinancialSubGroupDTO financialSubGroup) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_sub_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _financial_sub_group_id_from VARCHAR(7),
		query.append("?, "); //03 - IN _financial_sub_group_id_to VARCHAR(7),
		query.append("?, "); //04 - IN _financial_group_id varchar(100), 		
		query.append("?, "); //05 - IN _description varchar(100), 
		query.append("?, "); //06 - IN _inactive int, 
		query.append("?, "); //07 - IN _user_change BIGINT,
		query.append("?, "); //08 - IN _revenue_source INT,
		query.append("?  "); //09 - IN _revenue_type INT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_MAX.getType()); //01 - IN _operation INT,
			statement.setNull(2, Types.INTEGER); //02 - IN _financial_group_id_from int,
			statement.setNull(3, Types.INTEGER); //03 - IN _financial_group_id_to int,
			statement.setString(4, String.valueOf(financialSubGroup.getGroupId())); //04 - IN _financial_group_id varchar(100), 
			statement.setNull(5, Types.VARCHAR); //05 - IN _description varchar(100), 
			statement.setNull(6, Types.INTEGER); //06 - IN _inactive int, 
			statement.setNull(7, Types.BIGINT); //07 - IN _user_change BIGINT			
			statement.setNull(8, Types.INTEGER); //08 - IN _revenue_source INT,
			statement.setNull(9, Types.INTEGER); //09 - IN _revenue_type INT				        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final String maxIdSubGroup = resultSet.getString(DatabaseConstants.COLUMN_ID);	
	        	financialSubGroup.setMaxIdSubGroup(maxIdSubGroup);
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
}