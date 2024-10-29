package com.manager.systems.web.financial.cash.flow.grouping.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CashFlowFinancialGroupingUserDTO implements Serializable {

	private static final long serialVersionUID = 5238364586567688136L;
	
	private long userId;
	private String userName;
}
