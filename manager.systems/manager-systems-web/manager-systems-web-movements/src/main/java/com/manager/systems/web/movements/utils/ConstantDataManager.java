package com.manager.systems.web.movements.utils;

public final class ConstantDataManager 
{
	//Controller constants
	public static final String RESULT_HOME = "home-base";
	
	//Common controller methods
	public static final String COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD = "/open";
	public static final String COMMON_CONTROLLER_MAINTENANCE_OPEN_GROUP_METHOD = "/openGroup";
	public static final String COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD = "/save";
	public static final String COMMON_CONTROLLER_MAINTENANCE_INACTIVE_METHOD = "/inactive";
	public static final String COMMON_CONTROLLER_MAINTENANCE_DETAIL_METHOD = "/detail";
	public static final String COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD = "/filter";
	public static final String COMMON_CONTROLLER_MAINTENANCE_FILTER_GROUP_METHOD = "/filterGroup";
	public static final String COMMON_CONTROLLER_MAINTENANCE_AUTOCOMPLETE_METHOD = "/autocomplete";
	public static final String COMMON_CONTROLLER_REPORT_PDF_METHOD = "/reportPdf";
	public static final String COMMON_CONTROLLER_MAINTENANCE_FILTER_REPORT_METHOD = "/filterReport";

	//Document Movement Controller
	public static final String DOCUMENT_MOVEMENT_CONTROLLER = "/movement/document-movement";
	public static final String DOCUMENT_MOVEMENT_OPEN_METHOD_RESULT = "movements/document-movement/document-movement-base";
	public static final String DOCUMENT_MOVEMENT_OPEN_GROUP_METHOD_RESULT = "movements/document-movement-group/document-movement-group-base";
	public static final String DOCUMENT_MOVEMENT_CONTROLLER_DETAIL_DOCUMENT_METHOD = "/detailDocument";

	//Bank Account Statement Controller
	public static final String BANK_ACCOUNT_STATEMENT_CONTROLLER = "/movement/bankAccountStatement";
	public static final String BANK_ACCOUNT_STATEMENT_CONTROLLER_OPEN_METHOD_RESULT = "movements/bank-account-statement/bank-account-statement-base";
	public static final String BANK_ACCOUNT_STATEMENT_CONTROLLER_REPORT_PDF_METHOD = "/reportPdf";
	

	//Provider Statement Controller
	public static final String PROVIDER_STATEMENT_CONTROLLER = "/movement/providerStatement";
	public static final String PROVIDER_STATEMENT_CONTROLLER_OPEN_METHOD_RESULT = "movements/provider-statement/provider-statement-base";
	public static final String PROVIDER_STATEMENT_CONTROLLER_REPORT_PDF_METHOD = "/reportPdf";
	
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
	public static final String PARAMETER_DESCRIPTION = "description";
	public static final String PARAMETER_IS_MONTH = "isMonth";
	public static final String PARAMETER_IS_DOCUMENT_MOVEMENT = "isDocumentMovement";
	
	//Bank Account Maintenance
	public static final String PARAMETER_BANK_ACCOUNT_ID = "bankAccountId";
	public static final String PARAMETER_BANK_ACCOUNTS = "bankAccounts";
	
	//Provider Statement
	public static final String PARAMETER_PROVIDER = "provider";
	
	//Cash Statement
	public static final String PARAMETER_COMPANY = "company";
	
	
	//Financial Group
	public static final String PARAMETER_FINANCIAL_GROUP_ID = "financialGroupId";
	public static final String PARAMETER_FINANCIAL_GROUPS = "financialGroups";

	//Financial SubGroup
	public static final String PARAMETER_FINANCIAL_SUB_GROUP_ID = "financialSubGroupId";
	public static final String PARAMETER_FINANCIAL_SUB_GROUPS = "financialSubGroups";
	
	//Company
	public static final String PARAMETER_COMPANY_ID = "companyId";
	public static final String PARAMETER_COMPANYS_ID = "companysId";
	
	//Provider
	public static final String PARAMETER_PROVIDER_ID = "providerId";
	
