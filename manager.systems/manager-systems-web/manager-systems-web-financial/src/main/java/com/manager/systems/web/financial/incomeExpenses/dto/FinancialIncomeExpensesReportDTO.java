/*
 * Date create 24/08/2023.
 */
package com.manager.systems.web.financial.incomeExpenses.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.manager.systems.common.utils.ConstantDataManager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class FinancialIncomeExpensesReportDTO implements Serializable {

	private static final long serialVersionUID = -4726848616119667687L;
	
	private String title;
	private String period;
	private double income;
	private double expenses;
	private double balance;
	private double transfInput;
	private double transfOutput;
	private double profitDistribution;
	private boolean hasProfitDistribution;
	
	private int groupBy;
	private boolean isAnalitc;	
	private int incomeExpense;
	private boolean isExpenseHaab;
	private boolean isTransfCashingClose;
	private List<Long> bankAccounts;
	private String bankAccountNames;
	private Map<Long, String> companys;
	
	private List<FinancialIncomeExpensesItemDetailDTO> items;
	private Map<Long, FinancialIncomeExpensesCompanyDTO> itemsCompanyMap;
	private Map<Long, FinancialIncomeExpensesDateDTO> itemsDateMap;
	private Map<String, FinancialIncomeExpensesCompanyDateDTO> itemsCompanyDateMap;
	private Map<Integer, FinancialIncomeExpensesWeekDTO> itemsWeekMap;
	private Map<String, FinancialIncomeExpensesCompanyWeekDTO> itemsCompanyWeekMap;
	private Map<Long, FinancialIncomeExpensesCompanyDTO> itemsOriginCompanyMap;
	private Map<String, FinancialIncomeExpensesCompanyWeekDTO> itemsOriginCompanyWeekMap;
	private Map<String, FinancialIncomeExpensesProviderDTO> itemsProviderMap;
	private Map<String, FinancialIncomeExpensesProviderDateDTO> itemsProviderDateMap;
	private Map<String, FinancialIncomeExpensesProviderWeekDTO> itemsProviderWeekMap;
	private Map<Long, FinancialIncomeExpensesTransfMonthDTO> itemsTrasnfMonthMap;
	private Map<String, FinancialIncomeExpensesFinancialGroupDTO> itemsFinancialGroupMap;
	private Map<String, FinancialIncomeExpensesFinancialGroupDateDTO> itemsFinancialGroupDateMap;
	private Map<String, FinancialIncomeExpensesFinancialGroupWeekDTO> itemsFinancialGroupWeekMap;
	private Map<String, FinancialIncomeExpensesFinancialSubGroupDTO> itemsFinancialSubGroupMap;
	private Map<String, FinancialIncomeExpensesFinancialSubGroupDateDTO> itemsFinancialSubGroupDateMap;
	private Map<String, FinancialIncomeExpensesFinancialSubGroupWeekDTO> itemsFinancialSubGroupWeekMap;
	private Map<String, FinancialIncomeExpensesBankAccountDTO> itemsIncomePerformedMap;
	private Map<String, FinancialIncomeExpensesBankAccountDTO> itemsIncomeEstimatedMap;
	private Map<String, FinancialIncomeExpensesBankAccountDTO> itemsExpensePerformedMap;
	private Map<String, FinancialIncomeExpensesBankAccountDTO> itemsExpenseEstimatedMap;
	private Map<String, FinancialIncomeExpensesBankAccountDTO> itemsTanferBankAccountMap;
	
	public void addCompany(final FinancialIncomeExpensesItemDetailDTO detail) {		
		if(this.companys == null) {
			this.companys = new TreeMap<>();
		}
		
		this.companys.put(detail.getCompanyId(), detail.getCompanyDescription());
	}
	
	public void addItemDetail(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(this.items == null) {
			this.items = new ArrayList<>();
		}
		
		if(detail.getMovementTypeId() == 1) {
			if(detail.isProfitDistribution()) {
				this.hasProfitDistribution = true;
				this.profitDistribution += detail.getDocumentValue();								
			} else if(!detail.isTransactionSameCompany()) {
				this.income += detail.getDocumentValue();								
			} else {
				this.transfInput += detail.getDocumentValue();
			}
		} else {
			if(detail.isProfitDistribution()) {
				this.hasProfitDistribution = true;
				this.profitDistribution -= detail.getDocumentValue();								
			} else if(!detail.isTransactionSameCompany()) {
				this.expenses += detail.getDocumentValue();
			} else {
				this.transfOutput += detail.getDocumentValue();	
			}
		}
		
		if(this.groupBy == 0) {
			if(detail.getDocumentValue() != 0) {
				this.items.add(detail);
			}
		} else if(this.groupBy == 1) {
			if(detail.getDocumentValue() != 0) {
				this.populateItemCompany(detail);
			}
		} else if(this.groupBy == 2) {
			if(detail.getDocumentValue() != 0) {
				this.populateItemDate(detail);
			}
		} else if(this.groupBy == 3) {
			if(detail.getDocumentValue() != 0) {
				this.populateItemCompanyDate(detail);
			}
		} else if(this.groupBy == 4) {
			if(detail.getDocumentValue() != 0) {
				this.populateItemWeek(detail);
			}
		} else if(this.groupBy == 5) {
			if(detail.getDocumentValue() != 0) {
				this.populateItemCompanyWeek(detail);
			}
		} else if(this.groupBy == 6) {
			if(detail.getDocumentValue() != 0) {
				this.populateItemOriginCompany(detail);
			}
		} else if(this.groupBy == 7) {
			if(detail.getDocumentValue() != 0) {
				this.populateItemOriginCompanyWeek(detail);
			}
		} else if(this.groupBy == 8) {
			if(detail.getDocumentValue() != 0) {
				this.populateItemProvider(detail);
			}
		} else if(this.groupBy == 9) {
			if(detail.getDocumentValue() != 0) {
				this.populateItemProviderDate(detail);
			}
		} else if(this.groupBy == 10) {
			if(detail.getDocumentValue() != 0) {
				this.populateItemProviderWeek(detail);
			}
		} else if(this.groupBy == 11) {
			if(detail.getDocumentValue() != 0) {
				this.populateItemTransfMonth(detail);
			}
		} else if(this.groupBy == 12) {
			if(detail.getDocumentValue() != 0) {
				this.populateItemFinancialGroup(detail);
			}
		} else if(this.groupBy == 13) {
			if(detail.getDocumentValue() != 0) {
				this.populateItemFinancialGroupDate(detail);
			}
		} else if(this.groupBy == 14) {
			if(detail.getDocumentValue() != 0) {
				this.populateItemFinancialGroupWeek(detail);
			}
		} else if(this.groupBy == 15) {
			if(detail.getDocumentValue() != 0) {
				this.populateItemFinancialSubGroup(detail);
			}
		} else if(this.groupBy == 16) {
			if(detail.getDocumentValue() != 0) {
				this.populateItemFinancialSubGroupDate(detail);
			}
		} else if(this.groupBy == 17) {
			if(detail.getDocumentValue() != 0) {
				this.populateItemFinancialSubGroupWeek(detail);
			}
		}
	}
	
	private void populateItemCompany(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(this.itemsCompanyMap == null) {
			this.itemsCompanyMap = new TreeMap<>();
		}
		
		final long keyCompany = detail.getCompanyId();
		
		FinancialIncomeExpensesCompanyDTO companyItem = this.itemsCompanyMap.get(keyCompany);
		if(companyItem == null) {
			companyItem = FinancialIncomeExpensesCompanyDTO.builder().build();
			companyItem.setCompanyId(keyCompany);
			companyItem.setCompanyDescription(detail.getCompanyDescription());
		}
		companyItem.addItem(detail);
		this.itemsCompanyMap.put(keyCompany, companyItem);		
	}
	
	private void populateItemDate(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(this.itemsDateMap == null) {
			this.itemsDateMap = new TreeMap<>();
		}
		
		final long keyDate = detail.getPaymentDateLong();
		
		FinancialIncomeExpensesDateDTO dateItem = this.itemsDateMap.get(keyDate);
		if(dateItem == null) {
			dateItem = FinancialIncomeExpensesDateDTO.builder().build();
			dateItem.setDate(detail.getPaymentDate());
		}
		dateItem.addItem(detail);
		this.itemsDateMap.put(keyDate, dateItem);		
	}
	
	private void populateItemCompanyDate(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(this.itemsCompanyDateMap == null) {
			this.itemsCompanyDateMap = new TreeMap<>();
		}
		
		final long keyCompany = detail.getCompanyId();
		final long keyDate = detail.getPaymentDateLong();
		final String keyCompanyDate = keyCompany+ConstantDataManager.PONTO_VIRGULA_STRING+keyDate;
		
		
		FinancialIncomeExpensesCompanyDateDTO companyDateItem = this.itemsCompanyDateMap.get(keyCompanyDate);
		if(companyDateItem == null) {
			companyDateItem = FinancialIncomeExpensesCompanyDateDTO.builder().build();
			companyDateItem.setCompanyId(keyCompany);
			companyDateItem.setCompanyDescription(detail.getCompanyDescription());
			companyDateItem.setDate(detail.getPaymentDate());
		}
		companyDateItem.addItem(detail);
		this.itemsCompanyDateMap.put(keyCompanyDate, companyDateItem);		
	}
	
	private void populateItemWeek(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(this.itemsWeekMap == null) {
			this.itemsWeekMap = new TreeMap<>();
		}
		
		final int keyWeek = detail.getWeekYear();
		
		FinancialIncomeExpensesWeekDTO weekItem = this.itemsWeekMap.get(keyWeek);
		if(weekItem == null) {
			weekItem = FinancialIncomeExpensesWeekDTO.builder().build();
			weekItem.setWeek(detail.getWeekYear());
			weekItem.setDateFrom(detail.getDateFrom());
			weekItem.setDateTo(detail.getDateTo());
			weekItem.setWeekDescription(detail.getWeekDescription());
		}
		weekItem.addItem(detail);
		this.itemsWeekMap.put(keyWeek, weekItem);		
	}
	
	private void populateItemCompanyWeek(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(this.itemsCompanyWeekMap == null) {
			this.itemsCompanyWeekMap = new TreeMap<>();
		}
		
		final long keyCompany = detail.getCompanyId();
		final int keyWeek = detail.getWeekYear();
		final String keyCompanyWeek = keyCompany+ConstantDataManager.PONTO_VIRGULA_STRING+keyWeek;
		
		
		FinancialIncomeExpensesCompanyWeekDTO companyWeekItem = this.itemsCompanyWeekMap.get(keyCompanyWeek);
		if(companyWeekItem == null) {
			companyWeekItem = FinancialIncomeExpensesCompanyWeekDTO.builder().build();
			companyWeekItem.setCompanyId(keyCompany);
			companyWeekItem.setCompanyDescription(detail.getCompanyDescription());
			companyWeekItem.setWeek(keyWeek);
			companyWeekItem.setDateFrom(detail.getDateFrom());
			companyWeekItem.setDateTo(detail.getDateTo());
			companyWeekItem.setWeekDescription(detail.getWeekDescription());
		}
		companyWeekItem.addItem(detail);
		this.itemsCompanyWeekMap.put(keyCompanyWeek, companyWeekItem);		
	}
	
	private void populateItemOriginCompany(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(this.itemsOriginCompanyMap == null) {
			this.itemsOriginCompanyMap = new TreeMap<>();
		}
		
		long keyCompany = detail.getBankAccountOriginId();
		String companyDescription = detail.getBankAccountOriginDescription();
		if(keyCompany == 0) {
			keyCompany = detail.getBankAccountId();			
			companyDescription = detail.getCompanyDescription();
		}
		
		FinancialIncomeExpensesCompanyDTO companyItem = this.itemsOriginCompanyMap.get(keyCompany);
		if(companyItem == null) {
			companyItem = FinancialIncomeExpensesCompanyDTO.builder().build();
			companyItem.setCompanyId(keyCompany);
			companyItem.setCompanyDescription(companyDescription);
		}
		companyItem.addItem(detail);
		this.itemsOriginCompanyMap.put(keyCompany, companyItem);		
	}
	
	private void populateItemOriginCompanyWeek(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(this.itemsOriginCompanyWeekMap == null) {
			this.itemsOriginCompanyWeekMap = new TreeMap<>();
		}
		
		long keyCompany = detail.getBankAccountOriginId();
		final int keyWeek = detail.getWeekYear();
		String companyDescription = detail.getBankAccountOriginDescription();
		if(keyCompany == 0) {
			keyCompany = detail.getBankAccountId();			
			companyDescription = detail.getCompanyDescription();
		}
		
		final String keyCompanyWeek = keyCompany+ConstantDataManager.PONTO_VIRGULA_STRING+keyWeek;
		
		
		FinancialIncomeExpensesCompanyWeekDTO companyWeekItem = this.itemsOriginCompanyWeekMap.get(keyCompanyWeek);
		if(companyWeekItem == null) {
			companyWeekItem = FinancialIncomeExpensesCompanyWeekDTO.builder().build();
			companyWeekItem.setCompanyId(keyCompany);
			companyWeekItem.setCompanyDescription(companyDescription);
			companyWeekItem.setWeek(keyWeek);
			companyWeekItem.setDateFrom(detail.getDateFrom());
			companyWeekItem.setDateTo(detail.getDateTo());
			companyWeekItem.setWeekDescription(detail.getWeekDescription());
		}
		companyWeekItem.addItem(detail);
		this.itemsOriginCompanyWeekMap.put(keyCompanyWeek, companyWeekItem);		
	}
	
	private void populateItemProvider(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(this.itemsProviderMap == null) {
			this.itemsProviderMap = new TreeMap<>();
		}
		
		final String keyProvider = detail.getProviderDescription();
		
		FinancialIncomeExpensesProviderDTO providerItem = this.itemsProviderMap.get(keyProvider);
		if(providerItem == null) {
			providerItem = FinancialIncomeExpensesProviderDTO.builder().build();
			providerItem.setProviderId(detail.getProviderId());
			providerItem.setProviderDescription(detail.getProviderDescription());
		}
		providerItem.addItem(detail);
		this.itemsProviderMap.put(keyProvider, providerItem);		
	}
	
	private void populateItemProviderDate(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(this.itemsProviderDateMap == null) {
			this.itemsProviderDateMap = new TreeMap<>();
		}
		
		final String keyProvider = detail.getProviderDescription();
		final long keyDate = detail.getPaymentDateLong();
		final String keyProviderDate = keyProvider+ConstantDataManager.PONTO_VIRGULA_STRING+keyDate;
		
		
		FinancialIncomeExpensesProviderDateDTO providerDateItem = this.itemsProviderDateMap.get(keyProviderDate);
		if(providerDateItem == null) {
			providerDateItem = FinancialIncomeExpensesProviderDateDTO.builder().build();
			providerDateItem.setProviderId(detail.getProviderId());
			providerDateItem.setProviderDescription(detail.getProviderDescription());
			providerDateItem.setDate(detail.getPaymentDate());
		}
		providerDateItem.addItem(detail);
		this.itemsProviderDateMap.put(keyProviderDate, providerDateItem);		
	}
	
	private void populateItemProviderWeek(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(this.itemsProviderWeekMap == null) {
			this.itemsProviderWeekMap = new TreeMap<>();
		}
		
		final String keyProvider = detail.getProviderDescription();
		final int keyWeek = detail.getWeekYear();
		final String keyProviderWeek = keyProvider+ConstantDataManager.PONTO_VIRGULA_STRING+keyWeek;
		
		
		FinancialIncomeExpensesProviderWeekDTO providerWeekItem = this.itemsProviderWeekMap.get(keyProviderWeek);
		if(providerWeekItem == null) {
			providerWeekItem = FinancialIncomeExpensesProviderWeekDTO.builder().build();
			providerWeekItem.setProviderId(detail.getProviderId());
			providerWeekItem.setProviderDescription(detail.getProviderDescription());
			providerWeekItem.setWeek(keyWeek);
			providerWeekItem.setDateFrom(detail.getDateFrom());
			providerWeekItem.setDateTo(detail.getDateTo());
			providerWeekItem.setWeekDescription(detail.getWeekDescription());
		}
		providerWeekItem.addItem(detail);
		this.itemsProviderWeekMap.put(keyProviderWeek, providerWeekItem);		
	}
	
	private void populateItemTransfMonth(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(this.itemsTrasnfMonthMap == null) {
			this.itemsTrasnfMonthMap = new TreeMap<>();
		}
		
		long keyCompany = detail.getBankAccountId();
		String companyDescription = detail.getBankAccountDescription();
		
		FinancialIncomeExpensesTransfMonthDTO companyItem = this.itemsTrasnfMonthMap.get(keyCompany);
		if(companyItem == null) {
			companyItem = FinancialIncomeExpensesTransfMonthDTO.builder().build();
			companyItem.setCompanyId(keyCompany);
			companyItem.setCompanyDescription(companyDescription);
		}
		companyItem.addItem(detail);
		this.itemsTrasnfMonthMap.put(keyCompany, companyItem);		
	}
	
	private void populateItemFinancialGroup(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(this.itemsFinancialGroupMap == null) {
			this.itemsFinancialGroupMap = new TreeMap<>();
		}
		
		final String keyFinancialGroup = detail.getFinancialGroupDescription();
		
		FinancialIncomeExpensesFinancialGroupDTO financialGroupItem = this.itemsFinancialGroupMap.get(keyFinancialGroup);
		if(financialGroupItem == null) {
			financialGroupItem = FinancialIncomeExpensesFinancialGroupDTO.builder().build();
			financialGroupItem.setFinancialGroupId(detail.getFinancialGroupId());
			financialGroupItem.setFinancialGroupDescription(detail.getFinancialGroupDescription());
		}
		financialGroupItem.addItem(detail);
		this.itemsFinancialGroupMap.put(keyFinancialGroup, financialGroupItem);		
	}
	
	private void populateItemFinancialGroupDate(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(this.itemsFinancialGroupDateMap == null) {
			this.itemsFinancialGroupDateMap = new TreeMap<>();
		}
		
		final String keyFinancialGroup = detail.getFinancialGroupDescription();
		final long keyDate = detail.getPaymentDateLong();
		final String keyFinancialGroupDate = keyFinancialGroup+ConstantDataManager.PONTO_VIRGULA_STRING+keyDate;
		
		
		FinancialIncomeExpensesFinancialGroupDateDTO financialGroupItem = this.itemsFinancialGroupDateMap.get(keyFinancialGroupDate);
		if(financialGroupItem == null) {
			financialGroupItem = FinancialIncomeExpensesFinancialGroupDateDTO.builder().build();
			financialGroupItem.setFinancialGroupId(detail.getFinancialGroupId());
			financialGroupItem.setFinancialGroupDescription(detail.getFinancialGroupDescription());
			financialGroupItem.setDate(detail.getPaymentDate());
		}
		financialGroupItem.addItem(detail);
		this.itemsFinancialGroupDateMap.put(keyFinancialGroupDate, financialGroupItem);		
	}
	
	private void populateItemFinancialGroupWeek(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(this.itemsFinancialGroupWeekMap == null) {
			this.itemsFinancialGroupWeekMap = new TreeMap<>();
		}
		
		final String keyFinancialGroup = detail.getFinancialGroupDescription();
		final int keyWeek = detail.getWeekYear();
		final String keyFinancialGroupWeek = keyFinancialGroup+ConstantDataManager.PONTO_VIRGULA_STRING+keyWeek;
		
		
		FinancialIncomeExpensesFinancialGroupWeekDTO financialGroupItem = this.itemsFinancialGroupWeekMap.get(keyFinancialGroupWeek);
		if(financialGroupItem == null) {
			financialGroupItem = FinancialIncomeExpensesFinancialGroupWeekDTO.builder().build();
			financialGroupItem.setFinancialGroupId(detail.getFinancialGroupId());
			financialGroupItem.setFinancialGroupDescription(detail.getFinancialGroupDescription());
			financialGroupItem.setWeek(keyWeek);
			financialGroupItem.setDateFrom(detail.getDateFrom());
			financialGroupItem.setDateTo(detail.getDateTo());
			financialGroupItem.setWeekDescription(detail.getWeekDescription());
		}
		financialGroupItem.addItem(detail);
		this.itemsFinancialGroupWeekMap.put(keyFinancialGroupWeek, financialGroupItem);		
	}
	
	private void populateItemFinancialSubGroup(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(this.itemsFinancialSubGroupMap == null) {
			this.itemsFinancialSubGroupMap = new TreeMap<>();
		}
		
		final String keyFinancialGroup = detail.getFinancialGroupDescription();
		final String keyFinancialSubGroup = detail.getFinancialSubGroupDescription();
		final String key = keyFinancialGroup +ConstantDataManager.PONTO_VIRGULA_STRING+ keyFinancialSubGroup;
		
		FinancialIncomeExpensesFinancialSubGroupDTO financialSubGroupItem = this.itemsFinancialSubGroupMap.get(key);
		if(financialSubGroupItem == null) {
			financialSubGroupItem = FinancialIncomeExpensesFinancialSubGroupDTO.builder().build();
			financialSubGroupItem.setFinancialGroupId(detail.getFinancialGroupId());
			financialSubGroupItem.setFinancialGroupDescription(detail.getFinancialGroupDescription());
			financialSubGroupItem.setFinancialSubGroupId(detail.getFinancialSubGroupId());
			financialSubGroupItem.setFinancialSubGroupDescription(detail.getFinancialSubGroupDescription());
		}
		financialSubGroupItem.addItem(detail);
		this.itemsFinancialSubGroupMap.put(key, financialSubGroupItem);		
	}
	
	private void populateItemFinancialSubGroupDate(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(this.itemsFinancialSubGroupDateMap == null) {
			this.itemsFinancialSubGroupDateMap = new TreeMap<>();
		}
		
		final String keyFinancialGroup = detail.getFinancialGroupDescription();
		final String keyFinancialSubGroup = detail.getFinancialSubGroupDescription();
		final long keyDate = detail.getPaymentDateLong();
		final String keyFinancialSubGroupDate = keyFinancialGroup+ConstantDataManager.PONTO_VIRGULA_STRING+keyFinancialSubGroup+ConstantDataManager.PONTO_VIRGULA_STRING+keyDate;
		
		FinancialIncomeExpensesFinancialSubGroupDateDTO financialSubGroupItem = this.itemsFinancialSubGroupDateMap.get(keyFinancialSubGroupDate);
		if(financialSubGroupItem == null) {
			financialSubGroupItem = FinancialIncomeExpensesFinancialSubGroupDateDTO.builder().build();
			financialSubGroupItem.setFinancialGroupId(detail.getFinancialGroupId());
			financialSubGroupItem.setFinancialGroupDescription(detail.getFinancialGroupDescription());
			financialSubGroupItem.setFinancialSubGroupId(detail.getFinancialSubGroupId());
			financialSubGroupItem.setFinancialSubGroupDescription(detail.getFinancialSubGroupDescription());
			financialSubGroupItem.setDate(detail.getPaymentDate());
		}
		financialSubGroupItem.addItem(detail);
		this.itemsFinancialSubGroupDateMap.put(keyFinancialSubGroupDate, financialSubGroupItem);		
	}
	
	private void populateItemFinancialSubGroupWeek(final FinancialIncomeExpensesItemDetailDTO detail) {
		if(this.itemsFinancialSubGroupWeekMap == null) {
			this.itemsFinancialSubGroupWeekMap = new TreeMap<>();
		}
		
		final String keyFinancialGroup = detail.getFinancialGroupDescription();
		final String keyFinancialSubGroup = detail.getFinancialSubGroupDescription();
		final int keyWeek = detail.getWeekYear();
		final String keyFinancialSubGroupWeek = keyFinancialGroup+ConstantDataManager.PONTO_VIRGULA_STRING+keyFinancialSubGroup+ConstantDataManager.PONTO_VIRGULA_STRING+keyWeek;
				
		FinancialIncomeExpensesFinancialSubGroupWeekDTO financialSubGroupItem = this.itemsFinancialSubGroupWeekMap.get(keyFinancialSubGroupWeek);
		if(financialSubGroupItem == null) {
			financialSubGroupItem = FinancialIncomeExpensesFinancialSubGroupWeekDTO.builder().build();
			financialSubGroupItem.setFinancialGroupId(detail.getFinancialGroupId());
			financialSubGroupItem.setFinancialGroupDescription(detail.getFinancialGroupDescription());
			financialSubGroupItem.setFinancialSubGroupId(detail.getFinancialSubGroupId());
			financialSubGroupItem.setFinancialSubGroupDescription(detail.getFinancialSubGroupDescription());
			financialSubGroupItem.setWeek(keyWeek);
			financialSubGroupItem.setDateFrom(detail.getDateFrom());
			financialSubGroupItem.setDateTo(detail.getDateTo());
			financialSubGroupItem.setWeekDescription(detail.getWeekDescription());
		}
		financialSubGroupItem.addItem(detail);
		this.itemsFinancialSubGroupWeekMap.put(keyFinancialSubGroupWeek, financialSubGroupItem);		
	}

	public void calculateTotais() {
		this.balance = (this.income + this.transfInput - this.expenses - this.transfOutput + this.profitDistribution);
		if(this.groupBy == 11) {
			this.income = 0;
			for (final Map.Entry<Long, FinancialIncomeExpensesTransfMonthDTO> entry : this.itemsTrasnfMonthMap.entrySet()) {
				entry.getValue().calculateTotais();
				this.income += entry.getValue().getIncome();
			}
			this.income = new BigDecimal(this.income).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
			this.balance = (this.income + this.transfInput - this.expenses - this.transfOutput + this.profitDistribution);
			this.balance = new BigDecimal(this.balance).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
		}
	}
	
	public boolean hasTransfeSameCompany() {
		return (this.transfInput != 0 || this.transfOutput != 0);
	}
}