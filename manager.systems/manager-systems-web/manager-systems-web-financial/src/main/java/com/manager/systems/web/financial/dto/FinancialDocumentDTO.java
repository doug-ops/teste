package com.manager.systems.web.financial.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FinancialDocumentDTO implements Serializable 
{
	private static final long serialVersionUID = 7085363245563957930L;
	
	private long id;
	private long documentId;
	private long documentParentId;
	private boolean credit; 
  	private long companyId;
    private long providerId; 
    private int bankAccountId;
    private int movementType;
    private LocalDateTime paymentDate;
    private LocalDateTime paymentExpiryDate;
    private int paymentStatus;
    //TODO: Change to Enum
    private int documentType;
    private String documentNumber;
    private double documentValue;
    private double documentDiscount;
    private double documentExtra;
    private LocalDateTime documentData;
    private LocalDateTime documentExpiryData;
    private String documentNote;
    private int documentStatus;
    private boolean paymentResidue;
    private boolean generateBillet;
    private String financialGroupId;
    private String financialSubGroupId;
    private boolean inactive;
    private long userChange;
    private LocalDateTime creationDate;
    private double paymentValue;
    private long documentTransferId;
    private int bankAccountOriginId;
    private int financialCostCenterId;
    private double totalDebitOrigin;
    private int residue;
    private long documentParentIdOrigin;
    private long documentIdOrigin;
    private String documentsKeys;
    private long documentTransactionId;
    
    public FinancialDocumentDTO() 
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

	public final boolean isCredit()
	{
		return this.credit;
	}

	public final void setCredit(final boolean credit) 
	{
		this.credit = credit;
	}

	public final long getCompanyId() 
	{
		return this.companyId;
	}

	public final void setCompanyId(final long companyId) 
	{
		this.companyId = companyId;
	}

	public final long getProviderId()
	{
		return this.providerId;
	}

	public final void setProviderId(final long providerId)
	{
		this.providerId = providerId;
	}

	public final int getBankAccountId() 
	{
		return this.bankAccountId;
	}

	public final void setBankAccountId(final int bankAccountId) 
	{
		this.bankAccountId = bankAccountId;
	}

	public final int getDocumentType()
	{
		return this.documentType;
	}

	public final void setDocumentType(final int documentType) 
	{
		this.documentType = documentType;
	}

	public final String getDocumentNumber()
	{
		return this.documentNumber;
	}

	public final void setDocumentNumber(final String documentNumber)
	{
		this.documentNumber = documentNumber;
	}

	public final double getDocumentValue() 
	{
		return this.documentValue;
	}

	public final void setDocumentValue(final double documentValue)
	{
		this.documentValue = documentValue;
	}

	public final String getDocumentNote() 
	{
		return this.documentNote;
	}

	public final void setDocumentNote(final String documentNote)
	{
		this.documentNote = documentNote;
	}

	public final int getDocumentStatus()
	{
		return this.documentStatus;
	}

	public final void setDocumentStatus(final int documentStatus) 
	{
		this.documentStatus = documentStatus;
	}

	public final double getDocumentDiscount() 
	{
		return this.documentDiscount;
	}

	public final void setDocumentDiscount(final double documentDiscount) 
	{
		this.documentDiscount = documentDiscount;
	}

	public final double getDocumentExtra()
	{
		return this.documentExtra;
	}

	public final void setDocumentExtra(final double documentExtra) 
	{
		this.documentExtra = documentExtra;
	}

	public final LocalDateTime getDocumentData() 
	{
		return this.documentData;
	}

	public final void setDocumentData(final LocalDateTime documentData) 
	{
		this.documentData = documentData;
	}

	public final LocalDateTime getDocumentExpiryData() 
	{
		return this.documentExpiryData;
	}

	public final void setDocumentExpiryData(final LocalDateTime documentExpiryData) 
	{
		this.documentExpiryData = documentExpiryData;
	}
	
	public final boolean isGenerateBillet() 
	{
		return this.generateBillet;
	}

	public final void setGenerateBillet(final boolean generateBillet)
	{
		this.generateBillet = generateBillet;
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

	public final boolean isInactive()
	{
		return this.inactive;
	}

	public final void setInactive(final boolean inactive)
	{
		this.inactive = inactive;
	}

	public final long getUserChange()
	{
		return this.userChange;
	}

	public final void setUserChange(final long userChange) 
	{
		this.userChange = userChange;
	}

	public final LocalDateTime getCreationDate() 
	{
		return this.creationDate;
	}

	public final void setCreationDate(final LocalDateTime creationDate) 
	{
		this.creationDate = creationDate;
	}    
	
	public final boolean isPaymentResidue() 
	{
		return this.paymentResidue;
	}
	
	public final void setPaymentResidue(final boolean paymentResidue) 
	{
		this.paymentResidue = paymentResidue;
	}

	public final long getDocumentId() 
	{
		return this.documentId;
	}

	public final void setDocumentId(final long documentId) 
	{
		this.documentId = documentId;
	}

	public final long getDocumentParentId() 
	{
		return this.documentParentId;
	}

	public final void setDocumentParentId(final long documentParentId)
	{
		this.documentParentId = documentParentId;
	}

	public final int getMovementType()
	{
		return this.movementType;
	}

	public final void setMovementType(final int movementType)
	{
		this.movementType = movementType;
	}

	public final LocalDateTime getPaymentDate() 
	{
		return this.paymentDate;
	}

	public final void setPaymentDate(final LocalDateTime paymentDate)
	{
		this.paymentDate = paymentDate;
	}
	
	public final LocalDateTime getPaymentExpiryDate() 
	{
		return this.paymentExpiryDate;
	}

	public final void setPaymentExpiryDate(final LocalDateTime paymentExpiryDate)
	{
		this.paymentExpiryDate = paymentExpiryDate;
	}

	public final int getPaymentStatus() 
	{
		return this.paymentStatus;
	}

	public final void setPaymentStatus(final int paymentStatus) 
	{
		this.paymentStatus = paymentStatus;
	}
	
	public final double getPaymentValue() 
	{
		return this.paymentValue;
	}

	public final void setPaymentValue(final double paymentValue)
	{
		this.paymentValue = paymentValue;
	}

	public final long getDocumentTransferId() {
		return this.documentTransferId;
	}

	public final void setDocumentTransferId(final long documentTransferId) {
		this.documentTransferId = documentTransferId;
	}
	
	public final int getBankAccountOriginId() 
	{
		return this.bankAccountOriginId;
	}

	public final void setBankAccountOriginId(final int bankAccountOriginId) 
	{
		this.bankAccountOriginId = bankAccountOriginId;
	}

	public final int getFinancialCostCenterId() {
		return this.financialCostCenterId;
	}

	public final void setFinancialCostCenterId(final int financialCostCenterId) {
		this.financialCostCenterId = financialCostCenterId;
	}

	public final double getTotalDebitOrigin() {
		return this.totalDebitOrigin;
	}

	public final void setTotalDebitOrigin(final double totalDebitOrigin) {
		this.totalDebitOrigin = totalDebitOrigin;
	}

	public final int isResidue() {
		return this.residue;
	}

	public final void setResidue(final int residue) {
		this.residue = residue;
	}
	
	public final long getDocumentParentIdOrigin() {
		return this.documentParentIdOrigin;
	}

	public final void setDocumentParentIdOrigin(final long documentParentIdOrigin) {
		this.documentParentIdOrigin = documentParentIdOrigin;
	}

	public final long getDocumentIdOrigin() {
		return this.documentIdOrigin;
	}

	public final void setDocumentIdOrigin(final long documentIdOrigin) {
		this.documentIdOrigin = documentIdOrigin;
	}

	public final String getDocumentsKeys() {
		return this.documentsKeys;
	}

	public final void setDocumentsKeys(final String documentsKeys) {
		this.documentsKeys = documentsKeys;
	}
	
	public final long getDocumentTransactionId() {
		return this.documentTransactionId;
	}

	public final void setDocumentTransactionId(final long documentTransactionId) {
		this.documentTransactionId = documentTransactionId;
	}

	public void addSumRestTransferValue(final double sobra) {
		this.documentValue += sobra;
		this.paymentValue += sobra;
	}
}