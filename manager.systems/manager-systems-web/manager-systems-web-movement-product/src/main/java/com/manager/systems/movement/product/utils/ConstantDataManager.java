package com.manager.systems.movement.product.utils;

public final class ConstantDataManager 
{	
	//Home
	public static final String PORTAL_MOVEMENT_PRODUCT_CONTROLLER_HOME = "/home";

	//Methods
	public static final String METHOD_BASE = "base";
	public static final String METHOD_INDEX = "index";
	public static final String METHOD_LOGIN = "login";
	
	//Http
	public static final String USER_AGENT = "User-Agent";
	public static final String MOZILA_USER_AGENT = "Mozilla/5.0";
	public static final String POST_METHOD = "POST";
	public static final String GET_METHOD = "GET";
	public static final String HOST = "Host";
	public static final String ACCEPT = "Accept";
	public static final String ACCEPT_LANGUAGE = "Accept-Language";
	public static final String ACCEPT_TYPE_ALL = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
	public static final String ACCEPT_LANGUAGE_EN_US = "en-US,en;q=0.5";
	public static final String CONNECTION = "Connection";
	public static final String CONNECTION_KEEP_ALIVE = "keep-alive";
	public static final String REFERER = "Referer";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
	public static final String CONTENT_LENGTH = "Content-Length";
	public static final String COOKIE = "Cookie";	
	public static final String SET_COOKIE = "Set-Cookie";
	public static final String FORM = "form";
	public static final String INPUT = "input";
	public static final String NAME = "name";
	public static final String VALUE = "value";
	public static final String TABLE = "table";
	public static final String TBODY = "tbody";
	public static final String TR = "tr";
	public static final String TD = "td";
	
	//Geral
	
	//User Parameters
	public static final String PARAMETER_USERNAME = "username";
	public static final String PARAMETER_PASSWORD = "password";
	public static final String PARAMTER_USERS_ID = "usersId";
	public static final String PARAMETER_USER_CHIDRENS_PARENT = "userChildrensParent";

	//Company Parameters
	public static final String PARAMETER_COMPANY_ID = "companyId";
	public static final String PARAMETER_COMPANY_ID_DESTINY = "companyIdDestiny";
	public static final String PARAMETER_ID  = "id";
	public static final String SELECTED_COMPANY  = "selectedCompany";
	public static final String EXPIRATION_DATE  = "expirationDate";
	public static final String BANK_ACCOUNT_ORIGIN_ID  = "bankAccountOriginId";
	public static final String BANK_ACCOUNT_DESTINY_ID  = "bankAccountDestinyId"; 
	public static final String WEEK_YEAR  = "weekYear";
	
	//Movement
	public static final String PARAMETER_MOVEMENT_ID = "movementId";
	public static final String PARAMETER_DOCUMENT_PARENT_ID = "documentParentId";

	//Product Parameters
	public static final String PARAMETER_PRODUCT_ID = "productId";
	
	//Product Movement
	public static final String PARAMETER_PRODUCT_MOVEMENT_ID = "movementId";
	public static final String PARAMETER_PRODUCT_INITIAL_INPUT = "initialInput";
	public static final String PARAMETER_PRODUCT_FINAL_INPUT = "finalInput";
	public static final String PARAMETER_PRODUCT_INITIAL_OUTPUT = "initialOutput";
	public static final String PARAMETER_PRODUCT_FINAL_OUTPUT = "finalOutput";
	public static final String PARAMETER_PRODUCT_INITIAL_CLOCK = "initialClock";
	public static final String PARAMETER_PRODUCT_FINAL_CLOCK = "finalClock";
	public static final String PARAMETER_PRODUCT_ENABLE_CLOCK = "enableClock";
		
	//Constroller
	public static final String RESULT_LOGIN = "login";
	public static final String RESULT_HOME = "home-base";
	public static final String RESULT_REDIRECT_HOME = "redirect:/home";
	
	//Login
	public static final String PORTAL_MOVEMENT_PRODUCT_CONTROLLER = "/*";
	public static final String PORTAL_MOVEMENT_PRODUCT_CONTROLLER_BASE = "/";	
	public static final String PORTAL_CONTROLLER_INDEX = "/login.html";
	public static final String PORTAL_MOVEMENT_PRODUCT_CONTROLLER_LOGIN = "/login";
	public static final String PORTAL_MOVEMENT_PRODUCT_CONTROLLER_LOGOUT = "/logout";	
	public static final String PORTAL_MOVEMENT_PRODUCT_CONTROLLER_ACESSO_EXPIRADO = "/acessoExpirado";
	
