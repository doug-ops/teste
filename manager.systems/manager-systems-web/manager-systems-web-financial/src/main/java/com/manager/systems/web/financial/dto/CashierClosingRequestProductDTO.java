package com.manager.systems.web.financial.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashierClosingRequestProductDTO implements Serializable {
	
	private static final long serialVersionUID = -2963721217235760747L;
	
	private long productId;
    private double inputValue;
    private double outputValue;
    private double totalValue;
    private long inputDocumentId;
    private long outputDocumentId;
    private long documentParentId;
    private String paymentDate;
}
