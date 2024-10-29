package com.manager.systems.web.adm.controller;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.manager.systems.common.dto.adm.AccessProfileDTO;
import com.manager.systems.common.dto.adm.AccessProfilePermissionDTO;
import com.manager.systems.common.dto.adm.AccessProfilePermissionFilterDTO;
import com.manager.systems.common.dto.adm.ReportAccessProfileDTO;
import com.manager.systems.common.service.adm.AccessProfilePermissionService;
import com.manager.systems.common.service.adm.AccessProfileService;
import com.manager.systems.common.service.impl.adm.AccessProfilePermissionServiceImpl;
import com.manager.systems.common.service.impl.adm.AccessProfileServiceImpl;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.ObjectType;
import com.manager.systems.model.admin.User;
import com.manager.systems.web.adm.exception.AdminException;
import com.manager.systems.web.adm.utils.ConstantDataManager;
import com.manager.systems.web.common.controller.BaseController;

@Controller
@RequestMapping(value=ConstantDataManager.ACCESS_PROFILE_MAINTENANCE_CONTROLLER)
public class AccessProfileMaintenanceController extends BaseController
{
	private static final Log log = LogFactory.getLog(AccessProfileMaintenanceController.class);
	
	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD)
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		log.info(ConstantDataManager.ACCESS_PROFILE_MAINTENANCE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD);
		
		String result = ConstantDataManager.ACCESS_PROFILE_MAINTENANCE_OPEN_METHOD_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			if(!user.hasPermission("MENU_CADASTROS", "MENU_CADASTROS_PERFIL_ACESSO")) {
				result = ConstantDataManager.RESULT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Perfil de Acesso.");
			}
			
			request.setAttribute(ConstantDataManager.PARAMETER_OBJECT_TYPE_ALIAS, com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_ACCESS_PROFILE);
		}
		catch(final Exception e) 
		{
			e.printStackTrace();
			message = e.getMessage();
		}
		finally 
		{
			//
		}
		request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE, message);
		return result;
	}
	
	@PostMapping(value=ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD)
	public ResponseEntity<String> save(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.ACCESS_PROFILE_MAINTENANCE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD);
		
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
		
		Connection connection = null;
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final String idParameter = request.getParameter(ConstantDataManager.PARAMETER_ID);			
			final String descriptionParameter = request.getParameter(ConstantDataManager.PARAMETER_DESCRIPTION);			
			final String roleParameter = request.getParameter(ConstantDataManager.PARAMETER_ROLE);
			String cashingClosingMaxDiscountParameter = request.getParameter(ConstantDataManager.PARAMETER_CASHING_CLOSING_MAX_DISCOUNT);
			
			if(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_ZERO_ZERO.equalsIgnoreCase(cashingClosingMaxDiscountParameter))
			{
				cashingClosingMaxDiscountParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_VIRGULA_ZERO_ZERO;
			}
			cashingClosingMaxDiscountParameter = StringUtils.replaceNonDigits(cashingClosingMaxDiscountParameter);
			final StringBuilder resultMoeda = new StringBuilder(cashingClosingMaxDiscountParameter);
			if(resultMoeda.length()>2)
			{
				resultMoeda.insert(resultMoeda.length() - 2, com.manager.systems.common.utils.ConstantDataManager.PONTO);
			}						
			cashingClosingMaxDiscountParameter = resultMoeda.toString();
			
			String activeParameter = request.getParameter(ConstantDataManager.PARAMETER_ACTIVE);
			if(com.manager.systems.common.utils.ConstantDataManager.ON.equalsIgnoreCase(activeParameter))
			{
				activeParameter = com.manager.systems.common.utils.ConstantDataManager.FALSE_STRING;
			}
			else
			{
				activeParameter = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
			}
						
			final AccessProfileDTO accessProfile = new AccessProfileDTO();			
			
			if(StringUtils.isLong(idParameter))
			{
				accessProfile.setId(Integer.valueOf(idParameter));				
			}
			accessProfile.setDescription(descriptionParameter);
			accessProfile.setRole(roleParameter);
			accessProfile.setInactive(Boolean.valueOf(activeParameter));
			accessProfile.setCashClosingMaxDiscount(Double.parseDouble(cashingClosingMaxDiscountParameter));
			final ChangeData changeData = new ChangeData();
			changeData.setUserChange(user.getId());
			changeData.setChangeDate(Calendar.getInstance(tz).getTime());
			accessProfile.setChangeData(changeData);
			accessProfile.validateFormSave();
			final AccessProfileService accessProfileService = new AccessProfileServiceImpl(connection);						
			final boolean isSaved = accessProfileService.save(accessProfile);
			
			if(!isSaved)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
			}
			
			accessProfileService.saveConfig(accessProfile);
								
			final List<String> permissions = Collections.list(request.getParameterNames());
			
			final AccessProfilePermissionService accessProfilePermissionService = new AccessProfilePermissionServiceImpl(connection);	
			accessProfilePermissionService.inactiveAll(AccessProfilePermissionDTO.builder().accessProfileId(accessProfile.getId()).build());
			
			List<String> discartedProperties = Arrays.asList("objectType", "personType", "id", "description", "active","changeDate", "changeUser", "cashingClosingMaxDiscount");
			
			final Map<Integer, AccessProfilePermissionDTO> permissionsToSave = new TreeMap<>();
			for(final String permission : permissions)
			{
				if(!discartedProperties.contains(permission))
				{
					final String[] ids = permission.split(com.manager.systems.web.common.utils.ConstantDataManager.UNDERSCORE);
					
					final AccessProfilePermissionDTO accessProfilePermissionParent = AccessProfilePermissionDTO.builder()
						  .accessProfileId(accessProfile.getId())
						  .permissionParentId(Integer.valueOf(ids[1]))
						  .permissionId(Integer.valueOf(ids[2]))
						  .permission(true)
						  .userChange(user.getId())	
						  .build();
					
					permissionsToSave.put(accessProfilePermissionParent.getPermissionId(), accessProfilePermissionParent);					
				}
			}					
			
			for(final Map.Entry<Integer, AccessProfilePermissionDTO> entry : permissionsToSave.entrySet()) {
				accessProfilePermissionService.save(entry.getValue());				
			}
			
			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
		}
		catch (final AdminException ex)
		{
			ex.printStackTrace();
			status = false;
			message =  ex.getMessage();
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			status = false;
			message =  ex.getMessage();
		}
		finally 
		{
			if(connection!=null)
			{
				connection.close();
			}
		}	
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}
	
	@PostMapping(value=ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_INACTIVE_METHOD)
    public ResponseEntity<String> inactive(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.ACCESS_PROFILE_MAINTENANCE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_INACTIVE_METHOD);
		
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
		
		Connection connection = null;
		
		try 
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
						
			final String idParameter = request.getParameter(ConstantDataManager.PARAMETER_ID);			
			final String inactiveParameter = request.getParameter(ConstantDataManager.PARAMETER_INACTIVE);
						
			if(!StringUtils.isLong(idParameter) || StringUtils.isNull(inactiveParameter))
			{	
				throw new AdminException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_DATA_REQUIRED_ERROR, null));				
			}
			
			final AccessProfileDTO accessProfile = new AccessProfileDTO();
			if(StringUtils.isLong(idParameter))
			{
				accessProfile.setId(Integer.valueOf(idParameter));				
			}
			accessProfile.setInactive(Boolean.valueOf(inactiveParameter));
			final ChangeData changeData = new ChangeData();
			changeData.setUserChange(user.getId());
			changeData.setChangeDate(Calendar.getInstance(tz).getTime());
			accessProfile.setChangeData(changeData);
			final AccessProfileService accessProfileService = new AccessProfileServiceImpl(connection);	
			final boolean wasInactivated = accessProfileService.inactive(accessProfile);
			
			if(!wasInactivated)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
			}
			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
		} 
		catch (final AdminException ex)
		{
			ex.printStackTrace();
			status = false;
			message =  ex.getMessage();
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			status = false;
			message =  ex.getMessage();
		}
		finally 
		{
			if(connection!=null)
			{
				connection.close();
			}
		}	
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}
	
	@PostMapping(value=ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_DETAIL_METHOD)
	public ResponseEntity<String> detail(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.ACCESS_PROFILE_MAINTENANCE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_DETAIL_METHOD);
		
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
		
		AccessProfileDTO accessProfile = null;
		AccessProfilePermissionFilterDTO accessProfilePermissionFilter = null;
		
		Connection connection = null;
				
		try 
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			accessProfile = new AccessProfileDTO();
			accessProfilePermissionFilter = new AccessProfilePermissionFilterDTO();
			
			final String idParameter = request.getParameter(ConstantDataManager.PARAMETER_ID);			
			if(StringUtils.isLong(idParameter))
			{
				accessProfile.setId(Integer.valueOf(idParameter));					
			}
						
			final AccessProfileService accessProfileService = new AccessProfileServiceImpl(connection);	
			accessProfileService.get(accessProfile);
			
			if(accessProfile.getDescription()==null)
			{
				throw new AdminException(this.messages.get(ConstantDataManager.MESSAGE_GROUP_INVALID, null));
			}
			
			accessProfilePermissionFilter.setAccessProfileId(accessProfile.getId());
			accessProfilePermissionFilter.setInactive(accessProfile.isInactive());
								
			final AccessProfilePermissionService accessProfilePermissionService = new AccessProfilePermissionServiceImpl(connection);	
			accessProfile.setPermissions(accessProfilePermissionService.getByAccessProfile(accessProfilePermissionFilter));
																				
			status = true;
			result.put(ConstantDataManager.PARAMETER_ACCES_PROFILE, accessProfile);
		} 
		catch (final AdminException ex)
		{
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		}
		finally 
		{
			if(connection!=null)
			{
				connection.close();
			}
		}	
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
    }
	
	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD)
	public ResponseEntity<String> filter(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.ACCESS_PROFILE_MAINTENANCE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD);
		
		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new Gson();
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
		
		Connection connection = null;
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final ReportAccessProfileDTO reportAccessProfile = this.processFilter(request, connection);
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT, reportAccessProfile);
			status = true;
		}
		catch (final AdminException ex)
		{
			ex.printStackTrace();
			status = false;
			message =  ex.getMessage();
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			status = false;
			message =  ex.getMessage();
		}
		finally 
		{
			if(connection!=null)
			{
				connection.close();
			}
		}	
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}
	
	public ReportAccessProfileDTO processFilter(final HttpServletRequest request, final Connection connection) throws Exception
	{
		final ReportAccessProfileDTO report = new ReportAccessProfileDTO();
		try
		{	
			String idFromParameter = request.getParameter(ConstantDataManager.PARAMETER_ID_FROM);
			if(!StringUtils.isLong(idFromParameter))
			{
				idFromParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			
			String idToParameter = request.getParameter(ConstantDataManager.PARAMETER_ID_TO);
			if(!StringUtils.isLong(idToParameter))
			{
				idToParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			
			String inactiveParameter = request.getParameter(ConstantDataManager.PARAMETER_INACTIVE);
			if(!StringUtils.isLong(inactiveParameter))
			{
				inactiveParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			
			final String descriptionParameter = request.getParameter(ConstantDataManager.PARAMETER_DESCRIPTION);
			
			final String objectTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_OBJECT_TYPE);
			if(StringUtils.isNull(objectTypeParameter))
			{
				throw new AdminException(this.messages.get(ConstantDataManager.MESSAGE_OBJECT_TYPE_NOT_INFORMED, null));
			}
			
			final ObjectType objectType = ObjectType.valueOfType(objectTypeParameter);
			if(objectType==null)
			{
				throw new AdminException(this.messages.get(ConstantDataManager.MESSAGE_OBJECT_TYPE_INVALID, null));
			}
			
			report.setIdFrom(idFromParameter);
			report.setIdTo(idToParameter);
			report.setInactive(inactiveParameter);
			report.setDescription(descriptionParameter);
			
			final AccessProfileService accessProfileService = new AccessProfileServiceImpl(connection);
			accessProfileService.getAll(report);
		}
		catch(final Exception ex) 
		{
			throw ex;
		}		
		return report;
	}	
}