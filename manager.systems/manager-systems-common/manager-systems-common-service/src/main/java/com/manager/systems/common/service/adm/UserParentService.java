package com.manager.systems.common.service.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.UserDTO;
import com.manager.systems.common.dto.adm.UserParentDTO;
import com.manager.systems.common.vo.Combobox;

public interface UserParentService 
{
	boolean save(UserParentDTO userParent) throws Exception;
	boolean inactive(UserParentDTO userParent) throws Exception;
	List<UserDTO> get(long userId) throws Exception;
	List<Combobox> getAllParentCombobox(long userId) throws Exception;
	List<Combobox> getUserParentCombobox(long userId) throws Exception; 
	List<Combobox> getUserComboboxByClient(long userId) throws Exception;
	List<Combobox> getAllUserParentComboboxByOperation(long userId, int operation) throws Exception;
}