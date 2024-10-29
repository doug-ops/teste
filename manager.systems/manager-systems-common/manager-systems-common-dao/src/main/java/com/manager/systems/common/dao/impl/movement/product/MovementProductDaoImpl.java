package com.manager.systems.common.dao.impl.movement.product;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.movement.product.MovementProductDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.CompanyLastMovementExecutedDTO;
import com.manager.systems.common.dto.movement.company.MovementCompany;
import com.manager.systems.common.dto.movement.company.PreviewMovementCompanyDTO;
import com.manager.systems.common.dto.movement.company.PreviewMovementCompanyItemDTO;
import com.manager.systems.common.dto.movement.company.ReportMovementCompany;
import com.manager.systems.common.dto.movement.company.ReportMovementCompanyFinancialTransfer;
import com.manager.systems.common.dto.movement.company.ReportMovementCompanyProduct;
import com.manager.systems.common.dto.movement.product.MovementProductDTO;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.OperationType;

public class MovementProductDaoImpl implements MovementProductDao {
	private Connection connection;

	public MovementProductDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public void get(final MovementProductDTO movementProduct) throws Exception {
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_process_product_movement");
		query.append("(");
		query.append("?,"); // 01 - @operation INT
		query.append("?,"); // 02 - IN _movement_legacy_id BIGINT
		query.append("?,"); // 03 - IN _legacy_system_id INT
		query.append("?,"); // 04 - IN _product_id BIGINT
		query.append("?,"); // 05 - IN _company_description VARCHAR(10)
		query.append("?,"); // 06 - IN _credit_in_final BIGINT
		query.append("?,"); // 07 - IN _credit_out_final BIGINT
		query.append("?,"); // 08 - IN _reading_date DATETIME
		query.append("?,"); // 09 - IN _movement_type VARCHAR(1)
		query.append("?,"); // 10 - IN _inactive BIT
		query.append("?,"); // 11 - IN _change_user BIGINT
		query.append("?,"); // 12 - IN _credit_in_initial BIGINT,
		query.append("?,"); // 13 - IN _credit_out_initial BIGINT,
		query.append("?,"); // 14, IN _credit_clock_initial BIGINT,
		query.append("?,"); // 15 -- IN _credit_clock_final BIGINT,
		query.append("?,"); // 16 -- IN _company_id BIGINT,
		query.append("?,"); // 17 - IN _is_offline BIT,
		query.append("?,"); // 18 - IN _initial_date DATETIME,
		query.append("?,"); // 19 - IN _product_description VARCHAR(100),
		query.append("? "); // 20 - IN _user_id BIGINT
		query.append(")");
		query.append("}");

		ResultSet resultSet = null;

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); // 01 - @operation INT
			statement.setNull(2, Types.BIGINT); // 02 - IN _movement_legacy_id BIGINT
			statement.setNull(3, Types.INTEGER); // 03 - IN _legacy_system_id INT
			statement.setLong(4, movementProduct.getProductId()); // 04 - IN _product_id BIGINT
			statement.setNull(5, Types.VARCHAR); // 05 - IN _company_description VARCHAR(10)
			statement.setNull(6, Types.BIGINT); // 06 - IN _credit_in_final BIGINT
			statement.setNull(7, Types.BIGINT); // 07 - IN _credit_out_final BIGINT
			statement.setNull(8, Types.TIMESTAMP); // 08 - IN _reading_date DATETIME
			statement.setNull(9, Types.VARCHAR); // 09 - IN _movement_type VARCHAR(1)
			statement.setNull(10, Types.BIT); // 10 - IN _inactive BIT
			statement.setNull(11, Types.BIGINT); // 11 - IN _change_user BIGINT
			statement.setNull(12, Types.BIGINT); // 12 - IN _credit_in_initial BIGINT
			statement.setNull(13, Types.BIGINT); // 13 - IN _credit_out_initial BIGINT
			statement.setNull(14, Types.BIGINT); // 14 - IN _credit_clock_initial BIGINT
			statement.setNull(15, Types.BIGINT); // 15 - IN _credit_clock_final BIGINT
			statement.setLong(16, movementProduct.getCompanyId()); // 16 - IN _company_id BIGINT
			statement.setNull(17, Types.BIGINT); // 17 - IN _is_offline BIT
			statement.setNull(18, Types.TIMESTAMP); // 18 - IN _initial_date DATETIME
			statement.setNull(19, Types.VARCHAR); // 19 - IN _product_description VARCHAR(100),
			statement.setNull(20, Types.VARCHAR); // 20 - IN _user_id BIGINT
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				movementProduct.setMovementId(resultSet.getLong(DatabaseConstants.COLUMN_MOVEMENT_ID));
				movementProduct.setProductId(resultSet.getLong(DatabaseConstants.COLUMN_PRODUCT_ID));
				movementProduct
						.setProductDescription(resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_DESCRIPTION));
				movementProduct.setCompanyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID));
				movementProduct.setInitialInput(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_IN_INITIAL));
				movementProduct.setFinalInput(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_IN_FINAL));
				movementProduct.setInitialOutput(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_OUT_INITIAL));
				movementProduct.setFinalOutput(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_OUT_FINAL));
				movementProduct.setInitialClock(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_CLOCK_INITIAL));
				movementProduct.setFinalClock(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_CLOCK_FINAL));
				movementProduct.setLastProcessing(resultSet.getString(DatabaseConstants.COLUMN_LAST_PROCESSING));
				movementProduct
						.setEnableClockMovement(resultSet.getBoolean(DatabaseConstants.COLUMN_ENABLE_CLOCK_MOVEMENT));
			}
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
		}
	}

	@Override
	public boolean save(final MovementProductDTO movementProduct) throws Exception {
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_process_product_movement");
		query.append("(");
		query.append("?,"); // 01 - @operation INT
		query.append("?,"); // 02 - IN _movement_legacy_id BIGINT
		query.append("?,"); // 03 - IN _legacy_system_id INT
		query.append("?,"); // 04 - IN _product_id BIGINT
		query.append("?,"); // 05 - IN _company_description VARCHAR(10)
		query.append("?,"); // 06 - IN _credit_in_final BIGINT
		query.append("?,"); // 07 - IN _credit_out_final BIGINT
		query.append("?,"); // 08 - IN _reading_date DATETIME
		query.append("?,"); // 09 - IN _movement_type VARCHAR(1)
		query.append("?,"); // 10 - IN _inactive BIT
		query.append("?,"); // 11 - IN _change_user BIGINT
		query.append("?,"); // 12 - IN _credit_in_initial BIGINT,
		query.append("?,"); // 13 - IN _credit_out_initial BIGINT,
		query.append("?,"); // 14, IN _credit_clock_initial BIGINT,
		query.append("?,"); // 15 -- IN _credit_clock_final BIGINT,
		query.append("?,"); // 16 -- IN _company_id BIGINT,
		query.append("?,"); // 17 - IN _is_offline BIT,
		query.append("?,"); // 18 - IN _initial_date DATETIME,
		query.append("?,"); // 19 - IN _product_description VARCHAR(100),
		query.append("? "); // 20 - IN _user_id BIGINT
		query.append(")");
		query.append("}");

		boolean result = false;

		try {
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); // 01 - @operation INT
			statement.setLong(2, 0L); // 02 - IN _movement_legacy_id BIGINT
			statement.setInt(3, 0); // 03 - IN _legacy_system_id INT
			statement.setLong(4, movementProduct.getProductId()); // 04 - IN _product_id BIGINT
			statement.setString(5, movementProduct.getCompanyDescription()); // 05 - IN _company_description VARCHAR(10)
			statement.setLong(6, movementProduct.getFinalInput()); // 06 - IN _credit_in_final BIGINT
			statement.setLong(7, movementProduct.getFinalOutput()); // 07 - IN _credit_out_final BIGINT
			statement.setTimestamp(8, Timestamp.valueOf(movementProduct.getReadingDate())); // 08 - IN _reading_date
																							// DATETIME
			statement.setString(9, movementProduct.getMovementType()); // 09 - IN _movement_type VARCHAR(1)
			statement.setBoolean(10, movementProduct.isInactive()); // 10 - IN _inactive BIT
			statement.setLong(11, movementProduct.getChangeUser()); // 11 - IN _change_user BIGINT
			statement.setLong(12, movementProduct.getInitialInput()); // 12 - IN _credit_in_initial BIGINT
			statement.setLong(13, movementProduct.getInitialOutput()); // 13 - IN _credit_out_initial BIGINT
			statement.setLong(14, movementProduct.getInitialClock()); // 14 - IN _credit_clock_initial BIGINT
			statement.setLong(15, movementProduct.getFinalClock()); // 15 - IN _credit_clock_final BIGINT
			statement.setLong(16, movementProduct.getCompanyId()); // 16 - IN _company_id BIGINT
			statement.setBoolean(17, movementProduct.isOffline()); // 17 - IN _is_offline BIT
			statement.setNull(18, Types.TIMESTAMP); // 18 - IN _initial_date DATETIME
			statement.setNull(19, Types.VARCHAR); // 19 - IN _product_description VARCHAR(100),
			statement.setNull(20, Types.VARCHAR); // 20 - IN _user_id BIGINT
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
	public void getMovementsCompany(final MovementCompany movementCompany) throws Exception {
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_process_product_movement");
		query.append("(");
		query.append("?,"); // 01 - @operation INT
		query.append("?,"); // 02 - IN _movement_legacy_id BIGINT
		query.append("?,"); // 03 - IN _legacy_system_id INT
		query.append("?,"); // 04 - IN _product_id BIGINT
		query.append("?,"); // 05 - IN _company_description VARCHAR(10)
		query.append("?,"); // 06 - IN _credit_in_final BIGINT
		query.append("?,"); // 07 - IN _credit_out_final BIGINT
		query.append("?,"); // 08 - IN _reading_date DATETIME
		query.append("?,"); // 09 - IN _movement_type VARCHAR(1)
		query.append("?,"); // 10 - IN _inactive BIT
		query.append("?,"); // 11 - IN _change_user BIGINT
		query.append("?,"); // 12 - IN _credit_in_initial BIGINT,
		query.append("?,"); // 13 - IN _credit_out_initial BIGINT,
		query.append("?,"); // 14, IN _credit_clock_initial BIGINT,
		query.append("?,"); // 15 -- IN _credit_clock_final BIGINT,
		query.append("?,"); // 16 -- IN _company_id BIGINT,
		query.append("?,"); // 17 - IN _is_offline BIT,
		query.append("?,"); // 18 - IN _initial_date DATETIME,
		query.append("?,"); // 19 - IN _product_description VARCHAR(100),
		query.append("? "); // 20 - IN _user_id BIGINT
		query.append(")");
		query.append("}");

		ResultSet resultSet = null;

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); // 01 - @operation INT
			statement.setNull(2, Types.BIGINT); // 02 - IN _movement_legacy_id BIGINT
			statement.setNull(3, Types.INTEGER); // 03 - IN _legacy_system_id INT
			statement.setNull(4, Types.BIGINT); // 04 - IN _product_id BIGINT
			statement.setNull(5, Types.VARCHAR); // 05 - IN _company_description VARCHAR(10)
			statement.setNull(6, Types.BIGINT); // 06 - IN _credit_in_final BIGINT
			statement.setNull(7, Types.BIGINT); // 07 - IN _credit_out_final BIGINT
			statement.setNull(8, Types.TIMESTAMP); // 08 - IN _reading_date DATETIME
			statement.setNull(9, Types.VARCHAR); // 09 - IN _movement_type VARCHAR(1)
			statement.setNull(10, Types.BIT); // 10 - IN _inactive BIT
			statement.setNull(11, Types.BIGINT); // 11 - IN _change_user BIGINT
			statement.setNull(12, Types.BIGINT); // 12 - IN _credit_in_initial BIGINT
			statement.setNull(13, Types.BIGINT); // 13 - IN _credit_out_initial BIGINT
			statement.setNull(14, Types.BIGINT); // 14 - IN _credit_clock_initial BIGINT
			statement.setNull(15, Types.BIGINT); // 15 - IN _credit_clock_final BIGINT
			statement.setLong(16, movementCompany.getCompanyId()); // 16 - IN _company_id BIGINT
			statement.setBoolean(17, movementCompany.isOffline()); // 17 - IN _is_offline BIT
			statement.setNull(18, Types.TIMESTAMP); // 18 - IN _initial_date DATETIME
			statement.setNull(19, Types.VARCHAR); // 19 - IN _product_description VARCHAR(100),
			statement.setNull(20, Types.VARCHAR); // 20 - IN _user_id BIGINT
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				final MovementProductDTO movementProduct = new MovementProductDTO();
				movementProduct.setMovementId(resultSet.getLong(DatabaseConstants.COLUMN_MOVEMENT_ID));
				movementProduct.setProductId(resultSet.getLong(DatabaseConstants.COLUMN_PRODUCT_ID));
				movementProduct
						.setProductDescription(resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_DESCRIPTION));
				movementProduct.setCompanyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID));
				movementProduct.setInitialInput(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_IN_INITIAL));
				movementProduct.setFinalInput(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_IN_FINAL));
				movementProduct.setInitialOutput(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_OUT_INITIAL));
				movementProduct.setFinalOutput(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_OUT_FINAL));
				movementProduct.setInitialClock(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_CLOCK_INITIAL));
				movementProduct.setFinalClock(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_CLOCK_FINAL));
				movementProduct.setTotalMovement(resultSet.getDouble(DatabaseConstants.COLUMN_TOTAL));
				movementProduct.setReadingDateString(resultSet.getString(DatabaseConstants.COLUMN_READING_DATE));
				movementProduct.setConversionFactor(resultSet.getString(DatabaseConstants.COLUMN_CONVERSION_FACTOR));
				movementProduct.setInitialDateString(resultSet.getString(DatabaseConstants.COLUMN_INITIAL_DATE));
				movementCompany.addItem(movementProduct);

				final boolean enableClockMovement = resultSet
						.getBoolean(DatabaseConstants.COLUMN_ENABLE_CLOCK_MOVEMENT);

				if (enableClockMovement) {
					movementCompany.setEnableClockMovement(enableClockMovement);
				}
			}
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
		}
	}

	@Override
	public void previewProcessMovement(final PreviewMovementCompanyDTO movementCompany) throws Exception {
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_process_document_movement");
		query.append("(");
		query.append("?,"); // 01 - IN _user BIGINT
		query.append("?,"); // 02 - IN _company_id BIGINT
		query.append("?,"); // 03 - IN _is_offline BIT
		query.append("?,"); // 04 - IN _movement_ids VARCHAR(1000)
		query.append("? "); // 05 - IN _is_preview BIT
		query.append(")");
		query.append("}");

		ResultSet resultSet = null;

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setLong(1, movementCompany.getUserChange()); // 01 - IN _user BIGINT
			statement.setLong(2, movementCompany.getCompnayId()); // 02 - IN _company_id BIGINT
			statement.setBoolean(3, movementCompany.isOffline()); // 03 - IN _is_offline BIT
			statement.setString(4, movementCompany.getMovementsId()); // 04 - IN _movement_ids VARCHAR(1000)
			statement.setBoolean(5, movementCompany.isPreview()); // 05 - IN _is_preview BIT

			int count = 0;
			boolean hasResults = statement.execute();
			do {
				if (hasResults) {
					resultSet = statement.getResultSet();
					while (resultSet.next()) {
						if (0 == count) {
							movementCompany.setInitialBalance(
									resultSet.getDouble(DatabaseConstants.COLUMN_BANK_ACCOUNT_BALANCE));
							movementCompany
									.setBankAccountId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID));
							movementCompany.setBankAccountDescription(
									resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESCRIPTION));
						} else if (1 == count) {
							movementCompany.setCompnayId(resultSet.getLong(DatabaseConstants.COLUMN_ID));
							movementCompany.setCompanyDescription(
									resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION));
						} else {
							final int bankAccountId = resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID);
							if (!movementCompany.getListBanckAccountIdOutDocumentParent().contains(bankAccountId)) {
								final PreviewMovementCompanyItemDTO item = new PreviewMovementCompanyItemDTO();
								item.setExecutionOrder(
										resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_EXECUTION_ORDER));
								item.setGroupId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ID));
								item.setDescriptionGroup(resultSet
										.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_DESCRIPTION_GROUP));
								item.setCredit(resultSet.getBoolean(DatabaseConstants.COLUMN_CREDIT));
								item.setDocumentValue(resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
								item.setDocumentNote(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE));
								item.setPaymentDate(resultSet.getString(DatabaseConstants.COLUMN_PAYMENT_DATA));
								item.setInput(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_INITIAL));
								item.setOutput(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_FINAL));
								item.setProductMovement(resultSet
										.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_IS_PRODUCT_MOVEMENT));
								item.setExecutionOrder(resultSet.getInt(DatabaseConstants.COLUMN_EXECUTION_ORDER));
								item.setProductId(resultSet.getLong(DatabaseConstants.COLUMN_PRODUCT_ID));
								item.setProviderDescription(
										resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_DESCRIPTION));
								item.setGroupExecutionOrder(
										resultSet.getInt(DatabaseConstants.COLUMN_GROUP_EXECUTION_ORDER));
								item.setGroupItemExecutionOrder(
										resultSet.getInt(DatabaseConstants.COLUMN_GROUP_ITEM_EXECUTION_ORDER));
								movementCompany.addItem(String.valueOf(item.getGroupId()), item);

								if (movementCompany.getMovementCompany() == null) {
									movementCompany.setMovementCompany(new ReportMovementCompany());
								}
								final boolean isProductMovement = resultSet
										.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_IS_PRODUCT_MOVEMENT);
								if (isProductMovement) {
									movementCompany.setDocumentParentId(
											resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID));
									final ReportMovementCompanyProduct productMovement = new ReportMovementCompanyProduct();
									productMovement.setGroupId(
											resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ID));
									productMovement.setGroupDescription(resultSet
											.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_DESCRIPTION_GROUP));
									productMovement
											.setProductId(resultSet.getLong(DatabaseConstants.COLUMN_PRODUCT_ID));
									productMovement.setProductDescription(
											resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_DESCRIPTION));
									productMovement.populateDataDocumentNote(
											resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE));
									productMovement.setExecutionOrder(
											resultSet.getInt(DatabaseConstants.COLUMN_EXECUTION_ORDER));
									productMovement.setGroupExecutionOrder(
											resultSet.getInt(DatabaseConstants.COLUMN_GROUP_EXECUTION_ORDER));
									productMovement.setGroupItemExecutionOrder(
											resultSet.getInt(DatabaseConstants.COLUMN_GROUP_ITEM_EXECUTION_ORDER));
									if (productMovement.isInput()) {
										productMovement.setTotalCreditIn(
												resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
									} else {
										final String noteDocumentProduct = resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE);
										if(noteDocumentProduct.indexOf("CI:") > -1) {
											productMovement.setTotalCreditClock(resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
										} else {
											productMovement.setTotalCreditOut(resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
										}
									}
									movementCompany.getMovementCompany().addProductMovement(productMovement);
								} else {
									final ReportMovementCompanyFinancialTransfer transferMovement = new ReportMovementCompanyFinancialTransfer();
									transferMovement.setGroupId(
											resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_GROUP_ID));
									transferMovement.setGroupDescription(resultSet
											.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_DESCRIPTION_GROUP));
									transferMovement.setTransferDate(
											resultSet.getString(DatabaseConstants.COLUMN_PAYMENT_DATA));
									transferMovement.setTransferDescription(
											resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE));
									transferMovement.setTransferValue(
											resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
									transferMovement.setExecutionOrder(
											resultSet.getInt(DatabaseConstants.COLUMN_EXECUTION_ORDER));
									transferMovement.setGroupExecutionOrder(
											resultSet.getInt(DatabaseConstants.COLUMN_GROUP_EXECUTION_ORDER));
									transferMovement.setGroupItemExecutionOrder(
											resultSet.getInt(DatabaseConstants.COLUMN_GROUP_ITEM_EXECUTION_ORDER));
									movementCompany.getMovementCompany().addTransferMovement(transferMovement);
								}
							}
						}
					}
				}
				hasResults = statement.getMoreResults();
				count++;
			} while (hasResults);

			movementCompany.getMovementCompany().sorListMovement();
			movementCompany.sorListMovementProducts();
			movementCompany.sorListMovementTransfers();
			movementCompany.calculateBalanceMovement();
			movementCompany.calculateFinalBalance();
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
		}
	}

	@Override
	public long processMovement(final PreviewMovementCompanyDTO movementCompany) throws Exception {
		ResultSet resultSet = null;

		long documentParentId = 0L;

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_process_document_movement");
		query.append("(");
		query.append("?,"); // 01 - IN _user BIGINT
		query.append("?,"); // 02 - IN _company_id BIGINT
		query.append("?,"); // 03 - IN _is_offline BIT
		query.append("?,"); // 04 - IN _movement_ids VARCHAR(1000)
		query.append("? "); // 05 - IN _is_preview BIT
		query.append(")");
		query.append("}");

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setLong(1, movementCompany.getUserChange()); // 01 - IN _user BIGINT
			statement.setLong(2, movementCompany.getCompnayId()); // 02 - IN _company_id BIGINT
			statement.setBoolean(3, movementCompany.isOffline()); // 03 - IN _is_offline BIT
			statement.setString(4, movementCompany.getMovementsId()); // 04 - IN _movement_ids VARCHAR(1000)
			statement.setBoolean(5, movementCompany.isPreview()); // 05 - IN _is_preview BIT
			final boolean isProcessed = statement.execute();

			if (isProcessed) {
				resultSet = statement.getResultSet();
				while (resultSet.next()) {
					documentParentId = resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID);
				}
			}
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
		}
		return documentParentId;
	}

	@Override
	public PreviewMovementCompanyDTO reportCompanyMovement(final Long documentParentId, final Long companyId)
			throws Exception {
		int count = 0;
		PreviewMovementCompanyDTO movementCompany = null;

		CallableStatement statement = null;

		StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_movement_report_email");
		query.append("(");
		query.append("?,"); // 01 - IN _operation INT,
		query.append("?,"); // 02 - IN _document_parent_id BIGINT,
		query.append("? "); // 03 - IN _company_id BIGINT
		query.append(")");
		query.append("}");

		ResultSet resultSet = null;

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 1); // 01 - IN _operation INT,
			statement.setLong(2, documentParentId); // 02 - IN _document_parent_id BIGINT,
			statement.setLong(3, companyId); // 03 - IN _company_id BIGINT

			boolean hasResults = statement.execute();

			if (hasResults) {
				movementCompany = new PreviewMovementCompanyDTO();
			}

			do {
				if (hasResults) {
					resultSet = statement.getResultSet();
					while (resultSet.next()) {
						if (0 == count) {
							movementCompany.setInitialBalance(
									resultSet.getDouble(DatabaseConstants.COLUMN_BANK_ACCOUNT_BALANCE));
							movementCompany
									.setBankAccountId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID));
							movementCompany.setBankAccountDescription(
									resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESCRIPTION));
						} else if (1 == count) {
							movementCompany.setCompnayId(resultSet.getLong(DatabaseConstants.COLUMN_ID));
							movementCompany.setCompanyDescription(
									resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION));
						} else {
							movementCompany.setDocumentParentId(
									resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID));
							movementCompany.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
							movementCompany.setUserName(resultSet.getString(DatabaseConstants.COLUMN_USER_CHANGE_NAME));
							final int bankAccountId = resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID);
							if (!movementCompany.getListBanckAccountIdOutDocumentParent().contains(bankAccountId)) {
								final PreviewMovementCompanyItemDTO item = new PreviewMovementCompanyItemDTO();
								item.setExecutionOrder(resultSet.getInt(DatabaseConstants.COLUMN_ID));
								item.setGroupId(resultSet
										.getInt(DatabaseConstants.COLUMN_FINANCIAL_DOCUMENT_GROUP_TRANSFER_ID));
								// item.setDescriptionGroup(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_DESCRIPTION_GROUP));
								item.setCredit(resultSet.getBoolean(DatabaseConstants.COLUMN_CREDIT));
								item.setDocumentValue(resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
								item.setDocumentNote(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE));
								item.setPaymentDate(resultSet.getString(DatabaseConstants.COLUMN_PAYMENT_DATA));
								// item.setInput(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_INITIAL));
								// item.setOutput(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_FINAL));
								item.setProductMovement(resultSet
										.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_IS_PRODUCT_MOVEMENT));
								item.setProductId(resultSet.getLong(DatabaseConstants.COLUMN_PRODUCT_ID));
								item.setProviderDescription(
										resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_DESCRIPTION));
								item.setGroupExecutionOrder(
										resultSet.getInt(DatabaseConstants.COLUMN_GROUP_EXECUTION_ORDER));
								item.setGroupItemExecutionOrder(
										resultSet.getInt(DatabaseConstants.COLUMN_GROUP_ITEM_EXECUTION_ORDER));
								movementCompany.addItem(String.valueOf(item.getGroupId()), item);

								if (movementCompany.getMovementCompany() == null) {
									movementCompany.setMovementCompany(new ReportMovementCompany());
								}
								final boolean isProductMovement = resultSet
										.getBoolean(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_IS_PRODUCT_MOVEMENT);
								if (isProductMovement) {
									final ReportMovementCompanyProduct productMovement = new ReportMovementCompanyProduct();
									productMovement.setGroupId(resultSet
											.getInt(DatabaseConstants.COLUMN_FINANCIAL_DOCUMENT_GROUP_TRANSFER_ID));
									// productMovement.setGroupDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_DESCRIPTION_GROUP));
									productMovement
											.setProductId(resultSet.getLong(DatabaseConstants.COLUMN_PRODUCT_ID));
									productMovement.setProductDescription(
											resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_DESCRIPTION));
									productMovement.populateDataDocumentNote(
											resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE));
									productMovement.setExecutionOrder(resultSet.getInt(DatabaseConstants.COLUMN_ID));
									productMovement.setGroupExecutionOrder(
											resultSet.getInt(DatabaseConstants.COLUMN_GROUP_EXECUTION_ORDER));
									productMovement.setGroupItemExecutionOrder(
											resultSet.getInt(DatabaseConstants.COLUMN_GROUP_ITEM_EXECUTION_ORDER));
									if (productMovement.isInput()) {
										productMovement.setTotalCreditIn(
												resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
									} else {
										final String noteDocumentProduct = resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE);
										if(noteDocumentProduct.indexOf("CI:") > -1) {
											productMovement.setTotalCreditClock(resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
										} else {
											productMovement.setTotalCreditOut(resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
										}
									}
									movementCompany.getMovementCompany().addProductMovement(productMovement);
								} else {

									movementCompany.setMovementDate(
											resultSet.getTimestamp(DatabaseConstants.COLUMN_PAYMENT_DATA));

									final ReportMovementCompanyFinancialTransfer transferMovement = new ReportMovementCompanyFinancialTransfer();
									transferMovement.setGroupId(resultSet
											.getInt(DatabaseConstants.COLUMN_FINANCIAL_DOCUMENT_GROUP_TRANSFER_ID));
									// transferMovement.setGroupDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_TRANSFER_DESCRIPTION_GROUP));
									transferMovement.setTransferDate(StringUtils.formatDate(
											resultSet.getTimestamp(DatabaseConstants.COLUMN_PAYMENT_DATA),
											StringUtils.DATE_PATTERN_DD_MM_YYYY));
									transferMovement.setTransferDescription(
											resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE));
									transferMovement.setTransferValue(
											resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
									transferMovement.setExecutionOrder(resultSet.getInt(DatabaseConstants.COLUMN_ID));
									transferMovement.setGroupExecutionOrder(
											resultSet.getInt(DatabaseConstants.COLUMN_GROUP_EXECUTION_ORDER));
									transferMovement.setGroupItemExecutionOrder(
											resultSet.getInt(DatabaseConstants.COLUMN_GROUP_ITEM_EXECUTION_ORDER));
									movementCompany.getMovementCompany().addTransferMovement(transferMovement);
								}
							}
						}
					}
				}
				hasResults = statement.getMoreResults();
				count++;
			} while (hasResults);

			movementCompany.getMovementCompany().sorListMovement();
			movementCompany.sorListMovementProducts();
			movementCompany.sorListMovementTransfers();
			movementCompany.calculateBalanceMovement();
			movementCompany.calculateFinalBalance();
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}

		}
		return (movementCompany);
	}

	@Override
	public boolean saveSynchronize(final MovementProductDTO movementProduct) throws Exception {
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_process_product_movement");
		query.append("(");
		query.append("?,"); // 01 - @operation INT
		query.append("?,"); // 02 - IN _movement_legacy_id BIGINT
		query.append("?,"); // 03 - IN _legacy_system_id INT
		query.append("?,"); // 04 - IN _product_id BIGINT
		query.append("?,"); // 05 - IN _company_description VARCHAR(10)
		query.append("?,"); // 06 - IN _credit_in_final BIGINT
		query.append("?,"); // 07 - IN _credit_out_final BIGINT
		query.append("?,"); // 08 - IN _reading_date DATETIME
		query.append("?,"); // 09 - IN _movement_type VARCHAR(1)
		query.append("?,"); // 10 - IN _inactive BIT
		query.append("?,"); // 11 - IN _change_user BIGINT
		query.append("?,"); // 12 - IN _credit_in_initial BIGINT,
		query.append("?,"); // 13 - IN _credit_out_initial BIGINT,
		query.append("?,"); // 14, IN _credit_clock_initial BIGINT,
		query.append("?,"); // 15 -- IN _credit_clock_final BIGINT,
		query.append("?,"); // 16 -- IN _company_id BIGINT,
		query.append("?,"); // 17 - IN _is_offline BIT,
		query.append("?,"); // 18 - IN _initial_date DATETIME,
		query.append("?,"); // 19 - IN _product_description VARCHAR(100),
		query.append("? "); // 20 - IN _user_id BIGINT
		query.append(")");
		query.append("}");

		boolean result = false;

		try {
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE_SYNCHRONIZE.getType()); // 01 - @operation INT
			statement.setLong(2, 0L); // 02 - IN _movement_legacy_id BIGINT
			statement.setInt(3, 0); // 03 - IN _legacy_system_id INT
			statement.setLong(4, movementProduct.getProductId()); // 04 - IN _product_id BIGINT
			statement.setString(5, movementProduct.getCompanyDescription()); // 05 - IN _company_description VARCHAR(10)
			statement.setLong(6, movementProduct.getFinalInput()); // 06 - IN _credit_in_final BIGINT
			statement.setLong(7, movementProduct.getFinalOutput()); // 07 - IN _credit_out_final BIGINT
			statement.setTimestamp(8, Timestamp.valueOf(movementProduct.getReadingDate())); // 08 - IN _reading_date
																							// DATETIME
			statement.setString(9, movementProduct.getMovementType()); // 09 - IN _movement_type VARCHAR(1)
			statement.setBoolean(10, movementProduct.isInactive()); // 10 - IN _inactive BIT
			statement.setLong(11, movementProduct.getChangeUser()); // 11 - IN _change_user BIGINT
			statement.setNull(12, Types.INTEGER); // 12 - IN _credit_in_initial BIGINT
			statement.setNull(13, Types.INTEGER); // 13 - IN _credit_out_initial BIGINT
			statement.setLong(14, movementProduct.getInitialClock()); // 14 - IN _credit_clock_initial BIGINT
			statement.setLong(15, movementProduct.getFinalClock()); // 15 - IN _credit_clock_final BIGINT
			statement.setLong(16, movementProduct.getCompanyId()); // 16 - IN _company_id BIGINT
			statement.setBoolean(17, movementProduct.isOffline()); // 17 - IN _is_offline BIT
			statement.setNull(18, Types.TIMESTAMP); // 18 - IN _initial_date DATETIME
			statement.setNull(19, Types.VARCHAR); // 19 - IN _product_description VARCHAR(100),
			statement.setNull(20, Types.VARCHAR); // 20 - IN _user_id BIGINT
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
	public List<Long> hasNoMovementProduct(final long companyId, final long user) throws Exception {
		final List<Long> hasNoMovement = new ArrayList<>();

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_process_product_movement");
		query.append("(");
		query.append("?,"); // 01 - @operation INT
		query.append("?,"); // 02 - IN _movement_legacy_id BIGINT
		query.append("?,"); // 03 - IN _legacy_system_id INT
		query.append("?,"); // 04 - IN _product_id BIGINT
		query.append("?,"); // 05 - IN _company_description VARCHAR(10)
		query.append("?,"); // 06 - IN _credit_in_final BIGINT
		query.append("?,"); // 07 - IN _credit_out_final BIGINT
		query.append("?,"); // 08 - IN _reading_date DATETIME
		query.append("?,"); // 09 - IN _movement_type VARCHAR(1)
		query.append("?,"); // 10 - IN _inactive BIT
		query.append("?,"); // 11 - IN _change_user BIGINT
		query.append("?,"); // 12 - IN _credit_in_initial BIGINT,
		query.append("?,"); // 13 - IN _credit_out_initial BIGINT,
		query.append("?,"); // 14, IN _credit_clock_initial BIGINT,
		query.append("?,"); // 15 -- IN _credit_clock_final BIGINT,
		query.append("?,"); // 16 -- IN _company_id BIGINT,
		query.append("?,"); // 17 - IN _is_offline BIT,
		query.append("?,"); // 18 - IN _initial_date DATETIME,
		query.append("?,"); // 19 - IN _product_description VARCHAR(100),
		query.append("? "); // 20 - IN _user_id BIGINT
		query.append(")");
		query.append("}");

		ResultSet resultSet = null;
		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_HAS_NO_MOVEMENT_PRODUCT.getType()); // 01 - @operation INT
			statement.setNull(2, Types.BIGINT); // 02 - IN _movement_legacy_id BIGINT
			statement.setNull(3, Types.INTEGER); // 03 - IN _legacy_system_id INT
			statement.setNull(4, Types.BIGINT); // 04 - IN _product_id BIGINT
			statement.setNull(5, Types.VARCHAR); // 05 - IN _company_description VARCHAR(10)
			statement.setNull(6, Types.BIGINT); // 06 - IN _credit_in_final BIGINT
			statement.setNull(7, Types.BIGINT); // 07 - IN _credit_out_final BIGINT
			statement.setNull(8, Types.TIMESTAMP); // 08 - IN _reading_date DATETIME
			statement.setNull(9, Types.VARCHAR); // 09 - IN _movement_type VARCHAR(1)
			statement.setNull(10, Types.BIT); // 10 - IN _inactive BIT
			statement.setNull(11, Types.BIGINT); // 11 - IN _change_user BIGINT
			statement.setNull(12, Types.BIGINT); // 12 - IN _credit_in_initial BIGINT
			statement.setNull(13, Types.BIGINT); // 13 - IN _credit_out_initial BIGINT
			statement.setNull(14, Types.BIGINT); // 14 - IN _credit_clock_initial BIGINT
			statement.setNull(15, Types.BIGINT); // 15 - IN _credit_clock_final BIGINT
			statement.setLong(16, companyId); // 16 - IN _company_id BIGINT
			statement.setNull(17, Types.BOOLEAN); // 17 - IN _is_offline BIT
			statement.setNull(18, Types.TIMESTAMP); // 18 - IN _initial_date DATETIME
			statement.setNull(19, Types.VARCHAR); // 19 - IN _product_description VARCHAR(100)
			statement.setLong(20, user); // 20 - IN _user_id BIGINT
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				hasNoMovement.add(resultSet.getLong(DatabaseConstants.COLUMN_HAS_NO_MOVEMENT));
			}
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return hasNoMovement;
	}
	
	@Override
	public CompanyLastMovementExecutedDTO companyLastMovementExecuted(final long companyId, final long user) throws Exception {
		CompanyLastMovementExecutedDTO item = null;

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_process_product_movement");
		query.append("(");
		query.append("?,"); // 01 - @operation INT
		query.append("?,"); // 02 - IN _movement_legacy_id BIGINT
		query.append("?,"); // 03 - IN _legacy_system_id INT
		query.append("?,"); // 04 - IN _product_id BIGINT
		query.append("?,"); // 05 - IN _company_description VARCHAR(10)
		query.append("?,"); // 06 - IN _credit_in_final BIGINT
		query.append("?,"); // 07 - IN _credit_out_final BIGINT
		query.append("?,"); // 08 - IN _reading_date DATETIME
		query.append("?,"); // 09 - IN _movement_type VARCHAR(1)
		query.append("?,"); // 10 - IN _inactive BIT
		query.append("?,"); // 11 - IN _change_user BIGINT
		query.append("?,"); // 12 - IN _credit_in_initial BIGINT,
		query.append("?,"); // 13 - IN _credit_out_initial BIGINT,
		query.append("?,"); // 14, IN _credit_clock_initial BIGINT,
		query.append("?,"); // 15 -- IN _credit_clock_final BIGINT,
		query.append("?,"); // 16 -- IN _company_id BIGINT,
		query.append("?,"); // 17 - IN _is_offline BIT,
		query.append("?,"); // 18 - IN _initial_date DATETIME,
		query.append("?,"); // 19 - IN _product_description VARCHAR(100),
		query.append("? "); // 20 - IN _user_id BIGINT
		query.append(")");
		query.append("}");

		ResultSet resultSet = null;
		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 23); // 01 - @operation INT
			statement.setNull(2, Types.BIGINT); // 02 - IN _movement_legacy_id BIGINT
			statement.setNull(3, Types.INTEGER); // 03 - IN _legacy_system_id INT
			statement.setNull(4, Types.BIGINT); // 04 - IN _product_id BIGINT
			statement.setNull(5, Types.VARCHAR); // 05 - IN _company_description VARCHAR(10)
			statement.setNull(6, Types.BIGINT); // 06 - IN _credit_in_final BIGINT
			statement.setNull(7, Types.BIGINT); // 07 - IN _credit_out_final BIGINT
			statement.setNull(8, Types.TIMESTAMP); // 08 - IN _reading_date DATETIME
			statement.setNull(9, Types.VARCHAR); // 09 - IN _movement_type VARCHAR(1)
			statement.setNull(10, Types.BIT); // 10 - IN _inactive BIT
			statement.setNull(11, Types.BIGINT); // 11 - IN _change_user BIGINT
			statement.setNull(12, Types.BIGINT); // 12 - IN _credit_in_initial BIGINT
			statement.setNull(13, Types.BIGINT); // 13 - IN _credit_out_initial BIGINT
			statement.setNull(14, Types.BIGINT); // 14 - IN _credit_clock_initial BIGINT
			statement.setNull(15, Types.BIGINT); // 15 - IN _credit_clock_final BIGINT
			statement.setLong(16, companyId); // 16 - IN _company_id BIGINT
			statement.setNull(17, Types.BOOLEAN); // 17 - IN _is_offline BIT
			statement.setNull(18, Types.TIMESTAMP); // 18 - IN _initial_date DATETIME
			statement.setNull(19, Types.VARCHAR); // 19 - IN _product_description VARCHAR(100)
			statement.setLong(20, user); // 20 - IN _user_id BIGINT
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				item = new CompanyLastMovementExecutedDTO();
				item.setCompanyId(resultSet.getInt(DatabaseConstants.COLUMN_COMPANY_ID));
				item.setDocumentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID));
				item.setDateTimeExecution(resultSet.getString(DatabaseConstants.COLUMN_PAYMENT_DATA));
			}
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return item;
	}
}