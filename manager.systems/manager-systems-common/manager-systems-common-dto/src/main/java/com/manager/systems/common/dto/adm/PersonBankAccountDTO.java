package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.Calendar;

public class PersonBankAccountDTO implements Serializable {
	private static final long serialVersionUID = 2409774086257054924L;

	private long objectId;
	private String objectType;
	private int bankAccountId;
	private String description;
	private boolean inactive;
	private long userChange;
	private Calendar changeDate;
	private String origin;

	public PersonBankAccountDTO() {
		super();
	}

	public final long getObjectId() {
		return this.objectId;
	}

	public final void setObjectId(final long objectId) {
		this.objectId = objectId;
	}

	public final String getObjectType() {
		return this.objectType;
	}

	public final void setObjectType(final String objectType) {
		this.objectType = objectType;
	}

	public final boolean isInactive() {
		return this.inactive;
	}

	public final int getInactive() {
		return this.inactive ? 1 : 0;
	}

	public final void setInactive(final boolean inactive) {
		this.inactive = inactive;
	}

	public final long getUserChange() {
		return this.userChange;
	}

	public final void setUserChange(final long userChange) {
		this.userChange = userChange;
	}

	public final Calendar getChangeDate() {
		return this.changeDate;
	}

	public final void setChangeDate(final Calendar changeDate) {
		this.changeDate = changeDate;
	}

	public final int getBankAccountId() {
		return this.bankAccountId;
	}

	public final void setBankAccountId(final int bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public final String getDescription() {
		return this.description;
	}

	public final void setDescription(final String description) {
		this.description = description;
	}
	
	public final String getOrigin() {
		return this.origin;
	}

	public final void setOrigin(final String origin) {
		this.origin = origin;
	}
}