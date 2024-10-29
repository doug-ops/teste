/*
 * Date create 14/02/2024.
 */
package com.manager.systems.web.financial.cashier.close.preview.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CashingCloseUserWeekMovementCompanyItemProductDTO implements Serializable {

	private static final long serialVersionUID = -2202176216772874413L;
	private long productId;
	private double inputValue;
	private double outputValue;
	private double balance;
	private long inputDocumentMovementId;
	private long outputDocumentMovementId;	
	
	public void calculateBalance() {
		this.balance = (this.inputValue + this.outputValue);
	}
}