/*
 * Date create 18/03/2023
 */
package com.manager.systems.web.financial.dto;

import java.io.Serializable;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferValueRequestExpense implements Serializable {
	
	private static final long serialVersionUID = 166716064163653864L;
	
	private long companyId;
	private String bankAccountDescription;
	private int bankAccountExpense;
	private String paymentExpiryDateExpense;
	private double documentValueExpense;
	private int documentStatusExpense;
	private long providerExpense;
	private String financialGroupExpense;
	private String financialSubGroupExpense;
	private int launchType;
	private String documentNoteExpense;
	
	public String getDocumentNoteExpenseDecode() {
		String result = ConstantDataManager.BLANK;
		if(!StringUtils.isNull(this.documentNoteExpense)) {
			result = StringUtils.decodeString(this.documentNoteExpense);
		}
		return result;
	}
}