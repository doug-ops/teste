package com.manager.systems.web.movements.dto.bankAccount;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;

public class BankAccountStatementItemDTO {

	private long documentParentId;
	private long documentId;
	private String documentDate;
	private long documentDateLong;
	private int bankAccountId;
	private String banAccountDescription;
	private String documentNote;
	private double documentValue;
	private int companyId;
	private String companyDescription;

	public BankAccountStatementItemDTO() {
		super();
	}

	public final long getDocumentParentId() {
		return this.documentParentId;
	}

	public final String getDocumentDate() {
		return this.documentDate;
	}

	public final int getBankAccountId() {
		return this.bankAccountId;
	}

	public final String getBanAccountDescription() {
		return this.banAccountDescription;
	}

	public final String getDocumentNote() {
		return this.documentNote;
	}

	public final double getDocumentValue() {
		return this.documentValue;
	}

	public final void setDocumentParentId(final long documentParentId) {
		this.documentParentId = documentParentId;
	}

	public final void setDocumentDate(final String documentDate) {
		this.documentDate = documentDate;
	}

	public final void setBankAccountId(int bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public final void setBanAccountDescription(final String banAccountDescription) {
		this.banAccountDescription = banAccountDescription;
	}

	public final void setDocumentNote(final String documentNote) {
		this.documentNote = documentNote;
	}

	public final void setDocumentValue(final double documentValue) {
		this.documentValue = documentValue;
	}
	
	public final long getDocumentId() {
		return this.documentId;
	}
	
	public final void setDocumentId(final long documentId) {
		this.documentId = documentId;
	}
	
	public final int getCompanyId() {
		return this.companyId;
	}

	public final String getCompanyDescription() {
		return this.companyDescription;
	}

	public final void setCompanyId(final int companyId) {
		this.companyId = companyId;
	}

	public final void setCompanyDescription(final String companyDescription) {
		this.companyDescription = companyDescription;
	}
	
	public final long getDocumentDateLong() {
		return this.documentDateLong;
	}

	public final void setDocumentDateLong(final long documentDateLong) {
		this.documentDateLong = documentDateLong;
	}

	public String getDebitString() {
		String result = ConstantDataManager.BLANK;
		if(this.documentValue<0)
		{
			result =  StringUtils.formatDecimalValue(this.documentValue);			
		}
		return result;
	}
	
	public String getCreditString() {
		String result = ConstantDataManager.BLANK;
		if(this.documentValue>-1)
		{
			result =  StringUtils.formatDecimalValue(this.documentValue);			
		}
		return result;
	}

	public String getBankAccountLabel()
	{
		final StringBuilder label = new StringBuilder();
		label.append(this.banAccountDescription);
		label.append(ConstantDataManager.SPACE);
		label.append(ConstantDataManager.PARENTESES_LEFT);
		label.append(this.bankAccountId);
		label.append(ConstantDataManager.PARENTESES_RIGHT);
		return label.toString();
	}
}