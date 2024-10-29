package com.manager.systems.common.dto.dashboard;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DashboardCompanyExecutionWeekDTO implements Serializable {
	private static final long serialVersionUID = 4998338035847455580L;

	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;
	private String dateFromString;
	private String dateToString;
	private long user;
	private List<DashboardCompanyExecutionWeekItemDTO> itens = new ArrayList<DashboardCompanyExecutionWeekItemDTO>();
	private Map<Integer, DashboardCompanyExecutionWeekItemDTO> mapData = new TreeMap<Integer, DashboardCompanyExecutionWeekItemDTO>();
	private int selectedWeekYear;
	private String userChildrensParent;
	private int typeDashboard;
	
	public DashboardCompanyExecutionWeekDTO() {
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
	
	public final long getUser() {
		return this.user;
	}

	public final void setUser(final long user) {
		this.user = user;
	}

	public final String getDateFromString() {
		return this.dateFromString;
	}

	public final void setDateFromString(final String dateFromString) {
		this.dateFromString = dateFromString;
	}

	public final String getDateToString() {
		return this.dateToString;
	}

	public final void setDateToString(final String dateToString) {
		this.dateToString = dateToString;
	}

	public final List<DashboardCompanyExecutionWeekItemDTO> getItens() 
	{
		return this.itens;
	}
	
	public final void addItem(final DashboardCompanyExecutionWeekItemDTO item)
	{
		this.itens.add(item);
	}
	
	public final Map<Integer, DashboardCompanyExecutionWeekItemDTO> getMapData() {
		return this.mapData;
	}

	public final int getSelectedWeekYear() 
	{
		return this.selectedWeekYear;
	}

	public final void setSelectedWeekYear(final int selectedWeekYear) 
	{
		this.selectedWeekYear = selectedWeekYear;
	}

	public final void setUserChildrensParent(final String userChildrensParent) 
	{
		this.userChildrensParent = 	userChildrensParent;
	}
	
	public final String getUserChildrensParent() 
	{
		return this.userChildrensParent;
	}

	public final int getTypeDashboard() 
	{
		return typeDashboard;
	}

	public void setTypeDashboard(final int typeDashboard) 
	{
		this.typeDashboard = typeDashboard;
	}	
}