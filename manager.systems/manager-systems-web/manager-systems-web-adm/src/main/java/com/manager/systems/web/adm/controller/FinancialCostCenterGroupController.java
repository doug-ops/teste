package com.manager.systems.web.adm.controller;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.manager.systems.common.dto.adm.FinancialCastCenterGroupDTO;
import com.manager.systems.common.dto.adm.IntegrationSystemsDTO;
import com.manager.systems.common.dto.adm.ReportFinancialCastCenterGroupDTO;
import com.manager.systems.common.service.IntegrationSystemsService;
import com.manager.systems.common.service.adm.FinancialCastCenterGroupService;
import com.manager.systems.common.service.impl.IntegrationSystemsServiceImpl;
import com.manager.systems.common.service.impl.adm.FinancialCastCenterGroupServiceImpl;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.ObjectType;
import com.manager.systems.model.admin.User;
import com.manager.systems.web.adm.exception.AdminException;
import com.manager.systems.web.adm.utils.ConstantDataManager;
import com.manager.systems.web.common.controller.BaseController;

@Controller
@RequestMapping(value=ConstantDataManager.FINANCIAL_COST_CENTER_GROUP_CONTROLLER)
public class FinancialCostCenterGroupController extends BaseController
{
	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD)
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		String result = ConstantDataManager.FINANCIAL_COST_CENTER_GROUP_OPEN_METHOD_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
		Connection connection = null;
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			if(!user.hasPermission("MENU_FINANCEIRO", "MENU_FINANCEIRO_CENTRO_CUSTO")) {
				result = ConstantDataManager.RESULT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Financeiro Centro de Custo.");
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
			final List<Combobox> integrationsSystems = integrationSystemsService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_INTEGRATION_SYSTEMS, integrationsSystems);
			
			request.setAttribute(ConstantDataManager.PARAMETER_OBJECT_TYPE_ALIAS, com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_FINANCIAL_COST_CENTER);
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
		return result;
	}
	
	@PostMapping(value=ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD)
	public ResponseEntity<String> save(final HttpServletRequest request) throws Exception
	{
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
			final String[] integrationSystemValuesParameter = request.getParameterValues(ConstantDataManager.PARAMETER_INTEGRATION_SYSTEMS_VALUES);
	
			String activeParameter = request.getParameter(ConstantDataManager.PARAMETER_ACTIVE);
			if(com.manager.systems.common.utils.ConstantDataManager.ON.equalsIgnoreCase(activeParameter))
			{
				activeParameter = com.manager.systems.common.utils.ConstantDataManager.FALSE_STRING;
			}
			else
			{
				activeParameter = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
			}
						
			final FinancialCastCenterGroupDTO financialCastCenterGroup = new FinancialCastCenterGroupDTO();
			if(StringUtils.isLong(idParameter))
			{
				financialCastCenterGroup.setId(Integer.valueOf(idParameter));				
			}
			financialCastCenterGroup.setDescription(descriptionParameter.toUpperCase());
			financialCastCenterGroup.setInactive(Boolean.valueOf(activeParameter));
			final ChangeData changeData = new ChangeData();
			changeData.setUserChange(user.getId());
			changeData.setChangeDate(Calendar.getInstance(tz).getTime());
			financialCastCenterGroup.setChangeData(changeData);
			
			if(integrationSystemValuesParameter!=null && integrationSystemValuesParameter.length>0)
			{
				for (final String item : integrationSystemValuesParameter) 
				{
					final String[] itemArray = item.split(com.manager.systems.common.utils.ConstantDataManager.PONTO_VIRGULA_STRING);
					if(itemArray.length==2)
					{
						financialCastCenterGroup.addIntegrationSystemsValue(itemArray[0], itemArray[1]);
					}
				}
			}
			
			financialCastCenterGroup.validateFormSave();
			final FinancialCastCenterGroupService financialCastCenterGroupService = new FinancialCastCenterGroupServiceImpl(connection);						
			final boolean isSaved = financialCastCenterGroupService.save(financialCastCenterGroup);
			
			if(!isSaved)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
			}
			
			final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
			final IntegrationSystemsDTO integrationSystem = new IntegrationSystemsDTO();
			integrationSystem.setObjectId(String.valueOf(financialCastCenterGroup.getId()));
			integrationSystem.setObjectType(ObjectType.PROD_GROUP.getType());
			integrationSystem.setInactive(true);
			integrationSystem.setUserChange(financialCastCenterGroup.getChangeData().getUserChange());
			integrationSystem.setChangeDate(Calendar.getInstance(tz));
			integrationSystemsService.delete(integrationSystem);
			
			if(financialCastCenterGroup.getIntegrationSystemsValues()!=null && financialCastCenterGroup.getIntegrationSystemsValues().size()>0)
			{			
				for (final IntegrationSystemsDTO item : financialCastCenterGroup.getIntegrationSystemsValues()) 
				{
					integrationSystem.setInactive(false);
					integrationSystem.setObjectType(ObjectType.PROD_GROUP.getType());
					integrationSystemsService.save(item);	
				}
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
	
	@RequestMapping(value=ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_INACTIVE_METHOD, method=RequestMethod.POST)
    public ResponseEntity<String> inactive(final HttpServletRequest request) throws Exception
	{
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
			
			final FinancialCastCenterGroupDTO financialCastCenterGroup = new FinancialCastCenterGroupDTO();
			if(StringUtils.isLong(idParameter))
			{
				financialCastCenterGroup.setId(Integer.valueOf(idParameter));				
			}
			financialCastCenterGroup.setInactive(Boolean.valueOf(inactiveParameter));
			final ChangeData changeData = new ChangeData();
			changeData.setUserChange(user.getId());
			changeData.setChangeDate(Calendar.getInstance(tz).getTime());
			financialCastCenterGroup.setChangeData(changeData);
			final FinancialCastCenterGroupService financialCastCenterService = new FinancialCastCenterGroupServiceImpl(connection);	
			final boolean wasInactivated = financialCastCenterService.inactive(financialCastCenterGroup);
			
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
	
	@RequestMapping(value=ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_DETAIL_METHOD, method=RequestMethod.POST)
	public ResponseEntity<String> detail(final HttpServletRequest request) throws Exception
	{
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
		
		FinancialCastCenterGroupDTO financialCastCenterGroup = null;
		
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
			financialCastCenterGroup = new FinancialCastCenterGroupDTO();
			if(StringUtils.isLong(idParameter))
			{
				financialCastCenterGroup.setId(Integer.valueOf(idParameter));				
			}
						
			final FinancialCastCenterGroupService financialCastCenterGroupService = new FinancialCastCenterGroupServiceImpl(connection);	
			financialCastCenterGroupService.get(financialCastCenterGroup);
			if(financialCastCenterGroup.getDescription()==null)
			{
				throw new AdminException(this.messages.get(ConstantDataManager.MESSAGE_GROUP_INVALID, null));
			}
			
			final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
			final IntegrationSystemsDTO integrationSystem = new IntegrationSystemsDTO();
			integrationSystem.setObjectId(String.valueOf(financialCastCenterGroup.getId()));
			integrationSystem.setObjectType(ObjectType.PROD_GROUP.getType());
			financialCastCenterGroup.getIntegrationSystemsValues().addAll(integrationSystemsService.getAll(integrationSystem));	
						
			status = true;
			result.put(ConstantDataManager.PARAMETER_FINANCIAL_COST_CENTER_GROUP, financialCastCenterGroup);
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
	
	@RequestMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD, method=RequestMethod.POST)
	public ResponseEntity<String> filter(final HttpServletRequest request) throws Exception
	{
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
			
			final ReportFinancialCastCenterGroupDTO reportFinancialCastCenterGroup = this.processFilter(request, connection);
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT, reportFinancialCastCenterGroup);
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
	
	public ReportFinancialCastCenterGroupDTO processFilter(final HttpServletRequest request, final Connection connection) throws Exception
	{
		final ReportFinancialCastCenterGroupDTO report = new ReportFinancialCastCenterGroupDTO();
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
			
			report.setFinancialCastCenterGroupIdFrom(idFromParameter);
			report.setFinancialCastCenterGroupIdTo(idToParameter);
			report.setInactive(inactiveParameter);
			report.setDescription(descriptionParameter);
			
			final FinancialCastCenterGroupService financialCastCenterGroupService = new FinancialCastCenterGroupServiceImpl(connection);
			financialCastCenterGroupService.getAll(report);
		}
		catch(final Exception ex) 
		{
			throw ex;
		}		
		return report;
	}	
}