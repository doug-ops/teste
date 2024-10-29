package com.manager.systems.web.movements.dto.dre;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

public class DreReportFilterDTO 
{
	private long companyId;
	private int bankAccountId;
	private String financialGroupId;
	private String financialSubGroupId;
	private int documentType;
	private String documentStatus;
	private String documentNumber;
	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;
	
	private Map<Integer, DreReportItemDTO> itens = new TreeMap<Integer, DreReportItemDTO>();
	private double totalCredit;
	private double totalDebit;
	private double totalSaldo;
	private long userOperation;	
	private String companysId;
	private String documentsParentId;
	private long userId;
	private int filterBy;
	private int typeDocumentValue;
	
	public DreReportFilterDTO() 
	{
		super();
	}

	public final long getCompanyId()
	{
		return this.companyId;
	}

	public final void setCompanyId(final long companyId) 
	{
		this.companyId = companyId;
	}

	public final int getBankAccountId() 
	{
		return this.bankAccountId;
	}

	public final void setBankAccountId(final int bankAccountId) 
	{
		this.bankAccountId = bankAccountId;
	}

	public final String getFinancialGroupId()
	{
		return this.financialGroupId;
	}

	public final void setFinancialGroupId(final String financialGroupId) 
	{
		this.financialGroupId = financialGroupId;
	}

	public final String getFinancialSubGroupId()
	{
		return this.financialSubGroupId;
	}

	public final void setFinancialSubGroupId(final String financialSubGroupId)
	{
		this.financialSubGroupId = financialSubGroupId;
	}

	public final int getDocumentType() 
	{
		return this.documentType;
	}

	public final void setDocumentType(final int documentType) 
	{
		this.documentType = documentType;
	}

	public final String getDocumentStatus() 
	{
		return this.documentStatus;
	}

	public final void setDocumentStatus(final String documentStatus) 
	{
		this.documentStatus = documentStatus;
	}
	
	public final String getDocumentNumber() 
	{
		return this.documentNumber;
	}

	public final void setDocumentNumber(final String documentNumber)
	{
		this.documentNumber = documentNumber;
	}

	public final LocalDateTime getDateFrom()
	{
		return this.dateFrom;
	}

	public final void setDateFrom(final LocalDateTime dateFrom) 
	{
		this.dateFrom = dateFrom;
	}

	public final LocalDateTime getDateTo()
	{
		return this.dateTo;
	}

	public final void setDateTo(final LocalDateTime dateTo) 
	{
		this.dateTo = dateTo;
	}

	
	public final double getTotalCredit() 
	{
		return this.totalCredit;
	}

	public final double getTotalDebit() 
	{
		return this.totalDebit;
	}

	public final double getTotalSaldo()
	{
		return this.totalSaldo;
	}
	
	public final void calculateSaldo()
	{
		this.totalSaldo = (this.totalCredit - this.totalDebit);
	}

	public final Map<Integer, DreReportItemDTO> getItens() 
	{
		return this.itens;
	}
	
	public final long getUserOperation() {
		return this.userOperation;
	}

	public final void setUserOperation(final long userOperation) {
		this.userOperation = userOperation;
	}
		
	public final String getCompanysId() {
		return this.companysId;
	}

	public final void setCompanysId(final String companysId) {
		this.companysId = companysId;
	}

	public final String getDocumentsParentId() {
		return this.documentsParentId;
	}

	public final void setDocumentsParentId(final String documentsParentId) {
		this.documentsParentId = documentsParentId;
	}

	public final long getUserId() {
		return this.userId;
	}

	public final void setUserId(final long userId) {
		this.userId = userId;
	}
	
	public final int getFilterBy()
	{
		return this.filterBy;
	}

	public final void setFilterBy(final int filterBy)
	{
		this.filterBy = filterBy;
	}
	
	public final int getTypeDocumentValue() {
		return this.typeDocumentValue;
	}

	public final void setTypeDocumentValue(final int typeDocumentValue) {
		this.typeDocumentValue = typeDocumentValue;
	}
}