/*
 * Date create 29/03/2024.
 */
package com.manager.systems.web.financial.cashier.close.preview.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashierClosingPreviewFinishRequest implements Serializable {

	private static final long serialVersionUID = 2360506253832378421L;
	
	private long cashierClosingId;
	private long userOperator;
	private String companysIds;
	private String paymentDate;
	private long userChange;
	private boolean process;
	private List<CashingCloseUserWeekMovementCompanyLaunchDTO> launchs;
	
	public String convertPaymentDate() {
		final String[] paymentExpiryDateExpense = this.paymentDate.split(com.manager.systems.common.utils.ConstantDataManager.BARRA);
		return (paymentExpiryDateExpense[2]+com.manager.systems.common.utils.ConstantDataManager.TRACO+paymentExpiryDateExpense[1]+com.manager.systems.common.utils.ConstantDataManager.TRACO+paymentExpiryDateExpense[0]+com.manager.systems.common.utils.ConstantDataManager.SPACE+com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO);	
	}
	
}