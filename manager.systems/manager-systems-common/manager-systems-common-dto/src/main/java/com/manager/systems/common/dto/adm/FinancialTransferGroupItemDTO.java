package com.manager.systems.common.dto.adm;

import java.io.Serializable;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.vo.ChangeData;

public class FinancialTransferGroupItemDTO implements Serializable
{
	private static final long serialVersionUID = 2397854657167108449L;

	private int id;
	private int groupId;
	private int bankAccountOriginId;
	private int bankAccountDestinyId;
	private int order;
    private String description;	
	private long providerId;
	private String providerName;
	private String finantialGroupId;
	private String finantialGroupName;
	private String finantialSubGroupId;
	private String finantialSubGroupName;
    private int transferType;
    private int launchType;
    private double valueTransfer;
    private int bankAccountId;
    private String bankAccountDescription;
    private String groupSubGroupConcactId;
    private int groupProductId;
    private String groupProductDescription;
    private int subGroupProductId;
    private String subGroupProductDescription;
    private boolean creditDebit;
    private int transferState;
    private int expense;
    private boolean overTotal;
    private boolean useRemainingBalance;
	private boolean inactive;
	private ChangeData changeData;
	private String groupDescription;
	private int groupExecutionOrder;
	
	public FinancialTransferGroupItemDTO() 
	{
		super();
	}
	
	public int getId() 
	{
		return this.id;
	}
	
	public void setId(final int id) 
	{
		this.id = id;
	}
	
	public int getGroupId() 
	{
		return this.groupId;
	}
	
	public void setGroupId(final int groupId) 
	{
		this.groupId = groupId;
	}

	public final int getBankAccountOriginId()
	{
		return this.bankAccountOriginId;
	}

	public final void setBankAccountOriginId(final int bankAccountOriginId) 
	{
		this.bankAccountOriginId = bankAccountOriginId;
	}

	public final int getBankAccountDestinyId() 
	{
		return this.bankAccountDestinyId;
	}

	public final void setBankAccountDestinyId(final int bankAccountDestinyId)
	{
		this.bankAccountDestinyId = bankAccountDestinyId;
	}

	public final int getOrder() 
	{
		return this.order;
	}

	public final void setOrder(final int order) 
	{
		this.order = order;
	}

	public final String getDescription()
	{
		return this.description;
	}

	public final void setDescription(final String description)
	{
		this.description = description;
	}

	public final long getProviderId() 
	{
		return this.providerId;
	}

	public final void setProviderId(final long providerId)
	{
		this.providerId = providerId;
	}
	
	public String getProviderName() 
	{
		return this.providerName;
	}
	
	public void setProviderName(final String providerName) 
	{
		this.providerName = providerName;
	}

	public final double getValueTransfer() 
	{
		return this.valueTransfer;
	}

	public final void setValueTransfer(final double valueTransfer)
	{
		this.valueTransfer = valueTransfer;
	}

	public final int getBankAccountId() 
	{
		return this.bankAccountId;
	}

	public final void setBankAccountId(final int bankAccountId) 
	{
		this.bankAccountId = bankAccountId;
	}
	
	public String getBankAccountDescription() 
	{
		return this.bankAccountDescription;
	}
	
	public void setBankAccountDescription(final String bankAccountDescription) 
	{
		this.bankAccountDescription = bankAccountDescription;
	}

	public final boolean isCreditDebit() 
	{
		return this.creditDebit;
	}

	public final void setCreditDebit(final boolean creditDebit)
	{
		this.creditDebit = creditDebit;
	}

	public final int getTransferState() 
	{
		return this.transferState;
	}

	public final void setTransferState(final int transferState)
	{
		this.transferState = transferState;
	}
	
	public final int getTransferType() 
	{
		return this.transferType;
	}
	
	public final void setTransferType(final int transferType) 
	{
		this.transferType = transferType;
	}

	public final int getLaunchType() {
		return this.launchType;
	}

	public final void setLaunchType(final int launchType) {
		this.launchType = launchType;
	}

	public final boolean isOverTotal() 
	{
		return this.overTotal;
	}

	public final void setOverTotal(final boolean overTotal) 
	{
		this.overTotal = overTotal;
	}
	
	public final boolean isUseRemainingBalance() 
	{
		return this.useRemainingBalance;
	}

	public final void setUseRemainingBalance(final boolean useRemainingBalance) 
	{
		this.useRemainingBalance = useRemainingBalance;
	}
	
	public final int getExpense() 
	{
		return this.expense;
	}
	
	public final void setExpense(final int expense)
	{
		this.expense = expense;
	}
	
	public boolean isInactive() 
	{
		return this.inactive;
	}
	
	public void setInactive(final boolean inactive) 
	{
		this.inactive = inactive;
	}
	
	public final int getInactiveInt()
	{
		return (this.inactive ? 1 : 0);
	}

	public final ChangeData getChangeData()
	{
		return this.changeData;
	}

	public final void setChangeData(final ChangeData changeData)
	{
		this.changeData = changeData;
	}
	public final String getGroupDescription()
	{
		return this.groupDescription;
	}

	public final void setGroupDescription(final String groupDescription)
	{
		this.groupDescription = groupDescription;
	}
	
	public final int getGroupExecutionOrder() 
	{
		return this.groupExecutionOrder;
	}

	public final void setGroupExecutionOrder(final int groupExecutionOrder) 
	{
		this.groupExecutionOrder = groupExecutionOrder;
	}

	public final String getFinantialGroupId() {
		return finantialGroupId;
	}

	public final void setFinantialGroupId(final String finantialGroupId) {
		this.finantialGroupId = finantialGroupId;
	}

	public final String getFinantialGroupName() {
		return this.finantialGroupName;
	}

	public final void setFinantialGroupName(final String finantialGroupName) {
		this.finantialGroupName = finantialGroupName;
	}

	public final String getFinantialSubGroupId() {
		return this.finantialSubGroupId;
	}

	public final void setFinantialSubGroupId(final String finantialSubGroupId) {
		this.finantialSubGroupId = finantialSubGroupId;
	}

	public final String getFinantialSubGroupName() {
		return this.finantialSubGroupName;
	}

	public final void setFinantialSubGroupName(final String finantialSubGroupName) {
		this.finantialSubGroupName = finantialSubGroupName;
	}

	public final int getGroupProductId() {
		return this.groupProductId;
	}

	public final void setGroupProductId(final int groupProductId) {
		this.groupProductId = groupProductId;
	}

	public final String getGroupProductDescription() {
		return this.groupProductDescription;
	}

	public final void setGroupProductDescription(final String groupProductDescription) {
		this.groupProductDescription = groupProductDescription;
	}
	
	public final int getSubGroupProductId() {
		return this.subGroupProductId;
	}

	public final void setSubGroupProductId(final int subGroupProductId) {
		this.subGroupProductId = subGroupProductId;
		if(this.groupProductId > 0 && this.subGroupProductId > 0) {
			this.setGroupSubGroupConcactId(this.subGroupProductId + ConstantDataManager.UNDERSCORE + this.groupProductId);
		}
	}

	public final String getSubGroupProductDescription() {
		return this.subGroupProductDescription;
	}

	public final void setSubGroupProductDescription(final String subGroupProductDescription) {
		this.subGroupProductDescription = subGroupProductDescription;
	}

	public final String getGroupSubGroupConcactId() {
		return this.groupSubGroupConcactId;
	}

	public final void setGroupSubGroupConcactId(final String groupSubGroupConcactId) {
		this.groupSubGroupConcactId = groupSubGroupConcactId;
	}
}