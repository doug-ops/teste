package com.manager.systems.web.movements.dto.bankAccount;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.manager.systems.common.utils.StringUtils;

public class BankAccountStatemenReportDTO {

	private int operation;
	private String bankAccounts;
	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;
	private double initialBankAccountAvaliable;
	private double finalBankAccountAvaliable;
   	private double totalDebit;
   	private double totalCredit;
	private List<String> bankAccountLabels = new ArrayList<String>();
	private Map<Integer, BankAccountStatementDTO> itensMap = new TreeMap<Integer, BankAccountStatementDTO>();
	private List<BankAccountStatementItemDTO> itens = new ArrayList<BankAccountStatementItemDTO>();
	private Map<Integer, BankAccountStatementCompanyDTO> companys = new TreeMap<Integer, BankAccountStatementCompanyDTO>();

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
	
	public final Map<Integer, BankAccountStatementDTO> getItensMap() {
		return this.itensMap;
	}
	
	public void addItem(final int key, final BankAccountStatementDTO value) {
		
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
	
	public final void addItem(final BankAccountStatementItemDTO item)
	{
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
	
	public final double getTotalBalance()
	{
		return (this.initialBankAccountAvaliable + this.totalDebit + this.totalCredit);
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
	
	public final Map<Integer, BankAccountStatementCompanyDTO> getCompanys() {
		return this.companys;
	}
}