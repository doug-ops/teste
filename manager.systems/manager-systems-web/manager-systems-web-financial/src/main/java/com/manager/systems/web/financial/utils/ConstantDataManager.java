package com.manager.systems.web.financial.utils;

public final class ConstantDataManager 
{
	//Controller constants
	public static final String RESULT_HOME = "home-base";
	
	// Usuario
	public static final String PARAMETER_USERNAME = "username";
	public static final String PARAMETER_PASSWORD = "password";
	public static final String PARAMETER_IS_OFFLINE = "is_offline";
	public static final String PARAMETER_IS_MANAGER = "is_manager";
	
	//Common controller methods
	public static final String COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD = "/open";
	public static final String COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD = "/save";
	public static final String COMMON_CONTROLLER_MAINTENANCE_INACTIVE_METHOD = "/inactive";
	public static final String COMMON_CONTROLLER_MAINTENANCE_DETAIL_METHOD = "/detail";
	public static final String COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD = "/filter";
	public static final String COMMON_CONTROLLER_MAINTENANCE_AUTOCOMPLETE_METHOD = "/autocomplete";
	public static final String COMMON_CONTROLLER_MAINTENANCE_UNGROUP_METHOD = "/ungroup";
	public static final String COMMON_CONTROLLER_MAINTENANCE_GROUP_PARENT_METHOD = "/groupParent";
	public static final String COMMON_CONTROLLER_MAINTENANCE_CHANGE_STATUS_METHOD = "/changeStatus";
	public static final String COMMON_CONTROLLER_MAINTENANCE_CHANGE_METHOD = "/change";
	
	//Financial Sub Group Controller
	public static final String PRODUCT_SUB_GROUP_MAINTENANCE_CONTROLLER = "/adm/product-sub-group";
	public static final String PRODUCT_SUB_GROUP_MAINTENANCE_OPEN_METHOD_RESULT = "adm/product-sub-group/product-sub-group-base";
	
	//Document Movement Controller
	public static final String DOCUMENT_MOVEMENT_CONTROLLER = "/movement/documentMovement";
	public static final String DOCUMENT_MOVEMENT_OPEN_METHOD_RESULT = "movements/document-movement/document-movement-base";
	
	//Financial Document Controller
	public static final String FINANCIAL_DOCUMENT_CONTROLLER = "/financial/document";
	
	//Parameters
	
	//Global
	public static final String PARAMETER_INACTIVE = "inactive";
	public static final String PARAMETER_ACTIVE = "active";
	public static final String PARAMETER_ID = "id";
	public static final String PARAMETER_IDS = "ids";
	public static final String PARAMETER_NAME = "name";
	public static final String PARAMETER_ALIAS_NAME = "aliasName";
	public static final String PARAMETER_INTEGRATION_SYSTEMS = "integrationSystems";
	public static final String PARAMETER_INTEGRATION_SYSTEMS_VALUES = "integrationSystemValues";
	public static final String PARAMETER_BANKS = "banks";
	public static final String PARAMETER_ID_FROM = "idFrom";
	public static final String PARAMETER_ID_TO = "idTo";
	public static final String PARAMETER_REPORT = "report";	
	public static final String PARAMETER_LAUNCHS = "launchs";
	
	//Person Maintenance
	public static final String PARAMETER_ZIP_CODE = "zipCode";	
	public static final String PARAMETER_PERSON = "person";	
	public static final String PARAMETER_PERSON_ID_FROM = "personIdFrom";
	public static final String PARAMETER_PERSON_ID_TO = "personIdTo";
	public static final String PARAMETER_DESCRIPTION = "description";
	public static final String PARAMETER_PERSON_ID = "personId";
	public static final String PARAMETER_PERSON_TYPE = "personType";
	public static final String PARAMETER_OBJECT_TYPE = "objectType";
	public static final String PARAMETER_OBJECT_TYPE_ALIAS = "objectTypeAlias";
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
	public static final String PARAMETER_COMPANIES = "companies";
	
	//User
	public static final String PARAMETER_ACCESS_DATA_USER = "accessDataUser";
	public static final String PARAMETER_ACCESS_DATA_PASSWORD = "accessDataPassword";
	
	//Company
	public static final String PARAMETER_COMPANY_ID = "companyId";
	public static final String PARAMETER_COMPANYS_ID = "companysId";
	
