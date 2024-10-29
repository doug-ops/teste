package com.manager.systems.movement.product.bravo.dto;

import java.lang.reflect.Field;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingBravoGamesJsonDTO 
{
	private Integer page;
    private Integer total;
    private Integer records;
    private List<BillingBravoGamesJsonItemDTO> rows;
    
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
