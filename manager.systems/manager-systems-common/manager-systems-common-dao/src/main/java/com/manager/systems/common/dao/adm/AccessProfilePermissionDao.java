package com.manager.systems.common.dao.adm;

import java.util.Map;

import com.manager.systems.common.dto.adm.AccessProfilePermissionDTO;
import com.manager.systems.common.dto.adm.AccessProfilePermissionFilterDTO;

public interface AccessProfilePermissionDao 
{
	boolean save(AccessProfilePermissionDTO accessProfilePermission) throws Exception;
	boolean inactiveAll(AccessProfilePermissionDTO accessProfilePermission) throws Exception;
	Map<Integer, AccessProfilePermissionDTO> getByAccessProfile(AccessProfilePermissionFilterDTO filter) throws Exception;
}