package com.manager.systems.web.common.utils;

public final class ConstantDataManager 
{
	//Application
	public static final String PROJECT_ALIAS = "manager-systems";
	public static final String PROJECT_XML_ALIAS = "xml";
	public static final String VERSAO_APLICATIVO = "2.1";
	public static final String SEND_EMAIL_AMAZON = "send_email_amazon";
	public static final String TIMER_SINCRONISMO = "timer.sincronismo";
	
	public static final Boolean TRUE = true;
	public static final Boolean FALSE = false;
	public static final String TRUE_STRING = "true";
	public static final String FALSE_STRING = "false";
	public static final String PATTERN_YYYY_MM_DD ="yyyy-MM-dd";	
	public static final String PATTERN_HH_MM_SS ="HH:mm:ss";
	public static final String PATTERN_DD_MM_YYYY_HH_MM_SS ="dd/MM/yyyy HH:mm";	
	public static final String PATTERN_DD_MM_YYYY ="dd/MM/yyyy";	
	public static final String PATTERN_YY_MM_DD_HH_MM_SS ="yy-MM-dd HH:mm";	
	public static final String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String BARRA = "/";
	public static final String TRACO = "-";
	public static final String VIRGULA = ",";
	public static final String UNDERSCORE = "_";
	public static final String BLANK = "";
	public static final String PONTO = ".";
	public static final String SPACE = " ";
	public static final String ZERO_STRING = "0";
	public static final String VIRGULA_STRING = ",";
	public static final String PONTO_VIRGULA_STRING = ";";
	public static final String SIMPLE_ASPAS = "'";
	public static final String IGUAL = "=";
	public static final String QUESTION = "?";
	public static final String AND = "AND";
	public static final String ACTIVE = "AT";
	public static final String INACTIVE = "IN";
	public static final String STATUS_DESCRIPTION = "status";
	public static final String MESSAGE_CODE = "messageCode";
	public static final String POPULATE_FORM = "populateForm";

	public static final String MESSAGE_DESCRIPTION = "message";
	public static final String S = "S";
	public static final String N = "N";
	public static final String SCAPE_PIPE = "\\|";
	public static final String PT = "pt";
	public static final String BR = "BR";
	public static final String ON = "on";
	public static final String OFF = "off";
	public static final String TWO_ZERO_STRING = "00";
	
	//Pacotes
	public static final String PACKAGE_PORTAL = "com.manager.systems";
	
	//Applications
	public static final String APPLICATION_PORTAL = "portal";
	public static final String APPLICATION_RETAGUARDA = "retaguarda";
	public static final String APPLICATION_JOBS = "jobs";
	
	//Datasources
	public static final String DATA_SOURCE_PORTAL = "java:comp/env/jdbc/portal";
	public static final String DATA_SOURCE_RETAGUARDA = "java:comp/env/jdbc/retaguarda";
	
	//TimeZone
	public static final String TIMEZONE_SAO_PAULO = "America/Sao_Paulo";
	
	// Parameters XML
	public static final String UTF_8 = "UTF-8";
	public static final String ISO_8859 = "ISO-8859-1";
	public static final String ALIAS_ROOT = "root";
	public static final String RESPONSE_TYPE_XML = "text/xml";
	public static final String RESPONSE_TYPE_JSON = "application/json";
	public static final String RESPONSE_TYPE_TEXT = "text/plain";
	public static final String RESPONSE_CACHE_CONTROL = "Cache-Control";
	public static final String RESPONSE_NO_CACHE = "no-cache";
	public static final String RESPONSE_TYPE_EXCEL = "application/vnd.ms-excel";
	
	// Parameters PDF
	public static final String RESPONSE_TYPE_PDF = "application/pdf";
	public static final String RESPONSE_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	public static final String RESPONSE_ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	public static final String RESPONSE_ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	public static final String RESPONSE_GET = "GET";
	public static final String RESPONSE_POST = "POST";
	public static final String RESPONSE_PUT = "PUT";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_DISPOSITION = "Content-Disposition";
	public static final String INLINE = "inline";
	public static final String FILENAME = "filename";
	public static final String RESPONSE_NO_STORE = "no-store";
	public static final String RESPONSE_MUST_REVALIDATE = "must-revalidate";
	public static final String RESPONSE_PRAGMA = "Pragma";
	public static final String RESPONSE_EXPIRES = "Expires";	
		
	public static final String ERROR_BUSSINESS = "errorBussiness";
	
