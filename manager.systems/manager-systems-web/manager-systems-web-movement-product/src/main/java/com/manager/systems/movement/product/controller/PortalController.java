package com.manager.systems.movement.product.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.manager.systems.common.dto.adm.AccessProfilePermissionDTO;
import com.manager.systems.common.dto.adm.AccessProfilePermissionFilterDTO;
import com.manager.systems.common.dto.adm.CompanyDTO;
import com.manager.systems.common.service.LoginService;
import com.manager.systems.common.service.address.AddressService;
import com.manager.systems.common.service.adm.AccessProfilePermissionService;
import com.manager.systems.common.service.adm.UserCompanyService;
import com.manager.systems.common.service.adm.UserParentService;
import com.manager.systems.common.service.impl.LoginServiceImpl;
import com.manager.systems.common.service.impl.address.AddressServiceImpl;
import com.manager.systems.common.service.impl.adm.AccessProfilePermissionServiceImpl;
import com.manager.systems.common.service.impl.adm.UserCompanyServiceImpl;
import com.manager.systems.common.service.impl.adm.UserParentServiceImpl;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.AccessData;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.model.admin.User;
import com.manager.systems.movement.product.utils.ConstantDataManager;
import com.manager.systems.web.common.controller.BaseController;

@Controller
@RequestMapping(value=ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER)
public class PortalController extends BaseController
{		
	private static final Log log = LogFactory.getLog(PortalController.class);
	
	@GetMapping(value=ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER_BASE)
    public static String base(final HttpServletRequest request) 
    {
		log.info(ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER + ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER_BASE);
		
		String result = ConstantDataManager.RESULT_REDIRECT_HOME;
		
		final User user = (User) request.getSession().getAttribute(ConstantDataManager.PARAMETER_USERNAME);
		if(user==null)
		{
			removeSession(request);	
			result = ConstantDataManager.RESULT_LOGIN;
		}
		return result;
    }
	
	@GetMapping(value=ConstantDataManager.PORTAL_CONTROLLER_INDEX)
    public static String index(final HttpServletRequest request) 
	{	
		log.info(ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER + ConstantDataManager.PORTAL_CONTROLLER_INDEX);
		
		String result = ConstantDataManager.RESULT_REDIRECT_HOME;
		
		final User user = (User) request.getSession().getAttribute(ConstantDataManager.PARAMETER_USERNAME);
		if(user==null)
		{
			removeSession(request);	
			result = ConstantDataManager.RESULT_LOGIN;
		}
		return result;
	}
	
	@RequestMapping(value=ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER_LOGIN, method={RequestMethod.GET, RequestMethod.POST})
    public String login(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER + ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER_LOGIN);
		
		TimeZone.setDefault(tz);
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		String result = ConstantDataManager.RESULT_LOGIN;

		Connection connection = null;

