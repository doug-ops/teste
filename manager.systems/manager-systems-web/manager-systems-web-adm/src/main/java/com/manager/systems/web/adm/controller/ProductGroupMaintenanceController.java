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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.manager.systems.common.dto.adm.IntegrationSystemsDTO;
import com.manager.systems.common.dto.adm.ProductGroupDTO;
import com.manager.systems.common.dto.adm.ReportProductGroupDTO;
import com.manager.systems.common.service.IntegrationSystemsService;
import com.manager.systems.common.service.adm.ProductGroupService;
import com.manager.systems.common.service.impl.IntegrationSystemsServiceImpl;
import com.manager.systems.common.service.impl.adm.ProductGroupServiceImpl;
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
@RequestMapping(value=ConstantDataManager.PRODUCT_GROUP_MAINTENANCE_CONTROLLER)
public class ProductGroupMaintenanceController extends BaseController
{
	@RequestMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD, method = {RequestMethod.GET, RequestMethod.POST})
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		String result = ConstantDataManager.PRODUCT_GROUP_MAINTENANCE_OPEN_METHOD_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
		Connection connection = null;
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			if(!user.hasPermission("MENU_CADASTROS", "MENU_CADASTROS_GRUPO_PRODUTO")) {
				result = ConstantDataManager.RESULT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Cadastro de Grupo de Produto.");
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
			final List<Combobox> integrationsSystems = integrationSystemsService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_INTEGRATION_SYSTEMS, integrationsSystems);
			
			request.setAttribute(ConstantDataManager.PARAMETER_OBJECT_TYPE_ALIAS, com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_PROD_GROUP);
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
	
	@RequestMapping(value=ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD, method=RequestMethod.POST)
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
						
			final ProductGroupDTO productGroup = new ProductGroupDTO();
			if(StringUtils.isLong(idParameter))
			{
				productGroup.setId(Integer.valueOf(idParameter));				
			}
			productGroup.setDescription(descriptionParameter.toUpperCase());
			productGroup.setInactive(Boolean.valueOf(activeParameter));
			final ChangeData changeData = new ChangeData();
			changeData.setUserChange(user.getId());
			changeData.setChangeDate(Calendar.getInstance(tz).getTime());
			productGroup.setChangeData(changeData);
			
			String transferHab = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_TRANSFER_HAB);
	        if(!StringUtils.isNull(transferHab))
			{
				if(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_ZERO_ZERO.equalsIgnoreCase(transferHab))
				{
					transferHab = com.manager.systems.common.utils.ConstantDataManager.ZERO_VIRGULA_ZERO_ZERO;
				}
				transferHab = StringUtils.replaceNonDigits(transferHab);
				final StringBuilder resultMoeda = new StringBuilder(transferHab);
				if(resultMoeda.length()>2)
				{
					resultMoeda.insert(resultMoeda.length() - 2, com.manager.systems.common.utils.ConstantDataManager.PONTO);
				}						
				transferHab = resultMoeda.toString();
			}
			else
			{
				transferHab = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
	        
	        productGroup.setTransferHab(Double.valueOf(transferHab));
			
			if(integrationSystemValuesParameter!=null && integrationSystemValuesParameter.length>0)
			{
				for (final String item : integrationSystemValuesParameter) 
				{
					final String[] itemArray = item.split(com.manager.systems.common.utils.ConstantDataManager.PONTO_VIRGULA_STRING);
					if(itemArray.length==2)
					{
						productGroup.addIntegrationSystemsValue(itemArray[0], itemArray[1]);
					}
				}
			}
			
			productGroup.validateFormSave();
			final ProductGroupService productGroupService = new ProductGroupServiceImpl(connection);						
			final boolean isSaved = productGroupService.save(productGroup);
			
			if(!isSaved)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
			}
			
			final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
			final IntegrationSystemsDTO integrationSystem = new IntegrationSystemsDTO();
			integrationSystem.setObjectId(String.valueOf(productGroup.getId()));
			integrationSystem.setObjectType(ObjectType.PROD_GROUP.getType());
			integrationSystem.setInactive(true);
			integrationSystem.setUserChange(productGroup.getChangeData().getUserChange());
			integrationSystem.setChangeDate(Calendar.getInstance(tz));
			integrationSystemsService.delete(integrationSystem);
			
			if(productGroup.getIntegrationSystemsValues()!=null && productGroup.getIntegrationSystemsValues().size()>0)
			{			
				for (final IntegrationSystemsDTO item : productGroup.getIntegrationSystemsValues()) 
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
			
			final ProductGroupDTO productGroup = new ProductGroupDTO();
			if(StringUtils.isLong(idParameter))
			{
				productGroup.setId(Integer.valueOf(idParameter));				
			}
			productGroup.setInactive(Boolean.valueOf(inactiveParameter));
			final ChangeData changeData = new ChangeData();
			changeData.setUserChange(user.getId());
			changeData.setChangeDate(Calendar.getInstance(tz).getTime());
			productGroup.setChangeData(changeData);
			final ProductGroupService productGroupService = new ProductGroupServiceImpl(connection);	
			final boolean wasInactivated = productGroupService.inactive(productGroup);
			
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
		
		ProductGroupDTO productGroup = null;
		
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
			productGroup = new ProductGroupDTO();
			if(StringUtils.isLong(idParameter))
			{
				productGroup.setId(Integer.valueOf(idParameter));				
			}
						
			final ProductGroupService productGroupService = new ProductGroupServiceImpl(connection);	
			productGroupService.get(productGroup);
			if(productGroup.getDescription()==null)
			{
				throw new AdminException(this.messages.get(ConstantDataManager.MESSAGE_GROUP_INVALID, null));
			}
			
			final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
			final IntegrationSystemsDTO integrationSystem = new IntegrationSystemsDTO();
			integrationSystem.setObjectId(String.valueOf(productGroup.getId()));
			integrationSystem.setObjectType(ObjectType.PROD_GROUP.getType());
			productGroup.getIntegrationSystemsValues().addAll(integrationSystemsService.getAll(integrationSystem));	
						
			status = true;
			result.put(ConstantDataManager.PARAMETER_PRODUCT_GROUP, productGroup);
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
			
			final ReportProductGroupDTO reportProductGroup = this.processFilter(request, connection);
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT, reportProductGroup);
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
	
	public ReportProductGroupDTO processFilter(final HttpServletRequest request, final Connection connection) throws Exception
	{
		final ReportProductGroupDTO report = new ReportProductGroupDTO();
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
			
			report.setProductGroupIdFrom(idFromParameter);
			report.setProductGroupIdTo(idToParameter);
			report.setInactive(inactiveParameter);
			report.setDescription(descriptionParameter);
			
			final ProductGroupService productGroupService = new ProductGroupServiceImpl(connection);
			productGroupService.getAll(report);
		}
		catch(final Exception ex) 
		{
			throw ex;
		}		
		return report;
	}	
}