package com.manager.systems.movement.product.bravo.dto;

import java.lang.reflect.Field;
import java.util.List;

import com.manager.systems.common.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingBravoGamesJsonItemDTO 
{
	private Integer id;
    private List<String> cell;
    public BravoMachineDTO getMachine(){
    	BravoMachineDTO machine = null;
    	int count = 0;
    	for (final String cell : this.cell) {
    		switch (count) {
			case 0:
				machine = new BravoMachineDTO();
				final int initialPosition = cell.indexOf("\">");
				final int lastPosition = cell.indexOf("</a>");
				if(initialPosition>-1) {
					final String content = cell.substring((initialPosition+2), lastPosition);
					machine.setMachineId(Long.valueOf(content));
				}
				break;
			case 1:
				machine.setMachineDescription(StringUtils.lTrim(cell).trim());
				break;
			case 2:
				machine.setContract(StringUtils.lTrim(cell).trim());
				break;
			case 6:
				final String inputMovement = StringUtils.lTrim(cell).trim();
				if(StringUtils.isLong(inputMovement)) {
					machine.setInputMovement(Long.parseLong(inputMovement));					
				}
				break;
			case 7:
				final String outputMovement = StringUtils.lTrim(cell).trim();
				if(StringUtils.isLong(outputMovement)) {
					machine.setOutputMovement(Long.parseLong(outputMovement));					
				}
				break;
			default:
				break;
			}
    		count++;
		}
    	return machine;
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