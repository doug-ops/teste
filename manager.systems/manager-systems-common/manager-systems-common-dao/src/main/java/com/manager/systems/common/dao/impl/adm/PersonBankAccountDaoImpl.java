package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.manager.systems.common.dao.adm.PersonBankAccountDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.PersonBankAccountDTO;
import com.manager.systems.common.dto.adm.PersonBankAccountPermissionDTO;
import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.OperationType;

public class PersonBankAccountDaoImpl implements PersonBankAccountDao {
	private Connection connection;

	public PersonBankAccountDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public boolean save(final PersonBankAccountDTO item) throws Exception {
		boolean result = false;

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();

		query.append("{");
		query.append("call pr_maintenance_person_bank_account");
		query.append("(");
		query.append("?, "); // 01 - operation
		query.append("?, "); // 02 - IN _object_id BIGINT
		query.append("?, "); // 03 - IN _object_type VARCHAR(1),
		query.append("?, "); // 04 - IN bank_account_id INT,
		query.append("?, "); // 05 - IN _inactive INT,
		query.append("?  "); // 06 - IN _user_change BIGINT,
		query.append(")");
		query.append("}");

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); // 01 - operation
			statement.setLong(2, item.getObjectId()); // 02 - IN _object_id BIGINT
			statement.setString(3, item.getObjectType()); // 03 - IN _object_type VARCHAR(10),
			statement.setInt(4, item.getBankAccountId()); // 04 - IN _bank_account_id INT,
			statement.setInt(5, item.getInactive()); // 06 - _inactive INT,
			statement.setLong(6, item.getUserChange()); // 07 - _user_change BIGINT,
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
	public boolean delete(final PersonBankAccountDTO item) throws Exception {
		boolean result = false;

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();

		query.append("{");
		query.append("call pr_maintenance_person_bank_account");
		query.append("(");
		query.append("?, "); // 01 - operation
		query.append("?, "); // 02 - IN _object_id BIGINT
		query.append("?, "); // 03 - IN _object_type VARCHAR(1),
		query.append("?, "); // 04 - IN bank_account_id INT,
		query.append("?, "); // 05 - IN _inactive INT,
		query.append("?  "); // 06 - IN _user_change BIGINT,
		query.append(")");
		query.append("}");

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.DELETE.getType()); // 01 - operation
			statement.setLong(2, item.getObjectId()); // 02 - IN _object_id BIGINT
			statement.setString(3, item.getObjectType()); // 03 - IN _object_type VARCHAR(10),
			statement.setInt(4, item.getBankAccountId()); // 04 - IN _bank_account_id INT,
			statement.setInt(5, item.getInactive()); // 06 - _inactive INT,
			statement.setLong(6, item.getUserChange()); // 07 - _user_change BIGINT,
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
	public List<PersonBankAccountDTO> getAll(final PersonBankAccountDTO filter) throws Exception {
		final List<PersonBankAccountDTO> itens = new ArrayList<PersonBankAccountDTO>();

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();

		query.append("{");
		query.append("call pr_maintenance_person_bank_account");
		query.append("(");
		query.append("?, "); // 01 - operation
		query.append("?, "); // 02 - IN _object_id BIGINT
		query.append("?, "); // 03 - IN _object_type VARCHAR(1),
		query.append("?, "); // 04 - IN bank_account_id INT,
		query.append("?, "); // 05 - IN _inactive INT,
		query.append("?  "); // 06 - IN _user_change BIGINT,
		query.append(")");
		query.append("}");

		ResultSet resultSet = null;

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); // 01 - operation
			statement.setLong(2, filter.getObjectId()); // 02 - IN _object_id BIGINT
			statement.setString(3, filter.getObjectType()); // 03 - IN _object_type VARCHAR(10),
			statement.setInt(4, filter.getBankAccountId()); // 04 - IN _bank_account_id INT,
			statement.setInt(5, filter.getInactive()); // 06 - _inactive INT,
			statement.setLong(6, filter.getUserChange()); // 07 - _user_change BIGINT,
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				final PersonBankAccountDTO item = new PersonBankAccountDTO();
				item.setObjectId(resultSet.getLong(DatabaseConstants.COLUMN_PERSON_ID));
				item.setBankAccountId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID));
				item.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
				itens.add(item);
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
		return itens;
	}
	
	@Override
	public List<Combobox> getAllBankAccountByPerson(final PersonBankAccountDTO filter) throws Exception {
		final List<Combobox> itens = new ArrayList<Combobox>();

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();

		query.append("{");
		query.append("call pr_maintenance_person_bank_account");
		query.append("(");
		query.append("?, "); // 01 - operation
		query.append("?, "); // 02 - IN _object_id BIGINT
		query.append("?, "); // 03 - IN _object_type VARCHAR(1),
		query.append("?, "); // 04 - IN bank_account_id INT,
		query.append("?, "); // 05 - IN _inactive INT,
		query.append("?  "); // 06 - IN _user_change BIGINT,
		query.append(")");
		query.append("}");

		ResultSet resultSet = null;

		try {
			statement = this.connection.prepareCall(query.toString());
			if(ConstantDataManager.ORIGIN_BANK_ACCOUNT_STATEMENT.equalsIgnoreCase(filter.getOrigin())) {
				statement.setInt(1, 8); // 01 - operation				
			} else {
				statement.setInt(1, 7); // 01 - operation				
			}
			statement.setLong(2, filter.getObjectId()); // 02 - IN _object_id BIGINT
			statement.setString(3, filter.getObjectType()); // 03 - IN _object_type VARCHAR(10),
			statement.setInt(4, filter.getBankAccountId()); // 04 - IN _bank_account_id INT,
			statement.setInt(5, filter.getInactive()); // 06 - _inactive INT,
			statement.setLong(6, filter.getUserChange()); // 07 - _user_change BIGINT,
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				itens.add(new Combobox(
						resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID),
						resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION),
						resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_AVAILABLE), 
						resultSet.getString(DatabaseConstants.COLUMN_MAIN_BANK_ACCOUNT)
						));
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
		return itens;
	}
	
	@Override
	public Map<Long, List<Combobox>> getAllBankAccountByCompanys(final String companys) throws Exception {
		final Map<Long, List<Combobox>> items = new TreeMap<>();

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();

		query.append("{");
		query.append("call pr_filter_bank_account_by_company");
		query.append("(");
		query.append("?, "); // 01 - IN _operation
		query.append("?  "); // 02 - IN _companys VARCHAR(1000)
		query.append(")");
		query.append("}");

		ResultSet resultSet = null;

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, 1); // 01 - IN _operation
			statement.setString(2, companys); // 02 - IN _companys VARCHAR(1000)
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				final long companyId = resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID);
				List<Combobox> banksAccount = items.get(companyId);
				if(banksAccount == null) {
					banksAccount = new ArrayList<>();
				}
				
				banksAccount.add(new Combobox(
						resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID),
						resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESCRIPTION),
						resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_AVAILABLE), 
						resultSet.getString(DatabaseConstants.COLUMN_COMPANY_ID)
						));
				
				items.put(companyId, banksAccount);
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
		return items;
	}

	@Override
	public boolean saveBankAccountPermission(final PersonBankAccountPermissionDTO item) throws Exception {
		boolean result = false;

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();

		query.append("{");
		query.append("call pr_maintenance_person_bank_account_permission");
		query.append("(");
		query.append("?, "); // 01 - operation
		query.append("?, "); // 02 - IN _bank_account_id INT
		query.append("?, "); // 03 - IN _user_id BIGINT,
		query.append("?, "); // 04 - IN _inactive BIT,
		query.append("?  "); // 05 - IN _user_change BIGINT
		query.append(")");
		query.append("}");

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); // 01 - operation
			statement.setInt(2, item.getBankAccountId()); // 02 - IN _bank_account_id INT
			statement.setLong(3, item.getUserId()); // 03 - IN _user_id BIGINT,
			statement.setBoolean(4, item.isInactive()); // 04 - IN _inactive BIT,
			statement.setLong(5, item.getUserChange()); // 05 - IN _user_change BIGINT
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
	public boolean inactiveAllBankAccountPermission(final PersonBankAccountPermissionDTO item) throws Exception {
		boolean result = false;

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();

		query.append("{");
		query.append("call pr_maintenance_person_bank_account_permission");
		query.append("(");
		query.append("?, "); // 01 - operation
		query.append("?, "); // 02 - IN _bank_account_id INT
		query.append("?, "); // 03 - IN _user_id BIGINT,
		query.append("?, "); // 04 - IN _inactive BIT,
		query.append("?  "); // 05 - IN _user_change BIGINT
		query.append(")");
		query.append("}");

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.INACTIVE.getType()); // 01 - operation
			statement.setInt(2, item.getBankAccountId()); // 02 - IN _bank_account_id INT
			statement.setLong(3, item.getUserId()); // 03 - IN _user_id BIGINT,
			statement.setBoolean(4, item.isInactive()); // 04 - IN _inactive BIT,
			statement.setLong(5, item.getUserChange()); // 05 - IN _user_change BIGINT
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
	public List<PersonBankAccountPermissionDTO> getAllBankAccountPermissionsByUser(final long userId) throws Exception {
		final List<PersonBankAccountPermissionDTO> items = new ArrayList<PersonBankAccountPermissionDTO>();

		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();

		query.append("{");
		query.append("call pr_maintenance_person_bank_account_permission");
		query.append("(");
		query.append("?, "); // 01 - operation
		query.append("?, "); // 02 - IN _bank_account_id INT
		query.append("?, "); // 03 - IN _user_id BIGINT,
		query.append("?, "); // 04 - IN _inactive BIT,
		query.append("?  "); // 05 - IN _user_change BIGINT
		query.append(")");
		query.append("}");

		ResultSet resultSet = null;

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); // 01 - operation
			statement.setNull(2, Types.INTEGER); // 02 - IN _bank_account_id INT
			statement.setLong(3, userId); // 03 - IN _user_id BIGINT,
			statement.setNull(4, Types.BIT); // 04 - IN _inactive BIT,
			statement.setNull(5, Types.BIGINT); // 05 - IN _user_change BIGINT
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				items.add(PersonBankAccountPermissionDTO.builder().bankAccountId(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID)).userId(resultSet.getLong(DatabaseConstants.COLUMN_USER_ID)).build());
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
		return items;
	}
}