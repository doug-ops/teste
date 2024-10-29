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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.manager.systems.common.dto.bankAccount.BankAccountStatemenReportDTO;
import com.manager.systems.common.service.bankAccount.BankAccountStatementService;
import com.manager.systems.common.service.bankAccount.impl.BankAccountStatementServiceImpl;
import com.manager.systems.common.service.financialCostCenter.FinancialCostCenterService;
import com.manager.systems.common.service.financialCostCenter.impl.FinancialCostCenterServiceImpl;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.model.admin.User;
import com.manager.systems.web.common.controller.BaseController;
import com.manager.systems.web.movements.exception.MovementException;
import com.manager.systems.web.movements.utils.ConstantDataManager;

@Controller
@RequestMapping(value = ConstantDataManager.BANK_ACCOUNT_STATEMENT_CONTROLLER)
public class BankAccountStatementController extends BaseController {
	
	private static final Log log = LogFactory.getLog(BankAccountStatementController.class);
	
	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD)
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		log.info(ConstantDataManager.BANK_ACCOUNT_STATEMENT_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD);

		
		String result = ConstantDataManager.BANK_ACCOUNT_STATEMENT_CONTROLLER_OPEN_METHOD_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
		Connection connection = null;
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}		
			
			if(!user.hasPermission("MENU_MOVIMENTOS", "MENU_MOVIMENTOS_EXTRATO_CONTAS")) {
				result = ConstantDataManager.RESULT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Extrato de Contas.");
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final FinancialCostCenterService financialCostCenterService = new FinancialCostCenterServiceImpl(connection);
			final List<Combobox> financialCostCenters = financialCostCenterService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_FINANCIAL_COST_CENTERS, financialCostCenters);
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
		log.info(ConstantDataManager.BANK_ACCOUNT_STATEMENT_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD);
		
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
			
			final BankAccountStatemenReportDTO report = this.processFilter(request, connection);
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
	
	private BankAccountStatemenReportDTO processFilter(final HttpServletRequest request, final Connection connection) throws Exception
	{
		final BankAccountStatemenReportDTO report = new BankAccountStatemenReportDTO();

		try
		{	
			final String operationParameter = request.getParameter(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_OPERATION);
			if(!StringUtils.isLong(operationParameter))
			{
				throw new MovementException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_OPERATION_INVALID, null));								
			}
			
			final String dateParameter = request.getParameter(ConstantDataManager.PARAMETER_DATE);
			if(StringUtils.isNull(dateParameter))
			{
				throw new MovementException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_INVALID_DATE, null));								
			}
						
			final String[] bankAccountParameter = request.getParameterValues(ConstantDataManager.PARAMETER_BANK_ACCOUNTS);
			if(bankAccountParameter==null || bankAccountParameter.length==0)
			{
				throw new MovementException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_BANK_ACCOUNT_INVALID, null));								
			}			
			report.setOperation(Integer.parseInt(operationParameter));
			
			final String[] dateArray = dateParameter.split(com.manager.systems.common.utils.ConstantDataManager.TRACO);
			
			report.setDateFrom(StringUtils.convertStringDateHoraToLocalDateTime((dateArray[0].trim()+
					com.manager.systems.common.utils.ConstantDataManager.SPACE +
					com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO),StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));

			report.setDateTo(StringUtils.convertStringDateHoraToLocalDateTime((dateArray[1].trim()+
					com.manager.systems.common.utils.ConstantDataManager.SPACE +
					com.manager.systems.common.utils.ConstantDataManager.HOUR_FINAL),StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
			
			final StringBuilder bankAccountsFilters = new StringBuilder();			
			int count = 0;
			for (final String bankAccount : bankAccountParameter) {
				if(count>0)
				{
					bankAccountsFilters.append(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);
				}
				bankAccountsFilters.append(bankAccount);
				count++;
			}
			report.setBankAccounts(bankAccountsFilters.toString());
			
			final String financialCostCenterParameter = request.getParameter(ConstantDataManager.PARAMETER_FINANCIAL_COST_CENTER);
			if(StringUtils.isLong(financialCostCenterParameter)) {
				report.setFinancialCostCenter(Integer.valueOf(financialCostCenterParameter));				
			}
			
			final BankAccountStatementService service = new BankAccountStatementServiceImpl(connection);
			service.getBankAccountStatement(report);	
		}
		catch(final Exception ex) 
		{
			throw ex;
		}		
		return report;
	}

	@PostMapping(value=ConstantDataManager.BANK_ACCOUNT_STATEMENT_CONTROLLER_REPORT_PDF_METHOD)
	public ResponseEntity<byte[]> reportPdf(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.BANK_ACCOUNT_STATEMENT_CONTROLLER + ConstantDataManager.BANK_ACCOUNT_STATEMENT_CONTROLLER_REPORT_PDF_METHOD);
		
		final HttpHeaders headers = new HttpHeaders();
		byte[] pdfFile = null;
		Connection connection = null;		
		try
		{								
			
			//final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			//if(user==null)
			//{
			//	throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			//}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final BankAccountStatemenReportDTO report = this.processFilter(request, connection);
			final BankAccountStatementService  bankAccountService = new BankAccountStatementServiceImpl(connection);
			pdfFile = bankAccountService.processPdfReport(report);
			
	    	final StringBuilder filename = new StringBuilder();
	    	filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_REPORT);
	    	filename.append(com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE);
	    	filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_LAUNCH);
	    	filename.append(com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE);
	    	filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY);
	    	//filename.append(report.);
	    	filename.append(com.manager.systems.common.utils.ConstantDataManager.PONTO);
	    	filename.append(com.manager.systems.common.utils.ConstantDataManager.EXTENSION_PDF);
	    	
			headers.setContentType(MediaType.parseMediaType(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_TYPE_PDF));
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_ACCESS_CONTROL_ALLOW_ORIGIN, "*");
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_ACCESS_CONTROL_ALLOW_METHODS, com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_GET+com.manager.systems.web.common.utils.ConstantDataManager.VIRGULA+
					    com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_POST+com.manager.systems.web.common.utils.ConstantDataManager.VIRGULA+
					    com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_PUT);
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_ACCESS_CONTROL_ALLOW_HEADERS, com.manager.systems.web.common.utils.ConstantDataManager.CONTENT_TYPE);
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.CONTENT_DISPOSITION, 
					com.manager.systems.web.common.utils.ConstantDataManager.INLINE+com.manager.systems.common.utils.ConstantDataManager.PONTO_VIRGULA_STRING+
					com.manager.systems.web.common.utils.ConstantDataManager.FILENAME + com.manager.systems.common.utils.ConstantDataManager.IGUAL+filename.toString());
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_CACHE_CONTROL,com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_NO_CACHE+com.manager.systems.web.common.utils.ConstantDataManager.VIRGULA+
					com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_NO_STORE+com.manager.systems.web.common.utils.ConstantDataManager.VIRGULA+
					com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_MUST_REVALIDATE);
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_PRAGMA, com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_NO_CACHE);
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_EXPIRES, com.manager.systems.web.common.utils.ConstantDataManager.ZERO_STRING);
			  
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