		try 
		{						
			final String usernameParameter = request.getParameter(ConstantDataManager.PARAMETER_USERNAME);
			String passwordParameter = request.getParameter(ConstantDataManager.PARAMETER_PASSWORD);
			
			final AccessData accessData = new AccessData();
			accessData.setUsername(usernameParameter);
			accessData.setPassword(passwordParameter);
			
			final User user = new User();
			user.setAccessData(accessData);
					
			if (accessData.isValidFormLogin()) 
			{	
				connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
				if(connection==null)
				{
					throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
				}
				
				final LoginService loginService = new LoginServiceImpl(connection);					
				loginService.validate(user);				
				if (user.getId()>0) 
				{				
					final AccessProfilePermissionFilterDTO accessProfilePermissionFilter = new AccessProfilePermissionFilterDTO();					
					accessProfilePermissionFilter.setAccessProfileId(user.getAccessProfile().getId());
					accessProfilePermissionFilter.setInactive(user.getAccessProfile().isInactive());
										
					final AccessProfilePermissionService accessProfilePermissionService = new AccessProfilePermissionServiceImpl(connection);	
					user.getAccessProfile().setPermissions(accessProfilePermissionService.getByAccessProfile(accessProfilePermissionFilter));
					
					boolean hasPermissionAcess = false;					
					
					if(user.hasPermission("MENU_OFF_LINE", "MENU_OFF_LINE_MOVIMENTOS")) {
						hasPermissionAcess = true;
					}
					
					if(!hasPermissionAcess) {
						throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso a aplicação OffLine.");
					}
					
					final Map<String, Boolean> permissions = new TreeMap<>();
					if(user.getAccessProfile() != null && user.getAccessProfile().getPermissions() != null && user.getAccessProfile().getPermissions().size() > 0) {
						for(final Map.Entry<Integer, AccessProfilePermissionDTO> entry : user.getAccessProfile().getPermissions().entrySet()) {
							if(entry.getValue().isPermission()) {
								permissions.put(entry.getValue().getPermissionRole(), entry.getValue().isPermission());								
							}
							if(entry.getValue().getItems() != null && entry.getValue().getItems().size() > 0) {
								for(final Map.Entry<Integer, AccessProfilePermissionDTO> entryItem : entry.getValue().getItems().entrySet()) {
									if(entryItem.getValue().isPermission()) {
										permissions.put(entryItem.getValue().getPermissionRole(), entryItem.getValue().isPermission());										
									}
								}
							}
						}
					}
					
					request.getSession().setMaxInactiveInterval((-1));
					request.getSession().setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER, user);
					
					final AddressService addressService = new AddressServiceImpl(connection);
					request.getSession().setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_ADDRESS_STATES, addressService.getAllStatesMap());
					request.getSession().setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_ADDRESS_COUNTRIES, addressService.getAllCountry());
					request.getSession().setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_PERMISSIONS, permissions);
					
					result = ConstantDataManager.RESULT_REDIRECT_HOME;
				} 
				else 
				{
					message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_USER_PASSWORD_INVALID, null);
					result = ConstantDataManager.RESULT_LOGIN;
				}
			} 
			else 
			{
				message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_USER_PASSWORD_INVALID, null);
				result = ConstantDataManager.RESULT_LOGIN;
			}
		} 
		catch (final Exception e) 
		{
			result = ConstantDataManager.RESULT_LOGIN;
			e.printStackTrace();
			message = e.getMessage();
		} 
		finally 
		{
			if (connection != null) 
			{
				connection.close();
			}
		}
		request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE, message);
		return result;
    }
	
	@GetMapping(value=ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER_HOME)
    public String home(final HttpServletRequest request) throws Exception
	{		
		log.info(ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER + ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER_HOME);
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
			
			final UserParentService userParentService = new UserParentServiceImpl(connection);
			final List<Combobox> userChidrensParent = userParentService.getAllParentCombobox(user.getId());
			request.setAttribute(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT, userChidrensParent);
		}
		catch (final Exception ex)
		{
			throw ex;
		}
		finally 
		{
			if(connection!=null)
			{
				connection.close();
			}
		}	
		
		return ConstantDataManager.RESULT_HOME;
    }
	
	@GetMapping(value=ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER_LOGOUT)
    public String logout(final HttpServletRequest request) 
    {
		log.info(ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER + ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER_LOGOUT);
		
		removeSession(request);
		return ConstantDataManager.RESULT_LOGIN;
    }
	
	@GetMapping(value=ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER_ACESSO_EXPIRADO)
    public String acessoExpirado(final HttpServletRequest request) 
    {
		log.info(ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER + ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER_ACESSO_EXPIRADO);
		
		removeSession(request);
		request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE, this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
		return ConstantDataManager.RESULT_LOGIN;
    }
	
	@ExceptionHandler(LoginException.class)
	public static String handleLoginException(final HttpServletRequest request, final LoginException le) 
	{
		log.info(ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER + "LoginException");
		
		removeSession(request);
		request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE, le.getMessage());
		return ConstantDataManager.RESULT_LOGIN;
	}
	
	@ExceptionHandler(Exception.class)
	public static String handleException(final HttpServletRequest request, final Exception ex) 
	{
		log.info(ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER + "Exception");
		
		removeSession(request);
		request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE, ex.getMessage());
		return ConstantDataManager.RESULT_LOGIN;
	}
	  
	private static void removeSession(final HttpServletRequest request) 
    {
		request.getSession().removeAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
		request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE, com.manager.systems.web.common.utils.ConstantDataManager.BLANK);
    }
	
	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_COMPANY_HOME_METHOD)
	public ResponseEntity<String> companyHome(final HttpServletRequest request, final Model model) throws Exception 
	{
		log.info(ConstantDataManager.PORTAL_MOVEMENT_PRODUCT_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_COMPANY_HOME_METHOD);
		
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
			
			final List<CompanyDTO> companys = this.processFilter(request, connection);
			final List<CompanyDTO> report = companys.parallelStream().filter(x -> !x.isProcessMovementAutomatic() && !(com.manager.systems.common.utils.ConstantDataManager.BLANK.equalsIgnoreCase(x.getExpiryDataString().trim()))).collect(Collectors.toList());

			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT, report);
			status = true;
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

	private List<CompanyDTO> processFilter(final HttpServletRequest request, final Connection connection) throws Exception
	{
		final List<CompanyDTO> companys =  new ArrayList<CompanyDTO>();
		try
		{	
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			final StringBuilder usersId = new StringBuilder();
			final String usersIdParameter = request.getParameter(ConstantDataManager.PARAMTER_USERS_ID);
			
			if(StringUtils.isNull(usersIdParameter)) {
				usersId.append(user.getId());
			} else {
				usersId.append(usersIdParameter);
			}
			
			final UserCompanyService userCompanyService = new UserCompanyServiceImpl(connection);
			final List<CompanyDTO> companyFilter = userCompanyService.getDataMovementExecutionCompany(usersId.toString());
			if(companyFilter != null && companyFilter.size() > 0) {
				companys.addAll(companyFilter.parallelStream().filter(c -> c.getPersonTypeId() == 1).collect(Collectors.toList()));
			}
		}
		catch(final Exception ex) 
		{
			throw ex;
		}		
		return companys;
	}
}