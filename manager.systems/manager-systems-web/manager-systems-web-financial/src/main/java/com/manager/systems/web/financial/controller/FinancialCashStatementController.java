/*
 * Date create 02/01/2024
 */
package com.manager.systems.web.financial.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.manager.systems.common.service.adm.UserParentService;
import com.manager.systems.common.service.impl.adm.UserParentServiceImpl;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.model.admin.User;
import com.manager.systems.web.common.controller.BaseController;
import com.manager.systems.web.financial.cash.statement.dto.CashStatemenReportDTO;
import com.manager.systems.web.financial.cash.statement.dto.CashStatemenReportFilterDTO;
import com.manager.systems.web.financial.cash.statement.service.CashStatementService;
import com.manager.systems.web.financial.cash.statement.service.impl.CashStatementServiceImpl;
import com.manager.systems.web.financial.exception.FinancialException;
import com.manager.systems.web.financial.utils.ConstantDataManager;

@Controller
@RequestMapping(value=ConstantDataManager.FINANCIAL_CASH_STATEMENT_CONTROLLER)
public class FinancialCashStatementController extends BaseController
{	
	private static final Log log = LogFactory.getLog(FinancialCashFlowController.class);
	
	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD)
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		log.info(ConstantDataManager.FINANCIAL_CASH_STATEMENT_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD);

		
		String result = ConstantDataManager.FINANCIAL_CASH_STATEMENT_OPEN_METHOD_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
		Connection connection = null;
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}	
			
			if(!user.hasPermission("MENU_RELATORIOS", "MENU_RELATORIOS_FLUXO_CAIXA")) {
				result = ConstantDataManager.RESULT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Fluxo de Caixa.");
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
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
		return result;
	}
	
	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD)
	public ResponseEntity<String> filter(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.FINANCIAL_CASH_STATEMENT_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD);
		
		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new Gson();
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
		
		Connection connection = null;
		
		try
		{
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final CashStatemenReportDTO report = this.processFilter(request, connection);
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
		return ResponseEntity.ok(json);
	}
	
	private CashStatemenReportDTO processFilter(final HttpServletRequest request, final Connection connection) throws Exception
	{
		CashStatemenReportDTO report = null;
		
		try
		{	
			///final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			//if(user == null)
			//{
			//	throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			//}
			
			final User user = new User();
			user.setId(8);
			
			final CashStatemenReportFilterDTO filter = CashStatemenReportFilterDTO.builder().build();
			
			final String dateParameter = request.getParameter(ConstantDataManager.PARAMETER_DATE);
			if(StringUtils.isNull(dateParameter))
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_INVALID_DATE, null));								
			}
						
			final String companyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY);
			if(StringUtils.isLong(companyParameter)) {
				filter.setCompanyId(Long.valueOf(companyParameter));
			}
			
			final String[] userChildrensParentParameter = request.getParameterValues(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT_COMBOBOX);
			
			final String[] dateArray = dateParameter.split(com.manager.systems.common.utils.ConstantDataManager.TRACO);
			
			filter.setDateFrom(StringUtils.convertStringDateHoraToLocalDateTime((dateArray[0].trim()+
					com.manager.systems.common.utils.ConstantDataManager.SPACE +
					com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO),StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));

			filter.setDateTo(StringUtils.convertStringDateHoraToLocalDateTime((dateArray[1].trim()+
					com.manager.systems.common.utils.ConstantDataManager.SPACE +
					com.manager.systems.common.utils.ConstantDataManager.HOUR_FINAL),StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
			
			if(userChildrensParentParameter != null && userChildrensParentParameter.length > 0) {
				final StringBuilder userChildrensParentFilters = new StringBuilder();
				int count = 0;
				for (final String userParent : userChildrensParentParameter) {
					if (count > 0) {
						userChildrensParentFilters.append(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);
					}
					userChildrensParentFilters.append(userParent);
					count++;
				}
				filter.setUsersChildrenParent(userChildrensParentFilters.toString());
			} else {
				filter.setUsersChildrenParent(String.valueOf(user.getId()));
			}
			
			final CashStatementService service = new CashStatementServiceImpl(connection);
			report = service.getCashStatement(filter);
		}
		catch(final Exception ex) 
		{
			throw ex;
		}		
		return report;
	}
	
	@GetMapping(value = ConstantDataManager.FINANCIAL_CASH_STATEMENT_REPORT_PDF_METHOD)
	public ResponseEntity<byte[]> filterReport(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.FINANCIAL_CASH_STATEMENT_CONTROLLER + ConstantDataManager.FINANCIAL_CASH_STATEMENT_REPORT_PDF_METHOD);
		
		final HttpHeaders headers = new HttpHeaders();
		byte[] pdfFile = null;

		Connection connection = null;

		try {
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final CashStatementService service = new CashStatementServiceImpl(connection);
			final CashStatemenReportDTO report = processFilter(request, connection);

			report.setReportTitle(ConstantDataManager.TITLE_FINANCIAL_CASH_STATEMENT_GROUPING_REPORT);
			
			final StringBuilder period = new StringBuilder();
			period.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_PERIOD.toUpperCase());
			period.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
			period.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_FROM);   
			period.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
			period.append(StringUtils.formatDate(report.getDateFrom(), StringUtils.DATE_PATTERN_DD_MM_YYYY));
			period.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
			period.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_TO);    	
			period.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
	    	period.append(StringUtils.formatDate(report.getDateTo(), StringUtils.DATE_PATTERN_DD_MM_YYYY));
			report.setReportPeriod(period.toString());
			
			pdfFile = service.processPdfReport(report);		
			
			final StringBuilder filename = new StringBuilder();
			filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_REPORT);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_CASHIER);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_CLOSING);
			
			filename.append(com.manager.systems.common.utils.ConstantDataManager.PONTO);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.EXTENSION_EXCEL_XLSX);

			
			
			headers.setContentType(MediaType
					.parseMediaType(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_TYPE_EXCEL));
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
			if(connection!=null)
			{
				connection.close();
			}
		}
		final ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfFile, headers, HttpStatus.OK);
		return response;
	}
}