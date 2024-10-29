package com.manager.systems.common.dao.adm;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.CommonDao;
import com.manager.systems.common.dao.impl.CommonDaoImpl;
import com.manager.systems.common.dao.impl.adm.PersonDaoImpl;
import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.PersonDTO;
import com.manager.systems.common.dto.adm.ReportPersonDTO;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.ObjectType;
import com.manager.systems.common.vo.PersonType;

public class PersonDaoTest 
{
	private Connection connection = null;
	private PersonDao personDao = null;
	private CommonDao commonDao = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.personDao = new PersonDaoImpl(this.connection);
		this.commonDao = new CommonDaoImpl(this.connection);
	}
	
	@Test
	public void crudUser() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.personDao);
		
		final Long id = this.commonDao.getNextCode("user", 0L);
		Assert.assertTrue(id>0);
		
		//Save insert
		PersonDTO person = new PersonDTO();
		person.setPersonId(String.valueOf(id));
		person.setInactive(false);
		person.setName("Manager Name");
		person.setAliasName("Manager Alias");
		person.setObjectType(ObjectType.USER.getType());
		person.setPersonType(PersonType.FISICA.getType());
		person.setCpfCnpj("0");
		person.setRg("");
		person.setIe("");
		person.setEmail("managerteste@manager.com");
		person.setAddressZipCode("03050000");
		person.setAddressZipCodeSide("A");
		person.setAddressStreet("RUA HIPODROMO");
		person.setAddressNumber("650");
		person.setAddressComplement("AP 102 BL LEONARDO");
		person.setAddressDistrict("MOOCA");
	    person.setAddressCityIbge("3550308");
		person.setAddressStateIbge("35");
		person.setAddressCountryIbge("1058");
	    person.setAddressGia("1004");
		person.setAccessProfileId(1); 
	    person.setUserChange(1L); 
	    person.setChangeDate(Calendar.getInstance()); 
	    person.setAccessDataUser("admteste@manager.com"); 
	    person.setAccessDataPassword("123456"); 
	    person.setExpirationDate(Calendar.getInstance());
	    person.setLastAccessDate(Calendar.getInstance());
	    person.setNegativeClose(false);		
		final boolean isSaved = this.personDao.save(person);
		Assert.assertTrue(isSaved);
		
		//Get
		person = new PersonDTO();
		person.setPersonId(String.valueOf(id));
		person.setObjectType(ObjectType.USER.getType());
		this.personDao.get(person);
		Assert.assertEquals("Manager Name", person.getName());

		//Inactive
		person = new PersonDTO();
		person.setPersonId(String.valueOf(id));
		person.setObjectType(ObjectType.USER.getType());
		person.setInactive(true);
		person.setUserChange(1L);
		final boolean isInactive = this.personDao.inactive(person);
		Assert.assertTrue(isInactive);
		
		//Get
		person = new PersonDTO();
		person.setPersonId(String.valueOf(id));
		person.setObjectType(ObjectType.USER.getType());
		this.personDao.get(person);
		Assert.assertTrue(person.isInactive());
		
		//Save update
		person = new PersonDTO();
		person.setPersonId(String.valueOf(id));
		person.setInactive(true);
		person.setName("Manager Name");
		person.setAliasName("Manager Alias Update");
		person.setObjectType(ObjectType.USER.getType());
		person.setPersonType(PersonType.FISICA.getType());
		person.setCpfCnpj("0");
		person.setRg("");
		person.setIe("");
		person.setEmail("managerteste@manager.com");
		person.setAddressZipCode("03050000");
		person.setAddressZipCodeSide("A");
		person.setAddressStreet("RUA HIPODROMO");
		person.setAddressNumber("650");
		person.setAddressComplement("AP 102 BL LEONARDO");
		person.setAddressDistrict("MOOCA");
	    person.setAddressCityIbge("3550308");
		person.setAddressStateIbge("35");
		person.setAddressCountryIbge("1058");
	    person.setAddressGia("1004");
		person.setAccessProfileId(1); 
	    person.setUserChange(1L); 
	    person.setChangeDate(Calendar.getInstance()); 
	    person.setAccessDataUser("admteste@manager.com"); 
	    person.setAccessDataPassword("123456"); 
	    person.setExpirationDate(Calendar.getInstance());
	    person.setLastAccessDate(Calendar.getInstance());
	    person.setNegativeClose(false);		
		final boolean isUpdated = this.personDao.save(person);
		Assert.assertTrue(isUpdated);		

		//Get
		person = new PersonDTO();
		person.setPersonId(String.valueOf(id));
		person.setObjectType(ObjectType.USER.getType());
		this.personDao.get(person);
		Assert.assertEquals("Manager Alias Update", person.getAliasName());
		Assert.assertFalse(person.isInactive());		
		
		final ReportPersonDTO reportPerson = new ReportPersonDTO();
		reportPerson.setPersonIdFrom(String.valueOf(id));
		reportPerson.setPersonIdTo(String.valueOf(id));
		reportPerson.setInactive("0");
		reportPerson.setDescription("Manager Name");
		reportPerson.setObjectType(ObjectType.USER.getType());
		this.personDao.getAll(reportPerson);
		Assert.assertTrue(reportPerson.getPersons().size()>0);	
		
		final List<Combobox> itens = this.personDao.getAllAutocomplete(reportPerson);
		Assert.assertTrue(itens.size()>0);	
	}
	
	@Test
	public void crudCompany() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.personDao);
		
		final Long id = this.commonDao.getNextCode("company", 0L);
		Assert.assertTrue(id>0);
		
		//Save insert
		PersonDTO person = new PersonDTO();
		person.setPersonId(String.valueOf(id));
		person.setInactive(false);
		person.setName("Manager Name");
		person.setAliasName("Manager Alias");
		person.setObjectType(ObjectType.COMPANY.getType());
		person.setPersonType(PersonType.JURIDICA.getType());
		person.setCpfCnpj("0");
		person.setRg("");
		person.setIe("");
		person.setEmail("managerteste@manager.com");
		person.setAddressZipCode("03050000");
		person.setAddressZipCodeSide("A");
		person.setAddressStreet("RUA HIPODROMO");
		person.setAddressNumber("650");
		person.setAddressComplement("AP 102 BL LEONARDO");
		person.setAddressDistrict("MOOCA");
	    person.setAddressCityIbge("3550308");
		person.setAddressStateIbge("35");
		person.setAddressCountryIbge("1058");
	    person.setAddressGia("1004");
		person.setAccessProfileId(1); 
	    person.setUserChange(1L); 
	    person.setChangeDate(Calendar.getInstance()); 
	    person.setAccessDataUser("admteste@manager.com"); 
	    person.setAccessDataPassword("123456"); 
	    person.setExpirationDate(Calendar.getInstance());
	    person.setLastAccessDate(Calendar.getInstance());
	    person.setNegativeClose(false);		
		final boolean isSaved = this.personDao.save(person);
		Assert.assertTrue(isSaved);
		
		//Get
		person = new PersonDTO();
		person.setPersonId(String.valueOf(id));
		person.setObjectType(ObjectType.COMPANY.getType());
		this.personDao.get(person);
		Assert.assertEquals("Manager Name", person.getName());

		//Inactive
		person = new PersonDTO();
		person.setPersonId(String.valueOf(id));
		person.setObjectType(ObjectType.COMPANY.getType());
		person.setInactive(true);
		person.setUserChange(1L);
		final boolean isInactive = this.personDao.inactive(person);
		Assert.assertTrue(isInactive);
		
		//Get
		person = new PersonDTO();
		person.setPersonId(String.valueOf(id));
		person.setObjectType(ObjectType.COMPANY.getType());
		this.personDao.get(person);
		Assert.assertTrue(person.isInactive());
		
		//Save update
		person = new PersonDTO();
		person.setPersonId(String.valueOf(id));
		person.setInactive(true);
		person.setName("Manager Name");
		person.setAliasName("Manager Alias Update");
		person.setObjectType(ObjectType.COMPANY.getType());
		person.setPersonType(PersonType.JURIDICA.getType());
		person.setCpfCnpj("0");
		person.setRg("");
		person.setIe("");
		person.setEmail("managerteste@manager.com");
		person.setAddressZipCode("03050000");
		person.setAddressZipCodeSide("A");
		person.setAddressStreet("RUA HIPODROMO");
		person.setAddressNumber("650");
		person.setAddressComplement("AP 102 BL LEONARDO");
		person.setAddressDistrict("MOOCA");
	    person.setAddressCityIbge("3550308");
		person.setAddressStateIbge("35");
		person.setAddressCountryIbge("1058");
	    person.setAddressGia("1004");
		person.setAccessProfileId(1); 
	    person.setUserChange(1L); 
	    person.setChangeDate(Calendar.getInstance()); 
	    person.setAccessDataUser("admteste@manager.com"); 
	    person.setAccessDataPassword("123456"); 
	    person.setExpirationDate(Calendar.getInstance());
	    person.setLastAccessDate(Calendar.getInstance());
	    person.setNegativeClose(false);		
		final boolean isUpdated = this.personDao.save(person);
		Assert.assertTrue(isUpdated);		

		//Get
		person = new PersonDTO();
		person.setPersonId(String.valueOf(id));
		person.setObjectType(ObjectType.COMPANY.getType());
		this.personDao.get(person);
		Assert.assertEquals("Manager Alias Update", person.getAliasName());
		Assert.assertFalse(person.isInactive());		
		
		final ReportPersonDTO reportPerson = new ReportPersonDTO();
		reportPerson.setPersonIdFrom(String.valueOf(id));
		reportPerson.setPersonIdTo(String.valueOf(id));
		reportPerson.setInactive("0");
		reportPerson.setDescription("Manager Name");
		reportPerson.setObjectType(ObjectType.COMPANY.getType());
		this.personDao.getAll(reportPerson);
		Assert.assertTrue(reportPerson.getPersons().size()>0);	
	}
	
	@Test
	public void crudProvider() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.personDao);
		
		final Long id = this.commonDao.getNextCode("provider", 0L);
		Assert.assertTrue(id>0);
		
		//Save insert
		PersonDTO person = new PersonDTO();
		person.setPersonId(String.valueOf(id));
		person.setInactive(false);
		person.setName("Manager Name");
		person.setAliasName("Manager Alias");
		person.setObjectType(ObjectType.PROVIDER.getType());
		person.setPersonType(PersonType.JURIDICA.getType());
		person.setCpfCnpj("0");
		person.setRg("");
		person.setIe("");
		person.setEmail("managerteste@manager.com");
		person.setAddressZipCode("03050000");
		person.setAddressZipCodeSide("A");
		person.setAddressStreet("RUA HIPODROMO");
		person.setAddressNumber("650");
		person.setAddressComplement("AP 102 BL LEONARDO");
		person.setAddressDistrict("MOOCA");
	    person.setAddressCityIbge("3550308");
		person.setAddressStateIbge("35");
		person.setAddressCountryIbge("1058");
	    person.setAddressGia("1004");
		person.setAccessProfileId(1); 
	    person.setUserChange(1L); 
	    person.setChangeDate(Calendar.getInstance()); 
	    person.setAccessDataUser("admteste@manager.com"); 
	    person.setAccessDataPassword("123456"); 
	    person.setExpirationDate(Calendar.getInstance());
	    person.setLastAccessDate(Calendar.getInstance());
	    person.setNegativeClose(false);		
		final boolean isSaved = this.personDao.save(person);
		Assert.assertTrue(isSaved);
		
		//Get
		person = new PersonDTO();
		person.setPersonId(String.valueOf(id));
		person.setObjectType(ObjectType.PROVIDER.getType());
		this.personDao.get(person);
		Assert.assertEquals("Manager Name", person.getName());

		//Inactive
		person = new PersonDTO();
		person.setPersonId(String.valueOf(id));
		person.setObjectType(ObjectType.PROVIDER.getType());
		person.setInactive(true);
		person.setUserChange(1L);
		final boolean isInactive = this.personDao.inactive(person);
		Assert.assertTrue(isInactive);
		
		//Get
		person = new PersonDTO();
		person.setPersonId(String.valueOf(id));
		person.setObjectType(ObjectType.PROVIDER.getType());
		this.personDao.get(person);
		Assert.assertTrue(person.isInactive());
		
		//Save update
		person = new PersonDTO();
		person.setPersonId(String.valueOf(id));
		person.setInactive(true);
		person.setName("Manager Name");
		person.setAliasName("Manager Alias Update");
		person.setObjectType(ObjectType.PROVIDER.getType());
		person.setPersonType(PersonType.JURIDICA.getType());
		person.setCpfCnpj("0");
		person.setRg("");
		person.setIe("");
		person.setEmail("managerteste@manager.com");
		person.setAddressZipCode("03050000");
		person.setAddressZipCodeSide("A");
		person.setAddressStreet("RUA HIPODROMO");
		person.setAddressNumber("650");
		person.setAddressComplement("AP 102 BL LEONARDO");
		person.setAddressDistrict("MOOCA");
	    person.setAddressCityIbge("3550308");
		person.setAddressStateIbge("35");
		person.setAddressCountryIbge("1058");
	    person.setAddressGia("1004");
		person.setAccessProfileId(1); 
	    person.setUserChange(1L); 
	    person.setChangeDate(Calendar.getInstance()); 
	    person.setAccessDataUser("admteste@manager.com"); 
	    person.setAccessDataPassword("123456"); 
	    person.setExpirationDate(Calendar.getInstance());
	    person.setLastAccessDate(Calendar.getInstance());
	    person.setNegativeClose(false);		
		final boolean isUpdated = this.personDao.save(person);
		Assert.assertTrue(isUpdated);		

		//Get
		person = new PersonDTO();
		person.setPersonId(String.valueOf(id));
		person.setObjectType(ObjectType.PROVIDER.getType());
		this.personDao.get(person);
		Assert.assertEquals("Manager Alias Update", person.getAliasName());
		Assert.assertFalse(person.isInactive());		
		
		final ReportPersonDTO reportPerson = new ReportPersonDTO();
		reportPerson.setPersonIdFrom(String.valueOf(id));
		reportPerson.setPersonIdTo(String.valueOf(id));
		reportPerson.setInactive("0");
		reportPerson.setDescription("Manager Name");
		reportPerson.setObjectType(ObjectType.PROVIDER.getType());
		this.personDao.getAll(reportPerson);
		Assert.assertTrue(reportPerson.getPersons().size()>0);	
	}

	@After
	public void finaliza() throws Exception
	{
		if(this.connection!=null)
		{
			this.connection.rollback();
			this.connection.close();
		}
		Assert.assertTrue(this.connection.isClosed());
		this.connection = null;		
	}
}
