/*
 * Date create 18/09/2023.
 */
package com.manager.systems.common.dto.adm;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CompanyIntegrationSystemDTO implements Serializable {
	
	private static final long serialVersionUID = -4532889522911867696L;
	
	private long companyId;
	private String companyDescription;
	private String bravoRoomId;
}