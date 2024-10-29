package com.manager.systems.web.adm.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.manager.systems.common.dto.adm.BankAccountDTO;
import com.manager.systems.common.dto.adm.IntegrationSystemsDTO;
import com.manager.systems.common.dto.adm.PersonBankAccountDTO;
import com.manager.systems.common.dto.adm.ReportBankAccountDTO;
import com.manager.systems.common.service.IntegrationSystemsService;
import com.manager.systems.common.service.adm.BankAccountService;
import com.manager.systems.common.service.adm.BankService;
import com.manager.systems.common.service.adm.PersonBankAccountService;
import com.manager.systems.common.service.impl.BankServiceImpl;
import com.manager.systems.common.service.impl.IntegrationSystemsServiceImpl;
import com.manager.systems.common.service.impl.adm.BankAccountServiceImpl;
import com.manager.systems.common.service.impl.adm.PersonBankAccountServiceImpl;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.ObjectType;
import com.manager.systems.model.admin.User;
import com.manager.systems.web.adm.exception.AdminException;
import com.manager.systems.web.adm.utils.ConstantDataManager;
import com.manager.systems.web.common.controller.BaseController;

@Controller
@RequestMapping(value = ConstantDataManager.BANK_ACCOUNT_MAINTENANCE_CONTROLLER)
public class BankAccountMaintenanceController extends BaseController {
	
