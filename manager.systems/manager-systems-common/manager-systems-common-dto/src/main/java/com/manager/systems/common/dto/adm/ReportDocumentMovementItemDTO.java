package com.manager.systems.common.dto.adm;

import java.time.LocalDateTime;

public class ReportDocumentMovementItemDTO
{
	private long id;
    private long parentId;
	private boolean credit; 
  	private long companyId;
    private long providerId; 
    private int bankAccountId;
    private int documentType;
    private String documentNumber;
    private double documentValue;
    private String documentNote;
    private int documentStatus;
    private double paymentValue;
    private double paymentDiscount;
    private double paymentExtra;
    private boolean paymentResidue;
    private LocalDateTime paymentData;
    private LocalDateTime paymentExpiryData;
    private long paymentUser;
    private long nfeNumber;
    private int nfeSerieNumber;
    private boolean generateBillet;
    private int financialGroupId;
    private int financialSubGroupId;
    private boolean inactive;
    private long userChange;
    private byte[] versionRegister;
    
    public ReportDocumentMovementItemDTO() 
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

	public final long getParentId() 
	{
		return this.parentId;
	}

	public final void setParentId(final long parentId) 
	{
		this.parentId = parentId;
	}

	public final boolean isCredit()
	{
		return credit;
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

	public final double getPaymentValue()
	{
		return this.paymentValue;
	}

	public final void setPaymentValue(final double paymentValue) 
	{
		this.paymentValue = paymentValue;
	}

	public final double getPaymentDiscount() 
	{
		return this.paymentDiscount;
	}

	public final void setPaymentDiscount(final double paymentDiscount) 
	{
		this.paymentDiscount = paymentDiscount;
	}

	public final double getPaymentExtra()
	{
		return this.paymentExtra;
	}

	public final void setPaymentExtra(final double paymentExtra) 
	{
		this.paymentExtra = paymentExtra;
	}

	public final boolean isPaymentResidue()
	{
		return this.paymentResidue;
	}

	public final void setPaymentResidue(final boolean paymentResidue) 
	{
		this.paymentResidue = paymentResidue;
	}

	public final LocalDateTime getPaymentData() 
	{
		return this.paymentData;
	}

	public final void setPaymentData(final LocalDateTime paymentData) 
	{
		this.paymentData = paymentData;
	}

	public final LocalDateTime getPaymentExpiryData() 
	{
		return this.paymentExpiryData;
	}

	public final void setPaymentExpiryData(final LocalDateTime paymentExpiryData) 
	{
		this.paymentExpiryData = paymentExpiryData;
	}

	public final long getPaymentUser() 
	{
		return this.paymentUser;
	}

	public final void setPaymentUser(final long paymentUser) 
	{
		this.paymentUser = paymentUser;
	}

	public final long getNfeNumber() 
	{
		return this.nfeNumber;
	}

	public final void setNfeNumber(final long nfeNumber) 
	{
		this.nfeNumber = nfeNumber;
	}

	public final int getNfeSerieNumber() 
	{
		return this.nfeSerieNumber;
	}

	public final void setNfeSerieNumber(final int nfeSerieNumber) 
	{
		this.nfeSerieNumber = nfeSerieNumber;
	}

	public final boolean isGenerateBillet() 
	{
		return this.generateBillet;
	}

	public final void setGenerateBillet(final boolean generateBillet)
	{
		this.generateBillet = generateBillet;
	}

	public final int getFinancialGroupId() 
	{
		return this.financialGroupId;
	}

	public final void setFinancialGroupId(final int financialGroupId)
	{
		this.financialGroupId = financialGroupId;
	}

	public final int getFinancialSubGroupId() 
	{
		return this.financialSubGroupId;
	}

	public final void setFinancialSubGroupId(final int financialSubGroupId) 
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
	
	public final byte[] getVersionRegister() 
	{
		return this.versionRegister;
	}
	
	public final void setVersionRegister(final byte[] versionRegister) 
	{
		this.versionRegister = versionRegister;
	}
}