	//Document Movement
	public static final String PARAMETER_DOCUMENT_TYPE = "documentType";
	public static final String PARAMETER_DOCUMENT_NUMBER = "documentNumber";
	public static final String PARAMETER_FILTER_DATE = "filterDate";
	public static final String PARAMETER_DATE = "date";
	public static final String PARAMETER_MOVEMENT_TYPE_ID = "movementTypeId";
	public static final String PARAMETER_CREDIT_ALIAS = "CR";
	public static final String PARAMETER_DEBIT_ALIAS = "DE";
	public static final String PARAMETER_OPEN_ALIAS = "AB";
	public static final String PARAMETER_CLOSE_ALIAS = "BA";
	public static final String PARAMETER_REMOVE_ALIAS = "EX";
	public static final String PARAMETER_CONFERED_ALIAS = "CO";
	public static final String PARAMETER_PENDING_ALIAS = "PE";
	public static final String PARAMETER_PROVISIONED_ALIAS = "PR";
	public static final String PARAMETER_TRANSFER = "TRANSF";
	public static final String PARAMETER_TRANSFER_HAB = "HAB";
	public static final String PARAMETER_DOCUMENT_ID = "documentId";
	public static final String PARAMETER_DOCUMENT_PARENT_ID = "documentParentId";
	public static final String PARAMETER_DOCUMENTS_PARENT_ID = "documentsParentId";
	public static final String PARAMETER_ANALITIC = "analitic";
	public static final String PARAMETER_SINTETIC = "sintetic";
	
	//Bank Account Statement
	public static final String PARAMETER_GROUP_BY = "groupBy";
	public static final String PARAMETER_TYPE_DOCUMENT = "typeDocument";
	
	//Company Movement Execution
	public static final String COMPANY_MOVEMENT_EXECUTION_CONTROLLER = "/companyMovementExecution";
	public static final String FILTER_COMPANY_EXECUTION_MOVEMENT_CONTROLLER = "/filterCompanyMovementExecution";
	public static final String COMPANY_EXECUTION_MOVEMENT_CONTROLLER_OPEN_METHOD_RESULT = "movements/company-movement-execution/company-movement-execution-base";

	public static final String PARAMETER_FILD_CHANGE = "fildChange";
	public static final String PARAMETER_COMPANYS = "company";
	public static final String PARAMETER_MOTIVE = "motive";
	public static final String PARAMETER_DOCUMENT_NOTE = "documentNote";
	
	public static final String PARAMETER_DATE_FROM = "dateFrom";
	public static final String PARAMETER_DATE_TO = "dateTo";
	public static final String PARAMETER_MOVEMENT_PRODUCT = "p;";
	public static final String PARAMETER_MOVEMENT_TRANSFER = "t;";
	
	//Product Group
	//Create 11/08/2022
	public static final String PARAMETER_PRODUCT_GROUPS = "productGroups";
	public static final String PARAMETER_PRODUCT_GROUP_ID = "productGroup";
	
	//Product Sub-Group
	//Create 11/08/2022
	public static final String PARAMETER_PRODUCT_SUB_GROUP_ID = "productSubGroup";
	
	//Dre
	//Create 08/09/2022
	public static final String DRE_CONTROLLER = "/dre";
	public static final String DRE_OPEN_METHOD_RESULT = "movements/dre/dre-base";
	public static final String PARAMETER_USER_ID = "userId";
	public static final String PARAMETER_USERS = "users";
	
	public static final String PARAMETER_USER_CHIDRENS_PARENT = "userChidrensParent";
	public static final String PARAMETER_USER_CHIDRENS_PARENT_COMBOBOX = "userChildrensParentCombobox";	

	//Cashier Closing Controller
	//Create 08/10/2022
	public static final String DOCUMENT_CASHIER_CLOSING_CONTROLLER = "/cashierClosingg/cashier-closingg";
	public static final String DOCUMENT_CASHIER_CLOSING_METHOD_RESULT = "adm/cashier-closing/cashier-closing-base";
	public static final String CONTROLLER_SAVE_CASHIER_CLOSING = "/cashierClosing/saveCashierClosing";
	
