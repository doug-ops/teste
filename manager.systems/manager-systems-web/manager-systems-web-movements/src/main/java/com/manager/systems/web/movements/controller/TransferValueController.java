package com.manager.systems.web.movements.controller;

import java.sql.Connection;
import java.util.List;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manager.systems.common.dto.adm.ReportBankAccountDTO;
import com.manager.systems.common.dto.adm.ReportPersonDTO;
import com.manager.systems.common.service.adm.BankAccountService;
import com.manager.systems.common.service.adm.FinancialGroupService;
import com.manager.systems.common.service.adm.PersonService;
import com.manager.systems.common.service.adm.UserParentService;
import com.manager.systems.common.service.financialCostCenter.FinancialCostCenterService;
import com.manager.systems.common.service.financialCostCenter.impl.FinancialCostCenterServiceImpl;
import com.manager.systems.common.service.impl.adm.BankAccountServiceImpl;
import com.manager.systems.common.service.impl.adm.FinancialGroupServiceImpl;
import com.manager.systems.common.service.impl.adm.PersonServiceImpl;
import com.manager.systems.common.service.impl.adm.UserParentServiceImpl;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.ObjectType;
import com.manager.systems.model.admin.User;
import com.manager.systems.web.common.controller.BaseController;
import com.manager.systems.web.movements.utils.ConstantDataManager;

@Controller
@RequestMapping(value=ConstantDataManager.TRANSFER_VALUE_CONTROLLER)
public class TransferValueController extends BaseController
{
	
	private static final Log log = LogFactory.getLog(TransferValueController.class);
		
