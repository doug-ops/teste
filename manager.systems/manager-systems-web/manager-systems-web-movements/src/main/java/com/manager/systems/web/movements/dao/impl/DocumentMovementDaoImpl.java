package com.manager.systems.web.movements.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;

import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.movement.company.PreviewMovementCompanyDTO;
import com.manager.systems.common.dto.movement.company.PreviewMovementCompanyItemDTO;
import com.manager.systems.common.vo.OperationType;
import com.manager.systems.web.movements.dao.DocumentMovementDao;
import com.manager.systems.web.movements.dto.DocumentMovementDTO;
import com.manager.systems.web.movements.dto.DocumentMovementGroup;
import com.manager.systems.web.movements.dto.DocumentMovementGroupFilter;
import com.manager.systems.web.movements.dto.DocumentMovementGroupItem;
import com.manager.systems.web.movements.dto.DocumentMovementItemDTO;
import com.manager.systems.web.movements.utils.ConstantDataManager;

public class DocumentMovementDaoImpl implements DocumentMovementDao {
	private Connection connection;

	public DocumentMovementDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public DocumentMovementGroup getDocumentMovementGroup(final DocumentMovementGroupFilter filter) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;

		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_document_movement_report");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT,
		query.append("?, "); // 02 - IN _company_id BIGINT,
		query.append("?, "); // 03 - IN _provider_id BIGINT,
		query.append("?, "); // 04 - IN _financial_group_id INT,
		query.append("?, "); // 05 - IN _financial_sub_group_id INT,
		query.append("?, "); // 06 - IN _bank_account_id INT,
		query.append("?, "); // 07 - IN _document_type VARCHAR(50),
		query.append("?, "); // 08 - IN _document_number VARCHAR(20),
		query.append("?, "); // 09 - IN _moviment_type_credit BIT,
		query.append("?, "); // 10 - IN _moviment_type_debit BIT,
		query.append("?, "); // 11 - IN _moviment_type_financial BIT,
		query.append("?, "); // 12 - IN _moviment_type_open BIT,
		query.append("?, "); // 13 - IN _moviment_type_close BIT,
		query.append("?, "); // 14 - IN _moviment_type_removed BIT,
		query.append("?, "); // 15 - IN _moviment_transf BIT,
		query.append("?, "); // 16 - IN _moviment_transf_hab BIT,
		query.append("?, "); // 17 - IN _date_from DATETIME,
		query.append("?, "); // 18 - IN _date_to DATETIME,
		query.append("?, "); // 19 - IN _filter_data_type INT
		query.append("?, "); // 20 - IN _id BIGINT,
		query.append("?, "); // 21 - IN _document_id BIGINT,
		query.append("?, "); // 22 - IN _document_parent_id BIGINT,
		query.append("?, "); // 23 - IN _user_operation BIGINT,
		query.append("?, "); // 24 - IN _product_group_id VARCHAR(10),
		query.append("?, "); // 25 - IN _product_sub_group_id INT,
		query.append("?, "); // 26 - IN _user_childrens_parent VARCHAR(200),
		query.append("?, "); // 27 - IN _moviment_type_closing BIT,
		query.append("?, "); // 28 - IN _financial_cost_center_id		
		query.append("?, "); // 29 - IN _moviment_type_launch BIT,
		query.append("?, "); // 30 - IN _moviment_type_input_movement BIT,
		query.append("?, "); // 31 - IN _moviment_type_output_movement BIT,
		query.append("?  "); // 32 - IN _moviment_type_duplicate_movement BIT,
		query.append(")");
		query.append("}");
		
		final DocumentMovementGroup movementGroup = new DocumentMovementGroup();

