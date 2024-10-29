package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Map;

import com.manager.systems.common.dao.adm.FinancialTransferGroupDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.FinancialTransferGroupDTO;
import com.manager.systems.common.dto.adm.FinancialTransferGroupItemDTO;
import com.manager.systems.common.dto.adm.FinancialTransferProductDTO;
import com.manager.systems.common.dto.adm.ReportFinancialTransferGroupDTO;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.OperationType;

public class FinancialTransferGroupDaoImpl implements FinancialTransferGroupDao 
{
	private Connection connection;
	
	public FinancialTransferGroupDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}
	
	@Override
	public boolean save(final FinancialTransferGroupDTO financialTransfer) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_transfer");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT
		query.append("?, "); //02 - IN _bank_account_origin_id INT
		query.append("?, "); //03 - IN _bank_account_destiny_id INT
		query.append("?, "); //04 - IN _group_id INT
		query.append("?, "); //05 - IN _execution_order_group TINYINT 
		query.append("?, "); //06 - IN _group_description VARCHAR(100)
		query.append("?, "); //07 - IN _inactive TINYINT 
		query.append("?, "); //08 - IN _group_item_id INT		
		query.append("?, "); //09 - IN _execution_order_item TINYINT 
		query.append("?, "); //10 - IN _description_transfer VARCHAR(100)
		query.append("?, "); //11 - IN _provider_id BIGINT
		query.append("?, "); //12 - IN _transfer_type TINYINT
		query.append("?, "); //13 - IN _value_transfer DECIMAL(19,2)
		query.append("?, "); //14 - IN _bank_account_id INT
		query.append("?, "); //15 - IN _credit_debit BIT
		query.append("?, "); //16 - IN _transfer_state TINYINT
		query.append("?, "); //17 - IN _is_over_total BIT
		query.append("?, "); //18 - IN _expense TINYINT
		query.append("?, "); //19 - IN _is_use_remaining_balance BIT
		query.append("?, "); //20 - IN _inactive_transfer INT
		query.append("?, "); //21 - IN _products_id VARCHAR(MAX)
		query.append("?, "); //22 - IN _user_change	BIGINT
		query.append("?, "); //23 - IN _bank_account_automatic_transfer_id	BIGINT
		query.append("?, "); //24 - IN _execution_period INT
		query.append("?, "); //25 - IN _execution_initial_period DATE
		query.append("?, "); //26 - IN _fixed_day BIT
		query.append("?, "); //27 - IN _week_day INT
		query.append("?, "); //28 - IN _automatic_processing BIT
		query.append("?, "); //29 - IN _move_launch_type INT	
		query.append("?, "); //30 - IN _financial_group_id VARCHAR(7)	
		query.append("?, "); //31 - IN _financial_sub_group_id VARCHAR(7)	
		query.append("?, "); //32 - IN _execution_days VARCHAR(50)
		query.append("?, "); //33 - IN _execution_time VARCHAR(5)
		query.append("?, "); //34 - IN _fixed_day_month BIT
		query.append("?, "); //35 - IN _group_product INT
		query.append("?  "); //36 - IN _group_product INT
		
		query.append(")");
		query.append("}");
		
		try
		{
            for(final Map.Entry<Integer, Map<Integer, FinancialTransferGroupItemDTO>> mapItens : financialTransfer.getItens().entrySet())
            {
                for(final Map.Entry<Integer, FinancialTransferGroupItemDTO> subMapItens : mapItens.getValue().entrySet())
                {
                	final FinancialTransferGroupItemDTO item = subMapItens.getValue();
    				statement = this.connection.prepareCall(query.toString());
    				statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
    				statement.setLong(2, financialTransfer.getBankAccountOriginId()); //02 - IN _bank_account_origin_id BIGINT
    	            statement.setLong(3, financialTransfer.getBankAccountDestinyId()); //03 - IN _bank_account_destiny_id BIGINT
                    statement.setInt(4, financialTransfer.getId()); //04 - IN _group_id INT
                    statement.setInt(5, financialTransfer.getOrder()); //05 - IN _execution_order_group TINYINT
                    statement.setString(6, financialTransfer.getDescription()); //06 - IN _description VARCHAR(100)
                    statement.setInt(7, financialTransfer.getInactiveInt()); //07 - IN _inactive INT
                    statement.setInt(8, item.getId()); //08 - IN _group_item_id INT
                    statement.setInt(9, item.getOrder()); //09 - IN _execution_order_item TINYINT 
                    statement.setString(10, item.getDescription()); //10 - IN _description_transfer VARCHAR(100)
                    statement.setLong(11, item.getProviderId()); //11 - IN _provider_id BIGINT
                    statement.setInt(12, item.getTransferType()); //12 - IN _transfer_type TINYINT
                    statement.setDouble(13, item.getValueTransfer()); //13 - IN _value_transfer DECIMAL(19,2)
                    statement.setInt(14, item.getBankAccountId()); //14 - IN _bank_account_id INT
                    statement.setBoolean(15, item.isCreditDebit()); //15 - IN _credit_debit BIT
                    statement.setInt(16, item.getTransferState()); //16 - IN _transfer_state TINYINT
                    statement.setBoolean(17, item.isOverTotal()); //17 - IN _is_over_total BIT
                    statement.setInt(18, item.getExpense()); //18 - IN _expense TINYINT
                    statement.setBoolean(19, item.isUseRemainingBalance()); //19 - IN _is_use_remaining_balance BIT
                    statement.setBoolean(20, item.isInactive()); //20 - IN _inactive_group_item INT
                    statement.setString(21,  financialTransfer.getProductsIdsByGroupItem(financialTransfer.getId())); //21 - IN _products_id VARCHAR(MAX)            	
                    statement.setLong(22,  financialTransfer.getChangeData().getUserChange()); //22 - IN _user_change BIGINT	
                    statement.setInt(23,  financialTransfer.getBankAccountAutomaticTransferId()); //23 - IN _bank_account_automatic_transfer_id	BIGINT 
                    statement.setInt(24,  financialTransfer.getExecutionPeriod()); //24 - IN _execution_period INT
                    statement.setDate(25, new java.sql.Date(financialTransfer.getInitialExecution().getTime())); //25 - IN execution_initial_period DATE);
                    statement.setBoolean(26,  financialTransfer.isFixedDay());   //26 - IN  _fixed_day BIT
                    statement.setInt(27,  financialTransfer.getWeekDay());   //27 - IN _week_day INT
                    statement.setBoolean(28,  financialTransfer.isAutomaticProcessing());   //28 - IN _automatic_processing BIT
                    statement.setInt(29,  item.getLaunchType());   //29 - IN _move_launch_type INT	
                    statement.setString(30, item.getFinantialGroupId()); //30 - IN _financial_group_id VARCHAR(7)	
                    statement.setString(31, item.getFinantialSubGroupId()); //31 - IN _financial_sub_group_id VARCHAR(7)
                    statement.setString(32, financialTransfer.getExecutionDaysString()); //32 - IN _execution_days VARCHAR(50)
                    statement.setString(33, financialTransfer.getExecutionTime()); //33 - IN _execution_time VARCHAR(5)
                    statement.setBoolean(34,  financialTransfer.isFixedDayMonth());   //34 - IN  _fixed_day_month BIT
                    statement.setInt(35,  item.getGroupProductId());   //35 - _group_product INT
                    statement.setInt(36,  item.getSubGroupProductId());   //36 - _group_product INT
        	        result = (statement.executeUpdate()>0);        	        
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
		return result;
	}

	@Override
	public boolean inactive(FinancialTransferGroupDTO financialTransfer) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_transfer");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT
		query.append("?, "); //02 - IN _bank_account_origin_id INT
		query.append("?, "); //03 - IN _bank_account_destiny_id INT
		query.append("?, "); //04 - IN _group_id INT
		query.append("?, "); //05 - IN _execution_order_group TINYINT 
		query.append("?, "); //06 - IN _group_description VARCHAR(100)
		query.append("?, "); //07 - IN _inactive TINYINT 
		query.append("?, "); //08 - IN _group_item_id INT		
		query.append("?, "); //09 - IN _execution_order_item TINYINT 
		query.append("?, "); //10 - IN _description_transfer VARCHAR(100)
		query.append("?, "); //11 - IN _provider_id BIGINT
		query.append("?, "); //12 - IN _transfer_type TINYINT
		query.append("?, "); //13 - IN _value_transfer DECIMAL(19,2)
		query.append("?, "); //14 - IN _bank_account_id INT
		query.append("?, "); //15 - IN _credit_debit BIT
		query.append("?, "); //16 - IN _transfer_state TINYINT
		query.append("?, "); //17 - IN _is_over_total BIT
		query.append("?, "); //18 - IN _expense TINYINT
		query.append("?, "); //19 - IN _is_use_remaining_balance BIT
		query.append("?, "); //20 - IN _inactive_transfer INT
		query.append("?, "); //21 - IN _products_id VARCHAR(MAX)
		query.append("?, "); //22 - IN _user_change	BIGINT
		query.append("?, "); //23 - IN _bank_account_automatic_transfer_id	BIGINT
		query.append("?, "); //24 - IN _execution_period INT
		query.append("?, "); //25 - IN _execution_initial_period DATE
		query.append("?, "); //26 - IN _fixed_day BIT
		query.append("?, "); //27 - IN _week_day INT
		query.append("?, "); //28 - IN _automatic_processing BIT
		query.append("?, "); //29 - IN _move_launch_type INT	
		query.append("?, "); //30 - IN _financial_group_id VARCHAR(7)	
		query.append("?, "); //31 - IN _financial_sub_group_id VARCHAR(7)	
		query.append("?, "); //32 - IN _execution_days VARCHAR(50)
		query.append("?, "); //33 - IN _execution_time VARCHAR(5)
		query.append("?, "); //34 - IN _fixed_day_month BIT
		query.append("?, "); //35 - IN _group_product INT
		query.append("?  "); //36 - IN _group_product INT
		query.append(")");
		query.append("}");
		
		try
		{
            for(final Map.Entry<Integer, Map<Integer, FinancialTransferGroupItemDTO>> mapItens : financialTransfer.getItens().entrySet())
            {
                for(final Map.Entry<Integer, FinancialTransferGroupItemDTO> subMapItens : mapItens.getValue().entrySet())
                {
                	final FinancialTransferGroupItemDTO item = subMapItens.getValue();
    				statement = this.connection.prepareCall(query.toString());
    				statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
    				statement.setLong(2, financialTransfer.getBankAccountOriginId()); //02 - IN _bank_account_origin_id BIGINT
    	            statement.setLong(3, financialTransfer.getBankAccountDestinyId()); //03 - IN _bank_account_destiny_id BIGINT
                    statement.setInt(4, financialTransfer.getId()); //04 - IN _group_id INT
                    statement.setInt(5, financialTransfer.getOrder()); //05 - IN _execution_order_group TINYINT
                    statement.setString(6, financialTransfer.getDescription()); //06 - IN _description VARCHAR(100)
                    statement.setInt(7, financialTransfer.getInactiveInt()); //07 - IN _inactive INT
                    statement.setInt(8, item.getId()); //08 - IN _group_item_id INT
                    statement.setInt(9, item.getOrder()); //09 - IN _execution_order_item TINYINT 
                    statement.setString(10, item.getDescription()); //10 - IN _description_transfer VARCHAR(100)
                    statement.setLong(11, item.getProviderId()); //11 - IN _provider_id BIGINT
                    statement.setInt(12, item.getTransferState()); //12 - IN _transfer_state TINYINT
                    statement.setDouble(13, item.getValueTransfer()); //13 - IN _value_transfer DECIMAL(19,2)
                    statement.setInt(14, item.getBankAccountId()); //14 - IN _bank_account_id INT
                    statement.setBoolean(15, item.isCreditDebit()); //15 - IN _credit_debit BIT
                    statement.setInt(16, item.getTransferState()); //16 - IN _transfer_state TINYINT
                    statement.setBoolean(17, item.isOverTotal()); //17 - IN _is_over_total BIT
                    statement.setInt(18, item.getExpense()); //18 - IN _expense TINYINT
                    statement.setBoolean(19, item.isUseRemainingBalance()); //19 - IN _is_use_remaining_balance BIT
                    statement.setBoolean(20, item.isInactive()); //20 - IN _inactive_group_item INT
                    statement.setString(21,  financialTransfer.getProductsIdsByGroupItem(financialTransfer.getId())); //21 - IN _products_id VARCHAR(MAX)            	
                    statement.setLong(22,  financialTransfer.getChangeData().getUserChange()); //22 - IN _user_change BIGINT
                    statement.setInt(23,  financialTransfer.getBankAccountAutomaticTransferId()); //23 - IN _bank_account_automatic_transfer_id	BIGINT     
                    statement.setInt(24,  financialTransfer.getExecutionPeriod()); //24 - IN _execution_period INT
                    statement.setDate(25, new java.sql.Date(financialTransfer.getInitialExecution().getTime())); //25 - IN execution_initial_period DATE);
                    statement.setBoolean(26,  financialTransfer.isFixedDay());   //26 - IN  _fixed_day BIT
                    statement.setInt(27,  financialTransfer.getWeekDay());   //27 - IN _week_day INT
                    statement.setBoolean(28,  financialTransfer.isAutomaticProcessing());   //28 - IN _automatic_processing BIT
                    statement.setInt(29,  item.getLaunchType());   //29 - IN _move_launch_type INT		
                    statement.setString(30, item.getFinantialGroupId()); //30 - IN _financial_group_id VARCHAR(7)	
                    statement.setString(31, item.getFinantialSubGroupId()); //31 - IN _financial_sub_group_id VARCHAR(7)
                    statement.setString(32, financialTransfer.getExecutionDaysString()); //32 - IN _execution_days VARCHAR(50)
                    statement.setString(33, financialTransfer.getExecutionTime()); //33 - IN _execution_time VARCHAR(5)
                    statement.setBoolean(34,  financialTransfer.isFixedDayMonth());   //34 - IN  _fixed_day_month BIT
                    statement.setInt(35,  item.getGroupProductId());   //35 - _group_product INT
                    statement.setInt(36,  item.getSubGroupProductId());   //36 - _group_product INT
        	        result = (statement.executeUpdate()>0);        	        
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
		return result;
	}

	@Override
	public void get(final FinancialTransferGroupDTO financialTransfer) throws Exception 
	{		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_transfer");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT
		query.append("?, "); //02 - IN _bank_account_origin_id INT
		query.append("?, "); //03 - IN _bank_account_destiny_id INT
		query.append("?, "); //04 - IN _group_id INT
		query.append("?, "); //05 - IN _execution_order_group TINYINT 
		query.append("?, "); //06 - IN _group_description VARCHAR(100)
		query.append("?, "); //07 - IN _inactive TINYINT 
		query.append("?, "); //08 - IN _group_item_id INT		
		query.append("?, "); //09 - IN _execution_order_item TINYINT 
		query.append("?, "); //10 - IN _description_transfer VARCHAR(100)
		query.append("?, "); //11 - IN _provider_id BIGINT
		query.append("?, "); //12 - IN _transfer_type TINYINT
		query.append("?, "); //13 - IN _value_transfer DECIMAL(19,2)
		query.append("?, "); //14 - IN _bank_account_id INT
		query.append("?, "); //15 - IN _credit_debit BIT
		query.append("?, "); //16 - IN _transfer_state TINYINT
		query.append("?, "); //17 - IN _is_over_total BIT
		query.append("?, "); //18 - IN _expense TINYINT
		query.append("?, "); //19 - IN _is_use_remaining_balance BIT
		query.append("?, "); //20 - IN _inactive_transfer INT
		query.append("?, "); //21 - IN _products_id VARCHAR(MAX)
		query.append("?, "); //22 - IN _user_change	BIGINT
		query.append("?, "); //23 - IN _bank_account_automatic_transfer_id	BIGINT
		query.append("?, "); //24 - IN _execution_period INT
		query.append("?, "); //25 - IN _execution_initial_period DATE
		query.append("?, "); //26 - IN _fixed_day BIT
		query.append("?, "); //27 - IN _week_day INT
		query.append("?, "); //28 - IN _automatic_processing BIT
		query.append("?, "); //29 - IN _move_launch_type INT	
		query.append("?, "); //30 - IN _financial_group_id VARCHAR(7)	
		query.append("?, "); //31 - IN _financial_sub_group_id VARCHAR(7)	
		query.append("?, "); //32 - IN _execution_days VARCHAR(50)
		query.append("?, "); //33 - IN _execution_time VARCHAR(5)
		query.append("?, "); //34 - IN _fixed_day_month BIT
		query.append("?, "); //35 - IN _group_product INT
		query.append("?  "); //36 - IN _group_product INT	
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); //01 - IN _operation INT,
			statement.setLong(2, financialTransfer.getBankAccountOriginId()); //02 - IN _bank_account_origin_id BIGINT
            statement.setLong(3, financialTransfer.getBankAccountDestinyId()); //03 - IN _bank_account_destiny_id BIGINT
            if(financialTransfer.getId()>0)
            {
                statement.setInt(4, financialTransfer.getId()); //04 - IN _group_id INT
            }
            else
            {
                statement.setNull(4, Types.INTEGER); //04 - IN _group_id INT            	
            }
            statement.setNull(5, Types.INTEGER); //05 - IN _execution_order_group TINYINT
            statement.setNull(6, Types.VARCHAR); //06 - IN _description VARCHAR(100)
            statement.setNull(7, Types.INTEGER); //07 - IN _inactive INT
            if(financialTransfer.getGroupItemId()>0)
            {
                statement.setInt(8, financialTransfer.getGroupItemId()); //08 - IN _group_item_id INT
            }
            else
            {
                statement.setNull(8, Types.INTEGER); //08 - IN _group_item_id INT            	
            }
            statement.setNull(9, Types.INTEGER); //09 - IN _execution_order_item TINYINT 
            statement.setNull(10, Types.VARCHAR); //10 - IN _description_transfer VARCHAR(100)
            statement.setNull(11, Types.BIGINT); //11 - IN _provider_id BIGINT
            statement.setNull(12, Types.INTEGER); //12 - IN _transfer_state TINYINT
            statement.setNull(13, Types.DECIMAL); //13 - IN _value_transfer DECIMAL(19,2)
            statement.setNull(14, Types.INTEGER); //14 - IN _bank_account_id INT
            statement.setNull(15, Types.BIT); //15 - IN _credit_debit BIT
            statement.setNull(16, Types.INTEGER); //16 - IN _transfer_state TINYINT
            statement.setNull(17, Types.BIT); //17 - IN _is_over_total BIT
            statement.setNull(18, Types.INTEGER); //18 - IN _expense TINYINT
            statement.setNull(19, Types.BIT); //19 - IN _is_use_remaining_balance BIT
            statement.setNull(20, Types.BIT); //20 - IN _inactive_group_item INT
            statement.setNull(21,  Types.VARCHAR); //21 - IN _products_id VARCHAR(MAX)            	
            statement.setNull(22,  Types.BIGINT); //22 - IN _user_change BIGINT	
            statement.setNull(23,  Types.INTEGER); //23 - IN _bank_account_automatic_transfer_id	BIGINT               
            statement.setNull(24,  Types.INTEGER); //24 - IN _execution_period INT
            statement.setNull(25,  Types.DATE);   //25 - IN _execution_initial_period DATE
            statement.setNull(26,  Types.BIT);   //26 - IN _fixed_day BIT
            statement.setNull(27,  Types.INTEGER);   //27 - IN _week_day INT
            statement.setNull(28,  Types.BIT);   //28 - IN _automatic_processing BIT
            statement.setNull(29,  Types.INTEGER);   //29 - IN _move_launch_type INT
            statement.setNull(30, Types.VARCHAR); //30 - IN _financial_group_id VARCHAR(7)	
            statement.setNull(31, Types.VARCHAR); //31 - IN _financial_sub_group_id VARCHAR(7)
            statement.setNull(32, Types.VARCHAR); //32 - IN _execution_days VARCHAR(50)
            statement.setNull(33, Types.VARCHAR); //33 - IN _execution_time VARCHAR(5)
            statement.setNull(34,  Types.BIT);   //34 - IN  _fixed_day_month BIT
            statement.setNull(35,  Types.INTEGER);   //35 - _group_product INT
            statement.setNull(36,  Types.INTEGER);   //36 - _group_product INT
	        resultSet = statement.executeQuery();
	        
	        while (resultSet.next()) 
	        {
	        	financialTransfer.setId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ID));
	        	financialTransfer.setBankAccountOriginId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ORIGIN_ID));
	        	financialTransfer.setBankAccountDestinyId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESTINY_ID));
	        	financialTransfer.setBankAccountOriginDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_ORIGIN_DESCRIPTION));
	        	financialTransfer.setBankAccountDestinyDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESTINY_DESCRIPTION));	        	
	        	financialTransfer.setBankAccountAutomaticTransferId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_AUTOMATIC_TRANSFER_ID));
	        	financialTransfer.setBankAccountAutomaticTransferDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_AUTOMATIC_TRANSFER_DESCRIPTION));
	        	financialTransfer.setDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_DESCRIPTION));
	        	financialTransfer.setOrder(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_EXECUTION_ORDER));
	        	financialTransfer.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE_GROUP));	
	        	financialTransfer.setExecutionPeriod(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_EXECUTION_PERIOD));
	        	financialTransfer.setInitialExecution(resultSet.getDate(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_EXECUTION_INITIAL_PERIOD));
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setUsernameChange(resultSet.getString(DatabaseConstants.COLUMN_USER_CHANGE_NAME));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	financialTransfer.setChangeData(changeData);
	        	financialTransfer.setFixedDay(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_FIXED_DAY));
	        	financialTransfer.setFixedDayMonth(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_FIXED_DAY_MONTH));
	        	financialTransfer.setWeekDay(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_WEEK_DAY));
	        	financialTransfer.setAutomaticProcessing(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_AUTOMATIC_PROCESSING));
	        	financialTransfer.setExecutionDayToArray(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_EXECUTION_DAYS));
	        	financialTransfer.setExecutionTime(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_EXECUTION_TIME));
	        		        	
	        	final int groupItemId = resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ITEM_ID);
	        	if(groupItemId>0)
	        	{
		        	final FinancialTransferGroupItemDTO item = new FinancialTransferGroupItemDTO();
		        	item.setId(groupItemId);
		        	item.setGroupId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ID));
		        	item.setOrder(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ITEM_EXECUTION_ORDER));
		        	item.setProviderId(resultSet.getLong(DatabaseConstants.COLUMN_PROVIDER_ID));
		        	item.setProviderName(resultSet.getString(DatabaseConstants.COLUMN_PROVIDER_NAME));
		        	item.setFinantialGroupId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_ID));
		        	item.setFinantialGroupName(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_DESCRIPTION));
		        	item.setFinantialSubGroupId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_ID));
		        	item.setFinantialSubGroupName(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_DESCRIPTION));
		        	item.setDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_DESCRIPTION_TRANFER));
		        	item.setTransferType(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_TRANSFER_TYPE));
		        	item.setValueTransfer(resultSet.getDouble(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_VALUE_TRANSFER));
		        	item.setBankAccountId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID));
		        	item.setBankAccountDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESCRIPTION));
		        	item.setCreditDebit(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_CREDIT_DEBIT));
		        	item.setTransferState(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_STATE));
		        	item.setOverTotal(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_IS_OVER_TOTAL));
		        	item.setExpense(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_EXPENSE));
		        	item.setUseRemainingBalance(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_IS_USE_REMAINING_BALANCE));
		        	item.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE_TRANSFER));
		        	item.setLaunchType(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_MOVE_LAUNCH_TYPE));		
		        	item.setGroupProductId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_GROUP_PRODUCT_ID));
		        	item.setGroupProductDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_PRODUCT_DESCRIPTION));
		        	item.setSubGroupProductId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_PRODUCT_ID));
		        	item.setSubGroupProductDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_PRODUCT_DESCRIPTION));

		        	financialTransfer.addItem(item);	        		
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
	public void getProductsGroup(final FinancialTransferGroupDTO financialTransfer) throws Exception 
	{		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_transfer");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT
		query.append("?, "); //02 - IN _bank_account_origin_id INT
		query.append("?, "); //03 - IN _bank_account_destiny_id INT
		query.append("?, "); //04 - IN _group_id INT
		query.append("?, "); //05 - IN _execution_order_group TINYINT 
		query.append("?, "); //06 - IN _group_description VARCHAR(100)
		query.append("?, "); //07 - IN _inactive TINYINT 
		query.append("?, "); //08 - IN _group_item_id INT		
		query.append("?, "); //09 - IN _execution_order_item TINYINT 
		query.append("?, "); //10 - IN _description_transfer VARCHAR(100)
		query.append("?, "); //11 - IN _provider_id BIGINT
		query.append("?, "); //12 - IN _transfer_type TINYINT
		query.append("?, "); //13 - IN _value_transfer DECIMAL(19,2)
		query.append("?, "); //14 - IN _bank_account_id INT
		query.append("?, "); //15 - IN _credit_debit BIT
		query.append("?, "); //16 - IN _transfer_state TINYINT
		query.append("?, "); //17 - IN _is_over_total BIT
		query.append("?, "); //18 - IN _expense TINYINT
		query.append("?, "); //19 - IN _is_use_remaining_balance BIT
		query.append("?, "); //20 - IN _inactive_transfer INT
		query.append("?, "); //21 - IN _products_id VARCHAR(MAX)
		query.append("?, "); //22 - IN _user_change	BIGINT
		query.append("?, "); //23 - IN _bank_account_automatic_transfer_id	BIGINT
		query.append("?, "); //24 - IN _execution_period INT
		query.append("?, "); //25 - IN _execution_initial_period DATE
		query.append("?, "); //26 - IN _fixed_day BIT
		query.append("?, "); //27 - IN _week_day INT
		query.append("?, "); //28 - IN _automatic_processing BIT
		query.append("?, "); //29 - IN _move_launch_type INT	
		query.append("?, "); //30 - IN _financial_group_id VARCHAR(7)	
		query.append("?, "); //31 - IN _financial_sub_group_id VARCHAR(7)	
		query.append("?, "); //32 - IN _execution_days VARCHAR(50)
		query.append("?, "); //33 - IN _execution_time VARCHAR(5)
		query.append("?, "); //34 - IN _fixed_day_month BIT
		query.append("?, "); //35 - IN _group_product INT
		query.append("?  "); //36 - IN _group_product INT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL_COMBOBOX.getType()); //01 - IN _operation INT,
			statement.setLong(2, financialTransfer.getBankAccountOriginId()); //02 - IN _bank_account_origin_id BIGINT
            statement.setLong(3, financialTransfer.getBankAccountDestinyId()); //03 - IN _bank_account_destiny_id BIGINT
            statement.setInt(4, financialTransfer.getId()); //04 - IN _group_id INT
            statement.setNull(5, Types.INTEGER); //05 - IN _execution_order_group TINYINT
            statement.setNull(6, Types.VARCHAR); //06 - IN _description VARCHAR(100)
            statement.setNull(7, Types.INTEGER); //07 - IN _inactive INT
            if(financialTransfer.getGroupItemId()>0)
            {
                statement.setInt(8, financialTransfer.getGroupItemId()); //08 - IN _group_item_id INT
            }
            else
            {
                statement.setNull(8, Types.INTEGER); //08 - IN _group_item_id INT            	
            }
            statement.setNull(9, Types.INTEGER); //09 - IN _execution_order_item TINYINT 
            statement.setNull(10, Types.VARCHAR); //10 - IN _description_transfer VARCHAR(100)
            statement.setNull(11, Types.BIGINT); //11 - IN _provider_id BIGINT
            statement.setNull(12, Types.INTEGER); //12 - IN _transfer_state TINYINT
            statement.setNull(13, Types.DECIMAL); //13 - IN _value_transfer DECIMAL(19,2)
            statement.setNull(14, Types.INTEGER); //14 - IN _bank_account_id INT
            statement.setNull(15, Types.BIT); //15 - IN _credit_debit BIT
            statement.setNull(16, Types.INTEGER); //16 - IN _transfer_state TINYINT
            statement.setNull(17, Types.BIT); //17 - IN _is_over_total BIT
            statement.setNull(18, Types.INTEGER); //18 - IN _expense TINYINT
            statement.setNull(19, Types.BIT); //19 - IN _is_use_remaining_balance BIT
            statement.setNull(20, Types.BIT); //20 - IN _inactive_group_item INT
            statement.setNull(21,  Types.VARCHAR); //21 - IN _products_id VARCHAR(MAX)            	
            statement.setNull(22,  Types.BIGINT); //22 - IN _user_change BIGINT	
            statement.setNull(23,  Types.INTEGER); //23 - IN _bank_account_automatic_transfer_id	BIGINT  
            statement.setNull(24,  Types.INTEGER); //24 - IN _execution_period INT
            statement.setNull(25,  Types.DATE);   //25 - IN  execution_initial_period DATE
            statement.setNull(26,  Types.BIT);   //26 - IN  _fixed_day BIT
            statement.setNull(27,  Types.INTEGER);   //27 - IN _week_day INT
            statement.setNull(28,  Types.BIT);   //28 - IN _automatic_processing BIT
            statement.setNull(29,  Types.INTEGER);   //29 - IN _move_launch_type INT	
            statement.setNull(30, Types.VARCHAR); //30 - IN _financial_group_id VARCHAR(7)	
            statement.setNull(31, Types.VARCHAR); //31 - IN _financial_sub_group_id VARCHAR(7)
            statement.setNull(32, Types.VARCHAR); //32 - IN _execution_days VARCHAR(50)
            statement.setNull(33, Types.VARCHAR); //33 - IN _execution_time VARCHAR(5)
            statement.setNull(34,  Types.BIT);   //34 - IN  _fixed_day_month BIT
            statement.setNull(35,  Types.INTEGER);   //35 - _group_product INT
            statement.setNull(36,  Types.INTEGER);   //36 - _group_product INT
	        resultSet = statement.executeQuery();
	        
	        while (resultSet.next()) 
	        {
	        	final long productId = resultSet.getLong(DatabaseConstants.COLUMN_PRODUCT_ID); 
	        	if(productId>0)
	        	{
		        	final FinancialTransferProductDTO product = new FinancialTransferProductDTO();
		        	product.setGroupItem(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ID));
		        	product.setProductId(productId);
		        	product.setProductDescription(resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_PRODUCT_DESCRIPTION));
		        	financialTransfer.addProduct(financialTransfer.getId(), product);	        		        		
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
	public void getAll(final ReportFinancialTransferGroupDTO reportFinancialTransfer) throws Exception 
	{
	CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_transfer");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT
		query.append("?, "); //02 - IN _bank_account_origin_id INT
		query.append("?, "); //03 - IN _bank_account_destiny_id INT
		query.append("?, "); //04 - IN _inactive_group INT 
		query.append("?, "); //05 - IN _group_item INT 
		query.append("?, "); //06 - IN _group_item_order TINYINT 
		query.append("?, "); //07 - IN _group_item_description VARCHAR(100)
		query.append("?, "); //08 - IN _execution_order TINYINT 
		query.append("?, "); //09 - IN _description_transfer VARCHAR(100)
		query.append("?, "); //10 - IN _provider_id BIGINT
		query.append("?, "); //11 - IN _transfer_type TINYINT
		query.append("?, "); //12 - IN _value_transfer DECIMAL(19,2)
		query.append("?, "); //13 - IN _bank_account_id INT
		query.append("?, "); //14 - IN _credit_debit BIT
		query.append("?, "); //15 - IN _transfer_state TINYINT
		query.append("?, "); //16 - IN _is_over_total BIT
		query.append("?, "); //17 - IN _expense TINYINT
		query.append("?, "); //18 - IN _is_variable_expense BIT
		query.append("?, "); //19 - IN _is_use_remaining_balance BIT
		query.append("?, "); //20 - IN _inactive_group_item INT
		query.append("?, "); //21 - IN _products_id VARCHAR(MAX)
		query.append("?, "); //22 - IN _user_change	BIGINT
		query.append("?, "); //23 - IN _bank_account_automatic_transfer_id	BIGINT
		query.append("?, "); //24 - IN _execution_period INT
		query.append("?, "); //25 - IN _execution_initial_period DATE
		query.append("?, "); //26 - IN _fixed_day BIT
		query.append("?, "); //27 - IN _week_day INT
		query.append("?, "); //28 - IN _automatic_processing BIT
		query.append("?, "); //29 - IN _move_launch_type INT	
		query.append("?, "); //30 - IN _financial_group_id VARCHAR(7)	
		query.append("?, "); //31 - IN _financial_sub_group_id VARCHAR(7)	
		query.append("?, "); //32 - IN _execution_days VARCHAR(50)
		query.append("?, "); //33 - IN _execution_time VARCHAR(5)
		query.append("?, "); //34 - IN _fixed_day_month BIT
		query.append("?, "); //35 - IN _group_product INT
		query.append("?  "); //36 - IN _group_product INT
		query.append(")  ");
		query.append("}  ");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - IN _operation INT,
			statement.setLong(2, reportFinancialTransfer.getBankAccountOriginId()); //02 - IN _bank_account_origin_id BIGINT
            statement.setLong(3, reportFinancialTransfer.getBankAccountDestinyId()); //03 - IN _bank_account_destiny_id BIGINT
            if(reportFinancialTransfer.getGroupId()>0)
            {
                statement.setInt(4, reportFinancialTransfer.getGroupId()); //04 - IN _group_id INT
            }
            else
            {
                statement.setNull(4, Types.INTEGER); //04 - IN _group_id INT            	
            }
            statement.setNull(5, Types.INTEGER); //05 - IN _execution_order_group TINYINT
            statement.setNull(6, Types.VARCHAR); //06 - IN _description VARCHAR(100)
            statement.setInt(7, reportFinancialTransfer.isInactive()); //07 - IN _inactive INT
            if(reportFinancialTransfer.getGroupItemId()>0)
            {
                statement.setInt(8, reportFinancialTransfer.getGroupItemId()); //08 - IN _group_item_id INT
            }
            else
            {
                statement.setNull(8, Types.INTEGER); //08 - IN _group_item_id INT            	
            }
            statement.setNull(9, Types.INTEGER); //09 - IN _execution_order_item TINYINT 
            statement.setNull(10, Types.VARCHAR); //10 - IN _description_transfer VARCHAR(100)
            statement.setNull(11, Types.BIGINT); //11 - IN _provider_id BIGINT
            statement.setNull(12, Types.INTEGER); //12 - IN _transfer_state TINYINT
            statement.setNull(13, Types.DECIMAL); //13 - IN _value_transfer DECIMAL(19,2)
            statement.setNull(14, Types.INTEGER); //14 - IN _bank_account_id INT
            statement.setNull(15, Types.BIT); //15 - IN _credit_debit BIT
            statement.setNull(16, Types.INTEGER); //16 - IN _transfer_state TINYINT
            statement.setNull(17, Types.BIT); //17 - IN _is_over_total BIT
            statement.setNull(18, Types.INTEGER); //18 - IN _expense TINYINT
            statement.setNull(19, Types.BIT); //19 - IN _is_use_remaining_balance BIT
            statement.setNull(20, Types.BIT); //20 - IN _inactive_group_item INT
            statement.setNull(21,  Types.VARCHAR); //21 - IN _products_id VARCHAR(MAX)            	
            statement.setLong(22,  reportFinancialTransfer.getUserOperation()); //22 - IN _user_change BIGINT	
            statement.setNull(23,  Types.INTEGER); //23 - IN _bank_account_automatic_transfer_id	BIGINT         
            statement.setNull(24,  Types.INTEGER); //24 - IN _execution_period INT
            statement.setNull(25,  Types.DATE);   //25 - IN  execution_initial_period DATE
            statement.setNull(26,  Types.BIT);   //26 - IN  _fixed_day BIT
            statement.setNull(27,  Types.INTEGER);   //27 - IN _week_day INT
            statement.setNull(28,  Types.BIT);   //28 - IN _automatic_processing BIT
            statement.setNull(29,  Types.INTEGER);   //29 - IN _move_launch_type INT	
            statement.setNull(30, Types.VARCHAR); //30 - IN _financial_group_id VARCHAR(7)	
            statement.setNull(31, Types.VARCHAR); //31 - IN _financial_sub_group_id VARCHAR(7)
            statement.setNull(32, Types.VARCHAR); //32 - IN _execution_days VARCHAR(50)
            statement.setNull(33, Types.VARCHAR); //33 - IN _execution_time VARCHAR(5)
            statement.setNull(34,  Types.BIT);   //34 - IN  _fixed_day_month BIT
            statement.setNull(35,  Types.INTEGER);   //35 - _group_product INT
            statement.setNull(36,  Types.INTEGER);   //36 - _group_product INT
	        resultSet = statement.executeQuery();
	        while (resultSet.next()) 
	        {
	        	final int bankAccountOriginId = resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ORIGIN_ID);
	        	final int bankAccountDestinyId = resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESTINY_ID);
	        	final int groupId = resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ID);
	        	final String key = String.valueOf(bankAccountOriginId)+com.manager.systems.common.utils.ConstantDataManager.PONTO_VIRGULA_STRING+String.valueOf(bankAccountDestinyId);
	        	
	        	FinancialTransferGroupDTO financialTransfer = reportFinancialTransfer.getItens().get(key);
	        	if(financialTransfer==null)
	        	{
	        		financialTransfer = new FinancialTransferGroupDTO();
	        	}	        	
	        	financialTransfer.setBankAccountOriginId(bankAccountOriginId);
	        	financialTransfer.setBankAccountDestinyId(bankAccountDestinyId);
	        	financialTransfer.setBankAccountOriginDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_ORIGIN_DESCRIPTION));
	        	financialTransfer.setBankAccountDestinyDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESTINY_DESCRIPTION));
	        	financialTransfer.setBankAccountAutomaticTransferId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_AUTOMATIC_TRANSFER_ID));
	        	financialTransfer.setBankAccountAutomaticTransferDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_AUTOMATIC_TRANSFER_DESCRIPTION));
	        	financialTransfer.setExecutionPeriod(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_EXECUTION_PERIOD));
	        	financialTransfer.setInitialExecution(resultSet.getDate(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_EXECUTION_INITIAL_PERIOD));
	        	financialTransfer.setId(groupId);
	        	financialTransfer.setDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_DESCRIPTION));
	        	financialTransfer.setOrder(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_EXECUTION_ORDER));
	        	financialTransfer.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE_GROUP));	
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setUsernameChange(resultSet.getString(DatabaseConstants.COLUMN_USER_CHANGE_NAME));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	financialTransfer.setChangeData(changeData);	        	
	        	financialTransfer.setFixedDay(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_FIXED_DAY));
	        	financialTransfer.setFixedDayMonth(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_FIXED_DAY_MONTH));
	        	financialTransfer.setWeekDay(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_WEEK_DAY));
	        	financialTransfer.setAutomaticProcessing(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_AUTOMATIC_PROCESSING));

	        	final int groupItemId = resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ITEM_ID);
	        	if(groupItemId>0)
	        	{
		        	final FinancialTransferGroupItemDTO item = new FinancialTransferGroupItemDTO();
		        	item.setId(groupItemId);
		        	item.setGroupId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ID));
		        	item.setOrder(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ITEM_EXECUTION_ORDER));
		        	item.setProviderId(resultSet.getLong(DatabaseConstants.COLUMN_PROVIDER_ID));
		        	item.setProviderName(resultSet.getString(DatabaseConstants.COLUMN_PROVIDER_NAME));
		        	item.setFinantialGroupId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_ID));
		        	item.setFinantialGroupName(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_DESCRIPTION));
		        	item.setFinantialSubGroupId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_ID));
		        	item.setFinantialSubGroupName(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_DESCRIPTION));
		        	item.setDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_DESCRIPTION_TRANFER));
		        	item.setTransferType(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_TRANSFER_TYPE));
		        	item.setValueTransfer(resultSet.getDouble(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_VALUE_TRANSFER));
		        	item.setBankAccountId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID));
		        	item.setBankAccountDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESCRIPTION));
		        	item.setCreditDebit(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_CREDIT_DEBIT));
		        	item.setTransferState(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_STATE));
		        	item.setOverTotal(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_IS_OVER_TOTAL));
		        	item.setExpense(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_EXPENSE));
		        	item.setUseRemainingBalance(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_IS_USE_REMAINING_BALANCE));
		        	item.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE_TRANSFER));
		        	item.setGroupDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_DESCRIPTION));
		        	item.setGroupExecutionOrder(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_EXECUTION_ORDER));
		        	item.setLaunchType(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_MOVE_LAUNCH_TYPE));
		        	item.setGroupProductId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_GROUP_PRODUCT_ID));
		        	item.setGroupProductDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_PRODUCT_DESCRIPTION));
		        	item.setSubGroupProductId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_PRODUCT_ID));
		        	item.setSubGroupProductDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_PRODUCT_DESCRIPTION));

		        	financialTransfer.addItem(item);	        		

	        	}
	        	reportFinancialTransfer.addItem(key, financialTransfer);
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
	public boolean clone(final FinancialTransferGroupDTO financialTransfer) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_transfer");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT
		query.append("?, "); //02 - IN _bank_account_origin_id INT
		query.append("?, "); //03 - IN _bank_account_destiny_id INT
		query.append("?, "); //04 - IN _group_id INT
		query.append("?, "); //05 - IN _execution_order_group TINYINT 
		query.append("?, "); //06 - IN _group_description VARCHAR(100)
		query.append("?, "); //07 - IN _inactive TINYINT 
		query.append("?, "); //08 - IN _group_item_id INT		
		query.append("?, "); //09 - IN _execution_order_item TINYINT 
		query.append("?, "); //10 - IN _description_transfer VARCHAR(100)
		query.append("?, "); //11 - IN _provider_id BIGINT
		query.append("?, "); //12 - IN _transfer_type TINYINT
		query.append("?, "); //13 - IN _value_transfer DECIMAL(19,2)
		query.append("?, "); //14 - IN _bank_account_id INT
		query.append("?, "); //15 - IN _credit_debit BIT
		query.append("?, "); //16 - IN _transfer_state TINYINT
		query.append("?, "); //17 - IN _is_over_total BIT
		query.append("?, "); //18 - IN _expense TINYINT
		query.append("?, "); //19 - IN _is_use_remaining_balance BIT
		query.append("?, "); //20 - IN _inactive_transfer INT
		query.append("?, "); //21 - IN _products_id VARCHAR(MAX)
		query.append("?, "); //22 - IN _user_change	BIGINT
		query.append("?, "); //23 - IN _bank_account_automatic_transfer_id	BIGINT
		query.append("?, "); //24 - IN _execution_period INT
		query.append("?, "); //25 - IN _execution_initial_period DATE
		query.append("?, "); //26 - IN _fixed_day BIT
		query.append("?, "); //27 - IN _week_day INT
		query.append("?, "); //28 - IN _automatic_processing BIT
		query.append("?, "); //29 - IN _move_launch_type INT	
		query.append("?, "); //30 - IN _financial_group_id VARCHAR(7)	
		query.append("?, "); //31 - IN _financial_sub_group_id VARCHAR(7)	
		query.append("?, "); //32 - IN _execution_days VARCHAR(50)
		query.append("?, "); //33 - IN _execution_time VARCHAR(5)
		query.append("?, "); //34 - IN _fixed_day_month BIT
		query.append("?, "); //35 - IN _group_product INT
		query.append("?  "); //36 - IN _group_product INT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.CLONE.getType()); //01 - IN _operation INT,
			statement.setLong(2, financialTransfer.getBankAccountOriginId()); //02 - IN _bank_account_origin_id BIGINT
            statement.setLong(3, financialTransfer.getBankAccountDestinyId()); //03 - IN _bank_account_destiny_id BIGINT
            statement.setNull(4, Types.INTEGER); //04 - IN _group_id INT
            statement.setNull(5, Types.INTEGER); //05 - IN _execution_order_group TINYINT
            statement.setNull(6, Types.VARCHAR); //06 - IN _description VARCHAR(100)
            statement.setNull(7, Types.INTEGER); //07 - IN _inactive INT
            statement.setNull(8, Types.INTEGER); //08 - IN _group_item_id INT            	
            statement.setNull(9, Types.INTEGER); //09 - IN _execution_order_item TINYINT 
            statement.setNull(10, Types.VARCHAR); //10 - IN _description_transfer VARCHAR(100)
            statement.setNull(11, Types.BIGINT); //11 - IN _provider_id BIGINT
            statement.setNull(12, Types.INTEGER); //12 - IN _transfer_state TINYINT
            statement.setNull(13, Types.DECIMAL); //13 - IN _value_transfer DECIMAL(19,2)
            statement.setNull(14, Types.INTEGER); //14 - IN _bank_account_id INT
            statement.setNull(15, Types.BIT); //15 - IN _credit_debit BIT
            statement.setNull(16, Types.INTEGER); //16 - IN _transfer_state TINYINT
            statement.setNull(17, Types.BIT); //17 - IN _is_over_total BIT
            statement.setNull(18, Types.INTEGER); //18 - IN _expense TINYINT
            statement.setNull(19, Types.BIT); //19 - IN _is_use_remaining_balance BIT
            statement.setNull(20, Types.BIT); //20 - IN _inactive_group_item INT
            statement.setNull(21,  Types.VARCHAR); //21 - IN _products_id VARCHAR(MAX)            	
            statement.setLong(22,  financialTransfer.getChangeData().getUserChange()); //22 - IN _user_change BIGINT
            statement.setNull(23,  Types.INTEGER); //23 - IN _bank_account_automatic_transfer_id	BIGINT  
            statement.setNull(24,  Types.INTEGER); //24 - IN _execution_period INT
            statement.setNull(25,  Types.DATE);   //25 - IN _execution_initial_period DATE
            statement.setNull(26,  Types.BIT);   //26 - IN  _fixed_day BIT
            statement.setNull(27,  Types.INTEGER);   //27 - IN _week_day INT
            statement.setNull(28,  Types.BIT);   //28 - IN _automatic_processing BIT
            statement.setNull(29,  Types.INTEGER);   //29 - IN _move_launch_type INT
            statement.setNull(30, Types.VARCHAR); //30 - IN _financial_group_id VARCHAR(7)	
            statement.setNull(31, Types.VARCHAR); //31 - IN _financial_sub_group_id VARCHAR(7)
            statement.setNull(32, Types.VARCHAR); //32 - IN _execution_days VARCHAR(50)
            statement.setNull(33, Types.VARCHAR); //33 - IN _execution_time VARCHAR(5)
            statement.setNull(34,  Types.BIT);   //34 - IN  _fixed_day_month BIT
            statement.setNull(35,  Types.INTEGER);   //35 - _group_product INT
            statement.setNull(36,  Types.INTEGER);   //36 - _group_product INT
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
	public void getFinancialTransfer(final ReportFinancialTransferGroupDTO reportFinancialTransfer) throws Exception 
	{
	CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_transfer");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT
		query.append("?, "); //02 - IN _bank_account_origin_id INT
		query.append("?, "); //03 - IN _bank_account_destiny_id INT
		query.append("?, "); //04 - IN _inactive_group INT 
		query.append("?, "); //05 - IN _group_item INT 
		query.append("?, "); //06 - IN _group_item_order TINYINT 
		query.append("?, "); //07 - IN _group_item_description VARCHAR(100)
		query.append("?, "); //08 - IN _execution_order TINYINT 
		query.append("?, "); //09 - IN _description_transfer VARCHAR(100)
		query.append("?, "); //10 - IN _provider_id BIGINT
		query.append("?, "); //11 - IN _transfer_type TINYINT
		query.append("?, "); //12 - IN _value_transfer DECIMAL(19,2)
		query.append("?, "); //13 - IN _bank_account_id INT
		query.append("?, "); //14 - IN _credit_debit BIT
		query.append("?, "); //15 - IN _transfer_state TINYINT
		query.append("?, "); //16 - IN _is_over_total BIT
		query.append("?, "); //17 - IN _expense TINYINT
		query.append("?, "); //18 - IN _is_variable_expense BIT
		query.append("?, "); //19 - IN _is_use_remaining_balance BIT
		query.append("?, "); //20 - IN _inactive_group_item INT
		query.append("?, "); //21 - IN _products_id VARCHAR(MAX)
		query.append("?, "); //22 - IN _user_change	BIGINT
		query.append("?, "); //23 - IN _bank_account_automatic_transfer_id	BIGINT
		query.append("?, "); //24 - IN _execution_period INT
		query.append("?, "); //25 - IN _execution_initial_period DATE
		query.append("?, "); //26 - IN _fixed_day BIT
		query.append("?, "); //27 - IN _week_day INT
		query.append("?, "); //28 - IN _automatic_processing BIT
		query.append("?, "); //29 - IN _move_launch_type INT	
		query.append("?, "); //30 - IN _financial_group_id VARCHAR(7)	
		query.append("?, "); //31 - IN _financial_sub_group_id VARCHAR(7)	
		query.append("?, "); //32 - IN _execution_days VARCHAR(50)
		query.append("?, "); //33 - IN _execution_time VARCHAR(5)
		query.append("?, "); //34 - IN _fixed_day_month BIT
		query.append("?, "); //35 - IN _group_product INT
		query.append("?  "); //36 - IN _group_product INT
		query.append(")  ");
		query.append("}  ");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - IN _operation INT,
			statement.setLong(2, reportFinancialTransfer.getBankAccountOriginId()); //02 - IN _bank_account_origin_id BIGINT
            statement.setLong(3, reportFinancialTransfer.getBankAccountDestinyId()); //03 - IN _bank_account_destiny_id BIGINT
            if(reportFinancialTransfer.getGroupId()>0)
            {
                statement.setInt(4, reportFinancialTransfer.getGroupId()); //04 - IN _group_id INT
            }
            else
            {
                statement.setNull(4, Types.INTEGER); //04 - IN _group_id INT            	
            }
            statement.setNull(5, Types.INTEGER); //05 - IN _execution_order_group TINYINT
            statement.setNull(6, Types.VARCHAR); //06 - IN _description VARCHAR(100)
            statement.setInt(7, reportFinancialTransfer.isInactive()); //07 - IN _inactive INT
            if(reportFinancialTransfer.getGroupItemId()>0)
            {
                statement.setInt(8, reportFinancialTransfer.getGroupItemId()); //08 - IN _group_item_id INT
            }
            else
            {
                statement.setNull(8, Types.INTEGER); //08 - IN _group_item_id INT            	
            }
            statement.setNull(9, Types.INTEGER); //09 - IN _execution_order_item TINYINT 
            statement.setNull(10, Types.VARCHAR); //10 - IN _description_transfer VARCHAR(100)
            statement.setNull(11, Types.BIGINT); //11 - IN _provider_id BIGINT
            statement.setNull(12, Types.INTEGER); //12 - IN _transfer_state TINYINT
            statement.setNull(13, Types.DECIMAL); //13 - IN _value_transfer DECIMAL(19,2)
            statement.setNull(14, Types.INTEGER); //14 - IN _bank_account_id INT
            statement.setNull(15, Types.BIT); //15 - IN _credit_debit BIT
            statement.setNull(16, Types.INTEGER); //16 - IN _transfer_state TINYINT
            statement.setNull(17, Types.BIT); //17 - IN _is_over_total BIT
            statement.setNull(18, Types.INTEGER); //18 - IN _expense TINYINT
            statement.setNull(19, Types.BIT); //19 - IN _is_use_remaining_balance BIT
            statement.setNull(20, Types.BIT); //20 - IN _inactive_group_item INT
            statement.setNull(21,  Types.VARCHAR); //21 - IN _products_id VARCHAR(MAX)            	
            statement.setLong(22,  reportFinancialTransfer.getUserOperation()); //22 - IN _user_change BIGINT	
            statement.setNull(23,  Types.INTEGER); //23 - IN _bank_account_automatic_transfer_id	BIGINT         
            statement.setNull(24,  Types.INTEGER); //24 - IN _execution_period INT
            statement.setNull(25,  Types.DATE);   //25 - IN  _execution_initial_period DATE
            statement.setNull(26,  Types.BIT);   //26 - IN  _fixed_day BIT
            statement.setNull(27,  Types.INTEGER);   //27 - IN _week_day INT
            statement.setNull(28,  Types.BIT);   //28 - IN _automatic_processing BIT
            statement.setNull(29,  Types.INTEGER);   //29 - IN _move_launch_type INT
            statement.setNull(30, Types.VARCHAR); //30 - IN _financial_group_id VARCHAR(7)	
            statement.setNull(31, Types.VARCHAR); //31 - IN _financial_sub_group_id VARCHAR(7)
            statement.setNull(32, Types.VARCHAR); //32 - IN _execution_days VARCHAR(50)
            statement.setNull(33, Types.VARCHAR); //33 - IN _execution_time VARCHAR(5)
            statement.setNull(34,  Types.BIT);   //34 - IN  _fixed_day_month BIT
            statement.setNull(35,  Types.INTEGER);   //35 - _group_product INT
            statement.setNull(36,  Types.INTEGER);   //36 - _group_product INT
	        resultSet = statement.executeQuery();
	        while (resultSet.next()) 
	        {
	        	final int bankAccountOriginId = resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ORIGIN_ID);
	        	final int bankAccountDestinyId = resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESTINY_ID);
	        	final int groupId = resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ID);
	        	final String key = String.valueOf(bankAccountOriginId)+com.manager.systems.common.utils.ConstantDataManager.PONTO_VIRGULA_STRING+String.valueOf(bankAccountDestinyId)+com.manager.systems.common.utils.ConstantDataManager.PONTO_VIRGULA_STRING+String.valueOf(groupId);
	        	
	        	FinancialTransferGroupDTO financialTransfer = reportFinancialTransfer.getItens().get(key);
	        	if(financialTransfer==null)
	        	{
	        		financialTransfer = new FinancialTransferGroupDTO();
	        	}	        	
	        	financialTransfer.setBankAccountOriginId(bankAccountOriginId);
	        	financialTransfer.setBankAccountDestinyId(bankAccountDestinyId);
	        	financialTransfer.setBankAccountOriginDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_ORIGIN_DESCRIPTION));
	        	financialTransfer.setBankAccountDestinyDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESTINY_DESCRIPTION));
	        	financialTransfer.setBankAccountAutomaticTransferId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_AUTOMATIC_TRANSFER_ID));
	        	financialTransfer.setBankAccountAutomaticTransferDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_AUTOMATIC_TRANSFER_DESCRIPTION));
	        	financialTransfer.setExecutionPeriod(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_EXECUTION_PERIOD));
	        	financialTransfer.setInitialExecution(resultSet.getDate(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_EXECUTION_INITIAL_PERIOD));
	        	financialTransfer.setId(groupId);
	        	financialTransfer.setDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_DESCRIPTION));
	        	financialTransfer.setOrder(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_EXECUTION_ORDER));
	        	financialTransfer.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE_GROUP));		        	
	        	financialTransfer.setExecutionDayToArray(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_EXECUTION_DAYS));
	        	financialTransfer.setExecutionTime(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_EXECUTION_TIME));
	        	
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setUsernameChange(resultSet.getString(DatabaseConstants.COLUMN_USER_CHANGE_NAME));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	financialTransfer.setChangeData(changeData);
	        	financialTransfer.setFixedDay(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_FIXED_DAY));
	        	financialTransfer.setFixedDayMonth(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_FIXED_DAY_MONTH));	        	
	        	financialTransfer.setWeekDay(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_WEEK_DAY));
	        	financialTransfer.setAutomaticProcessing(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_AUTOMATIC_PROCESSING));
	        	
	        	final int groupItemId = resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ITEM_ID);
	        	if(groupItemId>0)
	        	{
		        	final FinancialTransferGroupItemDTO item = new FinancialTransferGroupItemDTO();
		        	item.setId(groupItemId);
		        	item.setGroupId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ID));
		        	item.setOrder(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ITEM_EXECUTION_ORDER));
		        	item.setProviderId(resultSet.getLong(DatabaseConstants.COLUMN_PROVIDER_ID));
		        	item.setProviderName(resultSet.getString(DatabaseConstants.COLUMN_PROVIDER_NAME));
		        	item.setFinantialGroupId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_ID));
		        	item.setFinantialGroupName(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_DESCRIPTION));
		        	item.setFinantialSubGroupId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_ID));
		        	item.setFinantialSubGroupName(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_DESCRIPTION));
		        	item.setDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_DESCRIPTION_TRANFER));
		        	item.setTransferType(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_TRANSFER_TYPE));
		        	item.setValueTransfer(resultSet.getDouble(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_VALUE_TRANSFER));
		        	item.setBankAccountId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID));
		        	item.setBankAccountDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESCRIPTION));
		        	item.setCreditDebit(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_CREDIT_DEBIT));
		        	item.setTransferState(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_STATE));
		        	item.setOverTotal(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_IS_OVER_TOTAL));
		        	item.setExpense(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_EXPENSE));
		        	item.setUseRemainingBalance(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_IS_USE_REMAINING_BALANCE));
		        	item.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE_TRANSFER));
		        	item.setGroupDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_DESCRIPTION));
		        	item.setGroupExecutionOrder(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_EXECUTION_ORDER));
		        	item.setLaunchType(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_MOVE_LAUNCH_TYPE));
		        	item.setGroupProductId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_GROUP_PRODUCT_ID));
		        	item.setGroupProductDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_PRODUCT_DESCRIPTION));
		        	item.setSubGroupProductId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_PRODUCT_ID));
		        	item.setSubGroupProductDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_PRODUCT_DESCRIPTION));

		        	financialTransfer.addItem(item);
	        	}
	        	reportFinancialTransfer.addItem(key, financialTransfer);
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
	public FinancialTransferGroupDTO getGroupFinancialTransfer(final ReportFinancialTransferGroupDTO reportFinancialTransfer) throws Exception 
	{
	CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_transfer_offline");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT
		query.append("?, "); //02 - IN _bank_account_origin_id INT
		query.append("?, "); //03 - IN _bank_account_destiny_id INT
		query.append("?, "); //04 - IN _group_id INT 
		query.append("?, "); //05 - IN _group_item_id INT 
		query.append("?, "); //06 - IN _company_id BIGINT 
		query.append("?, "); //07 - IN _value_transfer DECIMAL(19,2)
		query.append("?, "); //08 - IN _expense INT
		query.append("?, "); //09 - IN _inactive INT 
		query.append("?  "); //10 - IN _user_change BIGINT
		query.append(")  ");
		query.append("}  ");
		
		ResultSet resultSet = null;
		
		FinancialTransferGroupDTO financialTransfer = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - IN _operation INT,
			statement.setLong(2, reportFinancialTransfer.getBankAccountOriginId()); //02 - IN _bank_account_origin_id BIGINT
            statement.setLong(3, reportFinancialTransfer.getBankAccountDestinyId()); //03 - IN _bank_account_destiny_id BIGINT
            statement.setInt(4, reportFinancialTransfer.getGroupId()); //04 - IN _group_id INT
            statement.setInt(5, reportFinancialTransfer.getGroupItemId()); //05 - IN _group_item_id INT
            statement.setLong(6, reportFinancialTransfer.getCompanyId()); //06 - IN _company_id BIGINT 
            statement.setDouble(7, reportFinancialTransfer.getTransferValue()); //07 - IN _value_transfer DECIMAL(19,2)
            statement.setInt(8, reportFinancialTransfer.getExpense()); //08 - IN _expense INT
            statement.setInt(9, reportFinancialTransfer.isInactive()); //09 - IN _inactive INT 
            statement.setLong(10, reportFinancialTransfer.getUserOperation()); //10 - IN _user_change BIGINT
            
	        resultSet = statement.executeQuery();
	        while (resultSet.next()) 
	        {
	        	if(financialTransfer == null) {
	        		financialTransfer = new FinancialTransferGroupDTO();
	        	}
	        	final int bankAccountOriginId = resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ORIGIN_ID);
	        	final int bankAccountDestinyId = resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESTINY_ID);
	        	final int groupId = resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ID);
	        	        		        	
	        	financialTransfer.setBankAccountOriginId(bankAccountOriginId);
	        	financialTransfer.setBankAccountDestinyId(bankAccountDestinyId);
	        	financialTransfer.setBankAccountOriginDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_ORIGIN_DESCRIPTION));
	        	financialTransfer.setBankAccountDestinyDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESTINY_DESCRIPTION));
	        	financialTransfer.setBankAccountAutomaticTransferId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_AUTOMATIC_TRANSFER_ID));
	        	financialTransfer.setBankAccountAutomaticTransferDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_AUTOMATIC_TRANSFER_DESCRIPTION));
	        	financialTransfer.setExecutionPeriod(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_EXECUTION_PERIOD));
	        	financialTransfer.setInitialExecution(resultSet.getDate(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_EXECUTION_INITIAL_PERIOD));
	        	financialTransfer.setId(groupId);
	        	financialTransfer.setDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_DESCRIPTION));
	        	financialTransfer.setOrder(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_EXECUTION_ORDER));
	        	financialTransfer.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE_GROUP));	
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setUsernameChange(resultSet.getString(DatabaseConstants.COLUMN_USER_CHANGE_NAME));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	financialTransfer.setChangeData(changeData);
	        	
	        	final int groupItemId = resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ITEM_ID);
	        	if(groupItemId>0)
	        	{
		        	final FinancialTransferGroupItemDTO item = new FinancialTransferGroupItemDTO();
		        	item.setId(groupItemId);
		        	item.setGroupId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ID));
		        	item.setOrder(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ITEM_EXECUTION_ORDER));
		        	item.setProviderId(resultSet.getLong(DatabaseConstants.COLUMN_PROVIDER_ID));
		        	item.setProviderName(resultSet.getString(DatabaseConstants.COLUMN_PROVIDER_NAME));		        	
		        	item.setDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_DESCRIPTION_TRANFER));
		        	item.setTransferType(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_TRANSFER_TYPE));
		        	item.setValueTransfer(resultSet.getDouble(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_VALUE_TRANSFER));
		        	item.setBankAccountId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID));
		        	item.setBankAccountDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESCRIPTION));
		        	item.setCreditDebit(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_CREDIT_DEBIT));
		        	item.setTransferState(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_STATE));
		        	item.setOverTotal(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_IS_OVER_TOTAL));
		        	item.setExpense(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_EXPENSE));
		        	item.setUseRemainingBalance(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_IS_USE_REMAINING_BALANCE));
		        	item.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE_TRANSFER));
		        	item.setGroupDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_DESCRIPTION));
		        	item.setGroupExecutionOrder(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_EXECUTION_ORDER));
		        	item.setGroupProductId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_GROUP_PRODUCT_ID));
		        	item.setGroupProductDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_PRODUCT_DESCRIPTION));
		        	item.setSubGroupProductId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_PRODUCT_ID));
		        	item.setSubGroupProductDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_PRODUCT_DESCRIPTION));

		        	financialTransfer.addItem(item);
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
		
		return financialTransfer;
	}
	
	@Override
	public boolean saveGroupFinancialTransfer(final ReportFinancialTransferGroupDTO reportFinancialTransfer) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_financial_transfer_offline");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT
		query.append("?, "); //02 - IN _bank_account_origin_id INT
		query.append("?, "); //03 - IN _bank_account_destiny_id INT
		query.append("?, "); //04 - IN _group_id INT 
		query.append("?, "); //05 - IN _group_item_id INT 
		query.append("?, "); //06 - IN _company_id BIGINT 
		query.append("?, "); //07 - IN _value_transfer DECIMAL(19,2)
		query.append("?, "); //08 - IN _expense INT
		query.append("?, "); //09 - IN _inactive INT 
		query.append("?  "); //10 - IN _user_change BIGINT
		query.append(")  ");
		query.append("}  ");
		
		boolean result = false;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
			statement.setLong(2, reportFinancialTransfer.getBankAccountOriginId()); //02 - IN _bank_account_origin_id BIGINT
            statement.setLong(3, reportFinancialTransfer.getBankAccountDestinyId()); //03 - IN _bank_account_destiny_id BIGINT
            statement.setInt(4, reportFinancialTransfer.getGroupId()); //04 - IN _group_id INT
            statement.setInt(5, reportFinancialTransfer.getGroupItemId()); //05 - IN _group_item_id INT
            statement.setLong(6, reportFinancialTransfer.getCompanyId()); //06 - IN _company_id BIGINT 
            statement.setDouble(7, reportFinancialTransfer.getTransferValue()); //07 - IN _value_transfer DECIMAL(19,2)
            statement.setInt(8, reportFinancialTransfer.getExpense()); //08 - IN _expense INT
            statement.setInt(9, reportFinancialTransfer.isInactive()); //09 - IN _inactive INT 
            statement.setLong(10, reportFinancialTransfer.getUserOperation()); //10 - IN _user_change BIGINT
            
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