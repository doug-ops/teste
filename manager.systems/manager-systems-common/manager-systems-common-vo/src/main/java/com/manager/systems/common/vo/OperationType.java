package com.manager.systems.common.vo;

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
	CHANGE_STATUS(9, ConstantDataManager.CHANGE_STATUS_METHOD_LABEL),
	GET_ALL_BY_COMPANY(10, ConstantDataManager.AUTOCOMPLETE_METHOD_LABEL), 
	PREVIEW_PROCESS(11, ConstantDataManager.PREVIEW_PROCESS_MOVEMENT_LABEL),
	PROCESS_MOVEMENT_LABEL(12, ConstantDataManager.PROCESS_MOVEMENT_LABEL),
	INACTIVE_ALL(13, ConstantDataManager.INACTIVE_ALL_METHOD_LABEL),
	CLONE(14, ConstantDataManager.INACTIVE_ALL_METHOD_LABEL),
	CHANGE(15, ConstantDataManager.CHANGE_METHOD_LABEL), 
	GROUP_PARENT(16, ConstantDataManager.GROUP_PARENT_METHOD_LABEL),
	INSERT(17, ConstantDataManager.SAVE_METHOD_LABEL),
	OTHER(99, ConstantDataManager.SAVE_METHOD_LABEL),
	UPDATE_LAST_ACCESS_USER(18, ConstantDataManager.SAVE_METHOD_LABEL),
	UPDATE_LAST_ACCESS_FINISH_USER(19, ConstantDataManager.SAVE_METHOD_LABEL),
	GET_MAX(20, ConstantDataManager.SAVE_METHOD_LABEL),
	SAVE_SYNCHRONIZE(21, ConstantDataManager.SAVE_METHOD_LABEL),
	GET_HAS_NO_MOVEMENT_PRODUCT(22, ConstantDataManager.GET_ALL_METHOD_LABEL),
	GET_COMPANY(25, ConstantDataManager.GET_ALL_METHOD_LABEL),
	UPDATE_MOTIVE_NEGATIVE(23, ConstantDataManager.SAVE_METHOD_LABEL),
	GET_ALL_PRODUCTS(24, ConstantDataManager.GET_ALL_METHOD_LABEL),
	SAVE_DISCARD_PRODUCT(25, ConstantDataManager.SAVE_METHOD_LABEL),
	SAVE_TRANSFER_VALUE(26, ConstantDataManager.SAVE_METHOD_LABEL),
	SAVE_TRANSFER_VALUE_RESIDUE(27, ConstantDataManager.SAVE_METHOD_LABEL),
	SAVE_NEW_DOCUMENT(28, ConstantDataManager.SAVE_METHOD_LABEL),
	SAVE_NEW_DOCUMENT_EXPENSE(29, ConstantDataManager.SAVE_METHOD_LABEL),
	GET_BANK_ACCOUNT_CONFIG_TRASNFER(30, ConstantDataManager.GET_METHOD_LABEL),
	GET_ALL_GROUNPING(5, ConstantDataManager.GET_ALL_COMBOBOX_METHOD_LABEL),
	GET_ALL_USERS_COMPANY(5, ConstantDataManager.GET_ALL_METHOD_LABEL);
	
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