	private static final Log log = LogFactory.getLog(BankAccountMaintenanceController.class);
	
	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD)
	public String open(final HttpServletRequest request, final Model model) throws Exception {
		
		log.info(ConstantDataManager.BANK_ACCOUNT_MAINTENANCE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD);
		
		String result = ConstantDataManager.BANK_ACCOUNT_MAINTENANCE_OPEN_METHOD_RESULT;

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;

		Connection connection = null;

		try {
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			if(!user.hasPermission("MENU_FINANCEIRO", "MENU_FINANCEIRO_CONTA_BANCARIA")) {
				result = ConstantDataManager.RESULT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Financeiro Conta Bancaria.");
			}

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
			final List<Combobox> integrationsSystems = integrationSystemsService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_INTEGRATION_SYSTEMS, integrationsSystems);

			final BankService bankService = new BankServiceImpl(connection);
			final List<Combobox> banks = bankService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_BANKS, banks);
			request.setAttribute(ConstantDataManager.PARAMETER_OBJECT_TYPE_ALIAS, com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_BANK_ACCOUNT);
		} catch (final Exception e) {
			e.printStackTrace();
			message = e.getMessage();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE, message);
		return result;
	}

	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD)
	public ResponseEntity<String> save(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.BANK_ACCOUNT_MAINTENANCE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD);
		
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;

		Connection connection = null;

		try {
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final String bankAccountIdParameter = request.getParameter(ConstantDataManager.PARAMETER_ID);
			final String descriptionParameter = request.getParameter(ConstantDataManager.PARAMETER_DESCRIPTION);
			String bankBalanceAvailableParameter = request.getParameter(ConstantDataManager.PARAMETER_BANK_BALANCE_AVAILABLE);
			String bankLimitAvailableParameter = request.getParameter(ConstantDataManager.PARAMETER_BANK_LIMIT_AVAILABLE);
			final String bankCodeParameter = request.getParameter(ConstantDataManager.PARAMETER_BANK_CODE);
			final String bankAngencyParameter = request.getParameter(ConstantDataManager.PARAMETER_BANK_ANGENCY);
			final String[] integrationSystemValuesParameter = request.getParameterValues(ConstantDataManager.PARAMETER_INTEGRATION_SYSTEMS_VALUES);
			
			String accumulateBalanceParameter = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_ACCUMULATE_BALANCE);
			if (com.manager.systems.common.utils.ConstantDataManager.ON.equalsIgnoreCase(accumulateBalanceParameter)) {
				accumulateBalanceParameter = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
			} else {
				accumulateBalanceParameter = com.manager.systems.common.utils.ConstantDataManager.FALSE_STRING;
			}

			String activeParameter = request.getParameter(ConstantDataManager.PARAMETER_ACTIVE);
			if (com.manager.systems.common.utils.ConstantDataManager.ON.equalsIgnoreCase(activeParameter)) {
				activeParameter = com.manager.systems.common.utils.ConstantDataManager.FALSE_STRING;
			} else {
				activeParameter = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
			}

			if (!StringUtils.isNull(bankBalanceAvailableParameter)) {
				if (com.manager.systems.common.utils.ConstantDataManager.VIRGULA_ZERO_ZERO.equalsIgnoreCase(bankBalanceAvailableParameter)) {
					bankBalanceAvailableParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_VIRGULA_ZERO_ZERO;
				}
				bankBalanceAvailableParameter = StringUtils.replaceNonDigits(bankBalanceAvailableParameter);
				final StringBuilder resultMoeda = new StringBuilder(bankBalanceAvailableParameter);
				if (resultMoeda.length() > 2) {
					resultMoeda.insert(resultMoeda.length() - 2, com.manager.systems.common.utils.ConstantDataManager.PONTO);
				}
				bankBalanceAvailableParameter = resultMoeda.toString();
			} else {
				bankBalanceAvailableParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}

			if (!StringUtils.isNull(bankLimitAvailableParameter)) {
				if (com.manager.systems.common.utils.ConstantDataManager.VIRGULA_ZERO_ZERO.equalsIgnoreCase(bankLimitAvailableParameter)) {
					bankLimitAvailableParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_VIRGULA_ZERO_ZERO;
				}
				bankLimitAvailableParameter = StringUtils.replaceNonDigits(bankLimitAvailableParameter);
				final StringBuilder resultMoeda = new StringBuilder(bankLimitAvailableParameter);
				if (resultMoeda.length() > 2) {
					resultMoeda.insert(resultMoeda.length() - 2, com.manager.systems.common.utils.ConstantDataManager.PONTO);
				}
				bankLimitAvailableParameter = resultMoeda.toString();
			} else {
				bankLimitAvailableParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}

			final BankAccountDTO bankAccount = new BankAccountDTO();
			bankAccount.setId(bankAccountIdParameter);
			bankAccount.setDescription(descriptionParameter);
			bankAccount.setBankBalanceAvailable(Double.valueOf(bankBalanceAvailableParameter));
			bankAccount.setBankLimitAvailable(Double.valueOf(bankLimitAvailableParameter));
			bankAccount.setBankCode(bankCodeParameter);
			bankAccount.setBankAngency(bankAngencyParameter);
			bankAccount.setInactive(Boolean.valueOf(activeParameter));
			bankAccount.setAccumulateBalance(Boolean.valueOf(accumulateBalanceParameter));
			final ChangeData changeData = new ChangeData();
			changeData.setUserChange(user.getId());
			changeData.setChangeDate(Calendar.getInstance(tz).getTime());
			bankAccount.setChangeData(changeData);

			if (integrationSystemValuesParameter != null && integrationSystemValuesParameter.length > 0) {
				for (final String item : integrationSystemValuesParameter) {
					final String[] itemArray = item.split(com.manager.systems.common.utils.ConstantDataManager.PONTO_VIRGULA_STRING);
					if (itemArray.length == 2) {
						bankAccount.addIntegrationSystemsValue(itemArray[0], itemArray[1]);
					}
				}
			}

			final BankAccountService bankAccountService = new BankAccountServiceImpl(connection);
			final boolean isSaved = bankAccountService.save(bankAccount);

			if (!isSaved) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
			}

			final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
			final IntegrationSystemsDTO integrationSystem = new IntegrationSystemsDTO();
			integrationSystem.setObjectId(bankAccount.getId());
			integrationSystem.setObjectType(ObjectType.BANK_ACCOUNT.getType());
			integrationSystem.setInactive(true);
			integrationSystem.setUserChange(bankAccount.getChangeData().getUserChange());
			integrationSystem.setChangeDate(Calendar.getInstance(tz));
			integrationSystemsService.delete(integrationSystem);

			if (bankAccount.getIntegrationSystemsValues() != null && bankAccount.getIntegrationSystemsValues().size() > 0) {
				for (final IntegrationSystemsDTO item : bankAccount.getIntegrationSystemsValues()) {
					integrationSystem.setInactive(false);
					integrationSystem.setObjectType(ObjectType.BANK_ACCOUNT.getType());
					integrationSystemsService.save(item);
				}
			}

			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
		} catch (final AdminException ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} catch (final Exception ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}

	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_INACTIVE_METHOD)
	public ResponseEntity<String> inactive(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.BANK_ACCOUNT_MAINTENANCE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_INACTIVE_METHOD);
		
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;

		Connection connection = null;

		try {
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final String bankAccountIdParameter = request.getParameter(ConstantDataManager.PARAMETER_ID);
			final String inactiveParameter = request.getParameter(ConstantDataManager.PARAMETER_INACTIVE);

			if (!StringUtils.isLong(bankAccountIdParameter) || StringUtils.isNull(inactiveParameter)) {
				throw new AdminException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_DATA_REQUIRED_ERROR, null));
			}

			final BankAccountDTO bankAccount = new BankAccountDTO();
			bankAccount.setId(bankAccountIdParameter);
			bankAccount.setInactive(Boolean.valueOf(inactiveParameter));
			final ChangeData changeData = new ChangeData();
			changeData.setUserChange(user.getId());
			changeData.setChangeDate(Calendar.getInstance(tz).getTime());
			bankAccount.setChangeData(changeData);
			final BankAccountService bankAccountService = new BankAccountServiceImpl(connection);
			final boolean wasInactivated = bankAccountService.inactive(bankAccount);

			if (!wasInactivated) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
			}
			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
		} catch (final AdminException ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} catch (final Exception ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}

	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_DETAIL_METHOD)
	public ResponseEntity<String> detail(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.BANK_ACCOUNT_MAINTENANCE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_DETAIL_METHOD);
		
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;

		BankAccountDTO bankAccount = null;

		Connection connection = null;

		try {
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final String bankAccountIdParameter = request.getParameter(ConstantDataManager.PARAMETER_ID);
			bankAccount = new BankAccountDTO();
			bankAccount.setId(bankAccountIdParameter);

			final BankAccountService bankAccountService = new BankAccountServiceImpl(connection);
			bankAccountService.get(bankAccount);
			if (bankAccount.getDescription() == null) {
				throw new AdminException(this.messages.get(ConstantDataManager.MESSAGE_NOT_FOUND_BANK_ACCOUNT, null));
			}

			final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
			final IntegrationSystemsDTO integrationSystem = new IntegrationSystemsDTO();
			integrationSystem.setObjectId(bankAccount.getId());
			integrationSystem.setObjectType(ObjectType.BANK_ACCOUNT.getType());
			bankAccount.getIntegrationSystemsValues().addAll(integrationSystemsService.getAll(integrationSystem));

			status = true;
			result.put(ConstantDataManager.PARAMETER_BANK_ACCOUNT, bankAccount);
		} catch (final AdminException ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} catch (final Exception ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}

	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD)
	public ResponseEntity<String> filter(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.BANK_ACCOUNT_MAINTENANCE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD);
		
		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new Gson();

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;

		Connection connection = null;

		try {
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final ReportBankAccountDTO reportBankAccount = this.processFilter(request, connection);
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT, reportBankAccount);
			status = true;
		} catch (final AdminException ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} catch (final Exception ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}

	public ReportBankAccountDTO processFilter(final HttpServletRequest request, final Connection connection) throws Exception {
		final ReportBankAccountDTO reportBankAccount = new ReportBankAccountDTO();
		try {
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			String bankAccountIdFromParameter = request.getParameter(ConstantDataManager.PARAMETER_ID_FROM);
			if (!StringUtils.isLong(bankAccountIdFromParameter)) {
				bankAccountIdFromParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}

			String bankAccountIdToParameter = request.getParameter(ConstantDataManager.PARAMETER_ID_TO);
			if (!StringUtils.isLong(bankAccountIdToParameter)) {
				bankAccountIdToParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}

			String inactiveParameter = request.getParameter(ConstantDataManager.PARAMETER_INACTIVE);
			if (!StringUtils.isLong(inactiveParameter)) {
				inactiveParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}

			final String descriptionParameter = request.getParameter(ConstantDataManager.PARAMETER_DESCRIPTION);

			final String objectTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_OBJECT_TYPE);
			if (StringUtils.isNull(objectTypeParameter)) {
				throw new AdminException(this.messages.get(ConstantDataManager.MESSAGE_OBJECT_TYPE_NOT_INFORMED, null));
			}

			final ObjectType objectType = ObjectType.valueOfType(objectTypeParameter);
			if (objectType == null) {
				throw new AdminException(this.messages.get(ConstantDataManager.MESSAGE_OBJECT_TYPE_INVALID, null));
			}

			reportBankAccount.setBankAccountIdFrom(bankAccountIdFromParameter);
			reportBankAccount.setBankAccountIdTo(bankAccountIdToParameter);
			reportBankAccount.setInactive(inactiveParameter);
			reportBankAccount.setDescription(descriptionParameter);
			reportBankAccount.setObjectType(objectType.getType());
			reportBankAccount.setUserOperation(user.getId());

			final BankAccountService bankAccountService = new BankAccountServiceImpl(connection);
			bankAccountService.getAll(reportBankAccount);
		} catch (final Exception ex) {
			throw ex;
		}
		return reportBankAccount;
	}

	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_AUTOCOMPLETE_METHOD)
	public ResponseEntity<String> autocomplete(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.BANK_ACCOUNT_MAINTENANCE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_AUTOCOMPLETE_METHOD);
		
		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new Gson();

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;

		Connection connection = null;

		try {
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			String inactiveParameter = request.getParameter(ConstantDataManager.PARAMETER_INACTIVE);
			if (!StringUtils.isLong(inactiveParameter)) {
				inactiveParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}

			final String descriptionParameter = request.getParameter(ConstantDataManager.PARAMETER_DESCRIPTION);

			// 0 - all, 1 - true, 2 - false
			final String unboundBankAccountParameter = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_UNBOUND);
			String objectTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_OBJECT_TYPE);
			if (StringUtils.isNull(objectTypeParameter)) {
				objectTypeParameter = com.manager.systems.common.utils.ConstantDataManager.BLANK;
			}
			String personIdParameter = request.getParameter(ConstantDataManager.PARAMETER_ID);
			if (!StringUtils.isLong(personIdParameter)) {
				personIdParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}

			final ReportBankAccountDTO reportBankAccount = new ReportBankAccountDTO();
			reportBankAccount.setUserOperation(user.getId());
			reportBankAccount.setDescription(descriptionParameter);
			reportBankAccount.setInactive(inactiveParameter);
			if (StringUtils.isLong(unboundBankAccountParameter)) {
				reportBankAccount.setUnboundBankAccount(Integer.parseInt(unboundBankAccountParameter));
			}
			reportBankAccount.setObjectType(objectTypeParameter);
			reportBankAccount.setPersonId(Long.parseLong(personIdParameter));

			final BankAccountService bankAccountService = new BankAccountServiceImpl(connection);
			final List<Combobox> items = bankAccountService.getAllAutocomplete(reportBankAccount);

			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT, items);
			status = true;
		} catch (final AdminException ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} catch (final Exception ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}
	
	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_AUTOCOMPLETE_DESCRIPTION_METHOD)
	public ResponseEntity<String> autocompleteByDescription(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.BANK_ACCOUNT_MAINTENANCE_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_AUTOCOMPLETE_METHOD);
		
		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new Gson();

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;

		Connection connection = null;

		try {
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			String inactiveParameter = request.getParameter(ConstantDataManager.PARAMETER_INACTIVE);
			if (!StringUtils.isLong(inactiveParameter)) {
				inactiveParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}

			final String descriptionParameter = request.getParameter(ConstantDataManager.PARAMETER_DESCRIPTION);

			// 0 - all, 1 - true, 2 - false
			final String unboundBankAccountParameter = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_UNBOUND);
			String objectTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_OBJECT_TYPE);
			if (StringUtils.isNull(objectTypeParameter)) {
				objectTypeParameter = com.manager.systems.common.utils.ConstantDataManager.BLANK;
			}
			String personIdParameter = request.getParameter(ConstantDataManager.PARAMETER_ID);
			if (!StringUtils.isLong(personIdParameter)) {
				personIdParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}

			final ReportBankAccountDTO reportBankAccount = new ReportBankAccountDTO();
			reportBankAccount.setUserOperation(user.getId());
			reportBankAccount.setDescription(descriptionParameter);
			reportBankAccount.setInactive(inactiveParameter);
			if (StringUtils.isLong(unboundBankAccountParameter)) {
				reportBankAccount.setUnboundBankAccount(Integer.parseInt(unboundBankAccountParameter));
			}
			reportBankAccount.setObjectType(objectTypeParameter);
			reportBankAccount.setPersonId(Long.parseLong(personIdParameter));

			final BankAccountService bankAccountService = new BankAccountServiceImpl(connection);
			final List<Combobox> items = bankAccountService.getAllAutocomplete(reportBankAccount);

			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT, items);
			status = true;
		} catch (final AdminException ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} catch (final Exception ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}
	
	@PostMapping(value = ConstantDataManager.BANK_ACCOUNT_MAINTENANCE_GET_COMBOBOX_BY_PERSON_METHOD)
	public ResponseEntity<String> getAllComoboboxByPerson(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.BANK_ACCOUNT_MAINTENANCE_CONTROLLER + ConstantDataManager.BANK_ACCOUNT_MAINTENANCE_GET_COMBOBOX_BY_PERSON_METHOD);
		
		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new Gson();

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;

		Connection connection = null;

		try {

			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}

			String inactiveParameter = request.getParameter(ConstantDataManager.PARAMETER_INACTIVE);
			if (!StringUtils.isLong(inactiveParameter)) {
				inactiveParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			
			final String personIdsParameter = request.getParameter(ConstantDataManager.PARAMETER_PERSONS_ID);
			if (StringUtils.isNull(personIdsParameter)) {
				throw new AdminException(this.messages.get(ConstantDataManager.MESSAGE_INVALID_PERSON_ID, null));
			}
			final String objectTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_OBJECT_TYPE);
			if (StringUtils.isNull(objectTypeParameter)) {
				throw new AdminException(this.messages.get(ConstantDataManager.MESSAGE_OBJECT_TYPE_NOT_INFORMED, null));
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final PersonBankAccountService personBankAccountService = new PersonBankAccountServiceImpl(connection);
			final List<Combobox> bankAccounts = new ArrayList<Combobox>();
			
			final String origin = request.getParameter(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_ORIGIN);
						
			final PersonBankAccountDTO bankFilter = new PersonBankAccountDTO();
			for(final String id : personIdsParameter.split(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING)) {
				bankFilter.setObjectId(Long.parseLong(id));
				bankFilter.setObjectType(objectTypeParameter);
				bankFilter.setInactive(false);
				bankFilter.setUserChange(user.getId());
				bankFilter.setOrigin(origin);

				bankAccounts.addAll(personBankAccountService.getAllBankAccountByPerson(bankFilter));			
			}
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_ITENS, bankAccounts);
			status = true;
		} catch (final AdminException ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} catch (final Exception ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}
	
	@GetMapping(value = ConstantDataManager.BANK_ACCOUNT_MAINTENANCE_GET_COMBOBOX_BY_COMPANYS_METHOD)
	public ResponseEntity<String> getAllComoboboxByCompanys(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.BANK_ACCOUNT_MAINTENANCE_CONTROLLER + ConstantDataManager.BANK_ACCOUNT_MAINTENANCE_GET_COMBOBOX_BY_COMPANYS_METHOD);
		
		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new Gson();

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;

		Connection connection = null;

		try {
			
			final String companyIdsParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_ID);
			if (StringUtils.isNull(companyIdsParameter)) {
				throw new AdminException(this.messages.get(ConstantDataManager.MESSAGE_INVALID_PERSON_ID, null));
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final PersonBankAccountService personBankAccountService = new PersonBankAccountServiceImpl(connection);
			
			final Map<Long, List<Combobox>> banksAccount = personBankAccountService.getAllBankAccountByCompanys(companyIdsParameter); 
			
			final Optional<List<Combobox>> filtereds = banksAccount.entrySet().stream().filter(map -> map.getValue() != null && map.getValue().size() > 1).map(map -> map.getValue()).findAny();
						

			final List<Combobox> banksAccountResult = new ArrayList<Combobox>();
			if(filtereds.isPresent()) {
				banksAccountResult.addAll(filtereds.get());
			}
			
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_ITENS, banksAccountResult);
			
			status = true;
		} catch (final AdminException ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} catch (final Exception ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}
}
