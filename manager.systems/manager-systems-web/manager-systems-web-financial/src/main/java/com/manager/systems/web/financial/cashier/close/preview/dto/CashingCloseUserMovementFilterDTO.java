/*
 * Date create 07/02/2024.
 */
package com.manager.systems.web.financial.cashier.close.preview.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CashingCloseUserMovementFilterDTO implements  Serializable {

	private static final long serialVersionUID = 5946041349739552503L;
	
	private int operation;
	private long userId;
	private long userChange;
	private String usersChildrenParent;
	private String companysId;
	private int status;
	private int weekYear;
	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;
	private boolean isClose;
}