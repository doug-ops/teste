package com.manager.systems.model;

import java.lang.reflect.Field;

public class AccessProfile
{
	private int id;
	private String description;
	private String role;
	private boolean inactive;
	
	/**
	 * Default Constructor.
	 */
	public AccessProfile() 
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

	public final String getRole() 
	{
		return this.role;
	}

	public final void setRole(final String role)
	{
		this.role = role;
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