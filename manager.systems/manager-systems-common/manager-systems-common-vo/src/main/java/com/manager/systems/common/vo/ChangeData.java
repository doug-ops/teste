package com.manager.systems.common.vo;

import java.lang.reflect.Field;
import java.util.Date;

import com.manager.systems.common.utils.StringUtils;

public class ChangeData 
{
	private Date creationDate;
	private long userCreation;
	private String usernameCreation;
	private Date changeDate;
	private String changeDateString;
	private long userChange;
	private String usernameChange;
	
	public ChangeData() 
	{
		super();
	}
	
	public ChangeData(final long userChange)
	{
		super();
		this.userChange = userChange;
	}

	public final Date getCreationDate() 
	{
		return this.creationDate;
	}

	public final void setCreationDate(final Date creationDate)
	{
		this.creationDate = creationDate;
	}

	public final long getUserCreation() 
	{
		return this.userCreation;
	}

	public final void setUserCreation(final long userCreation)
	{
		this.userCreation = userCreation;
	}

	public final Date getChangeDate() 
	{
		return this.changeDate;
	}

	public final void setChangeDate(final Date changeDate)
	{
		this.changeDate = changeDate;
		if(this.changeDate!=null)
		{
			this.changeDateString = StringUtils.formatDate(this.changeDate, StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS);
		}
	}
	
	public String getChangeDateString() 
	{
		return this.changeDateString;
	}

	public final long getUserChange() 
	{
		return this.userChange;
	}

	public final void setUserChange(final long userChange) 
	{
		this.userChange = userChange;
	}
	
	public final String getUsernameCreation() 
	{
		return this.usernameCreation;
	}

	public final void setUsernameCreation(final String usernameCreation)
	{
		this.usernameCreation = usernameCreation;
	}

	public final String getUsernameChange() 
	{
		return this.usernameChange;
	}

	public final void setUsernameChange(final String usernameChange) 
	{
		this.usernameChange = usernameChange;
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