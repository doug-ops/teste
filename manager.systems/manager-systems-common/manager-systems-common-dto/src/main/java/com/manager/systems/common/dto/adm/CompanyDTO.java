package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.sql.Date;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDTO implements Serializable 
{
	private static final long serialVersionUID = 7927565459010117025L;
	
	private long id;
	private String description;
	private Date expiryData;
	private String expiryDataString;
	private String descriptionConcactId;
	private String backgroundColor;
	private long bankAccountOriginId;
	private long bankAccountDestinyId;
	private Integer weekYear;
	private int personTypeId;
	private String personTypeDescription;
	private boolean processMovementAutomatic;
	private long documentParentId;
	private String paymentDate;
	private String executionDate;
	private int registerOrder;
	
	
	public CompanyDTO() 
	{
		super();
	}

	public final void setDescription(final String description) 
	{
		this.description = description;
		this.descriptionConcactId = this.description + ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT + this.id +  ConstantDataManager.PARENTESES_RIGHT;
	}

	public final String getDescriptionIdConcact()
	{
		String concat = ConstantDataManager.BLANK;
		if(StringUtils.isNull(this.expiryDataString)) {
			concat=(this.description + ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT + this.id +  ConstantDataManager.PARENTESES_RIGHT);

		}else {
			concat = (this.description + ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT + this.id +  ConstantDataManager.PARENTESES_RIGHT + ConstantDataManager.SPACE + ConstantDataManager.TRACO + ConstantDataManager.SPACE + this.expiryDataString);

		}
		return concat;
	}
	
	public final String getIdString() {
		return String.valueOf(this.id);
	}
}