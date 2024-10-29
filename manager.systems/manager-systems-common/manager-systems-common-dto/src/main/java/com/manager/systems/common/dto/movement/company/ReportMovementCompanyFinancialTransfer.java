package com.manager.systems.common.dto.movement.company;

import java.io.Serializable;
import java.lang.reflect.Field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportMovementCompanyFinancialTransfer implements Serializable{
	
	private static final long serialVersionUID = -1182779957272416608L;
	
	private int groupId;
	private String groupDescription;
	private String transferDate;
	private String transferDescription;
	private double transferValue;
	private int executionOrder;
	private int groupExecutionOrder;
	private int groupItemExecutionOrder;
	
	public int getOrder() {
		return Integer.valueOf(this.getGroupExecutionOrder()+""+this.getGroupItemExecutionOrder()+""+this.getExecutionOrder());
	}
	
	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		final String newLine = System.getProperty("line.separator");

		result.append(this.getClass().getName());
		result.append(" Object {");
		result.append(newLine);

		final Field[] fields = this.getClass().getDeclaredFields();

		for (final Field field : fields) {
			result.append("  ");
			try {
				result.append(field.getName());
				result.append(": ");
				// requires access to private field:
				result.append(field.get(this));
			} catch (final IllegalAccessException ex) {

			}
			result.append(newLine);
		}
		result.append("}");
		return result.toString();
	}
}