	//Bank Account Maintenance
	public static final String PARAMETER_BANK_ACCOUNT_ID = "bankAccountId";	
	public static final String PARAMETER_BANK_ACCOUNT = "bankAccount";	
	public static final String PARAMETER_BANK_ACCOUNTS = "bankAccounts";	
	public static final String PARAMETER_BANK_ACCOUNTS_NAMES = "bankAccountsNames";
	public static final String PARAMETER_BANK_ACCOUNT_ID_FROM = "bankAccountIdFrom";
	public static final String PARAMETER_BANK_ACCOUNT_ID_TO = "bankAccountIdTo";
	public static final String PARAMETER_BANK_BALANCE_AVAILABLE = "bankBalanceAvailable";
	public static final String PARAMETER_BANK_LIMIT_AVAILABLE = "bankLimitAvailable";
	public static final String PARAMETER_BANK_CODE = "bankCode";
	public static final String PARAMETER_BANK_ANGENCY = "bankAngency";
	public static final String PARAMETER_BANK_INTEGRATION_SYSTEM_ID = "integrationSystemId";
	public static final String PARAMETER_BANK_INTEGRATION_ID = "integrationId";
	
	//Cash Flow
	public static final String PARAMETER_TYPE_DOCUMENT = "typeDocument";	
	
	//Financial Group
	public static final String PARAMETER_FINANCIAL_GROUP = "financialGroup";	
	public static final String PARAMETER_FINANCIAL_GROUPS = "financialGroups";
	public static final String PARAMETER_FINANCIAL_GROUP_ID = "financialGroupId";

	//Financial SubGroup
	public static final String PARAMETER_FINANCIAL_SUB_GROUP = "financialSubGroup";
	public static final String PARAMETER_FINANCIAL_SUB_GROUPS = "financialSubGroups";
	public static final String PARAMETER_FINANCIAL_SUB_GROUP_ID = "financialSubGroupId";
	
	//Product Group
	public static final String PARAMETER_PRODUCT_GROUP = "productGroup";	
	public static final String PARAMETER_PRODUCT_GROUPS = "productGroups";
	public static final String PARAMETER_PRODUCT_GROUP_ID = "productGroup";
	
	//Product SubGroup
	public static final String PARAMETER_PRODUCT_SUB_GROUP = "productSubGroup";
	public static final String PARAMETER_PRODUCT_SUB_GROUPS = "productSubGroups";
	public static final String PARAMETER_PRODUCT_SUB_GROUP_ID = "productSubGroup";
	
	//Product Maintenance
	public static final String PARAMETER_COST_PRICE = "costPrice";
	public static final String PARAMETER_SALE_PRICE = "salePrice";
	public static final String PARAMETER_INPUT_MOVEMENT = "inputMovement";
	public static final String PARAMETER_OUTPUT_MOVEMENT = "outputMovement";
	public static final String PARAMETER_CONVERSION_FACTOR = "conversionFactor";
	public static final String PARAMETER_COMPANY = "company";
	public static final String PARAMETER_PRODUCT = "product";
	
	//Financial Transfer
	public static final String PARAMETER_FINANCIAL_TRANSFER = "financialTransfer";	
	public static final String PARAMETER_BANK_ACCOUNT_ORIGIN = "bankAccountOrigin";
	public static final String PARAMETER_BANK_ACCOUNT_DESTINY = "bankAccountDestiny";
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
	public static final String PARAMETER_TRANSFER_TYPE = "transferType";
	public static final String PARAMETER_VALUE_TRANSFER = "valueTransfer";	
	public static final String PARAMETER_EXPENSE = "expense";
	public static final String PARAMETER_EXECUTION_ORDER = "executionOrder";
	public static final String PARAMETER_IS_OVER_TOTAL = "isOverTotal";
	public static final String PARAMETER_IS_VARIABLE_EXPENSE = "isVariableExpense";
	public static final String PARAMETER_IS_USE_REMAINING_BALANCE = "isUseRemainingBalance";
	public static final String PARAMETER_CREDIT_DEBIT = "creditDebit";
	public static final String PARAMETER_ACTIVE_ITEM = "activeItem";
	
