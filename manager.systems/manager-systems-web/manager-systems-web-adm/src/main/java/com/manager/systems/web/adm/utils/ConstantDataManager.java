package com.manager.systems.web.adm.utils;

public final class ConstantDataManager 
{
	//Controller constants
	public static final String RESULT_HOME = "home-base";
	
	//Common controller methods
	public static final String COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD = "/open";
	public static final String COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD = "/save";
	public static final String COMMON_CONTROLLER_MAINTENANCE_INACTIVE_METHOD = "/inactive";
	public static final String COMMON_CONTROLLER_MAINTENANCE_DETAIL_METHOD = "/detail";
	public static final String COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD = "/filter";
	public static final String COMMON_CONTROLLER_MAINTENANCE_AUTOCOMPLETE_METHOD = "/autocomplete";
	public static final String COMMON_CONTROLLER_MAINTENANCE_CLONE_METHOD = "/clone";
	public static final String COMMON_CONTROLLER_MAINTENANCE_AUTOCOMPLETE_DESCRIPTION_METHOD = "/autocompleteDescription";
	
	//Person Controller
	public static final String PERSON_MAINTENANCE_CONTROLLER = "/adm/person";
	public static final String PERSON_MAINTENANCE_OPEN_METHOD_METHOD = "/open/{objectType}";
	public static final String PERSON_MAINTENANCE_OPEN_METHOD_RESULT = "adm/person/person-base";

	//User Controller
	public static final String USER_MAINTENANCE_CONTROLLER = "/adm/user";
	public static final String USER_MAINTENANCE_OPEN_METHOD_RESULT = "adm/user/user-base";
		
	//Product Controller
	public static final String PRODUCT_MAINTENANCE_CONTROLLER = "/adm/product";
	public static final String PRODUCT_MAINTENANCE_OPEN_METHOD_RESULT = "adm/product/product-base";
	
	//Bank Account Controller
	public static final String BANK_ACCOUNT_MAINTENANCE_CONTROLLER = "/adm/bank-account";
	public static final String BANK_ACCOUNT_MAINTENANCE_OPEN_METHOD_RESULT = "adm/bank-account/bank-account-base";
	public static final String BANK_ACCOUNT_MAINTENANCE_GET_COMBOBOX_BY_PERSON_METHOD = "/getAllComoboboxByPerson";
	public static final String BANK_ACCOUNT_MAINTENANCE_GET_COMBOBOX_BY_COMPANYS_METHOD = "/getAllComoboboxByCompanys";

	//Financial Group Controller
	public static final String FINANCIAL_GROUP_MAINTENANCE_CONTROLLER = "/adm/financial-group";
	public static final String FINANCIAL_GROUP_MAINTENANCE_OPEN_METHOD_RESULT = "adm/financial-group/financial-group-base";

	//Financial Sub Group Controller
	public static final String FINANCIAL_SUB_GROUP_MAINTENANCE_CONTROLLER = "/adm/financial-sub-group";
	public static final String FINANCIAL_SUB_GROUP_MAINTENANCE_OPEN_METHOD_RESULT = "adm/financial-sub-group/financial-sub-group-base";

	//Financial Transfer Controller
	public static final String FINANCIAL_TRANSFER_MAINTENANCE_CONTROLLER = "/adm/financial-transfer";
	public static final String FINANCIAL_TRANSFER_MAINTENANCE_OPEN_METHOD_RESULT = "adm/financial-transfer/financial-transfer-base";
	public static final String FINANCIAL_TRANSFER_MAINTENANCE_CONTROLLER_EDIT_GROUP_TRANSFER = "/editGroupTransfer";
	public static final String FINANCIAL_TRANSFER_MAINTENANCE_CONTROLLER_EDIT_GROUP_ITEM_TRANSFER = "/editGroupItemTransfer";
	
	//Product Group Controller
	public static final String PRODUCT_GROUP_MAINTENANCE_CONTROLLER = "/adm/product-group";
	public static final String PRODUCT_GROUP_MAINTENANCE_OPEN_METHOD_RESULT = "adm/product-group/product-group-base";

