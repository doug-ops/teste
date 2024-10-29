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
import com.manager.systems.common.dto.adm.ProductSubGroupDTO;
import com.manager.systems.common.dto.adm.ReportProductGroupDTO;
import com.manager.systems.common.dto.adm.ReportProductSubGroupDTO;
import com.manager.systems.common.service.IntegrationSystemsService;
import com.manager.systems.common.service.adm.ProductGroupService;
import com.manager.systems.common.service.adm.ProductSubGroupService;
import com.manager.systems.common.service.impl.IntegrationSystemsServiceImpl;
import com.manager.systems.common.service.impl.adm.ProductGroupServiceImpl;
import com.manager.systems.common.service.impl.adm.ProductSubGroupServiceImpl;
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
@RequestMapping(value=ConstantDataManager.PRODUCT_SUB_GROUP_MAINTENANCE_CONTROLLER)
public class ProductSubGroupMaintenanceController extends BaseController
{
	@RequestMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD, method = {RequestMethod.GET, RequestMethod.POST})
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		String result = ConstantDataManager.PRODUCT_SUB_GROUP_MAINTENANCE_OPEN_METHOD_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
		Connection connection = null;
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			if(!user.hasPermission("MENU_CADASTROS", "MENU_CADASTROS_SUBGRUPO_PRODUTO")) {
				result = ConstantDataManager.RESULT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Cadastro de SubGrupo de Produto.");
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
			final List<Combobox> integrationsSystems = integrationSystemsService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_INTEGRATION_SYSTEMS, integrationsSystems);
			
			final ProductGroupService productGroupService = new ProductGroupServiceImpl(connection);
			final ReportProductGroupDTO reportProductGroup = new ReportProductGroupDTO();
			reportProductGroup.setDescription(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			reportProductGroup.setInactive(com.manager.systems.common.utils.ConstantDataManager.FALSE_INT);
			final List<Combobox> productGroups = productGroupService.getAllCombobox(reportProductGroup);
			request.setAttribute(ConstantDataManager.PARAMETER_PRODUCT_GROUPS, productGroups);
			
			request.setAttribute(ConstantDataManager.PARAMETER_OBJECT_TYPE_ALIAS, com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_PROD_SUB_GROUP);
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
			final String productGroupParameter = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_GROUP);
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
			
			String salePriceParameter = request.getParameter(ConstantDataManager.PARAMETER_SALE_PRICE);
			if(!StringUtils.isNull(salePriceParameter))
			{
				if(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_ZERO_ZERO.equalsIgnoreCase(salePriceParameter))
				{
					salePriceParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_VIRGULA_ZERO_ZERO;
				}
				salePriceParameter = StringUtils.replaceNonDigits(salePriceParameter);
				final StringBuilder resultMoeda = new StringBuilder(salePriceParameter);
				if(resultMoeda.length()>2)
				{
					resultMoeda.insert(resultMoeda.length() - 2, com.manager.systems.common.utils.ConstantDataManager.PONTO);
				}						
				salePriceParameter = resultMoeda.toString();
			}
			else
			{
				salePriceParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}

			String conversionFactorParameter = request.getParameter(ConstantDataManager.PARAMETER_CONVERSION_FACTOR);
			if(!StringUtils.isNull(conversionFactorParameter))
			{
				if(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_ZERO_ZERO.equalsIgnoreCase(conversionFactorParameter))
				{
					conversionFactorParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_VIRGULA_ZERO_ZERO;
				}
				conversionFactorParameter = StringUtils.replaceNonDigits(conversionFactorParameter);
				final StringBuilder resultMoeda = new StringBuilder(conversionFactorParameter);
				if(resultMoeda.length()>2)
				{
					resultMoeda.insert(resultMoeda.length() - 2, com.manager.systems.common.utils.ConstantDataManager.PONTO);
				}						
				conversionFactorParameter = resultMoeda.toString();
			}
			else
			{
				conversionFactorParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			
			final ProductSubGroupDTO productSubGroup = new ProductSubGroupDTO();
			if(StringUtils.isLong(idParameter))
			{
				productSubGroup.setId(Integer.valueOf(idParameter));				
			}
			productSubGroup.setDescription(descriptionParameter.toUpperCase());
			productSubGroup.setInactive(Boolean.valueOf(activeParameter));
			productSubGroup.setGroupId(productGroupParameter);
			productSubGroup.setSalePrice(Double.valueOf(salePriceParameter));
			productSubGroup.setConversionFactor(Double.valueOf(conversionFactorParameter));
			
			final ChangeData changeData = new ChangeData();
			changeData.setUserChange(user.getId());
			changeData.setChangeDate(Calendar.getInstance(tz).getTime());
			productSubGroup.setChangeData(changeData);
			
			if(integrationSystemValuesParameter!=null && integrationSystemValuesParameter.length>0)
			{
				for (final String item : integrationSystemValuesParameter) 
				{
					final String[] itemArray = item.split(com.manager.systems.common.utils.ConstantDataManager.PONTO_VIRGULA_STRING);
					if(itemArray.length==2)
					{
						productSubGroup.addIntegrationSystemsValue(itemArray[0], itemArray[1]);
					}
				}
			}
				
			productSubGroup.validateFormSave();
			final ProductSubGroupService productSubGroupService = new ProductSubGroupServiceImpl(connection);						
			final boolean isSaved = productSubGroupService.save(productSubGroup);
			
			if(!isSaved)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
			}
			
			final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
			final IntegrationSystemsDTO integrationSystem = new IntegrationSystemsDTO();
			integrationSystem.setObjectId(String.valueOf(productSubGroup.getId()));
			integrationSystem.setObjectType(ObjectType.PROD_SUB_GROUP.getType());
			integrationSystem.setInactive(true);
			integrationSystem.setUserChange(productSubGroup.getChangeData().getUserChange());
			integrationSystem.setChangeDate(Calendar.getInstance(tz));
			integrationSystemsService.delete(integrationSystem);
			
			if(productSubGroup.getIntegrationSystemsValues()!=null && productSubGroup.getIntegrationSystemsValues().size()>0)
			{			
				for (final IntegrationSystemsDTO item : productSubGroup.getIntegrationSystemsValues()) 
				{
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
			
			final ProductSubGroupDTO productSubGroup = new ProductSubGroupDTO();
			if(StringUtils.isLong(idParameter))
			{
				productSubGroup.setId(Integer.valueOf(idParameter));				
			}
			productSubGroup.setInactive(Boolean.valueOf(inactiveParameter));
			final ChangeData changeData = new ChangeData();
			changeData.setUserChange(user.getId());
			changeData.setChangeDate(Calendar.getInstance(tz).getTime());
			productSubGroup.setChangeData(changeData);
			final ProductSubGroupService productSubGroupService = new ProductSubGroupServiceImpl(connection);	
			final boolean wasInactivated = productSubGroupService.inactive(productSubGroup);
			
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
		
		ProductSubGroupDTO productSubGroup = null;
		
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
			productSubGroup = new ProductSubGroupDTO();
			if(StringUtils.isLong(idParameter))
			{
				productSubGroup.setId(Integer.valueOf(idParameter));				
			}
						
			final ProductSubGroupService productSubGroupService = new ProductSubGroupServiceImpl(connection);	
			productSubGroupService.get(productSubGroup);
			if(productSubGroup.getDescription()==null)
			{
				throw new AdminException(this.messages.get(ConstantDataManager.MESSAGE_SUBGROUP_INVALID, null));
			}
			
			final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
			final IntegrationSystemsDTO integrationSystem = new IntegrationSystemsDTO();
			integrationSystem.setObjectId(String.valueOf(productSubGroup.getId()));
			integrationSystem.setObjectType(ObjectType.PROD_SUB_GROUP.getType());
			productSubGroup.getIntegrationSystemsValues().addAll(integrationSystemsService.getAll(integrationSystem));	
						
			status = true;
			result.put(ConstantDataManager.PARAMETER_PRODUCT_SUB_GROUP, productSubGroup);
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
			
			final ReportProductSubGroupDTO reportProductGroup = this.processFilter(request, connection);
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
	
	public ReportProductSubGroupDTO processFilter(final HttpServletRequest request, final Connection connection) throws Exception
	{
		final ReportProductSubGroupDTO report = new ReportProductSubGroupDTO();
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
			
			report.setProductSubGroupIdFrom(idFromParameter);
			report.setProductSubGroupIdTo(idToParameter);
			report.setInactive(inactiveParameter);
			report.setDescription(descriptionParameter);
			
			final ProductSubGroupService productSubGroupService = new ProductSubGroupServiceImpl(connection);
			productSubGroupService.getAll(report);
		}
		catch(final Exception ex) 
		{
			throw ex;
		}		
		return report;
	}	
	
	@RequestMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_AUTOCOMPLETE_METHOD, method=RequestMethod.POST)
	public ResponseEntity<String> autocomplete(final HttpServletRequest request) throws Exception
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
			
			final String productGroupParameter = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_GROUP);
			if(!StringUtils.isLong(productGroupParameter))
			{
				throw new Exception(this.messages.get(ConstantDataManager.MESSAGE_GROUP_INVALID, null));
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final ReportProductSubGroupDTO reportProductSubGroup = new ReportProductSubGroupDTO();
			reportProductSubGroup.setProductSubGroupIdFrom(com.manager.systems.web.common.utils.ConstantDataManager.ZERO_STRING);
			reportProductSubGroup.setProductSubGroupIdTo(com.manager.systems.web.common.utils.ConstantDataManager.ZERO_STRING);
			reportProductSubGroup.setProductGroupIds(productGroupParameter);	
			reportProductSubGroup.setInactive(com.manager.systems.web.common.utils.ConstantDataManager.ZERO_STRING);
			reportProductSubGroup.setDescription(com.manager.systems.web.common.utils.ConstantDataManager.BLANK);
			
			final ProductSubGroupService productSubGroupService = new ProductSubGroupServiceImpl(connection);
			final List<Combobox> productSubGroups = productSubGroupService.getAllCombobox(reportProductSubGroup);
			result.put(ConstantDataManager.PARAMETER_PRODUCT_SUB_GROUPS, productSubGroups);
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
}