	//Document Movement
	public static final String PARAMETER_PROVIDERS = "providers";
	public static final String PARAMETER_MOVEMENT_TYPE = "movementType";
	public static final String PARAMETER_DOCUMENT_NUMBER = "documentNumber";
	public static final String PARAMETER_DOCUMENT_VALUE = "documentValue";
	public static final String PARAMETER_DOCUMENT_DATE = "documentDate";
	public static final String PARAMETER_DOCUMENT_NOTE = "documentNote";
	public static final String PARAMETER_DOCUMENT_ENCODED_NOTE = "documentNoteEncoded";
	public static final String PARAMETER_DOCUMENT_STATUS = "documentStatus";
	public static final String PARAMETER_PARENT_ID = "parentId";
	public static final String PARAMETER_CREDIT = "credit";
	public static final String PARAMETER_DOCUMENT_TYPE = "documentType";
	public static final String PARAMETER_PAYMENT_DATE = "paymentDate";
	public static final String PARAMETER_PAYMENT_EXPIRY_DATE = "paymentExpiryDate";
	public static final String PARAMETER_COUNT_DUPLICATES = "countDuplicates";	
	public static final String PARAMETER_PAYMENT_STATUS = "paymentStatus";
	public static final String PARAMETER_INSTALLMENT = "installment";
	
	public static final String PARAMETER_PAYMENT_TYPE_UNDERSCORE = "paymentType_";
	public static final String PARAMETER_DOCUMENT_NUMBER_UNDERSCORE = "documentNumber_";
	public static final String PARAMETER_PAYMENT_DATE_UNDERSCORE = "paymentDate_";
	public static final String PARAMETER_BANK_ACCOUNT_UNDERSCORE = "bankAccount_";
	public static final String PARAMETER_ORDER_VALUE_UNDERSCORE = "orderValue_";
	public static final String PARAMETER_DOCUMENT_ID = "documentId";
	public static final String PARAMETER_DOCUMENT_PARENT_ID = "documentParentId";
	
	public static final String PARAMETER_CASHING_CLOSE = "cashingClose";
	public static final String PARAMETER_CASH_STATUS = "cashStatus";
	public static final String PARAMETER_USER_OPERATOR = "userOperator";
	
	//Create Date 07/06/2022
	public static final String PARAMETER_PAYMENT_VALUE = "paymentValueTransf";
	
	//Messages
	
	//Labels
	public static final String COMPANY_LABEL = "company.label";
	public static final String USER_LABEL = "user.label";
	public static final String PROVIDER_LABEL = "provider.label";	
	
	//Transfer Account Origin and Destination
	public static final String CONTROLLER_SAVE_TRANSFER = "/saveTransfer";
	public static final String COMMON_CONTROLLER_MAINTENANCE_CHANGE_METHOD_TRANSFER = "/changeTransfer";
	
	//Transfer Financial Group
	public static final String PARAMETER_FINANCIAL_TRANSFER_GROUP = "financialGroupTrans";	
	public static final String PARAMETER_FINANCIAL_TRANSFER_GROUPS = "financialGroupsTrans";

	//Transfer Financial SubGroup
	public static final String PARAMETER_FINANCIAL_TRANSFER_SUB_GROUP = "financialSubGroupTrans";
	public static final String PARAMETER_FINANCIAL_TRANSFER_SUB_GROUPS = "financialSubGroupsTrans";
	
	//Transfer Account 
	public static final String PARAMETER_FINANCIAL_COMPANY_TRANSFER = "companyTransf";
	public static final String PARAMETER_DOCUMENT_ID_TRANSFER = "documentIdTransf";
	public static final String PARAMETER_DOCUMENT_PARENT_ID_TRANSFER = "documentParentIdTransf";
	public static final String PARAMETER_DOCUMENT_TYPE_TRANSFER = "documentTypeTransf";
	public static final String PARAMETER_PROVIDER_TRANSFER = "providerTransf";
	public static final String PARAMETER_FINANCIAL_GROUP_TRANSFER = "financialGroupTransf";
	public static final String PARAMETER_FINANCIAL_SUB_GROUP_TRANSFER = "financialSubGroupTransf";
	public static final String PARAMETER_MOVEMENT_TYPE_TRANSFER = "movementTypeTransf";
	public static final String PARAMETER_DOCUMENT_STATUS_TRANSFER = "documentStatusTransf";
	public static final String PARAMETER_DOCUMENT_NUMBER_TRANSFER = "documentNumberTransf";
	public static final String PARAMETER_DOCUMENT_VALUE_TRANSFER = "documentValueTransf";
	public static final String PARAMETER_PAYMENT_DATE_TRANSFER = "paymentDateTransf";
	public static final String PARAMETER_PAYMENT_EXPIRY_DATE_TRANSFER = "paymentExpiryDateTransf";
	public static final String PARAMETER_PAYMENT_STATUS_TRANSFER = "paymentStatusTransf";
	public static final String PARAMETER_BANK_ACCOUNT_TRANSFER = "bankAccountTransf";	
	public static final String PARAMETER_DOCUMENT_ENCODED_NOTE_TRANSFER = "documentNoteEncodedTransf";
	public static final String PARAMETER_INSTALLMENT_TRANSFER = "1";
	public static final String PARAMETER_FINANCIAL_COST_CENTER_TRANSFER = "financialCostCenterId";	
	public static final String LABEL_TRASNFER_CREDIT = "Transf. C";
	public static final String LABEL_TRASNFER_DEBIT = "Transf. D";
	public static final String LABEL_LAUNCH_DEBIT = "Lancamento D";
	public static final String LABEL_LAUNCH_CREDIT = "Lancamento C";
	public static final String LABEL_INSTALLMENT = "Parcela";
	
