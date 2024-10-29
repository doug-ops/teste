/**Create Date 08/09/2022*/
package com.manager.systems.web.movements.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;

import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.vo.OperationType;
import com.manager.systems.web.movements.dao.DreDao;
import com.manager.systems.web.movements.dto.dre.DreReportDTO;
import com.manager.systems.web.movements.dto.dre.DreReportFilterDTO;
import com.manager.systems.web.movements.dto.dre.DreReportItemDTO;

public class DreDaoImpl implements DreDao {
	private Connection connection;

	public DreDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public DreReportDTO getAllDre(final DreReportFilterDTO dre) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;

		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_dre");
		query.append("(");
		query.append("?, "); // 01 - IN _operation INT,
		query.append("?, "); // 02 - IN _company_id BIGINT,
		query.append("?, "); // 03 - IN _financial_group_id INT,
		query.append("?, "); // 04 - IN _financial_sub_group_id INT,
		query.append("?, "); // 05 - IN _bank_account_id INT,
		query.append("?, "); // 06 - IN _type_document_value BIT, 
		query.append("?, "); // 07 - IN _date_from DATETIME,
		query.append("?, "); // 08 - IN _date_to DATETIME,
		query.append("?, "); // 09 - IN _filter_data_type INT
		query.append("? "); //  10 - IN _user_id BIGINT
		query.append(")");
		query.append("}");
		
		final DreReportDTO report = new DreReportDTO();

		try {
			statement = connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); // 01 - IN _operation INT,
			if(dre.getCompanyId() != 0)
			{
				statement.setLong(2, dre.getCompanyId()); // 02 - IN _company_id BIGINT,
			}
			else
			{
				statement.setNull(2, Types.BIGINT); //02 - IN _company_id BIGINT,
			}
			statement.setString(3, dre.getFinancialGroupId()); // 03 - IN _financial_group_id VARCHAR(10),
			statement.setString(4, dre.getFinancialSubGroupId()); // 04 - IN _financial_sub_group_id VARCHAR(10),
			if(dre.getBankAccountId() != 0)
			{
				statement.setInt(5, dre.getBankAccountId()); // 05 - IN _bank_account_id INT,
			}
			else
			{
				statement.setNull(5, Types.INTEGER); // 05 - IN _bank_account_id INT,
			}
			if(dre.getTypeDocumentValue() != 0)
			{
				statement.setInt(6, dre.getTypeDocumentValue()); // 06 - IN _type_document_value INT,
			}
			else
			{
				statement.setNull(6, Types.BIGINT); // 06 - IN _type_document_value INT,
			}
			statement.setTimestamp(7, Timestamp.valueOf(dre.getDateFrom())); // 07 - IN _date_from DATETIME,
			statement.setTimestamp(8, Timestamp.valueOf(dre.getDateTo())); // 08 - IN _date_to DATETIME,
			if(dre.getFilterBy() != 0)
			{
				statement.setInt(9, dre.getFilterBy()); // 09 - IN _filter_data_type INT,
			}else
			{
				statement.setNull(9, Types.INTEGER); // 09 - IN _filter_data_type INT,
			}
			statement.setLong(10, dre.getUserOperation()); // 10 - IN _user_id BIGINT

			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				final DreReportItemDTO item = new DreReportItemDTO();
				item.setGroupTypeId(resultSet.getInt(DatabaseConstants.COLUMN_GROUP_TYPE_ID));
				item.setDescriptionGroupType(resultSet.getString(DatabaseConstants.COLUMN_GROUP_TYPE_DESCRIPTION));
				item.setFinancialGroupId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_ID));
				item.setDescriptionFinancialGroup(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP));
				item.setDescriptionFinancialSubGroup(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_ID));
				item.setFinancialSubGroupId(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP));
				item.setOrder(resultSet.getInt(DatabaseConstants.COLUMN_ORDER_TYPE));
				item.setDocumentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_ID));
				item.setDocumentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID));
				item.setDocumentNote(resultSet.getString(DatabaseConstants.COLUMN_DOCUMENT_NOTE));
				item.setDocumentValue(resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE));
				item.setPaymentValue(resultSet.getDouble(DatabaseConstants.COLUMN_PAYMENT_VALUE));
				if(dre.getDocumentType() == 2) {
					item.setTotal(item.getPaymentValue());					
				} else {
					item.setTotal(item.getDocumentValue());					
				}
				item.setCredit(resultSet.getBoolean(DatabaseConstants.COLUMN_CREDIT));
				report.addGroupType(item);
			}
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		
		return report;
	}
}