package com.manager.systems.web.adm.controller;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.manager.systems.common.dto.adm.FinancialGroupDTO;
import com.manager.systems.common.dto.adm.FinancialTransferGroupDTO;
import com.manager.systems.common.dto.adm.FinancialTransferGroupItemDTO;
import com.manager.systems.common.dto.adm.ReportBankAccountDTO;
import com.manager.systems.common.dto.adm.ReportFinancialTransferGroupDTO;
import com.manager.systems.common.dto.adm.ReportProductSubGroupDTO;
import com.manager.systems.common.service.adm.BankAccountService;
import com.manager.systems.common.service.adm.FinancialGroupService;
import com.manager.systems.common.service.adm.FinancialTransferGroupService;
import com.manager.systems.common.service.adm.ProductSubGroupService;
import com.manager.systems.common.service.impl.adm.BankAccountServiceImpl;
import com.manager.systems.common.service.impl.adm.FinancialGroupServiceImpl;
import com.manager.systems.common.service.impl.adm.FinancialTransferGroupServiceImpl;
import com.manager.systems.common.service.impl.adm.ProductSubGroupServiceImpl;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.model.admin.User;
import com.manager.systems.web.adm.exception.AdminException;
import com.manager.systems.web.adm.utils.ConstantDataManager;
import com.manager.systems.web.common.controller.BaseController;