	//Transfer Account Dest
	public static final String PARAMETER_FINANCIAL_COMPANY_TRANSFER_DEST = "companyTransfDest";
	public static final String PARAMETER_DOCUMENT_ID_TRANSFER_DEST = "documentIdTransfDest";
	public static final String PARAMETER_DOCUMENT_PARENT_ID_TRANSFER_DEST = "documentParentIdTransfDest";
	public static final String PARAMETER_DOCUMENT_TYPE_TRANSFER_DEST = "documentTypeTransfDest";
	public static final String PARAMETER_PROVIDER_TRANSFER_DEST = "providerTransfDest";
	public static final String PARAMETER_FINANCIAL_GROUP_TRANSFER_DEST = "financialGroupTransfDest";
	public static final String PARAMETER_FINANCIAL_SUB_GROUP_TRANSFER_DEST = "financialSubGroupTransfDest";
	public static final String PARAMETER_MOVEMENT_TYPE_TRANSFER_DEST = "movementTypeTransfDest";
	public static final String PARAMETER_DOCUMENT_STATUS_TRANSFER_DEST = "documentStatusTransfDest";
	public static final String PARAMETER_DOCUMENT_VALUE_TRANSFER_DEST = "documentValueTransfDest";
	public static final String PARAMETER_PAYMENT_DATE_TRANSFER_DEST = "paymentDateTransfDest";
	public static final String PARAMETER_PAYMENT_EXPIRY_DATE_TRANSFER_DEST = "paymentExpiryDateTransfDest";
	public static final String PARAMETER_PAYMENT_STATUS_TRANSFER_DEST = "paymentStatusTransfDest";
	public static final String PARAMETER_BANK_ACCOUNT_TRANSFER_DEST = "bankAccountTransfDest";	
	public static final String PARAMETER_DOCUMENT_ENCODED_NOTE_TRANSFER_DEST = "documentNoteEncodedTransfDest";
	public static final String PARAMETER_INSTALLMENT_TRANSFER_DEST = "1";
	public static final String PARAMETER_PAYMENT_VALUE_TRANSFER_DEST = "paymentValueTransfDest";
	public static final String PARAMETER_DOCUMENT_NUMBER_TRANSFER_DEST = "documentNumberTransfDest";
	public static final String PARAMETER_COMPANY_DESTINY = "companyDestiny";

