/*
 * Crete Date 08/10/2022
 */
package com.manager.systems.web.financial.controller;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.OptionalLong;
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
import com.google.gson.reflect.TypeToken;
import com.manager.systems.common.dto.movement.document.DocumentMovementIdDTO;
import com.manager.systems.common.service.adm.UserParentService;
import com.manager.systems.common.service.impl.adm.UserParentServiceImpl;
import com.manager.systems.common.service.impl.movement.company.DocumentMovementIdServiceImpl;
import com.manager.systems.common.service.movement.DocumentMovementIdService;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.model.admin.User;
import com.manager.systems.web.common.controller.BaseController;
import com.manager.systems.web.financial.dto.CashierClosingDTO;
import com.manager.systems.web.financial.dto.CashierClosingItemDTO;
import com.manager.systems.web.financial.dto.CashierClosingRequest;
import com.manager.systems.web.financial.dto.CashierClosingRequestExpense;
import com.manager.systems.web.financial.dto.CashierClosingRequestStoreDTO;
import com.manager.systems.web.financial.dto.FinancialDocumentDTO;
import com.manager.systems.web.financial.exception.FinancialException;
import com.manager.systems.web.financial.service.CashierClosingService;
import com.manager.systems.web.financial.service.impl.CashierClosingServiceImpl;
import com.manager.systems.web.financial.utils.ConstantDataManager;

@Controller
@RequestMapping(value=ConstantDataManager.CASHIER_CLOSING_CONTROLLER)
public class CashierClosingController extends BaseController
{
	
