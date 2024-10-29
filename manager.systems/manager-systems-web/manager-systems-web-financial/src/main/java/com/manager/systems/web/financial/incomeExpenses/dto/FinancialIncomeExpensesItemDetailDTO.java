/*
 * Date create 24/08/2023.
 */
package com.manager.systems.web.financial.incomeExpenses.dto;

import java.io.Serializable;

import com.manager.systems.common.utils.ConstantDataManager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class FinancialIncomeExpensesItemDetailDTO implements Serializable{

	private static final long serialVersionUID = 3406860105464792213L;
	
		private int movementTypeId;
		private String movementDescription;
		private long companyId;
		private String companyDescription;
		private int weekYear;
		private String dateFrom;
		private String dateTo;
		private String creationDate;
		private String paymentDate;
		private long paymentDateLong;
		private long documentParentId;
		private long documentId;
		private double documentValue;
		private long providerId;
		private String providerDescription;
		private String financialGroupId;
		private String financialGroupDescription;
		private String financialSubGroupId;
		private String financialSubGroupDescription;
		private long bankAccountId;
		private String bankAccountDescription;
		private long bankAccountOriginId;
		private String bankAccountOriginDescription;
		private String documentNote;
		private String documentDescription;
		private int documentType;
		private boolean transactionSameCompany;
		private boolean profitDistribution;
		
		public String getWeekDescription() {
			final StringBuilder result = new StringBuilder();
			result.append(String.valueOf(this.weekYear).substring(4, 6));
			result.append(ConstantDataManager.BARRA);
			result.append(String.valueOf(this.weekYear).substring(0, 4));
			result.append(ConstantDataManager.SPACE);
			result.append(ConstantDataManager.TRACO);
			result.append(ConstantDataManager.SPACE);
			
			if(this.dateFrom != null) {
				final String[] dateFromArray = this.dateFrom.split(ConstantDataManager.TRACO);
				result.append(dateFromArray[2]);
				result.append(ConstantDataManager.BARRA);
				result.append(dateFromArray[1]);
				result.append(ConstantDataManager.BARRA);
				result.append(dateFromArray[0]);
			}
			
			result.append(ConstantDataManager.SPACE);
			result.append(ConstantDataManager.LABEL_TO);
			result.append(ConstantDataManager.SPACE);
			
			if(this.dateTo != null) {
				final String[] dateFromArray = this.dateTo.split(ConstantDataManager.TRACO);
				result.append(dateFromArray[2]);
				result.append(ConstantDataManager.BARRA);
				result.append(dateFromArray[1]);
				result.append(ConstantDataManager.BARRA);
				result.append(dateFromArray[0]);
			}
			
			return result.toString();
		}
}
