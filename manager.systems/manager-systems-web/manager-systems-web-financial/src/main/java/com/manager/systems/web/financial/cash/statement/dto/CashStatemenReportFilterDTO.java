/*
 * Date create 25/01/2024.
 */
package com.manager.systems.web.financial.cash.statement.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CashStatemenReportFilterDTO implements Serializable {

	private static final long serialVersionUID = -794542032926943949L;
	
	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;
	private String usersChildrenParent;
	private long companyId;
}
