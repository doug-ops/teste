package com.manager.systems.common.vo;

import java.io.Serializable;
import java.lang.reflect.Field;

public class Country implements Serializable 
{
	private static final long serialVersionUID = -2241487847542607911L;
	
	private int id;
	private int ibgeCode;
	private int ddi;
	private String description;
	
	public Country() 
	{
		super();
	}

	public final int getId()
	{
		return this.id;
	}

	public final void setId(int id)
	{
		this.id = id;
	}

	public final int getIbgeCode() 
	{
		return this.ibgeCode;
	}

	public final void setIbgeCode(final int ibgeCode) 
	{
		this.ibgeCode = ibgeCode;
	}

	public final int getDdi() 
	{
		return this.ddi;
	}

	public final void setDdi(final int ddi) 
	{
		this.ddi = ddi;
	}

	public final String getDescription() 
	{
		return this.description;
	}

	public final void setDescription(final String description) 
	{
		this.description = description;
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