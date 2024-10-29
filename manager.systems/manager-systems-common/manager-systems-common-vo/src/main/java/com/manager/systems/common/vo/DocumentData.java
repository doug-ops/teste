package com.manager.systems.common.vo;

import java.lang.reflect.Field;

public class DocumentData
{
	private long cnpj;
	private long cfp;
	private String rg;
	private String ie;
	
	public DocumentData() 
	{
		super();
	}

	public final long getCnpj()
	{
		return this.cnpj;
	}

	public final void setCnpj(final long cnpj)
	{
		this.cnpj = cnpj;
	}

	public final long getCfp()
	{
		return this.cfp;
	}

	public final void setCfp(final long cfp) 
	{
		this.cfp = cfp;
	}

	public final String getRg() 
	{
		return this.rg;
	}

	public final void setRg(final String rg) 
	{
		this.rg = rg;
	}
	
	public String getIe() 
	{
		return this.ie;
	}
	
	public void setIe(final String ie) 
	{
		this.ie = ie;
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