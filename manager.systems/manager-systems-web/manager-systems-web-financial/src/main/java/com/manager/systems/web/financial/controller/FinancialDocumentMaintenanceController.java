package com.manager.systems.web.financial.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.manager.systems.common.dto.adm.BankAccountDTO;
import com.manager.systems.common.dto.adm.IntegrationSystemsDTO;
import com.manager.systems.common.dto.adm.ReportBankAccountDTO;
import com.manager.systems.common.service.IntegrationSystemsService;
import com.manager.systems.common.service.adm.BankAccountService;
import com.manager.systems.common.service.impl.IntegrationSystemsServiceImpl;
import com.manager.systems.common.service.impl.adm.BankAccountServiceImpl;
import com.manager.systems.common.service.impl.movement.company.DocumentMovementIdServiceImpl;
import com.manager.systems.common.service.movement.DocumentMovementIdService;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.ObjectType;
import com.manager.systems.model.admin.User;
import com.manager.systems.web.common.controller.BaseController;
import com.manager.systems.web.financial.dto.FinancialDocumentDTO;
import com.manager.systems.web.financial.dto.TransferValueRequest;
import com.manager.systems.web.financial.dto.TransferValueRequestExpense;
import com.manager.systems.web.financial.exception.FinancialException;
import com.manager.systems.web.financial.service.FinancialDocumentService;
import com.manager.systems.web.financial.service.impl.FinancialDocumentServiceImpl;
import com.manager.systems.web.financial.utils.ConstantDataManager;

@Controller
@RequestMapping(value=ConstantDataManager.FINANCIAL_DOCUMENT_CONTROLLER)
public class FinancialDocumentMaintenanceController extends BaseController
{	
	private static final Log log = LogFactory.getLog(FinancialDocumentMaintenanceController.class);
	
	@PostMapping(value=ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD)
	public ResponseEntity<String> save(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.FINANCIAL_DOCUMENT_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD);
		
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
			