	//Financial Sub Group Controller
	public static final String PRODUCT_SUB_GROUP_MAINTENANCE_CONTROLLER = "/adm/product-sub-group";
	public static final String PRODUCT_SUB_GROUP_MAINTENANCE_OPEN_METHOD_RESULT = "adm/product-sub-group/product-sub-group-base";

	//Access Profile Controller
	public static final String ACCESS_PROFILE_MAINTENANCE_CONTROLLER = "/adm/access-profile";
	public static final String ACCESS_PROFILE_MAINTENANCE_OPEN_METHOD_RESULT = "adm/access-profile/access-profile-base";
 
	//Parameters
	
	//Global
	public static final String PARAMETER_INACTIVE = "inactive";
	public static final String PARAMETER_ACTIVE = "active";
	public static final String PARAMETER_ID = "id";
	public static final String PARAMETER_NAME = "name";
	public static final String PARAMETER_ALIAS_NAME = "aliasName";
	public static final String PARAMETER_INTEGRATION_SYSTEMS = "integrationSystems";
	public static final String PARAMETER_INTEGRATION_SYSTEMS_VALUES = "integrationSystemValues";
	public static final String PARAMETER_BANKS = "banks";
	public static final String PARAMETER_ID_FROM = "idFrom";
	public static final String PARAMETER_ID_TO = "idTo";
	public static final String PARAMETER_REPORT = "report";	
	public static final String PARAMETER_OBJECT_TYPE_ALIAS = "objectTypeAlias";
	public static final String PARAMETER_SAVE_ALL = "saveAll";
	public static final String PARAMETER_REGISTER_TYPE = "registerType";
	public static final String PARAMETER_ID_SUB_GROUP_1 = "idSubgroup_1";
	
	//Person Maintenance
	public static final String PARAMETER_ZIP_CODE = "zipCode";	
	public static final String PARAMETER_PERSON = "person";	
	public static final String PARAMETER_PERSON_ID_FROM = "personIdFrom";
	public static final String PARAMETER_PERSON_ID_TO = "personIdTo";
	public static final String PARAMETER_DESCRIPTION = "description";
	public static final String PARAMETER_PERSON_ID = "personId";
	public static final String PARAMETER_PERSON_TYPE = "personType";
	public static final String PARAMETER_OBJECT_TYPE = "objectType";
	public static final String PARAMETER_SOCIAL_NAME = "socialName";
	public static final String PARAMETER_FANTASY_NAME = "fantasyName";
	public static final String PARAMETER_CPF_CNPJ = "cpfCnpj";
	public static final String PARAMETER_EMAIL = "email";
	public static final String PARAMETER_ADRESS_ZIP_CODE = "addressZipCode";
	public static final String PARAMETER_ADRESS_ZIP_CODE_SIDE = "addressZipCodeSide";
	public static final String PARAMETER_ADRESS_STREET = "addressStreet";
	public static final String PARAMETER_ADRESS_STREET_NUMBER = "addressStreetNumber";
	public static final String PARAMETER_ADRESS_STREET_COMPLEMENT = "addressStreetComplement";
	public static final String PARAMETER_ADRESS_DISTRICT = "addressDistrict";
	public static final String PARAMETER_ADRESS_STATE = "addressState";
	public static final String PARAMETER_ADRESS_STATE_IBGE = "addressStateIbge";
	public static final String PARAMETER_ADRESS_CITY = "addressCity";
	public static final String PARAMETER_ADRESS_CITY_IBGE = "addressCityIbge";
	public static final String PARAMETER_ADRESS_COUNTRY = "addressCountry";
	public static final String PARAMETER_ADRESS_CONTRY_IBGE = "addressCountryIbge";
	public static final String PARAMETER_NEGATIVE_CLOSE = "negativeClose";
	public static final String PARAMETER_MOVEMENT_AUTOMATIC = "movementAutomatic";
	public static final String PARAMETER_COMPANIES = "companies";
	public static final String PARAMETER_CREDIT_PROVIDER = "creditProvider";
	public static final String PARAMETER_DEBIT_PROVIDER = "debitProvider";
	public static final String PARAMETER_FINANCIAL_GROUP_ID = "financialGroupId";
	public static final String PARAMETER_FINANCIAL_SUB_GROUP_ID = "financialSubGroupId";
	public static final String PARAMETER_PERSONS_ID = "personsId";
	public static final String PARAMETER_COMPANY_TYPE = "companyType";
	public static final String PARAMETER_BANK_ACCOUNT_COMPANY_PERMISSION = "bankAccountsCompanyPermission";
	public static final String PARAMETER_BANK_ACCOUNTS_COMPANY = "bankAccountsCompany";

