package com.manager.systems.web.financial.dto;

import java.io.Serializable;
import java.util.List;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferValueRequest implements Serializable {

	private static final long serialVersionUID = -6246749197788040106L;
	
	private String paymentExpiryDateTransf;
	private double paymentValueTransf;
	private String companyDescriptionTransf;
	private long companyTransf;
	private int bankAccountTransf;
	private String documentNoteTransf;
	private String companyDescriptionTransfDest;
	private long companyTransfDest;
	private int bankAccountTransfDest;
	private String documentNoteTransfDest;
	private int movementTypeTransfDest;
	private int launchType;
	private int documentStatusTransf;
	private int countExpense;
	private List<TransferValueRequestExpense> expenses;
	
	public String getDocumentNoteTransfDecode() {
		String result = ConstantDataManager.BLANK;
		if(!StringUtils.isNull(this.documentNoteTransf)) {
			result = StringUtils.decodeString(this.documentNoteTransf);
		}
		return result;
	}
	
	public String getDocumentNoteTransfDestDecode() {
		String result = ConstantDataManager.BLANK;
		if(!StringUtils.isNull(this.documentNoteTransfDest)) {
			result = StringUtils.decodeString(this.documentNoteTransfDest);
		}
		return result;
	}
}