			final String companyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY);
			final String providerParameter = request.getParameter(ConstantDataManager.PARAMETER_PROVIDER);
			final String financialGroupParameter = request.getParameter(ConstantDataManager.PARAMETER_FINANCIAL_GROUP);
			final String financialSubGroupParameter = request.getParameter(ConstantDataManager.PARAMETER_FINANCIAL_SUB_GROUP);
			final String movementTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_MOVEMENT_TYPE);
			final String documentStatusParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_STATUS);			
			String documentNoteParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_ENCODED_NOTE);
			final String installmentParameter = request.getParameter(ConstantDataManager.PARAMETER_INSTALLMENT);			
			String documentNumberParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_NUMBER);	
			if(StringUtils.isNull(documentNumberParameter))
			{
				documentNumberParameter = StringUtils.generateDateTimeNumber();	
			}
			String documentValueParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_VALUE);			
			String paymentExpiryDateParameter = request.getParameter(ConstantDataManager.PARAMETER_PAYMENT_EXPIRY_DATE);	
			final String bankAccountParameter = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT);	
			//String paymentValueParameter = request.getParameter(ConstantDataManager.PARAMETER_PAYMENT_VALUE);	
			String financialCostCenterParameterTransf= request.getParameter(ConstantDataManager.PARAMETER_FINANCIAL_COST_CENTER_TRANSFER);	



			if(!StringUtils.isLong(installmentParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_INSTALLMENT_INVALID, null));
			}
			if(!StringUtils.isLong(companyParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMPANY_INVALID, null));								
			}
			if(StringUtils.isNull(financialGroupParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_FINANCIAL_GROUP_INVALID, null));								
			}
			if(StringUtils.isNull(financialSubGroupParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_FINANCIAL_SUB_GROUP_INVALID, null));								
			}
			if(!StringUtils.isLong(movementTypeParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_MOVEMENT_TYPE_INVALID, null));								
			}
			if(!StringUtils.isLong(documentStatusParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DOCUMENT_STATUS_INVALID, null));								
			}			
			if(StringUtils.isNull(documentNumberParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DOCUMENT_NUMBER_INVALID, null));								
			}
			if(StringUtils.isNull(documentValueParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DOCUMENT_VALUE_INVALID, null));								
			}
			if(StringUtils.isNull(paymentExpiryDateParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_EXPIRY_DATE_INVALID, null));								
			}
			if(!StringUtils.isLong(bankAccountParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_BANK_ACCOUNT_INVALID, null));								
			}			
			if(!StringUtils.isNull(documentNoteParameter))
			{
				documentNoteParameter = StringUtils.decodeString(documentNoteParameter);
			}
			else
			{
				documentNoteParameter = com.manager.systems.common.utils.ConstantDataManager.BLANK;
			}
			
			if(StringUtils.isNull(financialCostCenterParameterTransf))
			{
				financialCostCenterParameterTransf = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			
			final List<FinancialDocumentDTO> documents = new ArrayList<FinancialDocumentDTO>();
			
			final int installment = Integer.parseInt(installmentParameter);
			final boolean isInstallment = (installment>1);
			for (int i = 1; i <= installment; i++) 
			{
				final FinancialDocumentDTO document = new FinancialDocumentDTO();
				document.setDocumentId(0L);
				document.setDocumentParentId(0L);
				document.setCompanyId(Long.parseLong(companyParameter));
				document.setProviderId(Long.parseLong(providerParameter));
				document.setFinancialGroupId(financialGroupParameter);
				document.setFinancialSubGroupId(financialSubGroupParameter);
				document.setCredit(1==Integer.parseInt(movementTypeParameter) ? true : false);
				if(document.isCredit()) {
					document.setDocumentType(2);
				} else {
					document.setDocumentType(3);					
				}
				document.setDocumentStatus(Integer.parseInt(documentStatusParameter));
				document.setFinancialCostCenterId(Integer.parseInt(financialCostCenterParameterTransf));				
				
				if(!isInstallment)
				{
					document.setDocumentNumber(documentNumberParameter);
					if(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_ZERO_ZERO.equalsIgnoreCase(documentValueParameter))
					{
						documentValueParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_VIRGULA_ZERO_ZERO;
					}
					documentValueParameter = StringUtils.replaceNonDigits(documentValueParameter);
					final StringBuilder resultMoeda = new StringBuilder(documentValueParameter);
					if(resultMoeda.length()>2)
					{
						resultMoeda.insert(resultMoeda.length() - 2, com.manager.systems.common.utils.ConstantDataManager.PONTO);
					}						
					documentValueParameter = resultMoeda.toString();		
					document.setDocumentValue(Double.parseDouble(documentValueParameter));
					
					final String[] dateArrayPaymentExpiryDate = paymentExpiryDateParameter.split(com.manager.systems.common.utils.ConstantDataManager.BARRA);
					paymentExpiryDateParameter = dateArrayPaymentExpiryDate[2]+com.manager.systems.common.utils.ConstantDataManager.TRACO+dateArrayPaymentExpiryDate[1]+com.manager.systems.common.utils.ConstantDataManager.TRACO+dateArrayPaymentExpiryDate[0]+com.manager.systems.common.utils.ConstantDataManager.SPACE+com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO;
					document.setPaymentExpiryDate(StringUtils.convertStringDateHoraToLocalDateTime(paymentExpiryDateParameter));
					document.setPaymentDate(StringUtils.convertStringDateHoraToLocalDateTime(paymentExpiryDateParameter));
					
					document.setPaymentStatus(Integer.parseInt(documentStatusParameter));
					document.setBankAccountId(Integer.parseInt(bankAccountParameter));
					document.setBankAccountOriginId(Integer.parseInt(bankAccountParameter));
					if(StringUtils.isNull(documentNoteParameter)) {
						if(document.isCredit()) {
							documentNoteParameter = ConstantDataManager.LABEL_LAUNCH_CREDIT;
						} else {
							documentNoteParameter = ConstantDataManager.LABEL_LAUNCH_DEBIT;
						}
						document.setDocumentNote(documentNoteParameter);	
					} else {
						document.setDocumentNote(documentNoteParameter);						
					}
					documents.add(document);					
				}
				else
				{
					final String documentNumber = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_NUMBER+com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE+i);
					if(StringUtils.isNull(documentNumber))
					{
						throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DOCUMENT_NUMBER_INVALID, null) + com.manager.systems.common.utils.ConstantDataManager.SPACE + this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_INSTALLMENT, new String[] {String.valueOf(i)}));								
					}
					document.setDocumentNumber(documentNumber);
					
					String documentValue = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_VALUE+com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE+i);
					if(StringUtils.isNull(documentValueParameter))
					{
						throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DOCUMENT_VALUE_INVALID, null) + com.manager.systems.common.utils.ConstantDataManager.SPACE + this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_INSTALLMENT, new String[] {String.valueOf(i)}));							
					}
					if(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_ZERO_ZERO.equalsIgnoreCase(documentValue))
					{
						documentValue = com.manager.systems.common.utils.ConstantDataManager.ZERO_VIRGULA_ZERO_ZERO;
					}
					documentValue = StringUtils.replaceNonDigits(documentValue);
					final StringBuilder resultMoeda = new StringBuilder(documentValue);
					if(resultMoeda.length()>2)
					{
						resultMoeda.insert(resultMoeda.length() - 2, com.manager.systems.common.utils.ConstantDataManager.PONTO);
					}						
					documentValue = resultMoeda.toString();		
					document.setDocumentValue(Double.parseDouble(documentValue));
					
					String paymentExpiryDate = request.getParameter(ConstantDataManager.PARAMETER_PAYMENT_EXPIRY_DATE+com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE+i);
					if(StringUtils.isNull(paymentExpiryDate))
					{
						throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_EXPIRY_DATE_INVALID, null) + com.manager.systems.common.utils.ConstantDataManager.SPACE + this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_INSTALLMENT, new String[] {String.valueOf(i)}));								
					}
					
					final String[] dateArrayPaymentExpiryDate = paymentExpiryDate.split(com.manager.systems.common.utils.ConstantDataManager.BARRA);
					paymentExpiryDate = dateArrayPaymentExpiryDate[2]+com.manager.systems.common.utils.ConstantDataManager.TRACO+dateArrayPaymentExpiryDate[1]+com.manager.systems.common.utils.ConstantDataManager.TRACO+dateArrayPaymentExpiryDate[0]+com.manager.systems.common.utils.ConstantDataManager.SPACE+com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO;
					document.setPaymentExpiryDate(StringUtils.convertStringDateHoraToLocalDateTime(paymentExpiryDate));
					document.setPaymentDate(StringUtils.convertStringDateHoraToLocalDateTime(paymentExpiryDate));

					document.setPaymentStatus(Integer.parseInt(documentStatusParameter));
					
					final String bankAccount = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT+com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE+i);
					if(!StringUtils.isLong(bankAccount))
					{
						throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_BANK_ACCOUNT_INVALID, null) + com.manager.systems.common.utils.ConstantDataManager.SPACE + this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_INSTALLMENT, new String[] {String.valueOf(i)}));								
					}					
					document.setBankAccountId(Integer.parseInt(bankAccountParameter));
					document.setBankAccountOriginId(Integer.parseInt(bankAccountParameter));
					String documentNoteParameterInstallment = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_NOTE+com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE+i);
					if(!StringUtils.isNull(documentNoteParameterInstallment))
					{
						document.setDocumentNote(documentNoteParameterInstallment + com.manager.systems.common.utils.ConstantDataManager.SPACE + this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_INSTALLMENT, new String[] {String.valueOf(i)}));
					} 
					else 
					{
						document.setDocumentNote(documentNoteParameter + com.manager.systems.common.utils.ConstantDataManager.SPACE + this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_INSTALLMENT, new String[] {String.valueOf(i)}));						
					}
					documents.add(document);
				}
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			connection.setAutoCommit(false);
			final FinancialDocumentService financialDocumentService = new FinancialDocumentServiceImpl(connection);
			for (final FinancialDocumentDTO document : documents) 
			{
				final boolean isSaved = financialDocumentService.saveNewDocument(document);
				if(!isSaved)
				{
					throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));				
				}				
			}
			connection.commit();
			
			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
		}
		catch (final FinancialException ex)
		{
			ex.printStackTrace();
			status = false;
			message =  ex.getMessage();
			if(connection!=null)
			{
				connection.rollback();
			}
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
	
	@PostMapping(value=ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_CHANGE_METHOD)
	public ResponseEntity<String> change(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.FINANCIAL_DOCUMENT_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_CHANGE_METHOD);
		
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
			
			final String documentIdParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_ID);			
			final String documentParentIdParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_PARENT_ID);
			final String documentTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_TYPE);										
			final String companyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY);
			final String providerParameter = request.getParameter(ConstantDataManager.PARAMETER_PROVIDER);
			final String financialGroupParameter = request.getParameter(ConstantDataManager.PARAMETER_FINANCIAL_GROUP);
			final String financialSubGroupParameter = request.getParameter(ConstantDataManager.PARAMETER_FINANCIAL_SUB_GROUP);
			final String movementTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_MOVEMENT_TYPE);
			final String documentStatusParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_STATUS);			
			final String documentNumberParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_NUMBER);
			String documentValueParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_VALUE);
			String paymentDateParameter = request.getParameter(ConstantDataManager.PARAMETER_PAYMENT_DATE);
			String paymentExpiryDateParameter = request.getParameter(ConstantDataManager.PARAMETER_PAYMENT_EXPIRY_DATE);
			final String paymentStatusParameter = request.getParameter(ConstantDataManager.PARAMETER_PAYMENT_STATUS);			
			final String bankAccountParameter = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT);
			String documentNoteParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_ENCODED_NOTE);

			if(!StringUtils.isLong(documentIdParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_FINANCIAL_DOCUMENT_INVALID, null));
			}
			if(!StringUtils.isLong(documentParentIdParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_FINANCIAL_GROUP_DOCUMENT_INVALID, null));				
			}
			if(!StringUtils.isLong(documentTypeParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_FINANCIAL_DOCUMENT_TYPE_INVALID, null));								
			}
			if(!StringUtils.isLong(companyParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMPANY_INVALID, null));								
			}
			if(!StringUtils.isLong(documentTypeParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PROVIDER_INVALID, null));								
			}
			if(StringUtils.isNull(financialGroupParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_FINANCIAL_GROUP_INVALID, null));								
			}
			if(StringUtils.isNull(financialSubGroupParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_FINANCIAL_SUB_GROUP_INVALID, null));								
			}
			if(!StringUtils.isLong(movementTypeParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_MOVEMENT_TYPE_INVALID, null));								
			}
			if(!StringUtils.isLong(documentStatusParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DOCUMENT_STATUS_INVALID, null));								
			}
			if(StringUtils.isNull(documentNumberParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DOCUMENT_NUMBER_INVALID, null));								
			}
			if(StringUtils.isNull(documentValueParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DOCUMENT_VALUE_INVALID, null));								
			}
			if(StringUtils.isNull(paymentDateParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_DATE_INVALID, null));								
			}
			if(StringUtils.isNull(paymentExpiryDateParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_EXPIRY_DATE_INVALID, null));								
			}
			if(!StringUtils.isLong(paymentStatusParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_STATUS_INVALID, null));								
			}
			if(!StringUtils.isLong(bankAccountParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_BANK_ACCOUNT_INVALID, null));								
			}
			if(!StringUtils.isNull(documentNoteParameter))
			{
				documentNoteParameter = StringUtils.decodeString(documentNoteParameter);
			}
			else
			{
				documentNoteParameter = com.manager.systems.common.utils.ConstantDataManager.BLANK;
			}
			
			final FinancialDocumentDTO document = new FinancialDocumentDTO();
			document.setDocumentId(Long.parseLong(documentIdParameter));
			document.setDocumentParentId(Long.parseLong(documentParentIdParameter));
			document.setDocumentType(Integer.parseInt(documentTypeParameter));
			document.setCompanyId(Long.parseLong(companyParameter));
			document.setProviderId(Long.parseLong(providerParameter));
			document.setFinancialGroupId(financialGroupParameter);
			document.setFinancialSubGroupId(financialSubGroupParameter);
			document.setCredit(1==Integer.parseInt(movementTypeParameter) ? true : false);
			document.setDocumentStatus(Integer.parseInt(documentStatusParameter));
			document.setDocumentNumber(documentNumberParameter);
			if(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_ZERO_ZERO.equalsIgnoreCase(documentValueParameter))
			{
				documentValueParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_VIRGULA_ZERO_ZERO;
			}
			documentValueParameter = StringUtils.replaceNonDigits(documentValueParameter);
			final StringBuilder resultMoeda = new StringBuilder(documentValueParameter);
			if(resultMoeda.length()>2)
			{
				resultMoeda.insert(resultMoeda.length() - 2, com.manager.systems.common.utils.ConstantDataManager.PONTO);
			}						
			documentValueParameter = resultMoeda.toString();		
			document.setDocumentValue(Double.parseDouble(documentValueParameter));
			
			final String[] dateArrayPaymentDate = paymentDateParameter.split(com.manager.systems.common.utils.ConstantDataManager.BARRA);
			paymentDateParameter = dateArrayPaymentDate[2]+com.manager.systems.common.utils.ConstantDataManager.TRACO+dateArrayPaymentDate[1]+com.manager.systems.common.utils.ConstantDataManager.TRACO+dateArrayPaymentDate[0]+com.manager.systems.common.utils.ConstantDataManager.SPACE+com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO;
			document.setPaymentDate(StringUtils.convertStringDateHoraToLocalDateTime(paymentDateParameter));
			
			final String[] dateArrayPaymentExpiryDate = paymentExpiryDateParameter.split(com.manager.systems.common.utils.ConstantDataManager.BARRA);
			paymentExpiryDateParameter = dateArrayPaymentExpiryDate[2]+com.manager.systems.common.utils.ConstantDataManager.TRACO+dateArrayPaymentExpiryDate[1]+com.manager.systems.common.utils.ConstantDataManager.TRACO+dateArrayPaymentExpiryDate[0]+com.manager.systems.common.utils.ConstantDataManager.SPACE+com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO;
			document.setPaymentExpiryDate(StringUtils.convertStringDateHoraToLocalDateTime(paymentExpiryDateParameter));
			
			document.setPaymentStatus(Integer.parseInt(paymentStatusParameter));
			document.setBankAccountId(Integer.parseInt(bankAccountParameter));
			document.setDocumentNote(documentNoteParameter);
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			connection.setAutoCommit(false);
			final FinancialDocumentService financialDocumentService = new FinancialDocumentServiceImpl(connection);
			final boolean isSaved = financialDocumentService.change(document);
			if(!isSaved)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));				
			}
			connection.commit();
			
			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
		}
		catch (final FinancialException ex)
		{
			ex.printStackTrace();
			status = false;
			message =  ex.getMessage();
			if(connection!=null)
			{
				connection.rollback();
			}
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
	
	@PostMapping(value=ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_INACTIVE_METHOD)
    public ResponseEntity<String> inactive(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.FINANCIAL_DOCUMENT_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_INACTIVE_METHOD);
		
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
			
			final String documentIdParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_ID);			
			final String documentParentIdParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_PARENT_ID);
			final String documentTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_TYPE);			
			final String inactiveParameter = request.getParameter(ConstantDataManager.PARAMETER_INACTIVE);									
			
			if(!StringUtils.isLong(documentIdParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_FINANCIAL_DOCUMENT_INVALID, null));
			}
			
			if(!StringUtils.isLong(documentParentIdParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_FINANCIAL_GROUP_DOCUMENT_INVALID, null));				
			}
			
			if(!StringUtils.isLong(documentTypeParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_FINANCIAL_DOCUMENT_TYPE_INVALID, null));								
			}
			
			if(StringUtils.isNull(inactiveParameter))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_OPERATION_INVALID, null));								
			}
			
			final FinancialDocumentDTO document = new FinancialDocumentDTO();
			document.setId(Long.parseLong(documentIdParameter));
			document.setDocumentId(Long.parseLong(documentIdParameter));
			document.setDocumentParentId(Long.parseLong(documentParentIdParameter));
			document.setDocumentType(Integer.parseInt(documentTypeParameter));
			document.setInactive(Boolean.parseBoolean(inactiveParameter));
			document.setUserChange(user.getId());	
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			connection.setAutoCommit(false);
			
			
			final FinancialDocumentService financialDocumentService = new FinancialDocumentServiceImpl(connection);	
			final boolean wasInactivated = financialDocumentService.rollbackMovement(document);
			
			if(!wasInactivated)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
			}
			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
			
			connection.commit();
		} 
		catch (final FinancialException ex)
		{
			ex.printStackTrace();
			status = false;
			message =  ex.getMessage();
			
			if(connection!=null)
			{
				connection.rollback();
			}
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
	
	@PostMapping(value=ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_UNGROUP_METHOD)
    public ResponseEntity<String> ungroup(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.FINANCIAL_DOCUMENT_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_UNGROUP_METHOD);
		
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
						
			final String documentMovementIdsParameter = request.getParameter(ConstantDataManager.PARAMETER_IDS);
																																
			if(StringUtils.isNull(documentMovementIdsParameter))
			{	
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_DATA_REQUIRED_ERROR, null));				
			}
			
			final String[] documentMovementIdsParameterArray = documentMovementIdsParameter.split("_");
			final String documentMovementIdParameter = documentMovementIdsParameterArray[0];
			final String parentIdParameter = documentMovementIdsParameterArray[1];
			final String documentTypeParameter = documentMovementIdsParameterArray[2];
												
			final FinancialDocumentDTO document = new FinancialDocumentDTO();
			document.setId(Long.valueOf(documentMovementIdParameter));
			document.setDocumentId(Long.parseLong(documentMovementIdParameter));
			document.setDocumentParentId(Long.parseLong(parentIdParameter));
			document.setDocumentType(Integer.valueOf(documentTypeParameter));
			document.setUserChange(user.getId());	
			final FinancialDocumentService financialDocumentService = new FinancialDocumentServiceImpl(connection);		
			final boolean ungrouped = financialDocumentService.ungroup(document);
			
			if(!ungrouped)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
			}
			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
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
	
	@PostMapping(value=ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_GROUP_PARENT_METHOD)
    public ResponseEntity<String> groupParent(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.FINANCIAL_DOCUMENT_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_GROUP_PARENT_METHOD);
		
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
						
			final String documentParentIdsParameter = request.getParameter(ConstantDataManager.PARAMETER_IDS);
																																
			if(StringUtils.isNull(documentParentIdsParameter))
			{	
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_DATA_REQUIRED_ERROR, null));				
			}
			
			final String[] documentsArray = documentParentIdsParameter.split(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);
			if(documentsArray==null || documentsArray.length<2)
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.FINANCIAL_GROUP_REGROUP_ERROR, null));				
			}
			final FinancialDocumentDTO document = new FinancialDocumentDTO();
			document.setDocumentParentId(Long.parseLong(documentsArray[0]));
			document.setUserChange(user.getId());
			document.setDocumentNote(documentParentIdsParameter);
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final FinancialDocumentService financialDocumentService = new FinancialDocumentServiceImpl(connection);		
			final boolean isGrouped = financialDocumentService.groupParent(document);
			
			if(!isGrouped)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.FINANCIAL_GROUP_DATA_ERROR, null));
			}
			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
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
	
	@PostMapping(value=ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_CHANGE_STATUS_METHOD)
    public ResponseEntity<String> changeStatus(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.FINANCIAL_DOCUMENT_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_CHANGE_STATUS_METHOD);
		
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
										
			final String documentMovementIdsParameter = request.getParameter(ConstantDataManager.PARAMETER_IDS);				
			final String documentStatusParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_STATUS);
									
			if(StringUtils.isNull(documentMovementIdsParameter) || StringUtils.isNull(documentStatusParameter))
			{	
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_DATA_REQUIRED_ERROR, null));				
			}
			
			final String[] documentMovementIdsParameterArray = documentMovementIdsParameter.split("_");
			final String documentMovementIdParameter = documentMovementIdsParameterArray[0];
			final String parentIdParameter = documentMovementIdsParameterArray[1];
			final String documentTypeParameter = documentMovementIdsParameterArray[2];
			
			final FinancialDocumentDTO document = new FinancialDocumentDTO();
			document.setId(Long.valueOf(documentMovementIdParameter));
			document.setDocumentId(Long.parseLong(documentMovementIdParameter));
			document.setDocumentParentId(Long.parseLong(parentIdParameter));
			document.setDocumentType(Integer.valueOf(documentTypeParameter));
			document.setDocumentStatus(Integer.valueOf(documentStatusParameter));
			document.setUserChange(user.getId());	
			final FinancialDocumentService financialDocumentService = new FinancialDocumentServiceImpl(connection);		
			final boolean changed = financialDocumentService.changeStatus(document);
			
			if(!changed)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
			}
			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
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
	
	@PostMapping(value=ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_DETAIL_METHOD)
	public ResponseEntity<String> detail(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.FINANCIAL_DOCUMENT_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_DETAIL_METHOD);
		
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
		
		BankAccountDTO bankAccount = null;
		
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
			bankAccount = new BankAccountDTO();
			bankAccount.setId(bankAccountIdParameter);
						
			final BankAccountService bankAccountService = new BankAccountServiceImpl(connection);	
			bankAccountService.get(bankAccount);
			if(bankAccount.getDescription()==null)
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_NOT_FOUND_BANK_ACCOUNT, null));
			}
			
			final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
			final IntegrationSystemsDTO integrationSystem = new IntegrationSystemsDTO();
			integrationSystem.setObjectId(bankAccount.getId());
			integrationSystem.setObjectType(ObjectType.BANK_ACCOUNT.getType());
			bankAccount.getIntegrationSystemsValues().addAll(integrationSystemsService.getAll(integrationSystem));	
						
			status = true;
			result.put(ConstantDataManager.PARAMETER_BANK_ACCOUNT, bankAccount);
		} 
		catch (final FinancialException ex)
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
	
	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD)
	public ResponseEntity<String> filter(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.FINANCIAL_DOCUMENT_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD);
		
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
			
			final ReportBankAccountDTO reportBankAccount = this.processFilter(request, connection);
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT, reportBankAccount);
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
	
	private ReportBankAccountDTO processFilter(final HttpServletRequest request, final Connection connection) throws Exception
	{
		final ReportBankAccountDTO reportBankAccount = new ReportBankAccountDTO();
		try
		{	
			String bankAccountIdFromParameter = request.getParameter(ConstantDataManager.PARAMETER_ID_FROM);
			if(!StringUtils.isLong(bankAccountIdFromParameter))
			{
				bankAccountIdFromParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			
			String bankAccountIdToParameter = request.getParameter(ConstantDataManager.PARAMETER_ID_TO);
			if(!StringUtils.isLong(bankAccountIdToParameter))
			{
				bankAccountIdToParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
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
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_OBJECT_TYPE_NOT_INFORMED, null));
			}
			
			final ObjectType objectType = ObjectType.valueOfType(objectTypeParameter);
			if(objectType==null)
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_OBJECT_TYPE_INVALID, null));
			}
			
			reportBankAccount.setBankAccountIdFrom(bankAccountIdFromParameter);
			reportBankAccount.setBankAccountIdTo(bankAccountIdToParameter);
			reportBankAccount.setInactive(inactiveParameter);
			reportBankAccount.setDescription(descriptionParameter);
			reportBankAccount.setObjectType(objectType.getType());
			
			final BankAccountService bankAccountService = new BankAccountServiceImpl(connection);
			bankAccountService.getAll(reportBankAccount);
		}
		catch(final Exception ex) 
		{
			throw ex;
		}		
		return reportBankAccount;
	}	
	
	@PostMapping(value=ConstantDataManager.CONTROLLER_SAVE_TRANSFER)
	public ResponseEntity<String> saveTransfer(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.FINANCIAL_DOCUMENT_CONTROLLER + ConstantDataManager.CONTROLLER_SAVE_TRANSFER);
		
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
			
			final String jsonDataRequest =  request.getParameter(ConstantDataManager.PARAMETER_JSON_DATA_REQUEST);
			final TransferValueRequest transferValueRequest = gson.fromJson(jsonDataRequest, TransferValueRequest.class);
			
			if(transferValueRequest.getCompanyTransf() == 0)
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMPANY_INVALID, null));								
			}
			
			if(transferValueRequest.getPaymentValueTransf() == 0)
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DOCUMENT_VALUE_INVALID, null) + com.manager.systems.common.utils.ConstantDataManager.SPACE + this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_INSTALLMENT, null));							
			}
			
			if(StringUtils.isNull(transferValueRequest.getPaymentExpiryDateTransf()))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_EXPIRY_DATE_INVALID, null) + com.manager.systems.common.utils.ConstantDataManager.SPACE + this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_INSTALLMENT, null));								
			}
			
			if(transferValueRequest.getBankAccountTransf() == 0)
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_BANK_ACCOUNT_INVALID, null) + com.manager.systems.common.utils.ConstantDataManager.SPACE + this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_INSTALLMENT, null));								
			}				
			
			String documentIdParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_ID);
			if(!StringUtils.isLong(documentIdParameter))
			{
				documentIdParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;							
			}
			
			String documentParentIdParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_PARENT_ID);
			if(!StringUtils.isLong(documentParentIdParameter))
			{
				documentParentIdParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;							
			}
			
			final List<FinancialDocumentDTO> documents = new ArrayList<FinancialDocumentDTO>();
			
			
			final FinancialDocumentDTO documentOrigin = new FinancialDocumentDTO();
			documentOrigin.setDocumentParentIdOrigin(Long.parseLong(documentParentIdParameter));
			documentOrigin.setDocumentIdOrigin(Long.parseLong(documentIdParameter));
			documentOrigin.setDocumentId(0L);
			documentOrigin.setDocumentParentId(0L);
			
			if(transferValueRequest.getBankAccountTransf() == 199 && transferValueRequest.getBankAccountTransfDest() == 156) {
				documentOrigin.setDocumentType(3);
				
			} else {
				documentOrigin.setDocumentType(transferValueRequest.getLaunchType());
			}
			
			documentOrigin.setCompanyId(transferValueRequest.getCompanyTransf());
			documentOrigin.setCredit(false);
			documentOrigin.setDocumentStatus(transferValueRequest.getDocumentStatusTransf());
			documentOrigin.setDocumentNumber(StringUtils.generateDateTimeNumber());
			
			final String[] dateArrayPaymentExpiryDateTransf = transferValueRequest.getPaymentExpiryDateTransf().split(com.manager.systems.common.utils.ConstantDataManager.BARRA);
			final String paymentExpiryDateTransf = dateArrayPaymentExpiryDateTransf[2]+com.manager.systems.common.utils.ConstantDataManager.TRACO+dateArrayPaymentExpiryDateTransf[1]+com.manager.systems.common.utils.ConstantDataManager.TRACO+dateArrayPaymentExpiryDateTransf[0]+com.manager.systems.common.utils.ConstantDataManager.SPACE+com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO;
			documentOrigin.setPaymentExpiryDate(StringUtils.convertStringDateHoraToLocalDateTime(paymentExpiryDateTransf));
			documentOrigin.setPaymentDate(StringUtils.convertStringDateHoraToLocalDateTime(paymentExpiryDateTransf));
			documentOrigin.setPaymentStatus(transferValueRequest.getDocumentStatusTransf());
			
			final List<FinancialDocumentDTO> documentExpenses = new ArrayList<FinancialDocumentDTO>();
			
			double totalExpenseOrigin = 0;
			//double totalExpenseDest = 0;
			if(transferValueRequest.getExpenses() != null && transferValueRequest.getExpenses().size() > 0) {
				for (final TransferValueRequestExpense expense : transferValueRequest.getExpenses()) 
				{
					if(transferValueRequest.getBankAccountTransf() == 199 && transferValueRequest.getBankAccountTransfDest() == 156) {
						continue;
					}
					final FinancialDocumentDTO documentExpense = new FinancialDocumentDTO();
					documentExpense.setCompanyId(expense.getCompanyId());
					documentExpense.setDocumentParentIdOrigin(Long.parseLong(documentParentIdParameter));
					documentExpense.setDocumentIdOrigin(Long.parseLong(documentIdParameter));
					documentExpense.setDocumentId(0L);
					documentExpense.setDocumentParentId(0L);
					if(expense.getLaunchType() == 1) {
						documentExpense.setDocumentType(3);						
					} else {
						documentExpense.setDocumentType(expense.getLaunchType());		
					}
					documentExpense.setCredit(false);
					documentExpense.setDocumentStatus(expense.getDocumentStatusExpense());
					documentExpense.setProviderId(expense.getProviderExpense());
					documentExpense.setFinancialGroupId(expense.getFinancialGroupExpense());
					documentExpense.setFinancialSubGroupId(expense.getFinancialSubGroupExpense());
					if(expense.getLaunchType() == 2 || expense.getLaunchType() == 3) {
						documentExpense.setBankAccountOriginId(expense.getBankAccountExpense());
						documentExpense.setBankAccountId(transferValueRequest.getBankAccountTransf());
					} else {
						documentExpense.setBankAccountOriginId(transferValueRequest.getBankAccountTransf());
						documentExpense.setBankAccountId(expense.getBankAccountExpense());						
					}
					
					documentExpense.setDocumentNumber(StringUtils.generateDateTimeNumber());
					documentExpense.setDocumentValue(expense.getDocumentValueExpense());
					
					String paymentExpiryDate = expense.getPaymentExpiryDateExpense();
					if(StringUtils.isNull(paymentExpiryDate))
					{
						throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_EXPIRY_DATE_INVALID, null) + com.manager.systems.common.utils.ConstantDataManager.SPACE + this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_INSTALLMENT, new String[] {String.valueOf(expense.getDocumentValueExpense())}));								
					}
						
					final String[] dateArrayPaymentExpiryDate = paymentExpiryDate.split(com.manager.systems.common.utils.ConstantDataManager.BARRA);
					paymentExpiryDate = dateArrayPaymentExpiryDate[2]+com.manager.systems.common.utils.ConstantDataManager.TRACO+dateArrayPaymentExpiryDate[1]+com.manager.systems.common.utils.ConstantDataManager.TRACO+dateArrayPaymentExpiryDate[0]+com.manager.systems.common.utils.ConstantDataManager.SPACE+com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO;
					documentExpense.setPaymentExpiryDate(StringUtils.convertStringDateHoraToLocalDateTime(paymentExpiryDate));
					documentExpense.setPaymentDate(StringUtils.convertStringDateHoraToLocalDateTime(paymentExpiryDate));
					documentExpense.setPaymentStatus(expense.getDocumentStatusExpense());
					
					if(StringUtils.isNull(expense.getDocumentNoteExpense())) {
						expense.setDocumentNoteExpense(
								transferValueRequest.getCompanyDescriptionTransfDest()
								+ com.manager.systems.common.utils.ConstantDataManager.SPACE
					   			+ com.manager.systems.common.utils.ConstantDataManager.LARGER
					   			+ com.manager.systems.common.utils.ConstantDataManager.SPACE
					   			+ expense.getBankAccountDescription());						
					}
					documentExpense.setDocumentNote(expense.getDocumentNoteExpenseDecode());
					
					documentExpenses.add(documentExpense);
					
					if(expense.getLaunchType() == 2 || expense.getLaunchType() == 3) {
						
						final FinancialDocumentDTO documentCredit = new FinancialDocumentDTO();
						documentCredit.setCompanyId(expense.getCompanyId());
						documentCredit.setDocumentParentIdOrigin(Long.parseLong(documentParentIdParameter));
						documentCredit.setDocumentIdOrigin(Long.parseLong(documentIdParameter));								
						documentCredit.setDocumentId(0L);
						documentCredit.setDocumentParentId(0L);						
						documentCredit.setDocumentType(expense.getLaunchType());
						documentCredit.setCredit(true);
						documentCredit.setDocumentStatus(transferValueRequest.getDocumentStatusTransf());
						documentCredit.setDocumentNumber(StringUtils.generateDateTimeNumber());
						documentCredit.setProviderId(expense.getProviderExpense());
						documentCredit.setFinancialGroupId(expense.getFinancialGroupExpense());
						documentCredit.setFinancialSubGroupId(expense.getFinancialSubGroupExpense());
						documentCredit.setBankAccountOriginId(transferValueRequest.getBankAccountTransf());
						documentCredit.setBankAccountId(expense.getBankAccountExpense());
						documentCredit.setDocumentValue(expense.getDocumentValueExpense());
						documentCredit.setPaymentValue(expense.getDocumentValueExpense());
						documentCredit.setPaymentExpiryDate(StringUtils.convertStringDateHoraToLocalDateTime(paymentExpiryDate));
						documentCredit.setPaymentDate(StringUtils.convertStringDateHoraToLocalDateTime(paymentExpiryDate));
						documentCredit.setPaymentStatus(expense.getDocumentStatusExpense());						
						documentCredit.setDocumentNote(documentExpense.getDocumentNote());
						documentCredit.setFinancialCostCenterId(0); 
						documents.add(documentCredit);						
					}
				}
				
				totalExpenseOrigin = transferValueRequest.getExpenses().parallelStream().filter(x -> ((x.getCompanyId() == transferValueRequest.getCompanyTransf() && x.getBankAccountExpense() == transferValueRequest.getBankAccountTransf()) || !(x.getCompanyId() == transferValueRequest.getCompanyTransfDest() && x.getBankAccountExpense() == transferValueRequest.getBankAccountTransfDest()))).mapToDouble(TransferValueRequestExpense::getDocumentValueExpense).sum();
				//totalExpenseDest = transferValueRequest.getExpenses().parallelStream().filter(x -> (x.getCompanyId() == transferValueRequest.getCompanyTransfDest() && x.getBankAccountExpense() == transferValueRequest.getBankAccountTransfDest())).mapToDouble(TransferValueRequestExpense::getDocumentValueExpense).sum();
			}
			
			final BigDecimal bdTotalExpenseOrigin = new BigDecimal(totalExpenseOrigin).setScale(2, RoundingMode.HALF_EVEN);
			totalExpenseOrigin = bdTotalExpenseOrigin.doubleValue();
			
			documentOrigin.setDocumentValue(transferValueRequest.getPaymentValueTransf() - totalExpenseOrigin);
			documentOrigin.setPaymentValue(transferValueRequest.getPaymentValueTransf() - totalExpenseOrigin);
				
			documentOrigin.setBankAccountId(transferValueRequest.getBankAccountTransf());
			documentOrigin.setBankAccountOriginId(transferValueRequest.getBankAccountTransfDest());
			if(!StringUtils.isNull(transferValueRequest.getDocumentNoteTransfDecode())) {
				documentOrigin.setDocumentNote(transferValueRequest.getDocumentNoteTransfDecode());
			} else {
				if(transferValueRequest.getBankAccountTransf() == 199 && transferValueRequest.getBankAccountTransfDest() == 156) {
					documentOrigin.setDocumentNote("DESPESA HAB");	
					if(transferValueRequest.getExpenses() != null && transferValueRequest.getExpenses().size() > 0) {
						documentOrigin.setProviderId(transferValueRequest.getExpenses().get(0).getProviderExpense());
					}					
				} else {
					documentOrigin.setDocumentNote(ConstantDataManager.LABEL_TRASNFER_DEBIT);		
				}
			}
			documentOrigin.setFinancialCostCenterId(0); 
			if(transferValueRequest.getBankAccountTransf() == 199 && transferValueRequest.getBankAccountTransfDest() == 440) {
				//TODO: Adiciona o valor do documento origem na despesa e no credito da conta 605
				final Optional<FinancialDocumentDTO> expenseFromBankAccount605 = documentExpenses.parallelStream().filter(x -> x.getBankAccountOriginId() == 605).findFirst();
				if(expenseFromBankAccount605.isPresent()) {
					expenseFromBankAccount605.get().addSumRestTransferValue(documentOrigin.getDocumentValue());
				}
				
				final Optional<FinancialDocumentDTO> creditFromBankAccount605 = documents.parallelStream().filter(x -> x.getBankAccountId() == 605).findFirst();
				if(creditFromBankAccount605.isPresent()) {
					creditFromBankAccount605.get().addSumRestTransferValue(documentOrigin.getDocumentValue());
				}
			} else {
				documents.add(documentOrigin);				
			}
			
			final FinancialDocumentDTO documentDestiny = new FinancialDocumentDTO();
			documentDestiny.setDocumentParentIdOrigin(Long.parseLong(documentParentIdParameter));
			documentDestiny.setDocumentIdOrigin(Long.parseLong(documentIdParameter));
			documentDestiny.setDocumentId(0L);
			documentDestiny.setDocumentParentId(0L);
			
			documentDestiny.setDocumentType(transferValueRequest.getLaunchType());
			documentDestiny.setCompanyId(transferValueRequest.getCompanyTransfDest());
			documentDestiny.setCredit(true);
			documentDestiny.setDocumentStatus(transferValueRequest.getDocumentStatusTransf());
			documentDestiny.setDocumentNumber(StringUtils.generateDateTimeNumber());
			
			documentDestiny.setPaymentExpiryDate(StringUtils.convertStringDateHoraToLocalDateTime(paymentExpiryDateTransf));
			documentDestiny.setPaymentDate(StringUtils.convertStringDateHoraToLocalDateTime(paymentExpiryDateTransf));
			
			documentDestiny.setPaymentStatus(transferValueRequest.getDocumentStatusTransf());
			
			
			final String bankAccountTransfDest = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_TRANSFER_DEST);
			if(!StringUtils.isLong(bankAccountTransfDest))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_BANK_ACCOUNT_INVALID, null) + com.manager.systems.common.utils.ConstantDataManager.SPACE + this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_INSTALLMENT, null));								
			}					

			documentDestiny.setDocumentValue(transferValueRequest.getPaymentValueTransf() - totalExpenseOrigin);
			documentDestiny.setPaymentValue(transferValueRequest.getPaymentValueTransf() - totalExpenseOrigin);
			documentDestiny.setBankAccountId(transferValueRequest.getBankAccountTransfDest());
			documentDestiny.setBankAccountOriginId(transferValueRequest.getBankAccountTransf());
			if(!StringUtils.isNull(transferValueRequest.getDocumentNoteTransfDestDecode())) {
				documentDestiny.setDocumentNote(transferValueRequest.getDocumentNoteTransfDestDecode());
			} else {
				documentDestiny.setDocumentNote(ConstantDataManager.LABEL_TRASNFER_CREDIT);
			}
			documentDestiny.setFinancialCostCenterId(0); 
			if(transferValueRequest.getBankAccountTransf() == 199 && transferValueRequest.getBankAccountTransfDest() == 440) {
				//TODO: Adiciona o valor do documento destino na despesa e no credito da conta 605
			} else {
				documents.add(documentDestiny);				
			}
				
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			connection.setAutoCommit(false);
			
			final DocumentMovementIdService documentMovementIdService = new DocumentMovementIdServiceImpl(connection);
			final long documentTransactionId = documentMovementIdService.generateDocumentTransactionId();
			
			final FinancialDocumentService financialDocumentService = new FinancialDocumentServiceImpl(connection);
			for (final FinancialDocumentDTO documentTransfDest : documents) 
			{
				documentTransfDest.setDocumentTransactionId(documentTransactionId);
				financialDocumentService.saveTransfer(documentTransfDest);		
			}
			
			for (final FinancialDocumentDTO documentTransfExpense : documentExpenses) 
			{
				documentTransfExpense.setDocumentTransactionId(documentTransactionId);
				financialDocumentService.saveDocumentExpense(documentTransfExpense);			
			}
			
			connection.commit();
			
			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
		}
		catch (final FinancialException ex)
		{
			ex.printStackTrace();
			status = false;
			message =  ex.getMessage();
			if(connection!=null)
			{
				connection.rollback();
			}
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
}