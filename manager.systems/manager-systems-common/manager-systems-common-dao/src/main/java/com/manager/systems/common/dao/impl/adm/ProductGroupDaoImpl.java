package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.adm.ProductGroupDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.ProductGroupDTO;
import com.manager.systems.common.dto.adm.ReportProductGroupDTO;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.OperationType;

public class ProductGroupDaoImpl implements ProductGroupDao 
{
	private Connection connection;
	
	public ProductGroupDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}

	@Override
	public boolean save(final ProductGroupDTO productGroup) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_group_id_to int,
		query.append("?, "); //03 - IN _product_group_id_to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?, "); //06 - IN _user_change BIGINT,
		query.append("?  "); //07 - IN _transfer_hab DECIMAL(19,2)
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
			statement.setInt(2, productGroup.getId()); //02 - IN _product_group_id_from int,
			statement.setInt(3, productGroup.getId()); //03 - IN _product_group_id_to int,
			statement.setString(4, productGroup.getDescription()); //04 - IN _description varchar(100), 
			statement.setInt(5, productGroup.getInactiveInt()); //05 - IN _inactive int, 
			statement.setLong(6, productGroup.getChangeData().getUserChange()); //06 - IN _user_change BIGINT			
			statement.setDouble(7, productGroup.getTransferHab()); //07 - IN _transfer_hab DECIMAL(19,2)
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
	public boolean inactive(final ProductGroupDTO productGroup) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_group_id_to int,
		query.append("?, "); //03 - IN _product_group_id_to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?, "); //06 - IN _user_change BIGINT,
		query.append("?  "); //07 - IN _transfer_hab DECIMAL(19,2)
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.INACTIVE.getType()); //01 - IN _operation INT,
			statement.setInt(2, productGroup.getId()); //02 - IN _product_group_id_from int,
			statement.setInt(3, productGroup.getId()); //03 - IN _product_group_id_to int,
			statement.setNull(4, Types.VARCHAR); //04 - IN _description varchar(100), 
			statement.setInt(5, productGroup.getInactiveInt()); //05 - IN _inactive int, 
			statement.setLong(6, productGroup.getChangeData().getUserChange()); //06 - IN _user_change BIGINT		
			statement.setDouble(7, productGroup.getTransferHab()); //07 - IN _transfer_hab DECIMAL(19,2)
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
	public void get(final ProductGroupDTO productGroup) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_group_id_to int,
		query.append("?, "); //03 - IN _product_group_id_to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?, "); //06 - IN _user_change BIGINT,
		query.append("?  "); //07 - IN _transfer_hab DECIMAL(19,2)
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); //01 - IN _operation INT,
			statement.setInt(2, productGroup.getId()); //02 - IN _product_group_id_from int,
			statement.setInt(3, productGroup.getId()); //03 - IN _product_group_id_to int,
			statement.setNull(4, Types.VARCHAR); //04 - IN _description varchar(100), 
			statement.setNull(5, Types.INTEGER); //05 - IN _inactive int, 
			statement.setNull(6, Types.BIGINT); //06 - IN _user_change BIGINT	
			statement.setNull(7, Types.DECIMAL); //07 - IN _transfer_hab DECIMAL(19,2)
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	productGroup.setId(resultSet.getInt(DatabaseConstants.COLUMN_ID));
	        	productGroup.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
	        	productGroup.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	productGroup.setTransferHab(resultSet.getDouble(DatabaseConstants.COLUMN_TRANSFER_HAB));
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserCreation(resultSet.getLong(DatabaseConstants.COLUMN_USER_CREATION));
	        	changeData.setCreationDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CREATION_DATE));
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	productGroup.setChangeData(changeData);
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
	public void getAll(final ReportProductGroupDTO reportProductGroup) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_group_id_to int,
		query.append("?, "); //03 - IN _product_group_id_to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?, "); //06 - IN _user_change BIGINT,
		query.append("?  "); //07 - IN _transfer_hab DECIMAL(19,2)
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - IN _operation INT,
			statement.setInt(2, reportProductGroup.getProductGroupIdFrom()); //02 - IN _product_group_id_from int,
			statement.setInt(3, reportProductGroup.getProductGroupIdTo()); //03 - IN _product_group_id_to int,
			statement.setString(4, reportProductGroup.getDescription()); //04 - IN _description varchar(100), 
			statement.setInt(5, reportProductGroup.getInactive()); //05 - IN _inactive int, 
			statement.setNull(6, Types.BIGINT); //06 - IN _user_change BIGINT	
			statement.setNull(7, Types.DECIMAL); //07 - IN _transfer_hab DECIMAL(19,2)
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final ProductGroupDTO productGroup = new ProductGroupDTO();
	        	productGroup.setId(resultSet.getInt(DatabaseConstants.COLUMN_ID));
	        	productGroup.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
	        	productGroup.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	productGroup.setChangeData(changeData);
	        	reportProductGroup.addItem(productGroup);
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
	public List<Combobox> getAllCombobox(final ReportProductGroupDTO reportProductGroup) throws Exception 
	{
		final List<Combobox> items = new ArrayList<Combobox>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_group_id_to int,
		query.append("?, "); //03 - IN _product_group_id_to int,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?, "); //06 - IN _user_change BIGINT,
		query.append("?  "); //07 - IN _transfer_hab DECIMAL(19,2)
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL_COMBOBOX.getType()); //01 - IN _operation INT,
			statement.setInt(2, reportProductGroup.getProductGroupIdFrom()); //02 - IN _product_group_id_from int,
			statement.setInt(3, reportProductGroup.getProductGroupIdTo()); //03 - IN _product_group_id_to int,
			statement.setString(4, reportProductGroup.getDescription()); //04 - IN _description varchar(100), 
			statement.setInt(5, reportProductGroup.getInactive()); //05 - IN _inactive int, 
			statement.setNull(6, Types.BIGINT); //06 - IN _user_change BIGINT	
			statement.setNull(7, Types.DECIMAL); //07 - IN _transfer_hab DECIMAL(19,2)
	        
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