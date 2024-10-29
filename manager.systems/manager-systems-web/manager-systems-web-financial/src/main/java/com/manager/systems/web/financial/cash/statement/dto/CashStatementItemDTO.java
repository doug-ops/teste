/*
 * Date create 04/07/2023.
 */
package com.manager.systems.web.financial.cash.statement.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CashStatementItemDTO implements Serializable {

	private static final long serialVersionUID = -3626071478700389902L;
	
	private long companyId;
	private String companyDescription;
	private String paymentDate;
	private long paymentDateLong;
	private String dateFrom;
	private String dateTo;
	private int weekYear;
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
	private boolean transactionSameCompany;
	private boolean profitDistribution;
	private long documentParentId;
	private int documentType;
	private String documentNote;
	private int movementTypeId;
	private int groupTypeId;
	private double documentValue;
}
