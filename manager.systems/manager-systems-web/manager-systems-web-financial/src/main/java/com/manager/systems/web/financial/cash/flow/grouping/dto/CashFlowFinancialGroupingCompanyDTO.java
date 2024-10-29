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
public class CashFlowFinancialGroupingCompanyDTO implements Serializable {

	private static final long serialVersionUID = -743102390266512877L;
	
	private long companyId;
	private String companyDescription;
}
