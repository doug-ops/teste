/*
 * Date create 09/02/2023.
 */
package com.manager.systems.web.jobs.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RomaneioFilterDTO implements Serializable {

	private static final long serialVersionUID = -4336189515206783685L;
	
	private Integer operation; 
	private Long companyId;
	private Long documentParentId;
	private Long userId;                                          
	private String documentNote;
    private Integer countWeekProcess;
}
