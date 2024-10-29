/*
 * Date create 14/02/2024.
 */
package com.manager.systems.web.financial.cashier.close.preview.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CashingCloseUserWeekMovementDTO implements Serializable {

	private static final long serialVersionUID = -2990977015124113550L;
	
	private long cashierClosingId;
	private long userId;
	private String userName;
	private int weekYear;
	private String dateFrom;
	private String dateTo;
	private String cashierClosingStatus;
	private ChangeData changeData;
	private boolean inactive;
	private int status;
	private String note;
	
	private double totalMovement;
	private double totalPendingMovement;
	private double totalCompany;
	private double totalDiscount;
	private double totalPayment;
	
	private Map<Long, CashingCloseUserWeekMovementCompanyDTO> companys;
	private List<CashingCloseUserWeekMovementCompanyLaunchDTO> launchs;
	private List<Combobox> bankAccountsDestiny;
	
	public void addMovementCompany(final CashingCloseUserWeekMovementCompanyDTO item) {
		if(companys == null) {
			companys = new TreeMap<>();
		}
		
		this.totalMovement += item.getMovementValue();
		this.totalPendingMovement += item.getPendingMovementValue();
		this.totalDiscount += item.getDiscountTotal();
		this.totalCompany += item.getTotalCompany();
		this.totalPayment =+ item.getPaymentTotal();
		
		this.companys.put(item.getCompanyId(), item);
	}
	
	public void addMovementCompanyItem(final CashingCloseUserWeekMovementCompanyItemDTO item) {
		if(this.companys != null) {
			final CashingCloseUserWeekMovementCompanyDTO company = this.companys.get(item.getCompanyId());
			if(company != null) {
				company.addMovementCompanyItem(item);
			}
		}
	}
}