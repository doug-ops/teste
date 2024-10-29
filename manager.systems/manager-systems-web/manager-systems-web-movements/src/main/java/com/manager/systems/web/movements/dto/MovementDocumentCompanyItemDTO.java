/**
 *Create Date 21/02/2022
 */
package com.manager.systems.web.movements.dto;

import java.io.Serializable;

public class MovementDocumentCompanyItemDTO implements Serializable 
{
	private static final long serialVersionUID = -5234403116121957942L;

	private long companyId;
	private String companyDescription;
	private double total;
	
	public MovementDocumentCompanyItemDTO() {
		super();
	}

	public final Long getCompanyId() {
		return this.companyId;
	}

	public final void setCompanyId(final Long companyId) {
		this.companyId = companyId;
	}

	public final String getCompanyDescription() {
		return this.companyDescription;
	}

	public final void setCompanyDescription(final String companyDescription) {
		this.companyDescription = companyDescription;
	}

	public final double getTotal() {
		return this.total;
	}

	public final void setTotal(final double total) {
		this.total = total;
	}
	
	public final void sumTotal(final double total)
	{
		this.total += total;
	}
}