	//Cashier Closing Controller
	//Create 08/10/2022
	public static final String CASHIER_CLOSING_CONTROLLER = "/cashierClosing/cashier-closing";
	public static final String CASHIER_CLOSING_CONTROLLER_METHOD_RESULT = "adm/cashier-closing/cashier-closing-base";
	public static final String CASHIER_CLOSING_CONTROLLER_METHOD_SAVE = "/saveCashierClosing";
	public static final String CASHIER_CLOSING_CONTROLLER_METHOD_DETAIL_DOCUMENT = "/detailDocument";
	public static final String CASHIER_CLOSING_CONTROLLER_METHOD_REPORT_PDF = "/reportCashierClosingPdf}";

	
	public static final String PARAMETER_USER_CHIDRENS_PARENT = "userChidrensParent";
	public static final String PARAMETER_USER_CHIDRENS_PARENT_COMBOBOX = "userChildrensParentCombobox";
	public static final String COMMON_CONTROLLER_REPORT_PDF_METHOD = "/reportPdf";
	public static final String COMMON_CONTROLLER_MAINTENANCE_FILTER_REPORT_METHOD = "/filterReport";
	public static final String PARAMETER_PROVIDER_ID = "providerId";
	public static final String PARAMETER_FILTER_DATE = "filterDate";
	public static final String PARAMETER_DATE_FROM = "dateFrom";
	public static final String PARAMETER_DATE_TO = "dateTo";
	public static final String PARAMETER_DATE = "date";
	public static final String PARAMETER_JSON_DATA = "jsonData";
	public static final String PARAMETER_JSON_DATA_EXPENSE = "jsonDataExpense";
	public static final String PARAMETER_JSON_DATA_REQUEST = "jsonDataRequest";
	public static final String PARAMETER_CREDIT_ALIAS = "CR";
	public static final String PARAMETER_DEBIT_ALIAS = "DE";
	public static final String PARAMETER_OPEN_ALIAS = "AB";
	public static final String PARAMETER_CLOSE_ALIAS = "BA";
	public static final String PARAMETER_REMOVE_ALIAS = "EX";
	public static final String PARAMETER_CONFERED_ALIAS = "CO";
	public static final String PARAMETER_TRANSFER = "TRANSF";
	public static final String PARAMETER_TRANSFER_HAB = "HAB";
	public static final String PARAMETER_ANALITIC = "analitic";
	public static final String PARAMETER_INCOME_EXPENSE = "incomeExpense";
	public static final String PARAMETER_DOCUMENTS_PARENT_ID = "documentsParentId";
	public static final String PARAMETER_CASHIER_CLOSING_COMPANY_TRANSFER = "companyTransf";
	public static final String PARAMETER_MOVEMENT_TYPE_ID = "movementTypeId";
	public static final String PARAMETER_CLOSING_ALIAS = "FE";
	public static final String PARAMETER_GROUP_BY = "groupBy";
	public static final String PARAMETER_WEEK_VALUE = "weekValue";
	public static final String PARAMETER_WEEK_DESCRIPTION = "weekDescription";
	public static final String PARAMETER_USER_CHILDRENS_PARENT = "userChildrensParent";
	public static final String PARAMETER_COMPANYS = "companys";
	public static final String PARAMETER_FINANCIAL_COST_CENTERS = "financialCostCenters";
	public static final String PARAMETER_FINANCIAL_COST_CENTER = "financialCostCenter";
	
	
	public static final String PARAMETER_DOCUMENTS_KEYS_ALL= "documentskeysAll";
	
	//Cost Center 19/11/2022
	public static final String COST_CENTER_CONTROLLER = "/financial/financialCostCenter";

	//Document Expense 14/12/2022
	public static final String PARAMETER_COUNT_EXPENSE ="countExpense";
	public static final String PARAMETER_EXPENSE_ORIGIN_DESTINTY ="expenseOriginDestinty_";
	public static final String PARAMETER_EXPENSE_DOCUMENT_STATUS ="expenseDocumentStatus_";
	public static final String PARAMETER_O_ALIAS ="O";
	public static final String PARAMETER_D_ALIAS ="D";
	
	//Cashier Closing Expense 22/12/2022
	public static final String PARAMETER_EXPENSE_ORIGIN ="expenseOrigin_";
	
	public static final String PARAMETER_BANK_ACCOUNT_ORIGIN_ALL = "bankAccountOriginAll";
	public static final String PARAMETER_TOTAL_PAYMENT = "totalPayment";
	public static final String PARAMETER_COMPANY_ORIGIN_ALL = "companyOriginAll";
	
	//Cashier Movement Controller
	//Create 18/08/2023
	public static final String CASH_STATEMENT_CONTROLLER_OLD = "/movement/cashStatement";
	public static final String CASH_STATEMENT_CONTROLLER_OLD_OPEN_METHOD_RESULT = "movements/cash-statement/cash-statement-base";
	public static final String CASH_STATEMENT_CONTROLLER_OLD_REPORT_PDF_METHOD = "/reportPdf";
	public static final String LABEL_CASHIER_STATEMENT_OLD = "EXTRATO DE CAIXA";
	
	//Financial Income Expenses Controller
	//Create 24/08/2023
	public static final String FINANCIAL_INCOME_EXPENSES_CONTROLLER = "/financial/incomeExpenses";
	public static final String FINANCIAL_INCOME_EXPENSES_CONTROLLER_OPEN_METHOD_RESULT = "reports/financial-income-expenses/financial-income-expenses-base";
	public static final String FINANCIAL_INCOME_EXPENSES_CONTROLLER_REPORT_PDF_METHOD = "/reportPdf";
	public static final String TITLE_FINANCIAL_INCOME_EXPENSE_REPORT = "RELAT\u00d3RIO DE RECEITAS E DESPESAS";
	
