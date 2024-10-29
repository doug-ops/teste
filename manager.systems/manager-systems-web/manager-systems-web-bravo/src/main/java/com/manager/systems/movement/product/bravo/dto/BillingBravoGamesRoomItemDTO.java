package com.manager.systems.movement.product.bravo.dto;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingBravoGamesRoomItemDTO {
	private String roomName;
	private String titleQuantityOfflineMachine;
	private int quantityOfflineMachine;
	private long roomId;
	private int serverId;
	private int quantityMachine;
	private String urlMachinesRoom;
	private boolean inactive;
	private List<BravoMachineDTO> products;
	
	public void addProduct(final BravoMachineDTO product) {
		if(this.products == null) {
			this.products = new ArrayList<>();
		}
		
		this.products.add(product);
	}
	
	public String getRoomIdString() {
		String result = this.urlMachinesRoom;
		if(!StringUtils.isNull(this.urlMachinesRoom)) {
			result = result.replace("/room/", ConstantDataManager.BLANK);
			result = result.replace("/machines", ConstantDataManager.BLANK);			
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