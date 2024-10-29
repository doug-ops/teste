package com.manager.systems.web.financial.cash.flow.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CashFlowReportItemDTO implements Serializable {
	private static final long serialVersionUID = 8977268881694242330L;
	
	private int movementTypeId;
	private String movementDescription;
	private long companyId;
	private String companyDescription;
	private int weekYear;
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
	
	public String getProviderOrDocumentNote() {
		String result = this.providerDescription;
		if("NAO INFORMADO".equalsIgnoreCase(this.providerDescription)) {
			result = documentDescription;
		}		
		return result;
	}
}
