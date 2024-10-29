package com.manager.systems.web.adm.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.google.gson.Gson;
import com.manager.systems.common.dto.adm.CompanyDTO;
import com.manager.systems.common.dto.adm.IntegrationSystemsDTO;
import com.manager.systems.common.dto.adm.PersonBankAccountDTO;
import com.manager.systems.common.dto.adm.PersonBankAccountPermissionDTO;
import com.manager.systems.common.dto.adm.PersonDTO;
import com.manager.systems.common.dto.adm.ReportAccessProfileDTO;
import com.manager.systems.common.dto.adm.ReportBankAccountDTO;
import com.manager.systems.common.dto.adm.ReportPersonDTO;
import com.manager.systems.common.dto.adm.UserCompnayDTO;
import com.manager.systems.common.dto.adm.UserDTO;
import com.manager.systems.common.service.IntegrationSystemsService;
import com.manager.systems.common.service.adm.AccessProfileService;
import com.manager.systems.common.service.adm.BankAccountService;
import com.manager.systems.common.service.adm.EstablishmentTypeService;
import com.manager.systems.common.service.adm.FinancialGroupService;
import com.manager.systems.common.service.adm.PersonBankAccountService;
import com.manager.systems.common.service.adm.PersonService;
import com.manager.systems.common.service.adm.PersonTypeService;
import com.manager.systems.common.service.adm.ProductService;
import com.manager.systems.common.service.adm.UserParentService;
import com.manager.systems.common.service.financialCostCenter.FinancialCostCenterService;
import com.manager.systems.common.service.financialCostCenter.impl.FinancialCostCenterServiceImpl;
import com.manager.systems.common.service.impl.IntegrationSystemsServiceImpl;
import com.manager.systems.common.service.impl.adm.AccessProfileServiceImpl;
import com.manager.systems.common.service.impl.adm.BankAccountServiceImpl;
import com.manager.systems.common.service.impl.adm.EstablishmentTypeServiceImpl;
import com.manager.systems.common.service.impl.adm.FinancialGroupServiceImpl;
import com.manager.systems.common.service.impl.adm.PersonBankAccountServiceImpl;
import com.manager.systems.common.service.impl.adm.PersonServiceImpl;
import com.manager.systems.common.service.impl.adm.PersonTypeServiceImpl;
import com.manager.systems.common.service.impl.adm.ProductServiceImpl;
import com.manager.systems.common.service.impl.adm.UserParentServiceImpl;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.ObjectType;
import com.manager.systems.model.admin.User;
import com.manager.systems.web.adm.exception.AdminException;
import com.manager.systems.web.adm.utils.ConstantDataManager;
import com.manager.systems.web.common.controller.BaseController;

@Controller
@RequestMapping(value = ConstantDataManager.PERSON_MAINTENANCE_CONTROLLER)
public class PersonMaintenanceController extends BaseController {
	
	private static final Log log = LogFactory.getLog(PersonMaintenanceController.class);
	
	@GetMapping(value = ConstantDataManager.PERSON_MAINTENANCE_OPEN_METHOD_METHOD)
	public String open(@PathVariable(ConstantDataManager.PARAMETER_OBJECT_TYPE) final int objectType, final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.PERSON_MAINTENANCE_OPEN_METHOD_METHOD + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD);
		
		String result = ConstantDataManager.PERSON_MAINTENANCE_OPEN_METHOD_RESULT;

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;

		Connection connection = null;

		try {
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			String objectTypeAlias = com.manager.systems.common.utils.ConstantDataManager.BLANK;
			switch (objectType) {
			case 1:
				if(!user.hasPermission("MENU_CADASTROS", "MENU_CADASTROS_EMPRESA")) {
					result = ConstantDataManager.RESULT_HOME;
					throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Empresa.");
				}
				objectTypeAlias = com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_COMPANY;
				break;
			case 2:
				if(!user.hasPermission("MENU_CADASTROS", "MENU_CADASTROS_FORNECEDOR")) {
					result = ConstantDataManager.RESULT_HOME;
					throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Fornecedor.");
				}
				objectTypeAlias = com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_PROVIDER;
				break;
			case 3:
				if(!user.hasPermission("MENU_CADASTROS", "MENU_CADASTROS_USUARIO")) {
					result = ConstantDataManager.RESULT_HOME;
					throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Usuario.");
				}
				objectTypeAlias = com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_USER;
				break;
			default:
				break;
			}

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final BankAccountService bankAccountService = new BankAccountServiceImpl(connection);
			
			final ReportBankAccountDTO reportBankAccount = new ReportBankAccountDTO();
			reportBankAccount.setUserOperation(user.getId());
			final List<Combobox> bankAccounts = bankAccountService.getAllCombobox(reportBankAccount);
			request.setAttribute(ConstantDataManager.PARAMETER_BANK_ACCOUNTS, bankAccounts);

			final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
			final List<Combobox> integrationsSystems = integrationSystemsService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_INTEGRATION_SYSTEMS, integrationsSystems);

