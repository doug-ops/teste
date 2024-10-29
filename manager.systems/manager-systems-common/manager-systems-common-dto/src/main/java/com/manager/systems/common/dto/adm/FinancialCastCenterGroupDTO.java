package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ChangeData;

public class FinancialCastCenterGroupDTO implements Serializable
{
	private static final long serialVersionUID = 8241902352644662831L;

	private int id;
	private String description;
	private ChangeData changeData;
	private boolean inactive;
	private List<IntegrationSystemsDTO> integrationSystemsValues = new ArrayList<IntegrationSystemsDTO>();
	
	public FinancialCastCenterGroupDTO() 
	{
		super();
	}

	public final int getId()
	{
		return this.id;
	}

	public final void setId(final int id)
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
	
	public final ChangeData getChangeData()
	{
		return this.changeData;
	}

	public final void setChangeData(final ChangeData changeData)
	{
		this.changeData = changeData;
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
	
	public final List<IntegrationSystemsDTO> getIntegrationSystemsValues() 
	{
		return this.integrationSystemsValues;
	}
	
	public final void addIntegrationSystemsValue(final String legacyId, final String integrationSystemId)
	{
		final IntegrationSystemsDTO integrationSystem = new IntegrationSystemsDTO();
		integrationSystem.setObjectId(String.valueOf(this.id));
		integrationSystem.setObjectType(ConstantDataManager.OBJECT_TYPE_PROD_GROUP);
		integrationSystem.setInactive(false);
		integrationSystem.setIntegrationSystemId(Integer.valueOf(integrationSystemId));
		integrationSystem.setLegacyId(legacyId);
		integrationSystem.setUserChange(this.changeData.getUserChange());
		final Calendar changeDate = Calendar.getInstance();
		changeDate.setTime(this.changeData.getChangeDate());
		integrationSystem.setChangeDate(changeDate);
		this.integrationSystemsValues.add(integrationSystem);
	}
	
	public void validateFormSave() throws Exception
	{
		if(this.id==0)
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_ID, ConstantDataManager.BLANK));
		}
		if(StringUtils.isNull(this.description))
		{
				throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_DESCRIPTION, ConstantDataManager.BLANK));				
		}
	}
	
	@Override
	public String toString() 
	{
		final StringBuilder result = new StringBuilder();
		final String newLine = System.getProperty("line.separator");

		result.append(this.getClass().getName());
		result.append(" Object {");
		result.append(newLine);

		final Field[] fields = this.getClass().getDeclaredFields();

		for (final Field field : fields) 
		{
			result.append("  ");
			try 
			{
				result.append(field.getName());
				result.append(": ");
				// requires access to private field:
				result.append(field.get(this));
			} 
			catch (final IllegalAccessException ex) 
			{
				
			}
			result.append(newLine);
		}
		result.append("}");
		return result.toString();
	}
}