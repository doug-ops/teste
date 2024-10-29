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
import com.manager.systems.common.dto.adm.ProductDTO;
import com.manager.systems.common.dto.adm.ReportPersonDTO;
import com.manager.systems.common.dto.adm.ReportProductDTO;
import com.manager.systems.common.dto.adm.ReportProductGroupDTO;
import com.manager.systems.common.dto.adm.ReportProductSubGroupDTO;
import com.manager.systems.common.service.IntegrationSystemsService;
import com.manager.systems.common.service.adm.BankAccountService;
import com.manager.systems.common.service.adm.GameTypeService;
import com.manager.systems.common.service.adm.MachineTypeService;
import com.manager.systems.common.service.adm.PersonService;
import com.manager.systems.common.service.adm.ProductGroupService;
import com.manager.systems.common.service.adm.ProductService;
import com.manager.systems.common.service.adm.ProductSubGroupService;
import com.manager.systems.common.service.impl.IntegrationSystemsServiceImpl;
import com.manager.systems.common.service.impl.adm.BankAccountServiceImpl;
import com.manager.systems.common.service.impl.adm.GameTypeServiceImpl;
import com.manager.systems.common.service.impl.adm.MachineTypeServiceImpl;
import com.manager.systems.common.service.impl.adm.PersonServiceImpl;
import com.manager.systems.common.service.impl.adm.ProductGroupServiceImpl;
import com.manager.systems.common.service.impl.adm.ProductServiceImpl;
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
@RequestMapping(value=ConstantDataManager.PRODUCT_MAINTENANCE_CONTROLLER)
public class ProductMaintenanceController extends BaseController
{
	@RequestMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD, method = {RequestMethod.GET, RequestMethod.POST})
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		String result = ConstantDataManager.PRODUCT_MAINTENANCE_OPEN_METHOD_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
		Connection connection = null;
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			if(!user.hasPermission("MENU_CADASTROS", "MENU_CADASTROS_PRODUTO")) {
				result = ConstantDataManager.RESULT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Cadastro de Produto.");
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
						
			final PersonService companyService = new PersonServiceImpl(connection);
			final ReportPersonDTO reportPerson = new ReportPersonDTO();
			reportPerson.setPersonIdFrom(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			reportPerson.setPersonIdTo(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			reportPerson.setInactive(com.manager.systems.common.utils.ConstantDataManager.FALSE_INT);
			reportPerson.setDescription(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			reportPerson.setObjectType(ObjectType.COMPANY.getType());
			reportPerson.setUserOperation(user.getId());
			final List<Combobox> companies = companyService.getAllCombobox(reportPerson);
			request.setAttribute(ConstantDataManager.PARAMETER_COMPANIES, companies);
			
			final GameTypeService gameTypeService = new GameTypeServiceImpl(connection);
			
			final List<Combobox> gameTypes = gameTypeService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_GAME_TYPES, gameTypes);
			
			final MachineTypeService machineTypeService = new MachineTypeServiceImpl(connection);
			
			final List<Combobox> machineTypes = machineTypeService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_MACHINE_TYPES, machineTypes);
			
			request.setAttribute(ConstantDataManager.PARAMETER_OBJECT_TYPE_ALIAS, com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_PRODUCT);
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
			final String groupIdParameter = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_GROUP);
			final String subGroupIdParameter = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_SUB_GROUP);
			String inputMovementParameter = request.getParameter(ConstantDataManager.PARAMETER_INPUT_MOVEMENT);
			if(!StringUtils.isLong(inputMovementParameter))
			{
				inputMovementParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			String outputMovementParameter = request.getParameter(ConstantDataManager.PARAMETER_OUTPUT_MOVEMENT);
			if(!StringUtils.isLong(outputMovementParameter))
			{
				outputMovementParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			final String companyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY);
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
			
			String costPriceParameter = request.getParameter(ConstantDataManager.PARAMETER_COST_PRICE);
			if(!StringUtils.isNull(costPriceParameter))
			{
				if(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_ZERO_ZERO.equalsIgnoreCase(costPriceParameter))
				{
					costPriceParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_VIRGULA_ZERO_ZERO;
				}
				costPriceParameter = StringUtils.replaceNonDigits(costPriceParameter);
				final StringBuilder resultMoeda = new StringBuilder(costPriceParameter);
				if(resultMoeda.length()>2)
				{
					resultMoeda.insert(resultMoeda.length() - 2, com.manager.systems.common.utils.ConstantDataManager.PONTO);
				}						
				costPriceParameter = resultMoeda.toString();
			}
			else
			{
				costPriceParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
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
			
			String clockMovementParameter = request.getParameter(ConstantDataManager.PARAMETER_CLOCK_MOVEMENT);
			if(!StringUtils.isLong(clockMovementParameter))
			{
				clockMovementParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}

			String enableClockMovementParameter = request.getParameter(ConstantDataManager.PARAMETER_ENABLE_CLOCK_MOVEMENT);
			if(com.manager.systems.common.utils.ConstantDataManager.ON.equalsIgnoreCase(enableClockMovementParameter))
			{
				enableClockMovementParameter = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
			}
			else
			{
				enableClockMovementParameter = com.manager.systems.common.utils.ConstantDataManager.FALSE_STRING;
			}
			
			final String aliasProduct = request.getParameter(ConstantDataManager.PARAMETER_ALIAS_PRODUCT);
			
			String gameTypeIdParameter = request.getParameter(ConstantDataManager.PARAMETER_GAME_TYPE_ID);

			if (!StringUtils.isLong(gameTypeIdParameter)) {
				gameTypeIdParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			
			String machineTypeIdParameter = request.getParameter(ConstantDataManager.PARAMETER_MACHINE_TYPE_ID);

			if (!StringUtils.isLong(machineTypeIdParameter)) {
				machineTypeIdParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
						
			final ProductDTO product = new ProductDTO();
			product.setId(idParameter);
			product.setDescription(descriptionParameter.toUpperCase());
			product.setGroupId(groupIdParameter);
			product.setSubGroupId(subGroupIdParameter);
			product.setCostPrice(costPriceParameter);
			product.setSalePrice(salePriceParameter);
			product.setInputMovement(inputMovementParameter);
			product.setOutputMovement(outputMovementParameter);
			product.setConversionFactor(conversionFactorParameter);
			product.setCompanyId(companyParameter);
			product.setInactive(Boolean.valueOf(activeParameter));
			product.setClockMovement(clockMovementParameter);
			product.setEnableClockMovement(Boolean.valueOf(enableClockMovementParameter));
			final ChangeData changeData = new ChangeData();
			changeData.setUserChange(user.getId());
			changeData.setChangeDate(Calendar.getInstance(tz).getTime());
			product.setChangeData(changeData);
			product.setAliasProduct(aliasProduct.toUpperCase());
			product.setGameTypeId(Integer.valueOf(gameTypeIdParameter));
			product.setMachineTypeId(Integer.valueOf(machineTypeIdParameter));
			
			if(integrationSystemValuesParameter!=null && integrationSystemValuesParameter.length>0)
			{
				for (final String item : integrationSystemValuesParameter) 
				{
					final String[] itemArray = item.split(com.manager.systems.common.utils.ConstantDataManager.PONTO_VIRGULA_STRING);
					if(itemArray.length==2)
					{
						product.addIntegrationSystemsValue(itemArray[0], itemArray[1]);
					}
				}
			}
					
			product.validateFormSave();
			final ProductService productService = new ProductServiceImpl(connection);						
			final boolean isSaved = productService.save(product);
			
			if(!isSaved)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
			}
			
			final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
			final IntegrationSystemsDTO integrationSystem = new IntegrationSystemsDTO();
			integrationSystem.setObjectId(product.getId());
			integrationSystem.setObjectType(ObjectType.PRODUCT.getType());
			integrationSystem.setInactive(true);
			integrationSystem.setUserChange(product.getChangeData().getUserChange());
			integrationSystem.setChangeDate(Calendar.getInstance(tz));
			integrationSystemsService.delete(integrationSystem);
			
			if(product.getIntegrationSystemsValues()!=null && product.getIntegrationSystemsValues().size()>0)
			{			
				for (final IntegrationSystemsDTO item : product.getIntegrationSystemsValues()) 
				{
					integrationSystem.setInactive(false);
					integrationSystem.setObjectType(ObjectType.PRODUCT.getType());
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
			
			final ProductDTO product = new ProductDTO();
			product.setId(idParameter);
			product.setInactive(Boolean.valueOf(inactiveParameter));
			final ChangeData changeData = new ChangeData();
			changeData.setUserChange(user.getId());
			changeData.setChangeDate(Calendar.getInstance(tz).getTime());
			product.setChangeData(changeData);
			final ProductService productService = new ProductServiceImpl(connection);	
			final boolean wasInactivated = productService.inactive(product);
			
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
		
		ProductDTO product = null;
		
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
			product = new ProductDTO();
			product.setId(idParameter);
			product.setUser(user.getId());
						
			final ProductService productService = new ProductServiceImpl(connection);	
			productService.get(product);
			
			if(product.getUser()==0)
			{
				result.put(ConstantDataManager.NOT_USER_PRODUCT_VALUE, true);
				throw new AdminException(this.messages.get(ConstantDataManager.MESSAGE_NOT_USER_PRODUCT, null));
			}
			
			if(product.getDescription()==null)
			{
				throw new AdminException(this.messages.get(ConstantDataManager.MESSAGE_NOT_FOUND_PRODUCT, null));
			}
			
			final ReportProductSubGroupDTO reportProductSubGroup = new ReportProductSubGroupDTO();
			reportProductSubGroup.setProductSubGroupIdFrom(com.manager.systems.web.common.utils.ConstantDataManager.ZERO_STRING);
			reportProductSubGroup.setProductSubGroupIdTo(com.manager.systems.web.common.utils.ConstantDataManager.ZERO_STRING);
			reportProductSubGroup.setProductGroupIds(product.getGroupId());	
			reportProductSubGroup.setInactive(com.manager.systems.web.common.utils.ConstantDataManager.ZERO_STRING);
			reportProductSubGroup.setDescription(com.manager.systems.web.common.utils.ConstantDataManager.BLANK);
			
			final ProductSubGroupService productSubGroupService = new ProductSubGroupServiceImpl(connection);
			final List<Combobox> productSubGroups = productSubGroupService.getAllCombobox(reportProductSubGroup);
			result.put(ConstantDataManager.PARAMETER_PRODUCT_SUB_GROUPS, productSubGroups);
			
			final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
			final IntegrationSystemsDTO integrationSystem = new IntegrationSystemsDTO();
			integrationSystem.setObjectId(product.getId());
			integrationSystem.setObjectType(ObjectType.PRODUCT.getType());
			product.getIntegrationSystemsValues().addAll(integrationSystemsService.getAll(integrationSystem));	
						
			status = true;
			result.put(ConstantDataManager.PARAMETER_PRODUCT, product);
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
			
			final ReportProductDTO reportProduct = this.processFilter(request, connection);
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT, reportProduct);
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
	
	public ReportProductDTO processFilter(final HttpServletRequest request, final Connection connection) throws Exception
	{
		final ReportProductDTO report = new ReportProductDTO();
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
			
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);

			report.setProductIdFrom(idFromParameter);
			report.setProductIdTo(idToParameter);
			report.setInactive(inactiveParameter);
			report.setDescription(descriptionParameter);
			report.setUser(user.getId());
			
			final ProductService productService = new ProductServiceImpl(connection);
			productService.getAll(report);
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
			
			final String descriptionParameter = request.getParameter(ConstantDataManager.PARAMETER_DESCRIPTION);
			final String companyIdParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_ID);
			final String bankAccountIdIdParameter = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_ID);
			String inactiveParameter = request.getParameter(ConstantDataManager.PARAMETER_INACTIVE);
			if(!StringUtils.isLong(inactiveParameter))
			{
				inactiveParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}			
			final ReportProductDTO reportProduct = new ReportProductDTO();
			if(StringUtils.isLong(descriptionParameter))
			{
				reportProduct.setProductIdFrom(descriptionParameter);
				reportProduct.setProductIdTo(descriptionParameter);	
				reportProduct.setDescription(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			}
			else
			{
				reportProduct.setProductIdFrom(com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING);
				reportProduct.setProductIdTo(com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING);	
				reportProduct.setDescription(descriptionParameter);
			}
			reportProduct.setInactive(inactiveParameter);
			if(StringUtils.isLong(companyIdParameter))
			{				
				reportProduct.setCompanyId(companyIdParameter);
			}
			else
			{
				reportProduct.setCompanyId(com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING);
			}
			if(StringUtils.isLong(bankAccountIdIdParameter))
			{				
				reportProduct.setBankAccountId(bankAccountIdIdParameter);
			}
			else
			{
				reportProduct.setBankAccountId(com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING);
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			if(StringUtils.isLong(reportProduct.getBankAccountId()))
			{
				final BankAccountService bankAccountService = new BankAccountServiceImpl(connection);
				final int companyId = bankAccountService.getCompanyIdByBankAccount(Integer.valueOf(reportProduct.getBankAccountId()));
				reportProduct.setCompanyId(String.valueOf(companyId));
			}
			
			final ProductService productService = new ProductServiceImpl(connection);
			final List<Combobox> items = productService.getAllAutocomplete(reportProduct);
			
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT, items);
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
	
	@RequestMapping(value = ConstantDataManager.COMMON_CONTROLLER_GET_PRODUCTS, method=RequestMethod.POST)
	public ResponseEntity<String> getAllProducts(final HttpServletRequest request) throws Exception
	{
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
				
		Connection connection = null;
		
		final ReportProductDTO reportProduct = new ReportProductDTO();
		
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

	        final String bankAccountOrigin = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_ORIGIN);
	        if(StringUtils.isLong(bankAccountOrigin))
	        {
	        	reportProduct.setBankAccountId(bankAccountOrigin);
	        }
	        final String bankAccountDestiny = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_DESTINY);
	        if(StringUtils.isLong(bankAccountDestiny))
	        {
	        	reportProduct.setBankAccountDestinyId(bankAccountDestiny);
	        }
	        			
			if(StringUtils.isLong(reportProduct.getBankAccountId()))
			{
				final BankAccountService bankAccountService = new BankAccountServiceImpl(connection);
				final int companyId = bankAccountService.getCompanyIdByBankAccount(Integer.valueOf(reportProduct.getBankAccountId()));
				reportProduct.setCompanyId(String.valueOf(companyId));
			}
			
			final ProductService productService = new ProductServiceImpl(connection);					
			productService.getAllProducts(reportProduct);
			
			status = true;
			result.put(ConstantDataManager.PARAMETER_REPORT_PRODUCT, reportProduct);
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
}