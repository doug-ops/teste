package com.manager.systems.web.financial.cash.flow.grouping.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CashFlowFinancialGroupingReportItemDTO implements Serializable {

	private static final long serialVersionUID = -3428656088874587986L;
	
	private long key;
	private int movementTypeId;
	private String movementDescription;
	private long companyId;
	private String companyDescription;
	private int weekYear;
	private int monthYear;
	private String dateFrom;
	private String dateTo;
	private String creationDate;
	private String paymentDate;
	private long paymentDateLong;
	private long documentParentId;
	private long documentId;
	private double documentValue;
	private long providerId;
	private String providerDescription;
	private String financialGroupId;
	private String financialGroupDescription;
	private String financialSubGroupId;
	private String financialSubGroupDescription;
	private long bankAccountId;
	private String bankAccountDescription;
	private long bankAccountOriginId;
	private String bankAccountOriginDescription;
	private String documentNote;
	private String documentDescription;
	private int documentType;
	private boolean transactionSameCompany;
	
	//0 - Day - 1 - Week, 2 - Month
	public void populateKey(final int groupingType) {
		if(groupingType == 0) {			
			this.key = this.paymentDateLong;
		} else if(groupingType == 1) {
			this.key = this.weekYear;
		} else if(groupingType == 2) {
			this.key = this.monthYear;
		}
	}
	
	public String getProviderOrDocumentNote() {
		String result = this.providerDescription;
		if("NAO INFORMADO".equalsIgnoreCase(this.providerDescription)) {
			result = documentDescription;
		}		
		return result;
	}
}
