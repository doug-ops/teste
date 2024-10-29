/**.
 * Date Create 23/08/2019
 */

package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.vo.ChangeData;

public class BankAccountDTO implements Serializable 
{
	private static final long serialVersionUID = 7462587933188719125L;
	
	private String id;
	private String description;
	private double bankBalanceAvailable;
	private double bankLimitAvailable;
	private String bankCode;
	private String bankAngency;
	private List<IntegrationSystemsDTO> integrationSystemsValues = new ArrayList<IntegrationSystemsDTO>();
	private boolean inactive;
	private boolean accumulateBalance;
	private ChangeData changeData;

	public BankAccountDTO() 
	{
		super();
	}

	public final String getId() 
	{
		return this.id;
	}

	public final void setId(final String id) 
	{
		this.id = id;
	}

	public final String getDescription() 
	{
		return this.description;
	}

	public final void setDescription(final String description) 
	{
		this.description = description;
	}

	public final double getBankBalanceAvailable() 
	{
		return this.bankBalanceAvailable;
	}

	public final void setBankBalanceAvailable(final double bankBalanceAvailable) 
	{
		this.bankBalanceAvailable = bankBalanceAvailable;
	}

	public final double getBankLimitAvailable() 
	{
		return this.bankLimitAvailable;
	}

	public final void setBankLimitAvailable(final double bankLimitAvailable) 
	{
		this.bankLimitAvailable = bankLimitAvailable;
	}

	public final String getBankCode() 
	{
		return this.bankCode;
	}

	public final void setBankCode(final String bankCode) 
	{
		this.bankCode = bankCode;
	}

	public final String getBankAngency() 
	{
		return this.bankAngency;
	}

	public final void setBankAngency(final String bankAngency) 
	{
		this.bankAngency = bankAngency;
	}

	public final List<IntegrationSystemsDTO> getIntegrationSystemsValues() 
	{
		return this.integrationSystemsValues;
	}
	
	public final void addIntegrationSystemsValue(final String legacyId, final String integrationSystemId)
	{
		final IntegrationSystemsDTO integrationSystem = new IntegrationSystemsDTO();
		integrationSystem.setObjectId(this.id);
		integrationSystem.setObjectType(ConstantDataManager.OBJECT_TYPE_BANK_ACCOUNT);
		integrationSystem.setInactive(false);
		integrationSystem.setIntegrationSystemId(Integer.valueOf(integrationSystemId));
		integrationSystem.setLegacyId(legacyId);
		integrationSystem.setUserChange(this.changeData.getUserChange());
		final Calendar changeDate = Calendar.getInstance();
		changeDate.setTime(this.changeData.getChangeDate());
		integrationSystem.setChangeDate(changeDate);
		this.integrationSystemsValues.add(integrationSystem);
	}

	public final boolean isInactive() 
	{
		return this.inactive;
	}
	
	public final int getInactiveInt()
	{
		return this.inactive ? 1 : 0;
	}

	public final void setInactive(final boolean inactive) 
	{
		this.inactive = inactive;
	}

	public final ChangeData getChangeData() 
	{
		return this.changeData;
	}
	
	public final void setChangeData(final ChangeData changeData) 
	{
		this.changeData = changeData;
	}

	public final boolean isAccumulateBalance() {
		return this.accumulateBalance;
	}

	public final void setAccumulateBalance(final boolean accumulateBalance) {
		this.accumulateBalance = accumulateBalance;
	}
}