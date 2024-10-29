/*
 * Date create 28/10/2023.
 */
package com.manager.systems.web.financial.cash.flow.grouping.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
public class CashFlowFinancialGroupingReportDTO implements Serializable {
	
	private static final long serialVersionUID = -4751154515665542026L;
	
	private String title;
	private String period;
	private int groupingType; //0 - Day - 1 - Week, 2 - Month
	private Map<Long, String> keysGroupingType;
	private int subGroupingType; //0 - Total, 1 - Operator
	private int reportType; //0 - Previsto e Realizado, 1 - Previsto, 2 - Realizado
	private int movementType; // 0 - Todos, 1 - Faturamento Loja, 2 - Pagamento, 3 - Recebimento 
	private List<CashFlowFinancialGroupingCompanyDTO> companys;
	private List<CashFlowFinancialGroupingUserDTO> users;
	private String bankAccountNames;
	private boolean isAnalitic;
	private List<Integer> typeDocuments;
	
	private double incomeExpected;
	private double incomeFulfilled;
	private double paymentExpected;
	private double paymentFulfilled;
	private double transferCredit;
	private double transferDebit;
	
	private Map<String, CashFlowFinancialGroupingItemDTO> incomeItems;
	private Map<String, CashFlowFinancialGroupingItemDTO> paymentItems;
	private Map<String, CashFlowFinancialGroupingItemDTO> transferItems;
	
	public void initializaData(final CashFlowFinancialGroupingFilterDTO filter) {
		if(this.keysGroupingType == null) {
			this.keysGroupingType = new TreeMap<>();
		}
		
		this.groupingType = filter.getGroupingType();
		this.keysGroupingType = filter.getKeys();
		
		if(this.incomeItems == null) {
			this.incomeItems = new TreeMap<>();
		}
		
		if(this.paymentItems == null) {
			this.paymentItems = new TreeMap<>();
		}
		
		if(this.transferItems == null) {
			this.transferItems = new TreeMap<>();
		}
	}
	
	public void addItem(final CashFlowFinancialGroupingReportItemDTO item) {
		
		final String key = item.getFinancialGroupId();
		if(item.getMovementTypeId() == 1) {
			this.incomeExpected += item.getDocumentValue();		
			
			CashFlowFinancialGroupingItemDTO value = this.incomeItems.get(key);
			if(value == null) {
				value = CashFlowFinancialGroupingItemDTO.builder().keysGroupingType(this.keysGroupingType).build();
				value.setGrouping(item.getFinancialGroupId());
				value.setGroupingDescription(item.getFinancialGroupDescription());
				value.initializaData(item.getMovementTypeId());				
			}
			
			value.addItem(item);
			this.incomeItems.put(key, value);
			
		} else if(item.getMovementTypeId() == 2) {
			this.incomeFulfilled += item.getDocumentValue();
			
			CashFlowFinancialGroupingItemDTO value = this.incomeItems.get(key);
			if(value == null) {
				value = CashFlowFinancialGroupingItemDTO.builder().keysGroupingType(this.keysGroupingType).build();
				value.setGrouping(item.getFinancialGroupId());
				value.setGroupingDescription(item.getFinancialGroupDescription());
				value.initializaData(item.getMovementTypeId());
			}
			
			value.addItem(item);
			this.incomeItems.put(key, value);
			
		} else if(item.getMovementTypeId() == 3) {
			this.paymentFulfilled += item.getDocumentValue();
			
			CashFlowFinancialGroupingItemDTO value = this.paymentItems.get(key);
			if(value == null) {
				value = CashFlowFinancialGroupingItemDTO.builder().keysGroupingType(this.keysGroupingType).build();
				value.setGrouping(item.getFinancialGroupId());
				value.setGroupingDescription(item.getFinancialGroupDescription());
				value.initializaData(item.getMovementTypeId());
			}
			
			value.addItem(item);
			this.paymentItems.put(key, value);
			
		} else if(item.getMovementTypeId() == 4) {
			this.paymentExpected += item.getDocumentValue();
			
			CashFlowFinancialGroupingItemDTO value = this.paymentItems.get(key);
			if(value == null) {
				value = CashFlowFinancialGroupingItemDTO.builder().keysGroupingType(this.keysGroupingType).build();
				value.setGrouping(item.getFinancialGroupId());
				value.setGroupingDescription(item.getFinancialGroupDescription());
				value.initializaData(item.getMovementTypeId());
			}
			
			value.addItem(item);
			this.paymentItems.put(key, value);
			
		} else if(item.getMovementTypeId() == 5) {
			if(item.getDocumentValue() < 0) {
				this.transferDebit += item.getDocumentValue();				
			} else {
				this.transferCredit += item.getDocumentValue();
			}
			
			CashFlowFinancialGroupingItemDTO value = this.transferItems.get(key);
			if(value == null) {
				value = CashFlowFinancialGroupingItemDTO.builder().keysGroupingType(this.keysGroupingType).build();
				value.setGrouping(item.getFinancialGroupId());
				value.setGroupingDescription(item.getFinancialGroupDescription());
				value.initializaData(item.getMovementTypeId());
			}
			
			value.addItem(item);
			this.transferItems.put(key, value);
			
		}
	}
	
	public void addCompany(final CashFlowFinancialGroupingCompanyDTO company) {
		if(this.companys == null) {
			this.companys = new ArrayList<>();			
		}		
		this.companys.add(company);
	}
	
	public void addUser(final CashFlowFinancialGroupingUserDTO user) {
		if(this.users == null) {
			this.users = new ArrayList<>();			
		}		
		this.users.add(user);
	}
	
	public String getCompanysDescriptionFilter() {
		return this.companys.parallelStream().map(CashFlowFinancialGroupingCompanyDTO::getCompanyDescription).collect(Collectors.joining(ConstantDataManager.VIRGULA_STRING + ConstantDataManager.SPACE));
	}
	
	public String getUsersDescriptionFilter() {
		return this.users.parallelStream().map(CashFlowFinancialGroupingUserDTO::getUserName).collect(Collectors.joining(ConstantDataManager.VIRGULA_STRING + ConstantDataManager.SPACE));
	}
	
	public float getReportMarginTop() {
		//return this.companys.size() < 4 ? 40 : (45 + (this.companys.size() / 3) * 3);
		return 40;
	}
}