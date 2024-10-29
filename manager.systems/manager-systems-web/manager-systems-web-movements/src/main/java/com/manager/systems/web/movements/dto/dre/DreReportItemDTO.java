package com.manager.systems.web.movements.dto.dre;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DreReportItemDTO implements Serializable
{
	private static final long serialVersionUID = 8376214560768445645L;

	private int groupTypeId;
	private String descriptionGroupType;
	private String financialGroupId;
	private String descriptionFinancialGroup;
	private String financialSubGroupId;
	private String descriptionFinancialSubGroup;
	private long documentParentId;
	private long documentId;
	private String documentNote;
	private double total;
	private double percentSales;
	private int order;
	private LocalDateTime documentData;
	private double documentValue;
	private double paymentValue;	
	private boolean analitic;
	private boolean credit;
	
	public DreReportItemDTO() {
		super();
	}

	public final int getGroupTypeId() {
		return this.groupTypeId;
	}

	public final void setGroupTypeId(final int groupTypeId) {
		this.groupTypeId = groupTypeId;
	}

	public final String getDescriptionGroupType() {
		return this.descriptionGroupType;
	}

	public final void setDescriptionGroupType(final String descriptionGroupType) {
		this.descriptionGroupType = descriptionGroupType;
	}

	public final String getFinancialGroupId() {
		return this.financialGroupId;
	}

	public final void setFinancialGroupId(final String financialGroupId) {
		this.financialGroupId = financialGroupId;
	}

	public final String getDescriptionFinancialGroup() {
		return this.descriptionFinancialGroup;
	}

	public final void setDescriptionFinancialGroup(final String descriptionFinancialGroup) {
		this.descriptionFinancialGroup = descriptionFinancialGroup;
	}

	public final String getFinancialSubGroupId() {
		return this.financialSubGroupId;
	}

	public final void setFinancialSubGroupId(final String financialSubGroupId) {
		this.financialSubGroupId = financialSubGroupId;
	}

	public final String getDescriptionFinancialSubGroup() {
		return this.descriptionFinancialSubGroup;
	}

	public final void setDescriptionFinancialSubGroup(final String descriptionFinancialSubGroup) {
		this.descriptionFinancialSubGroup = descriptionFinancialSubGroup;
	}

	public final long getDocumentParentId() {
		return this.documentParentId;
	}

	public final void setDocumentParentId(final long documentParentId) {
		this.documentParentId = documentParentId;
	}

	public final long getDocumentId() {
		return this.documentId;
	}

	public final void setDocumentId(final long documentId) {
		this.documentId = documentId;
	}

	public final String getDocumentNote() {
		return this.documentNote;
	}

	public final void setDocumentNote(final String documentNote) {
		this.documentNote = documentNote;
	}

	public final double getTotal() {
		return this.total;
	}

	public final void setTotal(final double total) {
		this.total = total;
	}

	public final double getPercentSales() {
		return this.percentSales;
	}

	public final void setPercentSales(final double percentSales) {
		this.percentSales = percentSales;
	}

	public final int getOrder() {
		return this.order;
	}

	public final void setOrder(final int order) {
		this.order = order;
	}

	public final LocalDateTime getDocumentData() {
		return this.documentData;
	}

	public final void setDocumentData(final LocalDateTime documentData) {
		this.documentData = documentData;
	}
	
	public final double getDocumentValue() {
		return this.documentValue;
	}

	public final void setDocumentValue(final double documentValue) {
		this.documentValue = documentValue;
	}

	public final double getPaymentValue() {
		return this.paymentValue;
	}

	public final void setPaymentValue(final double paymentValue) {
		this.paymentValue = paymentValue;
	}	
	
	public final boolean isAnalitic() {
		return analitic;
	}

	public final void setAnalitic(final boolean analitic) {
		this.analitic = analitic;
	}
	
	public final boolean isCredit()
	{
		return this.credit;
	}
	
	public final void setCredit(final boolean credit) {
		this.credit = credit;
	}
}