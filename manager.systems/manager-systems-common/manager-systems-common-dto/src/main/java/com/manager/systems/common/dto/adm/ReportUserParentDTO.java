package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.sql.Date;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;

public class ReportUserParentDTO implements Serializable 
{
	private static final long serialVersionUID = 7927565459010117025L;
	
	private long id;
	private String name;
	private Date expiryData;
	private String expiryDataString;
	private String nameConcactId;
	private String backgroundColor;
	private long bankAccountOriginId;
	private long bankAccountDestinyId;
	private Integer weekYear;
	private String inactive;
	
	
	public ReportUserParentDTO() 
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

	public final String getName() 
	{
		return this.name;
	}

	public final void setName(final String name) 
	{
		this.name = name;
		this.nameConcactId = this.name + ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT + this.id +  ConstantDataManager.PARENTESES_RIGHT;
	}
	
	public final Date getExpiryData() {
		return this.expiryData;
	}
	
	public final void setExpiryData(final Date expiryData) {
		this.expiryData = expiryData;
	}
	
	public final String getExpiryDataString() {
		return this.expiryDataString;
	}

	public final void setExpiryDataString(final String expiryDataString) {
		this.expiryDataString = expiryDataString;
			
	}

	public final String getNameConcactId() {
		return this.nameConcactId;
	}

	public final void setNameConcactId(final String nameConcactId) {
		this.nameConcactId = nameConcactId;
	}

	public final String getBackgroundColor() {
		return this.backgroundColor;
	}

	public final void setBackgroundColor(final String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public final long getBankAccountOriginId() {
		return this.bankAccountOriginId;
	}

	public final void setBankAccountOriginId(final long bankAccountOriginId) {
		this.bankAccountOriginId = bankAccountOriginId;
	}
	
	public final long getBankAccountDestinyId() 
	{
		return this.bankAccountDestinyId;
	}

	public final void setBankAccountDestinyId(final long bankAccountDestinyId) 
	{
		this.bankAccountDestinyId = bankAccountDestinyId;
	}
	
	public final Integer getWeekYear() {
		return this.weekYear;
	}

	public final void setWeekYear(final Integer weekYear) {
		this.weekYear = weekYear;
	}
	
	public final int getInactive() 
	{
		return StringUtils.isLong(this.inactive) ? Integer.valueOf(this.inactive) : 2;
	}

	public final void setInactive(final String inactive) 
	{
		this.inactive = inactive;
	}
}