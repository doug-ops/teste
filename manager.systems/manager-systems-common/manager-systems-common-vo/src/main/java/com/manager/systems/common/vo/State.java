package com.manager.systems.common.vo;

import java.lang.reflect.Field;

public class State 
{
	private int id;
	private int ibgeCode;
	private String alias;
	private String description;
	
	public State() 
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

	public final int getIbgeCode() 
	{
		return this.ibgeCode;
	}

	public final void setIbgeCode(final int ibgeCode) 
	{
		this.ibgeCode = ibgeCode;
	}

	public final String getAlias()
	{
		return this.alias;
	}

	public final void setAlias(final String alias) 
	{
		this.alias = alias;
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