/*
 * Crete Date 08/10/2022
 */
package com.manager.systems.web.financial.controller;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manager.systems.common.service.adm.UserParentService;
import com.manager.systems.common.service.impl.adm.UserParentServiceImpl;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.model.UserComboboxEnum;
import com.manager.systems.model.admin.User;
import com.manager.systems.web.common.controller.BaseController;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashierClosingPreviewFinishRequest;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserMovementDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserMovementFilterDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserPreviewHeaderDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserWeekMovementCompanyDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserWeekMovementCompanyLaunchDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserWeekMovementDTO;
import com.manager.systems.web.financial.cashier.close.preview.service.CashingClosePreviewService;
import com.manager.systems.web.financial.cashier.close.preview.service.CashingCloseUserMovementService;
import com.manager.systems.web.financial.cashier.close.preview.service.impl.CashingClosePreviewServiceImpl;
import com.manager.systems.web.financial.cashier.close.preview.service.impl.CashingCloseUserMovementServiceImpl;
import com.manager.systems.web.financial.exception.FinancialException;
import com.manager.systems.web.financial.utils.ConstantDataManager;

@Controller
@RequestMapping(value=ConstantDataManager.CASHIER_CLOSING_PREVIEW_FINISH_CONTROLLER)
public class CashierClosingPreviewFinishController extends BaseController
{
	
