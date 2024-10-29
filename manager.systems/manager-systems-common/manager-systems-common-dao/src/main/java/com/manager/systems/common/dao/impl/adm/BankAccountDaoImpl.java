/**.
 * Date Create 23/08/2019
 */

package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.adm.BankAccountDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.BankAccountDTO;
import com.manager.systems.common.dto.adm.ReportBankAccountDTO;
import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.OperationType;

public class BankAccountDaoImpl implements BankAccountDao 
{
	private Connection connection;
	
	public BankAccountDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}

	@Override
	public boolean save(BankAccountDTO bankAccount) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_bank_account");
		query.append("(");
		query.append("?, "); 	//01 - IN _id_account_from BIGINT,
		query.append("?, "); 	//02 - IN _id_account_to BIGINT,
		query.append("?, "); 	//03 - IN _description VARCHAR(100),
		query.append("?, "); 	//04 - IN _bank_balance_available DECIMAL(19,2), 
		query.append("?, "); 	//05 - IN _bank_limit_available DECIMAL(19,2), 
		query.append("?, "); 	//06 - IN _bank_code VARCHAR(10), 
		query.append("?, "); 	//07 - IN _bank_angency VARCHAR(10), 
		query.append("?, "); 	//08 - IN _inactive VARCHAR, 
		query.append("?, "); 	//09 - IN _user_change BIGINT, 
		query.append("?, "); 	//10 - IN _operation TINYINT
		query.append("?, "); 	//11 - IN _bank_account_unbound TINYINT	
		query.append("?, "); 	//12 - IN _person_id BIGINT	
		query.append("?, "); 	//13 - IN _object_type VARCHAR(3)
		query.append("?  "); 	//14 - IN _accumulate_balance BIT		
		query.append(")");
		query.append("}");
		
		try 
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setLong(1, Long.valueOf(bankAccount.getId())); //01 - IN _id_account_from BIGINT,
			statement.setLong(2, Long.valueOf(bankAccount.getId())); //02 - IN _id_account_to BIGINT,
			statement.setString(3, bankAccount.getDescription()); //03 - IN _description VARCHAR(100),
			statement.setDouble(4, bankAccount.getBankBalanceAvailable()); //04 - IN _bank_balance_available DECIMAL(19,2),
			statement.setDouble(5, bankAccount.getBankLimitAvailable()); //05 - IN _bank_limit_available DECIMAL(19,2),
			statement.setString(6, bankAccount.getBankCode()); //06 - IN _bank_code VARCHAR(10),
			statement.setString(7, bankAccount.getBankAngency()); //07 - IN _bank_angency VARCHAR(10),
			statement.setInt(8, bankAccount.getInactiveInt()); //08 - IN _inactive INT,
			statement.setLong(9, bankAccount.getChangeData().getUserChange()); //09 - IN _user_change BIGINT,
			statement.setInt(10, OperationType.SAVE.getType()); //10 - IN _operation INT
			statement.setInt(11, 0); //11 - IN _bank_account_unbound TINYINT	
			statement.setLong(12, 0L); //12 - IN _person_id BIGINT
			statement.setString(13, ConstantDataManager.BLANK); // 13 - IN _object_type VARCHAR(3)
			statement.setBoolean(14, bankAccount.isAccumulateBalance()); //14 - IN _accumulate_balance BIT
			result = (statement.executeUpdate()>0);			
		} 
		catch (final Exception ex) 
		{
			throw ex;
		}
		finally 
		{
			if(statement != null)
			{
				statement.close();
			}
		}
		
		return result;
	}

	@Override
	public boolean inactive(BankAccountDTO bankAccount) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_bank_account");
		query.append("(");
		query.append("?, "); 	//01 - IN _id_account_from BIGINT,
		query.append("?, "); 	//02 - IN _id_account_to BIGINT,
		query.append("?, "); 	//03 - IN _description VARCHAR(100),
		query.append("?, "); 	//04 - IN _bank_balance_available DECIMAL(19,2), 
		query.append("?, "); 	//05 - IN _bank_limit_available DECIMAL(19,2), 
		query.append("?, "); 	//06 - IN _bank_code VARCHAR(10), 
		query.append("?, "); 	//07 - IN _bank_angency VARCHAR(10), 
		query.append("?, "); 	//08 - IN _inactive VARCHAR, 
		query.append("?, "); 	//09 - IN _user_change BIGINT, 
		query.append("?, "); 	//10 - IN _operation TINYINT
		query.append("?, "); 	//11 - IN _bank_account_unbound TINYINT	
		query.append("?, "); 	//12 - IN _person_id BIGINT	
		query.append("?, "); 	//13 - IN _object_type VARCHAR(3)
		query.append("?  "); 	//14 - IN _accumulate_balance BIT	
		query.append(")");
		query.append("}");
		
		try 
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setLong(1, Long.valueOf(bankAccount.getId())); //01 - IN _id_account_from BIGINT,
			statement.setLong(2, Long.valueOf(bankAccount.getId())); //02 - IN _id_account_to BIGINT,
			statement.setNull(3, Types.VARCHAR); //03 - IN _description VARCHAR(100),
			statement.setNull(4, Types.DECIMAL); //04 - IN _bank_balance_available DECIMAL(19,2),
			statement.setNull(5, Types.DECIMAL); //05 - IN _bank_limit_available DECIMAL(19,2),
			statement.setNull(6, Types.VARCHAR); //06 - IN _bank_code VARCHAR(10),
			statement.setNull(7, Types.VARCHAR); //07 - IN _bank_angency VARCHAR(10),
			statement.setBoolean(8, bankAccount.isInactive()); //08 - IN _inactive INT,
			statement.setLong(9, bankAccount.getChangeData().getUserChange()); //09 - IN _user_change BIGINT,
			statement.setInt(10, OperationType.INACTIVE.getType()); //10 - IN _operation INT
			statement.setInt(11, 0); //11 - IN _bank_account_unbound TINYINT
			statement.setLong(12, 0L); //12 - IN _person_id BIGINT
			statement.setString(13, ConstantDataManager.BLANK); // 13 - IN _object_type VARCHAR(3)
			statement.setBoolean(14, bankAccount.isAccumulateBalance()); //14 - IN _accumulate_balance BIT
	        result = (statement.executeUpdate()>0);
		} 
		catch (final Exception ex) 
		{
			throw ex;
		}
		finally 
		{
			if(statement != null)
			{
				statement.close();
			}
		}
		
		return result;
	}

	@Override
	public void get(BankAccountDTO bankAccount) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_bank_account");
		query.append("(");
		query.append("?, "); 	//01 - IN _id_account_from BIGINT,
		query.append("?, "); 	//02 - IN _id_account_to BIGINT,
		query.append("?, "); 	//03 - IN _description VARCHAR(100),
		query.append("?, "); 	//04 - IN _bank_balance_available DECIMAL(19,2), 
		query.append("?, "); 	//05 - IN _bank_limit_available DECIMAL(19,2), 
		query.append("?, "); 	//06 - IN _bank_code VARCHAR(10), 
		query.append("?, "); 	//07 - IN _bank_angency VARCHAR(10), 
		query.append("?, "); 	//08 - IN _inactive VARCHAR, 
		query.append("?, "); 	//09 - IN _user_change BIGINT, 
		query.append("?, "); 	//10 - IN _operation TINYINT
		query.append("?, "); 	//11 - IN _bank_account_unbound TINYINT	
		query.append("?, "); 	//12 - IN _person_id BIGINT	
		query.append("?, "); 	//13 - IN _object_type VARCHAR(3)
		query.append("?  "); 	//14 - IN _accumulate_balance BIT		
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;

		try 
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setLong(1, Long.valueOf(bankAccount.getId())); //01 - IN _id_account_from BIGINT,
			statement.setLong(2, Long.valueOf(bankAccount.getId())); //02 - IN _id_account_to BIGINT,
			statement.setNull(3, Types.VARCHAR); //03 - IN _description VARCHAR(100),
			statement.setNull(4, Types.DECIMAL); //04 - IN _bank_balance_available DECIMAL(19,2),
			statement.setNull(5, Types.DECIMAL); //05 - IN _bank_limit_available DECIMAL(19,2),
			statement.setNull(6, Types.VARCHAR); //06 - IN _bank_code VARCHAR(10),
			statement.setNull(7, Types.VARCHAR); //07 - IN _bank_angency VARCHAR(10),
			statement.setNull(8, Types.VARCHAR); //08 - IN _inactive INT,
			statement.setLong(9, Types.BIGINT); //09 - IN _user_change BIGINT,
			statement.setInt(10, OperationType.GET.getType()); //10 - IN _operation INT
			statement.setInt(11, 0); //11 - IN _bank_account_unbound TINYINT
			statement.setLong(12, 0L); //12 - IN _person_id BIGINT
			statement.setString(13, ConstantDataManager.BLANK); // 13 - IN _object_type VARCHAR(3)
			statement.setBoolean(14, bankAccount.isAccumulateBalance()); //14 - IN _accumulate_balance BIT

			resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				bankAccount.setId(resultSet.getString(DatabaseConstants.COLUMN_ID));
				bankAccount.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
				bankAccount.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserCreation(resultSet.getLong(DatabaseConstants.COLUMN_USER_CREATION));
	        	changeData.setCreationDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CREATION_DATE));
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	changeData.setUsernameChange(resultSet.getString(DatabaseConstants.COLUMN_USER_CHANGE_NAME));
	        	bankAccount.setChangeData(changeData);
	        	bankAccount.setBankBalanceAvailable(resultSet.getDouble(DatabaseConstants.COLUMN_BANK_ACCOUNT_BALANCE));
	        	bankAccount.setBankLimitAvailable(resultSet.getDouble(DatabaseConstants.COLUMN_BANK_ACCOUNT_LIMIT));
	        	bankAccount.setBankCode(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_CODE));
	        	bankAccount.setBankAngency(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_ANGENCY));
	        	bankAccount.setAccumulateBalance(resultSet.getBoolean(DatabaseConstants.COLUMN_BANK_ACCOUNT_ACCUMULATE_BALANCE));
			}
		} 
		catch (final Exception e) 
		{
			throw e;
		}
		finally 
		{
			if(resultSet != null)
			{
				resultSet.close();
			}
			if(statement != null)
			{
				statement.close();
			}
		}
	}

	@Override
	public void getAll(final ReportBankAccountDTO reportBankAccount) throws Exception 
	{
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_bank_account");
		query.append("(");
		query.append("?, "); 	//01 - IN _id_account_from BIGINT,
		query.append("?, "); 	//02 - IN _id_account_to BIGINT,
		query.append("?, "); 	//03 - IN _description VARCHAR(100),
		query.append("?, "); 	//04 - IN _bank_balance_available DECIMAL(19,2), 
		query.append("?, "); 	//05 - IN _bank_limit_available DECIMAL(19,2), 
		query.append("?, "); 	//06 - IN _bank_code VARCHAR(10), 
		query.append("?, "); 	//07 - IN _bank_angency VARCHAR(10), 
		query.append("?, "); 	//08 - IN _inactive VARCHAR, 
		query.append("?, "); 	//09 - IN _user_change BIGINT, 
		query.append("?, "); 	//10 - IN _operation TINYINT
		query.append("?, "); 	//11 - IN _bank_account_unbound TINYINT	
		query.append("?, "); 	//12 - IN _person_id BIGINT	
		query.append("?, "); 	//13 - IN _object_type VARCHAR(3)
		query.append("?  "); 	//14 - IN _accumulate_balance BIT	
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;

		try 
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setString(1, reportBankAccount.getBankAccountIdFrom()); //01 - IN _id_account_from BIGINT,
			statement.setString(2, reportBankAccount.getBankAccountIdTo()); //02 - IN _id_account_to BIGINT,
			statement.setString(3, reportBankAccount.getDescription()); //03 - IN _description VARCHAR(100),
			statement.setNull(4, Types.DECIMAL); //04 - IN _bank_balance_available DECIMAL(19,2),
			statement.setNull(5, Types.DECIMAL); //05 - IN _bank_limit_available DECIMAL(19,2),
			statement.setNull(6, Types.VARCHAR); //06 - IN _bank_code VARCHAR(10),
			statement.setNull(7, Types.VARCHAR); //07 - IN _bank_angency VARCHAR(10),
			statement.setInt(8, reportBankAccount.getInactive()); //08 - IN _inactive INT,
			statement.setLong(9, reportBankAccount.getUserOperation()); //09 - IN _user_change BIGINT,
			statement.setInt(10, OperationType.GET_ALL.getType()); //10 - IN _operation INT
			statement.setInt(11, 0); //11 - IN _bank_account_unbound TINYINT
			statement.setLong(12, 0L); //12 - IN _person_id BIGINT
			statement.setString(13, ConstantDataManager.BLANK); // 13 - IN _object_type VARCHAR(3)
			statement.setNull(14, Types.BOOLEAN); //14 - IN _accumulate_balance BIT

			resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				final BankAccountDTO item = new BankAccountDTO();
				item.setId(resultSet.getString(DatabaseConstants.COLUMN_ID));
				item.setDescription(resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION));
	        	item.setBankBalanceAvailable(resultSet.getDouble(DatabaseConstants.COLUMN_BANK_ACCOUNT_BALANCE));
	        	item.setBankLimitAvailable(resultSet.getDouble(DatabaseConstants.COLUMN_BANK_ACCOUNT_LIMIT));
	        	item.setBankCode(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_CODE));
	        	item.setBankAngency(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_ANGENCY));
				item.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	final ChangeData changeData = new ChangeData();
	        	changeData.setUserCreation(resultSet.getLong(DatabaseConstants.COLUMN_USER_CREATION));
	        	changeData.setCreationDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CREATION_DATE));
	        	changeData.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	changeData.setChangeDate(resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE));
	        	item.setChangeData(changeData);
	        	item.setAccumulateBalance(resultSet.getBoolean(DatabaseConstants.COLUMN_BANK_ACCOUNT_ACCUMULATE_BALANCE));
	        	reportBankAccount.addItem(item);
			}
		}
		catch (final Exception e) 
		{
			throw e;
		}
		finally 
		{
			if(resultSet != null)
			{
				resultSet.close();
			}
			if(statement != null)
			{
				statement.close();
			}
		}
	}

	@Override
	public List<Combobox> getAllCombobox(final ReportBankAccountDTO reportBankAccount) throws Exception 
	{
		final List<Combobox> itens = new ArrayList<Combobox>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_bank_account");
		query.append("(");
		query.append("?, "); 	//01 - IN _id_account_from BIGINT,
		query.append("?, "); 	//02 - IN _id_account_to BIGINT,
		query.append("?, "); 	//03 - IN _description VARCHAR(100),
		query.append("?, "); 	//04 - IN _bank_balance_available DECIMAL(19,2), 
		query.append("?, "); 	//05 - IN _bank_limit_available DECIMAL(19,2), 
		query.append("?, "); 	//06 - IN _bank_code VARCHAR(10), 
		query.append("?, "); 	//07 - IN _bank_angency VARCHAR(10), 
		query.append("?, "); 	//08 - IN _inactive VARCHAR, 
		query.append("?, "); 	//09 - IN _user_change BIGINT, 
		query.append("?, "); 	//10 - IN _operation TINYINT
		query.append("?, "); 	//11 - IN _bank_account_unbound TINYINT	
		query.append("?, "); 	//12 - IN _person_id BIGINT	
		query.append("?, "); 	//13 - IN _object_type VARCHAR(3)
		query.append("?  "); 	//14 - IN _accumulate_balance BIT		
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;

		try 
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setNull(1, Types.BIGINT); //01 - IN _id_account_from BIGINT,
			statement.setNull(2, Types.BIGINT); //02 - IN _id_account_to BIGINT,
			statement.setNull(3, Types.VARCHAR); //03 - IN _description VARCHAR(100),
			statement.setNull(4, Types.DECIMAL); //04 - IN _bank_balance_available DECIMAL(19,2),
			statement.setNull(5, Types.DECIMAL); //05 - IN _bank_limit_available DECIMAL(19,2),
			statement.setNull(6, Types.VARCHAR); //06 - IN _bank_code VARCHAR(10),
			statement.setNull(7, Types.VARCHAR); //07 - IN _bank_angency VARCHAR(10),
			statement.setNull(8, Types.INTEGER); //08 - IN _inactive INT,
			statement.setLong(9, reportBankAccount.getUserOperation()); //09 - IN _user_change BIGINT,
			statement.setInt(10, OperationType.GET_ALL_COMBOBOX.getType()); //10 - IN _operation INT
			statement.setInt(11, 0); //11 - IN _bank_account_unbound TINYINT
			statement.setLong(12, 0L); //12 - IN _person_id BIGINT
			statement.setString(13, ConstantDataManager.BLANK); // 13 - IN _object_type VARCHAR(3)
			statement.setNull(14, Types.BOOLEAN); //14 - IN _accumulate_balance BIT

			resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				itens.add(new Combobox(resultSet.getString(DatabaseConstants.COLUMN_ID), resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION)));
			}
		}
		catch (final Exception e) 
		{
			throw e;
		}
		finally 
		{
			if(resultSet != null)
			{
				resultSet.close();
			}
			if(statement != null)
			{
				statement.close();
			}
		}
		
		return itens;
	}

	@Override
	public int getCompanyIdByBankAccount(final int bankAccountId) throws Exception
	{
		int companyId = 0;
		
		final StringBuilder query = new StringBuilder();
		query.append("select co.id as company_id from company co inner join person pe on co.person_id=pe.id where pe.bank_account_id=?; ");				
		
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try
		{
			statement = this.connection.prepareStatement(query.toString());
			statement.setInt(1, bankAccountId);		
			rs = statement.executeQuery();
			
			while(rs.next())
			{
				companyId = rs.getInt(DatabaseConstants.COLUMN_COMPANY_ID);
			}
		}
		catch(final Exception e)
		{
			throw e;
		}
		return companyId;
	}

	@Override
	public List<Combobox> getAllAutocomplete(final ReportBankAccountDTO reportBankAccount) throws Exception 
	{
		final List<Combobox> itens = new ArrayList<Combobox>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_bank_account");
		query.append("(");
		query.append("?, "); 	//01 - IN _id_account_from BIGINT,
		query.append("?, "); 	//02 - IN _id_account_to BIGINT,
		query.append("?, "); 	//03 - IN _description VARCHAR(100),
		query.append("?, "); 	//04 - IN _bank_balance_available DECIMAL(19,2), 
		query.append("?, "); 	//05 - IN _bank_limit_available DECIMAL(19,2), 
		query.append("?, "); 	//06 - IN _bank_code VARCHAR(10), 
		query.append("?, "); 	//07 - IN _bank_angency VARCHAR(10), 
		query.append("?, "); 	//08 - IN _inactive VARCHAR, 
		query.append("?, "); 	//09 - IN _user_change BIGINT, 
		query.append("?, "); 	//10 - IN _operation TINYINT
		query.append("?, "); 	//11 - IN _bank_account_unbound TINYINT	
		query.append("?, "); 	//12 - IN _person_id BIGINT	
		query.append("?, "); 	//13 - IN _object_type VARCHAR(3)
		query.append("?  "); 	//14 - IN _accumulate_balance BIT		
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;

		try 
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setNull(1, Types.BIGINT); //01 - IN _id_account_from BIGINT,
			statement.setNull(2, Types.BIGINT); //02 - IN _id_account_to BIGINT,
			statement.setString(3, reportBankAccount.getDescription()); //03 - IN _description VARCHAR(100),
			statement.setNull(4, Types.DECIMAL); //04 - IN _bank_balance_available DECIMAL(19,2),
			statement.setNull(5, Types.DECIMAL); //05 - IN _bank_limit_available DECIMAL(19,2),
			statement.setNull(6, Types.VARCHAR); //06 - IN _bank_code VARCHAR(10),
			statement.setNull(7, Types.VARCHAR); //07 - IN _bank_angency VARCHAR(10),
			statement.setInt(8, reportBankAccount.getInactive()); //08 - IN _inactive INT,
			statement.setLong(9, reportBankAccount.getUserOperation()); //09 - IN _user_change BIGINT,
			statement.setInt(10, OperationType.AUTOCOMPLETE.getType()); //10 - IN _operation INT
			statement.setInt(11, reportBankAccount.getUnboundBankAccount()); //11 - IN _bank_account_unbound TINYINT
			statement.setLong(12, reportBankAccount.getPersonId()); //12 - IN _person_id BIGINT
			statement.setString(13, reportBankAccount.getObjectType()); // 13 - IN _object_type VARCHAR(3)
			statement.setNull(14, Types.BOOLEAN); //14 - IN _accumulate_balance BIT

			resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				itens.add(new Combobox(resultSet.getString(DatabaseConstants.COLUMN_ID), resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION)));
			}
		}
		catch (final Exception e) 
		{
			throw e;
		}
		finally 
		{
			if(resultSet != null)
			{
				resultSet.close();
			}
			if(statement != null)
			{
				statement.close();
			}
		}
		
		return itens;
	}

	@Override
	public List<Integer> getListBanckAccountIdOutDocumentParent() throws Exception {
		final List<Integer> itens = new ArrayList<Integer>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_bank_account");
		query.append("(");
		query.append("?, "); 	//01 - IN _id_account_from BIGINT,
		query.append("?, "); 	//02 - IN _id_account_to BIGINT,
		query.append("?, "); 	//03 - IN _description VARCHAR(100),
		query.append("?, "); 	//04 - IN _bank_balance_available DECIMAL(19,2), 
		query.append("?, "); 	//05 - IN _bank_limit_available DECIMAL(19,2), 
		query.append("?, "); 	//06 - IN _bank_code VARCHAR(10), 
		query.append("?, "); 	//07 - IN _bank_angency VARCHAR(10), 
		query.append("?, "); 	//08 - IN _inactive VARCHAR, 
		query.append("?, "); 	//09 - IN _user_change BIGINT, 
		query.append("?, "); 	//10 - IN _operation TINYINT
		query.append("?, "); 	//11 - IN _bank_account_unbound TINYINT	
		query.append("?, "); 	//12 - IN _person_id BIGINT	
		query.append("?, "); 	//13 - IN _object_type VARCHAR(3)
		query.append("?  "); 	//14 - IN _accumulate_balance BIT		
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;

		try 
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setNull(1, Types.BIGINT); //01 - IN _id_account_from BIGINT,
			statement.setNull(2, Types.BIGINT); //02 - IN _id_account_to BIGINT,
			statement.setNull(3, Types.VARCHAR); //03 - IN _description VARCHAR(100),
			statement.setNull(4, Types.DECIMAL); //04 - IN _bank_balance_available DECIMAL(19,2),
			statement.setNull(5, Types.DECIMAL); //05 - IN _bank_limit_available DECIMAL(19,2),
			statement.setNull(6, Types.VARCHAR); //06 - IN _bank_code VARCHAR(10),
			statement.setNull(7, Types.VARCHAR); //07 - IN _bank_angency VARCHAR(10),
			statement.setInt(8, Types.INTEGER); //08 - IN _inactive INT,
			statement.setNull(9, Types.BIGINT); //09 - IN _user_change BIGINT,
			statement.setInt(10, OperationType.OTHER.getType()); //10 - IN _operation INT
			statement.setNull(11, Types.INTEGER); //11 - IN _bank_account_unbound TINYINT
			statement.setNull(12, Types.BIGINT); //12 - IN _person_id BIGINT
			statement.setNull(13, Types.VARCHAR); // 13 - IN _object_type VARCHAR(3)
			statement.setNull(14, Types.BOOLEAN); //14 - IN _accumulate_balance BIT

			resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				itens.add(resultSet.getInt(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID));
			}
		}
		catch (final Exception e) 
		{
			throw e;
		}
		finally 
		{
			if(resultSet != null)
			{
				resultSet.close();
			}
			if(statement != null)
			{
				statement.close();
			}
		}
		
		return itens;
	}
}