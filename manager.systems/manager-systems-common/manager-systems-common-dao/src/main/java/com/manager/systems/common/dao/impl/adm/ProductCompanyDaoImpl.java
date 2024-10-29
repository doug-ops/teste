package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import com.manager.systems.common.dao.adm.ProductCompanyDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.ProductCompanyDTO;
import com.manager.systems.common.dto.adm.ReportProductCompanyDTO;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.OperationType;

public class ProductCompanyDaoImpl implements ProductCompanyDao 
{
	private Connection connection;
	
	public ProductCompanyDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}

	@Override
	public boolean save(final ProductCompanyDTO productCompany) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product_company");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_id BIGINT,
		query.append("?, "); //03 - IN _company_id BIGINT,
		query.append("?, "); //04 - IN _bank_account_id INT, 		
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?  "); //06 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
			statement.setLong(2, productCompany.getProductId()); //02 - IN _product_id BIGINT,
			statement.setLong(3, productCompany.getCompanyId()); //03 - IN _company_id BIGINT,
			statement.setInt(4, productCompany.getBankAccountId()); //04 - IN _bank_account_id INT, 		
			statement.setInt(5, productCompany.getInactiveInt()); //05 - IN _inactive int, 
			statement.setLong(6, productCompany.getChangeData().getUserChange()); //06 - IN _user_change BIGINT			
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
	public boolean inactive(final ProductCompanyDTO productCompany) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product_company");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_id BIGINT,
		query.append("?, "); //03 - IN _company_id BIGINT,
		query.append("?, "); //04 - IN _bank_account_id INT, 		
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?  "); //06 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.INACTIVE.getType()); //01 - IN _operation INT,
			statement.setLong(2, productCompany.getProductId()); //02 - IN _product_id BIGINT,
			statement.setLong(3, productCompany.getCompanyId()); //03 - IN _company_id BIGINT,
			statement.setInt(4, productCompany.getBankAccountId()); //04 - IN _bank_account_id INT, 		
			statement.setInt(5, productCompany.getInactiveInt()); //05 - IN _inactive int, 
			statement.setLong(6, productCompany.getChangeData().getUserChange()); //06 - IN _user_change BIGINT			
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
	public void get(final ProductCompanyDTO productCompany) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product_company");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_id BIGINT,
		query.append("?, "); //03 - IN _company_id BIGINT,
		query.append("?, "); //04 - IN _bank_account_id INT, 		
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?  "); //06 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); //01 - IN _operation INT,
			statement.setLong(2, productCompany.getProductId()); //02 - IN _product_id BIGINT,
			statement.setLong(3, productCompany.getCompanyId()); //03 - IN _company_id BIGINT,
			statement.setInt(4, productCompany.getBankAccountId()); //04 - IN _bank_account_id INT, 		
			statement.setInt(5, productCompany.getInactiveInt()); //05 - IN _inactive int, 
			statement.setLong(6, productCompany.getChangeData().getUserChange()); //06 - IN _user_change BIGINT			
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	productCompany.setCompanyId(resultSet.getInt(DatabaseConstants.COLUMN_COMPANY_ID));
	        	productCompany.setProductId(resultSet.getInt(DatabaseConstants.COLUMN_PRODUCT_ID));
	        	productCompany.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserCreation(resultSet.getLong(DatabaseConstants.COLUMN_USER_CREATION));
	        	changeData.setCreationDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CREATION_DATE));
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	productCompany.setChangeData(changeData);
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
	public void getAll(final ReportProductCompanyDTO reportProductCompany) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product_company");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_id BIGINT,
		query.append("?, "); //03 - IN _company_id BIGINT,
		query.append("?, "); //04 - IN _bank_account_id INT, 		
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?  "); //06 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - IN _operation INT,
			statement.setLong(2, reportProductCompany.getProductId()); //02 - IN _product_id BIGINT,
			statement.setLong(3, reportProductCompany.getCompanyId()); //03 - IN _company_id BIGINT,
			statement.setInt(4, reportProductCompany.getBankAccountId()); //04 - IN _bank_account_id INT, 		
			statement.setInt(5, reportProductCompany.getInactiveInt()); //05 - IN _inactive int, 
			statement.setLong(6, reportProductCompany.getChangeData().getUserChange()); //06 - IN _user_change BIGINT	
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final ProductCompanyDTO productCompany = new ProductCompanyDTO();
	        	productCompany.setCompanyId(resultSet.getInt(DatabaseConstants.COLUMN_COMPANY_ID));
	        	productCompany.setProductId(resultSet.getInt(DatabaseConstants.COLUMN_PRODUCT_ID));
	        	productCompany.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	productCompany.setChangeData(changeData);
	        	reportProductCompany.addItem(productCompany);
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
	public boolean delete(final ProductCompanyDTO productCompany) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_product_company");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _product_id BIGINT,
		query.append("?, "); //03 - IN _company_id BIGINT,
		query.append("?, "); //04 - IN _bank_account_id INT, 		
		query.append("?, "); //05 - IN _inactive int, 
		query.append("?  "); //06 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.DELETE.getType()); //01 - IN _operation INT,
			statement.setLong(2, productCompany.getProductId()); //02 - IN _product_id BIGINT,
			statement.setLong(3, productCompany.getCompanyId()); //03 - IN _company_id BIGINT,
			statement.setInt(4, productCompany.getBankAccountId()); //04 - IN _bank_account_id INT, 		
			statement.setInt(5, productCompany.getInactiveInt()); //05 - IN _inactive int, 
			statement.setLong(6, productCompany.getChangeData().getUserChange()); //06 - IN _user_change BIGINT			
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