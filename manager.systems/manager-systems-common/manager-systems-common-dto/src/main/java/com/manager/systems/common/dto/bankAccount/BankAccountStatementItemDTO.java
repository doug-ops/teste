package com.manager.systems.common.dto.bankAccount;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountStatementItemDTO {

	private long documentParentId;
	private long documentId;
	private String documentDate;
	private long documentDateLong;
	private long bankAccountId;
	private String banAccountDescription;
	private long providerId;
	private String providerDescription;
	private String documentNote;
	private double documentValue;
	private long companyId;
	private String companyDescription;
	private String movementDestint;
	private String bankAccountOrigin;
	private String bankAccountDestiny;
	private String bankAccountOriginDescription;
	private String bankAccountDestinyDescription;
	private boolean isInput;
	private int bankAccountOriginId;
	private double finalBankAccountAvaliable;
	private int documentType;
	private boolean isGroupMovement;
	private String typeMovementDescription;
	private String movementDescription;
	
	public BankAccountStatementItemDTO() {
		super();
	}
	
	public String getDebitString() {
		String result = ConstantDataManager.BLANK;
		if(this.documentValue<0)
		{
			result =  StringUtils.formatDecimalValue(this.documentValue);			
		}
		return result;
	}
	
	public String getCreditString() {
		String result = ConstantDataManager.BLANK;
		if(this.documentValue>-1)
		{
			result =  StringUtils.formatDecimalValue(this.documentValue);			
		}
		return result;
	}

	public String getBankAccountLabel()
	{
		final StringBuilder label = new StringBuilder();
		label.append(this.banAccountDescription);
		label.append(ConstantDataManager.SPACE);
		label.append(ConstantDataManager.PARENTESES_LEFT);
		label.append(this.bankAccountId);
		label.append(ConstantDataManager.PARENTESES_RIGHT);
		return label.toString();
	}
	
	public void populateDataDocumentNote() {
		if((this.documentType == 1 || this.documentType == 4) && isGroupMovement) {
			this.documentNote = "ORIGEM >> MOVIMENTO: " + this.documentParentId;
		}else if(this.documentType == 3 && this.documentNote.indexOf("TRANFERÊNCIA FEC. CAIXA") > -1) {
			String result = this.documentNote.replace("TRANFERÊNCIA FEC. CAIXA >> EMPRESA: (", ConstantDataManager.BLANK);	
			 final int indexOfParenteses =  result.indexOf("(");
			 if(indexOfParenteses > 01) {
				 result = result.substring(0, indexOfParenteses -1); 	
			 }			 
			this.documentNote = "TRANFERÊNCIA FEC. CAIXA: EMPRESA: " + result;
		}  else if(this.documentType == 2 && this.documentNote.indexOf("DESPESA") > -1) {
			final String[] documentNoteArray = this.documentNote.split(">>");
			if(documentNoteArray.length == 5) {
				this.documentNote = "DESPESA: " + documentNoteArray[4];
			}
		} else if(this.documentType == 3 && this.documentNote.indexOf("LANCAMENTO") > -1) {
			final String[] documentNoteArray = this.documentNote.split(">>");
			if(documentNoteArray.length == 5) {
				this.documentNote = "LANÇAMENTO: " + documentNoteArray[4];
			}
		} else if(this.documentType == 2 && this.documentNote.indexOf("LANCAMENTO") > -1) {
			final String[] documentNoteArray = this.documentNote.split(">>");
			if(documentNoteArray.length == 5) {
				this.documentNote = "LANÇAMENTO: " + documentNoteArray[4];
			}
		}
		else if(this.documentType == 2 && this.documentNote.indexOf("TRANFERENCIA") > -1) {
			final String[] documentNoteArray = this.documentNote.split(">>");
			if(documentNoteArray.length == 5) {
				this.documentNote = "TRANFERÊNCIA: " + documentNoteArray[4].trim();
			}
		}
	}
}