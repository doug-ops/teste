/**
 *Create Date 12/02/2022
 */
package com.manager.systems.web.financial.dto;

import java.io.Serializable;

public class CashierClosingWekendItemDTO implements Serializable 
{
	private static final long serialVersionUID = -5234403116121957942L;

	private Long companyId;
	private String companyDescription;
	private Double documentValue;
	private Integer week;
	private String weekDescription;
	
		
	public CashierClosingWekendItemDTO() {
		super();
	}

	public final Long getCompanyId() {
		return this.companyId;
	}

	public final void setCompanyId(final Long companyId) {
		this.companyId = companyId;
	}

	public final String getCompanyDescription() {
		return this.companyDescription;
	}

	public final void setCompanyDescription(final String companyDescription) {
		this.companyDescription = companyDescription;
	}

	public final Double getDocumentValue() {
		return this.documentValue;
	}

	public final void sumDocumentValue(final Double documentValue) {
		if(this.documentValue == null) {
			this.documentValue = documentValue;
		}
		else {
			this.documentValue += documentValue;
		}
	}

	public final Integer getWeek() {
		return this.week;
	}

	public final void setWeek(final Integer week) {
		this.week = week;
	}

	public final String getWeekDescription() {
		return this.weekDescription;
	}

	public final void setWeekDescription(final String weekDescription) {
		this.weekDescription = weekDescription;
	}
}