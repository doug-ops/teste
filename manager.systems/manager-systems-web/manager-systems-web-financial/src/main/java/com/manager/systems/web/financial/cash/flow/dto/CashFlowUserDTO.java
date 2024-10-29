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
public class CashFlowUserDTO implements Serializable {
	private static final long serialVersionUID = 2100587462372896183L;
	
	private long userId;
	private String userName;
}
