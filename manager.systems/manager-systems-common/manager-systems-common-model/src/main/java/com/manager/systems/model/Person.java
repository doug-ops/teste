package com.manager.systems.model;

import java.lang.reflect.Field;

import com.manager.systems.common.dto.adm.AccessProfileDTO;
import com.manager.systems.common.vo.AccessData;
import com.manager.systems.common.vo.Address;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.ContactData;
import com.manager.systems.common.vo.DocumentData;
import com.manager.systems.common.vo.ObjectType;
import com.manager.systems.common.vo.PersonType;

public abstract class Person
{
	private long id;
	private PersonType personType;
	private DocumentData documentData;
	private ObjectType objectType;
	private ContactData contactData;
	private Address address;
	private AccessData accessData;
	private ChangeData changeData;
	private boolean inactive;
	private AccessProfileDTO accessProfile;
	private IntegrationSystems integrationSystems;
	private long mainCompanyId;
	private String mainCompanyDescription;
	private int mainCompanyBankAccountId;
	private String mainCompanyBankAccountDescription;
	
	public Person() 
	{
		super();
	}

	public final long getId() 
	{
		return this.id;
	}

	public final void setId(final long id) 
	{
		this.id = id;
	}

	public final PersonType getPersonType() 
	{
		return this.personType;
	}

	public final void setPersonType(final PersonType personType) 
	{
		this.personType = personType;
	}

	public final DocumentData getDocumentData()
	{
		return this.documentData;
	}

	public final void setDocumentData(final DocumentData documentData)
	{
		this.documentData = documentData;
	}

	public final ObjectType getObjectType()
	{
		return this.objectType;
	}

	public final void setObjectType(final ObjectType objectType)
	{
		this.objectType = objectType;
	}

	public final ContactData getContactData()
	{
		return this.contactData;
	}

	public final void setContactData(final ContactData contactData)
	{
		this.contactData = contactData;
	}

	public final Address getAddress()
	{
		return this.address;
	}

	public final void setAddress(final Address address) 
	{
		this.address = address;
	}

	public final AccessData getAccessData()
	{
		return this.accessData;
	}

	public final void setAccessData(final AccessData accessData)
	{
		this.accessData = accessData;
	}

	public final ChangeData getChangeData()
	{
		return this.changeData;
	}

	public final void setChangeData(final ChangeData changeData)
	{
		this.changeData = changeData;
	}

	public final boolean isInactive() 
	{
		return this.inactive;
	}

	public final void setInactive(final boolean inactive)
	{
		this.inactive = inactive;
	}

	public final AccessProfileDTO getAccessProfile() 
	{
		return this.accessProfile;
	}

	public final void setAccessProfile(final AccessProfileDTO accessProfile)
	{
		this.accessProfile = accessProfile;
	}
	
	public IntegrationSystems getIntegrationSystems() 
	{
		return this.integrationSystems;
	}
	
	public void setIntegrationSystems(final IntegrationSystems integrationSystems) 
	{
		this.integrationSystems = integrationSystems;
	}
	
	public final long getMainCompanyId() {
		return this.mainCompanyId;
	}

	public final void setMainCompanyId(final long mainCompanyId) {
		this.mainCompanyId = mainCompanyId;
	}

	public final String getMainCompanyDescription() {
		return this.mainCompanyDescription;
	}

	public final void setMainCompanyDescription(final String mainCompanyDescription) {
		this.mainCompanyDescription = mainCompanyDescription;
	}

	public final int getMainCompanyBankAccountId() {
		return this.mainCompanyBankAccountId;
	}

	public final void setMainCompanyBankAccountId(final int mainCompanyBankAccountId) {
		this.mainCompanyBankAccountId = mainCompanyBankAccountId;
	}
	
	public final String getMainCompanyBankAccountDescription() {
		return this.mainCompanyBankAccountDescription;
	}

	public final void setMainCompanyBankAccountDescription(final String mainCompanyBankAccountDescription) {
		this.mainCompanyBankAccountDescription = mainCompanyBankAccountDescription;
	}

	@Override
	public String toString() 
	{
		final StringBuilder result = new StringBuilder();
		final String newLine = System.getProperty("line.separator");

		result.append(this.getClass().getName());
		result.append(" Object {");
		result.append(newLine);

		final Field[] fields = this.getClass().getDeclaredFields();

		for (final Field field : fields) 
		{
			result.append("  ");
			try 
			{
				result.append(field.getName());
				result.append(": ");
				// requires access to private field:
				result.append(field.get(this));
			} 
			catch (final IllegalAccessException ex) 
			{

			}
			result.append(newLine);
		}
		result.append("}");
		return result.toString();
	}	
}