package com.manager.systems.model;

import java.io.Serializable;
import java.lang.reflect.Field;

public class IntegrationSystems implements Serializable
{
	private static final long serialVersionUID = -9111099610230654881L;

	private int integrationSystemId;
	private String integrationId;
	
	public IntegrationSystems() 
	{
		super();
	}
	
	public IntegrationSystems(final int integrationSystemId, final String integrationId) 
	{
		super();
		this.integrationSystemId = integrationSystemId;
		this.integrationId = integrationId;
	}

	public final int getIntegrationSystemId() 
	{
		return this.integrationSystemId;
	}

	public final void setIntegrationSystemId(final int integrationSystemId) 
	{
		this.integrationSystemId = integrationSystemId;
	}

	public final String getIntegrationId() 
	{
		return this.integrationId;
	}

	public final void setIntegrationId(final String integrationId) 
	{
		this.integrationId = integrationId;
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