package com.manager.systems.common.dto.dashboard;

import java.io.Serializable;

public class MovementPendingDTO implements Serializable {
	private static final long serialVersionUID = 4998338035847455580L;

	private String customer;
	private String movementDate;
	private boolean online;
	private int countProducts;
	
	public MovementPendingDTO() {
		super();
	}

	public final String getCustomer() {
		return this.customer;
	}

	public final String getMovementDate() {
		return this.movementDate;
	}

	public final boolean isOnline() {
		return this.online;
	}

	public final int getCountProducts() {
		return this.countProducts;
	}

	public final void setCustomer(final String customer) {
		this.customer = customer;
	}

	public final void setMovementDate(final String movementDate) {
		this.movementDate = movementDate;
	}

	public final void setOnline(final boolean online) {
		this.online = online;
	}

	public final void setCountProducts(final int countProducts) {
		this.countProducts = countProducts;
	}
}