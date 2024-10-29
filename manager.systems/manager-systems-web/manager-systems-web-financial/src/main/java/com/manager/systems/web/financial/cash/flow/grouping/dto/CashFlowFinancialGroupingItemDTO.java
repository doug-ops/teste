package com.manager.systems.web.financial.cash.flow.grouping.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import com.manager.systems.common.utils.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CashFlowFinancialGroupingItemDTO implements Serializable {

	private static final long serialVersionUID = 5736243154476471087L;
	
	private String grouping; 
	private String groupingDescription;
	
	private double incomeExpected;
	private double incomeFulfilled;
	private double paymentExpected;
	private double paymentFulfilled;
	private double transferCredit;
	private double transferDebit;
	private Map<Long, String> keysGroupingType;
		
	private Map<Long, CashFlowFinancialGroupingKeyDTO> incomeItems;
	private Map<Long, CashFlowFinancialGroupingKeyDTO> paymentItems;
	private Map<Long, CashFlowFinancialGroupingKeyDTO> transferItems;
	
	private Map<String, CashFlowFinancialSubGroupingItemDTO> incomeItemsSubGroups;
	private Map<String, CashFlowFinancialSubGroupingItemDTO> paymentItemsSubGroups;
	private Map<String, CashFlowFinancialSubGroupingItemDTO> transferItemsSubGroups;
	
	public void initializaData(final int movementTypeId) {
		
		if(this.incomeItems == null) {
			this.incomeItems = new TreeMap<>();
		}
		
		if(this.paymentItems == null) {
			this.paymentItems = new TreeMap<>();
		}
		
		if(this.transferItems == null) {
			this.transferItems = new TreeMap<>();
		}
		
		initializaDataSubGroups();
		
		for(final Map.Entry<Long, String> entry : this.keysGroupingType.entrySet()) {
			if(movementTypeId == 1) {
				this.addItem(CashFlowFinancialGroupingReportItemDTO.builder().movementTypeId(1).movementDescription("RECEBIMENTO PREVISTO").key(entry.getKey()).key(entry.getKey()).build());				
			} else if(movementTypeId == 2) {
				this.addItem(CashFlowFinancialGroupingReportItemDTO.builder().movementTypeId(2).movementDescription("RECEBIMENTO REALIZADO").key(entry.getKey()).key(entry.getKey()).build());
			} else if(movementTypeId == 4) {
				this.addItem(CashFlowFinancialGroupingReportItemDTO.builder().movementTypeId(4).movementDescription("PAGAMENTO PREVISTO").key(entry.getKey()).key(entry.getKey()).build());				
			} else if(movementTypeId == 3) {
				this.addItem(CashFlowFinancialGroupingReportItemDTO.builder().movementTypeId(3).movementDescription("PAGAMENTO REALIZADO").key(entry.getKey()).key(entry.getKey()).build());				
			} else if(movementTypeId == 5) {
				this.addItem(CashFlowFinancialGroupingReportItemDTO.builder().movementTypeId(5).movementDescription("TRANSFERENCIAS CONTAS").key(entry.getKey()).key(entry.getKey()).build());				
			}
		}
	}
	
	public void addItem(final CashFlowFinancialGroupingReportItemDTO item) {
		
		final Long key = item.getKey();
		if(item.getMovementTypeId() == 1) {
			this.incomeExpected += item.getDocumentValue();		
			
			CashFlowFinancialGroupingKeyDTO value = this.incomeItems.get(key);
			if(value == null) {
				value = CashFlowFinancialGroupingKeyDTO.builder().build();
				value.setKey(key);
			}
			
			value.addItem(item);
			this.incomeItems.put(key, value);
			
		} else if(item.getMovementTypeId() == 2) {
			this.incomeFulfilled += item.getDocumentValue();
			
			CashFlowFinancialGroupingKeyDTO value = this.incomeItems.get(key);
			if(value == null) {
				value = CashFlowFinancialGroupingKeyDTO.builder().build();
				value.setKey(key);
			}
			
			value.addItem(item);
			this.incomeItems.put(key, value);
			
		} else if(item.getMovementTypeId() == 3) {
			this.paymentFulfilled += item.getDocumentValue();
			
			CashFlowFinancialGroupingKeyDTO value = this.paymentItems.get(key);
			if(value == null) {
				value = CashFlowFinancialGroupingKeyDTO.builder().build();
				value.setKey(key);
			}
			
			value.addItem(item);
			this.paymentItems.put(key, value);
			
		} else if(item.getMovementTypeId() == 4) {
			this.paymentExpected += item.getDocumentValue();
			
			CashFlowFinancialGroupingKeyDTO value = this.paymentItems.get(key);
			if(value == null) {
				value = CashFlowFinancialGroupingKeyDTO.builder().build();
				value.setKey(key);
			}
			
			value.addItem(item);
			this.paymentItems.put(key, value);
			
		} else if(item.getMovementTypeId() == 5) {
			if(item.getDocumentValue() < 0) {
				this.transferDebit += item.getDocumentValue();				
			} else {
				this.transferCredit += item.getDocumentValue();
			}
			
			CashFlowFinancialGroupingKeyDTO value = this.transferItems.get(key);
			if(value == null) {
				value = CashFlowFinancialGroupingKeyDTO.builder().build();
				value.setKey(key);
			}
			
			value.addItem(item);
			this.transferItems.put(key, value);			
		}
		
		this.addItemSubGroup(item);
	}
	
	public void initializaDataSubGroups() {
		if(this.incomeItemsSubGroups == null) {
			this.incomeItemsSubGroups = new TreeMap<>();
		}
		
		if(this.paymentItemsSubGroups == null) {
			this.paymentItemsSubGroups = new TreeMap<>();
		}
		
		if(this.transferItemsSubGroups == null) {
			this.transferItemsSubGroups = new TreeMap<>();
		}
	}
	
	public void addItemSubGroup(final CashFlowFinancialGroupingReportItemDTO item) {
		
		final String key = item.getFinancialSubGroupId();
		if(StringUtils.isNull(key)) {
			return;
		}
		if(item.getMovementTypeId() == 1) {	
			
			CashFlowFinancialSubGroupingItemDTO value = this.incomeItemsSubGroups.get(key);
			if(value == null) {
				value = CashFlowFinancialSubGroupingItemDTO.builder().keysGroupingType(this.keysGroupingType).build();
				value.setSubGrouping(item.getFinancialSubGroupId());
				value.setSubGroupingDescription(item.getFinancialSubGroupDescription());
				value.initializaData(item.getMovementTypeId());				
			}
			
			value.addItem(item);
			this.incomeItemsSubGroups.put(key, value);
			
		} else if(item.getMovementTypeId() == 2) {			
			CashFlowFinancialSubGroupingItemDTO value = this.incomeItemsSubGroups.get(key);
			if(value == null) {
				value = CashFlowFinancialSubGroupingItemDTO.builder().keysGroupingType(this.keysGroupingType).build();
				value.setSubGrouping(item.getFinancialSubGroupId());
				value.setSubGroupingDescription(item.getFinancialSubGroupDescription());
				value.initializaData(item.getMovementTypeId());
			}
			
			value.addItem(item);
			this.incomeItemsSubGroups.put(key, value);
			
		} else if(item.getMovementTypeId() == 3) {			
			CashFlowFinancialSubGroupingItemDTO value = this.paymentItemsSubGroups.get(key);
			if(value == null) {
				value = CashFlowFinancialSubGroupingItemDTO.builder().keysGroupingType(this.keysGroupingType).build();
				value.setSubGrouping(item.getFinancialSubGroupId());
				value.setSubGroupingDescription(item.getFinancialSubGroupDescription());
				value.initializaData(item.getMovementTypeId());
			}
			
			value.addItem(item);
			this.paymentItemsSubGroups.put(key, value);
			
		} else if(item.getMovementTypeId() == 4) {			
			CashFlowFinancialSubGroupingItemDTO value = this.paymentItemsSubGroups.get(key);
			if(value == null) {
				value = CashFlowFinancialSubGroupingItemDTO.builder().keysGroupingType(this.keysGroupingType).build();
				value.setSubGrouping(item.getFinancialSubGroupId());
				value.setSubGroupingDescription(item.getFinancialSubGroupDescription());
				value.initializaData(item.getMovementTypeId());
			}
			
			value.addItem(item);
			this.paymentItemsSubGroups.put(key, value);
			
		} else if(item.getMovementTypeId() == 5) {
			if(item.getDocumentValue() < 0) {
				this.transferDebit += item.getDocumentValue();				
			} else {
				this.transferCredit += item.getDocumentValue();
			}
			
			CashFlowFinancialSubGroupingItemDTO value = this.transferItemsSubGroups.get(key);
			if(value == null) {
				value = CashFlowFinancialSubGroupingItemDTO.builder().keysGroupingType(this.keysGroupingType).build();
				value.setSubGrouping(item.getFinancialSubGroupId());
				value.setSubGroupingDescription(item.getFinancialSubGroupDescription());
				value.initializaData(item.getMovementTypeId());
			}
			
			value.addItem(item);
			this.transferItemsSubGroups.put(key, value);			
		}
	}
}