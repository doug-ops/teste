package com.manager.systems.web.jobs.dto;

import java.time.LocalDateTime;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovementDataProductSincItemDTO 
{
	private long movementLegacyId;
	private int legacySystemId;
	private long productId;
	private String companyDescription;
	private long creditInFinal;
	private long creditOutFinal;
	private LocalDateTime readingDate;
	private LocalDateTime initialDate;
	private String movementType;
	private boolean processing;
	private boolean inactive;
	private byte[] versionRecord;
	private long changeUser;
	private String terminalType;
	private int companyId;
	private String companyName;
	private boolean registerEnabled;
	
	public MovementDataProductSincItemDTO() 
	{
		super();
	}
	
	public String getProductDescription() {
		String result = companyDescription;
		if(!StringUtils.isNull(terminalType)) {
			if(ConstantDataManager.COLETIVA_ALIAS.equalsIgnoreCase(terminalType)) {
				result = ConstantDataManager.COLETIVA_DESCRIPTION;
			}
			else {
				result = ConstantDataManager.INDIVIDUAL_DESCRITPION;
			}
		}
		return result;
	}
}