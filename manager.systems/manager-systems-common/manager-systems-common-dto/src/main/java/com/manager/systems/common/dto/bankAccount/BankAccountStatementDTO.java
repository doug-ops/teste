package com.manager.systems.common.dto.bankAccount;

import java.util.ArrayList;
import java.util.List;


public class BankAccountStatementDTO {
	
	private double initialBankAccountAvaliable;
	private double finalBankAccountAvaliable;
	private int companyId;
	private String companyDescription;
	private int bankAccountId;
	private String banAccountDescription;	
	private double totalDebit;
	private double totalCredit;
	private List<BankAccountStatementItemDTO> itens = new ArrayList<BankAccountStatementItemDTO>();

	public BankAccountStatementDTO() {
		super();
	}

	public final int getBankAccountId() {
		return this.bankAccountId;
	}

	public final double getInitialBankAccountAvaliable() {
		return this.initialBankAccountAvaliable;
	}

	public final double getFinalBankAccountAvaliable() {
		return this.finalBankAccountAvaliable;
	}

	public final List<BankAccountStatementItemDTO> getItens() {
		return this.itens;
	}

	public final void setBankAccountId(int bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public final void setInitialBankAccountAvaliable(final double initialBankAccountAvaliable) {
		this.initialBankAccountAvaliable = initialBankAccountAvaliable;
	}

	public final void setFinalBankAccountAvaliable(final double finalBankAccountAvaliable) {
		this.finalBankAccountAvaliable = finalBankAccountAvaliable;
	}

	public final void addItens(final BankAccountStatementItemDTO item) {
		this.itens.add(item);
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
	
	public final String getBanAccountDescription() {
		return this.banAccountDescription;
	}

	public final void setBanAccountDescription(final String banAccountDescription) {
		this.banAccountDescription = banAccountDescription;
	}
	
	public final double getTotalBalance()
	{
		return (this.totalDebit + this.totalCredit);
	}
}