	//Common COntroller
	public static final String COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD = "/open";
		
	//Product Movement
	public static final String PRODUCT_MOVEMENT_CONTROLLER = "/product-movement";
	public static final String PRODUCT_MOVEMENT_OPEN_METHOD_RESULT = "movements/product-movement/product-movement-base";
	public static final String PRODUCT_MOVEMENT_CONTROLLER_GETALL_PRODUCTS_COMPANY = "/getAllProductsByCompany";
	public static final String PRODUCT_MOVEMENT_CONTROLLER_GET_DATA_PRODUCT = "/getDataProduct";
	public static final String PRODUCT_MOVEMENT_CONTROLLER_SAVE_DATA_PRODUCT = "/saveDataProduct";
	public static final String PRODUCT_MOVEMENT_CONTROLLER_GETL_MOVEMENT_PRODUCT_OFF_SYSTEM_OLD_BY_COMPANY = "/getMovementProductOffSystemOldByCompany/{companyId}";
	public static final String PRODUCT_WITHDRAWAL_MOVEMENT_CONTROLLER = "/productWithdrawalMovement";
	public static final String PRODUCT_WITHDRAWA_MOVEMENT_OPEN_METHOD_RESULT = "movements/product-movement/product-withdrawal-movement-base";
		
	//Create 14/04/2022
	public static final String COMMON_CONTROLLER_MAINTENANCE_COMPANY_HOME_METHOD = "/companyHome";
	public static final String COMPANY_PREVIEW_MOVEMENT_OPEN_METHOD_RESULT = "company-home-base";
	
	//Company Movement
	public static final String COMPANY_MOVEMENT_CONTROLLER = "/company-movement";
	public static final String COMPANY_MOVEMENT_OPEN_METHOD_RESULT = "movements/company-movement/company-movement-base";
	public static final String COMPANY_MOVEMENT_CONTROLLER_GET_DATA_COMPANY = "/getDataCompany";
	public static final String COMPANY_MOVEMENT_CONTROLLER_PREVIEW_PROCESS_MOVEMENT = "/previewProcessMovement";
	public static final String COMPANY_MOVEMENT_CONTROLLER_PROCESS_MOVEMENT = "/processMovement";
	public static final String COMPANY_MOVEMENT_REPORT_PDF = "/reportCompanyMovementPdf/{documentParentId}/{companyId}";
	
	//21/04/2022
	public static final String COMPANY_MOVEMENT_EXECUTION_CONTROLLER = "/companyMovementExecution";
	public static final String COMPANY_MOVEMEN_EXECUTION_CONTROLLER_OPEN_METHOD_RESULT = "movements/company-movement-execution/company-movement-execution-base";
	public static final String FILTER_COMPANY_MOVEMENT_EXECUTION_CONTROLLER = "/filterCompanyMovementExecution";
	public static final String COMPANY_MOVEMENT_EXECUTION_CONTROLLER_SAVE = "/save";
	public static final String PARAMETER_COMPANYS = "companyId";
	public static final String PARAMETER_FILD_CHANGE = "fildChange";
	public static final String PARAMETER_MOTIVE = "motive";

	//Bravo 
	public static String HOST_BRAVO_URL = "billing.bravo.games";
	public static String LOGIN_BRAVO_URL = "https://billing.bravo.games/accounts/login/?next=/";
	public static String MACHINES_BRAVO_URL = "https://billing.bravo.games/machines";
	public static String ROOMS_BRAVO_URL = "https://billing.bravo.games/rooms";
	public static String PARAM_CSR_TOKEN = "csrfmiddlewaretoken";
	public static final String BRAVO_MOVEMENT_CONTROLLER = "/bravo-movement";
	public static final String BRAVO_MOVEMENT_CONTROLLER_GET_DATA_MOVEMENT = "/getMovment";

	//public static String USERNAME_BRAVO = "Nine";
	//public static String PASSWORD_BRAVO = "bmawselectro";	
	
	//Cashier Closing Controller Preview
	//Create 26/01/2024
	public static final String CASHIER_CLOSING_PREVIEW_CONTROLLER = "/cashierClosingPreview/cashier-closing";
	public static final String CASHIER_CLOSING_PREVIEW_METHOD_RESULT = "movements/cashing-closing-preview/cashier-closing-preview-base";
	public static final String PARAMETER_FRAME_URL = "frameUrl";
	public static final String LOOPBACK_IP = "127.0.0.1";
}