	//Create date 18/08/2022
	public static final String PARAMETER_ESTABLISHMENT_TYPE_ID = "establishmentTypeId";

	//Create date 02/10/2022
	public static final String PARAMETER_BLOCKED = "blocked";	
	
	
	//Create date 06/06/2022
	//Filter Provider Type Person
	public static final String PARAMETER_FILTER_PERSON_TYPE = "filterPersonType";
	
	
	//Create date 14/11/2021
	//Person Email collector and operator
	public static final String PARAMETER_EMAIL_COLLECTOR = "emailCollector";
	public static final String PARAMETER_EMAIL_OPERATOR = "emailOperator";
	
	//User
	public static final String PARAMETER_ACCESS_DATA_USER = "accessDataUser";
	public static final String PARAMETER_ACCESS_DATA_PASSWORD = "accessDataPassword";
			
	//Create 21/09/2022
	public static final String PARAMETER_USERS = "users";
	public static final String PARAMETER_USER_CHILDRENS_PARENT_COMBOBOX = "userChildrensParentCombobox";
	public static final String PARAMETER_USERS_COMPANY_COMBOBOX = "usersCompanyCombobox";
	
	//Company
	public static final String PARAMETER_COMPANY_ID = "companyId";
	public static final String PARAMETER_COMPANYS_ID = "companysId";
	public static final String PARAMETER_COMPANY = "company";
	public static final String COMPANY_NOT_TO_USER = "companyNotToUser";
	
	//Access Profile
	public static final String PARAMETER_ACCES_PROFILE_ID = "accessProfileId";
	public static final String PARAMETER_ACCES_PROFILE = "accessProfile";
	public static final String PARAMETER_ACCES_PROFILES = "accessProfiles";
	
	//Bank Account Maintenance
	public static final String PARAMETER_BANK_ACCOUNT_ID = "bankAccountId";	
	public static final String PARAMETER_BANK_ACCOUNT = "bankAccount";	
	public static final String PARAMETER_BANK_ACCOUNTS = "bankAccounts";	
	public static final String PARAMETER_BANK_ACCOUNT_ID_FROM = "bankAccountIdFrom";
	public static final String PARAMETER_BANK_ACCOUNT_ID_TO = "bankAccountIdTo";
	public static final String PARAMETER_BANK_BALANCE_AVAILABLE = "bankBalanceAvailable";
	public static final String PARAMETER_BANK_LIMIT_AVAILABLE = "bankLimitAvailable";
	public static final String PARAMETER_BANK_CODE = "bankCode";
	public static final String PARAMETER_BANK_ANGENCY = "bankAngency";
	public static final String PARAMETER_BANK_INTEGRATION_SYSTEM_ID = "integrationSystemId";
	public static final String PARAMETER_BANK_INTEGRATION_ID = "integrationId";
	public static final String PARAMETER_BANK_ACCOUNT_UNBOUND = "unboundBankAccount";
	public static final String PARAMETER_BANK_ACCOUNT_OTHERS = "bankAccountOthers";
	public static final String PARAMETER_BANK_ACCOUNT_ACCUMULATE_BALANCE = "accumulateBalance";
	
	
	//Financial Group
	public static final String PARAMETER_FINANCIAL_GROUP = "financialGroup";	
	public static final String PARAMETER_FINANCIAL_GROUPS = "financialGroups";
	public static final String PARAMETER_REVENUE_SOURCE = "revenueSource";
	public static final String PARAMETER_REVENUE_TYPE = "revenueType";

	//Financial SubGroup
	public static final String PARAMETER_FINANCIAL_SUB_GROUP = "financialSubGroup";
	public static final String PARAMETER_FINANCIAL_SUB_GROUPS = "financialSubGroups";
	public static final String PARAMETER_IS_SAVE_DYNAMIC = "isSaveDynamic"; 
	
