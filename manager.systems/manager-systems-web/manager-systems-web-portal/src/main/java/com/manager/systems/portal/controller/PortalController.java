package com.manager.systems.portal.controller;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manager.systems.common.dto.adm.AccessProfilePermissionDTO;
import com.manager.systems.common.dto.adm.AccessProfilePermissionFilterDTO;
import com.manager.systems.common.service.LoginService;
import com.manager.systems.common.service.address.AddressService;
import com.manager.systems.common.service.adm.AccessProfilePermissionService;
import com.manager.systems.common.service.adm.UserParentService;
import com.manager.systems.common.service.adm.UserService;
import com.manager.systems.common.service.impl.LoginServiceImpl;
import com.manager.systems.common.service.impl.address.AddressServiceImpl;
import com.manager.systems.common.service.impl.adm.AccessProfilePermissionServiceImpl;
import com.manager.systems.common.service.impl.adm.UserParentServiceImpl;
import com.manager.systems.common.service.impl.adm.UserServiceImpl;
import com.manager.systems.common.vo.AccessData;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.model.admin.User;
import com.manager.systems.portal.utils.ConstantDataManager;
import com.manager.systems.web.common.controller.BaseController;

@Controller
@RequestMapping(value = ConstantDataManager.PORTAL_CONTROLLER)
public class PortalController extends BaseController {
	private static final Logger logger = LogManager.getLogger(PortalController.class.getName());

	@GetMapping(value = ConstantDataManager.PORTAL_CONTROLLER_BASE)
	public static String base(final HttpServletRequest request) {
		logger.debug(ConstantDataManager.METHOD_BASE);

		String result = ConstantDataManager.RESULT_REDIRECT_HOME;

		final User user = (User) request.getSession().getAttribute(ConstantDataManager.PARAMETER_USERNAME);
		if (user == null) {
			removeSession(request);
			result = ConstantDataManager.RESULT_LOGIN;
		}
		return result;
	}

	@GetMapping(value = ConstantDataManager.PORTAL_CONTROLLER_INDEX)
	public static String index(final HttpServletRequest request) {
		logger.debug(ConstantDataManager.METHOD_INDEX);

		String result = ConstantDataManager.RESULT_REDIRECT_HOME;

		final User user = (User) request.getSession().getAttribute(ConstantDataManager.PARAMETER_USERNAME);
		if (user == null) {
			removeSession(request);
			result = ConstantDataManager.RESULT_LOGIN;
		}
		return result;
	}

	@RequestMapping(value = ConstantDataManager.PORTAL_CONTROLLER_LOGIN, method = { RequestMethod.GET, RequestMethod.POST })
	public String login(final HttpServletRequest request) throws Exception {
		logger.debug(ConstantDataManager.METHOD_LOGIN);

		TimeZone.setDefault(tz);

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		String result = ConstantDataManager.RESULT_LOGIN;

		Connection connection = null;

		try {
			final String usernameParameter = request.getParameter(ConstantDataManager.PARAMETER_USERNAME);
			final String passwordParameter = request.getParameter(ConstantDataManager.PARAMETER_PASSWORD);

			final AccessData accessData = new AccessData();
			accessData.setUsername(usernameParameter);
			accessData.setPassword(passwordParameter);

			final User user = new User();
			user.setAccessData(accessData);

			if (accessData.isValidFormLogin()) {
				connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
				if (connection == null) {
					throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
				}

				final LoginService loginService = new LoginServiceImpl(connection);
				loginService.validate(user);
				if (user.getId() > 0) {
					final UserService saveDateuser = new UserServiceImpl(connection);
			    	user.setUserLastAcessDate(Calendar.getInstance(tz));
					saveDateuser.save(user);
					
					final AccessProfilePermissionFilterDTO accessProfilePermissionFilter = new AccessProfilePermissionFilterDTO();					
					accessProfilePermissionFilter.setAccessProfileId(user.getAccessProfile().getId());
					accessProfilePermissionFilter.setInactive(user.getAccessProfile().isInactive());
										
					final AccessProfilePermissionService accessProfilePermissionService = new AccessProfilePermissionServiceImpl(connection);	
					user.getAccessProfile().setPermissions(accessProfilePermissionService.getByAccessProfile(accessProfilePermissionFilter));
					
					boolean hasPermissionAcess = false;					
					
					if(user.hasPermissionManager()) {
						hasPermissionAcess = true;
					}
					
					if(!hasPermissionAcess) {
						throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso a aplicação Manager.");
					}

					request.getSession().setMaxInactiveInterval((-1));
					request.getSession().setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER, user);
					
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

					final AddressService addressService = new AddressServiceImpl(connection);
					request.getSession().setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_ADDRESS_STATES, addressService.getAllStatesMap());
					request.getSession().setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_ADDRESS_COUNTRIES, addressService.getAllCountry());
					request.getSession().setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_PERMISSIONS, permissions);

					result = ConstantDataManager.RESULT_REDIRECT_HOME;
				} else {
					message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_USER_PASSWORD_INVALID, null);
					result = ConstantDataManager.RESULT_LOGIN;
				}
			} else {
				message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_USER_PASSWORD_INVALID, null);
				result = ConstantDataManager.RESULT_LOGIN;
			}
		} catch (final Exception e) {
			result = ConstantDataManager.RESULT_LOGIN;
			e.printStackTrace();
			message = e.getMessage();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE, message);
		return result;
	}

	@GetMapping(value = ConstantDataManager.PORTAL_CONTROLLER_HOME)
	public String home(final HttpServletRequest request) throws Exception {
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
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
			long userId = user.getId();
			final List<Combobox> userChidrensParent = userParentService.getAllParentCombobox(userId);
			request.setAttribute(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT, userChidrensParent);			
		}
		catch(final Exception e) 
		{
			e.printStackTrace();
			message = e.getMessage();
		}
		finally 
		{
			if(connection!=null)
			{
				connection.close();
			}
		}
		
		request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE, message);
		return ConstantDataManager.RESULT_HOME;
	}

	@RequestMapping(value = ConstantDataManager.PORTAL_CONTROLLER_LOGOUT, method = { RequestMethod.GET, RequestMethod.POST })
	public String logout(final HttpServletRequest request) throws Exception{
		Connection connection = null;

		try {
			
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user!=null)
			{
				connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
				if (connection == null) {
					throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
				}
				
				final UserService userService = new UserServiceImpl(connection);
				user.setUserExitAcessDate(Calendar.getInstance(tz));
				userService.updateLastAccessFinishUser(user);
			}			
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		removeSession(request);
		return ConstantDataManager.RESULT_LOGIN;
	}

	@GetMapping(value = ConstantDataManager.PORTAL_CONTROLLER_ACESSO_EXPIRADO)
	public String acessoExpirado(final HttpServletRequest request) {
		removeSession(request);
		request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE, this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
		return ConstantDataManager.RESULT_LOGIN;
	}

	@ExceptionHandler(LoginException.class)
	public static String handleLoginException(final HttpServletRequest request, final LoginException le) {
		removeSession(request);
		request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE, le.getMessage());
		return ConstantDataManager.RESULT_LOGIN;
	}

	@ExceptionHandler(Exception.class)
	public static String handleException(final HttpServletRequest request, final Exception ex) {
		removeSession(request);
		request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE, ex.getMessage());
		return ConstantDataManager.RESULT_LOGIN;
	}

	private static void removeSession(final HttpServletRequest request) {
		request.getSession().removeAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
		request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE, com.manager.systems.web.common.utils.ConstantDataManager.BLANK);
	}
}