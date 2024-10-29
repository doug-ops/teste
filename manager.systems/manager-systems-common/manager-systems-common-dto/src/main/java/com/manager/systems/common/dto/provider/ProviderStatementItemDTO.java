/*
 * 28/06/2023.
 */
package com.manager.systems.common.dto.provider;

import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderStatementItemDTO {

	private long documentParentId;
	private long documentId;
	private String documentDate;
	private long documentDateLong;
	private long providerId;
	private String providerDescription;
	private double documentValue;
	private int companyId;
	private String companyDescription;
	private int documentType;
	private boolean isGroupMovement;
	private String typeMovementDescription;
	private String movementDescription;
	private String financialGroupId;
	private String financialGroupDescription;
	private String financialSubGroupId;
	private String financialSubGroupDescription;
	private double initialProviderAvaliable;
	private double finalProviderAvaliable;
	private double totalDebit;
	private double totalCredit;
	private double totalBalance;
	private List<ProviderStatementItemDTO> itens = new ArrayList<ProviderStatementItemDTO>();
	
	public ProviderStatementItemDTO() {
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
	
	public final void addDocumentValue(final double documentValue) {
		if(documentValue<0) {
			this.totalDebit += documentValue;
		}
		else {
			this.totalCredit+=documentValue;
		}
		this.calculateTotalBalance();
	}
	
	public final void calculateTotalBalance()
	{
		this.totalBalance = (this.totalDebit + this.totalCredit);
	}
}