	@RequestMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD)
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		log.info(ConstantDataManager.TRANSFER_VALUE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD);
		
		String result = ConstantDataManager.DOCUMENT_MOVEMENT_OPEN_METHOD_DOCUMENT_TRANSFER_VALUE_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
		Connection connection = null;
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			if(!user.hasPermission("MENU_MOVIMENTOS", "MENU_MOVIMENTOS_TRANSF_SALDO")) {
				result = ConstantDataManager.RESULT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Transferencia de Saldo Mensal.");
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
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
			
			String companyIdParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_ID_TRANSFER);
			if(StringUtils.isNull(companyIdParameter))
			{
				companyIdParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;							
			}
			String companyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_TRANSFER);
			if(StringUtils.isNull(companyParameter))
			{
				companyParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;							
			}
			String paymentValueParameter = request.getParameter(ConstantDataManager.PARAMETER_PAYMENT_VALUE_TRANSFER);
			if(StringUtils.isNull(paymentValueParameter))
			{
				paymentValueParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;							
				request.setAttribute(ConstantDataManager.PARAMETER_IS_DOCUMENT_MOVEMENT, 0);
			} else {
				request.setAttribute(ConstantDataManager.PARAMETER_IS_DOCUMENT_MOVEMENT, 1);
			}
			String paymentExpiryDataParameter = request.getParameter(ConstantDataManager.PARAMETER_PAYMENT_EXPIRY_DATE_TRANSFER);
			if(StringUtils.isNull(paymentExpiryDataParameter))
			{
				paymentExpiryDataParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;							
			}
			String documentTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_TYPE_TRANSFER);
			if(StringUtils.isNull(documentTypeParameter))
			{
				documentTypeParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;							
			}
			model.addAttribute(ConstantDataManager.PARAMETER_COMPANY_ID_TRANSFER, StringUtils.decodeString(companyIdParameter).toUpperCase());
			model.addAttribute(ConstantDataManager.PARAMETER_COMPANY_TRANSFER, StringUtils.decodeString(companyParameter).toUpperCase());
			model.addAttribute(ConstantDataManager.PARAMETER_PAYMENT_VALUE_TRANSFER, StringUtils.decodeString(paymentValueParameter).toUpperCase());
			model.addAttribute(ConstantDataManager.PARAMETER_PAYMENT_EXPIRY_DATE_TRANSFER, StringUtils.decodeString(paymentExpiryDataParameter).toUpperCase());
			model.addAttribute(ConstantDataManager.PARAMETER_DOCUMENT_TYPE_TRANSFER, StringUtils.decodeString(paymentExpiryDataParameter).toUpperCase());
			model.addAttribute(ConstantDataManager.PARAMETER_DOCUMENT_ID, documentIdParameter);
			model.addAttribute(ConstantDataManager.PARAMETER_DOCUMENT_PARENT_ID, documentParentIdParameter);
			
			final BankAccountService bankAccountService = new BankAccountServiceImpl(connection);	
			
			final ReportBankAccountDTO reportBankAccount = new ReportBankAccountDTO();
			reportBankAccount.setUserOperation(user.getId());
			final List<Combobox> bankAccounts = bankAccountService.getAllCombobox(reportBankAccount);
			request.setAttribute(ConstantDataManager.PARAMETER_BANK_ACCOUNTS, bankAccounts);
			
			final FinancialGroupService financialGroupService = new FinancialGroupServiceImpl(connection);
			final List<Combobox> financialGroups = financialGroupService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_FINANCIAL_GROUPS, financialGroups);
			
			final UserParentService userParentService = new UserParentServiceImpl(connection);
			long userId = user.getId();
			final List<Combobox> userChidrensParent = userParentService.getAllParentCombobox(userId);
			request.setAttribute(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT, userChidrensParent);
			
			final FinancialCostCenterService financialCostCenterService = new FinancialCostCenterServiceImpl(connection);
			final List<Combobox> financialCostCenters = financialCostCenterService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_FINANCIAL_COST_CENTERS, financialCostCenters);
			

			final PersonService companyService = new PersonServiceImpl(connection);
			final ReportPersonDTO reportPerson = new ReportPersonDTO();
			reportPerson.setPersonIdFrom(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			reportPerson.setPersonIdTo(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			reportPerson.setInactive(com.manager.systems.common.utils.ConstantDataManager.FALSE_INT);
			reportPerson.setDescription(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			reportPerson.setObjectType(ObjectType.COMPANY.getType());
			reportPerson.setUserOperation(user.getId());
			final List<Combobox> companies = companyService.getAllCombobox(reportPerson);
			request.setAttribute(ConstantDataManager.PARAMETER_COMPANIES, companies);
			
			request.setAttribute(ConstantDataManager.PARAMETER_OBJECT_TYPE_ALIAS, com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_DOCUMENT_MOVEMENT);
			request.setAttribute(ConstantDataManager.PARAMETER_IS_MONTH, 0);
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
	
	@RequestMapping(value = ConstantDataManager.TRANSFER_VALUE_CONTROLLER_OPEN_MONTH_METHOD, method = {RequestMethod.GET, RequestMethod.POST})
	public String openMonth(final HttpServletRequest request, final Model model) throws Exception 
	{
		log.info(ConstantDataManager.TRANSFER_VALUE_CONTROLLER + ConstantDataManager.TRANSFER_VALUE_CONTROLLER_OPEN_MONTH_METHOD);
		
		String result = ConstantDataManager.DOCUMENT_MOVEMENT_OPEN_METHOD_DOCUMENT_TRANSFER_VALUE_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
		Connection connection = null;
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			if(!user.hasPermission("MENU_MOVIMENTOS", "MENU_MOVIMENTOS_TRANSF_SALDO_MENSAL")) {
				result = ConstantDataManager.RESULT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Transferencia de Saldo.");
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
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
			
			String companyIdParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_ID_TRANSFER);
			if(StringUtils.isNull(companyIdParameter))
			{
				companyIdParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;							
			}
			String companyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_TRANSFER);
			if(StringUtils.isNull(companyParameter))
			{
				companyParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;							
			}
			String paymentValueParameter = request.getParameter(ConstantDataManager.PARAMETER_PAYMENT_VALUE_TRANSFER);
			if(StringUtils.isNull(paymentValueParameter))
			{
				paymentValueParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;							
				request.setAttribute(ConstantDataManager.PARAMETER_IS_DOCUMENT_MOVEMENT, 0);
			} else {
				request.setAttribute(ConstantDataManager.PARAMETER_IS_DOCUMENT_MOVEMENT, 1);
			}
			String paymentExpiryDataParameter = request.getParameter(ConstantDataManager.PARAMETER_PAYMENT_EXPIRY_DATE_TRANSFER);
			if(StringUtils.isNull(paymentExpiryDataParameter))
			{
				paymentExpiryDataParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;							
			}
			String documentTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_TYPE_TRANSFER);
			if(StringUtils.isNull(documentTypeParameter))
			{
				documentTypeParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;							
			}
			model.addAttribute(ConstantDataManager.PARAMETER_COMPANY_ID_TRANSFER, StringUtils.decodeString(companyIdParameter).toUpperCase());
			model.addAttribute(ConstantDataManager.PARAMETER_COMPANY_TRANSFER, StringUtils.decodeString(companyParameter).toUpperCase());
			model.addAttribute(ConstantDataManager.PARAMETER_PAYMENT_VALUE_TRANSFER, StringUtils.decodeString(paymentValueParameter).toUpperCase());
			model.addAttribute(ConstantDataManager.PARAMETER_PAYMENT_EXPIRY_DATE_TRANSFER, StringUtils.decodeString(paymentExpiryDataParameter).toUpperCase());
			model.addAttribute(ConstantDataManager.PARAMETER_DOCUMENT_TYPE_TRANSFER, StringUtils.decodeString(paymentExpiryDataParameter).toUpperCase());
			model.addAttribute(ConstantDataManager.PARAMETER_DOCUMENT_ID, documentIdParameter);
			model.addAttribute(ConstantDataManager.PARAMETER_DOCUMENT_PARENT_ID, documentParentIdParameter);
			
			final BankAccountService bankAccountService = new BankAccountServiceImpl(connection);	
			
			final ReportBankAccountDTO reportBankAccount = new ReportBankAccountDTO();
			reportBankAccount.setUserOperation(user.getId());
			final List<Combobox> bankAccounts = bankAccountService.getAllCombobox(reportBankAccount);
			request.setAttribute(ConstantDataManager.PARAMETER_BANK_ACCOUNTS, bankAccounts);
			
			final FinancialGroupService financialGroupService = new FinancialGroupServiceImpl(connection);
			final List<Combobox> financialGroups = financialGroupService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_FINANCIAL_GROUPS, financialGroups);
			
			final UserParentService userParentService = new UserParentServiceImpl(connection);
			long userId = user.getId();
			final List<Combobox> userChidrensParent = userParentService.getAllParentCombobox(userId);
			request.setAttribute(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT, userChidrensParent);
			
			final FinancialCostCenterService financialCostCenterService = new FinancialCostCenterServiceImpl(connection);
			final List<Combobox> financialCostCenters = financialCostCenterService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_FINANCIAL_COST_CENTERS, financialCostCenters);
			

			final PersonService companyService = new PersonServiceImpl(connection);
			final ReportPersonDTO reportPerson = new ReportPersonDTO();
			reportPerson.setPersonIdFrom(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			reportPerson.setPersonIdTo(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			reportPerson.setInactive(com.manager.systems.common.utils.ConstantDataManager.FALSE_INT);
			reportPerson.setDescription(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			reportPerson.setObjectType(ObjectType.COMPANY.getType());
			reportPerson.setUserOperation(user.getId());
			final List<Combobox> companies = companyService.getAllCombobox(reportPerson);
			request.setAttribute(ConstantDataManager.PARAMETER_COMPANIES, companies);
			
			request.setAttribute(ConstantDataManager.PARAMETER_OBJECT_TYPE_ALIAS, com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_DOCUMENT_MOVEMENT);
			request.setAttribute(ConstantDataManager.PARAMETER_IS_MONTH, 1);
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
}
