package com.manager.systems.web.jobs.vo;

import com.manager.systems.common.utils.ConstantDataManager;

public enum OperationType 
{
	SAVE(1, ConstantDataManager.SAVE_METHOD_LABEL),
	INACTIVE(2, ConstantDataManager.INACTIVE_METHOD_LABEL),
	GET(3, ConstantDataManager.GET_METHOD_LABEL),
	GET_ALL(4, ConstantDataManager.GET_ALL_METHOD_LABEL),
	GET_ALL_COMBOBOX(5, ConstantDataManager.GET_ALL_COMBOBOX_METHOD_LABEL),
	DELETE(6, ConstantDataManager.DELETE_METHOD_LABEL), 
	AUTOCOMPLETE(7, ConstantDataManager.AUTOCOMPLETE_METHOD_LABEL),
	UNGROUP(8, ConstantDataManager.UNGROUP_METHOD_LABEL),
	CHANGE_STATUS(9, ConstantDataManager.UNGROUP_METHOD_LABEL), 
	PROCESS_MOVEMENT(10, ConstantDataManager.PROCESS_MOVEMENT_LABEL);
	
	private int type;
	private String description;
	
	private OperationType(final int type, final String description) 
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