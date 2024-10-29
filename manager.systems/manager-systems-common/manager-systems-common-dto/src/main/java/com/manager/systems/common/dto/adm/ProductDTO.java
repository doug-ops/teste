package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ChangeData;

public class ProductDTO implements Serializable
{
	private static final long serialVersionUID = 2590189280685677749L;

	private String id;
	private String description;
	private boolean inactive;
	private String companyId;
	private String companyDescription;
	private String groupId;
	private String groupDescription;
	private String subGroupId;
	private String subGroupDescription;
	private String salePrice;
	private String costPrice;
	private String conversionFactor;
	private String inputMovement;
	private String outputMovement;
	private String clockMovement;
	private boolean enableClockMovement;
	private ChangeData changeData;
	private long productId;
	private String productDescription;
	private long user;
	private int companyDestinyId;
	private List<IntegrationSystemsDTO> integrationSystemsValues = new ArrayList<IntegrationSystemsDTO>();
	private int gameTypeId;
	private int machineTypeId;
	private String aliasProduct;
	
	public ProductDTO() 
	{
		super();
	}

	public final String getId()
	{
		return this.id;
	}

	public final void setId(final String id)
	{
		this.id = id;
	}

	public final String getDescription() 
	{
		return this.description;
	}

	public final void setDescription(final String description) 
	{
		this.description = description;
	}

	public final boolean isInactive() 
	{
		return this.inactive;
	}
	
	public final int getInactiveInt()
	{
		return this.inactive ? 1 : 0;
	}

	public final void setInactive(final boolean inactive) 
	{
		this.inactive = inactive;
	}

	public final String getCompanyId() 
	{
		return this.companyId;
	}

	public final void setCompanyId(final String companyId)
	{
		this.companyId = companyId;
	}
	
	public final String getCompanyDescription() 
	{
		return this.companyDescription;
	}
	
	public final void setCompanyDescription(final String companyDescription) 
	{
		this.companyDescription = companyDescription;
	}

	public final String getSalePrice()
	{
		return this.salePrice;
	}

	public final void setSalePrice(final String salePrice) 
	{
		this.salePrice = salePrice;
	}

	public final String getCostPrice()
	{
		return this.costPrice;
	}

	public final void setCostPrice(final String costPrice) 
	{
		this.costPrice = costPrice;
	}

	public final String getConversionFactor()
	{
		return this.conversionFactor;
	}

	public final void setConversionFactor(final String conversionFactor)
	{
		this.conversionFactor = conversionFactor;
	}

	public final String getGroupId()
	{
		return this.groupId;
	}

	public final void setGroupId(final String groupId)
	{
		this.groupId = groupId;
	}

	public final String getSubGroupId()
	{
		return this.subGroupId;
	}

	public final void setSubGroupId(final String subGroupId) 
	{
		this.subGroupId = subGroupId;
	}

	public final String getInputMovement()
	{
		return this.inputMovement;
	}

	public final void setInputMovement(final String inputMovement)
	{
		this.inputMovement = inputMovement;
	}

	public final String getOutputMovement()
	{
		return this.outputMovement;
	}

	public final void setOutputMovement(final String outputMovement) 
	{
		this.outputMovement = outputMovement;
	}

	public final ChangeData getChangeData() 
	{
		return this.changeData;
	}

	public final void setChangeData(final ChangeData changeData) 
	{
		this.changeData = changeData;
	}
	
	public final List<IntegrationSystemsDTO> getIntegrationSystemsValues() 
	{
		return this.integrationSystemsValues;
	}
	
	public final void addIntegrationSystemsValue(final String legacyId, final String integrationSystemId)
	{
		final IntegrationSystemsDTO integrationSystem = new IntegrationSystemsDTO();
		integrationSystem.setObjectId(this.id);
		integrationSystem.setObjectType(ConstantDataManager.OBJECT_TYPE_PRODUCT);
		integrationSystem.setInactive(false);
		integrationSystem.setIntegrationSystemId(Integer.valueOf(integrationSystemId));
		integrationSystem.setLegacyId(legacyId);
		integrationSystem.setUserChange(this.changeData.getUserChange());
		final Calendar changeDate = Calendar.getInstance();
		changeDate.setTime(this.changeData.getChangeDate());
		integrationSystem.setChangeDate(changeDate);
		this.integrationSystemsValues.add(integrationSystem);
	}
	
	public final long getProductId() 
	{
		return this.productId;
	}

	public final void setProductId(final long productId) 
	{
		this.productId = productId;
	}
	
	public String getProductDescription() 
	{
		return this.productDescription;
	}
	
	public void setProductDescription(final String productDescription) 
	{
		this.productDescription = productDescription;
	}

	public final long getUser() {
		return this.user;
	}

	public final void setUser(final long user) {
		this.user = user;
	}

	public final int getCompanyDestinyId() {
		return companyDestinyId;
	}

	public final void setCompanyDestinyId(int companyDestinyId) {
		this.companyDestinyId = companyDestinyId;
	}

	public final String getGroupDescription()
	{
		return this.groupDescription;
	}

	public final void setGroupDescription(final String groupDescription)
	{
		this.groupDescription = groupDescription;
	}

	public final String getSubGroupDescription() 
	{
		return this.subGroupDescription;
	}

	public final void setSubGroupDescription(final String subGroupDescription) 
	{
		this.subGroupDescription = subGroupDescription;
	}
	
	public final String getClockMovement() {
		return this.clockMovement;
	}

	public final void setClockMovement(final String clockMovement) {
		this.clockMovement = clockMovement;
	}

	public final boolean isEnableClockMovement() {
		return this.enableClockMovement;
	}

	public final void setEnableClockMovement(final boolean enableClockMovement) {
		this.enableClockMovement = enableClockMovement;
	}
	
	
	public final String getAliasProduct() 
	{
		return this.aliasProduct;
	}

	public final void setAliasProduct(final String aliasProduct) 
	{
		this.aliasProduct = aliasProduct;
	}
	
	public final int getMachineTypeId() {
		return machineTypeId;
	}

	public final void setMachineTypeId(final int machineTypeId) {
		this.machineTypeId = machineTypeId;
	}
	
	public final int getGameTypeId() {
		return gameTypeId;
	}

	public final void setGameTypeId(final int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public void validateFormSave() throws Exception
	{
		if(!StringUtils.isLong(this.id))
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_ID, ConstantDataManager.BLANK));
		}
		if(StringUtils.isNull(this.description))
		{
				throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_DESCRIPTION, ConstantDataManager.BLANK));				
		}
		if(!StringUtils.isLong(this.groupId))
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_PRODUCT_GROUP, ConstantDataManager.BLANK));
		}
		if(!StringUtils.isLong(this.subGroupId))
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_PRODUCT_SUB_GROUP, ConstantDataManager.BLANK));
		}
		if(!StringUtils.isLong(this.inputMovement))
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_INPUT_MOVEMENT, ConstantDataManager.BLANK));
		}
		if(!StringUtils.isLong(this.outputMovement))
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_OUTPUT_MOVEMENT, ConstantDataManager.BLANK));
		}
		if(!StringUtils.isLong(this.clockMovement))
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_CLOCK_MOVEMENT, ConstantDataManager.BLANK));
		}
	}
}