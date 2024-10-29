/*
 * Create Date 28/10/2023
 */
package com.manager.systems.web.financial.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

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

import com.manager.systems.common.service.adm.UserParentService;
import com.manager.systems.common.service.impl.adm.UserParentServiceImpl;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.model.admin.User;
import com.manager.systems.web.common.controller.BaseController;
import com.manager.systems.web.financial.cash.flow.dto.CashFlowFilterDTO;
import com.manager.systems.web.financial.cash.flow.dto.CashFlowReportDTO;
import com.manager.systems.web.financial.cash.flow.service.CashFlowService;
import com.manager.systems.web.financial.cash.flow.service.impl.CashFlowServiceImpl;
import com.manager.systems.web.financial.utils.ConstantDataManager;

@Controller
@RequestMapping(value=ConstantDataManager.FINANCIAL_CASH_FLOW_CONTROLLER)
public class FinancialCashFlowController extends BaseController
{	
	private static final Log log = LogFactory.getLog(FinancialCashFlowController.class);
	
	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD)
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		log.info(ConstantDataManager.FINANCIAL_CASH_FLOW_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD);

		
		String result = ConstantDataManager.FINANCIAL_CASH_FLOW_OPEN_METHOD_RESULT;
		
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
	
	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_REPORT_PDF_METHOD)
	public ResponseEntity<byte[]> filterReport(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.FINANCIAL_CASH_FLOW_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_REPORT_METHOD);
		
		final HttpHeaders headers = new HttpHeaders();
		byte[] pdfFile = null;

		Connection connection = null;

		try {

			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user == null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			final CashFlowFilterDTO filter = CashFlowFilterDTO.builder().build();
			
			String groupParameter = request.getParameter(ConstantDataManager.PARAMETER_GROUP_BY);
			if (!StringUtils.isLong(groupParameter)) {
				groupParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
		
			String analiticParameter = request.getParameter(ConstantDataManager.PARAMETER_ANALITIC);
			if (!StringUtils.isLong(analiticParameter)) {
				analiticParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			
			final String[] incomeExpenseParameter = request.getParameterValues(ConstantDataManager.PARAMETER_INCOME_EXPENSE);
			if (incomeExpenseParameter != null) {
				
				boolean isIncome = false;
				boolean isExpense = false;
				boolean isExpenseHab = false;
				boolean isTansfCashingClose = false;
				
				for(final String item : incomeExpenseParameter) {
					if(1 == Integer.valueOf(item)) {
						isIncome = true;
					} else if(2 == Integer.valueOf(item)) {
						isExpense = true;
					} else if(3 == Integer.valueOf(item)) {
						isExpenseHab = true;
					} else if(4 == Integer.valueOf(item)) {
						isTansfCashingClose = true;
					}
				}
				
				if(isIncome && isExpense) {
					filter.setIncomeExpense(0);
				} if(isIncome && !isExpense) {
					filter.setIncomeExpense(1);
				} if(!isIncome && isExpense) {
					filter.setIncomeExpense(2);
				} if(!isIncome && !isExpense) {
					filter.setIncomeExpense(02);
				} 
				
				filter.setExpenseHaab(isExpenseHab);
				filter.setTransfCashingClose(isTansfCashingClose);
			} else {
				filter.setGroupBy(0);
			}
			
			final String[] bankAccountParameter = request.getParameterValues(ConstantDataManager.PARAMETER_BANK_ACCOUNTS);
			final List<Long> bankAccountsFilters = new ArrayList<>();	
			if(bankAccountParameter != null) {
				for (final String bankAccount : bankAccountParameter) {				
					bankAccountsFilters.add(Long.parseLong(bankAccount));
				}				
			}
			filter.setBankAccounts(bankAccountsFilters);
			
			//final String bankAccountNames = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNTS_NAMES);
						
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
			
			filter.setOperation(1);
			
			final String[] dateArray = dateParameter.split(com.manager.systems.common.utils.ConstantDataManager.TRACO);
			
			filter.setDateFrom(StringUtils.convertStringDateHoraToLocalDateTime((dateArray[0].trim()+
					com.manager.systems.common.utils.ConstantDataManager.SPACE +
					com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO),StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));

			filter.setDateTo(StringUtils.convertStringDateHoraToLocalDateTime((dateArray[1].trim()+
					com.manager.systems.common.utils.ConstantDataManager.SPACE +
					com.manager.systems.common.utils.ConstantDataManager.HOUR_FINAL),StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
			filter.setGroupBy(Integer.valueOf(groupParameter));
			filter.setAnalitc(Integer.parseInt(analiticParameter) == 1 ? true : false);
			
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
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final CashFlowService service = new CashFlowServiceImpl(connection);
			final CashFlowReportDTO report = service.getCashFlowReport(filter);
			//report.calculateTotais();
			report.setTitle(ConstantDataManager.TITLE_FINANCIAL_CASH_FLOW_REPORT);
			
			final StringBuilder period = new StringBuilder();
			period.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_PERIOD.toUpperCase());
			period.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
			period.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_FROM);   
			period.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
			period.append(StringUtils.formatDate(filter.getDateFrom(), StringUtils.DATE_PATTERN_DD_MM_YYYY));
			period.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
			period.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_TO);    	
			period.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
	    	period.append(StringUtils.formatDate(filter.getDateTo(), StringUtils.DATE_PATTERN_DD_MM_YYYY));
			report.setPeriod(period.toString());
			//report.setBankAccountNames(bankAccountNames);
			
			pdfFile = service.processPdfReport(report);		
			
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
			if(connection!=null)
			{
				connection.close();
			}
		}
		final ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfFile, headers, HttpStatus.OK);
		return response;
	}
}
