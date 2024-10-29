/*
 * Date create 29/03/2024.
 */
package com.manager.systems.web.financial.cashier.close.preview.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CashingCloseUserWeekMovementCompanyLaunchDTO implements Serializable {

	private static final long serialVersionUID = 8877813961995228520L;
	
	private long id;
	private int count;
	private long cashierClosingId;
	private int type;
	private String cashingClosingStatus;
	private long companyId;
	private int bankAccountExpense;
	private String bankAccountDescription;
	private String paymentExpiryDateExpense;
	private double documentValueExpense;
	private int documentStatusExpense;
	private long providerExpense;
	private String providerDescription;
	private String financialGroupExpense;
	private String financialGroupDescription;
	private String financialSubGroupExpense;
	private String financialSubGroupDescription;
	private String documentNoteExpense;
	private boolean credit;
	private long userChange;
	private boolean inactive;
	private long userOperator;
	
	public String convertPaymentExpiryDateExpense() {
		final String[] paymentExpiryDateExpense = this.paymentExpiryDateExpense.split(com.manager.systems.common.utils.ConstantDataManager.BARRA);
		return (paymentExpiryDateExpense[2]+com.manager.systems.common.utils.ConstantDataManager.TRACO+paymentExpiryDateExpense[1]+com.manager.systems.common.utils.ConstantDataManager.TRACO+paymentExpiryDateExpense[0]+com.manager.systems.common.utils.ConstantDataManager.SPACE+com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO);	
	}	
}