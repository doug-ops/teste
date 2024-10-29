package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.manager.systems.common.dao.adm.PersonDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.PersonDTO;
import com.manager.systems.common.dto.adm.ReportPersonDTO;
import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.OperationType;

public class PersonDaoImpl implements PersonDao 
{
	private Connection connection;
	
	public PersonDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}

	@Override
	public boolean save(final PersonDTO person) throws Exception 
	{
		CallableStatement statement = null;
		
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_maintenance_person");
		query.append("(");
		query.append("?, "); //01 - _operation INT,
		query.append("?, "); //02 - _person_id_from BIGINT,
		query.append("?, "); //03 - _person_id_to BIGINT,
		query.append("?, "); //04 - _inactive INT,
		query.append("?, "); //05 - _name VARCHAR(100),
		query.append("?, "); //06 - _alias_name VARCHAR(100),
		query.append("?, "); //07 - _object_type VARCHAR(3),
		query.append("?, "); //08 - _person_type VARCHAR(2),
		query.append("?, "); //09 - _cpf_cnpj BIGINT,
		query.append("?, "); //10 - _rg VARCHAR(20),
		query.append("?, "); //11 - _ie VARCHAR(20),
		query.append("?, "); //12 - _email VARCHAR(100),
		query.append("?, "); //13 - _address_zip_code BIGINT,
		query.append("?, "); //14 - _address_zip_code_side VARCHAR(1),
		query.append("?, "); //15 - _address_street VARCHAR(100),
		query.append("?, "); //16 - _address_street_number VARCHAR(20),
		query.append("?, "); //17 - _address_street_complement VARCHAR(50),
		query.append("?, "); //18 - _address_district VARCHAR(100),
		query.append("?, "); //19 - _address_city_ibge INT,
		query.append("?, "); //20 - _address_state_ibge INT,
		query.append("?, "); //21 - _address_country_ibge INT,
		query.append("?, "); //22 - _address_gia VARCHAR(10),
		query.append("?, "); //23 - _access_profile_id INT,
		query.append("?, "); //24 - _user_change BIGINT,
		query.append("?, "); //25 - _change_date DATETIME,
		query.append("?, "); //26 - _access_data_user VARCHAR(100),
		query.append("?, "); //27 - _access_data_password VARCHAR(100),
		query.append("?, "); //28 - _expiration_date DATETIME,
		query.append("?, "); //29 - _last_access_date DATETIME,
		query.append("?, "); //30 - _negative_close BIT
		query.append("?, "); //31 - _bank_account_id BIGINT
		query.append("?, "); //32 - _credit_provider_id BIGINT
		query.append("?, "); //33 - _debit_provider_id BIGINT
		query.append("?, "); //34 - _financial_group_id VARCHAR(7)
		query.append("?, "); //35 - _financial_sub_group_id VARCHAR(7)
		query.append("?, "); //36 - IN _register_type TINYINT
		query.append("?, "); //37 - IN _movement_automatic BIT,
		query.append("?, "); //38 - IN _exit_access_date DATETIME,
		query.append("?, "); //39 - IN _person_type_id INT,
		query.append("?, "); //40 - IN _establishment_type_id INT,
		query.append("?, "); //41 - IN _blocked INT,
		query.append("?, "); //42 - IN _main_company_id BIGINT,
		query.append("?, "); //43 - IN _client_id INT,
		query.append("?  "); //44 - IN financial_cost_center_id INT
		query.append(")");
		query.append("}");
		
		boolean isSaved = false;
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - _operation INT,
			statement.setLong(2, Long.valueOf(person.getPersonId())); //02 - _person_id_from BIGINT,
			statement.setLong(3,  Long.valueOf(person.getPersonId())); //03 - _person_id_to BIGINT,
			statement.setInt(4,  person.getInactive()); //04 - _inactive INT,
			statement.setString(5,  person.getName()); //05 - _name VARCHAR(100),
			statement.setString(6,  person.getAliasName()); //06 - _alias_name VARCHAR(100),
			statement.setString(7,  person.getObjectType()); //07 - _object_type VARCHAR(3),
			statement.setString(8,  person.getPersonType());  //08 - _person_type VARCHAR(2),
			if(StringUtils.isLong(person.getCpfCnpj()))
			{
				statement.setLong(9,  Long.parseLong(person.getCpfCnpj())); //09 - _cpf_cnpj BIGINT,	
			}
			else
			{
				statement.setNull(9,  Types.BIGINT); //09 - _cpf_cnpj BIGINT,
			}			
			statement.setString(10,  person.getRg()); //10 - _rg VARCHAR(20),
			statement.setString(11,  person.getIe()); //11 - _ie VARCHAR(20),
			statement.setString(12,  person.getEmail()); //12 - _email VARCHAR(100),
			if(StringUtils.isLong(person.getAddressZipCode()))
			{
				statement.setLong(13,  Long.parseLong(person.getAddressZipCode())); //13 - _address_zip_code BIGINT,				
			}
			else
			{
				statement.setNull(13,  Types.BIGINT); //13 - _address_zip_code BIGINT,
			}
			statement.setString(14,  person.getAddressZipCodeSide()); //14 - _address_zip_code_side VARCHAR(1),
			statement.setString(15,  person.getAddressStreet()); //15 - _address_street VARCHAR(100),
			statement.setString(16,  person.getAddressNumber()); //16 - _address_street_number VARCHAR(20),
			statement.setString(17,  person.getAddressComplement()); //17 - _address_street_complement VARCHAR(50),
			statement.setString(18,  person.getAddressDistrict()); //18 - _address_district VARCHAR(100),
			if(StringUtils.isLong(person.getAddressCityIbge()))
			{
				statement.setInt(19, Integer.valueOf(person.getAddressCityIbge())); //19 - _address_city_ibge INT,	
			}
			else
			{
				statement.setNull(19, Types.INTEGER); //19 - _address_city_ibge INT,
			}
			if(StringUtils.isLong(person.getAddressStateIbge()))
			{
				statement.setInt(20, Integer.valueOf(person.getAddressStateIbge())); //20 - _address_state_ibge INT,				
			}
			else
			{
				statement.setNull(20, Types.INTEGER); //20 - _address_state_ibge INT,
			}
			if(StringUtils.isLong(person.getAddressCountryIbge()))
			{
				statement.setInt(21, Integer.valueOf(person.getAddressCountryIbge())); //21 - _address_country_ibge INT,				
			}
			else
			{
				statement.setNull(21, Types.INTEGER); //21 - _address_country_ibge INT,				
			}
			statement.setString(22,  person.getAddressGia()); //22 - _address_gia VARCHAR(10),
			statement.setInt(23,  person.getAccessProfileId()); //23 - _access_profile_id INT,
			statement.setLong(24,  person.getUserChange()); //24 - _user_change BIGINT,
			statement.setTimestamp(25, new java.sql.Timestamp(person.getChangeDate().getTimeInMillis())); //25 - _change_date DATETIME,
			statement.setString(26,  person.getAccessDataUser()); //26 - _access_data_user VARCHAR(100),
			statement.setString(27,  person.getAccessDataPassword()); //27 - _access_data_password VARCHAR(100),
			if(person.getExpirationDate()!=null)
			{
				statement.setTimestamp(28, new java.sql.Timestamp(person.getExpirationDate().getTimeInMillis())); //28 - _expiration_date DATETIME,				
			}
			else
			{
				statement.setNull(28, Types.TIMESTAMP); //28 - _expiration_date DATETIME,								
			}
			if(person.getLastAccessDate()!=null)
			{
				statement.setTimestamp(29, new java.sql.Timestamp(person.getLastAccessDate().getTimeInMillis())); //29 - _last_access_date DATETIME,				
			}
			else
			{
				statement.setNull(29, Types.TIMESTAMP); //29 - _last_access_date DATETIME,								
			}
			statement.setBoolean(30, person.isNegativeClose()); //30 - _negative_close BIT
			statement.setLong(31, person.getBankAccount()); //31 - IN _bank_account_id BIGINT
			statement.setLong(32, person.getCreditProvider()); //32 - IN _credit_provider_id BIGINT
			statement.setLong(33, person.getDebitProvider()); //33 - IN _debit_provider_id BIGIN
			statement.setString(34, person.getFinancialGroup()); //34 - IN _financial_group_id INT
			statement.setString(35, person.getFinancialSubGroup()); //35 - IN _financial_sub_group_id INT
			statement.setInt(36, person.getRegisterType()); //36 - IN _register_type TINYINT
			statement.setBoolean(37, person.isMovementAutomatic()); //37 - IN _movement_automatic BIT,
			statement.setNull(38,Types.TIMESTAMP); //38 - IN _exit_access_date DATETIME,
			if(person.getPersonTypeId() != 0)
			{
				statement.setInt(39, person.getPersonTypeId());//39 - IN _person_type_id INT,
			}
			else
			{
				statement.setNull(39, Types.INTEGER);//39 - IN _person_type_id INT,
			}
			if(person.getEstablishmentTypeId() != 0)
			{
				statement.setInt(40, person.getEstablishmentTypeId());//40 - IN _establishment_type_id INT,
			}
			else
			{
				statement.setNull(40, Types.INTEGER);//40 - IN _establishment_type_id INT,
			}
			statement.setInt(41, person.getBlocked()); //41 - _blocked INT,
			statement.setLong(42, person.getMainCompanyId()); //42 - IN _main_company_id BIGINT
			statement.setInt(43, person.getClientId()); //43 - IN _client_id INT,
			statement.setInt(44, person.getFinancialCostCenterId()); //44 - IN financial_cost_center_id INT
			statement.executeUpdate();
			isSaved = true;
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
		
		return isSaved;
	}

	@Override
	public boolean inactive(final PersonDTO person) throws Exception 
	{
		boolean wasInactivated= false;
		
		CallableStatement statement = null;
		
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_maintenance_person");
		query.append("(");
		query.append("?, "); //01 - _operation INT,
		query.append("?, "); //02 - _person_id_from BIGINT,
		query.append("?, "); //03 - _person_id_to BIGINT,
		query.append("?, "); //04 - _inactive INT,
		query.append("?, "); //05 - _name VARCHAR(100),
		query.append("?, "); //06 - _alias_name VARCHAR(100),
		query.append("?, "); //07 - _object_type VARCHAR(3),
		query.append("?, "); //08 - _person_type VARCHAR(2),
		query.append("?, "); //09 - _cpf_cnpj BIGINT,
		query.append("?, "); //10 - _rg VARCHAR(20),
		query.append("?, "); //11 - _ie VARCHAR(20),
		query.append("?, "); //12 - _email VARCHAR(100),
		query.append("?, "); //13 - _address_zip_code BIGINT,
		query.append("?, "); //14 - _address_zip_code_side VARCHAR(1),
		query.append("?, "); //15 - _address_street VARCHAR(100),
		query.append("?, "); //16 - _address_street_number VARCHAR(20),
		query.append("?, "); //17 - _address_street_complement VARCHAR(50),
		query.append("?, "); //18 - _address_district VARCHAR(100),
		query.append("?, "); //19 - _address_city_ibge INT,
		query.append("?, "); //20 - _address_state_ibge INT,
		query.append("?, "); //21 - _address_country_ibge INT,
		query.append("?, "); //22 - _address_gia VARCHAR(10),
		query.append("?, "); //23 - _access_profile_id INT,
		query.append("?, "); //24 - _user_change BIGINT,
		query.append("?, "); //25 - _change_date DATETIME,
		query.append("?, "); //26 - _access_data_user VARCHAR(100),
		query.append("?, "); //27 - _access_data_password VARCHAR(100),
		query.append("?, "); //28 - _expiration_date DATETIME,
		query.append("?, "); //29 - _last_access_date DATETIME,
		query.append("?, "); //30 - _negative_close BIT
		query.append("?, "); //31 - _bank_account_id BIGINT
		query.append("?, "); //32 - _credit_provider_id BIGINT
		query.append("?, "); //33 - _debit_provider_id BIGINT
		query.append("?, "); //34 - _financial_group_id VARCHAR(7)
		query.append("?, "); //35 - _financial_sub_group_id VARCHAR(7)
		query.append("?, "); //36 - IN _register_type TINYINT
		query.append("?, "); //37 - IN _movement_automatic BIT,
		query.append("?, "); //38 - IN _exit_access_date DATETIME
		query.append("?, "); //39 - IN _person_type_id INT,
		query.append("?, "); //40 - IN _establishment_type_id INT,
		query.append("?, "); //41 - IN _blocked INT,
		query.append("?, "); //42 - IN _main_company_id BIGINT,
		query.append("?, "); //43 - IN _client_id INT,
		query.append("?  "); //44 - IN financial_cost_center_id INT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.INACTIVE.getType()); //01 - _operation INT,
			statement.setLong(2, Long.valueOf(person.getPersonId())); //02 - _person_id_from BIGINT,
			statement.setLong(3, Long.valueOf(person.getPersonId())); //03 - _person_id_to BIGINT,
			statement.setBoolean(4, person.isInactive()); //04 - _inactive INT,
			statement.setNull(5, Types.VARCHAR); //05 - _name VARCHAR(100),
			statement.setNull(6, Types.VARCHAR); //06 - _alias_name VARCHAR(100),
			statement.setString(7, person.getObjectType()); //07 - _object_type VARCHAR(3),
			statement.setNull(8, Types.VARCHAR);  //08 - _person_type VARCHAR(2),
			statement.setNull(9, Types.BIGINT); //09 - _cpf_cnpj BIGINT,
			statement.setNull(10, Types.VARCHAR); //10 - _rg VARCHAR(20),
			statement.setNull(11, Types.VARCHAR); //11 - _ie VARCHAR(20),
			statement.setNull(12, Types.VARCHAR); //12 - _email VARCHAR(100),
			statement.setNull(13, Types.BIGINT); //13 - _address_zip_code BIGINT,
			statement.setNull(14, Types.VARCHAR); //14 - _address_zip_code_side VARCHAR(1),
			statement.setNull(15, Types.VARCHAR); //15 - _address_street VARCHAR(100),
			statement.setNull(16, Types.VARCHAR); //16 - _address_street_number VARCHAR(20),
			statement.setNull(17, Types.VARCHAR); //17 - _address_street_complement VARCHAR(50),
			statement.setNull(18, Types.VARCHAR); //18 - _address_district VARCHAR(100),
			statement.setNull(19, Types.INTEGER); //19 - _address_city_ibge INT,
			statement.setNull(20, Types.INTEGER); //20 - _address_state_ibge INT,
			statement.setNull(21, Types.INTEGER); //21 - _address_country_ibge INT,
			statement.setNull(22, Types.VARCHAR); //22 - _address_gia VARCHAR(10),
			statement.setNull(23, Types.INTEGER); //23 - _access_profile_id INT,
			statement.setLong(24, Long.valueOf(person.getUserChange())); //24 - _user_change BIGINT,
			statement.setNull(25, Types.TIMESTAMP); //25 - _change_date DATETIME,
			statement.setNull(26, Types.VARCHAR); //26 - _access_data_user VARCHAR(100),
			statement.setNull(27, Types.VARCHAR); //27 - _access_data_password VARCHAR(100),
			statement.setNull(28, Types.TIMESTAMP); //28 - _expiration_date DATETIME,
			statement.setNull(29, Types.TIMESTAMP); //29 - _last_access_date DATETIME,
			statement.setNull(30, Types.BIT); //30 - _negative_close BIT
			statement.setNull(31, Types.BIGINT); //31 - IN _bank_account_id BIGINT
			statement.setNull(32, Types.BIGINT); //32 - IN _credit_provider_id BIGINT
			statement.setNull(33, Types.BIGINT); //33 - IN _debit_provider_id BIGINT
			statement.setNull(34, Types.INTEGER); //34 - IN IN _financial_group_id INT
			statement.setNull(35, Types.INTEGER); //35 - IN _financial_sub_group_id INT
			statement.setNull(36, Types.INTEGER); //36 - IN _register_type TINYINT
			statement.setNull(37, Types.BIT); //37 - IN _movement_automatic BIT,
			statement.setNull(38,Types.TIMESTAMP); //38 - IN _exit_access_date DATETIME,
			statement.setNull(39, Types.INTEGER);//39 - IN _person_type_id INT,
			statement.setNull(40, Types.INTEGER);//40 - IN _establishment_type_id INT,
			statement.setNull(41, Types.BIT); //41 - _blocked INT,
			statement.setLong(42, person.getMainCompanyId()); //42 - IN _main_company_id BIGINT
			statement.setInt(43, person.getClientId()); //43 - IN _client_id INT,
			statement.setNull(44, Types.INTEGER); //44 - IN financial_cost_center_id INT
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
	public void get(final PersonDTO person) throws Exception 
	{
		CallableStatement statement = null;
		
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_maintenance_person");
		query.append("(");
		query.append("?, "); //01 - _operation INT,
		query.append("?, "); //02 - _person_id_from BIGINT,
		query.append("?, "); //03 - _person_id_to BIGINT,
		query.append("?, "); //04 - _inactive INT,
		query.append("?, "); //05 - _name VARCHAR(100),
		query.append("?, "); //06 - _alias_name VARCHAR(100),
		query.append("?, "); //07 - _object_type VARCHAR(3),
		query.append("?, "); //08 - _person_type VARCHAR(2),
		query.append("?, "); //09 - _cpf_cnpj BIGINT,
		query.append("?, "); //10 - _rg VARCHAR(20),
		query.append("?, "); //11 - _ie VARCHAR(20),
		query.append("?, "); //12 - _email VARCHAR(100),
		query.append("?, "); //13 - _address_zip_code BIGINT,
		query.append("?, "); //14 - _address_zip_code_side VARCHAR(1),
		query.append("?, "); //15 - _address_street VARCHAR(100),
		query.append("?, "); //16 - _address_street_number VARCHAR(20),
		query.append("?, "); //17 - _address_street_complement VARCHAR(50),
		query.append("?, "); //18 - _address_district VARCHAR(100),
		query.append("?, "); //19 - _address_city_ibge INT,
		query.append("?, "); //20 - _address_state_ibge INT,
		query.append("?, "); //21 - _address_country_ibge INT,
		query.append("?, "); //22 - _address_gia VARCHAR(10),
		query.append("?, "); //23 - _access_profile_id INT,
		query.append("?, "); //24 - _user_change BIGINT,
		query.append("?, "); //25 - _change_date DATETIME,
		query.append("?, "); //26 - _access_data_user VARCHAR(100),
		query.append("?, "); //27 - _access_data_password VARCHAR(100),
		query.append("?, "); //28 - _expiration_date DATETIME,
		query.append("?, "); //29 - _last_access_date DATETIME,
		query.append("?, "); //30 - _negative_close BIT
		query.append("?, "); //31 - _bank_account_id BIGINT
		query.append("?, "); //32 - _credit_provider_id BIGINT
		query.append("?, "); //33 - _debit_provider_id BIGINT
		query.append("?, "); //34 - _financial_group_id INT
		query.append("?, "); //35 - _financial_sub_group_id INT
		query.append("?, "); //36 - _register_type TINYINT
		query.append("?, "); //37 - IN _movement_automatic BIT,
		query.append("?, "); //38 - IN _exit_access_date DATETIME,
		query.append("?, "); //39 - IN _person_type_id INT,
		query.append("?, "); //40 - IN _establishment_type_id INT,
		query.append("?, "); //41 - IN _blocked INT,
		query.append("?, "); //42 - IN _main_company_id BIGINT,
		query.append("?, "); //43 - IN _client_id INT,
		query.append("?  "); //44 - IN financial_cost_center_id INT
		query.append(")");
		query.append("}");
		
				
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); //01 - _operation INT,
			statement.setLong(2, Long.valueOf(person.getPersonId())); //02 - _person_id_from BIGINT,
			statement.setLong(3, Long.valueOf(person.getPersonId())); //03 - _person_id_to BIGINT,
			statement.setNull(4, Types.INTEGER); //04 - _inactive INT,
			statement.setNull(5, Types.VARCHAR); //05 - _name VARCHAR(100),
			statement.setNull(6, Types.VARCHAR); //06 - _alias_name VARCHAR(100),
			statement.setString(7, person.getObjectType()); //07 - _object_type VARCHAR(3),
			statement.setNull(8, Types.VARCHAR);  //08 - _person_type VARCHAR(2),
			statement.setNull(9, Types.BIGINT); //09 - _cpf_cnpj BIGINT,
			statement.setNull(10, Types.VARCHAR); //10 - _rg VARCHAR(20),
			statement.setNull(11, Types.VARCHAR); //11 - _ie VARCHAR(20),
			statement.setNull(12, Types.VARCHAR); //12 - _email VARCHAR(100),
			statement.setNull(13, Types.BIGINT); //13 - _address_zip_code BIGINT,
			statement.setNull(14, Types.VARCHAR); //14 - _address_zip_code_side VARCHAR(1),
			statement.setNull(15, Types.VARCHAR); //15 - _address_street VARCHAR(100),
			statement.setNull(16, Types.VARCHAR); //16 - _address_street_number VARCHAR(20),
			statement.setNull(17, Types.VARCHAR); //17 - _address_street_complement VARCHAR(50),
			statement.setNull(18, Types.VARCHAR); //18 - _address_district VARCHAR(100),
			statement.setNull(19, Types.INTEGER); //19 - _address_city_ibge INT,
			statement.setNull(20, Types.INTEGER); //20 - _address_state_ibge INT,
			statement.setNull(21, Types.INTEGER); //21 - _address_country_ibge INT,
			statement.setNull(22, Types.VARCHAR); //22 - _address_gia VARCHAR(10),
			statement.setNull(23, Types.INTEGER); //23 - _access_profile_id INT,
			statement.setLong(24, person.getUserChange()); //24 - _user_change BIGINT,
			statement.setNull(25, Types.TIMESTAMP); //25 - _change_date DATETIME,
			statement.setNull(26, Types.VARCHAR); //26 - _access_data_user VARCHAR(100),
			statement.setNull(27, Types.VARCHAR); //27 - _access_data_password VARCHAR(100),
			statement.setNull(28, Types.TIMESTAMP); //28 - _expiration_date DATETIME,
			statement.setNull(29, Types.TIMESTAMP); //29 - _last_access_date DATETIME,
			statement.setNull(30, Types.BIT); //30 - _negative_close BIT
			statement.setNull(31, Types.BIGINT); //31 - IN _bank_account_id BIGINT
			statement.setNull(32, Types.BIGINT); //32 - IN _credit_provider_id BIGINT
			statement.setNull(33, Types.BIGINT); //33 - IN _debit_provider_id BIGINT
			statement.setNull(34, Types.INTEGER); //34 - IN IN _financial_group_id INT
			statement.setNull(35, Types.INTEGER); //35 - IN _financial_sub_group_id INT
			statement.setNull(36, Types.INTEGER); //36 - IN _register_type TINYINT
			statement.setNull(37, Types.BIT); //37 - IN _movement_automatic BIT
			statement.setNull(38, Types.TIMESTAMP); //38 - IN _exit_access_date DATETIME,
			statement.setNull(39, Types.INTEGER);//39 - IN _person_type_id INT,
			statement.setNull(40, Types.INTEGER);//40 - IN _establishment_type_id INT,
			statement.setNull(41, Types.BIT); //41 - IN _blocked INT,
			statement.setLong(42, person.getMainCompanyId()); //42 - IN _main_company_id BIGINT
			statement.setInt(43, person.getClientId()); //43 - IN _client_id INT,
			statement.setNull(44, Types.INTEGER); //44 - IN financial_cost_center_id INT
	        resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	person.setPersonId(resultSet.getString(DatabaseConstants.COLUMN_ID));
	        	person.setName(resultSet.getString(DatabaseConstants.COLUMN_NAME));
	        	person.setAliasName(resultSet.getString(DatabaseConstants.COLUMN_ALIAS_NAME));
	        	person.setPersonType(resultSet.getString(DatabaseConstants.COLUMN_PERSON_TYPE));
	        	person.setObjectType(resultSet.getString(DatabaseConstants.COLUMN_OBJECT_TYPE));
	        	person.setAccessProfileId(resultSet.getInt(DatabaseConstants.COLUMN_ACCESS_PROFILE_ID));
	        	person.setCpfCnpj(resultSet.getString(DatabaseConstants.COLUMN_CPF_CNPJ));
	        	person.setRg(resultSet.getString(DatabaseConstants.COLUMN_RG));
	        	person.setIe(resultSet.getString(DatabaseConstants.COLUMN_IE));
	        	person.setBankAccount(resultSet.getLong(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID));
	        	person.setBankAccountDescription(resultSet.getString(DatabaseConstants.COLUMN_BANK_ACCOUNT_DESCRIPTION));
	        	person.setCreditProvider(resultSet.getLong(DatabaseConstants.COLUMN_CREDIT_PROVIDER_ID));
	        	person.setCreditProviderDescription(resultSet.getString(DatabaseConstants.COLUMN_CREDIT_PROVIDER_DESCRIPTION));
	        	person.setDebitProvider(resultSet.getLong(DatabaseConstants.COLUMN_DEBIT_PROVIDER_ID));
	        	person.setDebitProviderDescription(resultSet.getString(DatabaseConstants.COLUMN_DEBIT_PROVIDER_DESCRIPTION));
	        	person.setFinancialGroup(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_ID));
	        	person.setFinancialGroupDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_DESCRIPTION));
	        	person.setFinancialSubGroup(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_ID));	        	
	        	person.setFinancialSubGroupDescription(resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_DESCRIPTION));
	        	person.setPersonTypeId(resultSet.getInt(DatabaseConstants.COLUMN_PERSON_TYPE_ID));
	        	person.setEstablishmentTypeId(resultSet.getInt(DatabaseConstants.COLUMN_ESTABLISHMENT_TYPE_ID));
	        	String zipCode = resultSet.getString(DatabaseConstants.COLUMN_ADDRESS_ZIP_CODE);
	        	if(StringUtils.isLong(zipCode))
	        	{
	        		zipCode = StringUtils.padLeft(zipCode, 8, ConstantDataManager.ZERO_STRING);
	        	}
	        	else
	        	{
	        		zipCode = ConstantDataManager.BLANK;
	        	}
	        	person.setAddressZipCode(zipCode);
	        	person.setAddressZipCodeSide(resultSet.getString(DatabaseConstants.COLUMN_ADDRESS_ZIP_CODE_SIDE));
	        	person.setAddressStreet(resultSet.getString(DatabaseConstants.COLUMN_ADDRESS_STREET));
	        	person.setAddressDistrict(resultSet.getString(DatabaseConstants.COLUMN_ADDRESS_DISTRICT));
	        	person.setAddressCityIbge(resultSet.getString(DatabaseConstants.COLUMN_ADDRESS_CITY_IBGE));
	        	person.setAddressCityDescription(resultSet.getString(DatabaseConstants.COLUMN_ADDRESS_CITY_DESCRIPTION));
	        	person.setAddressStateIbge(resultSet.getString(DatabaseConstants.COLUMN_ADDRESS_STATE_IBGE));
	        	person.setAddressStateDescription(resultSet.getString(DatabaseConstants.COLUMN_ADDRESS_STATE_DESCRIPTION));
	        	person.setAddressCountryIbge(resultSet.getString(DatabaseConstants.COLUMN_ADDRESS_COUNTRY_IBGE));
	        	person.setAddressGia(resultSet.getString(DatabaseConstants.COLUMN_ADDRESS_GIA));
	        	person.setAddressNumber(resultSet.getString(DatabaseConstants.COLUMN_ADDRESS_STREET_NUMBER));
	        	person.setAddressComplement(resultSet.getString(DatabaseConstants.COLUMN_ADDRESS_STREET_COMPLEMENT));
	        	person.setEmail(resultSet.getString(DatabaseConstants.COLUMN_EMAIL));
	        	person.setSite(resultSet.getString(DatabaseConstants.COLUMN_SITE));
	        	person.setAccessDataUser(resultSet.getString(DatabaseConstants.COLUMN_ACCESS_DATA_USER));
	        	person.setAccessDataPassword(resultSet.getString(DatabaseConstants.COLUMN_ACCESS_DATA_PASSWORD));
	        	person.setNegativeClose(resultSet.getBoolean(DatabaseConstants.COLUMN_COMPANY_NEGATIVE_CLOSE));
	        	person.setMovementAutomatic(resultSet.getBoolean(DatabaseConstants.COLUMN_COMPANY_PROCESS_MOVEMENT_AUTOMATIC));
	        	final Timestamp expirationDate =  resultSet.getTimestamp(DatabaseConstants.COLUMN_EXPIRATION_DATE);
	        	if(expirationDate!=null)
	        	{
	        		final Calendar calendar = Calendar.getInstance();
	        		calendar.setTimeInMillis(expirationDate.getTime());
	        		person.setExpirationDate(calendar);
	        	}
	        	final Timestamp lastAcessDate =  resultSet.getTimestamp(DatabaseConstants.COLUMN_LAST_ACCESS_DATE);
	        	if(lastAcessDate!=null)
	        	{
	        		final Calendar calendar = Calendar.getInstance();
	        		calendar.setTimeInMillis(lastAcessDate.getTime());
	        		person.setLastAccessDate(calendar);
	        	}
	        	person.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	person.setUserCreate(resultSet.getLong(DatabaseConstants.COLUMN_USER_CREATION));
	        	final Timestamp creationDate =  resultSet.getTimestamp(DatabaseConstants.COLUMN_CREATION_DATE);
	        	if(creationDate!=null)
	        	{
	        		final Calendar calendar = Calendar.getInstance();
	        		calendar.setTimeInMillis(creationDate.getTime());
	        		person.setCreationDate(calendar);
	        	}
	        	person.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	person.setUserChangeName(resultSet.getString(DatabaseConstants.COLUMN_USER_CHANGE_NAME));
	        	final Timestamp changeDate =  resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE);
	        	if(changeDate!=null)
	        	{
	        		final Calendar calendar = Calendar.getInstance();
	        		calendar.setTimeInMillis(changeDate.getTime());
	        		person.setChangeDate(calendar);
	        	}
	        	person.setRegisterType(resultSet.getInt(DatabaseConstants.COLUMN_REGISTER_TYPE));
	        	person.setBlocked(resultSet.getBoolean(DatabaseConstants.COLUMN_BLOCKED));
	        	person.setMainCompanyId(resultSet.getInt(DatabaseConstants.COLUMN_MAIN_COMPANY_ID));
	        	person.setClientId(resultSet.getInt(DatabaseConstants.COLUMN_CLIENT_ID));
	        	person.setCashClosingMaxDiscount(resultSet.getDouble(DatabaseConstants.COLUMN_CASH_CLOSING_MAX_DISCOUNT));
	        	person.setFinancialCostCenterId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_COST_CENTER_ID));
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
	public void getAll(final ReportPersonDTO reportPerson) throws Exception 
	{	
		CallableStatement statement = null;
		
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_maintenance_person");
		query.append("(");
		query.append("?, "); //01 - _operation INT,
		query.append("?, "); //02 - _person_id_from BIGINT,
		query.append("?, "); //03 - _person_id_to BIGINT,
		query.append("?, "); //04 - _inactive INT,
		query.append("?, "); //05 - _name VARCHAR(100),
		query.append("?, "); //06 - _alias_name VARCHAR(100),
		query.append("?, "); //07 - _object_type VARCHAR(3),
		query.append("?, "); //08 - _person_type VARCHAR(2),
		query.append("?, "); //09 - _cpf_cnpj BIGINT,
		query.append("?, "); //10 - _rg VARCHAR(20),
		query.append("?, "); //11 - _ie VARCHAR(20),
		query.append("?, "); //12 - _email VARCHAR(100),
		query.append("?, "); //13 - _address_zip_code BIGINT,
		query.append("?, "); //14 - _address_zip_code_side VARCHAR(1),
		query.append("?, "); //15 - _address_street VARCHAR(100),
		query.append("?, "); //16 - _address_street_number VARCHAR(20),
		query.append("?, "); //17 - _address_street_complement VARCHAR(50),
		query.append("?, "); //18 - _address_district VARCHAR(100),
		query.append("?, "); //19 - _address_city_ibge INT,
		query.append("?, "); //20 - _address_state_ibge INT,
		query.append("?, "); //21 - _address_country_ibge INT,
		query.append("?, "); //22 - _address_gia VARCHAR(10),
		query.append("?, "); //23 - _access_profile_id INT,
		query.append("?, "); //24 - _user_change BIGINT,
		query.append("?, "); //25 - _change_date DATETIME,
		query.append("?, "); //26 - _access_data_user VARCHAR(100),
		query.append("?, "); //27 - _access_data_password VARCHAR(100),
		query.append("?, "); //28 - _expiration_date DATETIME,
		query.append("?, "); //29 - _last_access_date DATETIME,
		query.append("?, "); //30 - _negative_close BIT
		query.append("?, "); //31 - _bank_account_id BIGINT
		query.append("?, "); //32 - _credit_provider_id BIGINT
		query.append("?, "); //33 - _debit_provider_id BIGINT
		query.append("?, "); //34 - _financial_group_id INT
		query.append("?, "); //35 - _financial_sub_group_id INT
		query.append("?, "); //36 - _register_type TINYINT
		query.append("?, "); //37 - IN _movement_automatic BIT,
		query.append("?, "); //38 - IN _exit_access_date DATETIME,
		query.append("?, "); //39 - IN _person_type_id INT,
		query.append("?, "); //40 - IN _establishment_type_id INT,
		query.append("?, "); //41 - IN _blocked INT,
		query.append("?, "); //42 - IN _main_company_id BIGINT,
		query.append("?, "); //43 - IN _client_id INT,
		query.append("?  "); //44 - IN financial_cost_center_id INT
		query.append(")");
		query.append("}");
		
				
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - _operation INT,
			statement.setLong(2, Long.valueOf(reportPerson.getPersonIdFrom())); //02 - _person_id_from BIGINT,
			statement.setLong(3, Long.valueOf(reportPerson.getPersonIdTo())); //03 - _person_id_to BIGINT,
			statement.setInt(4, reportPerson.getInactive()); //04 - _inactive INT,
			statement.setString(5, reportPerson.getDescription()); //05 - _name VARCHAR(100),
			statement.setNull(6, Types.VARCHAR); //06 - _alias_name VARCHAR(100),
			statement.setString(7, reportPerson.getObjectType()); //07 - _object_type VARCHAR(3),
			statement.setString(8, reportPerson.getPersonType());  //08 - _person_type VARCHAR(2),
			statement.setNull(9, Types.BIGINT); //09 - _cpf_cnpj BIGINT,
			statement.setNull(10, Types.VARCHAR); //10 - _rg VARCHAR(20),
			statement.setNull(11, Types.VARCHAR); //11 - _ie VARCHAR(20),
			statement.setNull(12, Types.VARCHAR); //12 - _email VARCHAR(100),
			statement.setNull(13, Types.BIGINT); //13 - _address_zip_code BIGINT,
			statement.setNull(14, Types.VARCHAR); //14 - _address_zip_code_side VARCHAR(1),
			statement.setNull(15, Types.VARCHAR); //15 - _address_street VARCHAR(100),
			statement.setNull(16, Types.VARCHAR); //16 - _address_street_number VARCHAR(20),
			statement.setNull(17, Types.VARCHAR); //17 - _address_street_complement VARCHAR(50),
			statement.setNull(18, Types.VARCHAR); //18 - _address_district VARCHAR(100),
			statement.setNull(19, Types.INTEGER); //19 - _address_city_ibge INT,
			statement.setNull(20, Types.INTEGER); //20 - _address_state_ibge INT,
			statement.setNull(21, Types.INTEGER); //21 - _address_country_ibge INT,
			statement.setNull(22, Types.VARCHAR); //22 - _address_gia VARCHAR(10),
			statement.setNull(23, Types.INTEGER); //23 - _access_profile_id INT,
			statement.setLong(24, reportPerson.getUserOperation()); //24 - _user_change BIGINT,
			statement.setNull(25, Types.TIMESTAMP); //25 - _change_date DATETIME,
			statement.setNull(26, Types.VARCHAR); //26 - _access_data_user VARCHAR(100),
			statement.setNull(27, Types.VARCHAR); //27 - _access_data_password VARCHAR(100),
			statement.setNull(28, Types.TIMESTAMP); //28 - _expiration_date DATETIME,
			statement.setNull(29, Types.TIMESTAMP); //29 - _last_access_date DATETIME,
			statement.setNull(30, Types.BIT); //30 - _negative_close BIT
			statement.setNull(31, Types.BIGINT); //31 - IN _bank_account_id BIGINT
			statement.setNull(32, Types.BIGINT); //32 - IN _credit_provider_id BIGINT
			statement.setNull(33, Types.BIGINT); //33 - IN _debit_provider_id BIGINT
			statement.setNull(34, Types.INTEGER); //34 - IN IN _financial_group_id INT
			statement.setNull(35, Types.INTEGER); //35 - IN _financial_sub_group_id INT		
			statement.setNull(36, Types.INTEGER); //36 - IN _register_type TINYINT
			statement.setNull(37, Types.BIT); //37 - IN _movement_automatic BIT,
			statement.setNull(38,Types.TIMESTAMP); //38 - IN _exit_access_date DATETIME,
			if(0 !=  reportPerson.getFilterPersonTypeId()) {
				statement.setInt(39, reportPerson.getFilterPersonTypeId());//39 - IN _person_type_id INT,
			}else {
				statement.setNull(39, Types.INTEGER);//39 - IN _person_type_id INT,
			}
			if(0 !=  reportPerson.getFilterEstablishmentTypeId()) {
				statement.setInt(40, reportPerson.getFilterEstablishmentTypeId());//40 - IN _establishment_type_id INT
			}else {
				statement.setNull(40, Types.INTEGER);//40 - IN _establishment_type_id INT,
			}
			statement.setNull(41, Types.BIT); //41 - _blocked INT,
			statement.setNull(42, Types.BIGINT); //42 - IN _main_company_id BIGINT
			statement.setInt(43, reportPerson.getClientId()); //43 - IN _client_id INT,
			statement.setNull(44, Types.INTEGER); //44 - IN financial_cost_center_id INT
	        resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final PersonDTO person = new PersonDTO();
	        	person.setPersonId(resultSet.getString(DatabaseConstants.COLUMN_ID));
	        	person.setName(resultSet.getString(DatabaseConstants.COLUMN_NAME));
	        	person.setPersonType(resultSet.getString(DatabaseConstants.COLUMN_PERSON_TYPE));
	        	person.setAliasName(resultSet.getString(DatabaseConstants.COLUMN_ALIAS_NAME));
	        	String cpfCnpj = resultSet.getString(DatabaseConstants.COLUMN_CPF_CNPJ);
	        	if(!StringUtils.isLong(cpfCnpj))
	        	{
	        		cpfCnpj = ConstantDataManager.TRACO;
	        	}
	        	person.setCpfCnpj(cpfCnpj);
	        	String email = resultSet.getString(DatabaseConstants.COLUMN_EMAIL);
	        	if(StringUtils.isNull(email))
	        	{
	        		email = ConstantDataManager.TRACO;
	        	}	        		
	        	person.setEmail(email);
	        	person.setBankAccount(resultSet.getLong(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID));
	        	person.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	person.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	final Timestamp changeDate =  resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE);
	        	final Timestamp userlastAccessDate = resultSet.getTimestamp(DatabaseConstants.COLUMN_LAST_ACCESS_DATE);
	        	final Timestamp userLastAccessFinish= resultSet.getTimestamp(DatabaseConstants.COLUMN_LAST_ACCESS_FINISH_DATE);

	        	if(changeDate!=null)
	        	{
	        		final Calendar calendar = Calendar.getInstance();
	        		calendar.setTimeInMillis(changeDate.getTime());
	        		person.setChangeDate(calendar);
	        	}	
	        	if(userlastAccessDate!=null)
	        	{
	        		final Calendar calendar = Calendar.getInstance();
	        		calendar.setTimeInMillis(userlastAccessDate.getTime());
	        		person.setLastAccessDate(calendar);
	        	}	
	        	if(userLastAccessFinish!=null)
	        	{
	        		final Calendar calendar = Calendar.getInstance();
	        		calendar.setTimeInMillis(userLastAccessFinish.getTime());
	        		person.setUserExitAcessDate(calendar);
	        	}	
	        	
	        	person.setPersonTypeId(resultSet.getInt(DatabaseConstants.COLUMN_PERSON_TYPE_ID));
	        	person.setPersonTypeDescription(resultSet.getString(DatabaseConstants.COLUMN_PERSON_TYPE_DESCRIPTION));
	        	person.setEstablishmentTypeId(resultSet.getInt(DatabaseConstants.COLUMN_ESTABLISHMENT_TYPE_ID));
	        	person.setEstablishmentTypeDescription(resultSet.getString(DatabaseConstants.COLUMN_ESTABLISHMENT_TYPE_DESCRIPTION));
	        	person.setCountProducts(resultSet.getInt(DatabaseConstants.COLUMN_COUNT_PRODUCT));
	        	person.setQtdeCompanys(resultSet.getInt(DatabaseConstants.COLUMN_COUNT_COMPANY));
	        	reportPerson.addItem(person);
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
	public List<Combobox> getAllAutocomplete(final ReportPersonDTO reportPerson) throws Exception
	{
		final List<Combobox> itens = new ArrayList<Combobox>();
		
		CallableStatement statement = null;
		
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_maintenance_person");
		query.append("(");
		query.append("?, "); //01 - _operation INT,
		query.append("?, "); //02 - _person_id_from BIGINT,
		query.append("?, "); //03 - _person_id_to BIGINT,
		query.append("?, "); //04 - _inactive INT,
		query.append("?, "); //05 - _name VARCHAR(100),
		query.append("?, "); //06 - _alias_name VARCHAR(100),
		query.append("?, "); //07 - _object_type VARCHAR(3),
		query.append("?, "); //08 - _person_type VARCHAR(2),
		query.append("?, "); //09 - _cpf_cnpj BIGINT,
		query.append("?, "); //10 - _rg VARCHAR(20),
		query.append("?, "); //11 - _ie VARCHAR(20),
		query.append("?, "); //12 - _email VARCHAR(100),
		query.append("?, "); //13 - _address_zip_code BIGINT,
		query.append("?, "); //14 - _address_zip_code_side VARCHAR(1),
		query.append("?, "); //15 - _address_street VARCHAR(100),
		query.append("?, "); //16 - _address_street_number VARCHAR(20),
		query.append("?, "); //17 - _address_street_complement VARCHAR(50),
		query.append("?, "); //18 - _address_district VARCHAR(100),
		query.append("?, "); //19 - _address_city_ibge INT,
		query.append("?, "); //20 - _address_state_ibge INT,
		query.append("?, "); //21 - _address_country_ibge INT,
		query.append("?, "); //22 - _address_gia VARCHAR(10),
		query.append("?, "); //23 - _access_profile_id INT,
		query.append("?, "); //24 - _user_change BIGINT,
		query.append("?, "); //25 - _change_date DATETIME,
		query.append("?, "); //26 - _access_data_user VARCHAR(100),
		query.append("?, "); //27 - _access_data_password VARCHAR(100),
		query.append("?, "); //28 - _expiration_date DATETIME,
		query.append("?, "); //29 - _last_access_date DATETIME,
		query.append("?, "); //30 - _negative_close BIT
		query.append("?, "); //31 - _bank_account_id BIGINT
		query.append("?, "); //32 - _credit_provider_id BIGINT
		query.append("?, "); //33 - _debit_provider_id BIGINT
		query.append("?, "); //34 - _financial_group_id INT
		query.append("?, "); //35 - _financial_sub_group_id INT
		query.append("?, "); //36 - _register_type TINYINT
		query.append("?, "); //37 - IN _movement_automatic BIT,
		query.append("?, "); //38 - IN _exit_access_date DATETIME,
		query.append("?, "); //39 - IN _person_type_id INT,
		query.append("?, "); //40 - IN _establishment_type_id INT,
		query.append("?, "); //41 - IN _blocked INT,
		query.append("?, "); //42 - IN _main_company_id BIGINT,
		query.append("?, "); //43 - IN _client_id INT,
		query.append("?  "); //44 - IN financial_cost_center_id INT
		query.append(")");
		query.append("}");
		
				
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			if(ConstantDataManager.ORIGIN_BANK_ACCOUNT_STATEMENT.equalsIgnoreCase(reportPerson.getOrigin())) {
				statement.setInt(1, 8); //01 - _operation INT,				
			} else {
				statement.setInt(1, OperationType.AUTOCOMPLETE.getType()); //01 - _operation INT,				
			}
			statement.setLong(2, Long.valueOf(reportPerson.getPersonIdFrom())); //02 - _person_id_from BIGINT,
			statement.setLong(3, Long.valueOf(reportPerson.getPersonIdTo())); //03 - _person_id_to BIGINT,
			statement.setInt(4, reportPerson.getInactive()); //04 - _inactive INT,
			statement.setString(5, reportPerson.getDescription()); //05 - _name VARCHAR(100),
			statement.setNull(6, Types.VARCHAR); //06 - _alias_name VARCHAR(100),
			statement.setString(7, reportPerson.getObjectType()); //07 - _object_type VARCHAR(3),
			
			if(reportPerson.getPersonType() == null) {
				statement.setNull(8, Types.VARCHAR);  //08 - _person_type VARCHAR(2),
			}else {
				statement.setString(8, reportPerson.getPersonType());  //08 - _person_type VARCHAR(2),
			}
			
			statement.setString(8, reportPerson.getPersonType());  //08 - _person_type VARCHAR(2),
			statement.setNull(9, Types.BIGINT); //09 - _cpf_cnpj BIGINT,
			statement.setNull(10, Types.VARCHAR); //10 - _rg VARCHAR(20),
			statement.setNull(11, Types.VARCHAR); //11 - _ie VARCHAR(20),
			statement.setNull(12, Types.VARCHAR); //12 - _email VARCHAR(100),
			statement.setNull(13, Types.BIGINT); //13 - _address_zip_code BIGINT,
			statement.setNull(14, Types.VARCHAR); //14 - _address_zip_code_side VARCHAR(1),
			statement.setNull(15, Types.VARCHAR); //15 - _address_street VARCHAR(100),
			statement.setNull(16, Types.VARCHAR); //16 - _address_street_number VARCHAR(20),
			statement.setNull(17, Types.VARCHAR); //17 - _address_street_complement VARCHAR(50),
			statement.setNull(18, Types.VARCHAR); //18 - _address_district VARCHAR(100),
			statement.setNull(19, Types.INTEGER); //19 - _address_city_ibge INT,
			statement.setNull(20, Types.INTEGER); //20 - _address_state_ibge INT,
			statement.setNull(21, Types.INTEGER); //21 - _address_country_ibge INT,
			statement.setNull(22, Types.VARCHAR); //22 - _address_gia VARCHAR(10),
			statement.setNull(23, Types.INTEGER); //23 - _access_profile_id INT,
			statement.setLong(24, reportPerson.getUserOperation()); //24 - _user_change BIGINT,
			statement.setNull(25, Types.TIMESTAMP); //25 - _change_date DATETIME,
			statement.setNull(26, Types.VARCHAR); //26 - _access_data_user VARCHAR(100),
			statement.setNull(27, Types.VARCHAR); //27 - _access_data_password VARCHAR(100),
			statement.setNull(28, Types.TIMESTAMP); //28 - _expiration_date DATETIME,
			statement.setNull(29, Types.TIMESTAMP); //29 - _last_access_date DATETIME,
			statement.setNull(30, Types.BIT); //30 - _negative_close BIT
			statement.setNull(31, Types.BIGINT); //31 - IN _bank_account_id BIGINT
			statement.setNull(32, Types.BIGINT); //32 - IN _credit_provider_id BIGINT
			statement.setNull(33, Types.BIGINT); //33 - IN _debit_provider_id BIGINT			
			statement.setNull(34, Types.INTEGER); //34 - IN IN _financial_group_id INT
			statement.setNull(35, Types.INTEGER); //35 - IN _financial_sub_group_id INT
			statement.setNull(36, Types.INTEGER); //36 - IN _register_type TINYINT
			statement.setNull(37, Types.BIT); //37 - IN _movement_automatic BIT,
			statement.setNull(38,Types.TIMESTAMP); //38 - IN _exit_access_date DATETIME,
			statement.setNull(39, Types.INTEGER);//39 - IN _person_type_id INT,
			statement.setNull(40, Types.INTEGER);//40 - IN _establishment_type_id INT,
			statement.setNull(41, Types.BIT); //41 - _blocked INT,
			statement.setNull(42, Types.BIGINT); //42 - IN _main_company_id BIGINT
			statement.setNull(43, Types.INTEGER); //43 - IN _client_id INT,
			statement.setNull(44, Types.INTEGER); //44 - IN financial_cost_center_id INT
	        resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	itens.add(new Combobox(
	        			resultSet.getString(DatabaseConstants.COLUMN_ID), 
	        			resultSet.getString(DatabaseConstants.COLUMN_NAME), 
	        			resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_ID), 
	        			resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_ID),
	        			resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_GROUP_DESCRIPTION),
	        			resultSet.getString(DatabaseConstants.COLUMN_FINANCIAL_SUB_GROUP_DESCRIPTION)
	        			));
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
	public List<Combobox> getAllCombobox(final ReportPersonDTO reportPerson) throws Exception
	{
		final List<Combobox> itens = new ArrayList<Combobox>();
		
		CallableStatement statement = null;
		
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_maintenance_person");
		query.append("(");
		query.append("?, "); //01 - _operation INT,
		query.append("?, "); //02 - _person_id_from BIGINT,
		query.append("?, "); //03 - _person_id_to BIGINT,
		query.append("?, "); //04 - _inactive INT,
		query.append("?, "); //05 - _name VARCHAR(100),
		query.append("?, "); //06 - _alias_name VARCHAR(100),
		query.append("?, "); //07 - _object_type VARCHAR(3),
		query.append("?, "); //08 - _person_type VARCHAR(2),
		query.append("?, "); //09 - _cpf_cnpj BIGINT,
		query.append("?, "); //10 - _rg VARCHAR(20),
		query.append("?, "); //11 - _ie VARCHAR(20),
		query.append("?, "); //12 - _email VARCHAR(100),
		query.append("?, "); //13 - _address_zip_code BIGINT,
		query.append("?, "); //14 - _address_zip_code_side VARCHAR(1),
		query.append("?, "); //15 - _address_street VARCHAR(100),
		query.append("?, "); //16 - _address_street_number VARCHAR(20),
		query.append("?, "); //17 - _address_street_complement VARCHAR(50),
		query.append("?, "); //18 - _address_district VARCHAR(100),
		query.append("?, "); //19 - _address_city_ibge INT,
		query.append("?, "); //20 - _address_state_ibge INT,
		query.append("?, "); //21 - _address_country_ibge INT,
		query.append("?, "); //22 - _address_gia VARCHAR(10),
		query.append("?, "); //23 - _access_profile_id INT,
		query.append("?, "); //24 - _user_change BIGINT,
		query.append("?, "); //25 - _change_date DATETIME,
		query.append("?, "); //26 - _access_data_user VARCHAR(100),
		query.append("?, "); //27 - _access_data_password VARCHAR(100),
		query.append("?, "); //28 - _expiration_date DATETIME,
		query.append("?, "); //29 - _last_access_date DATETIME,
		query.append("?, "); //30 - _negative_close BIT
		query.append("?, "); //31 - _bank_account_id BIGINT
		query.append("?, "); //32 - _credit_provider_id BIGINT
		query.append("?, "); //33 - _debit_provider_id BIGINT
		query.append("?, "); //34 - _financial_group_id INT
		query.append("?, "); //35 - _financial_sub_group_id INT
		query.append("?, "); //36 - _register_type TINYINT
		query.append("?, "); //37 - IN _movement_automatic BIT,
		query.append("?, "); //38 - IN _exit_access_date DATETIME,
		query.append("?, "); //39 - IN _person_type_id INT,
		query.append("?, "); //40 - IN _establishment_type_id INT,
		query.append("?, "); //41 - IN _blocked INT,
		query.append("?, "); //42 - IN _main_company_id BIGINT,
		query.append("?, "); //43 - IN _client_id INT,
		query.append("?  "); //44 - IN financial_cost_center_id INT
		query.append(")");
		query.append("}");
		
				
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL_COMBOBOX.getType()); //01 - _operation INT,
			statement.setLong(2, Long.valueOf(reportPerson.getPersonIdFrom())); //02 - _person_id_from BIGINT,
			statement.setLong(3, Long.valueOf(reportPerson.getPersonIdTo())); //03 - _person_id_to BIGINT,
			statement.setInt(4, reportPerson.getInactive()); //04 - _inactive INT,
			statement.setString(5, reportPerson.getDescription()); //05 - _name VARCHAR(100),
			statement.setNull(6, Types.VARCHAR); //06 - _alias_name VARCHAR(100),
			statement.setString(7, reportPerson.getObjectType()); //07 - _object_type VARCHAR(3),
			statement.setString(8, reportPerson.getPersonType());  //08 - _person_type VARCHAR(2),
			statement.setNull(9, Types.BIGINT); //09 - _cpf_cnpj BIGINT,
			statement.setNull(10, Types.VARCHAR); //10 - _rg VARCHAR(20),
			statement.setNull(11, Types.VARCHAR); //11 - _ie VARCHAR(20),
			statement.setNull(12, Types.VARCHAR); //12 - _email VARCHAR(100),
			statement.setNull(13, Types.BIGINT); //13 - _address_zip_code BIGINT,
			statement.setNull(14, Types.VARCHAR); //14 - _address_zip_code_side VARCHAR(1),
			statement.setNull(15, Types.VARCHAR); //15 - _address_street VARCHAR(100),
			statement.setNull(16, Types.VARCHAR); //16 - _address_street_number VARCHAR(20),
			statement.setNull(17, Types.VARCHAR); //17 - _address_street_complement VARCHAR(50),
			statement.setNull(18, Types.VARCHAR); //18 - _address_district VARCHAR(100),
			statement.setNull(19, Types.INTEGER); //19 - _address_city_ibge INT,
			statement.setNull(20, Types.INTEGER); //20 - _address_state_ibge INT,
			statement.setNull(21, Types.INTEGER); //21 - _address_country_ibge INT,
			statement.setNull(22, Types.VARCHAR); //22 - _address_gia VARCHAR(10),
			statement.setNull(23, Types.INTEGER); //23 - _access_profile_id INT,
			statement.setLong(24, reportPerson.getUserOperation()); //24 - _user_change BIGINT,
			statement.setNull(25, Types.TIMESTAMP); //25 - _change_date DATETIME,
			statement.setNull(26, Types.VARCHAR); //26 - _access_data_user VARCHAR(100),
			statement.setNull(27, Types.VARCHAR); //27 - _access_data_password VARCHAR(100),
			statement.setNull(28, Types.TIMESTAMP); //28 - _expiration_date DATETIME,
			statement.setNull(29, Types.TIMESTAMP); //29 - _last_access_date DATETIME,
			statement.setNull(30, Types.BIT); //30 - _negative_close BIT
			statement.setNull(31, Types.BIGINT); //31 - IN _bank_account_id BIGINT
			statement.setNull(32, Types.BIGINT); //32 - IN _credit_provider_id BIGINT
			statement.setNull(33, Types.BIGINT); //33 - IN _debit_provider_id BIGINT
			statement.setNull(34, Types.INTEGER); //34 - IN IN _financial_group_id INT
			statement.setNull(35, Types.INTEGER); //35 - IN _financial_sub_group_id INT
			statement.setNull(36, Types.INTEGER); //36 - IN _register_type TINYINT
			statement.setNull(37, Types.BIT); //37 - IN _movement_automatic BIT,
			statement.setNull(38,Types.TIMESTAMP); //38 - IN _exit_access_date DATETIME,
			statement.setNull(39, Types.INTEGER);//39 - IN _person_type_id INT,
			statement.setNull(40, Types.INTEGER);//40 - IN _establishment_type_id INT,
			statement.setNull(41, Types.BIT); //41 - _blocked INT,
			statement.setNull(42, Types.BIGINT); //42 - IN _main_company_id BIGINT
			statement.setNull(43, Types.INTEGER); //43 - IN _client_id INT,
			statement.setNull(44, Types.INTEGER); //44 - IN financial_cost_center_id INT
	        resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	itens.add(new Combobox(resultSet.getString(DatabaseConstants.COLUMN_ID), resultSet.getString(DatabaseConstants.COLUMN_NAME)));
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
	public void getAllReport(final ReportPersonDTO reportPerson) throws Exception {
		CallableStatement statement = null;
		
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_person_report");
		query.append("(");
		query.append("?, "); //01 - _operation INT,
		query.append("?, "); //02 - _person_id_from BIGINT,
		query.append("?, "); //03 - _person_id_to BIGINT,
		query.append("?, "); //04 - _inactive INT,
		query.append("?, "); //05 - _description VARCHAR(100),
		query.append("?, "); //06 - _object_type VARCHAR(3),
		query.append("?, "); //07 - _person_type VARCHAR(2),
		query.append("?, "); //08 - IN _person_type_id INT,
		query.append("?, "); //09 - IN _establishment_type_id INT,
		query.append("?, "); //10 - IN _users VARCHAR(1000),
		query.append("?  "); //11 - IN _client_id INT
		query.append(")");
		query.append("}");
		
				
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - _operation INT
			statement.setLong(2, Long.valueOf(reportPerson.getPersonIdFrom())); //02 - _person_id_from BIGINT
			statement.setLong(3, Long.valueOf(reportPerson.getPersonIdTo())); //03 - _person_id_to BIGINT
			statement.setInt(4, reportPerson.getInactive()); //04 - _inactive INT
			statement.setString(5, reportPerson.getDescription()); //05 - _description VARCHAR(100)
			statement.setString(6, reportPerson.getObjectType()); //06 - _object_type VARCHAR(3)
			statement.setString(7, reportPerson.getPersonType());  //07 - _person_type VARCHAR(2)
			statement.setInt(8, reportPerson.getFilterPersonTypeId());  //08 - IN _person_type_id INT
			statement.setInt(9, reportPerson.getFilterEstablishmentTypeId());//09 - IN _establishment_type_id INT
			statement.setString(10, reportPerson.getUsersChildrens()); ///10 - IN _users VARCHAR(1000),
			statement.setInt(11, reportPerson.getClientId()); //11 - IN _client_id INT
	        resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final PersonDTO person = new PersonDTO();
	        	person.setPersonId(resultSet.getString(DatabaseConstants.COLUMN_ID));
	        	person.setName(resultSet.getString(DatabaseConstants.COLUMN_NAME));
	        	person.setPersonType(resultSet.getString(DatabaseConstants.COLUMN_PERSON_TYPE));
	        	person.setAliasName(resultSet.getString(DatabaseConstants.COLUMN_ALIAS_NAME));
	        	String cpfCnpj = resultSet.getString(DatabaseConstants.COLUMN_CPF_CNPJ);
	        	if(!StringUtils.isLong(cpfCnpj))
	        	{
	        		cpfCnpj = ConstantDataManager.TRACO;
	        	}
	        	person.setCpfCnpj(cpfCnpj);
	        	String email = resultSet.getString(DatabaseConstants.COLUMN_EMAIL);
	        	if(StringUtils.isNull(email))
	        	{
	        		email = ConstantDataManager.TRACO;
	        	}	        		
	        	person.setEmail(email);
	        	person.setBankAccount(resultSet.getLong(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID));
	        	person.setInactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE));
	        	person.setUserChange(resultSet.getLong(DatabaseConstants.COLUMN_USER_CHANGE));
	        	final Timestamp changeDate =  resultSet.getTimestamp(DatabaseConstants.COLUMN_CHANGE_DATE);
	        	final Timestamp userlastAccessDate = resultSet.getTimestamp(DatabaseConstants.COLUMN_LAST_ACCESS_DATE);
	        	final Timestamp userLastAccessFinish= resultSet.getTimestamp(DatabaseConstants.COLUMN_LAST_ACCESS_FINISH_DATE);

	        	if(changeDate!=null)
	        	{
	        		final Calendar calendar = Calendar.getInstance();
	        		calendar.setTimeInMillis(changeDate.getTime());
	        		person.setChangeDate(calendar);
	        	}	
	        	if(userlastAccessDate!=null)
	        	{
	        		final Calendar calendar = Calendar.getInstance();
	        		calendar.setTimeInMillis(userlastAccessDate.getTime());
	        		person.setLastAccessDate(calendar);
	        	}	
	        	if(userLastAccessFinish!=null)
	        	{
	        		final Calendar calendar = Calendar.getInstance();
	        		calendar.setTimeInMillis(userLastAccessFinish.getTime());
	        		person.setUserExitAcessDate(calendar);
	        	}	
	        	
	        	person.setPersonTypeId(resultSet.getInt(DatabaseConstants.COLUMN_PERSON_TYPE_ID));
	        	person.setPersonTypeDescription(resultSet.getString(DatabaseConstants.COLUMN_PERSON_TYPE_DESCRIPTION));
	        	person.setEstablishmentTypeId(resultSet.getInt(DatabaseConstants.COLUMN_ESTABLISHMENT_TYPE_ID));
	        	person.setEstablishmentTypeDescription(resultSet.getString(DatabaseConstants.COLUMN_ESTABLISHMENT_TYPE_DESCRIPTION));
	        	person.setCountProducts(resultSet.getInt(DatabaseConstants.COLUMN_COUNT_PRODUCT));
	        	person.setAttachedUsers(resultSet.getString(DatabaseConstants.COLUMN_ATTACHED_USERS));
	        	person.setFinancialCostCenterId(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_COST_CENTER_ID));
	        	reportPerson.addItem(person);
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
}