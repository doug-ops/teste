/**Create Date 08/09/2022*/
package com.manager.systems.web.movements.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.manager.systems.common.dto.adm.ReportBankAccountDTO;
import com.manager.systems.common.service.adm.BankAccountService;
import com.manager.systems.common.service.adm.FinancialGroupService;
import com.manager.systems.common.service.adm.UserParentService;
import com.manager.systems.common.service.impl.adm.BankAccountServiceImpl;
import com.manager.systems.common.service.impl.adm.FinancialGroupServiceImpl;
import com.manager.systems.common.service.impl.adm.UserParentServiceImpl;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.model.admin.User;
import com.manager.systems.web.common.controller.BaseController;
import com.manager.systems.web.movements.dto.dre.DreReportDTO;
import com.manager.systems.web.movements.dto.dre.DreReportFilterDTO;
import com.manager.systems.web.movements.exception.MovementException;
import com.manager.systems.web.movements.service.DreService;
import com.manager.systems.web.movements.service.impl.DreServiceImpl;
import com.manager.systems.web.movements.utils.ConstantDataManager;

@Controller
@RequestMapping(value=ConstantDataManager.DRE_CONTROLLER)
public class DreController extends BaseController
{
	private static final Log log = LogFactory.getLog(DreController.class);
	
	@RequestMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD, method = {RequestMethod.GET, RequestMethod.POST})
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		log.info(ConstantDataManager.DRE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD);
		
		String result = ConstantDataManager.DRE_OPEN_METHOD_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
		Connection connection = null;
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			if(!user.hasPermission("MENU_MOVIMENTOS", "MENU_MOVIMENTOS_DRE")) {
				result = ConstantDataManager.RESULT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu DRE.");
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final BankAccountService bankAccountService = new BankAccountServiceImpl(connection);	
			
			final ReportBankAccountDTO reportBankAccount = new ReportBankAccountDTO();
			reportBankAccount.setUserOperation(user.getId());
			final List<Combobox> bankAccounts = bankAccountService.getAllCombobox(reportBankAccount);
			request.setAttribute(ConstantDataManager.PARAMETER_BANK_ACCOUNTS, bankAccounts);
			
			final FinancialGroupService financialGroupService = new FinancialGroupServiceImpl(connection);
			final List<Combobox> financialGroups = financialGroupService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_FINANCIAL_GROUPS, financialGroups);
			
			final UserParentService userParentService = new UserParentServiceImpl(connection);
			final List<Combobox> userChildrensParent = userParentService.getAllParentCombobox(user.getId());

			request.setAttribute(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT, userChildrensParent);
			
			request.setAttribute(ConstantDataManager.PARAMETER_OBJECT_TYPE_ALIAS, com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_DOCUMENT_MOVEMENT);
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
	
	@RequestMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD, method=RequestMethod.POST)
	public ResponseEntity<String> filter(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.DRE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD);
		
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
			
			final DreReportDTO report = this.processFilter(request, connection);
			//report.calculateSaldo();
			
			
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
	
	private DreReportDTO processFilter(final HttpServletRequest request, final Connection connection) throws Exception
	{
		final DreReportFilterDTO dre = new DreReportFilterDTO();
		final DreReportDTO dreReport;
		
		try
		{	
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			dre.setUserOperation(user.getId());

			final String companyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_ID);
			if(StringUtils.isLong(companyParameter))
			{
				dre.setCompanyId(Long.valueOf(companyParameter));
			}
			final String financialGroupParameter = request.getParameter(ConstantDataManager.PARAMETER_FINANCIAL_GROUP_ID);
			if(!StringUtils.isNull(financialGroupParameter))
			{
				dre.setFinancialGroupId(financialGroupParameter);
			}
			final String financialSubGroupParameter = request.getParameter(ConstantDataManager.PARAMETER_FINANCIAL_SUB_GROUP_ID);
			if(!StringUtils.isNull(financialSubGroupParameter))
			{
				dre.setFinancialSubGroupId(financialSubGroupParameter);
			}
			final String bankAccountParameter = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_ID);
			if(StringUtils.isLong(bankAccountParameter))
			{
				dre.setBankAccountId(Integer.valueOf(bankAccountParameter));				
			}		
			final String documentTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_TYPE);
			if (!StringUtils.isNull(documentTypeParameter)) {
				dre.setDocumentType(Integer.valueOf(documentTypeParameter));
			}
			
			final String filterDateParameter = request.getParameter(ConstantDataManager.PARAMETER_FILTER_DATE);
			if(!StringUtils.isNull(filterDateParameter))
			{
				dre.setFilterBy(Integer.valueOf(filterDateParameter));
			}
			final String dateParameter = request.getParameter(ConstantDataManager.PARAMETER_DATE);

			final String[] dateArray = dateParameter.split(com.manager.systems.common.utils.ConstantDataManager.TRACO);
			dre.setDateFrom(StringUtils.convertStringDateHoraToLocalDateTime((dateArray[0].trim()+com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS+com.manager.systems.common.utils.ConstantDataManager.ZERO_ZERO),StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
			dre.setDateTo(StringUtils.convertStringDateHoraToLocalDateTime((dateArray[1].trim()+com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS+com.manager.systems.common.utils.ConstantDataManager.FIVE_NINE),StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
			final String userIdParameter = request.getParameter(ConstantDataManager.PARAMETER_USER_ID);
			if(StringUtils.isLong(userIdParameter))
			{
				dre.setUserId(Long.valueOf(userIdParameter));
			}			
						
			final DreService dreService = new DreServiceImpl(connection);
			dreReport = dreService.getAllDre(dre);
		}
		catch(final Exception ex) 
		{
			throw ex;
		}		
		return dreReport;
	}
	
	@RequestMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_REPORT_METHOD, method = RequestMethod.POST)
	public ResponseEntity<byte[]> filterReport(final HttpServletRequest request, final Long documentParentId, final Long companyId) throws Exception {
		
		log.info(ConstantDataManager.DRE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_REPORT_METHOD);
		
		final HttpHeaders headers = new HttpHeaders();
		byte[] pdfFile = null;

		Connection connection = null;

		try {

			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
		
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			DreReportDTO dreReport = new DreReportDTO();
			final DreReportDTO report = this.processFilter(request, connection);

			String analiticParameter = request.getParameter(ConstantDataManager.PARAMETER_ANALITIC);
						
			final DreService dreService = new DreServiceImpl(connection);
			
			if(Integer.parseInt(analiticParameter) == 0 ? true : false) {
				pdfFile = dreService.generatePDFDre(report);
				dreReport.setAnalitic(true);
				//report.getDocumentNumber();
			}
			else if(Integer.parseInt(analiticParameter) == 1 ? false : true) {
				//pdfFile = documentMovementsReportService.generatePDFMovementDocumentsGroupCompany(report.convetGroupCompany());
				dreReport.setAnalitic(false);
			}
			
			final StringBuilder filename = new StringBuilder();
			filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_REPORT);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_LAUNCH);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY);
			//filename.append(report.getCompanyId());
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
		} catch (final Exception ex) {
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