	//Product Group
	public static final String PARAMETER_PRODUCT_GROUP = "productGroup";	
	public static final String PARAMETER_PRODUCT_GROUPS = "productGroups";
	
	//Product SubGroup
	public static final String PARAMETER_PRODUCT_SUB_GROUP = "productSubGroup";
	public static final String PARAMETER_PRODUCT_SUB_GROUPS = "productSubGroups";
	
	//Product Maintenance
	public static final String PARAMETER_COST_PRICE = "costPrice";
	public static final String PARAMETER_SALE_PRICE = "salePrice";
	public static final String PARAMETER_INPUT_MOVEMENT = "inputMovement";
	public static final String PARAMETER_OUTPUT_MOVEMENT = "outputMovement";
	public static final String PARAMETER_CONVERSION_FACTOR = "conversionFactor";
	public static final String PARAMETER_ENABLE_CLOCK_MOVEMENT = "enableClockMovement";
	public static final String PARAMETER_CLOCK_MOVEMENT = "clockMovement";	
	public static final String PARAMETER_PRODUCT = "product";
	
	//Financial Transfer
	public static final String PARAMETER_FINANCIAL_TRANSFER = "financialTransfer";	
	public static final String PARAMETER_BANK_ACCOUNT_ORIGIN = "bankAccountOrigin";
	public static final String PARAMETER_BANK_ACCOUNT_DESTINY = "bankAccountDestiny";
	public static final String PARAMETER_BANK_ACCOUNT_AUTOMATIC_TRANSFER = "bankAccountAutomaticTransfer";
	public static final String PARAMETER_FINANCIALTRANSFER_EXECUTION_PERIOD = "executionPeriod";
	
	public static final String PARAMETER_FINANCIALTRANSFER_WEEK_DAY = "weekDay";
	public static final String PARAMETER_FINANCIALTRANSFER_FIXED_DAY = "fixedDay";
	public static final String PARAMETER_FINANCIALTRANSFER_FIXED_DAY_MONTH = "fixedDayMonth";
	public static final String PARAMETER_FINANCIALTRANSFER_AUTOMATIC_PROCESSING = "automaticProcessing";
	public static final String PARAMETER_FINANCIALTRANSFER_LAUNCH_TYPE = "launchType";
	public static final String PARAMETER_FINANCIALTRANSFER_EXECUTION_DAYS = "executionDays";
	public static final String PARAMETER_FINANCIALTRANSFER_EXECUTION_TIME = "executionTime";
	
	public static final String PARAMETER_ACTIVE_GROUP = "activeGroup";
	public static final String PARAMETER_GROUP_ID = "groupId";
	public static final String PARAMETER_GROUP_ITEM_ID = "groupItemId";
	public static final String PARAMETER_DESCRIPTION_GROUP = "descriptionGroup";
	public static final String PARAMETER_EXECUTION_ORDER_GROUP = "executionOrderGroup";
	public static final String PARAMETER_EXECUTION_ORDER_GROUP_ITEM = "executionOrderGroupItem";
	public static final String PARAMETER_PRODUCTS = "products";
	public static final String PARAMETER_DESCRIPTION_TRANSFER = "descriptionTransfer";
	public static final String PARAMETER_PROVIDER = "provider";
	public static final String PARAMETER_TRANSFER_STATE = "transferState";
	public static final String PARAMETER_GROUP_PRODUCT = "groupProduct";
	public static final String PARAMETER_SUB_GROUP_PRODUCT = "subGroupProduct";
	public static final String PARAMETER_TRANSFER_TYPE = "transferType";
	public static final String PARAMETER_VALUE_TRANSFER = "valueTransfer";	
	public static final String PARAMETER_EXPENSE = "expense";
	public static final String PARAMETER_EXECUTION_ORDER = "executionOrder";
	public static final String PARAMETER_IS_OVER_TOTAL = "isOverTotal";
	public static final String PARAMETER_IS_VARIABLE_EXPENSE = "isVariableExpense";
	public static final String PARAMETER_IS_USE_REMAINING_BALANCE = "isUseRemainingBalance";
	public static final String PARAMETER_CREDIT_DEBIT = "creditDebit";
	public static final String PARAMETER_ACTIVE_ITEM = "activeItem";
	public static final String PARAMETER_INITIAL_EXECUTION = "initialExecution";