			final FinancialGroupService financialGroupService = new FinancialGroupServiceImpl(connection);
			final List<Combobox> financialGroups = financialGroupService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_FINANCIAL_GROUPS, financialGroups);
			
			final PersonTypeService personTypeService = new PersonTypeServiceImpl(connection);
			
			final List<Combobox> personTypes = personTypeService.getAllCombobox(objectTypeAlias);
			request.setAttribute(ConstantDataManager.PARAMETER_PERSON_TYPES, personTypes);
			
			if (1 == objectType) 
			{

				final EstablishmentTypeService establishmentTypeService = new EstablishmentTypeServiceImpl(connection);
				final List<Combobox> establishmentTypes = establishmentTypeService.getAllCombobox();
				request.setAttribute(ConstantDataManager.PARAMETER_ESTABLISHMENT_TYPES, establishmentTypes);
								
				final FinancialCostCenterService financialCostCenterService = new FinancialCostCenterServiceImpl(connection);
				final List<Combobox> financialCostCenters = financialCostCenterService.getAllCombobox();
				request.setAttribute(ConstantDataManager.PARAMETER_FINANCIAL_COST_CENTERS, financialCostCenters);
			}
									
			if (1 == objectType || 3 == objectType) 
			{
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
				
				if (3 == objectType) 
				{
					final String companysId =  companies.parallelStream().map(Combobox::getKey).collect(Collectors.joining(","));
					if(!StringUtils.isNull(companysId)) {
						final PersonBankAccountService personBankAccountService = new PersonBankAccountServiceImpl(connection);
						final Map<Long, List<Combobox>> banksAccount = personBankAccountService.getAllBankAccountByCompanys(companysId); 
						
						final Optional<List<Combobox>> filtereds = banksAccount.entrySet().stream().filter(map -> map.getValue() != null && map.getValue().size() > 1).map(map -> map.getValue()).findAny();
									

						final List<Combobox> banksAccountResult = new ArrayList<Combobox>();
						if(filtereds.isPresent()) {
							banksAccountResult.addAll(filtereds.get());
						}
						
						request.setAttribute(ConstantDataManager.PARAMETER_BANK_ACCOUNT_COMPANY_PERMISSION, banksAccountResult);					
					}					
				}
				
				final UserParentService userParentService = new UserParentServiceImpl(connection);
				final List<Combobox> userChidrensParent = userParentService.getAllParentCombobox(user.getId());
				request.setAttribute(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT, userChidrensParent);
			}
			
			if (1 == objectType || 3 == objectType) 
			{
				if(3 == objectType) {
					final ReportAccessProfileDTO reportAccessProfile = new ReportAccessProfileDTO();
					final AccessProfileService accessProfileService = new AccessProfileServiceImpl(connection);
					final List<Combobox> accessProfiles = accessProfileService.getAllCombobox(reportAccessProfile);
					request.setAttribute(ConstantDataManager.PARAMETER_ACCES_PROFILES, accessProfiles);		
					
					final UserParentService userParentService = new UserParentServiceImpl(connection);
					final List<Combobox> users = userParentService.getUserParentCombobox(user.getId());
					request.setAttribute(ConstantDataManager.PARAMETER_USERS, users);
				}
				else {
					final UserParentService userParentService = new UserParentServiceImpl(connection);
					final List<Combobox> users = userParentService.getUserComboboxByClient(user.getId());
					request.setAttribute(ConstantDataManager.PARAMETER_USERS, users);
				}
			}

			this.processLabelsPage(objectType, request);
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

	@Override
	public void processLabelsPage(final int objectType, final HttpServletRequest request) {
		final Map<String, String> labelsPage = new TreeMap<String, String>();

		String objectTypeLabel = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		String objectTypeAlias = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		switch (objectType) {
		case 1:
			objectTypeLabel = this.messages.get(com.manager.systems.web.adm.utils.ConstantDataManager.COMPANY_LABEL, null);
			objectTypeAlias = com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_COMPANY;
			break;
		case 2:
			objectTypeLabel = this.messages.get(com.manager.systems.web.adm.utils.ConstantDataManager.PROVIDER_LABEL, null);
			objectTypeAlias = com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_PROVIDER;
			break;
		case 3:
			objectTypeLabel = this.messages.get(com.manager.systems.web.adm.utils.ConstantDataManager.USER_LABEL, null);
			objectTypeAlias = com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_USER;
			break;
		default:
			break;
		}

		labelsPage.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_PAGE_TITLE, (this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LABEL_COMMON_MAINTENANCE, null) + com.manager.systems.web.common.utils.ConstantDataManager.SPACE
				+ this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LABEL_COMMON_OF, null) + com.manager.systems.web.common.utils.ConstantDataManager.SPACE + objectTypeLabel));

		request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_LABELS_PAGE, labelsPage);
		request.setAttribute(ConstantDataManager.PARAMETER_OBJECT_TYPE_ALIAS, objectTypeAlias);
	}

	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD)
	public ResponseEntity<String> save(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.PERSON_MAINTENANCE_OPEN_METHOD_METHOD + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD);
		
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

			final String personIdParameter = request.getParameter(ConstantDataManager.PARAMETER_ID);
			final String nameParameter = request.getParameter(ConstantDataManager.PARAMETER_NAME);
			final String aliasNameParameter = request.getParameter(ConstantDataManager.PARAMETER_ALIAS_NAME);
			String personTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_PERSON_TYPE);
			final String objectTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_OBJECT_TYPE);
			final String cpfCnpjParameter = request.getParameter(ConstantDataManager.PARAMETER_CPF_CNPJ);
			final String emailParameter = request.getParameter(ConstantDataManager.PARAMETER_EMAIL);
			final String addressZipCodeParameter = request.getParameter(ConstantDataManager.PARAMETER_ADRESS_ZIP_CODE);
			final String addressZipCodeSideParameter = request.getParameter(ConstantDataManager.PARAMETER_ADRESS_ZIP_CODE_SIDE);
			final String addressStreetParameter = request.getParameter(ConstantDataManager.PARAMETER_ADRESS_STREET);
			final String addressStreetNumberParameter = request.getParameter(ConstantDataManager.PARAMETER_ADRESS_STREET_NUMBER);
			final String addressStreetComplementParameter = request.getParameter(ConstantDataManager.PARAMETER_ADRESS_STREET_COMPLEMENT);
			final String addressDistrictParameter = request.getParameter(ConstantDataManager.PARAMETER_ADRESS_DISTRICT);
			final String addressStateIbgeParameter = request.getParameter(ConstantDataManager.PARAMETER_ADRESS_STATE_IBGE);
			final String addressCityIbgeParameter = request.getParameter(ConstantDataManager.PARAMETER_ADRESS_CITY_IBGE);
			final String companyDestinyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_DESTINY);
		
			String addressCountryIbgeParameter = request.getParameter(ConstantDataManager.PARAMETER_ADRESS_COUNTRY);
			if (!StringUtils.isLong(addressCountryIbgeParameter)) {
				addressCountryIbgeParameter = com.manager.systems.common.utils.ConstantDataManager.IBGE_CODE_COUNTRY_BRASIL;
			}

			String activeParameter = request.getParameter(ConstantDataManager.PARAMETER_ACTIVE);
			if (com.manager.systems.common.utils.ConstantDataManager.ON.equalsIgnoreCase(activeParameter)) {
				activeParameter = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
			} else {
				activeParameter = com.manager.systems.common.utils.ConstantDataManager.FALSE_STRING;
			}

			String negativeCloseParameter = request.getParameter(ConstantDataManager.PARAMETER_NEGATIVE_CLOSE);
			if (com.manager.systems.common.utils.ConstantDataManager.ON.equalsIgnoreCase(negativeCloseParameter)) {
				negativeCloseParameter = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
			} else {
				negativeCloseParameter = com.manager.systems.common.utils.ConstantDataManager.FALSE_STRING;
			}

			String movementAutomaticParameter = request.getParameter(ConstantDataManager.PARAMETER_MOVEMENT_AUTOMATIC);
			if (com.manager.systems.common.utils.ConstantDataManager.ON.equalsIgnoreCase(movementAutomaticParameter)) {
				movementAutomaticParameter = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
			} else {
				movementAutomaticParameter = com.manager.systems.common.utils.ConstantDataManager.FALSE_STRING;
			}

			final String[] integrationSystemValuesParameter = request.getParameterValues(ConstantDataManager.PARAMETER_INTEGRATION_SYSTEMS_VALUES);

			final String accessDataUserParameter = request.getParameter(ConstantDataManager.PARAMETER_ACCESS_DATA_USER);
			final String accessDataPasswordParameter = request.getParameter(ConstantDataManager.PARAMETER_ACCESS_DATA_PASSWORD);
			String bankAccountParameter = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT);
			if (!StringUtils.isLong(bankAccountParameter)) {
				bankAccountParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			String creditProviderParameter = request.getParameter(ConstantDataManager.PARAMETER_CREDIT_PROVIDER);
			if (!StringUtils.isLong(creditProviderParameter)) {
				creditProviderParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			String debitProviderParameter = request.getParameter(ConstantDataManager.PARAMETER_DEBIT_PROVIDER);
			if (!StringUtils.isLong(debitProviderParameter)) {
				debitProviderParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}

			String financialGroupParameter = request.getParameter(ConstantDataManager.PARAMETER_FINANCIAL_GROUP_ID);
			//if (StringUtils.isNull(financialGroupParameter)) {
			//	financialGroupParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			//}
			String financialSubGroupParameter = request.getParameter(ConstantDataManager.PARAMETER_FINANCIAL_SUB_GROUP_ID);
			//if (StringUtils.isNull(financialSubGroupParameter)) {
			//	financialSubGroupParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			//}

			final String[] companysParameter = request.getParameterValues(ConstantDataManager.PARAMETER_COMPANY);
			String accessProfileParameter = request.getParameter(ConstantDataManager.PARAMETER_ACCES_PROFILE);
			if (!StringUtils.isLong(accessProfileParameter)) {
				accessProfileParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}

			String registerTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_REGISTER_TYPE);
			if (!StringUtils.isLong(registerTypeParameter)) {
				registerTypeParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}

			String personTypeIdParameter = request.getParameter(ConstantDataManager.PARAMETER_PERSON_TYPE_ID);

			if (!StringUtils.isLong(personTypeIdParameter)) {
				personTypeIdParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}

			String establishmentTypeIdParameter = request.getParameter(ConstantDataManager.PARAMETER_ESTABLISHMENT_TYPE_ID);
			if (!StringUtils.isLong(establishmentTypeIdParameter)) {
				establishmentTypeIdParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			
			if(ObjectType.COMPANY.getType().equalsIgnoreCase(objectTypeParameter) && establishmentTypeIdParameter.equalsIgnoreCase(com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING)) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_ESTABLISHMENT_TYPE_INVALID, null));
			}

			final String[] bankAccountOthersParameter = request.getParameterValues(ConstantDataManager.PARAMETER_BANK_ACCOUNT_OTHERS);
			final String[] usersParameter = request.getParameterValues(ConstantDataManager.PARAMETER_USER_CHILDRENS_PARENT_COMBOBOX);
			final String[] usersCompanyCombobox = request.getParameterValues(ConstantDataManager.PARAMETER_USERS_COMPANY_COMBOBOX);
			
			String blockedParameter = request.getParameter(ConstantDataManager.PARAMETER_BLOCKED);
			if (com.manager.systems.common.utils.ConstantDataManager.ON.equalsIgnoreCase(blockedParameter)) {
				blockedParameter = com.manager.systems.common.utils.ConstantDataManager.TRUE_STRING;
			} else {
				blockedParameter = com.manager.systems.common.utils.ConstantDataManager.FALSE_STRING;
			}
			
			if(ObjectType.USER.getType().equalsIgnoreCase(objectTypeParameter)) {
				personTypeParameter = ConstantDataManager.PERSON_TYPE_USER;
			} else if(personTypeParameter == null) {
				personTypeParameter = ConstantDataManager.PERSON_TYPE_COMPANY;
			}
			
			String mainCompanyIdParameter = request.getParameter(ConstantDataManager.PARAMETER_MAIN_COMPANY_BANK_ACCOUNT_ID);
			if (StringUtils.isNull(mainCompanyIdParameter)) {
				mainCompanyIdParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			
			final String[] bankAccountsCompanyParameter = request.getParameterValues(ConstantDataManager.PARAMETER_BANK_ACCOUNTS_COMPANY);
			
			String cashingClosingMaxDiscountParameter = request.getParameter(ConstantDataManager.PARAMETER_CASHING_CLOSING_MAX_DISCOUNT);
			
			if(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_ZERO_ZERO.equalsIgnoreCase(cashingClosingMaxDiscountParameter))
			{
				cashingClosingMaxDiscountParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_VIRGULA_ZERO_ZERO;
			}
			cashingClosingMaxDiscountParameter = StringUtils.replaceNonDigits(cashingClosingMaxDiscountParameter);
			final StringBuilder resultMoeda = new StringBuilder(cashingClosingMaxDiscountParameter);
			if(resultMoeda.length()>2)
			{
				resultMoeda.insert(resultMoeda.length() - 2, com.manager.systems.common.utils.ConstantDataManager.PONTO);
			}						
			cashingClosingMaxDiscountParameter = resultMoeda.toString(); 
			if(StringUtils.isNull(cashingClosingMaxDiscountParameter)) {
				cashingClosingMaxDiscountParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			
			String financialCostCenterParameterTransf = request.getParameter(ConstantDataManager.PARAMETER_FINANCIAL_COST_CENTER_TRANSFER);	
			
			if(StringUtils.isNull(financialCostCenterParameterTransf))
			{
				financialCostCenterParameterTransf = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			
			final PersonDTO person = new PersonDTO();
			person.setPersonId(personIdParameter);
			person.setName(nameParameter);
			person.setAliasName(aliasNameParameter);
			person.setAccessDataUser(accessDataUserParameter);
			person.setAccessDataPassword(accessDataPasswordParameter);
			person.setPersonType(personTypeParameter);
			person.setObjectType(objectTypeParameter);
			person.setCpfCnpj(cpfCnpjParameter);
			person.setEmail(emailParameter);
			person.setAddressZipCode(addressZipCodeParameter);
			person.setAddressZipCodeSide(addressZipCodeSideParameter);
			person.setAddressStreet(addressStreetParameter);
			person.setAddressNumber(addressStreetNumberParameter);
			person.setAddressComplement(addressStreetComplementParameter);
			person.setAddressDistrict(addressDistrictParameter);
			person.setAddressStateIbge(addressStateIbgeParameter);
			person.setAddressCityIbge(addressCityIbgeParameter);
			person.setAddressCountryIbge(addressCountryIbgeParameter);
			person.setInactive(Boolean.valueOf(activeParameter));
			person.setNegativeClose(Boolean.valueOf(negativeCloseParameter));
			person.setMovementAutomatic(Boolean.valueOf(movementAutomaticParameter));
			person.setBankAccount(Long.valueOf(bankAccountParameter));
			person.setCreditProvider(Long.valueOf(creditProviderParameter));
			person.setDebitProvider(Long.valueOf(debitProviderParameter));
			person.setFinancialGroup(financialGroupParameter);
			person.setFinancialSubGroup(financialSubGroupParameter);
			person.setPersonTypeId(Integer.valueOf(personTypeIdParameter));
			person.setEstablishmentTypeId(Integer.valueOf(establishmentTypeIdParameter));
			person.setBlocked(Boolean.valueOf(blockedParameter));
			person.setMainCompanyId(Long.valueOf(mainCompanyIdParameter));
			if (companysParameter != null && companysParameter.length > 0) {
				for (final String item : companysParameter) {
					if (StringUtils.isLong(item)) {
						final CompanyDTO company = new CompanyDTO();
						company.setId(Long.valueOf(item));
						person.addCompnay(company);
					}
				}
			}
			
			if (bankAccountsCompanyParameter != null && bankAccountsCompanyParameter.length > 0) {
				for (final String item : bankAccountsCompanyParameter) {
					if (StringUtils.isLong(item)) {
						final PersonBankAccountPermissionDTO permission = new PersonBankAccountPermissionDTO();
						permission.setBankAccountId(Integer.valueOf(item));
						permission.setInactive(false);
						permission.setUserId(Long.valueOf(personIdParameter));
						permission.setUserChange(user.getId());
						person.addPersonBankAccountPermission(permission);
					}
				}
			}
			
			person.setAccessProfileId(Integer.parseInt(accessProfileParameter));
			person.setRegisterType(Integer.parseInt(registerTypeParameter));
			if (bankAccountOthersParameter != null && bankAccountOthersParameter.length > 0) {
				for (final String item : bankAccountOthersParameter) {
					if (StringUtils.isLong(item)) {
						final PersonBankAccountDTO bank = new PersonBankAccountDTO();
						bank.setBankAccountId(Integer.parseInt(item));
						person.addBankAccountOthers(bank);
					}
				}
			}
			person.setUserChange(user.getId());
			person.setChangeDate(Calendar.getInstance(tz));
			final Calendar expirationDate = Calendar.getInstance(tz);
			expirationDate.add(Calendar.YEAR, 2);
			person.setExpirationDate(expirationDate);
			person.setLastAccessDate(Calendar.getInstance(tz));
			person.validateFormSave(objectTypeParameter);

			if (integrationSystemValuesParameter != null && integrationSystemValuesParameter.length > 0) {
				for (final String item : integrationSystemValuesParameter) {
					final String[] itemArray = item.split(com.manager.systems.common.utils.ConstantDataManager.PONTO_VIRGULA_STRING);
					if (itemArray.length == 2) {
						person.addIntegrationSystemsValue(itemArray[0], itemArray[1]);
					}
				}
			}			
			
			if (usersParameter != null && usersParameter.length > 0) {
				for (final String item : usersParameter) {
					if (StringUtils.isLong(item)) {
						final UserDTO userDto = new UserDTO();
						userDto.setId(Long.valueOf(item));
						userDto.setUserOperation(Long.valueOf(person.getPersonId()));
						person.addUserParent(userDto);
					}
				}
			}
			
			if (usersCompanyCombobox != null && usersCompanyCombobox.length > 0) {
				for (final String item : usersCompanyCombobox) {
					if (StringUtils.isLong(item)) {
						final UserCompnayDTO userCompnay = new UserCompnayDTO();
						userCompnay.setUserId(Long.valueOf(item));
						userCompnay.setCompanyId(Long.valueOf(person.getPersonId()));
						userCompnay.setUserChange(user.getId());
						person.addUserCompany(userCompnay);
					}
				}
			}
			
			person.setCashClosingMaxDiscount(Double.valueOf(cashingClosingMaxDiscountParameter));
			person.setClientId(user.getClientId());
			person.setFinancialCostCenterId(Integer.parseInt(financialCostCenterParameterTransf));
			
			final PersonService personService = new PersonServiceImpl(connection);
			final boolean isSaved = personService.save(person);

			if (!isSaved) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
			}

			final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
			final IntegrationSystemsDTO integrationSystem = new IntegrationSystemsDTO();
			integrationSystem.setObjectId(person.getPersonId());
			integrationSystem.setObjectType(objectTypeParameter);
			integrationSystem.setInactive(true);
			integrationSystem.setUserChange(person.getUserChange());
			integrationSystem.setChangeDate(person.getChangeDate());
			integrationSystemsService.delete(integrationSystem);

			if (person.getIntegrationSystemsValues() != null && person.getIntegrationSystemsValues().size() > 0) {
				for (final IntegrationSystemsDTO item : person.getIntegrationSystemsValues()) {
					integrationSystem.setInactive(false);
					integrationSystem.setObjectType(objectTypeParameter);
					integrationSystemsService.save(item);
				}
			}

			int companyDestiny = 0;
			if(StringUtils.isLong(companyDestinyParameter)) {
				companyDestiny = Integer.valueOf(companyDestinyParameter);
			}
			
			if(com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_COMPANY.equalsIgnoreCase(objectTypeParameter) && !person.isInactive() && companyDestiny > 0) {
				final ProductService productService = new ProductServiceImpl(connection);
				
				productService.transferProductsToCompany(Integer.parseInt(person.getPersonId()), companyDestiny);				
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
		
		log.info(ConstantDataManager.PERSON_MAINTENANCE_OPEN_METHOD_METHOD + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_INACTIVE_METHOD);
		
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

			final String personIdParameter = request.getParameter(ConstantDataManager.PARAMETER_ID);
			final String inactiveParameter = request.getParameter(ConstantDataManager.PARAMETER_INACTIVE);
			final String objectTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_OBJECT_TYPE);
			final String companyDestinyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_DESTINY);

			ObjectType objectType = null;
			if (!StringUtils.isNull(objectTypeParameter)) {
				objectType = ObjectType.valueOfType(objectTypeParameter);
			}

			if (!StringUtils.isLong(personIdParameter) || StringUtils.isNull(inactiveParameter) || objectType == null) {
				throw new AdminException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_DATA_REQUIRED_ERROR, null));
			}

			final PersonDTO person = new PersonDTO();
			person.setPersonId(personIdParameter);
			person.setInactive(Boolean.valueOf(inactiveParameter));
			person.setObjectType(objectType.getType());
			final PersonService personService = new PersonServiceImpl(connection);
			final boolean wasInactivated = personService.inactive(person);

			if (!wasInactivated) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
			}
			
			int companyDestiny = 0;
			if(StringUtils.isLong(companyDestinyParameter)) {
				companyDestiny = Integer.valueOf(companyDestinyParameter);
			}
			
			if(com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_COMPANY.equalsIgnoreCase(objectTypeParameter) && person.isInactive() && companyDestiny > 0) {
				final ProductService productService = new ProductServiceImpl(connection);
				
				productService.transferProductsToCompany(Integer.parseInt(person.getPersonId()), companyDestiny);				
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
		
		log.info(ConstantDataManager.PERSON_MAINTENANCE_OPEN_METHOD_METHOD + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_DETAIL_METHOD);
		
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;

		PersonDTO person = null;

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

			final String personIdParameter = request.getParameter(ConstantDataManager.PARAMETER_ID);
			final String objectTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_OBJECT_TYPE);
			person = new PersonDTO();
			person.setPersonId(personIdParameter);
			person.setObjectType(objectTypeParameter);
			person.validateFormDetail();
			person.setUserChange(user.getId());
			person.setClientId(user.getClientId());

			final PersonService personService = new PersonServiceImpl(connection);
			personService.get(person);
			if (person.getName() == null) {
				throw new AdminException(this.messages.get(ConstantDataManager.MESSAGE_NOT_FOUND_COMPANY, null));
			}

			if(ObjectType.COMPANY.getType().equalsIgnoreCase(person.getObjectType())) {
				if(person.getClientId() != user.getClientId()) {
					result.put(ConstantDataManager.COMPANY_NOT_TO_USER, true);
					throw new AdminException("Empresa: " + person.getPersonId() + com.manager.systems.common.utils.ConstantDataManager.SPACE + person.getName() + " não pertence ao usuario logado.");
				}
				
				final Optional<UserCompnayDTO> userCompany =  person.getUsersCompanys().parallelStream().filter(x -> x.getUserId() == user.getId()).findFirst();
				if(!userCompany.isPresent()) {
					result.put(ConstantDataManager.COMPANY_NOT_TO_USER, true);
					throw new AdminException("Empresa: " + person.getPersonId() + com.manager.systems.common.utils.ConstantDataManager.SPACE + person.getName() + " não pertence ao usuario logado.");
				}
			}
			
			if (!com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_USER.equalsIgnoreCase(person.getObjectType())) {
				final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
				final IntegrationSystemsDTO integrationSystem = new IntegrationSystemsDTO();
				integrationSystem.setObjectId(person.getPersonId());
				integrationSystem.setObjectType(person.getObjectType());
				person.getIntegrationSystemsValues().addAll(integrationSystemsService.getAll(integrationSystem));
				person.setFinancialCostCenterId(person.getFinancialCostCenterId());
			}

			status = true;
			result.put(ConstantDataManager.PARAMETER_PERSON, person);
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
		
		log.info(ConstantDataManager.PERSON_MAINTENANCE_OPEN_METHOD_METHOD + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD);
		
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

			final ReportPersonDTO reportPerson = this.processFilter(request, connection);
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT, reportPerson);
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

	public ReportPersonDTO processFilter(final HttpServletRequest request, final Connection connection) throws Exception {
		final ReportPersonDTO reportPerson = new ReportPersonDTO();

		try {
			
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			String personIdFromParameter = request.getParameter(ConstantDataManager.PARAMETER_PERSON_ID_FROM);
			if (!StringUtils.isLong(personIdFromParameter)) {
				personIdFromParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}

			String personIdToParameter = request.getParameter(ConstantDataManager.PARAMETER_PERSON_ID_TO);
			if (!StringUtils.isLong(personIdToParameter)) {
				personIdToParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
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
			

			String personTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_FILTER_PERSON_TYPE);

			String filterPersonTypeIdParameter = request.getParameter(ConstantDataManager.PARAMETER_FILTER_PERSON_TYPE_ID);
			if (StringUtils.isNull(filterPersonTypeIdParameter)) {
				filterPersonTypeIdParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			
			String filterEstablishmentTypeIdParameter = request.getParameter(ConstantDataManager.PARAMETER_FILTER_ESTABLISHMENT_TYPE_ID);
			if (StringUtils.isNull(filterEstablishmentTypeIdParameter)) {
				filterEstablishmentTypeIdParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			
			String[] selectAllUsersFilter = request.getParameterValues(ConstantDataManager.PARAMETER_FILTER_USERS_CHILDRENS);
			
			final StringBuilder usersChildrensResult = new StringBuilder();
			if (selectAllUsersFilter != null && selectAllUsersFilter.length > 0) {
				int countUsersFilter = 0; 
				for(final String userFilter : selectAllUsersFilter) {
					if(countUsersFilter > 0) {
						usersChildrensResult.append(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);
					}
					usersChildrensResult.append(userFilter);
					countUsersFilter++;
				}
				
				reportPerson.setUsersChildrens(usersChildrensResult.toString());
			} else {
				usersChildrensResult.append(user.getId());
			}

			reportPerson.setPersonIdFrom(personIdFromParameter);
			reportPerson.setPersonIdTo(personIdToParameter);
			reportPerson.setInactive(inactiveParameter);
			reportPerson.setDescription(descriptionParameter);
			reportPerson.setObjectType(objectType.getType());
			reportPerson.setUserOperation(user.getId());
			reportPerson.setPersonType(personTypeParameter);
			reportPerson.setFilterPersonTypeId(Integer.valueOf(filterPersonTypeIdParameter));
			reportPerson.setFilterEstablishmentTypeId(Integer.valueOf(filterEstablishmentTypeIdParameter));
			reportPerson.setClientId(user.getClientId());

			final PersonService personService = new PersonServiceImpl(connection);
			if(ObjectType.COMPANY.getType().equalsIgnoreCase(objectType.getType())) {
				personService.getAllReport(reportPerson);
			} else {
				personService.getAll(reportPerson);				
			}
		} catch (final Exception ex) {
			throw ex;
		}
		return reportPerson;
	}

	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_AUTOCOMPLETE_METHOD)
	public ResponseEntity<String> autocomplete(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.PERSON_MAINTENANCE_OPEN_METHOD_METHOD + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_AUTOCOMPLETE_METHOD);
		
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

			String personIdFromParameter = request.getParameter(ConstantDataManager.PARAMETER_PERSON_ID_FROM);
			if (!StringUtils.isLong(personIdFromParameter)) {
				personIdFromParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}

			String personIdToParameter = request.getParameter(ConstantDataManager.PARAMETER_PERSON_ID_TO);
			if (!StringUtils.isLong(personIdToParameter)) {
				personIdToParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
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

			String personTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_PERSON_TYPE);
			
			final ReportPersonDTO reportPerson = new ReportPersonDTO();
			if (StringUtils.isLong(descriptionParameter)) {
				reportPerson.setPersonIdFrom(descriptionParameter);
				reportPerson.setPersonIdTo(descriptionParameter);
				reportPerson.setDescription(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			} else {
				reportPerson.setPersonIdFrom(personIdFromParameter);
				reportPerson.setPersonIdTo(personIdToParameter);
				reportPerson.setDescription(descriptionParameter);
			}
			reportPerson.setInactive(inactiveParameter);
			reportPerson.setObjectType(objectType.getType());
			reportPerson.setUserOperation(user.getId());
			reportPerson.setPersonType(personTypeParameter);
			
			final String origin = request.getParameter(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_ORIGIN);
			reportPerson.setOrigin(origin);

			final PersonService personService = new PersonServiceImpl(connection);
			final List<Combobox> items = personService.getAllAutocomplete(reportPerson);

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
}