	//Global variables
	public static final String MESSAGE = "message";
	
	//Global Parameters
	public static final String PARAMETER_USER = "user";
	public static final String PARAMETER_INACTIVE = "inactive";
	public static final String PARAMETER_REPORT = "report";
	public static final String PARAMETER_ITENS = "itens";
	public static final String PARAMETER_ITEM = "item";
	public static final String PARAMETER_REPORT_OLD = "reportOld";
	public static final String PARAMETER_DATE_FROM = "dateFrom";
	public static final String PARAMETER_DATE_TO = "dateTo";
	public static final String PARAMETER_DATA = "data";
	public static final String PARAMETER_DATA_SUB_GROUP = "dataSubGroup";
	public static final String PARAMETER_REPORT_GROUP_MOVEMENTS = "reportGroupMovements";
	public static final String PARAMETER_USER_TESTE = "1";
	public static final String PARAMETER_ORIGIN = "origin";	
	
	//Person parameters
	public static final String PARAMETER_PERSON_ID = "personId";
	
	//Company parameters
	public static final String PARAMETER_COMPANY_ID = "companyId";
	public static final String PARAMETER_COMPANYS = "companys";
	
	//Products parameters
	public static final String PARAMETER_PRODUCT_ID = "productId";
	public static final String PARAMETER_PRODUCTS = "products";
	public static final String PARAMETER_PRODUCT = "product";
	
	//Company Movement
	public static final String PARAMETER_PREVIEW = "preview";
	
	//Global Static Messages
	public static final String GLOBAL_MSG_CONNECTION_ERROR = "Erro ao criar conex\u00e3o com banco de dados.\nFavor entrar em contato com o Administrador do Sistema.";

	//Global Controller Messages
	public static final String GLOBAL_CONNECTION_ERROR = "global.conection.error";
	public static final String GLOBAL_SAVE_DATA_SUCCESS = "global.save.data.success";
	public static final String GLOBAL_SAVE_DATA_ERROR = "global.save.data.error";
	public static final String GLOBAL_DATA_REQUIRED_ERROR = "global.data.required.error";
	public static final String LOGIN_USER_PASSWORD_INVALID = "login.user.password.invalid";
	public static final String LOGIN_ACCESS_EXPIRED = "login.access.expired";
	
	//Exceptions
	public static final String EXCEPTION_DUPLICATE_ENTRY = "Duplicate entry";
	public static final String FINANCIAL_TRANSFER_ITEM_ORDER_INVALID = "financial.transfer.group.item.order.invalid";	
	
	//Company Controller
	public static final String ADDRESS_MAINTENANCE_CONTROLLER = "/address";
	public static final String ADDRESS_MAINTENANCE_CONTROLLER_SEARCH_BY_ZIP_CODE = "/seachByZipCode";
	
	//Common Administration Controller
	public static final String COMMON_ADM_CONTROLLER = "/adm/common";
	public static final String COMMON_ADM_CONTROLLER_NEXT_CODE_METHOD = "/nextCode";
	
	//Parameters
	public static final String PARAMETER_ZIP_CODE = "zipCode";
	public static final String PARAMETER_ADDRESS = "address";
	public static final String PARAMETER_ADDRESS_STATES = "addressStates";
	public static final String PARAMETER_ADDRESS_COUNTRIES = "addressCountries";
	public static final String PARAMETER_LABELS_PAGE = "labelsPage";
	public static final String PARAMETER_PERMISSIONS = "permissions";
	
	//Pages Parameters 
	public static final String PARAMETER_PAGE_TITLE = "pageTitle";
	
	//Labels
	public static final String LABEL_COMMON_MAINTENANCE = "common.label.maintenance";
	public static final String LABEL_COMMON_OF = "common.label.of";
	
	//Common Administration Controller
	public static final String PARAMETER_TABLE_NAME = "tableName";
	public static final String PARAMETER_INITIAL_CODE = "initialCode";
	public static final String PARAMETER_NEXT_CODE = "nextCode";
	
	//Movement Type
	public static final String PARAMETER_MOVEMENT_CLOSED = "A";
	public static final String PARAMETER_MOVEMENT_READ = "L";
	
	//Movement Company
	public static final String PARAMETER_MOVEMENT_COMPANY = "movementCompany";
	public static final String PARAMETER_LAST_MOVEMENT_COMPANY = "lastMovementCompany";
	public static final String PARAMETER_FINANTIAL_GROUP_TRANSFER = "finantialGroupTransfer";
	
