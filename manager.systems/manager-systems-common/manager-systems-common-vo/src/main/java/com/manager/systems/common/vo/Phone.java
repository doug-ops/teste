package com.manager.systems.common.vo;

import java.lang.reflect.Field;

public class Phone 
{
	private int ddd;
	private long number;
	private boolean principal;
	private boolean inactive;
	
	public Phone() 
	{
		super();
	}

	public final int getDdd()
	{
		return this.ddd;
	}

	public final void setDdd(final int ddd) 
	{
		this.ddd = ddd;
	}

	public final long getNumber() 
	{
		return this.number;
	}

	public final void setNumber(final long number) 
	{
		this.number = number;
	}

	public final boolean isPrincipal() 
	{
		return this.principal;
	}

	public final void setPrincipal(final boolean principal) 
	{
		this.principal = principal;
	}

	public final boolean isInactive() 
	{
		return this.inactive;
	}

	public final void setInactive(final boolean inactive)
	{
		this.inactive = inactive;
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