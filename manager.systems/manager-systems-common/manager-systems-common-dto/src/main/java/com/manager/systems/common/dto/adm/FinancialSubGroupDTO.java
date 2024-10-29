package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ChangeData;

public class FinancialSubGroupDTO implements Serializable
{
	private static final long serialVersionUID = -2609739892878888080L;

	private String id;
	private String description;
	private String groupId;
	private String descriptionGroup;
	private String revenueSource;
	private String revenueSourceDescription;
	private String revenueType;
	private String revenueTypeDescription;
	private ChangeData changeData;
	private boolean inactive;
	private String maxIdSubGroup;
	
	public FinancialSubGroupDTO() 
	{
		super();
	}
	
	public FinancialSubGroupDTO(final String groupId, final boolean inactive, final ChangeData changeData) 
	{
		super();
		this.groupId = groupId;
		this.inactive = inactive;
		this.changeData = changeData;
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

	public final String getGroupId() 
	{
		return this.groupId;
	}

	public final void setGroupId(final String groupId) 
	{
		this.groupId = groupId;
	}

	public final String getDescriptionGroup() 
	{
		return this.descriptionGroup;
	}

	public final void setDescriptionGroup(final String descriptionGroup)
	{
		this.descriptionGroup = descriptionGroup;
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
	
	public final String getMaxIdSubGroup() {
		return this.maxIdSubGroup;
	}

	public final void setMaxIdSubGroup(final String maxIdSubGroup) {
		this.maxIdSubGroup = maxIdSubGroup;
	}
	
	public final String getRevenueSource() {
		return this.revenueSource;
	}

	public final void setRevenueSource(final String revenueSource) {
		this.revenueSource = revenueSource;
	}

	public final String getRevenueType() {
		return this.revenueType;
	}

	public final void setRevenueType(final String revenueType) {
		this.revenueType = revenueType;
	}

	public final String getRevenueSourceDescription() {
		return this.revenueSourceDescription;
	}

	public final  void setRevenueSourceDescription(final String revenueSourceDescription) {
		this.revenueSourceDescription = revenueSourceDescription;
	}

	public final String getRevenueTypeDescription() {
		return this.revenueTypeDescription;
	}

	public final void setRevenueTypeDescription(final String revenueTypeDescription) {
		this.revenueTypeDescription = revenueTypeDescription;
	}

	public void validateFormSave() throws Exception
	{
		if(!StringUtils.isLong(this.id))
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_ID, ConstantDataManager.BLANK));
		}
		if(StringUtils.isNull(this.description))
		{
				throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_DESCRIPTION, ConstantDataManager.BLANK));				
		}
		if(!StringUtils.isLong(this.groupId))
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_FINANCIAL_GROUP, ConstantDataManager.BLANK));
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