	//Bank Account Statement
	public static final String PARAMETER_OPERATION = "operation";
	
	//Messages
	public static final String MESSAGE_COMMON_ADMN_INVALID_TABLE = "common.adm.invalid.table";
	public static final String MESSAGE_COMMON_ADMN_INVALID_INITIAL_CODE = "common.adm.invalid.initial_code";
	public static final String MESSAGE_COMMON_INVALID_ZIP_CODE = "common.address.zip.code";
	
	//Global
	public static final String MESSAGE_DOCUMENT_TYPE_INVALID = "document.type.invalid";	
	public static final String MESSAGE_DOCUMENT_VALUE_INVALID = "document.value.invalid";	
	public static final String MESSAGE_DOCUMENT_DATE_INVALID = "document.date.invalid";	
	public static final String MESSAGE_DOCUMENT_NUMBER_INVALID = "document.number.invalid";	
	public static final String MESSAGE_DOCUMENT_NOTE_INVALID = "document.note.invalid";
	public static final String MESSAGE_MOVEMENT_TYPE_INVALID = "movement.type.invalid";	
	public static final String MESSAGE_DOCUMENT_STATUS_INVALID = "document.status.invalid";	
	public static final String MESSAGE_DOCUMENT_ID_INVALID = "document.id.invalid";	
	public static final String MESSAGE_EXPIRE_DATE_INVALID = "document.expire.date.invalid";
	public static final String MESSAGE_DOCUMENT_PAID_VALUE_INVALID = "document.paid.value.invalid";
	
	//Provider
	public static final String MESSAGE_PROVIDER_INVALID = "provider.invalid";	
	
	//Company Messages
	public static final String MESSAGE_COMPANY_INVALID = "company.invalid";
	public static final String MESSAGE_MOVEMENT_INVALID = "movement.invalid";	
	
	//Product Messages
	public static final String MESSAGE_PRODUCT_INVALID = "product.invalid";
	
	//Global
	public static final String MESSAGE_OPERATION_INVALID = "global.operation.invalid";
	public static final String MESSAGE_INITIAL_DATE_INVALID = "global.initial.date.invalid";
	public static final String MESSAGE_FINAL_DATE_INVALID = "global.final.date.invalid";
	public static final String MESSAGE_INVALID_DATE = "global.date.invalid";
	public static final String MESSAGE_TYPE_INVALID = "global.type.invalid";
	
	//Person Maintenance
	public static final String MESSAGE_INVALID_PERSON_ID = "person.id.invalid";
	public static final String MESSAGE_NOT_FOUND_COMPANY = "company.adm.not.found";
	public static final String MESSAGE_OBJECT_TYPE_NOT_INFORMED = "person.object.type.not.informed";
	public static final String MESSAGE_OBJECT_TYPE_INVALID = "person.object.type.invalid";
	public static final String MESSAGE_ESTABLISHMENT_TYPE_INVALID = "person.establishment.type.not.informed";
	
	//Bank Account
	public static final String MESSAGE_NOT_FOUND_BANK_ACCOUNT = "bank.account.adm.not.found";
	public static final String MESSAGE_BANK_ACCOUNT_INVALID = "bank.account.invalid";

	//Financial Group
	public static final String MESSAGE_NOT_FOUND_FINANCIAL_GROUP = "financial.group.adm.not.found";
	public static final String MESSAGE_FINANCIAL_GROUP_INVALID = "financial.group.invalid";
	
	//Financial Sub Group
	public static final String MESSAGE_FINANCIAL_SUB_GROUP_INVALID = "financial.sub.group.invalid";
	
	//Product
	public static final String MESSAGE_NOT_FOUND_PRODUCT = "product.adm.not.found";
	
	//Group
	public static final String MESSAGE_GROUP_INVALID = "group.invalid";
	
	//SubGroup
	public static final String MESSAGE_SUBGROUP_INVALID = "subgroup.invalid";
	