		try {
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL_GROUNPING.getType()); // 01 - IN _operation INT,
			statement.setLong(2, filter.getCompanyId()); // 02 - IN _company_id BIGINT,
			statement.setLong(3, filter.getProviderId()); // 03 - IN _provider_id BIGINT,
			statement.setString(4, filter.getFinancialGroupId()); // 04 - IN _financial_group_id VARCHAR(10),
			statement.setString(5, filter.getFinancialSubGroupId()); // 05 - IN _financial_sub_group_id VARCHAR(10),
			statement.setInt(6, filter.getBankAccountId()); // 06 - IN _bank_account_id INT,
			statement.setString(7, filter.getDocumentType()); // 07 - IN _document_type VARCHAR,
			statement.setString(8, filter.getDocumentNumber()); // 08 - IN _document_number VARCHAR(20),
			statement.setBoolean(9, filter.isCredit()); // 09 - IN _moviment_type_credit BIT,
			statement.setBoolean(10, filter.isDebit()); // 10 - IN _moviment_type_debit BIT,
			statement.setBoolean(11, filter.isFinancial()); // 11 - IN _moviment_type_financial BIT,
			statement.setBoolean(12, filter.isOpen()); // 12 - IN _moviment_type_open BIT,
			statement.setBoolean(13, filter.isClose()); // 13 - IN _moviment_type_close BIT,
			statement.setBoolean(14, filter.isRemoved()); // 14 - IN _moviment_type_removed BIT,
			statement.setBoolean(15, filter.isTransfer()); // 15 - IN _moviment_transf BIT,
			statement.setBoolean(16, filter.isTransferHab()); // 15 - IN _moviment_transf_hab BIT,
			statement.setTimestamp(17, Timestamp.valueOf(filter.getDateFrom())); // 15 - IN _date_from DATETIME,
			statement.setTimestamp(18, Timestamp.valueOf(filter.getDateTo())); // 16 - IN _date_to DATETIME,
			statement.setInt(19, filter.getFilterBy()); // 17 - IN _filter_data_type INT,
			statement.setNull(20, Types.BIGINT); // 18 - IN _id BIGINT,
			statement.setNull(21, Types.BIGINT); // 19 - IN _document_id BIGINT,
			statement.setNull(22, Types.BIGINT); // 20 - IN _document_parent_id INT,
			statement.setLong(23, filter.getUserOperation()); // 23 - _user_operation BIGINT,
			statement.setInt(24, filter.getProductGroupId()); // 24 - IN _product_group_id INT,
			statement.setInt(25, filter.getProductSubGroupId()); // 25 - IN _product_sub_group_id INT,
			statement.setString(26, filter.getUserChildrensParent()); // 26 - IN _user_childrens_parent VARCHAR(200),
			statement.setNull(27, Types.BIT); // 27 - IN _moviment_type_closing BIT,
			if(filter.getFinancialCostCenterId() > 0) {
				statement.setInt(28, filter.getFinancialCostCenterId()); // 28 - IN _financial_cost_center_id INT
			}else {
				statement.setNull(28, Types.INTEGER); // 28 - IN _financial_cost_center_id INT
			}			
			statement.setBoolean(29, filter.isLaunch()); // 29 - IN _moviment_type_launch BIT,
			statement.setBoolean(30, filter.isInputMovement()); // 30 - IN _moviment_type_input_movement BIT,
			statement.setBoolean(31, filter.isOutputMovement()); // 31 - IN _moviment_type_output_movement BIT,
			statement.setBoolean(32, filter.isDuplicateMovement()); // 32 - IN _moviment_type_duplicate_movement BIT,
			
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				final DocumentMovementGroupItem item = new DocumentMovementGroupItem();
				item.setId(resultSet.getLong(DatabaseConstants.COLUMN_ID));
				item.setDocumentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID));
				item.setDocumentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_ID));
				item.setCompanyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID));
				item.setCompanyDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION));				
				item.setDocumentValue(resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
				item.setDocumentNote(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE));
				item.setInitialDate(resultSet.getString(DatabaseConstants.COLUMN_CREATION_DATE));
				item.setFinalDate(resultSet.getString(DatabaseConstants.COLUMN_PAYMENT_DATA));
				item.setInitialDateLong(resultSet.getLong(DatabaseConstants.COLUMN_INITIAL_DATE_LONG));
				item.setFinalDateLong(resultSet.getLong(DatabaseConstants.COLUMN_FINAL_DATE_LONG));

				final boolean isProductMovement = (resultSet.getInt(DatabaseConstants.COLUMN_IS_PRODUCT_MOVEMENT) == 1 ? true : false);
				final long productId = resultSet.getLong(DatabaseConstants.COLUMN_PRODUCT_ID);
				final boolean isCredit = resultSet.getBoolean(DatabaseConstants.COLUMN_CREDIT);
				final int documentType = resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_TYPE);
				
				movementGroup.addItem(item, isProductMovement, productId, isCredit, documentType);
			}
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		
		movementGroup.convertMapToList();
		return movementGroup;
	}

	@Override
	public void getDocumentMovementItem(final DocumentMovementItemDTO item) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;

		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_document_movement_report");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT,
		query.append("?, "); // 02 - IN _company_id BIGINT,
		query.append("?, "); // 03 - IN _provider_id BIGINT,
		query.append("?, "); // 04 - IN _financial_group_id VARCHAR(10),
		query.append("?, "); // 05 - IN _financial_sub_group_id VARCHAR(10),
		query.append("?, "); // 06 - IN _bank_account_id INT,
		query.append("?, "); // 07 - IN _document_type VARCHAR(50),
		query.append("?, "); // 08 - IN _document_number VARCHAR(20),
		query.append("?, "); // 09 - IN _moviment_type_credit BIT,
		query.append("?, "); // 10 - IN _moviment_type_debit BIT,
		query.append("?, "); // 11 - IN _moviment_type_financial BIT,
		query.append("?, "); // 12 - IN _moviment_type_open BIT,
		query.append("?, "); // 13 - IN _moviment_type_close BIT,
		query.append("?, "); // 14 - IN _moviment_type_removed BIT,
		query.append("?, "); // 15 - IN _moviment_transf BIT,
		query.append("?, "); // 16 - IN _moviment_transf_hab BIT,
		query.append("?, "); // 17 - IN _date_from DATETIME,
		query.append("?, "); // 18 - IN _date_to DATETIME,
		query.append("?, "); // 19 - IN _filter_data_type INT
		query.append("?, "); // 20 - IN _id BIGINT,
		query.append("?, "); // 21 - IN _document_id BIGINT,
		query.append("?, "); // 22 - IN _document_parent_id BIGINT,
		query.append("?, "); // 23 - IN _user_operation BIGINT,
		query.append("?, "); // 24 - IN _product_group_id INT,
		query.append("?, "); // 25 - IN _product_sub_group_id INT,
		query.append("?, "); // 26 - IN _user_childrens_parent VARCHAR(200),
		query.append("?, "); // 27 - IN _moviment_type_closing BIT,
		query.append("?, "); // 28 - IN _financial_cost_center_id		
		query.append("?, "); // 29 - IN _moviment_type_launch BIT,
		query.append("?, "); // 30 - IN _moviment_type_input_movement BIT,
		query.append("?, "); // 31 - IN _moviment_type_output_movement BIT,
		query.append("?  "); // 32 - IN _moviment_type_duplicate_movement BIT,
		query.append(")");
		query.append("}");

		try {
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); // 01 - IN _operation INT,
			statement.setNull(2, Types.BIGINT); // 02 - IN _company_id BIGINT,
			statement.setNull(3, Types.BIGINT); // 03 - IN _provider_id BIGINT,
			statement.setNull(4, Types.VARCHAR); // 04 - IN _financial_group_id VARCHAR(10),
			statement.setNull(5, Types.VARCHAR); // 05 - IN _financial_sub_group_id VARCHAR,
			statement.setNull(6, Types.VARCHAR); // 06 - IN _bank_account_id VARCHAR,
			statement.setInt(7, item.getDocumentTypeId()); // 07 - IN _document_type VARCHAR,
			statement.setNull(8, Types.VARCHAR); // 08 - IN _document_number VARCHAR(20),
			statement.setNull(9, Types.BIT); // 09 - IN _moviment_type_credit BIT,
			statement.setNull(10, Types.BIT); // 10 - IN _moviment_type_debit BIT,
			statement.setNull(11, Types.BIT); // 11 - IN _moviment_type_financial BIT,
			statement.setNull(12, Types.BIT); // 12 - IN _moviment_type_open BIT,
			statement.setNull(13, Types.BIT); // 13 - IN _moviment_type_close BIT,
			statement.setNull(14, Types.BIT); // 14 - IN _moviment_type_removed BIT,
			statement.setNull(15, Types.BIT); // 15 - IN _moviment_transf BIT,
			statement.setNull(16, Types.BIT); // 15 - IN _moviment_transf_hab BIT,
			statement.setNull(17, Types.TIMESTAMP); // 15 - IN _date_from DATETIME,
			statement.setNull(18, Types.TIMESTAMP); // 16 - IN _date_to DATETIME,
			statement.setNull(19, Types.INTEGER); // 17 - IN _filter_data_type INT,
			statement.setNull(20, Types.BIGINT); // 18 - IN _id BIGINT,
			statement.setLong(21, item.getDocumentId()); // 19 - IN _document_id BIGINT,
			statement.setLong(22, item.getDocumentParentId()); // 21 - IN _document_parent_id INT,
			statement.setNull(23, Types.BIGINT); // 18 - IN _user_operation BIGINT,
			statement.setNull(24, Types.INTEGER); // 24 - IN _product_group_id INT,
			statement.setNull(25, Types.INTEGER); // 25 - IN _product_sub_group_id INT,
			statement.setNull(26, Types.VARCHAR); // 26 - IN _user_childrens_parent VARCHAR(200),
			statement.setNull(27, Types.BIT); // 27 - IN _moviment_type_closing BIT,
			statement.setNull(28, Types.INTEGER); // 28 - IN _financial_cost_center_id INT
			statement.setNull(29, Types.BIT); // 29 - IN _moviment_type_launch BIT,
			statement.setNull(30, Types.BIT); // 30 - IN _moviment_type_input_movement BIT,
			statement.setNull(31, Types.BIT); // 31 - IN _moviment_type_output_movement BIT,
			statement.setNull(32, Types.BIT); // 32 - IN _moviment_type_duplicate_movement BIT,

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
				item.setDocumentResidue(resultSet.getBoolean(DatabaseConstants.COLUMN_PAYMENT_RESIDUE));
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
				//item.setFinancialGroup(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_DESCRIPTION));
				//item.setFinancialSubGroup(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_DESCRIPTION));
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
	public void getDocumentMovement(final PreviewMovementCompanyDTO previewMovementCompany) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;

		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_document_movement_report");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT,
		query.append("?, "); // 02 - IN _company_id BIGINT,
		query.append("?, "); // 03 - IN _provider_id BIGINT,
		query.append("?, "); // 04 - IN _financial_group_id VARCHAR(10),
		query.append("?, "); // 05 - IN _financial_sub_group_id VARCHAR(10),
		query.append("?, "); // 06 - IN _bank_account_id INT,
		query.append("?, "); // 07 - IN _document_type VARCHAR(50),
		query.append("?, "); // 08 - IN _document_number VARCHAR(20),
		query.append("?, "); // 09 - IN _moviment_type_credit BIT,
		query.append("?, "); // 10 - IN _moviment_type_debit BIT,
		query.append("?, "); // 11 - IN _moviment_type_financial BIT,
		query.append("?, "); // 12 - IN _moviment_type_open BIT,
		query.append("?, "); // 13 - IN _moviment_type_close BIT,
		query.append("?, "); // 14 - IN _moviment_type_removed BIT,
		query.append("?, "); // 15 - IN _moviment_transf BIT,
		query.append("?, "); // 16 - IN _moviment_transf_hab BIT,
		query.append("?, "); // 17 - IN _date_from DATETIME,
		query.append("?, "); // 18 - IN _date_to DATETIME,
		query.append("?, "); // 19 - IN _filter_data_type INT
		query.append("?, "); // 20 - IN _id BIGINT,
		query.append("?, "); // 21 - IN _document_id BIGINT,
		query.append("?, "); // 22 - IN _document_parent_id BIGINT,
		query.append("?, "); // 23 - IN _user_operation BIGINT,
		query.append("?, "); // 24 - IN _product_group_id INT,
		query.append("?, "); // 25 - IN _product_sub_group_id INT,
		query.append("?, "); // 26 - IN _user_childrens_parent VARCHAR(200),
		query.append("?, "); // 27 - IN _moviment_type_closing BIT,
		query.append("?, "); // 28 - IN _financial_cost_center_id		
		query.append("?, "); // 29 - IN _moviment_type_launch BIT,
		query.append("?, "); // 30 - IN _moviment_type_input_movement BIT,
		query.append("?, "); // 31 - IN _moviment_type_output_movement BIT,
		query.append("?  "); // 32 - IN _moviment_type_duplicate_movement BIT,
		query.append(")");
		query.append("}");

		try {
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); // 01 - IN _operation INT,
			statement.setLong(2, previewMovementCompany.getCompnayId()); // 02 - IN _company_id BIGINT,
			statement.setNull(3, Types.BIGINT); // 03 - IN _provider_id BIGINT,
			statement.setNull(4, Types.VARCHAR); // 04 - IN _financial_group_id VARCHAR(10),
			statement.setNull(5, Types.VARCHAR); // 05 - IN _financial_sub_group_id VARCHAR(10),
			statement.setNull(6, Types.INTEGER); // 06 - IN _bank_account_id INT,
			statement.setNull(7, Types.VARCHAR); // 07 - IN _document_type VARCHAR,
			statement.setNull(8, Types.VARCHAR); // 08 - IN _document_number VARCHAR(20),
			statement.setNull(9, Types.BIT); // 09 - IN _moviment_type_credit BIT,
			statement.setNull(10, Types.BIT); // 10 - IN _moviment_type_debit BIT,
			statement.setNull(11, Types.BIT); // 11 - IN _moviment_type_financial BIT,
			statement.setNull(12, Types.BIT); // 12 - IN _moviment_type_open BIT,
			statement.setNull(13, Types.BIT); // 13 - IN _moviment_type_close BIT,
			statement.setNull(14, Types.BIT); // 14 - IN _moviment_type_removed BIT,
			statement.setNull(15, Types.BIT); // 15 - IN _moviment_transf BIT,
			statement.setNull(16, Types.BIT); // 15 - IN _moviment_transf_hab BIT,
			statement.setNull(17, Types.DATE); // 15 - IN _date_from DATETIME,
			statement.setNull(18, Types.DATE); // 16 - IN _date_to DATETIME,
			statement.setNull(19, Types.INTEGER); // 17 - IN _filter_data_type INT,
			statement.setNull(20, Types.BIGINT); // 18 - IN _id BIGINT,
			statement.setNull(21, Types.BIGINT); // 19 - IN _document_id BIGINT,
			statement.setLong(22, previewMovementCompany.getDocumentParentId()); // 21 - IN _document_parent_id INT,
			statement.setNull(23, Types.BIGINT); // 18 - IN _user_operation BIGINT,
			statement.setNull(24, Types.INTEGER); // 24 - IN _product_group_id INT,
			statement.setNull(25, Types.INTEGER); // 25 - IN _product_sub_group_id INT,
			statement.setNull(26, Types.VARCHAR); // 26 - IN _user_childrens_parent VARCHAR(200),
			statement.setNull(27, Types.BIT); // 27 - IN _moviment_type_closing BIT,
			statement.setNull(28, Types.INTEGER); // 28 - IN _financial_cost_center_id INT
			statement.setNull(29, Types.BIT); // 29 - IN _moviment_type_launch BIT,
			statement.setNull(30, Types.BIT); // 30 - IN _moviment_type_input_movement BIT,
			statement.setNull(31, Types.BIT); // 31 - IN _moviment_type_output_movement BIT,
			statement.setNull(32, Types.BIT); // 32 - IN _moviment_type_duplicate_movement BIT,

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
	public void getAllDocumentMovementReport(final DocumentMovementDTO document) throws Exception {

		CallableStatement statement = null;
		ResultSet resultSet = null;

		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_document_movement_report_pdf");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT,
		query.append("?, "); // 02 - IN _date_from DATETIME,
		query.append("?, "); // 03 - IN _date_to DATETIME,
		query.append("?, "); // 04 - IN _companys_id VARCHAR(500),
		query.append("?  "); // 05 - IN _documents_parent_id VARCHAR(500),
		query.append(")");
		query.append("}");

		try {
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, 1); // 01 - IN _operation INT,
			statement.setTimestamp(2, Timestamp.valueOf(document.getDateFrom())); // 02 - IN _date_from DATE,
			statement.setTimestamp(3, Timestamp.valueOf(document.getDateTo())); // 03 - IN _date_to DATE
			statement.setString(4, document.getCompanysId()); // 04 - IN _companys_id VARCHAR(500),
			statement.setString(5, document.getDocumentsParentId()); // 05 - IN _documents_parent_id VARCHAR(500),

			resultSet = statement.executeQuery();
			while (resultSet.next()) {

				final DocumentMovementItemDTO item = new DocumentMovementItemDTO();

				item.setCompanyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID));
				item.setCompany(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION));
				item.setDocumentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID));
				item.setDocumentNumber(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_ID));
				item.setDocumentNote(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE));
				item.setPaymentData(resultSet.getString(DatabaseConstants.COLUMN_PAYMENT_DATA));
				item.setDocumentTypeId(resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_TYPE));
				item.setDocumentValue(resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
				item.setCredit(resultSet.getBoolean(DatabaseConstants.COLUMN_CREDIT));
				item.setDocumentStatusId(resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_STATUS));
				item.setCreateDate(resultSet.getString(DatabaseConstants.COLUMN_CREATION_DATE));
				document.addItem(item);
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
	public void getAllDocumentMovement(final DocumentMovementDTO document) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;

		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_document_movement_report");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT,
		query.append("?, "); // 02 - IN _company_id BIGINT,
		query.append("?, "); // 03 - IN _provider_id BIGINT,
		query.append("?, "); // 04 - IN _financial_group_id INT,
		query.append("?, "); // 05 - IN _financial_sub_group_id INT,
		query.append("?, "); // 06 - IN _bank_account_id INT,
		query.append("?, "); // 07 - IN _document_type VARCHAR(50),
		query.append("?, "); // 08 - IN _document_number VARCHAR(20),
		query.append("?, "); // 09 - IN _moviment_type_credit BIT,
		query.append("?, "); // 10 - IN _moviment_type_debit BIT,
		query.append("?, "); // 11 - IN _moviment_type_financial BIT,
		query.append("?, "); // 12 - IN _moviment_type_open BIT,
		query.append("?, "); // 13 - IN _moviment_type_close BIT,
		query.append("?, "); // 14 - IN _moviment_type_removed BIT,
		query.append("?, "); // 15 - IN _moviment_transf BIT,
		query.append("?, "); // 16 - IN _moviment_transf_hab BIT,
		query.append("?, "); // 17 - IN _date_from DATETIME,
		query.append("?, "); // 18 - IN _date_to DATETIME,
		query.append("?, "); // 19 - IN _filter_data_type INT
		query.append("?, "); // 20 - IN _id BIGINT,
		query.append("?, "); // 21 - IN _document_id BIGINT,
		query.append("?, "); // 22 - IN _document_parent_id BIGINT,
		query.append("?, "); // 23 - IN _user_operation BIGINT,
		query.append("?, "); // 24 - IN _product_group_id VARCHAR(10),
		query.append("?, "); // 25 - IN _product_sub_group_id INT,
		query.append("?, "); // 26 - IN _user_childrens_parent VARCHAR(200),
		query.append("?, "); // 27 - IN _moviment_type_closing BIT,
		query.append("?, "); // 28 - IN _financial_cost_center_id		
		query.append("?, "); // 29 - IN _moviment_type_launch BIT,
		query.append("?, "); // 30 - IN _moviment_type_input_movement BIT,
		query.append("?, "); // 31 - IN _moviment_type_output_movement BIT,
		query.append("?  "); // 32 - IN _moviment_type_duplicate_movement BIT,
		query.append(")");
		query.append("}");

		try {
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); // 01 - IN _operation INT,
			statement.setLong(2, document.getCompanyId()); // 02 - IN _company_id BIGINT,
			statement.setLong(3, document.getProviderId()); // 03 - IN _provider_id BIGINT,
			statement.setString(4, document.getFinancialGroupId()); // 04 - IN _financial_group_id VARCHAR(10),
			statement.setString(5, document.getFinancialSubGroupId()); // 05 - IN _financial_sub_group_id VARCHAR(10),
			statement.setInt(6, document.getBankAccountId()); // 06 - IN _bank_account_id INT,
			statement.setString(7, document.getDocumentType()); // 07 - IN _document_type VARCHAR,
			statement.setString(8, document.getDocumentNumber()); // 08 - IN _document_number VARCHAR(20),
			statement.setBoolean(9, document.isCredit()); // 09 - IN _moviment_type_credit BIT,
			statement.setBoolean(10, document.isDebit()); // 10 - IN _moviment_type_debit BIT,
			statement.setBoolean(11, document.isFinancial()); // 11 - IN _moviment_type_financial BIT,
			statement.setBoolean(12, document.isOpen()); // 12 - IN _moviment_type_open BIT,
			statement.setBoolean(13, document.isClose()); // 13 - IN _moviment_type_close BIT,
			statement.setBoolean(14, document.isRemoved()); // 14 - IN _moviment_type_removed BIT,
			statement.setBoolean(15, document.isTransfer()); // 15 - IN _moviment_transf BIT,
			statement.setBoolean(16, document.isTransferHab()); // 15 - IN _moviment_transf_hab BIT,
			statement.setTimestamp(17, Timestamp.valueOf(document.getDateFrom())); // 15 - IN _date_from DATETIME,
			statement.setTimestamp(18, Timestamp.valueOf(document.getDateTo())); // 16 - IN _date_to DATETIME,
			statement.setInt(19, document.getFilterBy()); // 17 - IN _filter_data_type INT,
			statement.setNull(20, Types.BIGINT); // 18 - IN _id BIGINT,
			statement.setNull(21, Types.BIGINT); // 19 - IN _document_id BIGINT,
			statement.setNull(22, Types.BIGINT); // 20 - IN _document_parent_id INT,
			statement.setLong(23, document.getUserOperation()); // 23 - _user_operation BIGINT,
			statement.setInt(24, document.getProductGroupId()); // 24 - IN _product_group_id INT,
			statement.setInt(25, document.getProductSubGroupId()); // 25 - IN _product_sub_group_id INT,
			statement.setString(26, document.getUserChildrensParent()); // 26 - IN _user_childrens_parent VARCHAR(200),
			statement.setNull(27, Types.BIT); // 27 - IN _moviment_type_closing BIT,
			if(document.getFinancialCostCenterId() > 0) {
				statement.setInt(28, document.getFinancialCostCenterId()); // 28 - IN _financial_cost_center_id INT
			}else {
				statement.setNull(28, Types.INTEGER); // 28 - IN _financial_cost_center_id INT
			}			
			statement.setBoolean(29, document.getLaunch()); // 29 - IN _moviment_type_launch BIT,
			statement.setBoolean(30, document.getInputMovement()); // 30 - IN _moviment_type_input_movement BIT,
			statement.setBoolean(31, document.getOutputMovement()); // 31 - IN _moviment_type_output_movement BIT,
			statement.setBoolean(32, document.getDuplicateMovement()); // 32 - IN _moviment_type_duplicate_movement BIT,
			
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				final DocumentMovementItemDTO item = new DocumentMovementItemDTO();
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
				item.setDocumentResidue(resultSet.getBoolean(DatabaseConstants.COLUMN_PAYMENT_RESIDUE));
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
				item.setFinancialSubGroup(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_DESCRIPTION));
				item.setProductId(resultSet.getLong(DatabaseConstants.COLUMN_PRODUCT_ID));
				item.setProductDescription(resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_DESCRIPTION));
				item.setDocumentTransfer(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_IS_DOCUMENT_TRANSFER));
				item.setAutomaticTransfer(resultSet.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_IS_AUTOMATIC_TRANSFER));
				
				if(item.getProductId()>0) {
					item.setKey(ConstantDataManager.PARAMETER_MOVEMENT_PRODUCT+item.getProductId());
				}
				else {
					item.setKey(ConstantDataManager.PARAMETER_MOVEMENT_TRANSFER+item.getDocumentId());
				}
				item.setBankAccountOriginDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_ORIGIN_DESCRIPTION));
				item.setProviderDescription(resultSet.getString(DatabaseConstants.COLUMN_PROVIDER_DESCRIPTION));
				item.populateDataDocumentNote();
				document.addItem(item);
			}
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}
}