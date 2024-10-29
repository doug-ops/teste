/*
 * Date create 18/08/2023.
 */
package com.manager.systems.web.financial.cashier.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.utils.ConstantDataManager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CashierMovementReportItemDTO implements Serializable{

	private static final long serialVersionUID = 9198104395486753496L;
	
	private int weekYear;
	private String dateFrom;
	private String dateTo;
	private int movementTypeId;
	private String movementDescription;
	private int groupType;
	private double total;
	private int orderItem;
	private List<CashierMovementReportItemDetailDTO> detailsList;
	private boolean isAcumulated;
	
	public void addDetail(final CashierMovementReportItemDetailDTO detail) {
		if(this.detailsList == null) {
			this.detailsList = new ArrayList<>();
		}
		
		this.total+= detail.getDocumentValue();
		
		this.detailsList.add(detail);
	}
	
	public String getWeekYearDescription() {
		final StringBuilder result = new StringBuilder();
		result.append(String.valueOf(this.weekYear).substring(4,6));
		result.append("/");
		result.append(String.valueOf(this.weekYear).substring(0,4));
		return result.toString();
	}
	
	public String getDateFromString() {
		final StringBuilder result = new StringBuilder();

		final String[] dateArray = this.dateFrom.split(ConstantDataManager.TRACO);
		if(dateArray != null && dateArray.length == 3) {
			result.append(dateArray[2]);
			result.append("/");
			result.append(dateArray[1]);
			result.append("/");
			result.append(dateArray[0]);
		}

		return result.toString();
	}
	
	public String getDateToString() {
		final StringBuilder result = new StringBuilder();

		final String[] dateArray = this.dateTo.split(ConstantDataManager.TRACO);
		if(dateArray != null && dateArray.length == 3) {
			result.append(dateArray[2]);
			result.append("/");
			result.append(dateArray[1]);
			result.append("/");
			result.append(dateArray[0]);
		}

		return result.toString();
	}
}
