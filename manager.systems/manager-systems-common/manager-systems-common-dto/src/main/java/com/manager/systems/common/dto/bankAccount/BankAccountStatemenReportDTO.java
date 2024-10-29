package com.manager.systems.common.dto.bankAccount;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.manager.systems.common.utils.StringUtils;

public class BankAccountStatemenReportDTO {

	private int operation;
	private String bankAccounts;
	private String persons;
	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;
	private double initialBankAccountAvaliable;
	private double finalBankAccountAvaliable;
   	private double totalDebit;
   	private double totalCredit;
   	private String companyDescription;
	private long companyId;
	private String email;
	private int financialCostCenter;
	private List<String> bankAccountLabels = new ArrayList<String>();
	private Map<Long, BankAccountStatementDTO> itensMap = new TreeMap<Long, BankAccountStatementDTO>();
	private List<BankAccountStatementItemDTO> itens = new ArrayList<BankAccountStatementItemDTO>();
	private Map<Long, BankAccountStatementCompanyDTO> companys = new TreeMap<Long, BankAccountStatementCompanyDTO>();
	private Map<Long, BankAccountStatementFilterDTO> filterMap = new TreeMap<>();
	private byte[] pdf;
	
	public BankAccountStatemenReportDTO() {
		super();
	}

	public final int getOperation() {
		return this.operation;
	}

	public final String getBankAccounts() {
		return this.bankAccounts;
	}

	public final LocalDateTime getDateFrom() {
		return this.dateFrom;
	}
	
	public final String getDateFromString() {
		return StringUtils.formatDate(this.dateFrom, StringUtils.DATE_PATTERN_DD_MM_YYYY);
	}

	public final LocalDateTime getDateTo() {
		return this.dateTo;
	}
	
	public final String getDateToString() {
		return StringUtils.formatDate(this.dateTo, StringUtils.DATE_PATTERN_DD_MM_YYYY);
	}

	public final void setOperation(final int operation) {
		this.operation = operation;
	}

	public final void setBankAccounts(final String bankAccounts) {
		this.bankAccounts = bankAccounts;
	}

	public final void setDateFrom(final LocalDateTime dateFrom) {
		this.dateFrom = dateFrom;
	}

	public final void setDateTo(final LocalDateTime dateTo) {
		this.dateTo = dateTo;
	}
	
	public final Map<Long, BankAccountStatementDTO> getItensMap() {
		return this.itensMap;
	}
	
	public void addItem(final Long key, final BankAccountStatementDTO value) {
		
		this.itensMap.put(key, value);
	}
	
	public final double getInitialBankAccountAvaliable() {
		return this.initialBankAccountAvaliable;
	}

	public final double getFinalBankAccountAvaliable() {
		return this.finalBankAccountAvaliable;
	}
	
	public final void setInitialBankAccountAvaliable(final double initialBankAccountAvaliable) {
		this.initialBankAccountAvaliable = initialBankAccountAvaliable;
	}

	public final void setFinalBankAccountAvaliable(final double finalBankAccountAvaliable) {
		this.finalBankAccountAvaliable = finalBankAccountAvaliable;
	}
	
	public final List<BankAccountStatementItemDTO> getItens() {
		return this.itens;
	}
	
	public final String getPersons() {
		return this.persons;
	}
	
	public final  void setPersons(final String persons) {
		this.persons = persons;
	}
	
	public final void addItem(final BankAccountStatementItemDTO item)
	{
		this.finalBankAccountAvaliable += item.getDocumentValue();			
		item.setFinalBankAccountAvaliable(this.finalBankAccountAvaliable);
		
		this.itens.add(item);
		
		if(item.getDocumentValue()<0)
		{
			sumDebit(item.getDocumentValue());				
		}
		else 
		{
			sumCredit(item.getDocumentValue());					
		}
		addItemCompany(item);
	}
	
	public final List<String> getBankAccountLabels() {
		return this.bankAccountLabels;
	}
	
	public final double getTotalCredit() {
		return this.totalCredit;
	}
	
	public final double getTotalDebit() {
		return this.totalDebit;
	}
	
	public final void sumDebit(final double totalDebit) {
		this.totalDebit += totalDebit;
	}

	public final void setTotalCredit(final double totalCredit) {
		this.totalCredit = totalCredit;
	}
	
	public final void sumCredit(final double totalCredit) {
		this.totalCredit += totalCredit;
	}
	
	public final String getCompanyDescription() {
		return this.companyDescription;
	}

	public final void setCompanyDescription(final String companyDescription) {
		this.companyDescription = companyDescription;
	}
	
	public final long getCompanyId() 
	{
		return this.companyId;
	}

	public final void setCompanyId(final long companyId)
	{
		this.companyId = companyId;
	}
	
	public final String getEmail() {
		return this.email;
	}

	public final void setEmail(String email) {
		this.email = email;
	}
	
	public final double getTotalBalance()
	{
		return (this.initialBankAccountAvaliable + this.totalDebit + this.totalCredit);
	}
	
	public final Map<Long, BankAccountStatementFilterDTO> getFilterMap() {
		return this.filterMap;
	}
	
	public final byte[] getPdf() {
		return this.pdf;
	}
	
	public void setPdf(final byte[] pdf) {
		this.pdf = pdf;
	}
	
	public final int getFinancialCostCenter() {
		return this.financialCostCenter;
	}

	public final void setFinancialCostCenter(final int financialCostCenter) {
		this.financialCostCenter = financialCostCenter;
	}

	public final void addFilter(final long userId, final long bankAccountId, final long person, final long companyId, final String email) {
		BankAccountStatementFilterDTO banksAccount = this.filterMap.get(userId);
		if(banksAccount == null) {
			banksAccount = new BankAccountStatementFilterDTO();
		}
		banksAccount.setUserId(userId);
		banksAccount.setCompanyId(companyId);
		banksAccount.setEmail(email);
		banksAccount.addBanksAccount(bankAccountId);
		banksAccount.addPerson(person);
		
		this.filterMap.put(userId, banksAccount);
	}
	
	public void addItemCompany(final BankAccountStatementItemDTO item) {
		BankAccountStatementCompanyDTO company = this.companys.get(item.getCompanyId());
		if(company==null) {
			company = new BankAccountStatementCompanyDTO();
			company.setCompanyId(item.getCompanyId());
			company.setCompanyDescription(item.getCompanyDescription());
		}
		company.addDocumentValue(item.getDocumentValue());
		company.addItemAccount(item);
		this.companys.put(item.getCompanyId(), company);
	}
	
	public final Map<Long, BankAccountStatementCompanyDTO> getCompanys() {
		return this.companys;
	}
}