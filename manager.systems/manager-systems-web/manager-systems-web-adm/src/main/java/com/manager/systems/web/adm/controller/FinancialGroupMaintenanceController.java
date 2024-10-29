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
import com.manager.systems.common.dto.ComboboxFilterDTO;
import com.manager.systems.common.dto.adm.FinancialGroupDTO;
import com.manager.systems.common.dto.adm.FinancialSubGroupDTO;
import com.manager.systems.common.dto.adm.ReportFinancialGroupDTO;
import com.manager.systems.common.service.ComboboxService;
import com.manager.systems.common.service.adm.FinancialGroupService;
import com.manager.systems.common.service.adm.FinancialSubGroupService;
import com.manager.systems.common.service.impl.ComboboxServiceImpl;
import com.manager.systems.common.service.impl.adm.FinancialGroupServiceImpl;
import com.manager.systems.common.service.impl.adm.FinancialSubGroupServiceImpl;
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
@RequestMapping(value=ConstantDataManager.FINANCIAL_GROUP_MAINTENANCE_CONTROLLER)
public class FinancialGroupMaintenanceController extends BaseController
{
	@RequestMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD, method = {RequestMethod.GET, RequestMethod.POST})
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		String result = ConstantDataManager.FINANCIAL_GROUP_MAINTENANCE_OPEN_METHOD_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
		Connection connection = null;
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			if(!user.hasPermission("MENU_FINANCEIRO", "MENU_FINANCEIRO_GRUPO_FINANCEIRO")) {
				result = ConstantDataManager.RESULT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Financeiro Grupo Financeiro.");
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
						
			final ComboboxService comboboxService = new ComboboxServiceImpl(connection);
			final List<Combobox> revenueSource = comboboxService.getCombobox(new ComboboxFilterDTO(1, false));
			request.setAttribute(ConstantDataManager.PARAMETER_REVENUE_SOURCE, revenueSource);			
			final List<Combobox> revenueType = comboboxService.getCombobox(new ComboboxFilterDTO(2, false));
			request.setAttribute(ConstantDataManager.PARAMETER_REVENUE_TYPE, revenueType);
			
			request.setAttribute(ConstantDataManager.PARAMETER_OBJECT_TYPE_ALIAS, com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_FIN_GROUP);
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
		
		FinancialGroupDTO financialGroup = null;
		
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
			
			String idParameter = request.getParameter(ConstantDataManager.PARAMETER_ID);			
			final String descriptionParameter = request.getParameter(ConstantDataManager.PARAMETER_DESCRIPTION);			
	
			String activeParameter = request.getParameter(ConstantDataManager.PARAMETER_ACTIVE);
			if(com.manager.systems.common.utils.ConstantDataManager.ON.equalsIgnoreCase(activeParameter))
			{
				activeParameter = com.manager.systems.common.utils.ConstantDataManager.FALSE_STRING;
			}
			else
			{
				activeParameter = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
			}
			if(StringUtils.isNull(idParameter)){
				final FinancialGroupService financialGroupService = new FinancialGroupServiceImpl(connection);	
				final String maxId = financialGroupService.getMaxId();
				double idMaxDouble = 1d;
				if(StringUtils.isDouble(maxId) || StringUtils.isLong(maxId)) {
					idMaxDouble = Double.parseDouble(maxId);
					idMaxDouble = (idMaxDouble + 1); 
				}					
				idParameter = StringUtils.formataDecimalQuantity(idMaxDouble);
				idParameter = StringUtils.padLeft(idParameter, 6, com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING);
				idParameter = idParameter.replace(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING, com.manager.systems.common.utils.ConstantDataManager.PONTO);
			}			
			financialGroup = new FinancialGroupDTO();
			financialGroup.setId(idParameter);
			financialGroup.setDescription(StringUtils.decodeString(descriptionParameter).toUpperCase());
			financialGroup.setInactive(Boolean.valueOf(activeParameter));
			final ChangeData changeData = new ChangeData();
			changeData.setUserChange(user.getId());
			changeData.setChangeDate(Calendar.getInstance(tz).getTime());
			financialGroup.setChangeData(changeData);
			
			final String isSaveDynamic = request.getParameter(ConstantDataManager.PARAMETER_IS_SAVE_DYNAMIC);
			String idParameterSubGroup = request.getParameter(ConstantDataManager.PARAMETER_ID_SUB_GROUP_1);	
			
			final Map<String, FinancialSubGroupDTO> subGroups = new TreeMap<String, FinancialSubGroupDTO>();			
				if(StringUtils.isLong(isSaveDynamic) && Integer.parseInt(isSaveDynamic) == 1) {
					if(StringUtils.isNull(idParameterSubGroup)){
						
						final FinancialSubGroupDTO financialSubGroup = new FinancialSubGroupDTO();	
						financialSubGroup.setGroupId(idParameter);
						
						final FinancialSubGroupService financialSubGroupService = new FinancialSubGroupServiceImpl(connection);		
						financialSubGroupService.getMaxId(financialSubGroup);
						String maxIdGroup = financialSubGroup.getMaxIdSubGroup();
						
						if(StringUtils.isNull(maxIdGroup)) {
							maxIdGroup = idParameter;
						}
						double idMaxDoubleSubGroup = 0d;
						if(StringUtils.isDouble(maxIdGroup) || StringUtils.isLong(maxIdGroup)) {
							idMaxDoubleSubGroup = Double.parseDouble(maxIdGroup);
							//TODO: Bug quando maxIdSubGroup = "31.009" e for somar 0.001
							idMaxDoubleSubGroup = (idMaxDoubleSubGroup + 0.001); 
						}					
						idParameterSubGroup = StringUtils.formataDecimalQuantity(idMaxDoubleSubGroup);
						idParameterSubGroup = StringUtils.padLeft(idParameterSubGroup, 6, com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING);
						idParameterSubGroup = idParameterSubGroup.replace(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING, com.manager.systems.common.utils.ConstantDataManager.PONTO);
					}	
					for (final Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) 
					{
					    final String parameterName = entry.getKey();
					    final String paramterValue = entry.getValue()[0];
					    				    
					    if(parameterName.indexOf("descriptionSubGroup_1")>-1 && !StringUtils.isNull(paramterValue)) {
					    	FinancialSubGroupDTO subgroup = subGroups.get(paramterValue);
					    	if(subgroup==null) {
					    		subgroup = new FinancialSubGroupDTO();
					    	}
				    		subgroup.setId(idParameterSubGroup);
				    		subgroup.setDescription(StringUtils.decodeString(request.getParameter("descriptionSubGroup_1")).toUpperCase());
				    		subgroup.setRevenueSource(request.getParameter("revenueSource_1"));
				    		subgroup.setRevenueType(request.getParameter("revenueType_1"));
				    		subgroup.setGroupId(idParameter);
				    		subgroup.setChangeData(changeData);
				    		subGroups.put(subgroup.getId(), subgroup);
					    }
					}
				}
			else {
			for (final Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) 
			{
			    final String parameterName = entry.getKey();
			    final String paramterValue = entry.getValue()[0];
			    
				    if(parameterName.indexOf("idSubgroup_")>-1 && !StringUtils.isNull(paramterValue)) {
			    	FinancialSubGroupDTO subgroup = subGroups.get(paramterValue);
			    	if(subgroup==null) {
			    		subgroup = new FinancialSubGroupDTO();
			    	}
		    		subgroup.setId(paramterValue);
		    		subgroup.setDescription(StringUtils.decodeString(request.getParameter("descriptionSubGroup_"+paramterValue)).toUpperCase());
		    		subgroup.setRevenueSource(request.getParameter("revenueSource_"+paramterValue));
		    		subgroup.setRevenueType(request.getParameter("revenueType_"+paramterValue));
		    		subgroup.setGroupId(idParameter);
		    		subgroup.setChangeData(changeData);
		    		subGroups.put(subgroup.getId(), subgroup);
			    }
			}
			}		
			final FinancialGroupService financialGroupService = new FinancialGroupServiceImpl(connection);						
			boolean isSaved = financialGroupService.save(financialGroup);	
			if(!isSaved)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
			}	
			
			final FinancialSubGroupService financialSubGroupService = new FinancialSubGroupServiceImpl(connection);						
			if(!StringUtils.isLong(isSaveDynamic) || Integer.parseInt(isSaveDynamic) == 0) {
			financialSubGroupService.inactiveAllByGroup(new FinancialSubGroupDTO(financialGroup.getId(), true, financialGroup.getChangeData()));
			}
			
			for(final Map.Entry<String, FinancialSubGroupDTO> entry : subGroups.entrySet()) {
				isSaved = financialSubGroupService.save(entry.getValue());
				if(!isSaved)
				{
					throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
				}	
				result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_DATA_SUB_GROUP, entry.getValue());
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
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_DATA, financialGroup);
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
						
			final String bankAccountIdParameter = request.getParameter(ConstantDataManager.PARAMETER_ID);			
			final String inactiveParameter = request.getParameter(ConstantDataManager.PARAMETER_INACTIVE);
						
			if(StringUtils.isNull(bankAccountIdParameter) || StringUtils.isNull(inactiveParameter))
			{	
				throw new AdminException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_DATA_REQUIRED_ERROR, null));				
			}
			
			final FinancialGroupDTO financialGroup = new FinancialGroupDTO();
			financialGroup.setId(bankAccountIdParameter);
			financialGroup.setInactive(Boolean.valueOf(inactiveParameter));
			final ChangeData changeData = new ChangeData();
			changeData.setUserChange(user.getId());
			changeData.setChangeDate(Calendar.getInstance(tz).getTime());
			financialGroup.setChangeData(changeData);
			final FinancialGroupService financialGroupService = new FinancialGroupServiceImpl(connection);	
			final boolean wasInactivated = financialGroupService.inactive(financialGroup);
			
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
		
		FinancialGroupDTO financialGroup = null;
		
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
			financialGroup = new FinancialGroupDTO();
			financialGroup.setId(idParameter);
						
			final FinancialGroupService financialGroupService = new FinancialGroupServiceImpl(connection);	
			financialGroupService.get(financialGroup);
			if(financialGroup.getDescription()==null)
			{
				status = true;
				result.put(com.manager.systems.web.common.utils.ConstantDataManager.POPULATE_FORM, false);
			}
			else
			{
				result.put(com.manager.systems.web.common.utils.ConstantDataManager.POPULATE_FORM, true);
				status = true;				
			}
						
			result.put(ConstantDataManager.PARAMETER_FINANCIAL_GROUP, financialGroup);
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
			
			final ReportFinancialGroupDTO reportFinancialGroup = this.processFilter(request, connection);
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT, reportFinancialGroup);
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
	
	public ReportFinancialGroupDTO processFilter(final HttpServletRequest request, final Connection connection) throws Exception
	{
		final ReportFinancialGroupDTO report = new ReportFinancialGroupDTO();
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
			
			report.setFinancialGroupIdFrom(idFromParameter);
			report.setFinancialGroupIdTo(idToParameter);
			report.setInactive(inactiveParameter);
			report.setDescription(descriptionParameter);
			
			final FinancialGroupService financialGroupService = new FinancialGroupServiceImpl(connection);
			financialGroupService.getAll(report);
		}
		catch(final Exception ex) 
		{
			throw ex;
		}		
		return report;
	}	
}