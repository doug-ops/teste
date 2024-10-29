/**
 * Date create 03/04/2020.
 */
package com.manager.systems.web.jobs.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.manager.systems.web.jobs.utils.ConstantDataManager;
import com.manager.systems.web.jobs.vo.OperationType;

public class MovementDataProccessDTO implements Serializable
{
	private static final long serialVersionUID = 1189443853814049133L;
	
	private long userId;
	private long companyId;
	private boolean offline;
	private StringBuilder movementsIds = new StringBuilder();
	private boolean preview;
	private OperationType operationType;
	
	/**
	 * Default Constructor.
	 */
	public MovementDataProccessDTO() 
	{
		super();
	}

	public final long getUserId() 
	{
		return this.userId;
	}

	public final long getCompanyId() 
	{
		return this.companyId;
	}

	public final boolean isOffline()
	{
		return this.offline;
	}

	public final String getMovementsIds() 
	{
		return this.movementsIds.toString();
	}

	public final boolean isPreview() 
	{
		return this.preview;
	}

	public final void setUserId(final long userId) 
	{
		this.userId = userId;
	}

	public final void setCompanyId(final long companyId) 
	{
		this.companyId = companyId;
	}

	public final void setOffline(final boolean offline) 
	{
		this.offline = offline;
	}

	public final void addMovementsId(final long movementId) 
	{
		if(this.movementsIds.length()>0)
		{
			this.movementsIds.append(ConstantDataManager.VIRGULA_STRING);
		}
		this.movementsIds.append(movementId);
	}

	public final void setPreview(final boolean preview) 
	{
		this.preview = preview;
	}

	public final OperationType getOperationType() 
	{
		return this.operationType;
	}

	public final void setOperation(final OperationType operationType)
	{
		this.operationType = operationType;
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