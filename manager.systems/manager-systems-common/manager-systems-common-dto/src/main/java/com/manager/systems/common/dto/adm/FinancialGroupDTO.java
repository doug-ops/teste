package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.vo.ChangeData;

public class FinancialGroupDTO implements Serializable
{
	private static final long serialVersionUID = 4092182836229660106L;

	private String id;
	private String description;
	private ChangeData changeData;
	private boolean inactive;
	private String idMax;
	private List<FinancialSubGroupDTO> subgroups = new ArrayList<FinancialSubGroupDTO>();
	
	public FinancialGroupDTO() 
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
	
	public final String getIdMax() {
		return this.idMax;
	}

	public void setIdMax(final String idMax) {
		this.idMax = idMax;
	}
	
	public final List<FinancialSubGroupDTO> getSubgroups() {
		return this.subgroups;
	}
	
	public final void addSubGroup(final FinancialSubGroupDTO item) {
		this.subgroups.add(item);
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