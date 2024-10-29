/*
 * Date create 07/02/2024.
 */
package com.manager.systems.web.financial.cashier.close.preview.dto;

import java.io.Serializable;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.vo.ChangeData;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CashingCloseUserMovementDTO implements Serializable {

	private static final long serialVersionUID = -8020543821495806940L;
	
	private long cashierClosingId;
	private long userId;
	private String userName;
	private int weekYear;
	private String dateFrom;
	private String dateTo;
	private int cashierClosingStatus;
	private ChangeData changeData;
	private boolean inactive;
	private String cashierClosingLabel;
	private int countOpen;
	private int countLaunch;
	private int countClose;
	
	public void sumStatusCompany(final int statusCompany) {
		switch (statusCompany) {
		case 0:
			this.countOpen = this.countOpen + 1;
			break;
		case 1:
			this.countLaunch = this.countLaunch + 1;
			break;
		case 2:
			this.countClose = this.countClose + 1;
			break;			
		default:
			break;
		}
	}
	
	public String getMovementLabel() {
		final StringBuilder result = this.processMovementLabel();		
		return result.toString();
	}
	
	public void setLabel() {
		final StringBuilder result = this.processMovementLabel();
		
		this.setCashierClosingLabel(result.toString());
	}

	private final StringBuilder processMovementLabel() {
		final String year = String.valueOf(this.weekYear).substring(0, 4);
		final String week = String.valueOf(this.weekYear).substring(4, 6);		
		final StringBuilder result = new StringBuilder();
		result.append(ConstantDataManager.LABEL_WEEK);
		result.append(ConstantDataManager.SPACE);
		result.append(week);
		result.append(ConstantDataManager.BARRA);
		result.append(year);
		result.append(ConstantDataManager.SPACE);
		result.append(ConstantDataManager.LABEL_FROM);
		result.append(ConstantDataManager.SPACE);
		result.append(this.dateFrom);
		result.append(ConstantDataManager.SPACE);
		result.append(ConstantDataManager.LABEL_TO);
		result.append(ConstantDataManager.SPACE);
		result.append(this.dateTo);
		result.append(ConstantDataManager.SPACE);
		result.append(ConstantDataManager.TRACO);
		result.append(ConstantDataManager.SPACE);
		result.append(ConstantDataManager.PARENTESES_LEFT);
		result.append("A: " + this.countOpen);
		result.append(ConstantDataManager.SPACE);
		result.append("L: " + this.countLaunch);
		result.append(ConstantDataManager.SPACE);
		result.append("F: " + this.countClose);
		result.append(ConstantDataManager.PARENTESES_RIGHT);
		return result;
	}
}