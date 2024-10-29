package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.utils.StringUtils;

public class ReportPersonDTO implements Serializable
{
	private static final long serialVersionUID = -6770887536432093361L;
	
	private String personIdFrom;
	private String personIdTo;
	private String inactive;
	private String description;
	private String objectType;
	private long userOperation;
	private String personType;
	private int filterPersonTypeId;
	private int filterEstablishmentTypeId;
	private int clientId;
	private String usersChildrens;
	private String origin;
	
	private List<PersonDTO> persons = new ArrayList<PersonDTO>();
	
	public ReportPersonDTO() 
	{
		super();
	}
	
	public final long getPersonIdFrom()
	{
		return StringUtils.isLong(this.personIdFrom) ? Long.valueOf(this.personIdFrom) : 0;
	}
	
	public final void setPersonIdFrom(final String personIdFrom) 
	{
		this.personIdFrom = personIdFrom;
	}

	public final long getPersonIdTo()
	{
		return StringUtils.isLong(this.personIdTo) ? Long.valueOf(this.personIdTo) : 0;
	}

	public final void setPersonIdTo(final String personIdTo) 
	{
		this.personIdTo = personIdTo;
	}

	public final int getInactive() 
	{
		return StringUtils.isLong(this.inactive) ? Integer.valueOf(this.inactive) : 2;
	}

	public final void setInactive(final String inactive) 
	{
		this.inactive = inactive;
	}

	public final String getDescription() 
	{
		return this.description;
	}

	public final void setDescription(final String description) 
	{
		this.description = description;
	}

	public String getObjectType() 
	{
		return this.objectType;
	}
	
	public void setObjectType(final String objectType) 
	{
		this.objectType = objectType;
	}
	
	public List<PersonDTO> getPersons() 
	{
		return this.persons;
	}
	
	public final void addItem(final PersonDTO person)
	{
		this.persons.add(person);
	}

	public final long getUserOperation() {
		return this.userOperation;
	}

	public final void setUserOperation(final long userOperation) {
		this.userOperation = userOperation;
	}
	
	public final String getPersonType() {
		return this.personType;
	}

	public final void setPersonType(final String personType) {
		this.personType = personType;
	}
	
	public final int getFilterPersonTypeId() {
		return this.filterPersonTypeId;
	}

	public final void setFilterPersonTypeId(final int filterPersonTypeId) {
		this.filterPersonTypeId = filterPersonTypeId;
	}

	public final int getFilterEstablishmentTypeId() {
		return this.filterEstablishmentTypeId;
	}

	public final void setFilterEstablishmentTypeId(final int filterEstablishmentTypeId) {
		this.filterEstablishmentTypeId = filterEstablishmentTypeId;
	}
	
	public final int getClientId() {
		return this.clientId;
	}

	public final void setClientId(final int clientId) {
		this.clientId = clientId;
	}

	public final String getUsersChildrens() {
		return this.usersChildrens;
	}

	public final void setUsersChildrens(final String usersChildrens) {
		this.usersChildrens = usersChildrens;
	}

	public final String getOrigin() {
		return this.origin;
	}

	public final void setOrigin(final String origin) {
		this.origin = origin;
	}
}