@Controller
@RequestMapping(value=ConstantDataManager.FINANCIAL_TRANSFER_MAINTENANCE_CONTROLLER)
public class FinancialTransferMaintenanceController extends BaseController
{
	private static final Log log = LogFactory.getLog(FinancialTransferMaintenanceController.class);
	
	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD)
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		
		log.info(ConstantDataManager.FINANCIAL_TRANSFER_MAINTENANCE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD);
		
		String result = ConstantDataManager.FINANCIAL_TRANSFER_MAINTENANCE_OPEN_METHOD_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
		Connection connection = null;
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			if(!user.hasPermission("MENU_FINANCEIRO", "MENU_FINANCEIRO_CONFIGURACAO_TRANSFERENCIA")) {
				result = ConstantDataManager.RESULT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Financeiro Configuração de Transferencia.");
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final ReportBankAccountDTO reportBankAccount = new ReportBankAccountDTO();
			reportBankAccount.setUserOperation(user.getId());
			final BankAccountService bankAccountService = new BankAccountServiceImpl(connection);	
			final List<Combobox> bankAccounts = bankAccountService.getAllCombobox(reportBankAccount);
			request.setAttribute(ConstantDataManager.PARAMETER_BANK_ACCOUNTS, bankAccounts);
			
			final FinancialGroupService financialGroupService = new FinancialGroupServiceImpl(connection);
			final List<Combobox> financialGroups = financialGroupService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_FINANCIAL_GROUPS, financialGroups);
			
			final ReportProductSubGroupDTO reportProductSubGroup = new ReportProductSubGroupDTO();
			final ProductSubGroupService productSubGroupService = new ProductSubGroupServiceImpl(connection);
			final List<Combobox> productSubGroups = productSubGroupService.getAllCombobox(reportProductSubGroup);
			request.setAttribute(ConstantDataManager.PARAMETER_PRODUCT_SUB_GROUPS, productSubGroups);
			
			request.setAttribute(ConstantDataManager.PARAMETER_OBJECT_TYPE_ALIAS, com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_FINANCIAL_TRANSFER);
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
	
	@PostMapping(value=ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD)
	public ResponseEntity<String> save(final HttpServletRequest request) throws Exception
	{
		
		log.info(ConstantDataManager.FINANCIAL_TRANSFER_MAINTENANCE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD);
		
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
		
		Connection connection = null;
		
		final FinancialTransferGroupDTO financialTransferGroup = new FinancialTransferGroupDTO();
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
						
	        final FinancialTransferGroupItemDTO financialTransferGroupItem = new FinancialTransferGroupItemDTO();
	        
	        String groupId = request.getParameter(ConstantDataManager.PARAMETER_GROUP_ID);
	        if(!StringUtils.isLong(groupId))
	        {
	        	groupId = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
	        }
	        financialTransferGroup.setId(Integer.valueOf(groupId));

	        final String bankAccountOrigin = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_ORIGIN);
	        if(StringUtils.isLong(bankAccountOrigin))
	        {
	        	financialTransferGroup.setBankAccountOriginId(Integer.valueOf(bankAccountOrigin));
	        }
	        final String bankAccountDestiny = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_DESTINY);
	        if(StringUtils.isLong(bankAccountDestiny))
	        {
	        	financialTransferGroup.setBankAccountDestinyId(Integer.valueOf(bankAccountDestiny));
	        }	      
	        String bankAccountAutomaticTransfer = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_AUTOMATIC_TRANSFER);
	        if(!StringUtils.isLong(bankAccountAutomaticTransfer)) {
	        	bankAccountAutomaticTransfer = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
	        }
	        financialTransferGroup.setBankAccountAutomaticTransferId(Integer.parseInt(bankAccountAutomaticTransfer));
	        
	        String executionPeriod = request.getParameter(ConstantDataManager.PARAMETER_FINANCIALTRANSFER_EXECUTION_PERIOD);	        
	        if(!StringUtils.isLong(executionPeriod)) {
	        	executionPeriod = String.valueOf(7);
	        }
	        financialTransferGroup.setExecutionPeriod(Integer.valueOf(executionPeriod));
	        
	        String weekDay = request.getParameter(ConstantDataManager.PARAMETER_FINANCIALTRANSFER_WEEK_DAY);	        
	        if(!StringUtils.isLong(weekDay)) {
	        	weekDay = String.valueOf(-1);
	        }
	        financialTransferGroup.setWeekDay(Integer.valueOf(weekDay));
	        
	        String fixedDay = request.getParameter(ConstantDataManager.PARAMETER_FINANCIALTRANSFER_FIXED_DAY);
	        if(com.manager.systems.common.utils.ConstantDataManager.ON.equalsIgnoreCase(fixedDay) || com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING.equalsIgnoreCase(fixedDay))
			{
	        	fixedDay = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
			}
			else
			{
				fixedDay = com.manager.systems.common.utils.ConstantDataManager.FALSE_STRING;
			}
	        financialTransferGroup.setFixedDay(Boolean.valueOf(fixedDay));
	        
	        String fixedDayMonth = request.getParameter(ConstantDataManager.PARAMETER_FINANCIALTRANSFER_FIXED_DAY_MONTH);
	        if(com.manager.systems.common.utils.ConstantDataManager.ON.equalsIgnoreCase(fixedDayMonth) || com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING.equalsIgnoreCase(fixedDayMonth))
			{
	        	fixedDayMonth = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
			}
			else
			{
				fixedDayMonth = com.manager.systems.common.utils.ConstantDataManager.FALSE_STRING;
			}
	        financialTransferGroup.setFixedDayMonth(Boolean.valueOf(fixedDayMonth));
	        
	        String automaticProcessing = request.getParameter(ConstantDataManager.PARAMETER_FINANCIALTRANSFER_AUTOMATIC_PROCESSING);
	        if(com.manager.systems.common.utils.ConstantDataManager.ON.equalsIgnoreCase(automaticProcessing) || com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING.equalsIgnoreCase(automaticProcessing))
			{
	        	automaticProcessing = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
			}
			else
			{
				automaticProcessing = com.manager.systems.common.utils.ConstantDataManager.FALSE_STRING;
			}
	        financialTransferGroup.setAutomaticProcessing(Boolean.valueOf(automaticProcessing));
	        
	        String executionTimeParameter = request.getParameter(ConstantDataManager.PARAMETER_FINANCIALTRANSFER_EXECUTION_TIME);
	        if(StringUtils.isNull(executionTimeParameter)) {
	        	executionTimeParameter = com.manager.systems.common.utils.ConstantDataManager.BLANK;
	        }
	        financialTransferGroup.setExecutionTime(executionTimeParameter);
	        
	        final String[] executionDaysParameter = request.getParameterValues(ConstantDataManager.PARAMETER_FINANCIALTRANSFER_EXECUTION_DAYS);
	        financialTransferGroup.setExecutionDays(executionDaysParameter);	        
	      	       
	        String initialExecution = request.getParameter(ConstantDataManager.PARAMETER_INITIAL_EXECUTION);	        
	        if(StringUtils.isNull(executionPeriod)) {
	        	initialExecution = StringUtils.formatDate(Calendar.getInstance().getTime(), StringUtils.DATE_PATTERN_DD_MM_YYYY);
	        }
			financialTransferGroup.setInitialExecution(StringUtils.convertStringDateToDate(initialExecution, StringUtils.DATE_FORMAT_DD_MM_YYYY));
	        
	        final String descriptionGroup = request.getParameter(ConstantDataManager.PARAMETER_DESCRIPTION_GROUP);
	        financialTransferGroup.setDescription(descriptionGroup.toUpperCase());
	        final String executionOrderGroup = request.getParameter(ConstantDataManager.PARAMETER_EXECUTION_ORDER_GROUP);
	        if(StringUtils.isLong(executionOrderGroup))
	        {
	        	financialTransferGroup.setOrder(Integer.valueOf(executionOrderGroup));
	        }	        
	        String active = request.getParameter(ConstantDataManager.PARAMETER_ACTIVE);
	        if(com.manager.systems.common.utils.ConstantDataManager.ON.equalsIgnoreCase(active) || com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING.equalsIgnoreCase(active))
			{
	        	active = com.manager.systems.common.utils.ConstantDataManager.FALSE_STRING;
			}
			else
			{
				active = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
			}
	        financialTransferGroup.setInactive(Boolean.valueOf(active));	        
	        final String[] products = request.getParameterValues(ConstantDataManager.PARAMETER_PRODUCTS);
	        financialTransferGroup.convertArrayProductsIdToList(financialTransferGroup.getId(), products);     
	        
	        String saveAllParameter = request.getParameter(ConstantDataManager.PARAMETER_SAVE_ALL);
	        if(StringUtils.isNull(saveAllParameter))
	        {
	        	saveAllParameter = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
	        }
	        financialTransferGroup.setSaveAll(Boolean.parseBoolean(saveAllParameter));
	        
	        if(financialTransferGroup.isSaveAll())
	        {
		        String groupItemId = request.getParameter(ConstantDataManager.PARAMETER_GROUP_ITEM_ID);
		        if(!StringUtils.isLong(groupItemId))
		        {
		        	groupItemId = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
		        }
		        financialTransferGroupItem.setId(Integer.valueOf(groupItemId));
		        //final String descriptionTransfer = request.getParameter(ConstantDataManager.PARAMETER_DESCRIPTION_TRANSFER);
		        financialTransferGroupItem.setDescription(com.manager.systems.common.utils.ConstantDataManager.BLANK);	        
		        
		        final String provider = request.getParameter(ConstantDataManager.PARAMETER_PROVIDER);
		        if(StringUtils.isLong(provider))
		        {
		        	financialTransferGroupItem.setProviderId(Long.valueOf(provider));
		        }
		        
		        final String financialGroup = request.getParameter(ConstantDataManager.PARAMETER_FINANCIAL_GROUP);
		        if(!StringUtils.isNull(financialGroup))
		        {
		        	financialTransferGroupItem.setFinantialGroupId(financialGroup);
		        }
		        
		        final String financialSubGroup = request.getParameter(ConstantDataManager.PARAMETER_FINANCIAL_SUB_GROUP);
		        if(!StringUtils.isNull(financialSubGroup))
		        {
		        	financialTransferGroupItem.setFinantialSubGroupId(financialSubGroup);
		        }
		        
		        final String bankAccount = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT);
		        if(StringUtils.isLong(bankAccount))
		        {
		        	financialTransferGroupItem.setBankAccountId(Integer.valueOf(bankAccount));
		        }
		        
		        final String subGroupProduct = request.getParameter(ConstantDataManager.PARAMETER_SUB_GROUP_PRODUCT);
		        if(!StringUtils.isNull(subGroupProduct))
		        {
		        	final String[] subGroupProductArray = subGroupProduct.split(com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE);
		        	if(subGroupProductArray != null && subGroupProductArray.length == 2) {
		        		 
		        		if(StringUtils.isLong(subGroupProductArray[0])){
					        financialTransferGroupItem.setSubGroupProductId(Integer.valueOf(subGroupProductArray[0]));		        			
		        		}
		        		
		        		if(StringUtils.isLong(subGroupProductArray[1])){
					        financialTransferGroupItem.setGroupProductId(Integer.valueOf(subGroupProductArray[1]));		        			
		        		}
		        		
		        	}
		        }
		        
		        final String transferState = request.getParameter(ConstantDataManager.PARAMETER_TRANSFER_STATE);
		        if(StringUtils.isLong(transferState))
		        {
			        financialTransferGroupItem.setTransferState(Integer.valueOf(transferState));	        	
		        }
		        
		        final String transferType = request.getParameter(ConstantDataManager.PARAMETER_TRANSFER_TYPE);
		        if(StringUtils.isLong(transferType))
		        {
		        	financialTransferGroupItem.setTransferType(Integer.valueOf(transferType));
		        }
		        
		        final String launchType = request.getParameter(ConstantDataManager.PARAMETER_FINANCIALTRANSFER_LAUNCH_TYPE);
		        if(StringUtils.isLong(launchType))
		        {
		        	financialTransferGroupItem.setLaunchType(Integer.valueOf(launchType));
		        }

		        String valueTransfer = request.getParameter(ConstantDataManager.PARAMETER_VALUE_TRANSFER);
		        if(!StringUtils.isNull(valueTransfer))
				{
					if(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_ZERO_ZERO.equalsIgnoreCase(valueTransfer))
					{
						valueTransfer = com.manager.systems.common.utils.ConstantDataManager.ZERO_VIRGULA_ZERO_ZERO;
					}
					valueTransfer = StringUtils.replaceNonDigits(valueTransfer);
					final StringBuilder resultMoeda = new StringBuilder(valueTransfer);
					if(resultMoeda.length()>2)
					{
						resultMoeda.insert(resultMoeda.length() - 2, com.manager.systems.common.utils.ConstantDataManager.PONTO);
					}						
					valueTransfer = resultMoeda.toString();
				}
				else
				{
					valueTransfer = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
				}
		        financialTransferGroupItem.setValueTransfer(Double.valueOf(valueTransfer));
		        final String expense = request.getParameter(ConstantDataManager.PARAMETER_EXPENSE);
		        if(StringUtils.isLong(expense))
		        {
		        	financialTransferGroupItem.setExpense(Integer.valueOf(expense));
		        }
		        final String executionOrderGroupItem = request.getParameter(ConstantDataManager.PARAMETER_EXECUTION_ORDER_GROUP_ITEM);
		        if(StringUtils.isLong(executionOrderGroupItem))
		        {
		        	financialTransferGroupItem.setOrder(Integer.valueOf(executionOrderGroupItem));
		        }
		        String isOverTotal = request.getParameter(ConstantDataManager.PARAMETER_IS_OVER_TOTAL);
		        if(com.manager.systems.common.utils.ConstantDataManager.ON.equalsIgnoreCase(isOverTotal))
				{
		        	isOverTotal = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
				}
				else
				{
					isOverTotal = com.manager.systems.common.utils.ConstantDataManager.FALSE_STRING;
				}
		        financialTransferGroupItem.setOverTotal(Boolean.valueOf(isOverTotal));
		        String isUseRemainingBalance = request.getParameter(ConstantDataManager.PARAMETER_IS_USE_REMAINING_BALANCE);
		        if(com.manager.systems.common.utils.ConstantDataManager.ON.equalsIgnoreCase(isUseRemainingBalance))
				{
		        	isUseRemainingBalance = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
				}
				else
				{
					isUseRemainingBalance = com.manager.systems.common.utils.ConstantDataManager.FALSE_STRING;
				}
		        financialTransferGroupItem.setUseRemainingBalance(Boolean.valueOf(isUseRemainingBalance));
		        String creditDebit = request.getParameter(ConstantDataManager.PARAMETER_CREDIT_DEBIT);
		        if(com.manager.systems.common.utils.ConstantDataManager.ON.equalsIgnoreCase(creditDebit))
				{
		        	creditDebit = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
				}
				else
				{
					creditDebit = com.manager.systems.common.utils.ConstantDataManager.FALSE_STRING;
				}
		        financialTransferGroupItem.setCreditDebit(Boolean.valueOf(creditDebit));

		        String activeItem = request.getParameter(ConstantDataManager.PARAMETER_ACTIVE_ITEM);	   
		        if(com.manager.systems.common.utils.ConstantDataManager.ON.equalsIgnoreCase(activeItem))
				{
		        	activeItem = com.manager.systems.common.utils.ConstantDataManager.FALSE_STRING;
				}
				else
				{
					activeItem = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
				}
		        financialTransferGroupItem.setInactive(Boolean.valueOf(activeItem));	        	
	        }

			final ChangeData changeData = new ChangeData();
			changeData.setUserChange(user.getId());
			changeData.setChangeDate(Calendar.getInstance(tz).getTime());
			financialTransferGroup.setChangeData(changeData);
			financialTransferGroupItem.setChangeData(changeData);
			financialTransferGroup.addItem(financialTransferGroupItem);
			financialTransferGroup.validateFormSave();
							
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final FinancialTransferGroupService financialTransferGroupService = new FinancialTransferGroupServiceImpl(connection);						
			financialTransferGroupService.save(financialTransferGroup);
			
			if(financialTransferGroup.isSaveAll())
			{
				final ReportFinancialTransferGroupDTO reportTransfer = new ReportFinancialTransferGroupDTO();
				reportTransfer.setBankAccountOriginId(financialTransferGroup.getBankAccountOriginId());
				reportTransfer.setBankAccountDestinyId(financialTransferGroup.getBankAccountDestinyId());
				financialTransferGroupService.getAll(reportTransfer);				
				result.put(ConstantDataManager.PARAMETER_REPORT, reportTransfer.getItens());				
			}
			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
		}
		catch (final AdminException ex)
		{
			ex.printStackTrace();
			status = false;
			message =  ex.getMessage();
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			if(ex.getMessage()!=null && ex.getMessage().indexOf(com.manager.systems.web.common.utils.ConstantDataManager.EXCEPTION_DUPLICATE_ENTRY)>-1)
			{
				message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.FINANCIAL_TRANSFER_ITEM_ORDER_INVALID, new String[] {financialTransferGroup.getDescription()});
			}
			else
			{
				message =  ex.getMessage();				
			}
			status = false;
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
		log.info(ConstantDataManager.FINANCIAL_TRANSFER_MAINTENANCE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_INACTIVE_METHOD);
		
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
						
			final String bankAccountIdParameter = request.getParameter(ConstantDataManager.PARAMETER_ID);			
			final String inactiveParameter = request.getParameter(ConstantDataManager.PARAMETER_INACTIVE);
						
			if(!StringUtils.isLong(bankAccountIdParameter) || StringUtils.isNull(inactiveParameter))
			{	
				throw new AdminException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_DATA_REQUIRED_ERROR, null));				
			}
			
			final FinancialGroupDTO financialGroup = new FinancialGroupDTO();
			financialGroup.setId(bankAccountIdParameter);
			financialGroup.setInactive(Boolean.valueOf(inactiveParameter));
			final ChangeData changeData = new ChangeData();
			changeData.setUserChange(user.getId());
			changeData.setChangeDate(Calendar.getInstance(tz).getTime());
			financialGroup.setChangeData(changeData);
			final FinancialGroupService financialGroupService = new FinancialGroupServiceImpl(connection);	
			final boolean wasInactivated = financialGroupService.inactive(financialGroup);
			
			if(!wasInactivated)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
			}
			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
		} 
		catch (final AdminException ex)
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
		
		log.info(ConstantDataManager.FINANCIAL_TRANSFER_MAINTENANCE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_DETAIL_METHOD);
		
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
				
		Connection connection = null;
				
		try 
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user == null || user.getId() == 0)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
					
			final FinancialTransferGroupDTO financialTransferGroup = new FinancialTransferGroupDTO();
	        String groupId = request.getParameter(ConstantDataManager.PARAMETER_GROUP_ID);
	        if(!StringUtils.isLong(groupId))
	        {
	        	groupId = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
	        }
	        financialTransferGroup.setId(Integer.valueOf(groupId));

	        final String bankAccountOrigin = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_ORIGIN);
	        if(StringUtils.isLong(bankAccountOrigin))
	        {
	        	financialTransferGroup.setBankAccountOriginId(Integer.valueOf(bankAccountOrigin));
	        }
	        final String bankAccountDestiny = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_DESTINY);
	        if(StringUtils.isLong(bankAccountDestiny))
	        {
	        	financialTransferGroup.setBankAccountDestinyId(Integer.valueOf(bankAccountDestiny));
	        }
	        
	        financialTransferGroup.validateFormGet();
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}	
			final FinancialTransferGroupService financialTransferGroupService = new FinancialTransferGroupServiceImpl(connection);						
			final ReportFinancialTransferGroupDTO reportTransfer = new ReportFinancialTransferGroupDTO();
			reportTransfer.setBankAccountOriginId(financialTransferGroup.getBankAccountOriginId());
			reportTransfer.setBankAccountDestinyId(financialTransferGroup.getBankAccountDestinyId());
			reportTransfer.setUserOperation(user.getId());
			financialTransferGroupService.getFinancialTransfer(reportTransfer);
			
			status = true;
			result.put(ConstantDataManager.PARAMETER_REPORT, reportTransfer.getItens());
		} 
		catch (final AdminException ex)
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
	
	@PostMapping(value=ConstantDataManager.FINANCIAL_TRANSFER_MAINTENANCE_CONTROLLER_EDIT_GROUP_TRANSFER)
	public ResponseEntity<String> editGroupTransfer(final HttpServletRequest request) throws Exception
	{
		
		log.info(ConstantDataManager.FINANCIAL_TRANSFER_MAINTENANCE_CONTROLLER + ConstantDataManager.FINANCIAL_TRANSFER_MAINTENANCE_CONTROLLER_EDIT_GROUP_TRANSFER);
		
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
					
			final FinancialTransferGroupDTO financialTransferGroup = new FinancialTransferGroupDTO();
	        String groupId = request.getParameter(ConstantDataManager.PARAMETER_GROUP_ID);
	        if(!StringUtils.isLong(groupId))
	        {
	        	groupId = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
	        }
	        financialTransferGroup.setId(Integer.valueOf(groupId));

	        final String bankAccountOrigin = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_ORIGIN);
	        if(StringUtils.isLong(bankAccountOrigin))
	        {
	        	financialTransferGroup.setBankAccountOriginId(Integer.valueOf(bankAccountOrigin));
	        }
	        final String bankAccountDestiny = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_DESTINY);
	        if(StringUtils.isLong(bankAccountDestiny))
	        {
	        	financialTransferGroup.setBankAccountDestinyId(Integer.valueOf(bankAccountDestiny));
	        }
	        
	        financialTransferGroup.validateFormEditTransfer();
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}	
			
			financialTransferGroup.getItens().clear();
			financialTransferGroup.getItensList().clear();
			financialTransferGroup.getProducts().clear();
			
			final FinancialTransferGroupService financialTransferGroupService = new FinancialTransferGroupServiceImpl(connection);						
			financialTransferGroupService.get(financialTransferGroup);
			
			status = true;
			result.put(ConstantDataManager.PARAMETER_FINANCIAL_TRANSFER, financialTransferGroup);
		} 
		catch (final AdminException ex)
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
	
	@PostMapping(value=ConstantDataManager.FINANCIAL_TRANSFER_MAINTENANCE_CONTROLLER_EDIT_GROUP_ITEM_TRANSFER)
	public ResponseEntity<String> editGroupItemTransfer(final HttpServletRequest request) throws Exception
	{
		
		log.info(ConstantDataManager.FINANCIAL_TRANSFER_MAINTENANCE_CONTROLLER + ConstantDataManager.FINANCIAL_TRANSFER_MAINTENANCE_CONTROLLER_EDIT_GROUP_ITEM_TRANSFER);
		
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
					
			final FinancialTransferGroupDTO financialTransferGroup = new FinancialTransferGroupDTO();
	        String groupId = request.getParameter(ConstantDataManager.PARAMETER_GROUP_ID);
	        if(!StringUtils.isLong(groupId))
	        {
	        	groupId = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
	        }
	        financialTransferGroup.setId(Integer.valueOf(groupId));

	        final String bankAccountOrigin = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_ORIGIN);
	        if(StringUtils.isLong(bankAccountOrigin))
	        {
	        	financialTransferGroup.setBankAccountOriginId(Integer.valueOf(bankAccountOrigin));
	        }
	        final String bankAccountDestiny = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_DESTINY);
	        if(StringUtils.isLong(bankAccountDestiny))
	        {
	        	financialTransferGroup.setBankAccountDestinyId(Integer.valueOf(bankAccountDestiny));
	        }	        
	        String groupItemId = request.getParameter(ConstantDataManager.PARAMETER_GROUP_ITEM_ID);
	        if(!StringUtils.isLong(groupItemId))
	        {
	        	groupItemId = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
	        }
	        financialTransferGroup.setGroupItemId(Integer.valueOf(groupItemId));
	        
	        financialTransferGroup.validateFormEditGroupItemTransfer();
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}	
			
			financialTransferGroup.getItens().clear();
			financialTransferGroup.getItensList().clear();
			financialTransferGroup.getProducts();

			final FinancialTransferGroupService financialTransferGroupService = new FinancialTransferGroupServiceImpl(connection);						
			financialTransferGroupService.get(financialTransferGroup);
			
			status = true;
			result.put(ConstantDataManager.PARAMETER_FINANCIAL_TRANSFER, financialTransferGroup);
		} 
		catch (final AdminException ex)
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
		
		log.info(ConstantDataManager.FINANCIAL_TRANSFER_MAINTENANCE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD);
		
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
			
			final ReportFinancialTransferGroupDTO report = this.processFilter(request, connection);
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT, report);
			status = true;
		}
		catch (final AdminException ex)
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
	
	public ReportFinancialTransferGroupDTO processFilter(final HttpServletRequest request, final Connection connection) throws Exception
	{
		final ReportFinancialTransferGroupDTO report = new ReportFinancialTransferGroupDTO();
		try
		{	
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			report.setUserOperation(user.getId());

	        final String bankAccountOrigin = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_ORIGIN);
	        if(StringUtils.isLong(bankAccountOrigin)) {
	        	report.setBankAccountOriginId(Integer.valueOf(bankAccountOrigin));
	        }
	        final String bankAccountDestiny = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_DESTINY);
	        if(StringUtils.isLong(bankAccountDestiny))
	        {
	        	report.setBankAccountDestinyId(Integer.valueOf(bankAccountDestiny));	
	        }

	        String active = request.getParameter(ConstantDataManager.PARAMETER_INACTIVE);
	        if(!StringUtils.isLong(active))
			{
	        	active = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
	        
	        report.setInactive(Integer.parseInt(active));
			final FinancialTransferGroupService financialTransferGroupService = new FinancialTransferGroupServiceImpl(connection);						
			financialTransferGroupService.getAll(report);
		}
		catch(final Exception ex) 
		{
			throw ex;
		}		
		return report;
	}	
	
	@PostMapping(value=ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_CLONE_METHOD)
	public ResponseEntity<String> clone(final HttpServletRequest request) throws Exception
	{
		
		log.info(ConstantDataManager.FINANCIAL_TRANSFER_MAINTENANCE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_CLONE_METHOD);
		
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
		
		Connection connection = null;
		
		final FinancialTransferGroupDTO financialTransferGroup = new FinancialTransferGroupDTO();
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
	        final String bankAccountOrigin = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_ORIGIN);
	        if(StringUtils.isLong(bankAccountOrigin))
	        {
	        	financialTransferGroup.setBankAccountOriginId(Integer.valueOf(bankAccountOrigin));
	        }
	        final String bankAccountDestiny = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_DESTINY);
	        if(StringUtils.isLong(bankAccountDestiny))
	        {
	        	financialTransferGroup.setBankAccountDestinyId(Integer.valueOf(bankAccountDestiny));
	        }	        	        
			final ChangeData changeData = new ChangeData();
			changeData.setUserChange(user.getId());
			financialTransferGroup.setChangeData(changeData);
			financialTransferGroup.validateFormClone();
							
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final FinancialTransferGroupService financialTransferGroupService = new FinancialTransferGroupServiceImpl(connection);						
			financialTransferGroupService.clone(financialTransferGroup);
			//if(!isCloned)
			//{
			//	throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
			//}
			
			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
		}
		catch (final AdminException ex)
		{
			ex.printStackTrace();
			status = false;
			message =  ex.getMessage();
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			if(ex.getMessage()!=null && ex.getMessage().indexOf(com.manager.systems.web.common.utils.ConstantDataManager.EXCEPTION_DUPLICATE_ENTRY)>-1)
			{
				message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.FINANCIAL_TRANSFER_ITEM_ORDER_INVALID, new String[] {financialTransferGroup.getDescription()});
			}
			else
			{
				message =  ex.getMessage();				
			}
			status = false;
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