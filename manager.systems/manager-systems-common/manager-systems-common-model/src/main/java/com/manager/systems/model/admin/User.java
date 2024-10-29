package com.manager.systems.model.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.manager.systems.common.dto.adm.AccessProfilePermissionDTO;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.model.Person;

public class User extends Person implements Serializable 
{
	private static final long serialVersionUID = 8966718846602733878L;
	
	private String name;
	private Calendar userLastAcessDate;
	private String userLastAcessDateString;
	private Calendar userExitAcessDate;
	private String userExitAcessDateString;
	private double cashClosingMaxDiscount;
	private List<Combobox> companys = new ArrayList<Combobox>();
	private int clientId;
	
	public User() 
	{
		super();
	}
	
	public String getName() 
	{
		return this.name;
	}
	
	public void setName(final String name) 
	{
		this.name = name;
	}
	
	public final Calendar getUserLastAcessDate() {
		return this.userLastAcessDate;
	}

	public final void setUserLastAcessDate(final Calendar userLastAcessDate) {
		this.userLastAcessDate = userLastAcessDate;
		this.userLastAcessDateString = StringUtils.formatDate(this.userLastAcessDate.getTime(), StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM);
	}
	
	public final String getUserLastAcessDateString() {
		return this.userLastAcessDateString;
	}
	
	public final void setUserLastAcessDateString(final String userLastAcessDateString) {
		this.userLastAcessDateString = userLastAcessDateString;
	}
	
	public final Calendar getUserExitAcessDate() {
		return this.userExitAcessDate;
	}

	public final void setUserExitAcessDate(final Calendar userExitAcessDate) {
		this.userExitAcessDate = userExitAcessDate;
		this.userExitAcessDateString = StringUtils.formatDate(this.userExitAcessDate.getTime(), StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM);
		}
	
	public final String getUserExitAcessDateString() {
		return this.userExitAcessDateString;
	}

	public final void setUserExitAcessDateString(String userExitAcessDateString) {
		this.userExitAcessDateString = userExitAcessDateString;
	}

	public List<Combobox> getCompanys() 
	{
		return this.companys;
	}
	
	public final void addCompanyItem(final Combobox companyItem)
	{
		this.companys.add(companyItem);
	}

	public final int getClientId() {
		return this.clientId;
	}

	public final void setClientId(final int clientId) {
		this.clientId = clientId;
	}
	
	public final double getCashClosingMaxDiscount() {
		return this.cashClosingMaxDiscount;
	}

	public final void setCashClosingMaxDiscount(final double cashClosingMaxDiscount) {
		this.cashClosingMaxDiscount = cashClosingMaxDiscount;
	}

	public boolean hasPermission(final String permissionRoleParent, final String permissionRole) {
		boolean result = false;
		
		if(this.getAccessProfile() != null && this.getAccessProfile().getPermissions() != null && this.getAccessProfile().getPermissions().size() > 0) {
			final Optional<AccessProfilePermissionDTO> opPermissionParent = this.getAccessProfile().getPermissions().values().parallelStream().filter(x -> x.getPermissionRole().equalsIgnoreCase(permissionRoleParent)).findFirst();
			if(!StringUtils.isNull(permissionRole)) {
				if(opPermissionParent.isPresent() && opPermissionParent.get().getItems() != null && opPermissionParent.get().getItems().size() > 0) {
					final Optional<AccessProfilePermissionDTO> opPermission = opPermissionParent.get().getItems().values().parallelStream().filter(x -> x.getPermissionRole().equalsIgnoreCase(permissionRole)).findFirst();
					if(opPermission != null && opPermission.isPresent() && opPermission.get().isPermission()) {
						result = true;						
					}
				}				
			} else if(opPermissionParent.isPresent()) {
				if(opPermissionParent != null && opPermissionParent.isPresent() && opPermissionParent.get().isPermission()) {
					result = true;						
				}
			}
		}
		
		return result;
	}
	
	public boolean hasPermissionManager() {
		boolean result = false;
		
		final List<String> permissionsManager = new ArrayList<>();
		permissionsManager.add("MENU_CADASTROS");
		permissionsManager.add("MENU_FINANCEIRO");
		permissionsManager.add("MENU_MOVIMENTOS");
		permissionsManager.add("MENU_RELATORIO");
		
		if(this.getAccessProfile() != null && this.getAccessProfile().getPermissions() != null && this.getAccessProfile().getPermissions().size() > 0) {
			final List<AccessProfilePermissionDTO> opPermissionParent = this.getAccessProfile().getPermissions().values().parallelStream().filter(x -> permissionsManager.contains(x.getPermissionRole()) && x.isPermission()).collect(Collectors.toList());
			if(opPermissionParent != null && opPermissionParent.size() > 0) {
				result = true;			
			} 
		}
		
		return result;		
	}
	
	public Map<String, String> getPermissionsNames(){
		final Map<String, String> permissionRole = new TreeMap<>();
		permissionRole.put("MENU_OFF_LINE", "MENU_OFF_LINE");
		return permissionRole;
	}
}