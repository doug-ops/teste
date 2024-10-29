package com.manager.systems.common.dto.movement.company;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompanyMovementExecutionDTO implements Serializable
{
private static final long serialVersionUID = 7927565459010117025L;
	
	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;
	private String companyId;
	private long userId;
	private Date expirationDate;
	private long bankAccountOriginId;
	private long bankAccountDestinyId;
	private Integer weekYear;
	private List<CompanyMovementExecutionItemDTO> itens = new ArrayList<CompanyMovementExecutionItemDTO>();

	
	public CompanyMovementExecutionDTO() 
	{
		super();
	}
	
	public final LocalDateTime getDateFrom()
	{
		return this.dateFrom;
	}

	public final void setDateFrom(final LocalDateTime dateFrom) 
	{
		this.dateFrom = dateFrom;
	}

	public final LocalDateTime getDateTo()
	{
		return this.dateTo;
	}

	public final void setDateTo(final LocalDateTime dateTo) 
	{
		this.dateTo = dateTo;
	}
	
	public final String getCompanyId() {
		return this.companyId;
	}

	public final void setCompanyId(final String companyId) {
		this.companyId = companyId;
	}

	public final long getUserId() {
		return this.userId;
	}

	public final void setUserId(final long userId) {
		this.userId = userId;
	}
	
	public final Date getExpirationDate() {
		return this.expirationDate;
	}
	
	public final void setExpirationDate(final Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public final long getBankAccountOriginId() {
		return this.bankAccountOriginId;
	}

	public final void setBankAccountOriginId(final long bankAccountOriginId) {
		this.bankAccountOriginId = bankAccountOriginId;
	}
	
	public final long getBankAccountDestinyId() 
	{
		return this.bankAccountDestinyId;
	}

	public final void setBankAccountDestinyId(final long bankAccountDestinyId) 
	{
		this.bankAccountDestinyId = bankAccountDestinyId;
	}
	
	public final Integer getWeekYear() {
		return this.weekYear;
	}

	public final void setWeekYear(final Integer weekYear) {
		this.weekYear = weekYear;
	}

	public final List<CompanyMovementExecutionItemDTO> getItens() 
	{
		return this.itens;
	}
	
	public final void addItem(final CompanyMovementExecutionItemDTO item)
	{
		this.itens.add(item);
	}
	
}