package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessProfilePermissionDTO implements Serializable 
{
	private static final long serialVersionUID = 7371140982374814058L;

	private int permissionId;
	private int permissionParentId;
	private int accessProfileId;
	private String acessProfileDescription;
	private String permissionDescription;
	private String permissionRole;
	private boolean permission;
	private boolean inactive;
	private long userChange;
	private Map<Integer, AccessProfilePermissionDTO> items;
	
	public void addItem(final AccessProfilePermissionDTO item) {
		if(this.items == null) {
			this.items = new TreeMap<>();
		}
		
		if(item.isPermission()) {
			this.permission = true;
		}
		this.items.put(item.getPermissionId(), item);
	}
}