	//Product Movement Messages
	public static final String MESSAGE_PRODUCT_MOVEMENT_ID_INVALID = "product.movement.id.invalid";
	public static final String MESSAGE_PRODUCT_INITIAL_INPUT_INVALID = "product.initial.input.invalid";
	public static final String MESSAGE_PRODUCT_FINAL_INPUT_INVALID = "product.final.input.invalid";
	public static final String MESSAGE_PRODUCT_INITIAL_OUTPUT_INVALID = "product.initial.output.invalid";
	public static final String MESSAGE_PRODUCT_FINAL_OUTPUT_INVALID = "product.final.output.invalid";
	public static final String MESSAGE_PRODUCT_INITIAL_CLOCK_INVALID = "product.initial.clock.invalid";
	public static final String MESSAGE_PRODUCT_FINAL_CLOCK_INVALID = "product.final.clock.invalid";
	public static final String MESSAGE_PRODUCT_FINAL_INPUT_LESS_THAN_INVALID = "product.final.input.less.than.invalid";
	public static final String MESSAGE_PRODUCT_FINAL_OUTPUT_LESS_THANINVALID = "product.final.output.less.than.invalid";
	public static final String MESSAGE_PRODUCT_FINAL_CLOCK_LESS_THAN_INVALID = "product.final.clock.less.than.invalid";
	public static final String MESSAGE_COMPANY_PROCESSING_SUCCESS = "company.processing.success";	
	
	//11/07/2022
	public static final String MESSAGE_COMPANY_MOTIVE_INVALID = "company.motive.invalid";
	
	//Financial Document
	public static final String MESSAGE_FINANCIAL_DOCUMENT_INVALID = "financial.document.invalid";
	public static final String MESSAGE_FINANCIAL_GROUP_DOCUMENT_INVALID = "financial.group.document.invalid";
	public static final String MESSAGE_FINANCIAL_DOCUMENT_TYPE_INVALID = "financial.document.type.invalid";	
	public static final String FINANCIAL_GROUP_DATA_ERROR = "financial.group.data.error";
	public static final String FINANCIAL_GROUP_REGROUP_ERROR = "financial.group.regroup.error";
	
	//Payment
	public static final String MESSAGE_PAYMENT_DATE_INVALID = "payment.date.invalid";	
	public static final String MESSAGE_PAYMENT_EXPIRY_DATE_INVALID = "payment.expiry.date.invalid";
	public static final String MESSAGE_PAYMENT_STATUS_INVALID = "payment.status.invalid";
	public static final String MESSAGE_PAYMENT_INSTALLMENT_INVALID = "payment.installment.invalid";
	public static final String MESSAGE_PAYMENT_INSTALLMENT = "payment.installment";
	
	//Labels
	//Bank Account Statement
	public static final String LABEL_MOVEMENT_EXTRACT = "EXTRATO DE MOVIMENTA\u00c7\u00c3O DE CONTAS";
	public static final String LABEL_MOVEMENT_EXTRACT_COMPANY = "EXTRATO DAS EMPRESAS";
	public static final String LABEL_ANALYTICAL = "ANAL\u00cdTICO";
	public static final String LABEL_ACCOUNTS = "CONTAS";
	public static final String LABEL_ACCOUNT = "CONTA";
	public static final String LABEL_START = "IN\u00cdCIO";
	public static final String LABEL_FINAL = "FINAL";
	public static final String LABEL_DESCRIPTION = "DESCRI\u00c7\u00c3O";
	public static final String LABEL_BALANCE = "SALDO";
	public static final String LABEL_INITIAL_BALANCE = "SALDO INICIAL";
	public static final String LABEL_FINAL_BALANCE = "SALDO FINAL";
	public static final String LABEL_DESCRIPTION_ABBREVIATED = "DESCR.";
	public static final String LABEL_DESCRIPTION_INITIAL = "INICIAL";
	public static final String LABEL_TOTAL_CREDIT = "TOTAL CR\u00c9DITO";
	public static final String LABEL_TOTAL_DEBIT = "TOTAL D\u00c9BITO";
	
	//Bravo
	public static final String PARAMETER_MOVEMENT_BRAVO = "movementBravo";
	public static final String BRAVO_SYNCHONYZE_DATA_SUCCESS = "bravo.synchonize.data.success";
	public static final String BRAVO_SYNCHONYZE_DATA_NOT_FOUND = "bravo.synchonize.data.error";	
	
	//03/02/2022
	//Menssager Product Movement
	public static final String MSG_HAS_NO_MOVEMENT_PRODUCT = "Os produtos %s configurados na empresa %s estão sem lançamento, Favor lançar os mesmos antes de processar.";

	public static final String PARAMETER_DASHBOARD = "dashboard";
	
	//31/10/2022
	//Products Site Bravo
	public static final String PARAMETER_ACUMULADO_DISPLAY_BRAZIL = "Acumulado Display Brazil";
	public static final String PARAMETER_SUN_BRAZIL_M = "Sun Brazil M";
}