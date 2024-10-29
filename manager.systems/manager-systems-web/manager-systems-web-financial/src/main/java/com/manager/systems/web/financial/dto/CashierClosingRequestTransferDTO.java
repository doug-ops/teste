package com.manager.systems.web.financial.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashierClosingRequestTransferDTO implements Serializable {

	private static final long serialVersionUID = -3419114217092411515L;
	
	private long documentId;
	private long documentParentId;
	private String documentNote;
	private double documentValue;
	private String initialDate;
	private String finalDate;
	private String paymentDate;
}