package com.manager.systems.common.dto;

public class ComboboxFilterDTO {
	private int operation;
	private boolean inactive;

	public ComboboxFilterDTO(final int operation, final boolean inactive) {
		super();
		this.operation = operation;
		this.inactive = inactive;
	}

	public final int getOperation() {
		return this.operation;
	}

	public final boolean isInactive() {
		return this.inactive;
	}

	public final void setOperation(final int operation) {
		this.operation = operation;
	}

	public final void setInactive(final boolean inactive) {
		this.inactive = inactive;
	}
}
