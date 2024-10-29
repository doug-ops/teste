package com.manager.systems.movement.product.bravo.dto;

import java.lang.reflect.Field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BravoMachineDTO {
	private long roomId;
	private long machineId;
	private String machineDescription;
	private long inputMovement;
	private long outputMovement;
	private String contract;
	private boolean inactive;
	
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