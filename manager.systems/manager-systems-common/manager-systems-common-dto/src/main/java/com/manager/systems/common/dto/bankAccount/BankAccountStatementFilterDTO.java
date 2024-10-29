package com.manager.systems.common.dto.bankAccount;

import java.util.ArrayList;
import java.util.List;

public class BankAccountStatementFilterDTO {

	private long companyId;
	private long userId;
	private String email;
	private List<Long> banksAccount = new ArrayList<Long>();
	private List<Long> persons = new ArrayList<Long>();
	
	public BankAccountStatementFilterDTO() {
		super();
	}

	public final long getCompanyId() {
		return this.companyId;
	}

	public final void setCompanyId(final long companyId){
		this.companyId = companyId;
	}
	
	public final long getUserId() {
		return this.userId;
	}

	public void setUserId(final long userId) {
		this.userId = userId;
	}
	
	public final String getEmail() {
		return email;
	}

	public final void setEmail(String email) {
		this.email = email;
	}

	public final List<Long> getBanksAccount() {
		return this.banksAccount;
	}
	
	public final List<Long> getPersons() {
		return this.persons;
	}
	
	public final void addBanksAccount(final long bankAccout) {
		if(!this.banksAccount.contains(bankAccout)) {
			this.banksAccount.add(bankAccout);	
		}
	}
	
	public final void addPerson(final long person) {
		if(!this.persons.contains(person)) {
			this.persons.add(person);	
		}
	}
}