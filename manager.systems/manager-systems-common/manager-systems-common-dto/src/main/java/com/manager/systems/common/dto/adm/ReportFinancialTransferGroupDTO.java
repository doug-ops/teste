package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class ReportFinancialTransferGroupDTO implements Serializable 
{
	private static final long serialVersionUID = -1569008514475250199L;

	private long bankAccountOriginId;
	private long bankAccountDestinyId;
	private int groupId;
	private int groupItemId;
	private int inactive;
	private long userOperation;
	private long companyId;
	private double transferValue;
	private int expense;
	private Map<String, FinancialTransferGroupDTO> itens = new TreeMap<String, FinancialTransferGroupDTO>();
	
	public ReportFinancialTransferGroupDTO() 
	{
		super();
	}

	public final long getBankAccountOriginId() 
	{
		return this.bankAccountOriginId;
	}

	public final void setBankAccountOriginId(final long bankAccountOriginId)
	{
		this.bankAccountOriginId = bankAccountOriginId;
	}

	public final long getBankAccountDestinyId()
	{
		return this.bankAccountDestinyId;
	}

	public final void setBankAccountDestinyId(final long bankAccountDestinyId) 
	{
		this.bankAccountDestinyId = bankAccountDestinyId;
	}

	public final int isInactive() 
	{
		return this.inactive;
	}

	public final void setInactive(final int inactive)
	{
		this.inactive = inactive;
	}
	
	public final Map<String, FinancialTransferGroupDTO> getItens() 
	{
		return this.itens;
	}
	
	public final void addItem(final String key, final FinancialTransferGroupDTO item)
	{
		this.itens.put(key, item);
	}

	public final int getGroupId() 
	{
		return this.groupId;
	}

	public final void setGroupId(final int groupId) 
	{
		this.groupId = groupId;
	}

	public final int getGroupItemId() 
	{
		return this.groupItemId;
	}

	public final void setGroupItemId(final int groupItemId)
	{
		this.groupItemId = groupItemId;
	}
	
	public final long getUserOperation() {
		return this.userOperation;
	}

	public final void setUserOperation(final long userOperation) {
		this.userOperation = userOperation;
	}

	public final long getCompanyId() {
		return this.companyId;
	}

	public final void setCompanyId(final long companyId) {
		this.companyId = companyId;
	}

	public final double getTransferValue() {
		return this.transferValue;
	}

	public final void setTransferValue(final double transferValue) {
		this.transferValue = transferValue;
	}

	public final int getExpense() {
		return this.expense;
	}

	public final void setExpense(final int expense) {
		this.expense = expense;
	}
}