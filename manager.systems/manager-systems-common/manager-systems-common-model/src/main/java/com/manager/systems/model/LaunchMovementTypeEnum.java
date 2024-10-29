/*
 * Creation date 22/05/2023.
 */
package com.manager.systems.model;

import com.manager.systems.common.utils.ConstantDataManager;

public enum LaunchMovementTypeEnum {
	EXPENSE(1, ConstantDataManager.LAUNCH_MOVMENT_TYPE_EXPENSE),
	LAUNCH_MOVMENT_TYPE_TRANSFER_BALANCE(2, ConstantDataManager.LAUNCH_MOVMENT_TYPE_TRANSFER_BALANCE),
	LAUNCH_MOVMENT_TYPE_LAUNCH(3, ConstantDataManager.LAUNCH_MOVMENT_TYPE_LAUNCH),
	LAUNCH_MOVMENT_PRODUCT_MOVEMENT(4, ConstantDataManager.LAUNCH_MOVMENT_PRODUCT_MOVEMENT);
	
	private int type;
	private String description;
	
	private LaunchMovementTypeEnum(final int type, final String description) 
	{
		this.type = type;
		this.description = description;
	}

	public final int getType() 
	{
		return this.type;
	}

	public final String getDescription() 
	{
		return this.description;
	}
}
