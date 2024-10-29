package com.manager.systems.web.financial.dto;

import java.io.Serializable;
import java.util.List;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashierClosingRequest implements Serializable {
	
	private static final long serialVersionUID = 676102729592229685L;
	
	private String companyDescriptionTransfDest;
	private long companyTransfDest;
	private int bankAccountTransfDest;
	private int movementTypeTransfDest;
	private int documentStatusTransfDest;
	private int paymentStatusTransfDest;
	private String paymentExpiryDateTransfDest;
	private double documentValueTransfDest;
	private String documentNoteTransfDest;
	private List<CashierClosingRequestExpense> expenses;
	
	public String getDocumentNoteTransfDestDecode() {
		String result = ConstantDataManager.BLANK;
		if(!StringUtils.isNull(this.documentNoteTransfDest)) {
			result = StringUtils.decodeString(this.documentNoteTransfDest);
		}
		return result;
	}
}