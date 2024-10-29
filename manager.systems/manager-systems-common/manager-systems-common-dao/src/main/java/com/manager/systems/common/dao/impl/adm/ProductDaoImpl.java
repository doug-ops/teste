package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.manager.systems.common.dao.adm.ProductDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.ProductDTO;
import com.manager.systems.common.dto.adm.ReportProductDTO;
import com.manager.systems.common.dto.movement.product.MovementProductDTO;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.OperationType;

public class ProductDaoImpl implements ProductDao 
{
	private Connection connection;
	
	public ProductDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}

	@Override
	public boolean save(final ProductDTO product) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_id_from BIGINT, 
		query.append("?, "); //03 - IN _product_id_to BIGINT,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _sale_price NUMERIC(19,2),
		query.append("?, "); //06- IN _cost_price NUMERIC(19,2),
		query.append("?, "); //07 - IN _conversion_factor NUMERIC(19,2),
		query.append("?, "); //08 - IN _product_group_id varchar(100),
		query.append("?, "); //09 - IN _product_sub_group_id varchar(100),
		query.append("?, "); //10 - IN _company_id BIGINT,
		query.append("?, "); //11 - IN _input_movement BIGINT,
		query.append("?, "); //12 - IN _output_movement BIGINT,
		query.append("?, "); //13 - IN _inactive int, 
		query.append("?, "); //14 - IN _user_change BIGINT,
		query.append("?, "); //15 - IN _clock_movement BIGINT, 
		query.append("?, "); //16 - IN _enable_clock_movement BIT,
		query.append("?, "); //17 - IN _user_id BIGINT,
		query.append("?, "); //18 - IN _alias_product varchar(100), 
		query.append("?, "); //19 - IN _game_type_id INT,
		query.append("?  "); //20 - IN _machine_type_id INT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
			statement.setLong(2, Long.valueOf(product.getId())); //02 - IN _product_id_from BIGINT, 
			statement.setLong(3, Long.valueOf(product.getId())); //03 - IN _product_id_to BIGINT,
			statement.setString(4, product.getDescription()); //04 - IN _description varchar(100), 
			statement.setString(5, product.getSalePrice()); //05 - IN _sale_price NUMERIC(19,2),
			statement.setString(6, product.getCostPrice()); //06- IN _cost_price NUMERIC(19,2),
			statement.setString(7, product.getConversionFactor()); //07 - IN _conversion_factor NUMERIC(19,2),
			statement.setString(8, String.valueOf(product.getGroupId())); //08 - IN _product_group_id varchar(100),
			statement.setString(9, String.valueOf(product.getSubGroupId())); //09 - IN _product_sub_group_id varchar(100),
			statement.setString(10, product.getCompanyId()); //10 - IN _company_id BIGINT,
			statement.setString(11, product.getInputMovement()); //11 - IN _input_movement BIGINT,
			statement.setString(12, product.getOutputMovement()); //12 - IN _output_movement BIGINT,
			statement.setInt(13, product.getInactiveInt()); //13 - IN _inactive int, 
			statement.setLong(14, product.getChangeData().getUserChange()); //14 - IN _user_change BIGINT	
			statement.setString(15, product.getClockMovement()); //15 - IN _clock_movement BIGINT, 
			statement.setBoolean(16, product.isEnableClockMovement()); //16 - IN _enable_clock_movement BIT,
			statement.setNull(17, Types.BIGINT); //17 - IN _user_id BIGINT,
			statement.setString(18, product.getAliasProduct()); //17 - IN _alias_product varchar(100),
			statement.setInt(19, product.getGameTypeId()); //17 - IN _game_type_id INT,
			statement.setInt(20, product.getMachineTypeId()); //17 - IN _machine_type_id INT
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
	public boolean saveBravo(final ProductDTO product) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_id_from BIGINT, 
		query.append("?, "); //03 - IN _product_id_to BIGINT,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _sale_price NUMERIC(19,2),
		query.append("?, "); //06- IN _cost_price NUMERIC(19,2),
		query.append("?, "); //07 - IN _conversion_factor NUMERIC(19,2),
		query.append("?, "); //08 - IN _product_group_id varchar(100),
		query.append("?, "); //09 - IN _product_sub_group_id varchar(100),
		query.append("?, "); //10 - IN _company_id BIGINT,
		query.append("?, "); //11 - IN _input_movement BIGINT,
		query.append("?, "); //12 - IN _output_movement BIGINT,
		query.append("?, "); //13 - IN _inactive int, 
		query.append("?, "); //14 - IN _user_change BIGINT,
		query.append("?, "); //15 - IN _clock_movement BIGINT, 
		query.append("?, "); //16 - IN _enable_clock_movement BIT,
		query.append("?, "); //17 - IN _user_id BIGINT,
		query.append("?, "); //18 - IN _alias_product varchar(100), 
		query.append("?, "); //19 - IN _game_type_id INT,
		query.append("?  "); //20 - IN _machine_type_id INT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.INSERT.getType()); //01 - IN _operation INT,
			statement.setLong(2, Long.valueOf(product.getId())); //02 - IN _product_id_from BIGINT, 
			statement.setLong(3, Long.valueOf(product.getId())); //03 - IN _product_id_to BIGINT,
			statement.setString(4, product.getDescription()); //04 - IN _description varchar(100), 
			statement.setString(5, product.getSalePrice()); //05 - IN _sale_price NUMERIC(19,2),
			statement.setString(6, product.getCostPrice()); //06- IN _cost_price NUMERIC(19,2),
			statement.setString(7, product.getConversionFactor()); //07 - IN _conversion_factor NUMERIC(19,2),
			statement.setString(8, String.valueOf(product.getGroupId())); //08 - IN _product_group_id varchar(100),
			statement.setString(9, String.valueOf(product.getSubGroupId())); //09 - IN _product_sub_group_id varchar(100),
			statement.setString(10, product.getCompanyId()); //10 - IN _company_id BIGINT,
			statement.setString(11, product.getInputMovement()); //11 - IN _input_movement BIGINT,
			statement.setString(12, product.getOutputMovement()); //12 - IN _output_movement BIGINT,
			statement.setInt(13, product.getInactiveInt()); //13 - IN _inactive int, 
			statement.setLong(14, product.getChangeData().getUserChange()); //14 - IN _user_change BIGINT			
			statement.setString(15, product.getClockMovement()); //15 - IN _clock_movement BIGINT, 
			statement.setBoolean(16, product.isEnableClockMovement()); //16 - IN _enable_clock_movement BIT,
			statement.setNull(17, Types.BIGINT); //17 - IN _user_id BIGINT,
			statement.setNull(18, Types.VARCHAR); //18 - IN _alias_product varchar(100), 
			statement.setNull(19, Types.INTEGER); //19 - IN _game_type_id INT,
			statement.setNull(20, Types.INTEGER); //20 - IN _machine_type_id INT
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
	public boolean inactive(final ProductDTO product) throws Exception 
	{
		boolean wasInactivated = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_id_from BIGINT, 
		query.append("?, "); //03 - IN _product_id_to BIGINT,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _sale_price NUMERIC(19,2),
		query.append("?, "); //06 - IN _cost_price NUMERIC(19,2),
		query.append("?, "); //07 - IN _conversion_factor NUMERIC(19,2),
		query.append("?, "); //08 - IN _product_group_id varchar(100),
		query.append("?, "); //09 - IN _product_sub_group_id varchar(100),
		query.append("?, "); //10 - IN _company_id BIGINT,
		query.append("?, "); //11 - IN _input_movement BIGINT,
		query.append("?, "); //12 - IN _output_movement BIGINT,
		query.append("?, "); //13 - IN _inactive int, 
		query.append("?, "); //14 - IN _user_change BIGINT,
		query.append("?, "); //15 - IN _clock_movement BIGINT, 
		query.append("?, "); //16 - IN _enable_clock_movement BIT,
		query.append("?, "); //17 - IN _user_id BIGINT,
		query.append("?, "); //18 - IN _alias_product varchar(100), 
		query.append("?, "); //19 - IN _game_type_id INT,
		query.append("?  "); //20 - IN _machine_type_id INT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.INACTIVE.getType()); //01 - IN _operation INT,
			statement.setLong(2, Long.valueOf(product.getId())); //02 - IN _product_id_from BIGINT, 
			statement.setLong(3, Long.valueOf(product.getId())); //03 - IN _product_id_to BIGINT,
			statement.setNull(4, Types.VARCHAR); //04 - IN _description varchar(100), 
			statement.setNull(5, Types.NUMERIC); //05 - IN _sale_price NUMERIC(19,2),
			statement.setNull(6, Types.NUMERIC); //06- IN _cost_price NUMERIC(19,2),
			statement.setNull(7, Types.NUMERIC); //07 - IN _conversion_factor NUMERIC(19,2),
			statement.setNull(8, Types.VARCHAR); //08 - IN _product_group_id varchar(100),
			statement.setNull(9, Types.VARCHAR); //09 - IN _product_sub_group_id varchar(100),
			statement.setNull(10, Types.BIGINT); //10 - IN _company_id BIGINT,
			statement.setNull(11, Types.BIGINT); //11 - IN _input_movement BIGINT,
			statement.setNull(12, Types.BIGINT); //12 - IN _output_movement BIGINT,
			statement.setInt(13, product.getInactiveInt()); //13 - IN _inactive int, 
			statement.setLong(14, product.getChangeData().getUserChange()); //14 - IN _user_change BIGINT			
			statement.setNull(15, Types.BIGINT); //15 - IN _clock_movement BIGINT, 
			statement.setNull(16, Types.BOOLEAN); //16 - IN _enable_clock_movement BIT,
			statement.setNull(17, Types.BIGINT); //17 - IN _user_id BIGINT,
			statement.setNull(18, Types.VARCHAR); //18 - IN _alias_product varchar(100), 
			statement.setNull(19, Types.INTEGER); //19 - IN _game_type_id INT,
			statement.setNull(20, Types.INTEGER); //20 - IN _machine_type_id INT
			wasInactivated = (statement.executeUpdate()>0);  
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
		return wasInactivated;
	}

	@Override
	public void get(final ProductDTO product) throws Exception 
	{		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_id_from BIGINT, 
		query.append("?, "); //03 - IN _product_id_to BIGINT,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _sale_price NUMERIC(19,2),
		query.append("?, "); //06 - IN _cost_price NUMERIC(19,2),
		query.append("?, "); //07 - IN _conversion_factor NUMERIC(19,2),
		query.append("?, "); //08 - IN _product_group_id varchar(100),
		query.append("?, "); //09 - IN _product_sub_group_id varchar(100),
		query.append("?, "); //10 - IN _company_id BIGINT,
		query.append("?, "); //11 - IN _input_movement BIGINT,
		query.append("?, "); //12 - IN _output_movement BIGINT,
		query.append("?, "); //13 - IN _inactive int, 
		query.append("?, "); //14 - IN _user_change BIGINT,
		query.append("?, "); //15 - IN _clock_movement BIGINT, 
		query.append("?, "); //16 - IN _enable_clock_movement BIT,
		query.append("?, "); //17 - IN _user_id BIGINT,
		query.append("?, "); //18 - IN _alias_product varchar(100), 
		query.append("?, "); //19 - IN _game_type_id INT,
		query.append("?  "); //20 - IN _machine_type_id INT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); //01 - IN _operation INT,
			statement.setLong(2, Long.valueOf(product.getId())); //02 - IN _product_id_from BIGINT, 
			statement.setLong(3, Long.valueOf(product.getId())); //03 - IN _product_id_to BIGINT,
			statement.setNull(4, Types.VARCHAR); //04 - IN _description varchar(100), 
			statement.setNull(5, Types.NUMERIC); //05 - IN _sale_price NUMERIC(19,2),
			statement.setNull(6, Types.NUMERIC); //06- IN _cost_price NUMERIC(19,2),
			statement.setNull(7, Types.NUMERIC); //07 - IN _conversion_factor NUMERIC(19,2),
			statement.setNull(8, Types.VARCHAR); //08 - IN _product_group_id varchar(100),
			statement.setNull(9, Types.VARCHAR); //09 - IN _product_sub_group_id varchar(100),
			statement.setNull(10, Types.BIGINT); //10 - IN _company_id BIGINT,
			statement.setNull(11, Types.BIGINT); //11 - IN _input_movement BIGINT,
			statement.setNull(12, Types.BIGINT); //12 - IN _output_movement BIGINT,
			statement.setNull(13, Types.INTEGER); //13 - IN _inactive int, 
			statement.setLong(14, Types.BIGINT); //14 - IN _user_change BIGINT	
			statement.setNull(15, Types.BIGINT); //15 - IN _clock_movement BIGINT, 
			statement.setNull(16, Types.BOOLEAN); //16 - IN _enable_clock_movement BIT,
			statement.setLong(17, Long.valueOf(product.getUser())); //17 - IN _user_id BIGINT,
			statement.setNull(18, Types.VARCHAR); //18 - IN _alias_product varchar(100), 
			statement.setNull(19, Types.INTEGER); //19 - IN _game_type_id INT,
			statement.setNull(20, Types.INTEGER); //20 - IN _machine_type_id INT
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	product.setId(resultSet.getString(DatabaseConstants.COLUMN_ID));
	        	product.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
	        	product.setSalePrice(resultSet.getString(DatabaseConstants.COLUMN_SALE_PRICE));
	        	product.setCostPrice(resultSet.getString(DatabaseConstants.COLUMN_COST_PRICE));
	        	product.setConversionFactor(resultSet.getString(DatabaseConstants.COLUMN_CONVERSION_FACTOR));
	        	product.setInputMovement(resultSet.getString(DatabaseConstants.COLUMN_INPUT_MOVEMENT));
	        	product.setOutputMovement(resultSet.getString(DatabaseConstants.COLUMN_OUTPUT_MOVEMENT));
	        	product.setCompanyId(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_ID));
	        	product.setGroupId(resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_GROUP_ID));
	        	product.setSubGroupId(resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_SUB_GROUP_ID));
	        	product.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	product.setClockMovement(resultSet.getString(DatabaseConstants.COLUMN_CLOCK_MOVEMENT));
	        	product.setEnableClockMovement(resultSet.getBoolean(DatabaseConstants.COLUMN_ENABLE_CLOCK_MOVEMENT));
	        	product.setUser(resultSet.getLong(DatabaseConstants.COLUMN_USER_ID));
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserCreation(resultSet.getLong(DatabaseConstants.COLUMN_USER_CREATION));
	        	changeData.setCreationDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CREATION_DATE));
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	product.setChangeData(changeData);
	        	product.setAliasProduct(resultSet.getString(DatabaseConstants.COLUMN_ALIAS_PRODUCT)); 
	        	product.setGameTypeId(resultSet.getInt(DatabaseConstants.COLUMN_GAME_TYPE)); 
	        	product.setMachineTypeId(resultSet.getInt(DatabaseConstants.COLUMN_MACHINE_TYPE));
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
	public void getAll(final ReportProductDTO reportProduct) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_id_from BIGINT, 
		query.append("?, "); //03 - IN _product_id_to BIGINT,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _sale_price NUMERIC(19,2),
		query.append("?, "); //06 - IN _cost_price NUMERIC(19,2),
		query.append("?, "); //07 - IN _conversion_factor NUMERIC(19,2),
		query.append("?, "); //08 - IN _product_group_id varchar(100),
		query.append("?, "); //09 - IN _product_sub_group_id varchar(100),
		query.append("?, "); //10 - IN _company_id BIGINT,
		query.append("?, "); //11 - IN _input_movement BIGINT,
		query.append("?, "); //12 - IN _output_movement BIGINT,
		query.append("?, "); //13 - IN _inactive int, 
		query.append("?, "); //14 - IN _user_change BIGINT,
		query.append("?, "); //15 - IN _clock_movement BIGINT, 
		query.append("?, "); //16 - IN _enable_clock_movement BIT,
		query.append("?, "); //17 - IN _user_id BIGINT,
		query.append("?, "); //18 - IN _alias_product varchar(100), 
		query.append("?, "); //19 - IN _game_type_id INT,
		query.append("?  "); //20 - IN _machine_type_id INT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - IN _operation INT,
			statement.setLong(2, reportProduct.getProductIdFrom()); //02 - IN _product_id_from BIGINT, 
			statement.setLong(3, reportProduct.getProductIdTo()); //03 - IN _product_id_to BIGINT,
			statement.setString(4, reportProduct.getDescription()); //04 - IN _description varchar(100), 
			statement.setNull(5, Types.NUMERIC); //05 - IN _sale_price NUMERIC(19,2),
			statement.setNull(6, Types.NUMERIC); //06- IN _cost_price NUMERIC(19,2),
			statement.setNull(7, Types.NUMERIC); //07 - IN _conversion_factor NUMERIC(19,2),
			statement.setString(8, reportProduct.getProductGroupIds()); //08 - IN _product_group_id varchar(100),
			statement.setString(9, reportProduct.getProductSubGroupIds()); //09 - IN _product_sub_group_id varchar(100),
			statement.setNull(10, Types.BIGINT); //10 - IN _company_id BIGINT,
			statement.setNull(11, Types.BIGINT); //11 - IN _input_movement BIGINT,
			statement.setNull(12, Types.BIGINT); //12 - IN _output_movement BIGINT,
			statement.setInt(13, reportProduct.getInactive()); //13 - IN _inactive int, 
			statement.setLong(14, Types.BIGINT); //14 - IN _user_change BIGINT		
			statement.setNull(15, Types.BIGINT); //15 - IN _clock_movement BIGINT, 
			statement.setNull(16, Types.BOOLEAN); //16 - IN _enable_clock_movement BIT,
			statement.setLong(17, reportProduct.getUser()); //17 - IN _user_id BIGINT,
			statement.setNull(18, Types.VARCHAR); //18 - IN _alias_product varchar(100), 
			statement.setNull(19, Types.INTEGER); //19 - IN _game_type_id INT,
			statement.setNull(20, Types.INTEGER); //20 - IN _machine_type_id INT
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final ProductDTO product = new ProductDTO();
	        	product.setId(resultSet.getString(DatabaseConstants.COLUMN_ID));
	        	product.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
	        	product.setSalePrice(resultSet.getString(DatabaseConstants.COLUMN_SALE_PRICE));
	        	product.setCostPrice(resultSet.getString(DatabaseConstants.COLUMN_COST_PRICE));
	        	product.setConversionFactor(resultSet.getString(DatabaseConstants.COLUMN_CONVERSION_FACTOR));
	        	product.setInputMovement(resultSet.getString(DatabaseConstants.COLUMN_INPUT_MOVEMENT));
	        	product.setOutputMovement(resultSet.getString(DatabaseConstants.COLUMN_OUTPUT_MOVEMENT));
	        	product.setCompanyId(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_ID));
	        	product.setCompanyDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION));
	        	product.setGroupId(resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_GROUP_ID));
	        	product.setGroupDescription(resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_GROUP_DESCRIPTION));
	        	product.setSubGroupId(resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_SUB_GROUP_ID));
	        	product.setSubGroupDescription(resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_SUB_GROUP_DESCRIPTION));
	        	product.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	product.setClockMovement(resultSet.getString(DatabaseConstants.COLUMN_CLOCK_MOVEMENT));
	        	product.setEnableClockMovement(resultSet.getBoolean(DatabaseConstants.COLUMN_ENABLE_CLOCK_MOVEMENT));
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	product.setChangeData(changeData);
	        	product.setAliasProduct(resultSet.getString(DatabaseConstants.COLUMN_ALIAS_PRODUCT)); //17 - IN _alias_product varchar(100),
	        	product.setGameTypeId(resultSet.getInt(DatabaseConstants.COLUMN_GAME_TYPE)); //17 - IN _game_type_id INT,
	        	product.setMachineTypeId(resultSet.getInt(DatabaseConstants.COLUMN_MACHINE_TYPE)); //17 - IN _machine_type_id INT

	        	reportProduct.addItem(product);
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
	public List<Combobox> getAllAutocomplete(final ReportProductDTO reportProduct) throws Exception 
	{
		final List<Combobox> itens = new ArrayList<Combobox>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_id_from BIGINT, 
		query.append("?, "); //03 - IN _product_id_to BIGINT,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _sale_price NUMERIC(19,2),
		query.append("?, "); //06 - IN _cost_price NUMERIC(19,2),
		query.append("?, "); //07 - IN _conversion_factor NUMERIC(19,2),
		query.append("?, "); //08 - IN _product_group_id varchar(100),
		query.append("?, "); //09 - IN _product_sub_group_id varchar(100),
		query.append("?, "); //10 - IN _company_id BIGINT,
		query.append("?, "); //11 - IN _input_movement BIGINT,
		query.append("?, "); //12 - IN _output_movement BIGINT,
		query.append("?, "); //13 - IN _inactive int, 
		query.append("?, "); //14 - IN _user_change BIGINT,
		query.append("?, "); //15 - IN _clock_movement BIGINT, 
		query.append("?, "); //16 - IN _enable_clock_movement BIT,
		query.append("?, "); //17 - IN _user_id BIGINT,
		query.append("?, "); //18 - IN _alias_product varchar(100), 
		query.append("?, "); //19 - IN _game_type_id INT,
		query.append("?  "); //20 - IN _machine_type_id INT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.AUTOCOMPLETE.getType()); //01 - IN _operation INT,
			statement.setLong(2, reportProduct.getProductIdFrom()); //02 - IN _product_id_from BIGINT, 
			statement.setLong(3, reportProduct.getProductIdTo()); //03 - IN _product_id_to BIGINT,
			statement.setString(4, reportProduct.getDescription()); //04 - IN _description varchar(100), 
			statement.setNull(5, Types.NUMERIC); //05 - IN _sale_price NUMERIC(19,2),
			statement.setNull(6, Types.NUMERIC); //06- IN _cost_price NUMERIC(19,2),
			statement.setNull(7, Types.NUMERIC); //07 - IN _conversion_factor NUMERIC(19,2),
			statement.setNull(8, Types.VARCHAR); //08 - IN _product_group_id varchar(100),
			statement.setNull(9, Types.VARCHAR); //09 - IN _product_sub_group_id varchar(100),
			statement.setLong(10, Long.valueOf(reportProduct.getCompanyId())); //10 - IN _company_id BIGINT,
			statement.setNull(11, Types.BIGINT); //11 - IN _input_movement BIGINT,
			statement.setNull(12, Types.BIGINT); //12 - IN _output_movement BIGINT,
			statement.setInt(13, reportProduct.getInactive()); //13 - IN _inactive int, 
			statement.setLong(14, Types.BIGINT); //14 - IN _user_change BIGINT		
			statement.setNull(15, Types.BIGINT); //15 - IN _clock_movement BIGINT, 
			statement.setNull(16, Types.BOOLEAN); //16 - IN _enable_clock_movement BIT,
			statement.setNull(17, Types.BIGINT); //17 - IN _user_id BIGINT,
			statement.setNull(18, Types.VARCHAR); //18 - IN _alias_product varchar(100), 
			statement.setNull(19, Types.INTEGER); //19 - IN _game_type_id INT,
			statement.setNull(20, Types.INTEGER); //20 - IN _machine_type_id INT
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	itens.add(new Combobox(resultSet.getString(DatabaseConstants.COLUMN_ID), resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION)));
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
	public List<Combobox> getAllAutocompleteByCompany(final ReportProductDTO reportProduct) throws Exception 
	{
		final List<Combobox> itens = new ArrayList<Combobox>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_id_from BIGINT, 
		query.append("?, "); //03 - IN _product_id_to BIGINT,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _sale_price NUMERIC(19,2),
		query.append("?, "); //06 - IN _cost_price NUMERIC(19,2),
		query.append("?, "); //07 - IN _conversion_factor NUMERIC(19,2),
		query.append("?, "); //08 - IN _product_group_id varchar(100),
		query.append("?, "); //09 - IN _product_sub_group_id varchar(100),
		query.append("?, "); //10 - IN _company_id BIGINT,
		query.append("?, "); //11 - IN _input_movement BIGINT,
		query.append("?, "); //12 - IN _output_movement BIGINT,
		query.append("?, "); //13 - IN _inactive int, 
		query.append("?, "); //14 - IN _user_change BIGINT,
		query.append("?, "); //15 - IN _clock_movement BIGINT, 
		query.append("?, "); //16 - IN _enable_clock_movement BIT,
		query.append("?, "); //17 - IN _user_id BIGINT,
		query.append("?, "); //18 - IN _alias_product varchar(100), 
		query.append("?, "); //19 - IN _game_type_id INT,
		query.append("?  "); //20 - IN _machine_type_id INT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL_BY_COMPANY.getType()); //01 - IN _operation INT,
			statement.setLong(2, reportProduct.getProductIdFrom()); //02 - IN _product_id_from BIGINT, 
			statement.setLong(3, reportProduct.getProductIdTo()); //03 - IN _product_id_to BIGINT,
			statement.setString(4, reportProduct.getDescription()); //04 - IN _description varchar(100), 
			statement.setNull(5, Types.NUMERIC); //05 - IN _sale_price NUMERIC(19,2),
			statement.setNull(6, Types.NUMERIC); //06- IN _cost_price NUMERIC(19,2),
			statement.setNull(7, Types.NUMERIC); //07 - IN _conversion_factor NUMERIC(19,2),
			statement.setNull(8, Types.VARCHAR); //08 - IN _product_group_id varchar(100),
			statement.setNull(9, Types.VARCHAR); //09 - IN _product_sub_group_id varchar(100),
			statement.setLong(10, Long.valueOf(reportProduct.getCompanyId())); //10 - IN _company_id BIGINT,
			statement.setNull(11, Types.BIGINT); //11 - IN _input_movement BIGINT,
			statement.setNull(12, Types.BIGINT); //12 - IN _output_movement BIGINT,
			statement.setInt(13, reportProduct.getInactive()); //13 - IN _inactive int, 
			statement.setLong(14, Types.BIGINT); //14 - IN _user_change BIGINT		
			statement.setNull(15, Types.BIGINT); //15 - IN _clock_movement BIGINT, 
			statement.setNull(16, Types.BOOLEAN); //16 - IN _enable_clock_movement BIT,
			statement.setLong(17, reportProduct.getUser()); //17 - IN _user_id BIGINT,
			statement.setNull(18, Types.VARCHAR); //18 - IN _alias_product varchar(100), 
			statement.setNull(19, Types.INTEGER); //19 - IN _game_type_id INT,
			statement.setNull(20, Types.INTEGER); //20 - IN _machine_type_id INT
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	itens.add(new Combobox(resultSet.getString(DatabaseConstants.COLUMN_ID), resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION), resultSet.getString(DatabaseConstants.COLUMN_PROCESSING)));
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
	public List<ProductDTO> getAllProductsByCompany(final long companyId) throws Exception {
		final List<ProductDTO> items = new ArrayList<ProductDTO>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_id_from BIGINT, 
		query.append("?, "); //03 - IN _product_id_to BIGINT,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _sale_price NUMERIC(19,2),
		query.append("?, "); //06 - IN _cost_price NUMERIC(19,2),
		query.append("?, "); //07 - IN _conversion_factor NUMERIC(19,2),
		query.append("?, "); //08 - IN _product_group_id varchar(100),
		query.append("?, "); //09 - IN _product_sub_group_id varchar(100),
		query.append("?, "); //10 - IN _company_id BIGINT,
		query.append("?, "); //11 - IN _input_movement BIGINT,
		query.append("?, "); //12 - IN _output_movement BIGINT,
		query.append("?, "); //13 - IN _inactive int, 
		query.append("?, "); //14 - IN _user_change BIGINT,
		query.append("?, "); //15 - IN _clock_movement BIGINT, 
		query.append("?, "); //16 - IN _enable_clock_movement BIT,
		query.append("?, "); //17 - IN _user_id BIGINT,
		query.append("?, "); //18 - IN _alias_product varchar(100), 
		query.append("?, "); //19 - IN _game_type_id INT,
		query.append("?  "); //20 - IN _machine_type_id INT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 18); //01 - IN _operation INT,
			statement.setLong(2, 0); //02 - IN _product_id_from BIGINT, 
			statement.setLong(3, 0); //03 - IN _product_id_to BIGINT,
			statement.setNull(4, Types.VARCHAR); //04 - IN _description varchar(100), 
			statement.setNull(5, Types.NUMERIC); //05 - IN _sale_price NUMERIC(19,2),
			statement.setNull(6, Types.NUMERIC); //06- IN _cost_price NUMERIC(19,2),
			statement.setNull(7, Types.NUMERIC); //07 - IN _conversion_factor NUMERIC(19,2),
			statement.setNull(8, Types.VARCHAR); //08 - IN _product_group_id varchar(100),
			statement.setNull(9, Types.VARCHAR); //09 - IN _product_sub_group_id varchar(100),
			statement.setLong(10, companyId); //10 - IN _company_id BIGINT,
			statement.setNull(11, Types.BIGINT); //11 - IN _input_movement BIGINT,
			statement.setNull(12, Types.BIGINT); //12 - IN _output_movement BIGINT,
			statement.setInt(13, 0); //13 - IN _inactive int, 
			statement.setLong(14, Types.BIGINT); //14 - IN _user_change BIGINT		
			statement.setNull(15, Types.BIGINT); //15 - IN _clock_movement BIGINT, 
			statement.setNull(16, Types.BOOLEAN); //16 - IN _enable_clock_movement BIT,
			statement.setNull(17, Types.BIGINT); //17 - IN _user_id BIGINT,
			statement.setNull(18, Types.VARCHAR); //18 - IN _alias_product varchar(100), 
			statement.setNull(19, Types.INTEGER); //19 - IN _game_type_id INT,
			statement.setNull(20, Types.INTEGER); //20 - IN _machine_type_id INT
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final ProductDTO item = new ProductDTO();
	        	item.setId(resultSet.getString(DatabaseConstants.COLUMN_ID));
	        	item.setInputMovement(resultSet.getString(DatabaseConstants.COLUMN_INPUT_MOVEMENT));
	        	item.setOutputMovement(resultSet.getString(DatabaseConstants.COLUMN_OUTPUT_MOVEMENT));
	        	items.add(item);
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
	public List<MovementProductDTO> getMovmentProductsSystemOld(final String query) throws Exception {
		
		final List<MovementProductDTO> items = new ArrayList<MovementProductDTO>();
		
		PreparedStatement statement = null;		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareStatement(query);     
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final MovementProductDTO item = new MovementProductDTO();
	        	item.setProductId(resultSet.getLong(DatabaseConstants.COLUMN_ID));
	        	item.setInitialInput(resultSet.getLong(DatabaseConstants.COLUMN_INITIAL_INPUT));
	        	item.setFinalInput(resultSet.getLong(DatabaseConstants.COLUMN_FINAL_INPUT));
	        	item.setInitialOutput(resultSet.getLong(DatabaseConstants.COLUMN_INITIAL_OUTPUT));
	        	item.setFinalOutput(resultSet.getLong(DatabaseConstants.COLUMN_FINAL_OUTPUT));
	        	item.setReadingDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(resultSet.getTimestamp(DatabaseConstants.COLUMN_READING_DATE).getTime()), TimeZone.getDefault().toZoneId()));
	        	items.add(item);
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
	public void getAllProducts(final ReportProductDTO reportProduct) throws Exception 
	{
	CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_id_from BIGINT, 
		query.append("?, "); //03 - IN _product_id_to BIGINT,
		query.append("?, "); //04 - IN _description varchar(100), 
		query.append("?, "); //05 - IN _sale_price NUMERIC(19,2),
		query.append("?, "); //06 - IN _cost_price NUMERIC(19,2),
		query.append("?, "); //07 - IN _conversion_factor NUMERIC(19,2),
		query.append("?, "); //08 - IN _product_group_id varchar(100),
		query.append("?, "); //09 - IN _product_sub_group_id varchar(100),
		query.append("?, "); //10 - IN _company_id BIGINT,
		query.append("?, "); //11 - IN _input_movement BIGINT,
		query.append("?, "); //12 - IN _output_movement BIGINT,
		query.append("?, "); //13 - IN _inactive int, 
		query.append("?, "); //14 - IN _user_change BIGINT,
		query.append("?, "); //15 - IN _clock_movement BIGINT, 
		query.append("?, "); //16 - IN _enable_clock_movement BIT,
		query.append("?, "); //17 - IN _user_id BIGINT,
		query.append("?, "); //18 - IN _alias_product varchar(100), 
		query.append("?, "); //19 - IN _game_type_id INT,
		query.append("?  "); //20 - IN _machine_type_id INT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL_PRODUCTS.getType()); //01 - IN _operation INT,
			statement.setLong(2, Types.BIGINT); //02 - IN _product_id_from BIGINT, 
			statement.setLong(3, Types.BIGINT); //03 - IN _product_id_to BIGINT,
			statement.setNull(4, Types.VARCHAR); //04 - IN _description varchar(100), 
			statement.setNull(5, Types.NUMERIC); //05 - IN _sale_price NUMERIC(19,2),
			statement.setNull(6, Types.NUMERIC); //06- IN _cost_price NUMERIC(19,2),
			statement.setNull(7, Types.NUMERIC); //07 - IN _conversion_factor NUMERIC(19,2),
			statement.setNull(8, Types.VARCHAR); //08 - IN _product_group_id varchar(100),
			statement.setNull(9, Types.VARCHAR); //09 - IN _product_sub_group_id varchar(100),
			statement.setString(10, reportProduct.getCompanyId()); //10 - IN _company_id BIGINT,
			statement.setNull(11, Types.BIGINT); //11 - IN _input_movement BIGINT,
			statement.setNull(12, Types.BIGINT); //12 - IN _output_movement BIGINT,
			statement.setInt(13, 0); //13 - IN _inactive int, 
			statement.setLong(14, Types.BIGINT); //14 - IN _user_change BIGINT		
			statement.setNull(15, Types.BIGINT); //15 - IN _clock_movement BIGINT, 
			statement.setNull(16, Types.BOOLEAN); //16 - IN _enable_clock_movement BIT,
			statement.setNull(17, Types.BIGINT); //17 - IN _user_id BIGINT,
			statement.setNull(18, Types.VARCHAR); //18 - IN _alias_product varchar(100), 
			statement.setNull(19, Types.INTEGER); //19 - IN _game_type_id INT,
			statement.setNull(20, Types.INTEGER); //20 - IN _machine_type_id INT
            resultSet = statement.executeQuery();
	        
	        while (resultSet.next()) 
	        {
	        	final long productId = resultSet.getLong(DatabaseConstants.COLUMN_PRODUCT_ID); 
	        	if(productId>0)
	        	{
		        	final ProductDTO product = new ProductDTO();
		        	product.setProductId(productId);
		        	product.setProductDescription(resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_DESCRIPTION));
		            reportProduct.addProduct(reportProduct.getId(), product);	        		        		
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
	public boolean transferProductsToCompany(int companyOrigin, int companyDestiny) throws Exception 
	{
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
			
		query.append("{? = call func_transfer_products_to_company(?, ?)}");
		
		//ResultSet resultSet = null;
		
		boolean result = false;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.registerOutParameter(1, Types.BIT);
			statement.setInt(2, companyOrigin); //01 - IN _operation INT,
			statement.setInt(3, companyDestiny); //02 - IN _product_id_from BIGINT,			
			
			statement.execute();
			result = statement.getBoolean(1);
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