/**.
 * Date Create 23/08/2019
 */

package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ChangeData;

public class ReportBankAccountDTO implements Serializable {
	private static final long serialVersionUID = 6742667327542415691L;

	private String bankAccountIdFrom;
	private String bankAccountIdTo;
	private String inactive;
	private String description;
	private String bankCode;
	private String bankAngency;
	private String objectType;
	private int unboundBankAccount; // 0 - all, 1 - true, 2 - false
	private long personId;
	private ChangeData changeData;
	private long userOperation;
	private List<BankAccountDTO> bankAccounts = new ArrayList<BankAccountDTO>();

	public ReportBankAccountDTO() {
		super();
	}

	public final String getBankAccountIdFrom() {
		return this.bankAccountIdFrom;
	}

	public final void setBankAccountIdFrom(final String bankAccountIdFrom) {
		this.bankAccountIdFrom = bankAccountIdFrom;
	}

	public final String getBankAccountIdTo() {
		return this.bankAccountIdTo;
	}

	public final void setBankAccountIdTo(final String bankAccountIdTo) {
		this.bankAccountIdTo = bankAccountIdTo;
	}

	public final int getInactive() {
		return StringUtils.isLong(this.inactive) ? Integer.valueOf(this.inactive) : 2;
	}

	public final void setInactive(final String inactive) {
		this.inactive = inactive;
	}

	public final String getDescription() {
		return this.description;
	}

	public final void setDescription(final String description) {
		this.description = description;
	}

	public final String getObjectType() {
		return this.objectType;
	}

	public final void setObjectType(final String objectType) {
		this.objectType = objectType;
	}

	public List<BankAccountDTO> getBankAccounts() {
		return this.bankAccounts;
	}

	public final void addItem(final BankAccountDTO bankAccount) {
		this.bankAccounts.add(bankAccount);
	}

	public final String getBankCode() {
		return this.bankCode;
	}

	public final void setBankCode(final String bankCode) {
		this.bankCode = bankCode;
	}

	public final String getBankAngency() {
		return this.bankAngency;
	}

	public final void setBankAngency(final String bankAngency) {
		this.bankAngency = bankAngency;
	}

	public final ChangeData getChangeData() {
		return this.changeData;
	}

	public final void setChangeData(final ChangeData changeData) {
		this.changeData = changeData;
	}

	public final int getUnboundBankAccount() {
		return this.unboundBankAccount;
	}

	public final void setUnboundBankAccount(final int unboundBankAccount) {
		this.unboundBankAccount = unboundBankAccount;
	}

	public final long getPersonId() {
		return this.personId;
	}

	public final void setPersonId(final long personId) {
		this.personId = personId;
	}
	
	public final long getUserOperation() {
		return this.userOperation;
	}

	public final void setUserOperation(final long userOperation) {
		this.userOperation = userOperation;
	}
}