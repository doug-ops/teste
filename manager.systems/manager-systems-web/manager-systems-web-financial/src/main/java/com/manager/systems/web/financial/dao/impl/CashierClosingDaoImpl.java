/*
 * Crete Date 08/10/2022
 */
package com.manager.systems.web.financial.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;

import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.movement.company.PreviewMovementCompanyDTO;
import com.manager.systems.common.dto.movement.company.PreviewMovementCompanyItemDTO;
import com.manager.systems.common.vo.OperationType;
import com.manager.systems.web.financial.dao.CashierClosingDao;
import com.manager.systems.web.financial.dto.CashierClosingDTO;
import com.manager.systems.web.financial.dto.CashierClosingItemDTO;
import com.manager.systems.web.financial.dto.FinancialDocumentDTO;

public class CashierClosingDaoImpl implements CashierClosingDao {
	private Connection connection;

	public CashierClosingDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public void getAllCashierClosing(final CashierClosingDTO cashierClosing) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;

		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_cashier_closing");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT,
		query.append("?, "); // 02 - IN _document_type VARCHAR(50),
		query.append("?, "); // 03 - IN _credit BIT,
		query.append("?, "); // 04 - IN _company_id BIGINT,
		query.append("?, "); // 05 - IN _bank_account_id INT,
		query.append("?, "); // 06 - IN _document_number VARCHAR(20),
		query.append("?, "); // 07 - IN _document_value DECIMAL(19,4),
		query.append("?, "); // 08 - IN _document_note VARCHAR(1000),
		query.append("?, "); // 09 - IN _document_status INT,
		query.append("?, "); // 10 - IN _payment_value DECIMAL(19,4),
		query.append("?, "); // 11 - IN _payment_data DATETIME,
		query.append("?, "); // 12 - IN _payment_expiry_data DATETIME,
		query.append("?, "); // 13 - IN _payment_user BIGINT,
		query.append("?, "); // 14 - IN _payment_status INT,
		query.append("?, "); // 15 - IN _financial_group_id VARCHAR,
		query.append("?, "); // 16 - IN _financial_sub_group_id VARCHAR,
		query.append("?, "); // 17 - IN _inactive BIT,
		query.append("?, "); // 18 - IN _user_change BIGINT,
		query.append("?, "); // 19 - IN _document_parent_id BIGINT,
		query.append("?, "); // 20 - IN _document_transfer_id BIGINT,
		query.append("?, "); // 21 - IN _bank_account_origin_id INT
		query.append("?, "); // 22 - IN _provider_id BIGINT,
		query.append("?, "); // 23 - IN _document_type VARCHAR(50),
		query.append("?, "); // 24 - IN _moviment_type_credit BIT,
		query.append("?, "); // 25 - IN _moviment_type_debit BIT,
		query.append("?, "); // 26 - IN _moviment_type_financial BIT,
		query.append("?, "); // 27 - IN _moviment_type_open BIT,
		query.append("?, "); // 28 - IN _moviment_type_close BIT,
		query.append("?, "); // 29 - IN _moviment_type_removed BIT,
		query.append("?, "); // 30 - IN _moviment_transf BIT,
		query.append("?, "); // 31 - IN _moviment_transf_hab BIT,
		query.append("?, "); // 32 - IN _date_from DATETIME,
		query.append("?, "); // 33 - IN _date_to DATETIME,
		query.append("?, "); // 34 - IN _filter_data_type INT
		query.append("?, "); // 35 - IN _user_operation BIGINT,
		query.append("?, "); // 36 IN _user_childrens_parent VARCHAR(5000),
		query.append("?, "); // 37 - IN _moviment_type_closing BIT,
		query.append("?, "); // 38 - IN _document_id BIGINT,
		query.append("?, "); // 39 - IN _residue INT,
		query.append("?  "); // 40 - IN _companysId VARCHAR(5000)
		query.append(")");
		query.append("}");

