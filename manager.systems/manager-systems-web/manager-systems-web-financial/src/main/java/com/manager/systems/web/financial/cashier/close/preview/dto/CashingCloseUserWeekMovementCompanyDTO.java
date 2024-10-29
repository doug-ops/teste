/*
 * Date create 14/02/2024.
 */
package com.manager.systems.web.financial.cashier.close.preview.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.manager.systems.common.vo.ChangeData;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CashingCloseUserWeekMovementCompanyDTO implements Serializable {

	private static final long serialVersionUID = -2990977015124113550L;
	
	private String key;
	private long cashierClosingId;
	private long userId;
	private String userName;
	private int weekYear;
	private String dateFrom;
	private String dateTo;
	private String cashierClosingStatus;
	private long companyId;
	private String companyDescription;
	private long bankAccountId;
	private String bankAccountDescription;
	private double movementValue;
	private double pendingMovementValue;
	private double totalCompany;
	private double discountTotal;
	private double paymentTotal;	
	private double pendingMovementAfterValue;
	private ChangeData changeData;
	private String launchDate;
	private long launchUser;
	private String closeDate;
	private long closeUser;
	private boolean inactive;
	private int status;
	private String note;
	private long documentParentId;
	private List<CashingCloseUserWeekMovementCompanyItemProductDTO> products;
	private Map<Long, CashingCloseUserWeekMovementCompanyItemProductDTO> itemsProducts;
	private List<CashingCloseUserWeekMovementCompanyItemDTO> items;
	
	public void calculatePaymentTotal() {
		this.paymentTotal = (movementValue + pendingMovementValue - discountTotal - pendingMovementAfterValue);
	}
	
	public void addMovementCompanyItem(final CashingCloseUserWeekMovementCompanyItemDTO item) {
		if(this.itemsProducts == null) {
			this.itemsProducts = new TreeMap<Long, CashingCloseUserWeekMovementCompanyItemProductDTO>();
		}
		if(this.items == null) {
			this.items = new ArrayList<CashingCloseUserWeekMovementCompanyItemDTO>();
		}	
		
		this.documentParentId = item.getDocumentParentId();
		
		if(item.isProductMovement()) {
			CashingCloseUserWeekMovementCompanyItemProductDTO itemMap = this.itemsProducts.get(item.getProductId());
			if(itemMap == null) {
				itemMap = CashingCloseUserWeekMovementCompanyItemProductDTO.builder().build();
				itemMap.setProductId(item.getProductId());
			}
			
			if(item.isCredit()) {
				itemMap.setInputValue(item.getDocumentValue());
				itemMap.setInputDocumentMovementId(item.getDocumentMovementId());
			} else {
				itemMap.setOutputValue(item.getDocumentValue());
				itemMap.setOutputDocumentMovementId(item.getDocumentMovementId());
			}
			itemMap.calculateBalance();
			this.itemsProducts.put(item.getProductId(), itemMap);
		} else {
			this.items.add(item);
		}		
	}
}