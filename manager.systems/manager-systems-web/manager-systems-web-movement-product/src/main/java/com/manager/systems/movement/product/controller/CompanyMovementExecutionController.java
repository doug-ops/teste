package com.manager.systems.movement.product.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
import com.manager.systems.common.dto.adm.CompanyDTO;
import com.manager.systems.common.dto.movement.company.CompanyMovementExecutionDTO;
import com.manager.systems.common.dto.movement.company.CompanyMovementExecutionItemDTO;
import com.manager.systems.common.service.adm.UserCompanyService;
import com.manager.systems.common.service.impl.adm.UserCompanyServiceImpl;
import com.manager.systems.common.service.impl.movement.company.CompanyMovementExecutionServiceImpl;
import com.manager.systems.common.service.movement.company.CompanyMovementExecutionService;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.model.admin.User;
import com.manager.systems.movement.product.utils.ConstantDataManager;
import com.manager.systems.web.common.controller.BaseController;

@Controller
@RequestMapping(value=ConstantDataManager.COMPANY_MOVEMENT_EXECUTION_CONTROLLER)
public class CompanyMovementExecutionController extends BaseController
{
	private static final Log log = LogFactory.getLog(CompanyMovementExecutionController.class);
	
	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD)
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		log.info(ConstantDataManager.COMPANY_MOVEMENT_EXECUTION_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD);
		
		String result = ConstantDataManager.COMPANY_MOVEMEN_EXECUTION_CONTROLLER_OPEN_METHOD_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
		Connection connection = null;
		
		try
		{
			final String selectedCompany = request.getParameter(ConstantDataManager.SELECTED_COMPANY);
			final String expirationDate = request.getParameter(ConstantDataManager.EXPIRATION_DATE); 
			final String bankAccountOriginId = request.getParameter(ConstantDataManager.BANK_ACCOUNT_ORIGIN_ID);
			final String bankAccountDestinyId = request.getParameter(ConstantDataManager.BANK_ACCOUNT_DESTINY_ID); 
			final String weekYear = request.getParameter(ConstantDataManager.WEEK_YEAR); 
			
			final User user = (User) request.getSession()
					.getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			final UserCompanyService userCompanyService = new UserCompanyServiceImpl(connection);
			final List<CompanyDTO> companysResult = userCompanyService.getDataMovementExecutionCompany(String.valueOf(user.getId()));
			
			final List<CompanyDTO> companys = companysResult.parallelStream().filter(x -> !x.isProcessMovementAutomatic()).collect(Collectors.toList());
						
			request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_COMPANYS, companys);

			model.addAttribute(ConstantDataManager.SELECTED_COMPANY, selectedCompany);
			model.addAttribute(ConstantDataManager.EXPIRATION_DATE, StringUtils.decodeString(expirationDate).toUpperCase());
			model.addAttribute(ConstantDataManager.BANK_ACCOUNT_ORIGIN_ID, bankAccountOriginId);
			model.addAttribute(ConstantDataManager.BANK_ACCOUNT_DESTINY_ID, bankAccountDestinyId);
			model.addAttribute(ConstantDataManager.WEEK_YEAR, weekYear);

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
	
	@RequestMapping(value = ConstantDataManager.FILTER_COMPANY_MOVEMENT_EXECUTION_CONTROLLER, method=RequestMethod.POST)
	public ResponseEntity<String> filterCompanyMovementExecution(final HttpServletRequest request, final Model model) throws Exception 
	{
		log.info(ConstantDataManager.COMPANY_MOVEMENT_EXECUTION_CONTROLLER + ConstantDataManager.FILTER_COMPANY_MOVEMENT_EXECUTION_CONTROLLER);
		
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
			
			final String companyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANYS);
			if(StringUtils.isNull(companyParameter))
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_DATA_REQUIRED_ERROR, null));								
			}
			
		    String expirationDateParameter = request.getParameter(ConstantDataManager.EXPIRATION_DATE);
			if(StringUtils.isNull(expirationDateParameter))
			{
				expirationDateParameter = com.manager.systems.common.utils.ConstantDataManager.BLANK;								
			}
			
			final String bankAccountOriginIdParameter = request.getParameter(ConstantDataManager.BANK_ACCOUNT_ORIGIN_ID);
			if(StringUtils.isNull(bankAccountOriginIdParameter))
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_DATA_REQUIRED_ERROR, null));								
			}
			
			final String bankAccountDestinyIdParameter = request.getParameter(ConstantDataManager.BANK_ACCOUNT_DESTINY_ID);
			if(StringUtils.isNull(bankAccountDestinyIdParameter))
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_DATA_REQUIRED_ERROR, null));								
			}
			
			String weekYearParameter = request.getParameter(ConstantDataManager.WEEK_YEAR);
			if(StringUtils.isNull(weekYearParameter))
			{
				weekYearParameter = com.manager.systems.common.utils.ConstantDataManager.BLANK;	
			}
			
			companyMovementExecution.setUserId(user.getId());
			companyMovementExecution.setCompanyId(companyParameter);
			if(com.manager.systems.common.utils.ConstantDataManager.BLANK == expirationDateParameter) {
				companyMovementExecution.setExpirationDate(null);

			}else {
				companyMovementExecution.setExpirationDate(StringUtils.convertStringDateToDate(expirationDateParameter, StringUtils.DATE_FORMAT_DD_MM_YYYY));
			}
			companyMovementExecution.setBankAccountOriginId(Integer.valueOf(bankAccountOriginIdParameter));
			companyMovementExecution.setBankAccountDestinyId(Integer.valueOf(bankAccountDestinyIdParameter));
			companyMovementExecution.setWeekYear(Integer.valueOf(weekYearParameter));

			
				
			final CompanyMovementExecutionService companyMovementExecutionService = new CompanyMovementExecutionServiceImpl(connection);
			companyMovementExecutionService.get(companyMovementExecution);
			
			
		}
		catch(final Exception ex) 
		{
			throw ex;
		}		
		return companyMovementExecution;
	}
	
	@RequestMapping(value = ConstantDataManager.COMPANY_MOVEMENT_EXECUTION_CONTROLLER_SAVE, method = RequestMethod.POST)
	public ResponseEntity<String> save(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.COMPANY_MOVEMENT_EXECUTION_CONTROLLER + ConstantDataManager.COMPANY_MOVEMENT_EXECUTION_CONTROLLER_SAVE);
		
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();
	
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
	
		Connection connection = null;
		
		try {
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
	
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
			
			if(StringUtils.isNull(fildName)) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_DATA_REQUIRED_ERROR, null));
			}
			if(StringUtils.isNull(fildId)) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_DATA_REQUIRED_ERROR, null));
			}
			if(ConstantDataManager.PARAMETER_MOTIVE.equalsIgnoreCase(fildName)) {
				if(!StringUtils.isLong(fildValue) || Integer.parseInt(fildValue) == 0) {
					throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMPANY_MOTIVE_INVALID, null));
				} 
			}else if(StringUtils.isNull(fildValue)) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_DATA_REQUIRED_ERROR, null));
			}
					
			final CompanyMovementExecutionItemDTO companyMovementExecutionItem = new CompanyMovementExecutionItemDTO();
			companyMovementExecutionItem.setFildId(Long.parseLong(fildId));
			companyMovementExecutionItem.setFildName(fildName);
			companyMovementExecutionItem.setFildValue(fildValue);
			
			final CompanyMovementExecutionService companyMovementExecutionService = new CompanyMovementExecutionServiceImpl(connection);
			companyMovementExecutionService.updateCompanyMovementExecution(companyMovementExecutionItem);
			
	
			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
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