	private static final Log log = LogFactory.getLog(CashierClosingController.class);
	
	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD)
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		log.info(ConstantDataManager.CASHIER_CLOSING_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD);
		
		String result = ConstantDataManager.CASHIER_CLOSING_CONTROLLER_METHOD_RESULT;
		
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
			final List<Combobox> userChidrensParent = userParentService.getAllParentCombobox(userId);
			request.setAttribute(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT, userChidrensParent);
			
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
	
	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD)
	public ResponseEntity<String> filter(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.CASHIER_CLOSING_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD);
		
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
			
			final CashierClosingDTO report = this.processFilter(request, connection);
			report.calculateSaldo();
			
			for(final Map.Entry<Long, CashierClosingItemDTO> entry : report.getItens().entrySet()) {
				entry.getValue().processGroupMovements();
			}
			
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
//		System.out.println(json);
		return ResponseEntity.ok(json);
	}
	
	//TODO remover os filter que nao vai ser usado
	public CashierClosingDTO processFilter(final HttpServletRequest request, final Connection connection) throws Exception
	{
		final CashierClosingDTO cashierClosing = new CashierClosingDTO();
		try
		{	
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			cashierClosing.setUserOperation(user.getId());

			final String providerParameter = request.getParameter(ConstantDataManager.PARAMETER_PROVIDER_ID);
			if(StringUtils.isLong(providerParameter))
			{
				cashierClosing.setProviderId(Long.valueOf(providerParameter));
			}
			final String financialGroupParameter = request.getParameter(ConstantDataManager.PARAMETER_FINANCIAL_GROUP_ID);
			if(!StringUtils.isNull(financialGroupParameter))
			{
				cashierClosing.setFinancialGroupId(financialGroupParameter);
			}
			final String financialSubGroupParameter = request.getParameter(ConstantDataManager.PARAMETER_FINANCIAL_SUB_GROUP_ID);
			if(!StringUtils.isNull(financialSubGroupParameter))
			{
				cashierClosing.setFinancialSubGroupId(financialSubGroupParameter);
			}
			final String bankAccountParameter = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_ID);
			if(StringUtils.isLong(bankAccountParameter))
			{
				cashierClosing.setBankAccountId(Integer.valueOf(bankAccountParameter));				
			}		
			final String documentTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_TYPE);
			cashierClosing.setDocumentType(documentTypeParameter);
			final String documentNumberParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_NUMBER);
			cashierClosing.setDocumentNumber(documentNumberParameter);
			final String filterDateParameter = request.getParameter(ConstantDataManager.PARAMETER_FILTER_DATE);
			cashierClosing.setFilterBy(Integer.valueOf(filterDateParameter));
			final String dateParameter = request.getParameter(ConstantDataManager.PARAMETER_DATE);

			final String[] dateArray = dateParameter.split(com.manager.systems.common.utils.ConstantDataManager.TRACO);
			cashierClosing.setDateFrom(StringUtils.convertStringDateHoraToLocalDateTime((dateArray[0].trim()+com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS+com.manager.systems.common.utils.ConstantDataManager.ZERO_ZERO),StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
			cashierClosing.setDateTo(StringUtils.convertStringDateHoraToLocalDateTime((dateArray[1].trim()+com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS+com.manager.systems.common.utils.ConstantDataManager.FIVE_NINE),StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
			
			final String[] movementTypeParameter = request.getParameterValues(ConstantDataManager.PARAMETER_MOVEMENT_TYPE_ID);
			if(movementTypeParameter!=null && movementTypeParameter.length>0)
			{
				final String movements = Arrays.toString(movementTypeParameter);
				if(movements.indexOf(ConstantDataManager.PARAMETER_CREDIT_ALIAS)>-1)
				{
					cashierClosing.setCredit(true);
				}
				if(movements.indexOf(ConstantDataManager.PARAMETER_DEBIT_ALIAS)>-1)
				{
					cashierClosing.setDebit(true);
				}
				if(movements.indexOf(ConstantDataManager.PARAMETER_OPEN_ALIAS)>-1)
				{
					cashierClosing.setOpen(true);
				}
				if(movements.indexOf(ConstantDataManager.PARAMETER_CLOSE_ALIAS)>-1)
				{
					cashierClosing.setClose(true);
				}
				if(movements.indexOf(ConstantDataManager.PARAMETER_REMOVE_ALIAS)>-1)
				{
					cashierClosing.setRemoved(true);
				}
				if(movements.indexOf(ConstantDataManager.PARAMETER_TRANSFER)>-1)
				{
					cashierClosing.setTransfer(true);
				}	
				if(movements.indexOf(ConstantDataManager.PARAMETER_TRANSFER_HAB)>-1)
				{
					cashierClosing.setTransferHab(true);
				}
				if(movements.indexOf(ConstantDataManager.PARAMETER_CLOSING_ALIAS)>-1)
				{
					cashierClosing.setClosing(true);
				}
				
			}
			final String productGroupParameter = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_GROUP_ID);
			if(StringUtils.isLong(productGroupParameter))
			{
				cashierClosing.setProductGroupId(Integer.valueOf(productGroupParameter));
			}
			final String productSubGroupParameter = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_SUB_GROUP_ID);
			if(StringUtils.isLong(productSubGroupParameter))
			{
				cashierClosing.setProductSubGroupId(Integer.valueOf(productSubGroupParameter));
			}
			
			final String[] companysIdParameter = request.getParameterValues(ConstantDataManager.PARAMETER_COMPANYS_ID);
			
			final StringBuilder CompanysIdFilters = new StringBuilder();			
			int countt = 0;
			for (final String companysId : companysIdParameter) {
				if(countt>0)
				{
					CompanysIdFilters.append(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);
				}
				CompanysIdFilters.append(companysId);
				countt++;
			}
			cashierClosing.setCompanysId(CompanysIdFilters.toString());
			
			final String[] userChildrensParentParameter = request.getParameterValues(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT_COMBOBOX);
			if(userChildrensParentParameter==null || userChildrensParentParameter.length==0)
			{
				cashierClosing.setUserChildrensParent(String.valueOf(user.getId()));								
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
				cashierClosing.setUserChildrensParent(userChildrensParentFilters.toString());
			}
						
			final CashierClosingService cashierClosingService = new CashierClosingServiceImpl(connection);
			cashierClosingService.getAllCashierClosing(cashierClosing);
		}
		catch(final Exception ex) 
		{
			throw ex;
		}		
		return cashierClosing;
	}
		
	@PostMapping(value=ConstantDataManager.CASHIER_CLOSING_CONTROLLER_METHOD_SAVE)
	public ResponseEntity<String> saveCashierClosing(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.CASHIER_CLOSING_CONTROLLER + ConstantDataManager.CASHIER_CLOSING_CONTROLLER_METHOD_SAVE);
		
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
			final String jsonData = request.getParameter(ConstantDataManager.PARAMETER_JSON_DATA);
			
			final Type type = new TypeToken<ArrayList<CashierClosingRequestStoreDTO>>(){}.getType();
			final List<CashierClosingRequestStoreDTO> originsCompanys = gson.fromJson(jsonData, type);
			
			final CashierClosingRequest cashierClosingRequest = gson.fromJson(jsonDataRequest, CashierClosingRequest.class);
  		
			final String documentNumberTransfDest = StringUtils.generateDateTimeNumber();	
			
			if(StringUtils.isNull(cashierClosingRequest.getPaymentExpiryDateTransfDest()))
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PAYMENT_EXPIRY_DATE_INVALID, null));								
			}
			
			final String[] dateArrayPaymentExpiryDateTransfDest = cashierClosingRequest.getPaymentExpiryDateTransfDest().split(com.manager.systems.common.utils.ConstantDataManager.BARRA);
			cashierClosingRequest.setPaymentExpiryDateTransfDest(dateArrayPaymentExpiryDateTransfDest[2]+com.manager.systems.common.utils.ConstantDataManager.TRACO+dateArrayPaymentExpiryDateTransfDest[1]+com.manager.systems.common.utils.ConstantDataManager.TRACO+dateArrayPaymentExpiryDateTransfDest[0]+com.manager.systems.common.utils.ConstantDataManager.SPACE+com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO);

			if(cashierClosingRequest.getCompanyTransfDest() == 0)
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMPANY_INVALID, null));								
			}
			
			if(cashierClosingRequest.getDocumentStatusTransfDest() == 0)
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DOCUMENT_STATUS_INVALID, null));								
			}		

			if(cashierClosingRequest.getBankAccountTransfDest() == 0)
			{
				throw new FinancialException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_BANK_ACCOUNT_INVALID, null));								
			}	

			final List<FinancialDocumentDTO> documentOrigins = new ArrayList<FinancialDocumentDTO>();
			final List<FinancialDocumentDTO> documentOriginsResidue = new ArrayList<FinancialDocumentDTO>();
			final List<FinancialDocumentDTO> documentExpenses = new ArrayList<FinancialDocumentDTO>();
			final List<FinancialDocumentDTO> documentsDestiny = new ArrayList<FinancialDocumentDTO>();
			
			int countDocuments = 0;
			
			//double totalDestinyExpense = 0;
			//double totalOriginExpense = 0;
			if(cashierClosingRequest.getExpenses() != null && cashierClosingRequest.getExpenses().size() > 0) {
				for (final CashierClosingRequestExpense expense : cashierClosingRequest.getExpenses()) {
					countDocuments++;
					
					final String[] paymentExpiryDateExpense = expense.getPaymentExpiryDateExpense().split(com.manager.systems.common.utils.ConstantDataManager.BARRA);
					expense.setPaymentExpiryDateExpense(paymentExpiryDateExpense[2]+com.manager.systems.common.utils.ConstantDataManager.TRACO+paymentExpiryDateExpense[1]+com.manager.systems.common.utils.ConstantDataManager.TRACO+paymentExpiryDateExpense[0]+com.manager.systems.common.utils.ConstantDataManager.SPACE+com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO);
					
					final FinancialDocumentDTO documentExpense = new FinancialDocumentDTO();	
					documentExpense.setCredit(false);
					documentExpense.setDocumentValue(expense.getDocumentValueExpense());
					documentExpense.setPaymentValue(expense.getDocumentValueExpense());	
					documentExpense.setDocumentParentId(0);
					documentExpense.setCompanyId(expense.getCompanyId());
					documentExpense.setBankAccountId(expense.getBankAccountExpense()); 
					documentExpense.setDocumentStatus(2);
					documentExpense.setDocumentTransferId(0L); // pegar do destino	
					documentExpense.setPaymentExpiryDate(StringUtils.convertStringDateHoraToLocalDateTime(expense.getPaymentExpiryDateExpense()));
					documentExpense.setPaymentDate(StringUtils.convertStringDateHoraToLocalDateTime(expense.getPaymentExpiryDateExpense()));
					documentExpense.setBankAccountOriginId(expense.getBankAccountExpense());  
					documentExpense.setUserChange(user.getId());
					documentExpense.setDocumentType(3);
					documentExpense.setResidue(0);	
					documentExpense.setProviderId(expense.getProviderExpense());
					documentExpense.setFinancialGroupId(expense.getFinancialGroupExpense());
					documentExpense.setFinancialSubGroupId(expense.getFinancialSubGroupExpense());
					documentExpense.setDocumentNumber(documentNumberTransfDest+com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE+countDocuments);
					
					documentExpense.setDocumentNote(com.manager.systems.common.utils.ConstantDataManager.LABEL_CASH_CLOSING_EXPENSE_TRANSF_LABEL +
							com.manager.systems.common.utils.ConstantDataManager.SPACE+
							com.manager.systems.common.utils.ConstantDataManager.LARGER+
							com.manager.systems.common.utils.ConstantDataManager.SPACE+ 
							com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER+ 
							com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS+
							com.manager.systems.common.utils.ConstantDataManager.SPACE+ 
							expense.getProviderExpense() +
							com.manager.systems.common.utils.ConstantDataManager.SPACE + 
							(StringUtils.isNull(expense.getDocumentNoteExpenseDecode()) ? 
									com.manager.systems.common.utils.ConstantDataManager.BLANK :
									(com.manager.systems.common.utils.ConstantDataManager.LABEL_OBS + com.manager.systems.common.utils.ConstantDataManager.SPACE + expense.getDocumentNoteExpenseDecode())
							)
							);
					
					documentExpenses.add(documentExpense);				
				}
				
				//totalDestinyExpense = cashierClosingRequest.getExpenses().parallelStream().filter(x -> x.getCompanyId() == cashierClosingRequest.getCompanyTransfDest()).mapToDouble(CashierClosingRequestExpense::getDocumentValueExpense).sum();
				//totalOriginExpense = cashierClosingRequest.getExpenses().parallelStream().filter(x -> x.getCompanyId() != cashierClosingRequest.getCompanyTransfDest()).mapToDouble(CashierClosingRequestExpense::getDocumentValueExpense).sum();
			}	
			
			final StringBuilder companysOriginsIds = new StringBuilder();			
			int countCompanysOriginsIds = 0;
			for (final CashierClosingRequestStoreDTO origin : originsCompanys) {
				if(countCompanysOriginsIds > 0) {
					companysOriginsIds.append(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);
				}
				companysOriginsIds.append(origin.getCompanyId());
				
				countCompanysOriginsIds++;
				
				
				//final double totalMovProduct = origin.getMovProd().parallelStream().mapToDouble(CashierClosingRequestProductDTO::getTotalValue).sum();
				//double totalMovTransf = origin.getMovTransf().parallelStream().mapToDouble(CashierClosingRequestTransferDTO::getDocumentValue).sum();
				final double totalMovement = origin.getTotalMovement();
				final double totalPendingBefore = origin.getTotalPendingBefore();
				final double totalPendingAfter = origin.getTotalPendingAfter();
				final double totalExpenseOrigin = cashierClosingRequest.getExpenses().parallelStream().filter(x -> x.getCompanyId() == origin.getCompanyId()).mapToDouble(CashierClosingRequestExpense::getDocumentValueExpense).sum();
				//totalMovTransf = totalMovTransf + totalPendingBefore;

				double totalCompanyBalance = totalMovement - totalPendingAfter;
				final double totalDestinyBalance = (totalCompanyBalance + totalPendingBefore - totalExpenseOrigin);
					
				countDocuments++;
				
				totalCompanyBalance = totalCompanyBalance - totalExpenseOrigin;
				
				final FinancialDocumentDTO documentOrigin = new FinancialDocumentDTO();	
				documentOrigin.setCredit((totalCompanyBalance >= 0 ? false : true));
				documentOrigin.setDocumentValue((totalCompanyBalance < 0 ? totalCompanyBalance * -1 : totalCompanyBalance));
				documentOrigin.setPaymentValue((totalCompanyBalance < 0 ? totalCompanyBalance * -1 : totalCompanyBalance));	
				documentOrigin.setDocumentParentId(0);
				documentOrigin.setCompanyId(origin.getCompanyId());
				documentOrigin.setBankAccountId(origin.getBankAccountId());
				documentOrigin.setBankAccountOriginId(cashierClosingRequest.getBankAccountTransfDest()); // pegar na procedure
				documentOrigin.setDocumentStatus(2);
				documentOrigin.setDocumentTransferId(0L); // pegar do destino	
				//if(origin.getPaymentDate() !=null) {
					//documentOrigin.setPaymentExpiryDate(StringUtils.convertStringDateHoraToLocalDateTime((origin.getPaymentDate() + com.manager.systems.common.utils.ConstantDataManager.SPACE + com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO), StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
					//documentOrigin.setPaymentDate(StringUtils.convertStringDateHoraToLocalDateTime((origin.getPaymentDate() + com.manager.systems.common.utils.ConstantDataManager.SPACE + com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO), StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
				//}
				//else {
					documentOrigin.setPaymentExpiryDate(StringUtils.convertStringDateHoraToLocalDateTime(cashierClosingRequest.getPaymentExpiryDateTransfDest()));
					documentOrigin.setPaymentDate(StringUtils.convertStringDateHoraToLocalDateTime(cashierClosingRequest.getPaymentExpiryDateTransfDest()));					
				//}
				documentOrigin.setUserChange(user.getId());
				documentOrigin.setDocumentType(2);
				documentOrigin.setResidue(0);	
				documentOrigin.setDocumentNumber(documentNumberTransfDest+com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE+countDocuments);
				documentOrigin.setDocumentsKeys(origin.getDocumentsKeys());
				
				documentOrigin.setDocumentNote(com.manager.systems.common.utils.ConstantDataManager.LABEL_CASH_CLOSING_TRANSF_LABEL +
						com.manager.systems.common.utils.ConstantDataManager.SPACE+
						com.manager.systems.common.utils.ConstantDataManager.LARGER+
						com.manager.systems.common.utils.ConstantDataManager.SPACE+
						com.manager.systems.common.utils.ConstantDataManager.LABEL_DESINY+
						com.manager.systems.common.utils.ConstantDataManager.SPACE+
						com.manager.systems.common.utils.ConstantDataManager.INPUT_MOVEMENET_DESTINO_DOCUMENT_NOTE_ALIAS+
						com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS+
						com.manager.systems.common.utils.ConstantDataManager.SPACE+
						//com.manager.systems.common.utils.ConstantDataManager.PARENTESES_LEFT +
						//cashierClosingRequest.getCompanyTransfDest() + com.manager.systems.common.utils.ConstantDataManager.SPACE + com.manager.systems.common.utils.ConstantDataManager.TRACO + com.manager.systems.common.utils.ConstantDataManager.SPACE + 
						cashierClosingRequest.getCompanyDescriptionTransfDest() +
						//com.manager.systems.common.utils.ConstantDataManager.PARENTESES_RIGHT +
						com.manager.systems.common.utils.ConstantDataManager.SPACE + 
						(StringUtils.isNull(cashierClosingRequest.getDocumentNoteTransfDestDecode()) ? 
								com.manager.systems.common.utils.ConstantDataManager.BLANK :
								(com.manager.systems.common.utils.ConstantDataManager.LABEL_OBS + com.manager.systems.common.utils.ConstantDataManager.SPACE + cashierClosingRequest.getDocumentNoteTransfDestDecode())
						)
						);
				
				if(documentOrigin.isCredit()) 
				{
					documentOrigin.setProviderId(52); //RECEITAS POSITIVAS
					documentOrigin.setFinancialGroupId("20.000"); //RECEITAS OPERACIONAIS
					documentOrigin.setFinancialSubGroupId("20.002"); //MOVIMENTOS DE ENTRADA
				} 
				else 
				{
					documentOrigin.setProviderId(53); //RECEITAS NEGATIVAS
					documentOrigin.setFinancialGroupId("20.000"); //DESPESAS OPERACIONAIS
					documentOrigin.setFinancialSubGroupId("20.003"); //MOVIMENTOS DE SAIDA
				}
				
				documentOrigins.add(documentOrigin);
				
				countDocuments++;
				final FinancialDocumentDTO documentDestiny = new FinancialDocumentDTO();	
				documentDestiny.setCredit((totalDestinyBalance < 0 ? false : true));
				documentDestiny.setDocumentValue((totalDestinyBalance < 0 ? totalDestinyBalance * -1 : totalDestinyBalance));
				documentDestiny.setPaymentValue((totalDestinyBalance < 0 ? totalDestinyBalance * -1 : totalDestinyBalance));	
				documentDestiny.setDocumentParentId(0); // Pegar na procedure
				documentDestiny.setCompanyId(cashierClosingRequest.getCompanyTransfDest());
				documentDestiny.setBankAccountId(cashierClosingRequest.getBankAccountTransfDest()); // pegar na procedure
				documentDestiny.setBankAccountOriginId(origin.getBankAccountId()); // pegar na procedure
				documentDestiny.setDocumentStatus(2);
				documentDestiny.setDocumentTransferId(0L); // pegar do destino	
				//if(origin.getPaymentDate() !=null) {
				//	documentDestiny.setPaymentExpiryDate(StringUtils.convertStringDateHoraToLocalDateTime((origin.getPaymentDate() + com.manager.systems.common.utils.ConstantDataManager.SPACE + com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO), StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
				//	documentDestiny.setPaymentDate(StringUtils.convertStringDateHoraToLocalDateTime((origin.getPaymentDate() + com.manager.systems.common.utils.ConstantDataManager.SPACE + com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO), StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
				//}
				//else {
					documentDestiny.setPaymentExpiryDate(StringUtils.convertStringDateHoraToLocalDateTime(cashierClosingRequest.getPaymentExpiryDateTransfDest()));
					documentDestiny.setPaymentDate(StringUtils.convertStringDateHoraToLocalDateTime(cashierClosingRequest.getPaymentExpiryDateTransfDest()));
				//}
				documentDestiny.setUserChange(user.getId());
				documentDestiny.setDocumentType(2);
				documentDestiny.setResidue(0);	
				documentDestiny.setDocumentNumber(documentNumberTransfDest+com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE+countDocuments);
				documentDestiny.setDocumentsKeys(origin.getDocumentsKeys());
				
				documentDestiny.setDocumentNote(com.manager.systems.common.utils.ConstantDataManager.LABEL_CASH_CLOSING_TRANSF_LABEL +
						com.manager.systems.common.utils.ConstantDataManager.SPACE+
						com.manager.systems.common.utils.ConstantDataManager.LARGER+
						com.manager.systems.common.utils.ConstantDataManager.SPACE+
						com.manager.systems.common.utils.ConstantDataManager.LABEL_ORIGIN+
						com.manager.systems.common.utils.ConstantDataManager.SPACE+
						com.manager.systems.common.utils.ConstantDataManager.INPUT_MOVEMENET_DESTINO_DOCUMENT_NOTE_ALIAS+
						com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS+
						com.manager.systems.common.utils.ConstantDataManager.SPACE+
						//com.manager.systems.common.utils.ConstantDataManager.PARENTESES_LEFT +
						origin.getCompanyId() + com.manager.systems.common.utils.ConstantDataManager.SPACE + com.manager.systems.common.utils.ConstantDataManager.TRACO + com.manager.systems.common.utils.ConstantDataManager.SPACE + origin.getCompanyDescription() + 
						//com.manager.systems.common.utils.ConstantDataManager.PARENTESES_RIGHT +
						com.manager.systems.common.utils.ConstantDataManager.SPACE + 
						(StringUtils.isNull(cashierClosingRequest.getDocumentNoteTransfDestDecode()) ? 
								com.manager.systems.common.utils.ConstantDataManager.BLANK :
								(com.manager.systems.common.utils.ConstantDataManager.LABEL_OBS + com.manager.systems.common.utils.ConstantDataManager.SPACE + cashierClosingRequest.getDocumentNoteTransfDestDecode())
						)
						);
				
				if(documentDestiny.isCredit()) 
				{
					documentDestiny.setProviderId(48); //RECEITAS POSITIVAS
					documentDestiny.setFinancialGroupId("01.000"); //RECEITAS OPERACIONAIS
					documentDestiny.setFinancialSubGroupId("01.001"); //MOVIMENTOS DE ENTRADA
				} 
				else 
				{
					documentDestiny.setProviderId(49); //RECEITAS NEGATIVAS
					documentDestiny.setFinancialGroupId("07.000"); //DESPESAS OPERACIONAIS
					documentDestiny.setFinancialSubGroupId("07.001"); //MOVIMENTOS DE SAIDA
				}
				
				documentsDestiny.add(documentDestiny);
				
				if(totalPendingAfter != 0) {
					countDocuments++;
					
					final FinancialDocumentDTO documentOriginResidue = new FinancialDocumentDTO();	
					documentOriginResidue.setCredit((origin.getTotalPendingAfter() < 0 ? true : false));
					documentOriginResidue.setDocumentValue((origin.getTotalPendingAfter() < 0 ? origin.getTotalPendingAfter() * -1 : origin.getTotalPendingAfter()));
					documentOriginResidue.setPaymentValue(0);	
					documentOriginResidue.setDocumentParentId(0);
					documentOriginResidue.setCompanyId(origin.getCompanyId());
					documentOriginResidue.setBankAccountId(origin.getBankAccountId()); // pegar na procedure
					documentOriginResidue.setDocumentStatus(1);
					documentOriginResidue.setDocumentTransferId(0L); // pegar do destino	
					if(origin.getPaymentDate() !=null) {
						documentOriginResidue.setPaymentExpiryDate(StringUtils.convertStringDateHoraToLocalDateTime((origin.getPaymentDate() + com.manager.systems.common.utils.ConstantDataManager.SPACE + com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO), StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
						documentOriginResidue.setPaymentDate(StringUtils.convertStringDateHoraToLocalDateTime((origin.getPaymentDate() + com.manager.systems.common.utils.ConstantDataManager.SPACE + com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO), StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
					}
					else {
						documentOriginResidue.setPaymentExpiryDate(StringUtils.convertStringDateHoraToLocalDateTime(cashierClosingRequest.getPaymentExpiryDateTransfDest()));
						documentOriginResidue.setPaymentDate(StringUtils.convertStringDateHoraToLocalDateTime(cashierClosingRequest.getPaymentExpiryDateTransfDest()));
					}
					documentOriginResidue.setBankAccountOriginId(0);  // pegar na procedure
					documentOriginResidue.setUserChange(user.getId());
					documentOriginResidue.setDocumentType(2);
					documentOriginResidue.setResidue(1);	
					documentOriginResidue.setDocumentNumber(documentNumberTransfDest+com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE+countDocuments);
					documentOriginResidue.setDocumentNote(com.manager.systems.common.utils.ConstantDataManager.LABEL_CASH_CLOSING_RESIDUE_TRANSF_LABEL +
							com.manager.systems.common.utils.ConstantDataManager.SPACE+
							com.manager.systems.common.utils.ConstantDataManager.LARGER+
							com.manager.systems.common.utils.ConstantDataManager.SPACE+
							com.manager.systems.common.utils.ConstantDataManager.INPUT_MOVEMENET_DESTINO_DOCUMENT_NOTE_ALIAS+
							com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS+
							com.manager.systems.common.utils.ConstantDataManager.SPACE+
							cashierClosingRequest.getCompanyDescriptionTransfDest() +
							com.manager.systems.common.utils.ConstantDataManager.SPACE + 
							(StringUtils.isNull(cashierClosingRequest.getDocumentNoteTransfDestDecode()) ? 
									com.manager.systems.common.utils.ConstantDataManager.BLANK :
									(com.manager.systems.common.utils.ConstantDataManager.LABEL_OBS + com.manager.systems.common.utils.ConstantDataManager.SPACE + cashierClosingRequest.getDocumentNoteTransfDestDecode())
							)
							);
					
					if(documentOriginResidue.isCredit()) 
					{
						documentOriginResidue.setProviderId(52); //RECEITAS POSITIVAS
						documentOriginResidue.setFinancialGroupId("20.000"); //RECEITAS OPERACIONAIS
						documentOriginResidue.setFinancialSubGroupId("20.002"); //MOVIMENTOS DE ENTRADA
					} 
					else 
					{
						documentOriginResidue.setProviderId(53); //RECEITAS NEGATIVAS
						documentOriginResidue.setFinancialGroupId("20.000"); //DESPESAS OPERACIONAIS
						documentOriginResidue.setFinancialSubGroupId("20.003"); //MOVIMENTOS DE SAIDA
					}
					
					documentOriginsResidue.add(documentOriginResidue);	
				}				
			}		
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			connection.setAutoCommit(false);
			final CashierClosingService cashierClosingService = new CashierClosingServiceImpl(connection);
			final DocumentMovementIdService documentMovementIdService = new DocumentMovementIdServiceImpl(connection);

			final DocumentMovementIdDTO documentMovementId = documentMovementIdService.generateDocumentMovementId(true, false);
			 
			for(final FinancialDocumentDTO origin : documentOrigins) {
				if(origin.getDocumentValue() != 0) {
					origin.setDocumentTransferId(documentMovementId.getDocumentParentId());
					origin.setDocumentParentId(cashierClosingService.creditTrasnferValuesOriginToDestinty(origin));					
				}
				
				final String[] keyArray = origin.getDocumentsKeys().split(com.manager.systems.common.utils.ConstantDataManager.PONTO_VIRGULA_STRING);
				if(keyArray != null && keyArray.length > 0) {
					for (final String keyArrayItem : keyArray) {
						final String[] keys = keyArrayItem.split(com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE);
						if(keys != null && keys.length > 0) {
							final FinancialDocumentDTO checkClosing = new FinancialDocumentDTO();
							checkClosing.setDocumentParentId(Long.valueOf(keys[0]));
							checkClosing.setCompanyId(Long.valueOf(keys[1]));
							checkClosing.setDocumentTransferId(documentMovementId.getDocumentParentId());
							
							
							cashierClosingService.checkCashingCloseSuccess(checkClosing);
						}
					}
				}
			}
			
			for(final FinancialDocumentDTO destiny : documentsDestiny) {
				destiny.setDocumentTransferId(documentMovementId.getDocumentParentId());
				destiny.setDocumentParentId(documentMovementId.getDocumentParentId());
				cashierClosingService.creditTrasnferValuesOriginToDestinty(destiny);
			}
			
			for(final FinancialDocumentDTO residual : documentOriginsResidue) {
				residual.setDocumentTransferId(documentMovementId.getDocumentParentId());
				cashierClosingService.debitTrasnferValuesOriginResidue(residual);
			}
			
			for(final FinancialDocumentDTO expense : documentExpenses) {
				if(expense.getCompanyId() == cashierClosingRequest.getCompanyTransfDest()) {
					expense.setDocumentParentId(documentMovementId.getDocumentParentId());					
				}
				else {
					final OptionalLong documentParentIdOrigin = documentOrigins.parallelStream().filter(x -> x.getCompanyId() == expense.getCompanyId()).mapToLong(FinancialDocumentDTO::getDocumentParentId).findFirst();
					if(documentParentIdOrigin.isPresent()) {
						expense.setDocumentParentId(documentParentIdOrigin.getAsLong());
						expense.setDocumentTransferId(documentMovementId.getDocumentParentId());
					}
				}
				expense.setUserChange(user.getId());
				cashierClosingService.saveDocumentExpense(expense);
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
	
	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_REPORT_METHOD)
	public ResponseEntity<byte[]> filterReport(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.CASHIER_CLOSING_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_REPORT_METHOD);
		
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
				analiticParameter = com.manager.systems.common.utils.ConstantDataManager.ONE_STRING;
			}
			
			final String dateParameter = request.getParameter(ConstantDataManager.PARAMETER_DATE);
			
			final String jsonData = request.getParameter(ConstantDataManager.PARAMETER_JSON_DATA);
			
			final Gson gson = new Gson();
			final Type type = new TypeToken<ArrayList<CashierClosingRequestStoreDTO>>(){}.getType();
			final List<CashierClosingRequestStoreDTO> items = gson.fromJson(jsonData, type);
			
			final CashierClosingService cashierClosingService = new CashierClosingServiceImpl(null);
			
			if(Integer.parseInt(analiticParameter) == 0) {
				pdfFile = cashierClosingService.generatePDFCashierClosingAnalitic(dateParameter, items);
			}else {
				pdfFile = cashierClosingService.generatePDFCashierClosingSintetic(dateParameter, items);
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
}
