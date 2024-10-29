/*
 * Date create 18/08/2023.
 */
package com.manager.systems.web.financial.cashier.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CashierMovementReportFilterDTO implements Serializable {

	private static final long serialVersionUID = -1089643468324066443L;
	
	private int operation;
	private Long companyId;
	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;
}