		try {
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); // 01 - IN _operation INT,
			statement.setString(2, cashierClosing.getDocumentType()); // 02 - IN _document_type VARCHAR(50),
			statement.setNull(3, Types.BIT); // 03 - IN _credit BIT,
			statement.setLong(4, cashierClosing.getCompanyId()); // 04 - IN _company_id BIGINT,
			statement.setInt(5, cashierClosing.getBankAccountId()); // 05 - IN _bank_account_id INT,
			statement.setString(6, cashierClosing.getDocumentNumber()); // 06 - IN _document_number VARCHAR(20),
			statement.setNull(7, Types.DOUBLE); // 07 - IN _document_value DECIMAL(19,4),
			statement.setNull(8, Types.VARCHAR); // 08 - IN _document_note VARCHAR(1000),
			statement.setNull(9, Types.INTEGER); // 09 - IN _document_status INT,
			statement.setNull(10, Types.DOUBLE); // 10 - IN _payment_value DECIMAL(19,4),
			statement.setNull(11, Types.TIMESTAMP); // 11 - IN _payment_data DATETIME,
			statement.setNull(12, Types.TIMESTAMP); // 12 - IN _payment_expiry_data DATETIME,
			statement.setNull(13, Types.BIGINT); // 13 - IN _payment_user BIGINT,
			statement.setNull(14, Types.INTEGER); // 14 - IN _payment_status INT,
			statement.setNull(15, Types.VARCHAR); // 15 - IN _financial_group_id VARCHAR(10),
			statement.setNull(16, Types.VARCHAR); // 16 - IN _financial_sub_group_id VARCHAR(10),
			statement.setNull(17, Types.BIT); // 17 - IN _inactive BIT,
			statement.setNull(18, Types.BIGINT); // 18 - IN _user_change BIGINT,
			statement.setNull(19, Types.BIGINT); // 19 - IN _user_change BIGINT,
			statement.setNull(20, Types.BIGINT); // 20 - IN _document_transfer_id BIGINT,
			statement.setNull(21, Types.INTEGER); // 21 - IN _bank_account_origin_id INT,
			statement.setNull(22, Types.BIGINT); // 22 - IN _provider_id BIGINT,
			statement.setBoolean(23, cashierClosing.isCredit()); // 23 - IN _moviment_type_credit BIT,
			statement.setBoolean(24, cashierClosing.isDebit()); // 24 - IN _moviment_type_debit BIT,
			statement.setBoolean(25, cashierClosing.isFinancial()); // 25 - IN _moviment_type_financial BIT,
			statement.setBoolean(26, cashierClosing.isOpen()); // 26 - IN _moviment_type_open BIT,
			statement.setBoolean(27, cashierClosing.isClose()); // 27 - IN _moviment_type_close BIT,
			statement.setBoolean(28, cashierClosing.isRemoved()); // 28 - IN _moviment_type_removed BIT,
			statement.setBoolean(29, cashierClosing.isTransfer()); // 29 - IN _moviment_transf BIT,
			statement.setBoolean(30, cashierClosing.isTransferHab()); // 30 - IN _moviment_transf_hab BIT,
			statement.setTimestamp(31, Timestamp.valueOf(cashierClosing.getDateFrom())); // 31 - IN _date_from DATETIME,
			statement.setTimestamp(32, Timestamp.valueOf(cashierClosing.getDateTo())); // 32 - IN _date_to DATETIME,
			statement.setInt(33, cashierClosing.getFilterBy()); // 33 - IN _filter_data_type INT,
			statement.setLong(34, cashierClosing.getUserOperation()); // 34 - _user_operation BIGINT,
			statement.setString(35, cashierClosing.getUserChildrensParent()); // 35 - IN _user_childrens_parent
																				// VARCHAR(200),
			statement.setBoolean(36, cashierClosing.isClosing()); // 36 - IN _moviment_type_closing BIT,
			statement.setNull(37, Types.INTEGER); // 37 - IN _financial_cost_center_id INT,
			statement.setNull(38, Types.BIGINT); // 38 - IN _document_id BIGINT,
			statement.setNull(39, Types.BIT); // 39 - IN _residue INT,
			statement.setString(40, cashierClosing.getCompanysId()); // 40 - IN _companysId VARCHAR(5000)
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				final CashierClosingItemDTO item = new CashierClosingItemDTO();
				item.setId(resultSet.getLong(DatabaseConstants.COLUMN_ID));
				item.setDocumentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_ID));
				item.setDocumentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID));
				item.setDocumentTypeId(resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_TYPE));
				item.setCredit(resultSet.getBoolean(DatabaseConstants.COLUMN_CREDIT));
				item.setCompanyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID));
				item.setProviderId(resultSet.getLong(DatabaseConstants.COLUMN_PROVIDER_ID));
				item.setBankAccountId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID));
				item.setFinancialGroupId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_ID));
				item.setFinancialSubGroupId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_ID));
				item.setDocumentNumber(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NUMBER));
				item.setDocumentValue(resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
				item.setDocumentNote(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE));
				item.setDocumentStatusId(resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_STATUS));
				item.setPaymentValue(resultSet.getDouble(DatabaseConstants.COLUMN_PAYMENT_VALUE));
				item.setDocumentDiscount(resultSet.getDouble(DatabaseConstants.COLUMN_PAYMENT_DISCOUNT));
				item.setDocumentExtra(resultSet.getDouble(DatabaseConstants.COLUMN_PAYMENT_EXTRA));
				item.setPaymentResidue(resultSet.getBoolean(DatabaseConstants.COLUMN_PAYMENT_RESIDUE));
				item.setPaymentData(resultSet.getString(DatabaseConstants.COLUMN_PAYMENT_DATA));
				item.setPaymentExpiryData(resultSet.getString(DatabaseConstants.COLUMN_PAYMENT_EXPIRY_DATA));
				item.setPaymentUserId(resultSet.getLong(DatabaseConstants.COLUMN_PAYMENT_USER));
				item.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
				item.setCreateUser(resultSet.getLong(DatabaseConstants.COLUMN_USER_CREATION));
				item.setChangeUser(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
				item.setCreateDate(resultSet.getString(DatabaseConstants.COLUMN_CREATION_DATE));
				item.setChangeDate(resultSet.getString(DatabaseConstants.COLUMN_CHANGE_DATE));
				item.setBankAccount(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESCRIPTION));
				item.setCompany(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION));
				item.setProvider(resultSet.getString(DatabaseConstants.COLUMN_PROVIDER_DESCRIPTION));
				item.setFinancialGroup(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_DESCRIPTION));
				item.setFinancialSubGroup(
						resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_DESCRIPTION));
				item.setProductId(resultSet.getLong(DatabaseConstants.COLUMN_PRODUCT_ID));
				item.setProductDescription(resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_DESCRIPTION));
				item.setProductMovement(
						resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_IS_PRODUCT_MOVEMENT));
				if (item.getProductId() > 0) {
					item.setKey(DatabaseConstants.PARAMETER_MOVEMENT_PRODUCT + item.getProductId());
				} else {
					item.setKey(DatabaseConstants.PARAMETER_MOVEMENT_TRANSFER + item.getDocumentId());
				}
				item.setDocumentTransferId(resultSet.getLong(DatabaseConstants.PARAMETER_DOCUMENT_TRANSFER_ID));

				item.populateDataDocumentNote();
				cashierClosing.addItem(item);
			}
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

	@Override
	public void getCashierClosingItem(final CashierClosingItemDTO item) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;

		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_cashier_closing");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT,
		query.append("?, "); // 02 - IN _document_type VARCHAR(50),
		query.append("?, "); // 03 - IN _credit BIT,
		query.append("?, "); // 04 - IN _company_id BIGINT,
		query.append("?, "); // 05 - IN _bank_account_id INT,
		query.append("?, "); // 06 - IN _document_number VARCHAR(20),
		query.append("?, "); // 07 - IN _document_value DECIMAL(19,4),
		query.append("?, "); // 08 - IN _document_note VARCHAR(1000),
		query.append("?, "); // 09 - IN _document_status INT,
		query.append("?, "); // 10 - IN _payment_value DECIMAL(19,4),
		query.append("?, "); // 11 - IN _payment_data DATETIME,
		query.append("?, "); // 12 - IN _payment_expiry_data DATETIME,
		query.append("?, "); // 13 - IN _payment_user BIGINT,
		query.append("?, "); // 14 - IN _payment_status INT,
		query.append("?, "); // 15 - IN _financial_group_id VARCHAR,
		query.append("?, "); // 16 - IN _financial_sub_group_id VARCHAR,
		query.append("?, "); // 17 - IN _inactive BIT,
		query.append("?, "); // 18 - IN _user_change BIGINT,
		query.append("?, "); // 19 - IN _document_parent_id BIGINT,
		query.append("?, "); // 20 - IN _document_transfer_id BIGINT,
		query.append("?, "); // 21 - IN _bank_account_origin_id INT
		query.append("?, "); // 22 - IN _provider_id BIGINT,
		query.append("?, "); // 23 - IN _document_type VARCHAR(50),
		query.append("?, "); // 24 - IN _moviment_type_credit BIT,
		query.append("?, "); // 25 - IN _moviment_type_debit BIT,
		query.append("?, "); // 26 - IN _moviment_type_financial BIT,
		query.append("?, "); // 27 - IN _moviment_type_open BIT,
		query.append("?, "); // 28 - IN _moviment_type_close BIT,
		query.append("?, "); // 29 - IN _moviment_type_removed BIT,
		query.append("?, "); // 30 - IN _moviment_transf BIT,
		query.append("?, "); // 31 - IN _moviment_transf_hab BIT,
		query.append("?, "); // 32 - IN _date_from DATETIME,
		query.append("?, "); // 33 - IN _date_to DATETIME,
		query.append("?, "); // 34 - IN _filter_data_type INT
		query.append("?, "); // 35 - IN _user_operation BIGINT,
		query.append("?, "); // 36 IN _user_childrens_parent VARCHAR(5000),
		query.append("?, "); // 37 - IN _moviment_type_closing BIT,
		query.append("?, "); // 38 - IN _document_id BIGINT,
		query.append("?, "); // 39 - IN _residue INT,
		query.append("?  "); // 40 - IN _companysId VARCHAR(5000)
		query.append(")");
		query.append("}");

		try {
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); // 01 - IN _operation INT,
			statement.setInt(2, item.getDocumentTypeId()); // 02 - IN _document_type VARCHAR(50),
			statement.setNull(3, Types.BIT); // 03 - IN _credit BIT,
			statement.setNull(4, Types.BIGINT); // 04 - IN _company_id BIGINT,
			statement.setNull(5, Types.INTEGER); // 05 - IN _bank_account_id INT,
			statement.setNull(6, Types.VARCHAR); // 06 - IN _document_number VARCHAR(20),
			statement.setNull(7, Types.DOUBLE); // 07 - IN _document_value DECIMAL(19,4),
			statement.setNull(8, Types.VARCHAR); // 08 - IN _document_note VARCHAR(1000),
			statement.setNull(9, Types.INTEGER); // 09 - IN _document_status INT,
			statement.setNull(10, Types.DOUBLE); // 10 - IN _payment_value DECIMAL(19,4),
			statement.setNull(11, Types.TIMESTAMP); // 11 - IN _payment_data DATETIME,
			statement.setNull(12, Types.TIMESTAMP); // 12 - IN _payment_expiry_data DATETIME,
			statement.setNull(13, Types.BIGINT); // 13 - IN _payment_user BIGINT,
			statement.setNull(14, Types.INTEGER); // 14 - IN _payment_status INT,
			statement.setNull(15, Types.VARCHAR); // 15 - IN _financial_group_id VARCHAR(10),
			statement.setNull(16, Types.VARCHAR); // 16 - IN _financial_sub_group_id VARCHAR(10),
			statement.setNull(17, Types.BIT); // 17 - IN _inactive BIT,
			statement.setNull(18, Types.BIGINT); // 18 - IN _user_change BIGINT,
			statement.setNull(19, Types.BIGINT); // 19 - _document_parent_id BIGINT,
			statement.setNull(20, Types.BIGINT); // 20 - IN _document_transfer_id BIGINT,
			statement.setNull(21, Types.INTEGER); // 21 - IN _bank_account_origin_id INT,
			statement.setNull(22, Types.BIGINT); // 22 - IN _provider_id BIGINT,
			statement.setNull(23, Types.BIT); // 23 - IN _moviment_type_credit BIT,
			statement.setNull(24, Types.BIT); // 24 - IN _moviment_type_debit BIT,
			statement.setNull(25, Types.BIT); // 25 - IN _moviment_type_financial BIT,
			statement.setNull(26, Types.BIT); // 26 - IN _moviment_type_open BIT,
			statement.setNull(27, Types.BIT); // 27 - IN _moviment_type_close BIT,
			statement.setNull(28, Types.BIT); // 28 - IN _moviment_type_removed BIT,
			statement.setNull(29, Types.BIT); // 29 - IN _moviment_transf BIT,
			statement.setNull(30, Types.BIT); // 30 - IN _moviment_transf_hab BIT,
			statement.setNull(31, Types.TIMESTAMP); // 31 - IN _date_from DATETIME,
			statement.setNull(32, Types.TIMESTAMP); // 32 - IN _date_to DATETIME,
			statement.setNull(33, Types.INTEGER); // 33 - IN _filter_data_type INT,
			statement.setNull(34, Types.BIGINT); // 34 - _user_operation BIGINT,
			statement.setNull(35, Types.VARCHAR); // 35 - IN _user_childrens_parent VARCHAR(200),
			statement.setNull(36, Types.BIT); // 36 - IN _moviment_type_closing BIT,
			statement.setNull(37, Types.INTEGER); // 37 - IN _financial_cost_center_id INT
			statement.setLong(38, item.getDocumentId()); // 38 - IN _document_id BIGINT,
			statement.setNull(39, Types.BIT); // 39 - IN _residue INT,
			statement.setNull(40, Types.VARCHAR); // 40 - IN _companysId VARCHAR(5000)
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				item.setId(resultSet.getLong(DatabaseConstants.COLUMN_ID));
				item.setDocumentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_ID));
				item.setDocumentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID));
				item.setDocumentTypeId(resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_TYPE));
				item.setCredit(resultSet.getBoolean(DatabaseConstants.COLUMN_CREDIT));
				item.setCompanyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID));
				item.setProviderId(resultSet.getLong(DatabaseConstants.COLUMN_PROVIDER_ID));
				item.setBankAccountId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID));
				item.setFinancialGroupId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_ID));
				item.setFinancialSubGroupId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_ID));
				item.setDocumentNumber(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NUMBER));
				item.setDocumentValue(resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
				item.setDocumentNote(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE));
				item.setDocumentStatusId(resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_STATUS));
				item.setPaymentValue(resultSet.getDouble(DatabaseConstants.COLUMN_PAYMENT_VALUE));
				item.setDocumentDiscount(resultSet.getDouble(DatabaseConstants.COLUMN_PAYMENT_DISCOUNT));
				item.setDocumentExtra(resultSet.getDouble(DatabaseConstants.COLUMN_PAYMENT_EXTRA));
				item.setPaymentResidue(resultSet.getBoolean(DatabaseConstants.COLUMN_PAYMENT_RESIDUE));
				item.setPaymentData(resultSet.getString(DatabaseConstants.COLUMN_PAYMENT_DATA));
				item.setPaymentExpiryData(resultSet.getString(DatabaseConstants.COLUMN_PAYMENT_EXPIRY_DATA));
				item.setPaymentUserId(resultSet.getLong(DatabaseConstants.COLUMN_PAYMENT_USER));
				item.setPaymentStatus(resultSet.getInt(DatabaseConstants.COLUMN_PAYMENT_STATUS));
				item.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
				item.setCreateUser(resultSet.getLong(DatabaseConstants.COLUMN_USER_CREATION));
				item.setChangeUser(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
				item.setCreateDate(resultSet.getString(DatabaseConstants.COLUMN_CREATION_DATE));
				item.setChangeDate(resultSet.getString(DatabaseConstants.COLUMN_CHANGE_DATE));
				item.setBankAccount(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESCRIPTION));
				item.setCompany(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION));
				item.setProvider(resultSet.getString(DatabaseConstants.COLUMN_PROVIDER_DESCRIPTION));
			}
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

	@Override
	public void getCashierClosing(final PreviewMovementCompanyDTO previewMovementCompany) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;

		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_cashier_closing");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT,
		query.append("?, "); // 02 - IN _document_type VARCHAR(50),
		query.append("?, "); // 03 - IN _credit BIT,
		query.append("?, "); // 04 - IN _company_id BIGINT,
		query.append("?, "); // 05 - IN _bank_account_id INT,
		query.append("?, "); // 06 - IN _document_number VARCHAR(20),
		query.append("?, "); // 07 - IN _document_value DECIMAL(19,4),
		query.append("?, "); // 08 - IN _document_note VARCHAR(1000),
		query.append("?, "); // 09 - IN _document_status INT,
		query.append("?, "); // 10 - IN _payment_value DECIMAL(19,4),
		query.append("?, "); // 11 - IN _payment_data DATETIME,
		query.append("?, "); // 12 - IN _payment_expiry_data DATETIME,
		query.append("?, "); // 13 - IN _payment_user BIGINT,
		query.append("?, "); // 14 - IN _payment_status INT,
		query.append("?, "); // 15 - IN _financial_group_id VARCHAR,
		query.append("?, "); // 16 - IN _financial_sub_group_id VARCHAR,
		query.append("?, "); // 17 - IN _inactive BIT,
		query.append("?, "); // 18 - IN _user_change BIGINT,
		query.append("?, "); // 19 - IN _document_parent_id BIGINT,
		query.append("?, "); // 20 - IN _document_transfer_id BIGINT,
		query.append("?, "); // 21 - IN _bank_account_origin_id INT
		query.append("?, "); // 22 - IN _provider_id BIGINT,
		query.append("?, "); // 23 - IN _document_type VARCHAR(50),
		query.append("?, "); // 24 - IN _moviment_type_credit BIT,
		query.append("?, "); // 25 - IN _moviment_type_debit BIT,
		query.append("?, "); // 26 - IN _moviment_type_financial BIT,
		query.append("?, "); // 27 - IN _moviment_type_open BIT,
		query.append("?, "); // 28 - IN _moviment_type_close BIT,
		query.append("?, "); // 29 - IN _moviment_type_removed BIT,
		query.append("?, "); // 30 - IN _moviment_transf BIT,
		query.append("?, "); // 31 - IN _moviment_transf_hab BIT,
		query.append("?, "); // 32 - IN _date_from DATETIME,
		query.append("?, "); // 33 - IN _date_to DATETIME,
		query.append("?, "); // 34 - IN _filter_data_type INT
		query.append("?, "); // 35 - IN _user_operation BIGINT,
		query.append("?, "); // 36 IN _user_childrens_parent VARCHAR(5000),
		query.append("?, "); // 37 - IN _moviment_type_closing BIT,
		query.append("?, "); // 38 - IN _document_id BIGINT,
		query.append("?, "); // 39 - IN _residue INT,
		query.append("?  "); // 40 - IN _companysId VARCHAR(5000)
		query.append(")");
		query.append("}");

		try {
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); // 01 - IN _operation INT,
			statement.setNull(2, Types.VARCHAR); // 02 - IN _document_type VARCHAR(50),
			statement.setNull(3, Types.BIT); // 03 - IN _credit BIT,
			statement.setLong(4, previewMovementCompany.getCompnayId()); // 04 - IN _company_id BIGINT,
			statement.setNull(5, Types.INTEGER); // 05 - IN _bank_account_id INT,
			statement.setNull(6, Types.VARCHAR); // 06 - IN _document_number VARCHAR(20),
			statement.setNull(7, Types.DOUBLE); // 07 - IN _document_value DECIMAL(19,4),
			statement.setNull(8, Types.VARCHAR); // 08 - IN _document_note VARCHAR(1000),
			statement.setNull(9, Types.INTEGER); // 09 - IN _document_status INT,
			statement.setNull(10, Types.DOUBLE); // 10 - IN _payment_value DECIMAL(19,4),
			statement.setNull(11, Types.TIMESTAMP); // 11 - IN _payment_data DATETIME,
			statement.setNull(12, Types.TIMESTAMP); // 12 - IN _payment_expiry_data DATETIME,
			statement.setNull(13, Types.BIGINT); // 13 - IN _payment_user BIGINT,
			statement.setNull(14, Types.INTEGER); // 14 - IN _payment_status INT,
			statement.setNull(15, Types.VARCHAR); // 15 - IN _financial_group_id VARCHAR(10),
			statement.setNull(16, Types.VARCHAR); // 16 - IN _financial_sub_group_id VARCHAR(10),
			statement.setNull(17, Types.BIT); // 17 - IN _inactive BIT,
			statement.setNull(18, Types.BIGINT); // 18 - IN _user_change BIGINT,
			statement.setLong(41, previewMovementCompany.getDocumentParentId()); // 19 - IN _document_parent_id BIGINT,
			statement.setNull(20, Types.BIGINT); // 20 - IN _document_transfer_id BIGINT,
			statement.setNull(21, Types.INTEGER); // 21 - IN _bank_account_origin_id INT,
			statement.setNull(22, Types.BIGINT); // 22 - IN _provider_id BIGINT,
			statement.setNull(23, Types.BIT); // 23 - IN _moviment_type_credit BIT,
			statement.setNull(24, Types.BIT); // 24 - IN _moviment_type_debit BIT,
			statement.setNull(25, Types.BIT); // 25 - IN _moviment_type_financial BIT,
			statement.setNull(26, Types.BIT); // 26 - IN _moviment_type_open BIT,
			statement.setNull(27, Types.BIT); // 27 - IN _moviment_type_close BIT,
			statement.setNull(28, Types.BIT); // 28 - IN _moviment_type_removed BIT,
			statement.setNull(29, Types.BIT); // 29 - IN _moviment_transf BIT,
			statement.setNull(30, Types.BIT); // 30 - IN _moviment_transf_hab BIT,
			statement.setNull(31, Types.TIMESTAMP); // 31 - IN _date_from DATETIME,
			statement.setNull(32, Types.TIMESTAMP); // 32 - IN _date_to DATETIME,
			statement.setNull(33, Types.INTEGER); // 33 - IN _filter_data_type INT,
			statement.setLong(34, previewMovementCompany.getUserChange()); // 34 - _user_operation BIGINT,
			statement.setNull(35, Types.VARCHAR); // 35 - IN _user_childrens_parent VARCHAR(200),
			statement.setNull(36, Types.BIT); // 36 - IN _moviment_type_closing BIT,
			statement.setNull(37, Types.INTEGER); // 37 - IN _financial_cost_center_id INT
			statement.setNull(38, Types.BIGINT); // 38 - IN _document_id BIGINT,
			statement.setNull(39, Types.BIT); // 39 - IN _residue INT,
			statement.setNull(40, Types.VARCHAR);// 40 - IN _companysId VARCHAR(5000)
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				final String keyMovement = (resultSet.getString("initials"));
				final PreviewMovementCompanyItemDTO item = new PreviewMovementCompanyItemDTO();
				item.setPaymentDate(resultSet.getString(DatabaseConstants.COLUMN_PAYMENT_DATA));
				item.setDocumentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID));
				item.setProviderId(resultSet.getLong(DatabaseConstants.COLUMN_PROVIDER_ID));
				item.setProviderDescription(resultSet.getString(DatabaseConstants.COLUMN_PROVIDER_DESCRIPTION));
				item.setDocumentNote(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE));
				item.setDocumentValue(resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
				item.setCredit(resultSet.getBoolean(DatabaseConstants.COLUMN_CREDIT));
				item.setProductId(resultSet.getLong(DatabaseConstants.COLUMN_PRODUCT_ID));
				item.setProductDescription(resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_DESCRIPTION));
				item.setInitialDate(resultSet.getString(DatabaseConstants.COLUMN_INITIAL_DATE));
				item.setReadingDate(resultSet.getString(DatabaseConstants.COLUMN_READING_DATE));
				item.setInitialEntry(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_IN_INITIAL));
				item.setFinalEntry(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_IN_FINAL));
				item.setInitialOutput(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_OUT_INITIAL));
				item.setFinalOutput(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_OUT_FINAL));
				item.setProductMovement(true);
				previewMovementCompany
						.setCompanyDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION));
				previewMovementCompany.addItemReport(keyMovement, item);
			}
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

	@Override
	public Long creditTrasnferValuesOriginToDestinty(final FinancialDocumentDTO document) throws Exception {
		long documentNumberResult = 0;
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		ResultSet resultSet = null;

		query.append("{");
		query.append("call pr_cashier_closing");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT,
		query.append("?, "); // 02 - IN _document_type VARCHAR(50),
		query.append("?, "); // 03 - IN _credit BIT,
		query.append("?, "); // 04 - IN _company_id BIGINT,
		query.append("?, "); // 05 - IN _bank_account_id INT,
		query.append("?, "); // 06 - IN _document_number VARCHAR(20),
		query.append("?, "); // 07 - IN _document_value DECIMAL(19,4),
		query.append("?, "); // 08 - IN _document_note VARCHAR(1000),
		query.append("?, "); // 09 - IN _document_status INT,
		query.append("?, "); // 10 - IN _payment_value DECIMAL(19,4),
		query.append("?, "); // 11 - IN _payment_data DATETIME,
		query.append("?, "); // 12 - IN _payment_expiry_data DATETIME,
		query.append("?, "); // 13 - IN _payment_user BIGINT,
		query.append("?, "); // 14 - IN _payment_status INT,
		query.append("?, "); // 15 - IN _financial_group_id VARCHAR,
		query.append("?, "); // 16 - IN _financial_sub_group_id VARCHAR,
		query.append("?, "); // 17 - IN _inactive BIT,
		query.append("?, "); // 18 - IN _user_change BIGINT,
		query.append("?, "); // 19 - IN _document_parent_id BIGINT,
		query.append("?, "); // 20 - IN _document_transfer_id BIGINT,
		query.append("?, "); // 21 - IN _bank_account_origin_id INT
		query.append("?, "); // 22 - IN _provider_id BIGINT,
		query.append("?, "); // 23 - IN _document_type VARCHAR(50),
		query.append("?, "); // 24 - IN _moviment_type_credit BIT,
		query.append("?, "); // 25 - IN _moviment_type_debit BIT,
		query.append("?, "); // 26 - IN _moviment_type_financial BIT,
		query.append("?, "); // 27 - IN _moviment_type_open BIT,
		query.append("?, "); // 28 - IN _moviment_type_close BIT,
		query.append("?, "); // 29 - IN _moviment_type_removed BIT,
		query.append("?, "); // 30 - IN _moviment_transf BIT,
		query.append("?, "); // 31 - IN _moviment_transf_hab BIT,
		query.append("?, "); // 32 - IN _date_from DATETIME,
		query.append("?, "); // 33 - IN _date_to DATETIME,
		query.append("?, "); // 34 - IN _filter_data_type INT
		query.append("?, "); // 35 - IN _user_operation BIGINT,
		query.append("?, "); // 36 IN _user_childrens_parent VARCHAR(5000),
		query.append("?, "); // 37 - IN _moviment_type_closing BIT,
		query.append("?, "); // 38 - IN _document_id BIGINT,
		query.append("?, "); // 39 - IN _residue INT,
		query.append("?  "); // 40 - IN _companysId VARCHAR(5000)
		query.append(")");
		query.append("}");

		try {
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); // 01 - IN _operation INT,
			statement.setInt(2, document.getDocumentType()); // 02 - IN _document_type VARCHAR(50),
			statement.setBoolean(3, document.isCredit()); // 03 - IN _credit BIT,
			statement.setLong(4, document.getCompanyId()); // 04 - IN _company_id BIGINT,
			statement.setInt(5, document.getBankAccountId()); // 05 - IN _bank_account_id INT,
			statement.setString(6, document.getDocumentNumber()); // 06 - IN _document_number VARCHAR(20),
			statement.setDouble(7, document.getDocumentValue()); // 07 - IN _document_value DECIMAL(19,4),
			statement.setString(8, document.getDocumentNote()); // 08 - IN _document_note VARCHAR(1000),
			statement.setInt(9, document.getDocumentStatus()); // 09 - IN _document_status INT,
			statement.setDouble(10, document.getPaymentValue()); // 10 - IN _payment_value DECIMAL(19,4),
			statement.setTimestamp(11, Timestamp.valueOf(document.getPaymentDate())); // 11 - IN _payment_data DATETIME,
			statement.setTimestamp(12, Timestamp.valueOf(document.getPaymentExpiryDate())); // 12 - IN
																							// _payment_expiry_data
																							// DATETIME,
			statement.setLong(13, document.getUserChange()); // 13 - IN _payment_user BIGINT,
			statement.setInt(14, document.getPaymentStatus()); // 14 - IN _payment_status INT,
			statement.setString(15, document.getFinancialGroupId()); // 15 - IN _financial_group_id VARCHAR,
			statement.setString(16, document.getFinancialSubGroupId()); // 16 - IN _financial_sub_group_id VARCHAR,
			statement.setBoolean(17, document.isInactive()); // 17 - IN _inactive BIT,
			statement.setLong(18, document.getUserChange()); // 18 - IN _user_change BIGINT,
			statement.setLong(19, document.getDocumentParentId()); // 19 - IN _document_parent_id BIGINT,
			statement.setLong(20, document.getDocumentTransferId()); // 20 - IN _document_transfer_id BIGINT,
			statement.setLong(21, document.getBankAccountOriginId()); // 21 - IN _bank_account_origin_id INT,
			statement.setNull(22, Types.BIGINT); // 22 - IN _provider_id BIGINT,
			statement.setNull(23, Types.BIT); // 23 - IN _moviment_type_credit BIT,
			statement.setNull(24, Types.BIT); // 24 - IN _moviment_type_debit BIT,
			statement.setNull(25, Types.BIT); // 25 - IN _moviment_type_financial BIT,
			statement.setNull(26, Types.BIT); // 26 - IN _moviment_type_open BIT,
			statement.setNull(27, Types.BIT); // 27 - IN _moviment_type_close BIT,
			statement.setNull(28, Types.BIT); // 28 - IN _moviment_type_removed BIT,
			statement.setNull(29, Types.BIT); // 29 - IN _moviment_transf BIT,
			statement.setNull(30, Types.BIT); // 30 - IN _moviment_transf_hab BIT,
			statement.setNull(31, Types.TIMESTAMP); // 31 - IN _date_from DATETIME,
			statement.setNull(32, Types.TIMESTAMP); // 32 - IN _date_to DATETIME,
			statement.setNull(33, Types.INTEGER); // 33 - IN _filter_data_type INT,
			statement.setLong(34, document.getUserChange()); // 34 - _user_operation BIGINT,
			statement.setNull(35, Types.VARCHAR); // 35 - IN _user_childrens_parent VARCHAR(200),
			statement.setNull(36, Types.BIT); // 36 - IN _moviment_type_closing BIT,
			statement.setNull(37, Types.INTEGER); // 37 - IN _financial_cost_center_id INT
			statement.setNull(38, Types.BIGINT); // 38 - IN _document_id BIGINT,
			statement.setNull(39, Types.BIT); // 39 - IN _residue INT,
			statement.setNull(40, Types.VARCHAR);// 40 - IN _companysId VARCHAR(5000)
			final boolean isProcessed = statement.execute();

			if (isProcessed) {
				resultSet = statement.getResultSet();
				while (resultSet.next()) {
					documentNumberResult = resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID);
				}
			}
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return documentNumberResult;
	}

	@Override
	public boolean debitTrasnferValuesOriginResidue(final FinancialDocumentDTO document) throws Exception {
		boolean result = false;

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();

		query.append("{");
		query.append("call pr_cashier_closing");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT,
		query.append("?, "); // 02 - IN _document_type VARCHAR(50),
		query.append("?, "); // 03 - IN _credit BIT,
		query.append("?, "); // 04 - IN _company_id BIGINT,
		query.append("?, "); // 05 - IN _bank_account_id INT,
		query.append("?, "); // 06 - IN _document_number VARCHAR(20),
		query.append("?, "); // 07 - IN _document_value DECIMAL(19,4),
		query.append("?, "); // 08 - IN _document_note VARCHAR(1000),
		query.append("?, "); // 09 - IN _document_status INT,
		query.append("?, "); // 10 - IN _payment_value DECIMAL(19,4),
		query.append("?, "); // 11 - IN _payment_data DATETIME,
		query.append("?, "); // 12 - IN _payment_expiry_data DATETIME,
		query.append("?, "); // 13 - IN _payment_user BIGINT,
		query.append("?, "); // 14 - IN _payment_status INT,
		query.append("?, "); // 15 - IN _financial_group_id VARCHAR,
		query.append("?, "); // 16 - IN _financial_sub_group_id VARCHAR,
		query.append("?, "); // 17 - IN _inactive BIT,
		query.append("?, "); // 18 - IN _user_change BIGINT,
		query.append("?, "); // 19 - IN _document_parent_id BIGINT,
		query.append("?, "); // 20 - IN _document_transfer_id BIGINT,
		query.append("?, "); // 21 - IN _bank_account_origin_id INT.
		query.append("?, "); // 22 - IN _provider_id BIGINT,
		query.append("?, "); // 23 - IN _document_type VARCHAR(50),
		query.append("?, "); // 24 - IN _moviment_type_credit BIT,
		query.append("?, "); // 25 - IN _moviment_type_debit BIT,
		query.append("?, "); // 26 - IN _moviment_type_financial BIT,
		query.append("?, "); // 27 - IN _moviment_type_open BIT,
		query.append("?, "); // 28 - IN _moviment_type_close BIT,
		query.append("?, "); // 29 - IN _moviment_type_removed BIT,
		query.append("?, "); // 30 - IN _moviment_transf BIT,
		query.append("?, "); // 31 - IN _moviment_transf_hab BIT,
		query.append("?, "); // 32 - IN _date_from DATETIME,
		query.append("?, "); // 33 - IN _date_to DATETIME,
		query.append("?, "); // 34 - IN _filter_data_type INT
		query.append("?, "); // 35 - IN _user_operation BIGINT,
		query.append("?, "); // 36 IN _user_childrens_parent VARCHAR(5000),
		query.append("?, "); // 37 - IN _moviment_type_closing BIT,
		query.append("?, "); // 38 - IN _document_id BIGINT,
		query.append("?, "); // 39 - IN _residue INT,
		query.append("?  "); // 40 - IN _companysId VARCHAR(5000)
		query.append(")");
		query.append("}");

		try {
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE_TRANSFER_VALUE_RESIDUE.getType()); // 01 - IN _operation INT,
			statement.setInt(2, document.getDocumentType()); // 02 - IN _document_type VARCHAR(50),
			statement.setBoolean(3, document.isCredit()); // 03 - IN _credit BIT,
			statement.setLong(4, document.getCompanyId()); // 04 - IN _company_id BIGINT,
			statement.setInt(5, document.getBankAccountId()); // 05 - IN _bank_account_id INT,
			statement.setString(6, document.getDocumentNumber()); // 06 - IN _document_number VARCHAR(20),
			statement.setDouble(7, document.getDocumentValue()); // 07 - IN _document_value DECIMAL(19,4),
			statement.setString(8, document.getDocumentNote()); // 08 - IN _document_note VARCHAR(1000),
			statement.setInt(9, document.getDocumentStatus()); // 09 - IN _document_status INT,
			statement.setDouble(10, document.getPaymentValue()); // 10 - IN _payment_value DECIMAL(19,4),
			statement.setTimestamp(11, Timestamp.valueOf(document.getPaymentDate())); // 11 - IN _payment_data DATETIME,
			statement.setTimestamp(12, Timestamp.valueOf(document.getPaymentExpiryDate())); // 12 - IN
																							// _payment_expiry_data
																							// DATETIME,
			statement.setLong(13, document.getUserChange()); // 13 - IN _payment_user BIGINT,
			statement.setInt(14, document.getPaymentStatus()); // 14 - IN _payment_status INT,
			statement.setString(15, document.getFinancialGroupId()); // 15 - IN _financial_group_id VARCHAR,
			statement.setString(16, document.getFinancialSubGroupId()); // 16 - IN _financial_sub_group_id VARCHAR,
			statement.setBoolean(17, document.isInactive()); // 17 - IN _inactive BIT,
			statement.setLong(18, document.getUserChange()); // 18 - IN _user_change BIGINT,
			statement.setLong(19, document.getDocumentParentId()); // 19 - IN _document_parent_id BIGINT,
			if (document.getDocumentTransferId() != 0) {
				statement.setLong(20, document.getDocumentTransferId()); // 20 - IN _document_transfer_id BIGINT
			} else {
				statement.setNull(20, Types.BIGINT); // 20 - IN _document_transfer_id BIGINT
			}
			statement.setInt(21, document.getBankAccountOriginId()); // 21 - IN _bank_account_origin_id INT,
			statement.setNull(22, Types.BIGINT); // 22 - IN _provider_id BIGINT,
			statement.setNull(23, Types.BIT); // 23 - IN _moviment_type_credit BIT,
			statement.setNull(24, Types.BIT); // 24 - IN _moviment_type_debit BIT,
			statement.setNull(25, Types.BIT); // 25 - IN _moviment_type_financial BIT,
			statement.setNull(26, Types.BIT); // 26 - IN _moviment_type_open BIT,
			statement.setNull(27, Types.BIT); // 27 - IN _moviment_type_close BIT,
			statement.setNull(28, Types.BIT); // 28 - IN _moviment_type_removed BIT,
			statement.setNull(29, Types.BIT); // 29 - IN _moviment_transf BIT,
			statement.setNull(30, Types.BIT); // 30 - IN _moviment_transf_hab BIT,
			statement.setNull(31, Types.TIMESTAMP); // 31 - IN _date_from DATETIME,
			statement.setNull(32, Types.TIMESTAMP); // 32 - IN _date_to DATETIME,
			statement.setNull(33, Types.INTEGER); // 33 - IN _filter_data_type INT,
			statement.setLong(34, document.getUserChange()); // 34 - _user_operation BIGINT,
			statement.setNull(35, Types.VARCHAR); // 35 - IN _user_childrens_parent VARCHAR(200),
			statement.setNull(36, Types.BIT); // 36 - IN _moviment_type_closing BIT,
			statement.setNull(37, Types.INTEGER); // 37 - IN _financial_cost_center_id INT
			statement.setNull(38, Types.BIGINT); // 38 - IN _document_id BIGINT,
			statement.setNull(39, Types.BIT); // 39 - IN _residue INT,
			statement.setNull(40, Types.VARCHAR);// 40 - IN _companysId VARCHAR(5000)
			statement.execute();
			result = true;
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return result;
	}

	@Override
	public boolean saveDocumentExpense(final FinancialDocumentDTO document) throws Exception {
		boolean result = false;

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();

		query.append("{");
		query.append("call pr_cashier_closing");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT,
		query.append("?, "); // 02 - IN _document_type VARCHAR(50),
		query.append("?, "); // 03 - IN _credit BIT,
		query.append("?, "); // 04 - IN _company_id BIGINT,
		query.append("?, "); // 05 - IN _bank_account_id INT,
		query.append("?, "); // 06 - IN _document_number VARCHAR(20),
		query.append("?, "); // 07 - IN _document_value DECIMAL(19,4),
		query.append("?, "); // 08 - IN _document_note VARCHAR(1000),
		query.append("?, "); // 09 - IN _document_status INT,
		query.append("?, "); // 10 - IN _payment_value DECIMAL(19,4),
		query.append("?, "); // 11 - IN _payment_data DATETIME,
		query.append("?, "); // 12 - IN _payment_expiry_data DATETIME,
		query.append("?, "); // 13 - IN _payment_user BIGINT,
		query.append("?, "); // 14 - IN _payment_status INT,
		query.append("?, "); // 15 - IN _financial_group_id VARCHAR,
		query.append("?, "); // 16 - IN _financial_sub_group_id VARCHAR,
		query.append("?, "); // 17 - IN _inactive BIT,
		query.append("?, "); // 18 - IN _user_change BIGINT,
		query.append("?, "); // 19 - IN _document_parent_id BIGINT,
		query.append("?, "); // 20 - IN _document_transfer_id BIGINT,
		query.append("?, "); // 21 - IN _bank_account_origin_id INT.
		query.append("?, "); // 22 - IN _provider_id BIGINT,
		query.append("?, "); // 23 - IN _document_type VARCHAR(50),
		query.append("?, "); // 24 - IN _moviment_type_credit BIT,
		query.append("?, "); // 25 - IN _moviment_type_debit BIT,
		query.append("?, "); // 26 - IN _moviment_type_financial BIT,
		query.append("?, "); // 27 - IN _moviment_type_open BIT,
		query.append("?, "); // 28 - IN _moviment_type_close BIT,
		query.append("?, "); // 29 - IN _moviment_type_removed BIT,
		query.append("?, "); // 30 - IN _moviment_transf BIT,
		query.append("?, "); // 31 - IN _moviment_transf_hab BIT,
		query.append("?, "); // 32 - IN _date_from DATETIME,
		query.append("?, "); // 33 - IN _date_to DATETIME,
		query.append("?, "); // 34 - IN _filter_data_type INT
		query.append("?, "); // 35 - IN _user_operation BIGINT,
		query.append("?, "); // 36 IN _user_childrens_parent VARCHAR(5000),
		query.append("?, "); // 37 - IN _moviment_type_closing BIT,
		query.append("?, "); // 38 - IN _document_id BIGINT,
		query.append("?, "); // 39 - IN _residue INT,
		query.append("?  "); // 40 - IN _companysId VARCHAR(5000)
		query.append(")");
		query.append("}");

		try {
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE_NEW_DOCUMENT_EXPENSE.getType()); // 01 - IN _operation INT,
			statement.setInt(2, document.getDocumentType()); // 02 - IN _document_type VARCHAR(50),
			statement.setBoolean(3, document.isCredit()); // 03 - IN _credit BIT,
			statement.setLong(4, document.getCompanyId()); // 04 - IN _company_id BIGINT,
			statement.setInt(5, document.getBankAccountId()); // 05 - IN _bank_account_id INT,
			statement.setString(6, document.getDocumentNumber()); // 06 - IN _document_number VARCHAR(20),
			statement.setDouble(7, document.getDocumentValue()); // 07 - IN _document_value DECIMAL(19,4),
			statement.setString(8, document.getDocumentNote()); // 08 - IN _document_note VARCHAR(1000),
			statement.setInt(9, document.getDocumentStatus()); // 09 - IN _document_status INT,
			statement.setDouble(10, document.getPaymentValue()); // 10 - IN _payment_value DECIMAL(19,4),
			statement.setTimestamp(11, Timestamp.valueOf(document.getPaymentDate())); // 11 - IN _payment_data DATETIME,
			statement.setTimestamp(12, Timestamp.valueOf(document.getPaymentExpiryDate())); // 12 - IN
																							// _payment_expiry_data
																							// DATETIME,
			statement.setLong(13, document.getUserChange()); // 13 - IN _payment_user BIGINT,
			statement.setInt(14, document.getPaymentStatus()); // 14 - IN _payment_status INT,
			statement.setString(15, document.getFinancialGroupId()); // 15 - IN _financial_group_id VARCHAR,
			statement.setString(16, document.getFinancialSubGroupId()); // 16 - IN _financial_sub_group_id VARCHAR,
			statement.setBoolean(17, document.isInactive()); // 17 - IN _inactive BIT,
			statement.setLong(18, document.getUserChange()); // 18 - IN _user_change BIGINT,
			statement.setLong(19, document.getDocumentParentId()); // 19 - IN _document_parent_id BIGINT,
			if (document.getDocumentTransferId() != 0) {
				statement.setLong(20, document.getDocumentTransferId()); // 20 - IN _document_transfer_id BIGINT
			} else {
				statement.setNull(20, Types.BIGINT); // 20 - IN _document_transfer_id BIGINT
			}
			statement.setInt(21, document.getBankAccountOriginId()); // 21 - IN _bank_account_origin_id INT,
			statement.setLong(22, document.getProviderId()); // 22 - IN _provider_id BIGINT,
			statement.setNull(23, Types.BIT); // 23 - IN _moviment_type_credit BIT,
			statement.setNull(24, Types.BIT); // 24 - IN _moviment_type_debit BIT,
			statement.setNull(25, Types.BIT); // 25 - IN _moviment_type_financial BIT,
			statement.setNull(26, Types.BIT); // 26 - IN _moviment_type_open BIT,
			statement.setNull(27, Types.BIT); // 27 - IN _moviment_type_close BIT,
			statement.setNull(28, Types.BIT); // 28 - IN _moviment_type_removed BIT,
			statement.setNull(29, Types.BIT); // 29 - IN _moviment_transf BIT,
			statement.setNull(30, Types.BIT); // 30 - IN _moviment_transf_hab BIT,
			statement.setNull(31, Types.TIMESTAMP); // 31 - IN _date_from DATETIME,
			statement.setNull(32, Types.TIMESTAMP); // 32 - IN _date_to DATETIME,
			statement.setNull(33, Types.INTEGER); // 33 - IN _filter_data_type INT,
			statement.setLong(34, document.getUserChange()); // 34 - _user_operation BIGINT,
			statement.setNull(35, Types.VARCHAR); // 35 - IN _user_childrens_parent VARCHAR(200),
			statement.setNull(36, Types.BIT); // 36 - IN _moviment_type_closing BIT,
			statement.setNull(37, Types.INTEGER); // 37 - IN _financial_cost_center_id INT
			statement.setLong(38, document.getDocumentId()); // 38 - IN _document_id BIGINT,
			statement.setNull(39, Types.BIT); // 39 - IN _residue INT,
			statement.setNull(40, Types.VARCHAR);// 40 - IN _companysId VARCHAR(5000)
			result = (statement.executeUpdate() > 0);
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return result;
	}
	
	@Override
	public boolean checkCashingCloseSuccess(final FinancialDocumentDTO document) throws Exception {
		boolean result = false;

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();

		query.append("{");
		query.append("call pr_cashier_closing");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT,
		query.append("?, "); // 02 - IN _document_type VARCHAR(50),
		query.append("?, "); // 03 - IN _credit BIT,
		query.append("?, "); // 04 - IN _company_id BIGINT,
		query.append("?, "); // 05 - IN _bank_account_id INT,
		query.append("?, "); // 06 - IN _document_number VARCHAR(20),
		query.append("?, "); // 07 - IN _document_value DECIMAL(19,4),
		query.append("?, "); // 08 - IN _document_note VARCHAR(1000),
		query.append("?, "); // 09 - IN _document_status INT,
		query.append("?, "); // 10 - IN _payment_value DECIMAL(19,4),
		query.append("?, "); // 11 - IN _payment_data DATETIME,
		query.append("?, "); // 12 - IN _payment_expiry_data DATETIME,
		query.append("?, "); // 13 - IN _payment_user BIGINT,
		query.append("?, "); // 14 - IN _payment_status INT,
		query.append("?, "); // 15 - IN _financial_group_id VARCHAR,
		query.append("?, "); // 16 - IN _financial_sub_group_id VARCHAR,
		query.append("?, "); // 17 - IN _inactive BIT,
		query.append("?, "); // 18 - IN _user_change BIGINT,
		query.append("?, "); // 19 - IN _document_parent_id BIGINT,
		query.append("?, "); // 20 - IN _document_transfer_id BIGINT,
		query.append("?, "); // 21 - IN _bank_account_origin_id INT.
		query.append("?, "); // 22 - IN _provider_id BIGINT,
		query.append("?, "); // 23 - IN _document_type VARCHAR(50),
		query.append("?, "); // 24 - IN _moviment_type_credit BIT,
		query.append("?, "); // 25 - IN _moviment_type_debit BIT,
		query.append("?, "); // 26 - IN _moviment_type_financial BIT,
		query.append("?, "); // 27 - IN _moviment_type_open BIT,
		query.append("?, "); // 28 - IN _moviment_type_close BIT,
		query.append("?, "); // 29 - IN _moviment_type_removed BIT,
		query.append("?, "); // 30 - IN _moviment_transf BIT,
		query.append("?, "); // 31 - IN _moviment_transf_hab BIT,
		query.append("?, "); // 32 - IN _date_from DATETIME,
		query.append("?, "); // 33 - IN _date_to DATETIME,
		query.append("?, "); // 34 - IN _filter_data_type INT
		query.append("?, "); // 35 - IN _user_operation BIGINT,
		query.append("?, "); // 36 IN _user_childrens_parent VARCHAR(5000),
		query.append("?, "); // 37 - IN _moviment_type_closing BIT,
		query.append("?, "); // 38 - IN _document_id BIGINT,
		query.append("?, "); // 39 - IN _residue INT,
		query.append("?  "); // 40 - IN _companysId VARCHAR(5000)
		query.append(")");
		query.append("}");

		try {
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, 30); // 01 - IN _operation INT,
			statement.setInt(2, document.getDocumentType()); // 02 - IN _document_type VARCHAR(50),
			statement.setBoolean(3, document.isCredit()); // 03 - IN _credit BIT,
			statement.setLong(4, document.getCompanyId()); // 04 - IN _company_id BIGINT,
			statement.setInt(5, document.getBankAccountId()); // 05 - IN _bank_account_id INT,
			statement.setString(6, document.getDocumentNumber()); // 06 - IN _document_number VARCHAR(20),
			statement.setDouble(7, document.getDocumentValue()); // 07 - IN _document_value DECIMAL(19,4),
			statement.setString(8, document.getDocumentNote()); // 08 - IN _document_note VARCHAR(1000),
			statement.setInt(9, document.getDocumentStatus()); // 09 - IN _document_status INT,
			statement.setDouble(10, document.getPaymentValue()); // 10 - IN _payment_value DECIMAL(19,4),
			statement.setNull(11, Types.TIMESTAMP); // 11 - IN _payment_data DATETIME,
			statement.setNull(12, Types.TIMESTAMP); // 12 - IN _payment_expiry_data DATETIME,
			statement.setLong(13, document.getUserChange()); // 13 - IN _payment_user BIGINT,
			statement.setInt(14, document.getPaymentStatus()); // 14 - IN _payment_status INT,
			statement.setString(15, document.getFinancialGroupId()); // 15 - IN _financial_group_id VARCHAR,
			statement.setString(16, document.getFinancialSubGroupId()); // 16 - IN _financial_sub_group_id VARCHAR,
			statement.setBoolean(17, document.isInactive()); // 17 - IN _inactive BIT,
			statement.setLong(18, document.getUserChange()); // 18 - IN _user_change BIGINT,
			statement.setLong(19, document.getDocumentParentId()); // 19 - IN _document_parent_id BIGINT,
			statement.setLong(20, document.getDocumentTransferId()); // 20 - IN _document_transfer_id BIGINT
			statement.setInt(21, document.getBankAccountOriginId()); // 21 - IN _bank_account_origin_id INT,
			statement.setLong(22, document.getProviderId()); // 22 - IN _provider_id BIGINT,
			statement.setNull(23, Types.BIT); // 23 - IN _moviment_type_credit BIT,
			statement.setNull(24, Types.BIT); // 24 - IN _moviment_type_debit BIT,
			statement.setNull(25, Types.BIT); // 25 - IN _moviment_type_financial BIT,
			statement.setNull(26, Types.BIT); // 26 - IN _moviment_type_open BIT,
			statement.setNull(27, Types.BIT); // 27 - IN _moviment_type_close BIT,
			statement.setNull(28, Types.BIT); // 28 - IN _moviment_type_removed BIT,
			statement.setNull(29, Types.BIT); // 29 - IN _moviment_transf BIT,
			statement.setNull(30, Types.BIT); // 30 - IN _moviment_transf_hab BIT,
			statement.setNull(31, Types.TIMESTAMP); // 31 - IN _date_from DATETIME,
			statement.setNull(32, Types.TIMESTAMP); // 32 - IN _date_to DATETIME,
			statement.setNull(33, Types.INTEGER); // 33 - IN _filter_data_type INT,
			statement.setLong(34, document.getUserChange()); // 34 - _user_operation BIGINT,
			statement.setNull(35, Types.VARCHAR); // 35 - IN _user_childrens_parent VARCHAR(200),
			statement.setNull(36, Types.BIT); // 36 - IN _moviment_type_closing BIT,
			statement.setNull(37, Types.INTEGER); // 37 - IN _financial_cost_center_id INT
			statement.setLong(38, document.getDocumentId()); // 38 - IN _document_id BIGINT,
			statement.setNull(39, Types.BIT); // 39 - IN _residue INT,
			statement.setNull(40, Types.VARCHAR);// 40 - IN _companysId VARCHAR(5000)
			result = (statement.executeUpdate() > 0);
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return result;
	}
}