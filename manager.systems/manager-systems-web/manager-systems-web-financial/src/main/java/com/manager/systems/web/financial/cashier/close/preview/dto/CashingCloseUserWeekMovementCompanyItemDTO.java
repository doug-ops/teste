/*
 * Date create 14/02/2024.
 */
package com.manager.systems.web.financial.cashier.close.preview.dto;

import java.io.Serializable;

import com.manager.systems.common.vo.ChangeData;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CashingCloseUserWeekMovementCompanyItemDTO implements Serializable {

	private static final long serialVersionUID = -2990977015124113550L;
	
	private long cashierClosingId;
	private long userId;
	private String userName;
	private int weekYear;
	private String dateFrom;
	private String dateTo;
	private long companyId;
	private long documentMovementId;
	private long documentParentId;
	private boolean residue;
	private long productId;
	private boolean productMovement;
	private boolean credit;
	private double documentValue;
	private String documentNote;
	private ChangeData changeData;
	private boolean inactive;
}