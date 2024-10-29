package com.manager.systems.web.movements.dto.bankAccount;

import java.util.Map;
import java.util.TreeMap;

public class BankAccountStatementCompanyDTO {
	
	private int companyId;
	private String companyDescription;
	private double totalDebit;
	private double totalCredit;
	private double totalBalance;
	private Map<Integer, BankAccountStatementAccountDTO> accounts = new TreeMap<Integer, BankAccountStatementAccountDTO>();

	public BankAccountStatementCompanyDTO() {
		super();
	}

	public final int getCompanyId() {
		return this.companyId;
	}

	public final String getCompanyDescription() {
		return this.companyDescription;
	}

	public final double getTotalDebit() {
		return this.totalDebit;
	}

	public final double getTotalCredit() {
		return this.totalCredit;
	}

	public final void setCompanyId(final int companyId) {
		this.companyId = companyId;
	}

	public final void setCompanyDescription(final String companyDescription) {
		this.companyDescription = companyDescription;
	}
	
	public final void sumDebit(final double totalDebit) {
		this.totalDebit += totalDebit;
	}
	
	public final void sumCredit(final double totalCredit) {
		this.totalCredit += totalCredit;
	}
	
	public final double getTotalBalance() {
		return this.totalBalance;
	}
	
	public final Map<Integer, BankAccountStatementAccountDTO> getAccounts() {
		return this.accounts;
	}
	
	public void addItemAccount(final BankAccountStatementItemDTO item) {
		BankAccountStatementAccountDTO account = this.accounts.get(item.getBankAccountId());
		if(account==null) {
			account = new BankAccountStatementAccountDTO();
			account.setBankAccountId(item.getBankAccountId());
			account.setBanAccountDescription(item.getBanAccountDescription());
		}
		account.addDocumentValue(item.getDocumentValue());
		this.accounts.put(item.getBankAccountId(), account);
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