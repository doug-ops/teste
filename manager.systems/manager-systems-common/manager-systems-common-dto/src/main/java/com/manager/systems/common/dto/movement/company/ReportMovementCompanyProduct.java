package com.manager.systems.common.dto.movement.company;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportMovementCompanyProduct implements Serializable{
	
	private static final long serialVersionUID = 8878766794238237877L;

	private int groupId;
	private String groupDescription;
	private long productId;
	private String productDescription;
	private long creditInInitial;
	private long creditInFinal;
	private long creditOutInitial;
	private long creditOutFinal;
	private long creditClockInitial;
	private long creditClockFinal;
	private double totalCreditIn;
	private double totalCreditOut;
	private double totalCreditClock;
	private double balance;
	private int executionOrder;
	private String cssBalance;
	private boolean isInput;
	private int groupExecutionOrder;
	private int groupItemExecutionOrder;
	private double salesPrice;
	
	public int getOrder() {
		return Integer.valueOf(this.getGroupExecutionOrder()+""+this.getGroupItemExecutionOrder()+""+this.getExecutionOrder());
	}
	
	public void calculateTotals() {
		this.balance = ((this.totalCreditIn) - (this.totalCreditClock + this.totalCreditOut));
		if(this.balance<0) {
			this.cssBalance = "color: red;";
		}
		else {
			this.cssBalance = "color: green;";
		}
		this.salesPrice =  ((this.balance==0 || (this.creditInFinal-this.creditInInitial) == 0) ? 0 : StringUtils.roundTo2Decimals((this.balance/(this.creditInFinal-this.creditInInitial))));
	}
	
	public void populateDataDocumentNote(final String documentNote) {
		if(documentNote!=null) {
			final String[] documentNoteArray = documentNote.split(ConstantDataManager.BARRA);
			if(documentNoteArray.length==3) {
				for (final String documentItem : documentNoteArray) {
					final String[] keyValueArray = documentItem.split(ConstantDataManager.DOIS_PONTOS);
					if(keyValueArray.length==2) {
						if(ConstantDataManager.INPUT_INITIAL_DOCUMENT_NOTE_ALIAS.equalsIgnoreCase(keyValueArray[0])) {
							this.creditInInitial = Long.parseLong(keyValueArray[1]);
							this.isInput = true;
						}
						else if(ConstantDataManager.INPUT_FINAL_DOCUMENT_NOTE_ALIAS.equalsIgnoreCase(keyValueArray[0])) {
							this.creditInFinal = Long.parseLong(keyValueArray[1]);
							this.isInput = true;
						}
						else if(ConstantDataManager.OUTPUT_INITIAL_DOCUMENT_NOTE_ALIAS.equalsIgnoreCase(keyValueArray[0])) {
							this.creditOutInitial = Long.parseLong(keyValueArray[1]);
							this.isInput = false;
						}
						else if(ConstantDataManager.OUTPUT_FINAL_DOCUMENT_NOTE_ALIAS.equalsIgnoreCase(keyValueArray[0])) {
							this.creditOutFinal = Long.parseLong(keyValueArray[1]);
							this.isInput = false;
						}
						else if(ConstantDataManager.CLOCK_INITIAL_DOCUMENT_NOTE_ALIAS.equalsIgnoreCase(keyValueArray[0])) {
							this.creditClockInitial = Long.parseLong(keyValueArray[1]);
							this.isInput = false;
						}
						else if(ConstantDataManager.CLOCK_FINAL_DOCUMENT_NOTE_ALIAS.equalsIgnoreCase(keyValueArray[0])) {
							this.creditClockFinal = Long.parseLong(keyValueArray[1]);
							this.isInput = false;
						}
					}
				}
			}
		}
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