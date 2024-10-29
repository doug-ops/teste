/*
 * Date create 28/10/2023.
 */
package com.manager.systems.web.financial.cash.flow.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.manager.systems.common.utils.ConstantDataManager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CashFlowReportDTO implements Serializable {
	private static final long serialVersionUID = 1188041484033862956L;
	
	private String title;
	private String period;
	private int groupBy;
	private List<CashFlowCompanyDTO> companys;
	private List<CashFlowUserDTO> users;
	private String bankAccountNames;
	private boolean isAnalitic;
	
	private CashFlowIncomeFulfilledDTO incomeFulfilled;
	private CashFlowIncomeExpectedDTO incomeExpected;
	private CashFlowPaymentFulfilledDTO paymentFulfilled;
	private CashFlowPaymentExpectedDTO paymentExpected;
	private CashFlowBankAccountTranferDTO bankAccountTranfer;
	
	public void addItem(final CashFlowReportItemDTO item) {
		if(item.getMovementTypeId() == 1) {			
			this.incomeExpected.addItem(item);
		} else if(item.getMovementTypeId() == 2) {			
			this.incomeFulfilled.addItem(item);
		} else if(item.getMovementTypeId() == 3) {			
			this.paymentFulfilled.addItem(item);
		} else if(item.getMovementTypeId() == 4) {
			this.paymentExpected.addItem(item);
		} else if(item.getMovementTypeId() == 5) {			
			this.bankAccountTranfer.addItem(item);
		}
	}
	
	public void initializeData() {
		this.incomeExpected = CashFlowIncomeExpectedDTO.builder().isAnalitic(this.isAnalitic).build();
		this.incomeFulfilled = CashFlowIncomeFulfilledDTO.builder().isAnalitic(this.isAnalitic).build();
		this.paymentFulfilled = CashFlowPaymentFulfilledDTO.builder().isAnalitic(this.isAnalitic).build();
		this.paymentExpected = CashFlowPaymentExpectedDTO.builder().isAnalitic(this.isAnalitic).build();
		this.bankAccountTranfer = CashFlowBankAccountTranferDTO.builder().isAnalitic(this.isAnalitic).build();
		this.bankAccountTranfer.initializeData();
	}
	
	public void addCompany(final CashFlowCompanyDTO company) {
		if(this.companys == null) {
			this.companys = new ArrayList<>();			
		}		
		this.companys.add(company);
	}
	
	public void addUser(final CashFlowUserDTO user) {
		if(this.users == null) {
			this.users = new ArrayList<>();			
		}		
		this.users.add(user);
	}
	
	public String getCompanysDescriptionFilter() {
		return this.companys.parallelStream().map(CashFlowCompanyDTO::getCompanyDescription).collect(Collectors.joining(ConstantDataManager.VIRGULA_STRING + ConstantDataManager.SPACE));
	}
	
	public String getUsersDescriptionFilter() {
		return this.users.parallelStream().map(CashFlowUserDTO::getUserName).collect(Collectors.joining(ConstantDataManager.VIRGULA_STRING + ConstantDataManager.SPACE));
	}
	
	public float getReportMarginTop() {
		return this.companys.size() < 4 ? 40 : (45 + (this.companys.size() / 3) * 3);
	}
}