package com.manager.systems.common.dto.movement.company;

import java.io.Serializable;

public class CompanyMovementExecutionItemDTO implements Serializable
{
	private static final long serialVersionUID = 7927565459010117025L;
	
	private long id;
	private long fildId;
	private String company;
	private long companyId;
	private String expiryData;
	private String executedData;
	private boolean inactive;
	private String motive;
	private String documentNote;
	private String fildName;
	private String fildValue;
	private long documentParentId;
	
	
	public CompanyMovementExecutionItemDTO() 
	{
		super();
	}
	
	public final long getId() {
		return this.id;
	}

	public final void setId(long id) {
		this.id = id;
	}

	public final long getFildId() {
		return this.fildId;
	}

	public final void setFildId(final long fildId) {
		this.fildId = fildId;
	}

	public final String getCompany() {
		return this.company;
	}

	public final void setCompany(final String company) {
		this.company = company;
	}
	
	public final long getCompanyId() 
	{
		return this.companyId;
	}

	public final void setCompanyId(final long companyId) 
	{
		this.companyId = companyId;
	}

	public final String getExpiryData() {
		return this.expiryData;
	}

	public final void setExpiryData(final String expiryData) {
		this.expiryData = expiryData;
	}

	public final String getExecutedData() {
		return this.executedData;
	}

	public final void setExecutedData(final String executedData) {
		this.executedData = executedData;
	}

	public final boolean isInactive() {
		return this.inactive;
	}

	public final void setInactive(final boolean inactive) {
		this.inactive = inactive;
	}

	public final String getMotive() {
		return this.motive;
	}

	public final void setMotive(final String motive) {
		this.motive = motive;
	}

	public final String getDocumentNote() {
		return this.documentNote;
	}

	public final void setDocumentNote(final String documentNote) {
		this.documentNote = documentNote;
	}

	public final String getFildName() {
		return this.fildName;
	}

	public final void setFildName(final String fildName) {
		this.fildName = fildName;
	}

	public final String getFildValue() {
		return this.fildValue;
	}

	public final void setFildValue(final String fildValue) {
		this.fildValue = fildValue;
	}

	public final long getDocumentParentId() {
		return this.documentParentId;
	}

	public final void setDocumentParentId(final long documentParentId) {
		this.documentParentId = documentParentId;
	}
}