	//Create 03/08/2022
	public static final String COMMON_CONTROLLER_GET_PRODUCTS = "/getAllProducts";
	public static final String PARAMETER_REPORT_PRODUCT = "reportProduct";	
	
	//Document Movement
	public static final String PARAMETER_PROVIDERS = "providers";
	
	//Access Profile
	public static final String PARAMETER_ROLE = "role";
	public static final String PARAMETER_CASHING_CLOSING_MAX_DISCOUNT = "cashingClosingMaxDiscount";
	
	//Messages
	//Person Maintenance
	public static final String MESSAGE_INVALID_PERSON_ID = "person.id.invalid";
	public static final String MESSAGE_NOT_FOUND_COMPANY = "company.adm.not.found";
	public static final String MESSAGE_OBJECT_TYPE_NOT_INFORMED = "person.object.type.not.informed";
	public static final String MESSAGE_OBJECT_TYPE_INVALID = "person.object.type.invalid";
	
	//Filter Person Maintenance 06/06/2022
	public static final String MESSAGE_PEROSON_TYPE_NOT_INFORMED = "person.type.not.informed";
	
	//Bank Account Maintenance
	public static final String MESSAGE_NOT_FOUND_BANK_ACCOUNT = "bank.account.adm.not.found";

	//Financial Group
	public static final String MESSAGE_NOT_FOUND_FINANCIAL_GROUP = "financial.group.adm.not.found";
	public static final String MESSAGE_FINANCIAL_GROUP_INVALID = "financial.group.invalid";
	
	//Product
	public static final String MESSAGE_NOT_FOUND_PRODUCT = "product.adm.not.found";
	public static final String MESSAGE_NOT_USER_PRODUCT = "product.adm.not.user";
	public static final String NOT_USER_PRODUCT_VALUE = "productNotUser";
	
	//Group
	public static final String MESSAGE_GROUP_INVALID = "group.invalid";
	
	//SubGroup
	public static final String MESSAGE_SUBGROUP_INVALID = "subgroup.invalid";
	
	//Labels
	public static final String COMPANY_LABEL = "company.label";
	public static final String USER_LABEL = "user.label";
	public static final String PROVIDER_LABEL = "provider.label";
	
	//Product Group Trasnfer 25/11/2021
	public static final String PARAMETER_PRODUCT_TRANSFER_HAB = "transferHab";
	
	//dashboard Romanian Company 02/03/2022
	public static final String PARAMETER_FILD_CHANGE = "fildChange";
	
	//Company type 02/03/2022
	public static final String PERSON_TYPE_CONTROLLER = "/adm/personType";
	public static final String COMMON_CONTROLLER_PERSON_TYPE_SAVE_METHOD = "/save";
	public static final String PARAMETER_COMPANY_TYPES = "companyTypes";	
	
	//Create date 18/08/2022
	//Filter Company 
	public static final String PARAMETER_FILTER_PERSON_TYPE_ID = "filterPersonTypeId";
	public static final String PARAMETER_FILTER_ESTABLISHMENT_TYPE_ID = "filterEstablishmentTypeId";
	public static final String PARAMETER_FILTER_USERS_CHILDRENS = "userChildrensParentComboboxFilter";
	
	//Create date 18/08/2022
	//Create Person Type
	public static final String SAVE_PERSON_COMPANY_TYPE_CONTROLLER = "/savePersonCompanyType";
	public static final String PARAMETER_PERSON_TYPES = "personTypes";
	public static final String PARAMETER_ESTABLISHMENT_TYPES = "establishmentTypes";
	public static final String PARAMETER_PERSON_TYPE_ID = "personTypeId";
	
	//Create date 18/08/2022
	//Create Company Establishment Type
	public static final String ESTABLISHMENT_TYPE_CONTROLLER = "/adm/establishmentType";
	
