package com.manager.systems.common.dao.impl.financialCostCenter;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.financialCostCenter.FinancialCostCenterDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.financialCostCenter.FinancialCostCenterDTO;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.OperationType;

public class FinancialCostCenterDaoImpl implements FinancialCostCenterDao 
{
	private Connection connection;
	
	public FinancialCostCenterDaoImpl(final Connection connection) 
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
		query.append("call pr_financial_cost_center");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _description VARCHAR(50),
		query.append("?, "); //03 - IN _user_change BIGINT,
		query.append("? "); //04 - IN _inactive INT, 
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		try
		{			
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL_COMBOBOX.getType()); //01 - IN _operation INT,
			statement.setNull(2, Types.INTEGER); //02 - IN _description VARCHAR(50),
			statement.setNull(3, Types.BIGINT); //03 -  IN _user_change BIGINT,	
			statement.setInt(4, 0); //04 - IN _inactive INT 
	        
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
	public Integer save(final FinancialCostCenterDTO financialCostCenter) throws Exception 
	{
		ResultSet resultSet = null;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		Integer financialCostCenterId = 0;
		
		query.append("{");
		query.append("call pr_financial_cost_center");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _description VARCHAR(50), 
		query.append("?, "); //03 - IN _user_change BIGINT,
		query.append("? "); //04 - IN _inactive INT,
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
			statement.setString(2, financialCostCenter.getDescription()); //02 - IN _description varchar(100), 
			statement.setLong(3, financialCostCenter.getChangeData().getUserChange()); //03 - IN _user_change BIGINT,
			statement.setInt(4, financialCostCenter.getInactiveInt()); //04 - IN _inactive INT
			final boolean isProcessed  = statement.execute();
	        
	    	if(isProcessed) {
	    		resultSet = statement.getResultSet();
				while (resultSet.next()) {
					financialCostCenterId = resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_COST_CENTER_ID);
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
		return financialCostCenterId;
	}
}