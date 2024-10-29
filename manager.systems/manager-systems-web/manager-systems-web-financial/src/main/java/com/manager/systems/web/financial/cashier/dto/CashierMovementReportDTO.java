/*
 * Date create 18/08/2023.
 */
package com.manager.systems.web.financial.cashier.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CashierMovementReportDTO implements Serializable {

	private static final long serialVersionUID = 5676789815273206455L;	
	
	private String title;
	private String period;
	private boolean isAnalitc;
	
	private Map<String, Map<String, CashierMovementReportItemDTO>> mapItem;
	
	public void addItem(final CashierMovementReportItemDetailDTO detail) {
		if(this.mapItem == null) {
			this.mapItem = new TreeMap<String, Map<String, CashierMovementReportItemDTO>>();
		}
		
		String mapItemKey = String.valueOf(detail.getWeekYear());
		if(this.isAnalitc) {
			mapItemKey += ConstantDataManager.UNDERSCORE;
			mapItemKey += detail.getCompanyId();
		}
		
		Map<String, CashierMovementReportItemDTO> mapItemLocal = this.mapItem.get(mapItemKey);
		if(mapItemLocal == null) {
			mapItemLocal = new TreeMap<String, CashierMovementReportItemDTO>();
		}
		
		String keyItem = StringUtils.padLeft(String.valueOf(detail.getMovementTypeId()), 2, "0");				
		keyItem += ConstantDataManager.UNDERSCORE;
		keyItem += String.valueOf(detail.getGroupType());
		keyItem += ConstantDataManager.UNDERSCORE;
		keyItem += String.valueOf(detail.getWeekYear());
		
		CashierMovementReportItemDTO item = mapItemLocal.get(keyItem);
		
		if(item == null) {
			item = CashierMovementReportItemDTO.builder()
					.weekYear(detail.getWeekYear())
					.dateFrom(detail.getDateFrom())
					.dateTo(detail.getDateTo())
					.movementTypeId(detail.getMovementTypeId())
					.movementDescription(detail.getMovementDescription())
					.groupType(detail.getGroupType())
					.orderItem(detail.getOrderItem())
					.build();
		}
		
		item.addDetail(detail);	
		mapItemLocal.put(keyItem, item);
		
		this.mapItem.put(mapItemKey, mapItemLocal);
	}
	
	public void calculateTotals() {
		/**
		Map<String, Map<String, CashierMovementReportItemDTO>> acumulatedMap = null;
		if(count == 0) {
			acumulatedMap = this.mapItem.entrySet()
	        .parallelStream().filter(x-> x.getKey() > this.mapItem.)
	        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
		}

		
		final CashierMovementReportItemDTO acumulated = CashierMovementReportItemDTO.builder().build();
		acumulated.setGroupType(3);
		acumulated.setMovementTypeId(14);
		*/
		

		double pendingBeforeTotal = 0;
		double duplicatesTotal = 0;
		double expenseStoreTotal = 0;
		double creditTotal = 0;
					
		double expenseSoftwareTotal = 0;
		double expenseTotal = 0;
		double cashTotal = 0;
		double boysTotal = 0;
		double pendingActualTotal = 0;
		double debitTotal = 0;
		
		double balanceCashTotal = 0;
		double balanceWeekTotal = 0;

		double acumulatedBalanceWeek = 0;
		
		String lastKey = null;
		for(final Map.Entry<String, Map<String, CashierMovementReportItemDTO>> entryWeek : this.mapItem.entrySet()) {	
			double pendingBefore = 0;
			double duplicates = 0;
			double expenseStore = 0;
			double credit = 0;
						
			double expenseSoftware = 0;
			double expense = 0;
			double cash = 0;
			double boys = 0;
			double pendingActual = 0;
			double debit = 0;
			
			double balanceCash = 0;
			double balanceWeek = 0;
			
			for(final Map.Entry<String, CashierMovementReportItemDTO> entryWeekItem : entryWeek.getValue().entrySet()) {
				switch (entryWeekItem.getValue().getMovementTypeId()) {
				case 1:
					pendingBefore = entryWeekItem.getValue().getTotal();
					break;
				case 2:
					duplicates = entryWeekItem.getValue().getTotal();
					break;
				case 3:
					expenseStore = entryWeekItem.getValue().getTotal();
					break;
				case 4:
					credit = (pendingBefore + duplicates + expenseStore);
					entryWeekItem.getValue().setTotal(credit);
					break;
				case 5:
					expenseSoftware = entryWeekItem.getValue().getTotal();
					break;
				case 6:
					expense = entryWeekItem.getValue().getTotal();
					break;
				case 7:
					cash = entryWeekItem.getValue().getTotal();
					break;
				case 8:
					boys = entryWeekItem.getValue().getTotal();
					break;
				case 9:
					pendingActual = entryWeekItem.getValue().getTotal();
					break;		
				case 10:
					debit = (expenseSoftware + expense + cash + boys + pendingActual);
					entryWeekItem.getValue().setTotal(debit);
					break;
				case 11:
					balanceCash = (credit - debit);
					entryWeekItem.getValue().setTotal(balanceCash);
					break;
				case 12:
					balanceWeek = (balanceCash + boys);
					entryWeekItem.getValue().setTotal(balanceWeek);
					break;
				case 13:
					acumulatedBalanceWeek += balanceWeek;
					entryWeekItem.getValue().setTotal(acumulatedBalanceWeek);
					break;
				default:
					break;
				}
			}
			
			pendingBeforeTotal += pendingBefore;
			duplicatesTotal += duplicates;
			expenseStoreTotal += expenseStore;
						
			expenseSoftwareTotal += expenseSoftware;
			expenseTotal += expense;
			cashTotal += cash;
			boysTotal += boys;
			pendingActualTotal += pendingActual;
			
			lastKey = entryWeek.getKey();
		}
		
		if(lastKey != null) {
			
			final String lastKeyMap = lastKey;				
			final Map<String, Map<String, CashierMovementReportItemDTO>> acumulatedMapCopy = this.mapItem.entrySet()
		        .parallelStream().filter(x-> x.getKey().equalsIgnoreCase(lastKeyMap))
		        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			
			final Map<String, CashierMovementReportItemDTO> acumulatedMap = new TreeMap<>();
			for(final Map.Entry<String, Map<String, CashierMovementReportItemDTO>> entryWeek : acumulatedMapCopy.entrySet()) {	
				for(final Map.Entry<String, CashierMovementReportItemDTO> entryWeekItem : entryWeek.getValue().entrySet()) {
					
					String keyItem = StringUtils.padLeft(String.valueOf(entryWeekItem.getValue().getMovementTypeId()), 2, "0");				
					keyItem += ConstantDataManager.UNDERSCORE;
					keyItem += String.valueOf(entryWeekItem.getValue().getGroupType());
					keyItem += ConstantDataManager.UNDERSCORE;
					keyItem += String.valueOf(entryWeekItem.getValue().getWeekYear()+"_A");
					
					final CashierMovementReportItemDTO item = CashierMovementReportItemDTO.builder()
							.isAcumulated(true)
							.movementTypeId(entryWeekItem.getValue().getMovementTypeId())
							.movementDescription(entryWeekItem.getValue().getMovementDescription())
							.groupType(entryWeekItem.getValue().getGroupType())
							.orderItem(entryWeekItem.getValue().getOrderItem())
							.total(0)
							.build();
					
					switch (entryWeekItem.getValue().getMovementTypeId()) {
					case 1:
						item.setTotal(pendingBeforeTotal);
						break;
					case 2:
						item.setTotal(duplicatesTotal);
						break;
					case 3:
						item.setTotal(expenseStoreTotal);
						break;
					case 4:
						creditTotal = (pendingBeforeTotal + duplicatesTotal + expenseStoreTotal);
						item.setTotal(creditTotal);
						break;
					case 5:
						item.setTotal(expenseSoftwareTotal);
						break;
					case 6:
						item.setTotal(expenseTotal);
						break;
					case 7:
						item.setTotal(cashTotal);
						break;
					case 8:
						item.setTotal(boysTotal);
						break;
					case 9:
						item.setTotal(pendingActualTotal);
						break;		
					case 10:
						debitTotal = (expenseSoftwareTotal + expenseTotal + cashTotal + boysTotal + pendingActualTotal);
						item.setTotal(debitTotal);
						break;
					case 11:
						balanceCashTotal = (creditTotal - debitTotal);
						item.setTotal(balanceCashTotal);
						break;
					case 12:
						balanceWeekTotal = (balanceCashTotal + boysTotal);
						item.setTotal(balanceWeekTotal);
						break;
					case 13:
						item.setTotal(acumulatedBalanceWeek);
						break;
					default:
						break;
					}
					
					acumulatedMap.put(keyItem, item);
				}
			}
			
			this.mapItem.put(lastKeyMap+"_A", acumulatedMap);
			
		}
	}
}
