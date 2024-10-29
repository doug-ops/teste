/*
 * Date create 18/08/2023.
 */
package com.manager.systems.web.financial.cashier.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CashierMovementReportItemDetailDTO implements Serializable {
	
	private static final long serialVersionUID = -4565909631607817464L;
	
	private int weekYear;
	private String dateFrom;
	private String dateTo;
	private int movementTypeId;
	private String movementDescription;
	private int groupType;
	private int orderItem;
	private String creationDate;
	private String paymentDate;
	private long documentParentId;
	private long documentId;
	private double documentValue;
	private long companyId;
	private String companyDescription;
	private long providerId;
	private String providerDescription;
	private long bankAccountId;
	private long bankAccountOriginId;
	private String bankAccountOriginDescription;
	private String documentNote;
	private String documentDescription;
	private int documentType;
}