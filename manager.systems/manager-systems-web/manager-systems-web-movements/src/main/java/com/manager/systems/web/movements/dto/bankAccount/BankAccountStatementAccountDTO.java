package com.manager.systems.web.movements.dto.bankAccount;

public class BankAccountStatementAccountDTO {	
	private int bankAccountId;
	private String banAccountDescription;	
	private double totalDebit;
	private double totalCredit;
	private double totalBalance;

	public BankAccountStatementAccountDTO() {
		super();
	}

	public final int getBankAccountId() {
		return this.bankAccountId;
	}

	public final void setBankAccountId(int bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public final double getTotalDebit() {
		return this.totalDebit;
	}

	public final double getTotalCredit() {
		return this.totalCredit;
	}
	
	public final String getBanAccountDescription() {
		return this.banAccountDescription;
	}

	public final void setBanAccountDescription(final String banAccountDescription) {
		this.banAccountDescription = banAccountDescription;
	}
	
	public final double getTotalBalance() {
		return this.totalBalance;
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