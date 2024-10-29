package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ChangeData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessProfileDTO implements Serializable
{
	private static final long serialVersionUID = 2955470143015648402L;

	private int id;
	private String description;
	private String role;
	private double cashClosingMaxDiscount;
	private ChangeData changeData;
	private boolean inactive;
	private Map<Integer, AccessProfilePermissionDTO> permissions;
	
	
	public final int getInactiveInt()
	{
		return this.inactive ? 1 : 0;
	}

	public void validateFormSave() throws Exception
	{
		if(this.id==0)
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_ID, ConstantDataManager.BLANK));
		}
		if(StringUtils.isNull(this.description))
		{
				throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_DESCRIPTION, ConstantDataManager.BLANK));				
		}
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

	public final void addPermissions(final Integer key, AccessProfilePermissionDTO value)
	{
		this.permissions.put(key, value);
	}	
}