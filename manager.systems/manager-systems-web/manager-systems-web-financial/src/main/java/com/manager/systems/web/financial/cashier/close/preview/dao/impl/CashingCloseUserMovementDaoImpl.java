package com.manager.systems.web.financial.cashier.close.preview.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.web.financial.cashier.close.preview.dao.CashingCloseUserMovementDao;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashierClosingPreviewFinishRequest;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserMovementDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserMovementFilterDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserWeekMovementCompanyDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserWeekMovementCompanyItemDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserWeekMovementCompanyLaunchDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserWeekMovementDTO;

public class CashingCloseUserMovementDaoImpl implements CashingCloseUserMovementDao {
	
	private Connection connection;

	public CashingCloseUserMovementDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}
	
	@Override
	public List<CashingCloseUserMovementDTO> getCashingCloseUserMovements(final CashingCloseUserMovementFilterDTO filter) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;

		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_cashier_closing_get_movement");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT
		query.append("?, "); // 02 - IN _user_id BIGINT
		query.append("?, "); // 03 - IN _status INT
		query.append("?, "); // 04 - IN _users_children_parent VARCHAR(5000)
		query.append("?, "); // 05 - IN _companys_id VARCHAR(5000)
		query.append("?  "); // 06 - IN _week_year INT
		query.append(")");
		query.append("}");
		
		final List<CashingCloseUserMovementDTO> items = new ArrayList<>();

		try {
			
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, filter.getOperation()); // 01 - IN _operation INT
			statement.setLong(2, filter.getUserId()); // 02 - IN _user_id BIGINT
			statement.setInt(3, filter.getStatus()); // 03 - IN _status INT
			statement.setString(4, filter.getUsersChildrenParent()); // 04 - IN _users_children_parent VARCHAR(5000)
			statement.setString(5, filter.getCompanysId()); // 05 - IN _companys_id VARCHAR(5000)
			statement.setInt(6, filter.getWeekYear()); // 06 - IN _week_year INT
			
			resultSet = statement.executeQuery();
			
			Map<Long, CashingCloseUserMovementDTO> cashMap = new TreeMap<Long, CashingCloseUserMovementDTO>();
        	while (resultSet.next()) 
	        {
        		final long cashClosingId = resultSet.getLong(DatabaseConstants.COLUMN_CASHIER_CLOSING_ID);
        		CashingCloseUserMovementDTO cash = cashMap.get(cashClosingId);
        		if(cash == null) {
        			cash = CashingCloseUserMovementDTO.builder()
            				.cashierClosingId(cashClosingId)
            				.userId(resultSet.getLong(DatabaseConstants.COLUMN_USER_ID))
            				.weekYear(resultSet.getInt(DatabaseConstants.COLUMN_WEEK_YEAR))
            				.dateFrom(resultSet.getString(DatabaseConstants.COLUMN_DATE_FROM))
            				.dateTo(resultSet.getString(DatabaseConstants.COLUMN_DATE_TO))
            				.cashierClosingStatus(resultSet.getInt(DatabaseConstants.COLUMN_CASHIER_CLOSING_STATUS))        		
            				.build();
        		}
        		cash.sumStatusCompany(resultSet.getInt(DatabaseConstants.COLUMN_CASHIER_CLOSING_COMPANY_STATUS));
        		cash.setLabel();
        		cashMap.put(cashClosingId, cash);
	        }
        	
        	items.addAll(cashMap.values().stream().sorted(Comparator.comparingLong(CashingCloseUserMovementDTO::getCashierClosingId).reversed()).limit(3).collect(Collectors.toList()));
        	
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		
		return items;
	}

	@Override
	public void generateMovementUser(final CashingCloseUserMovementFilterDTO filter) throws Exception {
		CallableStatement statement = null;

		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_cashier_closing_generate_movement");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT
		query.append("?, "); // 02 - IN _user_id BIGINT
		query.append("?, "); // 03 - IN _users_children_parent VARCHAR(5000)
		query.append("?, "); // 04 - IN _week_year INT
		query.append("?  "); // 05 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try {
			
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 1); // 01 - IN _operation INT
			statement.setLong(2, filter.getUserId()); // 02 - IN _user_id BIGINT
			statement.setString(3, filter.getUsersChildrenParent()); // 03 - IN _users_children_parent VARCHAR(5000)
			statement.setInt(4, filter.getWeekYear()); // 04 - IN _week_year INT
			statement.setLong(5, filter.getUserChange()); // 05 - IN _user_change BIGINT
			
			statement.executeUpdate();
	        
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}
	
	@Override
	public CashingCloseUserWeekMovementDTO getCashingCloseUserMovementsByWeek(final CashingCloseUserMovementFilterDTO filter) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;

		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_cashier_closing_get_movement");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT
		query.append("?, "); // 02 - IN _user_id BIGINT
		query.append("?, "); // 03 - IN _status INT
		query.append("?, "); // 04 - IN _users_children_parent VARCHAR(5000)
		query.append("?, "); // 05 - IN _companys_id VARCHAR(5000)
		query.append("?  "); // 06 - IN _week_year INT
		query.append(")");
		query.append("}");
		
		final CashingCloseUserWeekMovementDTO report = CashingCloseUserWeekMovementDTO.builder().build();

		try {
			
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 3); // 01 - IN _operation INT
			statement.setLong(2, filter.getUserId()); // 02 - IN _user_id BIGINT
			statement.setInt(3, filter.getStatus()); // 03 - IN _status INT
			statement.setString(4, filter.getUsersChildrenParent()); // 04 - IN _users_children_parent VARCHAR(5000)
			statement.setString(5, filter.getCompanysId()); // 05 - IN _companys_id VARCHAR(5000)
			statement.setInt(6, filter.getWeekYear()); // 06 - IN _week_year INT
			
			int count = 0;
	        boolean hasResults = statement.execute();  
	        do
	        {
	        	if(hasResults)
	        	{
		        	resultSet = statement.getResultSet();
		        	while (resultSet.next()) 
			        {
		        		if(0 == count)
		        		{
		        			report.setCashierClosingId(resultSet.getLong(DatabaseConstants.COLUMN_CASHIER_CLOSING_ID));
		        			report.setUserId(resultSet.getLong(DatabaseConstants.COLUMN_USER_ID));
		        			report.setWeekYear(resultSet.getInt(DatabaseConstants.COLUMN_WEEK_YEAR));
		        			report.setDateFrom(resultSet.getString(DatabaseConstants.COLUMN_DATE_FROM));
		        			report.setDateTo(resultSet.getString(DatabaseConstants.COLUMN_DATE_TO));
		        			report.setCashierClosingStatus(resultSet.getString(DatabaseConstants.COLUMN_CASHIER_CLOSING_STATUS));
		        			
		        			final ChangeData changeData = new ChangeData();
		        			changeData.setUserCreation(resultSet.getLong(DatabaseConstants.COLUMN_USER_CREATION));
		        			changeData.setCreationDate(resultSet.getDate(DatabaseConstants.COLUMN_CREATION_DATE));
		        			changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
		        			changeData.setChangeDate(resultSet.getDate(DatabaseConstants.COLUMN_CHANGE_DATE));		        			
		        			report.setChangeData(changeData);
		        			report.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
		        		} 
		        		else if(1 == count)
		        		{
		        			final CashingCloseUserWeekMovementCompanyDTO company = CashingCloseUserWeekMovementCompanyDTO.builder().build();
		        			company.setCashierClosingId(resultSet.getLong(DatabaseConstants.COLUMN_CASHIER_CLOSING_ID));
		        			company.setUserId(resultSet.getLong(DatabaseConstants.COLUMN_USER_ID));
		        			company.setWeekYear(resultSet.getInt(DatabaseConstants.COLUMN_WEEK_YEAR));
		        			company.setDateFrom(resultSet.getString(DatabaseConstants.COLUMN_DATE_FROM));
		        			company.setDateTo(resultSet.getString(DatabaseConstants.COLUMN_DATE_TO));
		        			company.setCompanyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID));
		        			company.setCompanyDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION));
		        			company.setBankAccountId(resultSet.getLong(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID));
		        			company.setBankAccountDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESCRIPTION));
		        			company.setCashierClosingStatus(resultSet.getString(DatabaseConstants.COLUMN_CASHIER_CLOSING_STATUS));
		        			if(filter.isClose()) {
			        			company.setMovementValue(resultSet.getDouble(DatabaseConstants.COLUMN_MOVEMENT_VALUE));
			        			company.setPendingMovementValue(resultSet.getDouble(DatabaseConstants.COLUMN_PENDING_MOVEMENT_VALUE));
			        			company.setDiscountTotal(resultSet.getDouble(DatabaseConstants.COLUMN_DISCOUNT_TOTAL));
			        			company.setTotalCompany(resultSet.getDouble(DatabaseConstants.COLUMN_TOTAL_COMPANY));
			        			if("A".equalsIgnoreCase(company.getCashierClosingStatus())) {
				        			company.setPaymentTotal(0);
				        			company.setPendingMovementAfterValue(resultSet.getDouble(DatabaseConstants.COLUMN_PAYMENT_TOTAL));
			        			} else {
				        			company.setPaymentTotal(resultSet.getDouble(DatabaseConstants.COLUMN_PAYMENT_TOTAL));
				        			company.setPendingMovementAfterValue(resultSet.getDouble(DatabaseConstants.COLUMN_PENDING_MOVEMENT_AFTER_VALUE));			        				
			        			}
			        			company.calculatePaymentTotal();
		        			} else {
			        			company.setMovementValue(resultSet.getDouble(DatabaseConstants.COLUMN_MOVEMENT_VALUE));
			        			company.setPendingMovementValue(resultSet.getDouble(DatabaseConstants.COLUMN_PENDING_MOVEMENT_VALUE));
			        			company.setTotalCompany(resultSet.getDouble(DatabaseConstants.COLUMN_TOTAL_COMPANY));
			        			company.setDiscountTotal(resultSet.getDouble(DatabaseConstants.COLUMN_DISCOUNT_TOTAL));
			        			company.setPaymentTotal(resultSet.getDouble(DatabaseConstants.COLUMN_PAYMENT_TOTAL));
			        			company.setPendingMovementAfterValue(resultSet.getDouble(DatabaseConstants.COLUMN_PENDING_MOVEMENT_AFTER_VALUE));			        
			        			company.calculatePaymentTotal();
		        			}
		        			company.setKey(resultSet.getLong(DatabaseConstants.COLUMN_CASHIER_CLOSING_ID) + ConstantDataManager.UNDERSCORE + resultSet.getInt(DatabaseConstants.COLUMN_WEEK_YEAR) + ConstantDataManager.UNDERSCORE + resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID));
		        			
		        			final ChangeData changeData = new ChangeData();
		        			changeData.setUserCreation(resultSet.getLong(DatabaseConstants.COLUMN_USER_CREATION));
		        			changeData.setCreationDate(resultSet.getDate(DatabaseConstants.COLUMN_CREATION_DATE));
		        			changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
		        			changeData.setChangeDate(resultSet.getDate(DatabaseConstants.COLUMN_CHANGE_DATE));		        			
		        			company.setChangeData(changeData);
		        			company.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
		        			
		        			company.setLaunchDate(resultSet.getString(DatabaseConstants.COLUMN_LAUNCH_DATE));
		        			company.setCloseDate(resultSet.getString(DatabaseConstants.COLUMN_CLOSE_DATE));
		        					        			
		        			report.addMovementCompany(company);
		        		}
		        		else
		        		{
		        			final CashingCloseUserWeekMovementCompanyItemDTO item = CashingCloseUserWeekMovementCompanyItemDTO.builder().build();
		        			item.setCashierClosingId(resultSet.getLong(DatabaseConstants.COLUMN_CASHIER_CLOSING_ID));
		        			item.setUserId(resultSet.getLong(DatabaseConstants.COLUMN_USER_ID));
		        			item.setWeekYear(resultSet.getInt(DatabaseConstants.COLUMN_WEEK_YEAR));
		        			item.setDateFrom(resultSet.getString(DatabaseConstants.COLUMN_DATE_FROM));
		        			item.setDateTo(resultSet.getString(DatabaseConstants.COLUMN_DATE_TO));
		        			item.setCompanyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID));
		        			item.setDocumentMovementId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_MOVEMENT_ID));
		        			item.setDocumentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID));
		        			item.setResidue(resultSet.getBoolean(DatabaseConstants.COLUMN_RESIDUE));
		        			item.setProductMovement(resultSet.getBoolean(DatabaseConstants.COLUMN_IS_PRODUCT_MOVEMENT));
		        			item.setProductId(resultSet.getLong(DatabaseConstants.COLUMN_PRODUCT_ID));
		        			item.setDocumentValue(resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
		        			item.setCredit(resultSet.getBoolean(DatabaseConstants.COLUMN_CREDIT));
		        			item.setDocumentNote(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE));
		        			
		        			final ChangeData changeData = new ChangeData();
		        			changeData.setUserCreation(resultSet.getLong(DatabaseConstants.COLUMN_USER_CREATION));
		        			changeData.setCreationDate(resultSet.getDate(DatabaseConstants.COLUMN_CREATION_DATE));
		        			changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
		        			changeData.setChangeDate(resultSet.getDate(DatabaseConstants.COLUMN_CHANGE_DATE));		        			
		        			item.setChangeData(changeData);
		        			item.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
		        			
		        			report.addMovementCompanyItem(item);		
		        		}
			        }
	        	}	        	
	        	hasResults = statement.getMoreResults();
	        	count++;
	        } while (hasResults);
        	
	        
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		
		return report;
	}

	@Override
	public void saveMovementHeader(final CashingCloseUserWeekMovementDTO movement) throws Exception {
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_cashier_closing_save_movement");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _cashier_closing_id BIGINT,
		query.append("?, "); //03 - IN _week_year INT,
		query.append("?, "); //04 - IN _movement_value DECIMAL(19,2), 
		query.append("?, "); //05 - IN _pending_movement_value DECIMAL(19,2), 
		query.append("?, "); //06 - IN _total_company DECIMAL(19,2),		
		query.append("?, "); //07 - IN _discount_total DECIMAL(19,2),
		query.append("?, "); //08 - IN _payment_total DECIMAL(19,2),
		query.append("?, "); //09 - IN _pending_movement_after_value DECIMAL(19,2),
		query.append("?, "); //10 - IN _status INT,
		query.append("?, "); //11 - IN _user_change BIGINT,
		query.append("?, "); //12 - IN _note_cash VARCHAR(2000),
		query.append("?, "); //13 - IN _note_company VARCHAR(2000),
		query.append("?  "); //14 - IN _company_id BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 1); //01 - IN _operation INT,
			statement.setLong(2, movement.getCashierClosingId());     //02 - IN _cashier_closing_id BIGINT,
			statement.setInt(3, movement.getWeekYear());     //03 - IN _week_year INT, 
			statement.setDouble(4, 0); //04 - IN _movement_value DECIMAL(19,2),  
			statement.setDouble(5, 0); //05 - IN _pending_movement_value DECIMAL(19,2),  
			statement.setDouble(6, 0); //06 - IN _total_company DECIMAL(19,2), 	
			statement.setDouble(7, 0); //07 - IN _discount_total DECIMAL(19,2),			
			statement.setDouble(8, 0); //08 - IN _payment_total DECIMAL(19,2),
			statement.setDouble(9, 0); //09 - IN _pending_movement_after_value DECIMAL(19,2),
			statement.setInt(10, movement.getStatus()); //09 - IN _status INT,
			statement.setLong(11, movement.getChangeData().getUserChange());    //10 - IN _user_change BIGINT,
			statement.setString(12, movement.getNote()); //11 - IN _note_cash VARCHAR(2000),
			statement.setString(13, ConstantDataManager.BLANK); //12 - IN _note_company VARCHAR(2000)
			statement.setLong(14, 0); // 13 - IN _company_id BIGINT
	        statement.executeUpdate();  
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
	}

	@Override
	public void saveMovementCompanys(final List<CashingCloseUserWeekMovementCompanyDTO> movementCompanys) throws Exception {
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_cashier_closing_save_movement");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _cashier_closing_id BIGINT,
		query.append("?, "); //03 - IN _week_year INT,
		query.append("?, "); //04 - IN _movement_value DECIMAL(19,2), 
		query.append("?, "); //05 - IN _pending_movement_value DECIMAL(19,2), 
		query.append("?, "); //06 - IN _total_company DECIMAL(19,2),		
		query.append("?, "); //07 - IN _discount_total DECIMAL(19,2),
		query.append("?, "); //08 - IN _payment_total DECIMAL(19,2),
		query.append("?, "); //09 - IN _pending_movement_after_value DECIMAL(19,2),
		query.append("?, "); //10 - IN _status INT,
		query.append("?, "); //11 - IN _user_change BIGINT,
		query.append("?, "); //12 - IN _note_cash VARCHAR(2000),
		query.append("?, "); //13 - IN _note_company VARCHAR(2000),
		query.append("?  "); //14 - IN _company_id BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			for(final CashingCloseUserWeekMovementCompanyDTO movement : movementCompanys) {
				statement = this.connection.prepareCall(query.toString());
				statement.setInt(1, 2); //01 - IN _operation INT,
				statement.setLong(2, movement.getCashierClosingId());     //02 - IN _cashier_closing_id BIGINT,
				statement.setInt(3, movement.getWeekYear());     //03 - IN _week_year INT, 
				statement.setDouble(4, movement.getMovementValue()); //04 - IN _movement_value DECIMAL(19,2),  
				statement.setDouble(5, movement.getPendingMovementValue()); //05 - IN _pending_movement_value DECIMAL(19,2),  
				statement.setDouble(6, movement.getTotalCompany()); //06 - IN _total_company DECIMAL(19,2), 	
				statement.setDouble(7, movement.getDiscountTotal()); //07 - IN _discount_total DECIMAL(19,2),			
				statement.setDouble(8, movement.getPaymentTotal()); //08 - IN _payment_total DECIMAL(19,2),
				statement.setDouble(9, movement.getPendingMovementAfterValue()); //09 - IN _pending_movement_after_value DECIMAL(19,2),
				statement.setInt(10, movement.getStatus()); //09 - IN _status INT,
				statement.setLong(11, movement.getChangeData().getUserChange());    //10 - IN _user_change BIGINT,
				statement.setString(12, movement.getNote()); //11 - IN _note_cash VARCHAR(2000),
				statement.setString(13, ConstantDataManager.BLANK); //12 - IN _note_company VARCHAR(2000)
				statement.setLong(14, movement.getCompanyId()); // 13 - IN _company_id BIGINT
				
		        statement.executeUpdate();  				
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
	}

	@Override
	public void saveCashierClosingLaunch(final List<CashingCloseUserWeekMovementCompanyLaunchDTO> cashingClosingLaunchList) throws Exception {
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_cashier_closing_save_launch");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _id BIGINT,
		query.append("?, "); //03 - IN _cashier_closing_id BIGINT,
		query.append("?, "); //04 - IN _user_operator BIGINT,
		query.append("?, "); //05 - IN _company_id BIGINT, 
		query.append("?, "); //06 - IN _bank_account_id INT, 
		query.append("?, "); //07 - IN _provider_id BIGINT,		
		query.append("?, "); //08 - IN _financial_group_id VARCHAR(7),
		query.append("?, "); //09 - IN _financial_sub_group_id VARCHAR(7),
		query.append("?, "); //10 - IN _credit BIT,
		query.append("?, "); //11 - IN _expense BIT,
		query.append("?, "); //12 - IN _payment_data DATETIME,
		query.append("?, "); //13 - IN _document_status TINYINT,
		query.append("?, "); //14 - IN _document_value DECIMAL(19,2),
		query.append("?, "); //15 - IN _document_note VARCHAR(1000),
		query.append("?, "); //16 - IN _user_change BIGINT,
		query.append("?  "); //17 - IN _inactive BIT
		query.append(")");
		query.append("}");
		
		try
		{
			for(final CashingCloseUserWeekMovementCompanyLaunchDTO movement : cashingClosingLaunchList) {
				statement = this.connection.prepareCall(query.toString());
				statement.setInt(1, 1); //01 - IN _operation INT
				statement.setLong(2, 0); //02 - IN _id BIGINT				
				statement.setLong(3, movement.getCashierClosingId()); //03 - IN _cashier_closing_id BIGINT
				statement.setLong(4, movement.getUserOperator()); // 04 - IN _user_operator BIGINT
				statement.setLong(5, movement.getCompanyId()); // 05 - IN _company_id BIGINT
				statement.setInt(6, movement.getBankAccountExpense()); //06 - IN _bank_account_id INT 
				statement.setLong(7, movement.getProviderExpense()); // 07 - IN _provider_id BIGINT,				
				statement.setString(8, movement.getFinancialGroupExpense()); //08 - IN _financial_group_id VARCHAR(7)
				statement.setString(9, movement.getFinancialSubGroupExpense()); //09 - IN _financial_sub_group_id VARCHAR(7)
				statement.setBoolean(10, movement.isCredit()); //10 - IN _credit BIT
				statement.setBoolean(11, movement.getType() == 1); //11 - IN _expense BIT
				statement.setString(12, movement.convertPaymentExpiryDateExpense()); //12 - IN _payment_data DATETIME				
				statement.setInt(13, movement.getDocumentStatusExpense()); //13 - IN _document_status TINYINT, 					
				statement.setDouble(14, movement.getDocumentValueExpense()); //14 - IN _document_value DECIMAL(19,2)				
				statement.setString(15, movement.getDocumentNoteExpense()); //15 - IN _document_note VARCHAR(1000)
				statement.setLong(16, movement.getUserChange()); //16 - IN _user_change BIGINT
				statement.setBoolean(17, movement.isInactive()); //17 - IN _inactive BIT
		        statement.executeUpdate();  				
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
	}
	
	@Override
	public void processCashierClosingLaunch(final CashierClosingPreviewFinishRequest request) throws Exception {
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_cashier_closing_process_movement");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _user_operator BIGINT,
		query.append("?, "); //03 - IN _companys_id VARCHAR(5000), 
		query.append("?, "); //04 - IN _cashier_closing_id BIGINT, 
		query.append("?, "); //05 - IN _payment_date DATETIME,
		query.append("?  "); //06 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 1); //01 - IN _operation INT
			statement.setLong(2, request.getUserOperator()); //02 - IN _user_operator BIGINT,		
			statement.setString(3, request.getCompanysIds()); //03 - IN _companys_id VARCHAR(5000), 
			statement.setLong(4, request.getCashierClosingId()); // 04 - IN _cashier_closing_id BIGINT, 
			statement.setString (5, request.convertPaymentDate()); // 05 - IN _payment_date DATETIME,
			statement.setLong(6, request.getUserChange()); //06 - IN _user_change BIGINT
	        statement.executeUpdate(); 
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
	}
	
	@Override
	public List<CashingCloseUserWeekMovementCompanyLaunchDTO> getCashierClosingLaunchs(final long cashierClosingId, final long userOperator) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_cashier_closing_get_launch");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _cashier_closing_id BIGINT,
		query.append("?  "); //03 - IN _user_operator BIGINT
		query.append(")");
		query.append("}");
		
		final List<CashingCloseUserWeekMovementCompanyLaunchDTO> items = new ArrayList<>();
		
		try
		{
			
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 1); // 01 - IN _operation INT
			statement.setLong(2, cashierClosingId); //02 - IN _cashier_closing_id BIGINT,
			statement.setLong(3, userOperator); //03 - IN _user_operator BIGINT
			
			resultSet = statement.executeQuery();
			
			int count = 1;
        	while (resultSet.next()) 
	        {
        		items.add(CashingCloseUserWeekMovementCompanyLaunchDTO.builder()
        		.id(resultSet.getLong(DatabaseConstants.COLUMN_ID))
        		.count(count)
        		.cashierClosingId(resultSet.getLong(DatabaseConstants.COLUMN_CASHIER_CLOSING_ID))
        		.cashingClosingStatus(resultSet.getString(DatabaseConstants.COLUMN_CASHIER_CLOSING_STATUS))
        		.userOperator(resultSet.getLong(DatabaseConstants.COLUMN_USER_OPERATOR))
        		.companyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID))
        		.bankAccountExpense(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID))
        		.bankAccountDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESCRIPTION))        		
        		.providerExpense(resultSet.getLong(DatabaseConstants.COLUMN_PROVIDER_ID))
        		.providerDescription(resultSet.getString(DatabaseConstants.COLUMN_PROVIDER_DESCRIPTION))
        		.financialGroupExpense(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_ID))
        		.financialGroupDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_DESCRIPTION))
        		.financialSubGroupExpense(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_ID))
        		.financialSubGroupDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_DESCRIPTION))
        		.credit(resultSet.getInt(DatabaseConstants.COLUMN_CREDIT) == 1)        		
        		.type(resultSet.getInt(DatabaseConstants.COLUMN_EXPENSE))
        		.documentStatusExpense(resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_STATUS))
        		.paymentExpiryDateExpense(resultSet.getString(DatabaseConstants.COLUMN_PAYMENT_DATA))
           		.documentValueExpense(resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE))     		
        		.documentNoteExpense(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE))        		
        		.userChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE))
        		.inactive(resultSet.getInt(DatabaseConstants.COLUMN_INACTIVE) == 1)
        		.build());
        		
        		count++;
	        }        	        
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		
		return items;		
	}
	
	@Override
	public void deleteCashierClosingLaunchs(final long cashierClosingId, final long userOperator) throws Exception {
		CallableStatement statement = null;
		
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_cashier_closing_delete_launch");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _cashier_closing_id BIGINT,
		query.append("?  "); //03 - IN _user_operator BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 1); // 01 - IN _operation INT
			statement.setLong(2, cashierClosingId); //02 - IN _cashier_closing_id BIGINT,
			statement.setLong(3, userOperator); //03 - IN _user_operator BIGINT
			
			statement.executeUpdate(); 	    
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}		
	}
}