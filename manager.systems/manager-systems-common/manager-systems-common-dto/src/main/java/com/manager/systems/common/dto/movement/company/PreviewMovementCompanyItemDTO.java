package com.manager.systems.common.dto.movement.company;

import java.io.Serializable;
import java.lang.reflect.Field;

public class PreviewMovementCompanyItemDTO implements Serializable {
	private static final long serialVersionUID = -1303070399975870203L;

	private int executionOrder;
	private int groupId;
	private String descriptionGroup;
	private long companyId;
	private boolean credit;
	private double documentValue;
	private String documentNote;
	private String paymentDate;
	private long input;
	private long output;
	private long productId;
	private String productDescription;
	private double balance;
	private boolean productMovement;
	private long providerId;
	private String providerDescription;
	private long documentParentId;
	private long initialEntry;
	private long finalEntry;
	private long initialOutput;
	private long finalOutput;
	private String typeWatch;
	private String initialDate;
	private String readingDate;
	private int groupExecutionOrder;
	private int groupItemExecutionOrder;

	public PreviewMovementCompanyItemDTO() {
		super();
	}

	public final int getExecutionOrder() {
		return this.executionOrder;
	}

	public final void setExecutionOrder(final int executionOrder) {
		this.executionOrder = executionOrder;
	}
	
	public final int getGroupExecutionOrder() {
		return this.groupExecutionOrder;
	}

	public final void setGroupExecutionOrder(final int groupExecutionOrder) {
		this.groupExecutionOrder = groupExecutionOrder;
	}

	public final int getGroupItemExecutionOrder() {
		return this.groupItemExecutionOrder;
	}

	public final void setGroupItemExecutionOrder(final int groupItemExecutionOrder) {
		this.groupItemExecutionOrder = groupItemExecutionOrder;
	}
	
	public int getOrder() {
		return Integer.valueOf(this.getGroupExecutionOrder()+""+this.getGroupItemExecutionOrder()+""+this.getExecutionOrder());
	}
	
	public final boolean isCredit() {
		return this.credit;
	}

	public final void setCredit(final boolean credit) {
		this.credit = credit;
	}

	public final double getDocumentValue() {
		return this.documentValue;
	}

	public final void setDocumentValue(final double documentValue) {
		this.documentValue = documentValue;
	}

	public final String getDocumentNote() {
		return this.documentNote;
	}

	public final void setDocumentNote(final String documentNote) {
		this.documentNote = documentNote;
	}

	public final String getPaymentDate() {
		return this.paymentDate;
	}

	public final void setPaymentDate(final String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public final long getInput() {
		return this.input;
	}

	public final void setInput(final long input) {
		this.input = input;
	}

	public final long getOutput() {
		return this.output;
	}

	public final void setOutput(final long output) {
		this.output = output;
	}

	public final long getProductId() {
		return this.productId;
	}

	public final void setProductId(final long productId) {
		this.productId = productId;
	}

	public final int getGroupId() {
		return this.groupId;
	}

	public final void setGroupId(final int groupId) {
		this.groupId = groupId;
	}

	public final String getDescriptionGroup() {
		return this.descriptionGroup;
	}

	public final void setDescriptionGroup(final String descriptionGroup) {
		this.descriptionGroup = descriptionGroup;
	}

	public final long getCompanyId() {
		return this.companyId;
	}

	public final void setCompanyId(final long companyId) {
		this.companyId = companyId;
	}

	public final double getBalance() {
		return this.balance;
	}

	public final void setBalance(final double balance) {
		this.balance = balance;
	}

	public final boolean isProductMovement() {
		return this.productMovement;
	}

	public final void setProductMovement(final boolean productMovement) {
		this.productMovement = productMovement;
	}

	public final long getProviderId() {
		return this.providerId;
	}

	public final void setProviderId(final long providerId) {
		this.providerId = providerId;
	}

	public final String getProviderDescription() {
		return this.providerDescription;
	}

	public final void setProviderDescription(final String providerDescription) {
		this.providerDescription = providerDescription;
	}

	public final long getDocumentParentId() {
		return this.documentParentId;
	}

	public final void setDocumentParentId(final long documentParentId) {
		this.documentParentId = documentParentId;
	}

	public final long getInitialEntry() {
		return this.initialEntry;
	}

	public final void setInitialEntry(final long initialEntry) {
		this.initialEntry = initialEntry;
	}

	public final long getFinalEntry() {
		return this.finalEntry;
	}

	public final void setFinalEntry(final long finalEntry) {
		this.finalEntry = finalEntry;
	}

	public final long getInitialOutput() {
		return this.initialOutput;
	}

	public final void setInitialOutput(final long initialOutput) {
		this.initialOutput = initialOutput;
	}

	public final long getFinalOutput() {
		return this.finalOutput;
	}

	public final void setFinalOutput(final long finalOutput) {
		this.finalOutput = finalOutput;
	}

	public String getTypeWatch() {
		return this.typeWatch;
	}

	public void setTypeWatch(final String typeWatch) {
		this.typeWatch = typeWatch;
	}

	public final String getInitialDate() {
		return this.initialDate;
	}

	public final String getReadingDate() {
		return this.readingDate;
	}

	public final void setInitialDate(final String initialDate) {
		this.initialDate = initialDate;
	}

	public final void setReadingDate(final String readingDate) {
		this.readingDate = readingDate;
	}

	public final String getProductDescription() {
		return this.productDescription;
	}

	public final void setProductDescription(final String productDescription) {
		this.productDescription = productDescription;
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