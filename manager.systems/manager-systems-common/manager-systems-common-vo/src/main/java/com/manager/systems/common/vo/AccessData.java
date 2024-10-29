package com.manager.systems.common.vo;

import java.lang.reflect.Field;
import java.util.Date;

import com.manager.systems.common.utils.StringUtils;

public class AccessData 
{
	private String username;
	private String password;
	private Date expirationDate;
	private Date lastAcessDate;
	private Date UserLastAcessDate;
	
	public AccessData() 
	{
		super();
	}

	public final String getUsername()
	{
		return this.username;
	}

	public final void setUsername(final String username) 
	{
		this.username = username;
	}

	public final String getPassword() 
	{
		return this.password;
	}

	public final void setPassword(final String password) 
	{
		this.password = password;
	}
	
	public final Date getExpirationDate() 
	{
		return this.expirationDate;
	}

	public final void setExpirationDate(final Date expirationDate) 
	{
		this.expirationDate = expirationDate;
	}

	public final Date getLastAcessDate()
	{
		return this.lastAcessDate;
	}

	public final void setLastAcessDate(final Date lastAcessDate)
	{
		this.lastAcessDate = lastAcessDate;
	}

	public final Date getUserLastAcessDate() {
		return this.UserLastAcessDate;
	}

	public final void setUserLastAcessDate(final Date userLastAcessDate) {
		this.UserLastAcessDate = userLastAcessDate;
	}
	
	public final boolean isValidFormLogin()
	{
		boolean result = true;
		if(StringUtils.isNull(this.username))
		{
			result = false;
		}
		else if(StringUtils.isNull(this.password))
		{
			result = false;
		}
		return result;
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