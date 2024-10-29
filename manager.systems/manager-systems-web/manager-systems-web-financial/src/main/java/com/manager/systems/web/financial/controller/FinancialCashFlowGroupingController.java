/*
 * Date create 02/01/2024
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
import com.manager.systems.web.financial.cash.flow.grouping.dto.CashFlowFinancialGroupingFilterDTO;
import com.manager.systems.web.financial.cash.flow.grouping.dto.CashFlowFinancialGroupingReportDTO;
import com.manager.systems.web.financial.cash.flow.grouping.service.CashFlowGroupingService;
import com.manager.systems.web.financial.cash.flow.grouping.service.impl.CashFlowGroupingServiceImpl;
import com.manager.systems.web.financial.utils.ConstantDataManager;

@Controller
@RequestMapping(value=ConstantDataManager.FINANCIAL_CASH_FLOW_GROUPING_CONTROLLER)
public class FinancialCashFlowGroupingController extends BaseController
{	
	private static final Log log = LogFactory.getLog(FinancialCashFlowController.class);
	
	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD)
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		log.info(ConstantDataManager.FINANCIAL_CASH_FLOW_GROUPING_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD);

		
		String result = ConstantDataManager.FINANCIAL_CASH_FLOW_GROUPING_OPEN_METHOD_RESULT;
		
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
	
	@PostMapping(value = ConstantDataManager.FINANCIAL_CASH_FLOW_GROUPING_REPORT_PDF_METHOD)
	public ResponseEntity<byte[]> filterReport(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.FINANCIAL_CASH_FLOW_GROUPING_CONTROLLER + ConstantDataManager.FINANCIAL_CASH_FLOW_GROUPING_REPORT_PDF_METHOD);
		
		final HttpHeaders headers = new HttpHeaders();
		byte[] pdfFile = null;

		Connection connection = null;

		try {

			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user == null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			final CashFlowFinancialGroupingFilterDTO filter = CashFlowFinancialGroupingFilterDTO.builder().build();
			
			final String[] bankAccountsParameter = request.getParameterValues(ConstantDataManager.PARAMETER_BANK_ACCOUNTS);
			final StringBuilder bankAccountsFilters = new StringBuilder();
			if(bankAccountsParameter != null && bankAccountsParameter.length > 0) {				
				int count = 0;
				for (final String bankAccount : bankAccountsParameter) {
					if (count > 0) {
						bankAccountsFilters.append(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);
					}
					bankAccountsFilters.append(bankAccount);
					count++;
				}			
			}
			filter.setBankAccountIds(bankAccountsFilters.toString());
			
			final String[] typeDocumentParameter = request.getParameterValues(ConstantDataManager.PARAMETER_TYPE_DOCUMENT);
			final List<Integer> typeDocumentsFilter = new ArrayList<>();
			if(typeDocumentParameter != null && typeDocumentParameter.length > 0) {				
				for (final String typeDocument : typeDocumentParameter) {					
					typeDocumentsFilter.add(Integer.valueOf(typeDocument));
				}			
			} else {
				typeDocumentsFilter.add(0);
				typeDocumentsFilter.add(1);
				typeDocumentsFilter.add(2);
				typeDocumentsFilter.add(3);
				typeDocumentsFilter.add(4);
			}
			filter.setTypeDocuments(typeDocumentsFilter);
			
			final String analiticParameter = request.getParameter(ConstantDataManager.PARAMETER_ANALITIC);
			filter.setAnalitic(StringUtils.isLong(analiticParameter) ? (Integer.valueOf(analiticParameter) == 1 ? true : false) : false);
									
			final String groupingTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_GROUP_BY);
			filter.setGroupingType(StringUtils.isLong(groupingTypeParameter) ? Integer.valueOf(groupingTypeParameter) : 0);
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
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final CashFlowGroupingService service = new CashFlowGroupingServiceImpl(connection);
			final CashFlowFinancialGroupingReportDTO report = service.getCashFlowGroupingReport(filter);

			report.setTitle(ConstantDataManager.TITLE_FINANCIAL_CASH_FLOW_GROUPING_REPORT);
			report.setAnalitic(filter.isAnalitic());
			report.setTypeDocuments(filter.getTypeDocuments());
			
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