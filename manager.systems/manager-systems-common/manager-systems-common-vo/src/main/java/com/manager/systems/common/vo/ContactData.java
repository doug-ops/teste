package com.manager.systems.common.vo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ContactData
{	
	private String email;
	private String site;
	private List<Phone> phones = new ArrayList<Phone>();
	
	public ContactData() 
	{
		super();
	}

	public final String getEmail() 
	{
		return this.email;
	}

	public final String getSite() 
	{
		return this.site;
	}

	public final List<Phone> getPhones() 
	{
		return this.phones;
	}

	public final void setEmail(final String email) 
	{
		this.email = email;
	}

	public final void setSite(final String site) 
	{
		this.site = site;
	}
	
	public final void addPhone(final Phone phone)
	{
		this.phones.add(phone);
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