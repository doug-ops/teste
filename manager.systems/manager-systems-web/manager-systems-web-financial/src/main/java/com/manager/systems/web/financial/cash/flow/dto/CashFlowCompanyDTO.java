package com.manager.systems.web.financial.cash.flow.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CashFlowCompanyDTO implements Serializable {
	private static final long serialVersionUID = -4673744717787568742L;
	
	private long companyId;
	private String companyDescription;
}
