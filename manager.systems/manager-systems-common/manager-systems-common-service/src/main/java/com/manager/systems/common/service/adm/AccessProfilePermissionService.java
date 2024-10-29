package com.manager.systems.common.service.adm;

import java.util.Map;

import com.manager.systems.common.dto.adm.AccessProfilePermissionDTO;
import com.manager.systems.common.dto.adm.AccessProfilePermissionFilterDTO;

public interface AccessProfilePermissionService 
{
	boolean save(AccessProfilePermissionDTO accessProfilePermission) throws Exception;
	boolean inactiveAll(AccessProfilePermissionDTO accessProfilePermission) throws Exception;
	Map<Integer, AccessProfilePermissionDTO> getByAccessProfile(AccessProfilePermissionFilterDTO filter) throws Exception;
}