	private static final Log log = LogFactory.getLog(CashierClosingPreviewFinishController.class);
	
	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD)
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		log.info(ConstantDataManager.CASHIER_CLOSING_PREVIEW_FINISH_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD);
		
		String result = ConstantDataManager.CASHIER_CLOSING_PREVIEW_FINISH_METHOD_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
		Connection connection = null;
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			if(!user.hasPermission("MENU_MOVIMENTOS", "MENU_MOVIMENTOS_FECHAMENTO_CAIXA")) {
				result = ConstantDataManager.RESULT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Fechamento de Caixa.");
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final UserParentService userParentService = new UserParentServiceImpl(connection);
			long userId = user.getId();
			final List<Combobox> userChidrensParent = userParentService.getAllUserParentComboboxByOperation(userId, UserComboboxEnum.CASHING_CLOSING_FINISH.getCode());
			if(!userChidrensParent.parallelStream().filter(f -> f.getKey().equalsIgnoreCase(String.valueOf(user.getId()))).findFirst().isPresent()) {
				userChidrensParent.add(new Combobox(String.valueOf(user.getId()), user.getName()));				
			}
			request.setAttribute(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT, 			
								 userChidrensParent.parallelStream().sorted(Comparator.comparing(Combobox::getValue)).collect(Collectors.toList())
								);
			
			if(user.getClientId() == 1) {
				request.setAttribute(ConstantDataManager.PARAMETER_BANK_ACCOUNT_ORIGIN, 199);				
			} else if(user.getClientId() == 2) {
				request.setAttribute(ConstantDataManager.PARAMETER_BANK_ACCOUNT_ORIGIN, 1565);
			}
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
	
	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD)
	public ResponseEntity<String> filter(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.CASHIER_CLOSING_PREVIEW_FINISH_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD);
		
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
			
			final CashingCloseUserWeekMovementDTO report = this.processFilter(request, connection);
	
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT, report);
			status = true;
		}
		catch (final FinancialException ex)
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
		//System.out.println(json);
		return ResponseEntity.ok(json);
	}
	
	public CashingCloseUserWeekMovementDTO processFilter(final HttpServletRequest request, final Connection connection) throws Exception
	{
		CashingCloseUserWeekMovementDTO report = null;
		try
		{	
			final CashingCloseUserMovementFilterDTO filter = CashingCloseUserMovementFilterDTO.builder().build();
			
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			final String userOperatorParameter = request.getParameter(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT_COMBOBOX);
			if(StringUtils.isLong(userOperatorParameter))
			{
				filter.setUserId(Long.valueOf(userOperatorParameter));
			} 
			else 
			{
				throw new Exception("Operador não informado.");				
			}			
	
			filter.setUserChange(user.getId());
			
			final String cashingCloseParameter = request.getParameter(ConstantDataManager.PARAMETER_CASHING_CLOSE);
			if(StringUtils.isLong(cashingCloseParameter))
			{
				filter.setWeekYear(Integer.valueOf(cashingCloseParameter));
			} 
			else 
			{
				throw new Exception("Caixa não informado.");
			}
			
			final String[] companysIdParameter = request.getParameterValues(ConstantDataManager.PARAMETER_COMPANYS_ID);
			
			final StringBuilder companysIdFilters = new StringBuilder();		
			if(!StringUtils.isNull(companysIdParameter)) {
				int count = 0;
				for (final String companysId : companysIdParameter) {
					if(count>0)
					{
						companysIdFilters.append(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);
					}
					companysIdFilters.append(companysId);
					count++;
				}				
			}
			filter.setCompanysId(companysIdFilters.toString());							
			filter.setUsersChildrenParent(userOperatorParameter);
			
			/**
			final String[] userChildrensParentParameter = request.getParameterValues(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT_COMBOBOX);
			if(userChildrensParentParameter==null || userChildrensParentParameter.length==0)
			{
			}else {
				final StringBuilder userChildrensParentFilters = new StringBuilder();			
				int count = 0;
				for (final String userParent : userChildrensParentParameter) {
					if(count>0)
					{
						userChildrensParentFilters.append(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);
					}
					userChildrensParentFilters.append(userParent);
					count++;
				}
				filter.setUsersChildrenParent(userChildrensParentFilters.toString());
			}
			*/
			
			String cashStatusParameter = request.getParameter(ConstantDataManager.PARAMETER_CASH_STATUS);
			if(!StringUtils.isLong(cashStatusParameter)) {
				cashStatusParameter = com.manager.systems.common.utils.ConstantDataManager.NINE_STRING;
			}
			filter.setStatus(Integer.valueOf(cashStatusParameter));
			
			filter.setClose(true);
						
			final CashingCloseUserMovementService cashingCloseUserMovementService = new CashingCloseUserMovementServiceImpl(connection);
			report = cashingCloseUserMovementService.getCashingCloseUserMovementsByWeek(filter);
			
			if(report != null && report.getCashierClosingId() > 0) {
				final List<CashingCloseUserWeekMovementCompanyLaunchDTO> launchs = cashingCloseUserMovementService.getCashierClosingLaunchs(report.getCashierClosingId(), Long.valueOf(userOperatorParameter));
				report.setLaunchs(launchs);
			}
		}
		catch(final Exception ex) 
		{
			throw ex;
		}		
		return report;
	}
		
	@PostMapping(value=ConstantDataManager.CASHIER_CLOSING_PREVIEW_FINISH_CONTROLLER_SAVE)
	public ResponseEntity<String> saveCashierClosing(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.CASHIER_CLOSING_PREVIEW_FINISH_CONTROLLER + ConstantDataManager.CASHIER_CLOSING_PREVIEW_CONTROLLER_SAVE);
		
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
			
			final String jsonData = request.getParameter(ConstantDataManager.PARAMETER_JSON_DATA);						
			final Type type = new TypeToken<ArrayList<CashingCloseUserWeekMovementCompanyDTO>>(){}.getType();
			final List<CashingCloseUserWeekMovementCompanyDTO> items = gson.fromJson(jsonData, type);
			
			final String jsonDataRequest =  request.getParameter(ConstantDataManager.PARAMETER_JSON_DATA_REQUEST);
			final CashierClosingPreviewFinishRequest cashierClosingRequest = gson.fromJson(jsonDataRequest, CashierClosingPreviewFinishRequest.class);
						
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			connection.setAutoCommit(false);
									
			final CashingCloseUserWeekMovementDTO movement = CashingCloseUserWeekMovementDTO.builder().build();
			movement.setCashierClosingId(items.get(0).getCashierClosingId());
			movement.setWeekYear(items.get(0).getWeekYear());
			movement.setStatus(1);
			movement.setNote("");
			movement.setChangeData(new ChangeData(user.getId()));
			
			final CashingCloseUserMovementService userMovementService = new CashingCloseUserMovementServiceImpl(connection);
			userMovementService.saveMovementHeader(movement);
			
			items.parallelStream().forEach(e -> this.populateChangeDataAndStatus(e, user.getId()));
			userMovementService.saveMovementCompanys(items);
			
			userMovementService.deleteCashierClosingLaunchs(cashierClosingRequest.getCashierClosingId(), cashierClosingRequest.getUserOperator());
			
			for(CashingCloseUserWeekMovementCompanyLaunchDTO launch : cashierClosingRequest.getLaunchs()) {
				if(!StringUtils.isNull(launch.getDocumentNoteExpense())){
					launch.setDocumentNoteExpense(StringUtils.decodeString(launch.getDocumentNoteExpense()));
				}
			}
			
			userMovementService.saveCashierClosingLaunch(cashierClosingRequest.getLaunchs());
						
			if(cashierClosingRequest.isProcess()) {
				cashierClosingRequest.setUserChange(user.getId());
				cashierClosingRequest.setPaymentDate(cashierClosingRequest.getLaunchs().get(0).getPaymentExpiryDateExpense());
				cashierClosingRequest.setLaunchs(null);
				
				final StringBuilder companysId = new StringBuilder();
				int countCompanys = 0;
				for(final CashingCloseUserWeekMovementCompanyDTO company : items) {
					if(countCompanys > 0) {
						companysId.append(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);
					}
					companysId.append(company.getCompanyId());
					countCompanys++;
				}
				cashierClosingRequest.setCompanysIds(companysId.toString());
				
				userMovementService.processCashierClosingLaunch(cashierClosingRequest);
			}
						
			connection.commit();
			
			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			status = false;
			message =  ex.getMessage();
			if(connection!=null)
			{
				connection.rollback();
			}
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
	
	private void populateChangeDataAndStatus(final CashingCloseUserWeekMovementCompanyDTO companyMovement, final long userChange) {
		companyMovement.setStatus(1);
		companyMovement.setChangeData(new ChangeData(userChange));
	}
	
	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_REPORT_METHOD)
	public ResponseEntity<byte[]> filterReport(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.CASHIER_CLOSING_PREVIEW_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_REPORT_METHOD);
		
		final HttpHeaders headers = new HttpHeaders();
		byte[] pdfFile = null;


		try {

			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
		
			String analiticParameter = request.getParameter(ConstantDataManager.PARAMETER_ANALITIC);
			if (!StringUtils.isLong(analiticParameter)) {
				analiticParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			
			final String weekValueParameter = request.getParameter(ConstantDataManager.PARAMETER_WEEK_VALUE);
			final String weekDescriptionParameter = request.getParameter(ConstantDataManager.PARAMETER_WEEK_DESCRIPTION);
			final String userChildrensParentParameter = request.getParameter(ConstantDataManager.PARAMETER_USER_CHILDRENS_PARENT);
			final String companysParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANYS);
			
			final String jsonData = request.getParameter(ConstantDataManager.PARAMETER_JSON_DATA);
			
			final CashingCloseUserPreviewHeaderDTO headerPdf = CashingCloseUserPreviewHeaderDTO.builder().build();
			
			final Gson gson = new Gson();
			final Type type = new TypeToken<ArrayList<CashingCloseUserWeekMovementCompanyDTO>>(){}.getType();
			final List<CashingCloseUserWeekMovementCompanyDTO> items = gson.fromJson(jsonData, type);
			
			final Type typeCombobox = new TypeToken<ArrayList<Combobox>>(){}.getType();
			
			if(!StringUtils.isNull(userChildrensParentParameter)) {
				final List<Combobox> userChildrensParentList = gson.fromJson(userChildrensParentParameter, typeCombobox);
				headerPdf.setUserChildrensParent(userChildrensParentList);
			}
			
			if(!StringUtils.isNull(companysParameter)) {
				final List<Combobox> companysList = gson.fromJson(companysParameter, typeCombobox);
				headerPdf.setCompanys(companysList);
			}
			
			if(StringUtils.isLong(weekValueParameter)) {
				headerPdf.setWeekYear(Integer.valueOf(weekValueParameter));				
			}
			
			headerPdf.setWeekYearDescription(weekDescriptionParameter);		
			
			final CashingClosePreviewService cashierClosingService = new CashingClosePreviewServiceImpl(null);
			
			if(Integer.parseInt(analiticParameter) == 0) {
				pdfFile = cashierClosingService.generatePDFCashierClosingAnalitic(headerPdf, items);
			}else {
				pdfFile = cashierClosingService.generatePDFCashierClosingSintetic(headerPdf, items);
			}			
			
			final StringBuilder filename = new StringBuilder();
			filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_REPORT);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_CASHIER);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_CLOSING);
			
			filename.append(com.manager.systems.common.utils.ConstantDataManager.PONTO);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.EXTENSION_PDF);

			headers.setContentType(MediaType
					.parseMediaType(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_TYPE_PDF));
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_ACCESS_CONTROL_ALLOW_ORIGIN,
					"*");
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_ACCESS_CONTROL_ALLOW_METHODS,
					com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_GET
							+ com.manager.systems.web.common.utils.ConstantDataManager.VIRGULA
							+ com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_POST
							+ com.manager.systems.web.common.utils.ConstantDataManager.VIRGULA
							+ com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_PUT);
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_ACCESS_CONTROL_ALLOW_HEADERS,
					com.manager.systems.web.common.utils.ConstantDataManager.CONTENT_TYPE);
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.CONTENT_DISPOSITION,
					com.manager.systems.web.common.utils.ConstantDataManager.INLINE
							+ com.manager.systems.common.utils.ConstantDataManager.PONTO_VIRGULA_STRING
							+ com.manager.systems.web.common.utils.ConstantDataManager.FILENAME
							+ com.manager.systems.common.utils.ConstantDataManager.IGUAL + filename.toString());
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_CACHE_CONTROL,
					com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_NO_CACHE
							+ com.manager.systems.web.common.utils.ConstantDataManager.VIRGULA
							+ com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_NO_STORE
							+ com.manager.systems.web.common.utils.ConstantDataManager.VIRGULA
							+ com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_MUST_REVALIDATE);
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_PRAGMA,
					com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_NO_CACHE);
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_EXPIRES,
					com.manager.systems.web.common.utils.ConstantDataManager.ZERO_STRING);

			headers.setContentLength(pdfFile.length);


		} 
		catch (final Exception ex) 
		{
			ex.printStackTrace();
		} 
		finally 
		{
			
		}
		final ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfFile, headers, HttpStatus.OK);
		return response;
	}
	
	@PostMapping(value = ConstantDataManager.CASHIER_CLOSING_PREVIEW_FINISH_CONTROLLER_FILTER_LAUNCH_CASH)
	public ResponseEntity<String> filterCasingClosingLaunchByUser(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.CASHIER_CLOSING_PREVIEW_FINISH_CONTROLLER + ConstantDataManager.CASHIER_CLOSING_PREVIEW_FINISH_CONTROLLER_FILTER_LAUNCH_CASH);
		
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

			final StringBuilder userChildrensParentFilters = new StringBuilder();			
			final String[] userChildrensParentParameter = request.getParameterValues(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT_COMBOBOX);
			if(userChildrensParentParameter != null && userChildrensParentParameter.length > 0)
			{
				int count = 0;
				for (final String userParent : userChildrensParentParameter) {
					if(count>0)
					{
						userChildrensParentFilters.append(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);
					}
					userChildrensParentFilters.append(userParent);
					count++;
				}				
			} else {
				userChildrensParentFilters.append(user.getId());
			}
			
			final CashingCloseUserMovementService cashingCloseUserMovementService = new CashingCloseUserMovementServiceImpl(connection);
			final List<CashingCloseUserMovementDTO> cashingCloseUserMovements  = cashingCloseUserMovementService.getCashingCloseUserMovements(CashingCloseUserMovementFilterDTO.builder().operation(1).userId(Long.valueOf(userChildrensParentFilters.toString())).status(2).build());

			result.put(ConstantDataManager.PARAMETER_CASH_MOVEMENTS, cashingCloseUserMovements);
			status = true;
		}
		catch (final FinancialException ex)
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
		//System.out.println(json);
		return ResponseEntity.ok(json);
	}
}