/*
 * Create date 16/01/2023.
 */
package com.manager.systems.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyLastMovementExecutedDTO implements Serializable {

	private static final long serialVersionUID = -4730793328802293214L;
	
	private Long documentParentId;
	private Integer companyId;
	private String dateTimeExecution;
}
