package com.manager.systems.model.admin;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.model.IntegrationSystems;

public class ProductGroup implements Serializable
{
	private static final long serialVersionUID = -8463938356619742859L;

	private int id;
	private String description;
	private ChangeData changeData;
	private boolean inactive;
	private IntegrationSystems integrationSystems;
	
	public ProductGroup() 
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

	public final void setInactive(final boolean inactive)
	{
		this.inactive = inactive;
	}
	
	public IntegrationSystems getIntegrationSystems() 
	{
		return this.integrationSystems;
	}
	
	public void setIntegrationSystems(final IntegrationSystems integrationSystems) 
	{
		this.integrationSystems = integrationSystems;
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