	//Create date 29/08/2022
	//Create Log System
	public static final String LOG_SYSTEM_CONTROLLER = "/logsystem";
	public static final String PARAMETER_ITEMS = "reportItems";
	public static final String PARAMETER_OBJECT_ID = "objectId";
	public static final String PARAMETER_SYSTEM_ID = "systemId";
	public static final String LOG_SYSTEM_OPEN_METHOD_RESULT = "adm/log-sys/log-sys-base";
	public static final String COMMON_CONTROLLER_MAINTENANCE_FILTER_LOG_SYSTEM_METHOD = "/filterLogSystem";
	public static final String PARAMETER_USER_CHIDRENS_PARENT = "userChildrensParent";
	public static final String PARAMETER_CHANGE_DATE = "changeDate";
	
	public static final String COMMON_CONTROLLER_REPORT_PDF_METHOD = "/reportPdf";
	public static final String COMMON_CONTROLLER_MAINTENANCE_FILTER_REPORT_METHOD = "/filterReport";
	public static final String PARAMETER_ANALITIC = "analitic";
	public static final String PARAMETER_ITEM = "item"; 
	
	//Dre
	//Create 08/09/2022
	public static final String DRE_CONTROLLER = "/dre";
	public static final String DRE_OPEN_METHOD_RESULT = "dre/dre-base";
	public static final String PARAMETER_PROVIDER_ID = "providerId";
	public static final String PARAMETER_DOCUMENT_TYPE = "documentType";
	public static final String PARAMETER_DOCUMENT_NUMBER = "documentNumber";
	public static final String PARAMETER_FILTER_DATE = "filterDate";
	public static final String PARAMETER_DATE = "date";
	public static final String PARAMETER_MOVEMENT_TYPE_ID = "movementTypeId";
	public static final String PARAMETER_PRODUCT_GROUP_ID = "productGroup";
	public static final String PARAMETER_PRODUCT_SUB_GROUP_ID = "productSubGroup";
	public static final String PARAMETER_USER_ID = "userId";
	
	//Create date 09/09/2022
	//Create Product Discard
	public static final String PARAMETER_COMPANY_DESTINY = "companyDestiny";
	
	public static final String PERSON_TYPE_USER = "F";
	public static final String PERSON_TYPE_COMPANY = "J";
	public static final String PERSON_TYPE_PROVIDER = "J";
	
	//Create date 21/11/2022
	//Financial Cost Center Group Controller
	public static final String FINANCIAL_COST_CENTER_GROUP_CONTROLLER = "/adm/financial-cost-center-group";
	public static final String FINANCIAL_COST_CENTER_GROUP_OPEN_METHOD_RESULT = "adm/financial-cost-center-group/financial-cost-center-group-base";
	
	//Financial Cost Cente Group
	public static final String PARAMETER_FINANCIAL_COST_CENTER_GROUP = "financialCastCenterGroup";	
	public static final String PARAMETER_FINANCIAL_COST_CENTER_GROUPS = "financialCastCenterGroups";

	//Create date 27/11/2022
	//Main Company Bank Account
	public static final String PARAMETER_MAIN_COMPANY_BANK_ACCOUNT_ID = "mainCompanyBankAccountId";
	
	
	//Alias Product 02/03/2022
	public static final String PARAMETER_ALIAS_PRODUCT = "aliasProduct";
	
	//Game type 02/03/2022
	public static final String GAME_TYPE_CONTROLLER = "/adm/gameType";
	public static final String PARAMETER_GAME_TYPES = "gameTypes";
	public static final String PARAMETER_GAME_TYPE_ID = "gameTypeId";
	
	//Machine type 02/03/2022
	public static final String MACHINE_TYPE_CONTROLLER = "/adm/machineType";
	public static final String PARAMETER_MACHINE_TYPES = "machineTypes";
	public static final String PARAMETER_MACHINE_TYPE_ID = "machineTypeId";
	
	//Cost Center
	//Create 12/10/2024
	public static final String PARAMETER_FINANCIAL_COST_CENTERS = "financialCostCenters";
	public static final String PARAMETER_FINANCIAL_COST_CENTER_TRANSFER = "financialCostCenterId";
} 