	//Financial Cash Flow Controller
	//Create 28/10/2023
	public static final String FINANCIAL_CASH_FLOW_CONTROLLER = "/financial/cashFlow";
	public static final String FINANCIAL_CASH_FLOW_OPEN_METHOD_RESULT = "reports/financial-cash-flow/financial-cash-flow-base";
	public static final String FINANCIAL_CASH_FLOW_REPORT_PDF_METHOD = "/reportPdf";
	public static final String TITLE_FINANCIAL_CASH_FLOW_REPORT = "RELAT\u00d3RIO DE FLUXO DE CAIXA";
	
	//Financial Cash Flow Grouping Controller
	//Create 02/01/2024
	public static final String FINANCIAL_CASH_FLOW_GROUPING_CONTROLLER = "/financial/cashFlow/grouping";
	public static final String FINANCIAL_CASH_FLOW_GROUPING_OPEN_METHOD_RESULT = "reports/financial-cash-flow-grouping/financial-cash-flow-grouping-base";
	public static final String FINANCIAL_CASH_FLOW_GROUPING_REPORT_PDF_METHOD = "/reportPdf";
	public static final String TITLE_FINANCIAL_CASH_FLOW_GROUPING_REPORT = "RELAT\u00d3RIO DE FLUXO DE CAIXA POR GRUPO FINANCEIRO";
	
	//Financial Cash Statement Controller
	//Create 25/01/2024
	public static final String FINANCIAL_CASH_STATEMENT_CONTROLLER = "/financial/cashStatement";
	public static final String FINANCIAL_CASH_STATEMENT_OPEN_METHOD_RESULT = "reports/financial-cash-statement/financial-cash-statement-base";
	public static final String FINANCIAL_CASH_STATEMENT_REPORT_PDF_METHOD = "/reportPdf";
	public static final String TITLE_FINANCIAL_CASH_STATEMENT_GROUPING_REPORT = "EXTRATO DE CAIXA";
	
	//Cashier Closing Controller Preview
	//Create 26/01/2024
	public static final String CASHIER_CLOSING_PREVIEW_CONTROLLER = "/cashierClosingPreview/cashier-closing";
	public static final String CASHIER_CLOSING_PREVIEW_METHOD_RESULT = "adm/cashier-closing-preview/cashier-closing-preview-base";
	public static final String CASHIER_CLOSING_PREVIEW_CONTROLLER_SAVE = "/saveCashierClosing";
	public static final String CASHIER_CLOSING_PREVIEW_CONTROLLER_DETAIL_DOCUMENT_METHOD = "/detailDocument";
	public static final String CASHIER_CLOSING_PREVIEW_CONTROLLER_SAVE_REPORT_PDF = "/reportCashierClosingPdf";
	public static final String CASHIER_CLOSING_PREVIEW_CONTROLLER_CASH_WEEK = "/cashWeeks";
	public static final String PARAMETER_CASH_MOVEMENTS = "cashMovements";
	public static final String CASHIER_CLOSING_PREVIEW_CONTROLLER_LAUNCH = "/launchs/cashier/{cashingClose}/operador/{userOperator}";
	
	//Cashier Closing Controller Preview Finish
	//Create 09/03/2024
	public static final String CASHIER_CLOSING_PREVIEW_FINISH_CONTROLLER = "/cashierClosingPreviewFinish/cashier-closing-finish";
	public static final String CASHIER_CLOSING_PREVIEW_FINISH_METHOD_RESULT = "adm/cashier-closing-preview-finish/cashier-closing-preview-finish-base";
	public static final String CASHIER_CLOSING_PREVIEW_FINISH_CONTROLLER_SAVE = "/saveCashierClosing";
	public static final String CASHIER_CLOSING_PREVIEW_FINISH_CONTROLLER_DETAIL_DOCUMENT_METHOD = "/detailDocument";
	public static final String CASHIER_CLOSING_PREVIEW_FINISH_CONTROLLER_SAVE_REPORT_PDF = "/reportCashierClosingPdf";
	public static final String CASHIER_CLOSING_PREVIEW_FINISH_CONTROLLER_CASH_WEEK = "/cashWeeks";
	public static final String CASHIER_CLOSING_PREVIEW_FINISH_CONTROLLER_FILTER_LAUNCH_CASH = "/filterCasingClosingLaunchByUser";
} 