package com.manager.systems.web.movements.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentMovementGroupFilter implements Serializable {

	private static final long serialVersionUID = 578655423976654418L;

	private long companyId;
	private long providerId;
	private String financialGroupId;
	private String financialSubGroupId;
	private int bankAccountId;
	private String documentType;
	private String documentNumber;
	private boolean credit;
	private boolean debit;
	private boolean financial;
	private boolean open;
	private boolean close;
	private boolean removed;
	private boolean transfer;
	private boolean transferHab;
	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;
	private int filterBy;
	private long userOperation;	
	private int productGroupId;
	private int productSubGroupId;
	private String userChildrensParent;
	private int financialCostCenterId;
	private boolean launch;
	private boolean inputMovement;
	private boolean outputMovement;
	private boolean duplicateMovement;
}
