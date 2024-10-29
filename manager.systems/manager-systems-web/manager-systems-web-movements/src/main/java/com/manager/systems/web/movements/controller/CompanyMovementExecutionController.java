package com.manager.systems.web.movements.controller;

import java.net.URLDecoder;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.manager.systems.common.dto.movement.company.CompanyMovementExecutionDTO;
import com.manager.systems.common.dto.movement.company.CompanyMovementExecutionItemDTO;
import com.manager.systems.common.service.impl.movement.company.CompanyMovementExecutionServiceImpl;
import com.manager.systems.common.service.movement.company.CompanyMovementExecutionService;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.model.admin.User;
import com.manager.systems.web.common.controller.BaseController;
import com.manager.systems.web.movements.exception.MovementException;
import com.manager.systems.web.movements.utils.ConstantDataManager;

@Controller
@RequestMapping(value=ConstantDataManager.COMPANY_MOVEMENT_EXECUTION_CONTROLLER)
public class CompanyMovementExecutionController extends BaseController
{
	private static final Log log = LogFactory.getLog(CompanyMovementExecutionController.class);
	
	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD)
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		log.info(ConstantDataManager.COMPANY_MOVEMENT_EXECUTION_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD);
		
		String result = ConstantDataManager.COMPANY_EXECUTION_MOVEMENT_CONTROLLER_OPEN_METHOD_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
		Connection connection = null;
		
		try
		{
			Calendar dateAtual = Calendar.getInstance(tz);
			
			SimpleDateFormat formatDate = new SimpleDateFormat(StringUtils.DATE_PATTERN_DD_MM_YYYY);
			
			String dateFromParameter = request.getParameter(ConstantDataManager.PARAMETER_DATE_FROM);
			String dateToParameter = request.getParameter(ConstantDataManager.PARAMETER_DATE_TO);
			if(StringUtils.isNull(dateFromParameter))
			{
				
				dateAtual.add(Calendar.DAY_OF_MONTH, -45); 
				String dateFormat = formatDate.format(dateAtual.getTime());	
				dateFromParameter = dateFormat;
			}
			if(StringUtils.isNull(dateToParameter))
			{
				dateAtual.add(Calendar.DAY_OF_MONTH, 15); 
				String dateFormat = formatDate.format(dateAtual.getTime());	
				dateToParameter = dateFormat;
			}
			
			//String modelDateFrom = java.net.URLDecoder.decode(dateFromParameter, StandardCharsets.UTF_8);
			//String modelDateTo = java.net.URLDecoder.decode(dateToParameter, StandardCharsets.UTF_8);
	
			model.addAttribute(ConstantDataManager.PARAMETER_DATE_FROM, StringUtils.decodeString(dateFromParameter).toUpperCase());
			model.addAttribute(ConstantDataManager.PARAMETER_DATE_TO, StringUtils.decodeString(dateToParameter).toUpperCase());
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
	
	@RequestMapping(value = ConstantDataManager.FILTER_COMPANY_EXECUTION_MOVEMENT_CONTROLLER, method=RequestMethod.POST)
	public ResponseEntity<String> filterCompanyMovementExecution(final HttpServletRequest request, final Model model) throws Exception 
	{
		log.info(ConstantDataManager.COMPANY_MOVEMENT_EXECUTION_CONTROLLER + ConstantDataManager.FILTER_COMPANY_EXECUTION_MOVEMENT_CONTROLLER);
		
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
			
			final CompanyMovementExecutionDTO report = this.processFilter(request, connection);

			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT, report);
			status = true;
		}
		catch (final MovementException ex)
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

	private CompanyMovementExecutionDTO processFilter(final HttpServletRequest request, final Connection connection) throws Exception
	{
		final CompanyMovementExecutionDTO companyMovementExecution = new CompanyMovementExecutionDTO();
		try
		{	
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			final String dateParameter = request.getParameter(ConstantDataManager.PARAMETER_DATE);
			if(StringUtils.isNull(dateParameter))
			{
				//dateParameter = "29/08/2021 00:00 - 04/09/2021 23:59";
				throw new MovementException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_INVALID_DATE, null));								
			}
			
			final String[] companyParameter = request.getParameterValues(ConstantDataManager.PARAMETER_COMPANYS);
		
			final String[] dateArray = dateParameter.split(com.manager.systems.common.utils.ConstantDataManager.TRACO);
				companyMovementExecution.setDateFrom(StringUtils.convertStringDateHoraToLocalDateTime((dateArray[0].trim()+
						com.manager.systems.common.utils.ConstantDataManager.SPACE +
						com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO),StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
				companyMovementExecution.setDateTo(StringUtils.convertStringDateHoraToLocalDateTime((dateArray[1].trim()+
						com.manager.systems.common.utils.ConstantDataManager.SPACE +
						com.manager.systems.common.utils.ConstantDataManager.HOUR_FINAL),StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
			
				if(!StringUtils.isArrayNull(companyParameter)) {
				final StringBuilder companyFilters = new StringBuilder();			
				int count = 0;
				for (final String company : companyParameter) {
					if(count>0)
					{
						companyFilters.append(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);
					}
					companyFilters.append(company);
					count++;
				}
				companyMovementExecution.setCompanyId(companyFilters.toString());
				}
				companyMovementExecution.setUserId(user.getId());
				
				final CompanyMovementExecutionService companyMovementExecutionService = new CompanyMovementExecutionServiceImpl(connection);
				companyMovementExecutionService.getAll(companyMovementExecution);
	
		}
		catch(final Exception ex) 
		{
			throw ex;
		}		
		return companyMovementExecution;
	}
	
	@RequestMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD, method = RequestMethod.POST)
	public ResponseEntity<String> save(final HttpServletRequest request) throws Exception {
		log.info(ConstantDataManager.COMPANY_MOVEMENT_EXECUTION_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD);
		
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();
	
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
	
		Connection connection = null;
		
		try {
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final String fildChange = request.getParameter(ConstantDataManager.PARAMETER_FILD_CHANGE);
			
			String fildId = null;
			String fildValue = null;
			String fildName = null;
			
			final String[] fildChangeArray = fildChange.split(com.manager.systems.web.common.utils.ConstantDataManager.PONTO_VIRGULA_STRING);
			if (fildChangeArray.length == 2) {
				final String[] itemArray = fildChangeArray[0].split(com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE);
				if(itemArray.length == 2) {
					fildName = itemArray[0];
					fildId = itemArray[1];
					fildValue = fildChangeArray[1];
				}
			}
			final CompanyMovementExecutionItemDTO companyMovementExecutionItem = new CompanyMovementExecutionItemDTO();

			if(StringUtils.isNull(fildName)) {
				throw new MovementException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_DATA_REQUIRED_ERROR, null));
			}
			if(StringUtils.isNull(fildId)) {
				throw new MovementException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_DATA_REQUIRED_ERROR, null));
			}		
			if(ConstantDataManager.PARAMETER_MOTIVE.equalsIgnoreCase(fildName)) {
				if(!StringUtils.isLong(fildValue) || Integer.parseInt(fildValue) == 0) {
					throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMPANY_MOTIVE_INVALID, null));
				} 
			}else if(StringUtils.isNull(fildValue)) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_DATA_REQUIRED_ERROR, null));
			}
			
			if(ConstantDataManager.PARAMETER_DOCUMENT_NOTE.equalsIgnoreCase(fildName)) {
				companyMovementExecutionItem.setFildValue(URLDecoder.decode(fildValue, "UTF-8"));
			}else {
				companyMovementExecutionItem.setFildValue(fildValue);

			}
			companyMovementExecutionItem.setFildId(Long.parseLong(fildId));
			companyMovementExecutionItem.setFildName(fildName);
			
			final CompanyMovementExecutionService companyMovementExecutionService = new CompanyMovementExecutionServiceImpl(connection);
			companyMovementExecutionService.updateCompanyMovementExecution(companyMovementExecutionItem);
			
	
			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
		} catch (final MovementException ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} catch (final Exception ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}
	
}
