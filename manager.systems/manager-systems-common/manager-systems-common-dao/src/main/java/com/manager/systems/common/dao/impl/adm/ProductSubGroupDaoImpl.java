package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.adm.ProductSubGroupDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.ProductSubGroupDTO;
import com.manager.systems.common.dto.adm.ReportProductSubGroupDTO;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.OperationType;

public class ProductSubGroupDaoImpl implements ProductSubGroupDao 
{
	private Connection connection;
	
	public ProductSubGroupDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}

	@Override
	public boolean save(final ProductSubGroupDTO productSubGroup) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product_sub_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_sub_group_id_from int,
		query.append("?, "); //03 - IN _product_sub_group_id_to int,
		query.append("?, "); //04 - IN _product_group_id varchar(100), 		
		query.append("?, "); //05 - IN _description varchar(100), 
		query.append("?, "); //06 - IN _sale_price NUMERIC(19,2),
		query.append("?, "); //07 - IN _conversion_factor NUMERIC(19,2),
		query.append("?, "); //08 - IN _inactive int, 
		query.append("?  "); //09 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
			statement.setInt(2, productSubGroup.getId()); //02 - IN _product_sub_group_id_from int,
			statement.setInt(3, productSubGroup.getId()); //03 - IN _product_sub_group_id_to int,
			statement.setString(4, String.valueOf(productSubGroup.getGroupId())); //04 - IN _product_group_id varchar(100), 
			statement.setString(5, productSubGroup.getDescription()); //05 - IN _description varchar(100), 
			statement.setDouble(6, productSubGroup.getSalePrice()); //06 - IN _sale_price NUMERIC(19,2),
			statement.setDouble(7, productSubGroup.getConversionFactor()); //07 - IN _conversion_factor NUMERIC(19,2),
			statement.setInt(8, productSubGroup.getInactiveInt()); //08 - IN _inactive int, 
			statement.setLong(9, productSubGroup.getChangeData().getUserChange()); //09 - IN _user_change BIGINT			
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
	public boolean inactive(final ProductSubGroupDTO productSubGroup) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product_sub_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_sub_group_id_from int,
		query.append("?, "); //03 - IN _product_sub_group_id_to int,
		query.append("?, "); //04 - IN _product_group_id varchar(100), 		
		query.append("?, "); //05 - IN _description varchar(100), 
		query.append("?, "); //06 - IN _sale_price NUMERIC(19,2),
		query.append("?, "); //07 - IN _conversion_factor NUMERIC(19,2),
		query.append("?, "); //08 - IN _inactive int, 
		query.append("?  "); //09 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.INACTIVE.getType()); //01 - IN _operation INT,
			statement.setInt(2, productSubGroup.getId()); //02 - IN _product_group_id_from int,
			statement.setInt(3, productSubGroup.getId()); //03 - IN _product_group_id_to int,
			statement.setNull(4, Types.VARCHAR); //04 - IN _product_group_id varchar(100), 
			statement.setNull(5, Types.VARCHAR); //05 - IN _description varchar(100), 
			statement.setDouble(6, productSubGroup.getSalePrice()); //06 - IN _sale_price NUMERIC(19,2),
			statement.setDouble(7, productSubGroup.getConversionFactor()); //07 - IN _conversion_factor NUMERIC(19,2),
			statement.setInt(8, productSubGroup.getInactiveInt()); //08 - IN _inactive int, 
			statement.setLong(9, productSubGroup.getChangeData().getUserChange()); //09 - IN _user_change BIGINT			
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
	public void get(final ProductSubGroupDTO productSubGroup) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product_sub_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_sub_group_id_from int,
		query.append("?, "); //03 - IN _product_sub_group_id_to int,
		query.append("?, "); //04 - IN _product_group_id varchar(100), 		
		query.append("?, "); //05 - IN _description varchar(100), 
		query.append("?, "); //06 - IN _sale_price NUMERIC(19,2),
		query.append("?, "); //07 - IN _conversion_factor NUMERIC(19,2),
		query.append("?, "); //08 - IN _inactive int, 
		query.append("?  "); //09 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); //01 - IN _operation INT,
			statement.setInt(2, productSubGroup.getId()); //02 - IN _product_group_id_from int,
			statement.setInt(3, productSubGroup.getId()); //03 - IN _product_group_id_to int,
			statement.setNull(4, Types.VARCHAR); //04 - IN _product_group_id varchar(100), 
			statement.setNull(5, Types.VARCHAR); //05 - IN _description varchar(100), 
			statement.setDouble(6, productSubGroup.getSalePrice()); //06 - IN _sale_price NUMERIC(19,2),
			statement.setDouble(7, productSubGroup.getConversionFactor()); //07 - IN _conversion_factor NUMERIC(19,2),
			statement.setNull(8, Types.INTEGER); //08 - IN _inactive int, 
			statement.setNull(9, Types.BIGINT); //09 - IN _user_change BIGINT			
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	productSubGroup.setId(resultSet.getInt(DatabaseConstants.COLUMN_ID));
	        	productSubGroup.setGroupId(resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_GROUP_ID));
	        	productSubGroup.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
	        	productSubGroup.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	productSubGroup.setSalePrice(resultSet.getDouble(DatabaseConstants.COLUMN_SALE_PRICE));
	        	productSubGroup.setConversionFactor(resultSet.getDouble(DatabaseConstants.COLUMN_CONVERSION_FACTOR));
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserCreation(resultSet.getLong(DatabaseConstants.COLUMN_USER_CREATION));
	        	changeData.setCreationDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CREATION_DATE));
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	productSubGroup.setChangeData(changeData);
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
	public void getAll(final ReportProductSubGroupDTO reportProductSubGroup) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product_sub_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_sub_group_id_from int,
		query.append("?, "); //03 - IN _product_sub_group_id_to int,
		query.append("?, "); //04 - IN _product_group_id varchar(100), 		
		query.append("?, "); //05 - IN _description varchar(100), 
		query.append("?, "); //06 - IN _sale_price NUMERIC(19,2),
		query.append("?, "); //07 - IN _conversion_factor NUMERIC(19,2),
		query.append("?, "); //08 - IN _inactive int, 
		query.append("?  "); //09 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - IN _operation INT,
			statement.setInt(2, reportProductSubGroup.getProductSubGroupIdFrom()); //02 - IN _product_sub_group_id_from int,
			statement.setInt(3, reportProductSubGroup.getProductSubGroupIdTo()); //03 - IN _product_sub_group_id_to int,
			statement.setString(4, reportProductSubGroup.getProductGroupIds()); //04 - IN _product_group_id varchar(100)
			statement.setString(5, reportProductSubGroup.getDescription()); //05 - IN _description varchar(100), 
			statement.setNull(6, Types.NUMERIC); //06 - IN _sale_price NUMERIC(19,2),
			statement.setNull(7, Types.NUMERIC); //07 - IN _conversion_factor NUMERIC(19,2),
			statement.setInt(8, reportProductSubGroup.getInactive()); //08 - IN _inactive int, 
			statement.setNull(9, Types.BIGINT); //09 - IN _user_change BIGINT			
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final ProductSubGroupDTO productSubGroup = new ProductSubGroupDTO();
	        	productSubGroup.setId(resultSet.getInt(DatabaseConstants.COLUMN_ID));
	        	productSubGroup.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
	        	productSubGroup.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	productSubGroup.setGroupId(resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_GROUP_ID));
	        	productSubGroup.setDescriptionGroup(resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_GROUP_DESCRIPTION));
	        	productSubGroup.setSalePrice(resultSet.getDouble(DatabaseConstants.COLUMN_SALE_PRICE));
	        	productSubGroup.setConversionFactor(resultSet.getDouble(DatabaseConstants.COLUMN_CONVERSION_FACTOR));
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	productSubGroup.setChangeData(changeData);
	        	reportProductSubGroup.addItem(productSubGroup);
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
	public List<Combobox> getAllCombobox(final ReportProductSubGroupDTO reportProductSubGroup) throws Exception 
	{
		final List<Combobox> items = new ArrayList<Combobox>();

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product_sub_group");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_sub_group_id_from int,
		query.append("?, "); //03 - IN _product_sub_group_id_to int,
		query.append("?, "); //04 - IN _product_group_id varchar(100), 		
		query.append("?, "); //05 - IN _description varchar(100), 
		query.append("?, "); //06 - IN _sale_price NUMERIC(19,2),
		query.append("?, "); //07 - IN _conversion_factor NUMERIC(19,2),
		query.append("?, "); //08 - IN _inactive int, 
		query.append("?  "); //09 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL_COMBOBOX.getType()); //01 - IN _operation INT,
			statement.setInt(2, reportProductSubGroup.getProductSubGroupIdFrom()); //02 - IN _product_sub_group_id_from int,
			statement.setInt(3, reportProductSubGroup.getProductSubGroupIdTo()); //03 - IN _product_sub_group_id_to int,
			statement.setString(4, reportProductSubGroup.getProductGroupIds()); //04 - IN _product_group_id varchar(100)
			statement.setString(5, reportProductSubGroup.getDescription()); //05 - IN _description varchar(100), 
			statement.setNull(6, Types.NUMERIC); //06 - IN _sale_price NUMERIC(19,2),
			statement.setNull(7, Types.NUMERIC); //07 - IN _conversion_factor NUMERIC(19,2),
			statement.setInt(8, reportProductSubGroup.getInactive()); //08 - IN _inactive int, 
			statement.setNull(9, Types.BIGINT); //09 - IN _user_change BIGINT			
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	items.add(new Combobox
	        	(
	    			resultSet.getString(DatabaseConstants.COLUMN_ID), 
	    			resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION), 
	    			resultSet.getString(DatabaseConstants.COLUMN_SALE_PRICE), 
	    			resultSet.getString(DatabaseConstants.COLUMN_CONVERSION_FACTOR),
	    			resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_GROUP_ID),
	    			resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_GROUP_DESCRIPTION)
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
}