	//Transfer Cashier Closing Account Origin
	//Create 28/10/2022
	public static final String PARAMETER_CASHIER_CLOSING_COMPANY_TRANSFER = "companyTransf";
	public static final String PARAMETER_DOCUMENT_ID_TRANSFER = "documentIdTransf";
	public static final String PARAMETER_DOCUMENT_PARENT_ID_TRANSFER = "documentParentIdTransf";
	public static final String PARAMETER_DOCUMENT_TYPE_TRANSFER = "documentTypeTransf";
	public static final String PARAMETER_PROVIDER_TRANSFER = "providerTransf";
	public static final String PARAMETER_CASHIER_CLOSING_GROUP_TRANSFER = "financialGroupTransf";
	public static final String PARAMETER_CASHIER_CLOSING_SUB_GROUP_TRANSFER = "financialSubGroupTransf";
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
	
	//Transfer Cashier Closing Account Dest
	//Create 28/10/2022
	public static final String PARAMETER_FINANCIAL_COMPANY_TRANSFER_DEST = "companyTransfDest";
	public static final String PARAMETER_DOCUMENT_ID_TRANSFER_DEST = "documentIdTransfDest";
	public static final String PARAMETER_DOCUMENT_PARENT_ID_TRANSFER_DEST = "documentParentIdTransfDest";
	public static final String PARAMETER_DOCUMENT_TYPE_TRANSFER_DEST = "documentTypeTransfDest";
	public static final String PARAMETER_PROVIDER_TRANSFER_DEST = "providerTransfDest";
	public static final String PARAMETER_CASHIER_CLOSING_GROUP_TRANSFER_DEST = "financialGroupTransfDest";
	public static final String PARAMETER_CASHIER_CLOSING_SUB_GROUP_TRANSFER_DEST = "financialSubGroupTransfDest";
	public static final String PARAMETER_MOVEMENT_TYPE_TRANSFER_DEST = "movementTypeTransfDest";
	public static final String PARAMETER_DOCUMENT_STATUS_TRANSFER_DEST = "documentStatusTransfDest";
	public static final String PARAMETER_DOCUMENT_VALUE_TRANSFER_DEST = "documentValueTransfDest";
	public static final String PARAMETER_PAYMENT_DATE_TRANSFER_DEST = "paymentDateTransfDest";
	public static final String PARAMETER_PAYMENT_EXPIRY_DATE_TRANSFER_DEST = "paymentExpiryDateTransfDest";
	public static final String PARAMETER_PAYMENT_STATUS_TRANSFER_DEST = "paymentStatusTransfDest";
	public static final String PARAMETER_BANK_ACCOUNT_TRANSFER_DEST = "bankAccountTransfDest";	
	public static final String PARAMETER_DOCUMENT_ENCODED_NOTE_TRANSFER_DEST = "documentNoteEncodedTransfDest";
	public static final String PARAMETER_INSTALLMENT_TRANSFER_DEST = "1";
	
	//Cost Center
	//Create 19/11/2022
	public static final String PARAMETER_FINANCIAL_COST_CENTERS = "financialCostCenters";
	public static final String PARAMETER_FILTER_FINANCIAL_COST_CENTER_ID = "filterFinancialCostCenter";
	public static final String PARAMETER_COMPANIES = "companies";
	public static final String PARAMETER_FINANCIAL_COST_CENTER = "financialCostCenter";
	
	//New document Create 07/01/2023
	public static final String NEW_DOCUMENT_CONTROLLER = "/movement/new-document";
	public static final String TRANSFER_VALUE_CONTROLLER = "/movement/transfer-value";
	public static final String TRANSFER_VALUE_CONTROLLER_OPEN_MONTH_METHOD = "/openMonth";
	public static final String DOCUMENT_MOVEMENT_OPEN_NEW_DOCUMENT_METHOD_RESULT = "movements/new-document/new-document-base";
	public static final String DOCUMENT_MOVEMENT_OPEN_METHOD_DOCUMENT_TRANSFER_VALUE_RESULT = "movements/transfer-value/transfer-value-base";
	
	public static final String PARAMETER_PAYMENT_VALUE_TRANSFER = "paymentValueTransf";
	public static final String PARAMETER_COMPANY_ID_TRANSFER = "companyIdTransf";
	public static final String PARAMETER_COMPANY_TRANSFER = "companyTransf";
	public static final String PARAMETER_ATTRIBUTES = "attributes";

} 