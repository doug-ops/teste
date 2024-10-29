package com.manager.systems.web.jobs.utils;

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
	public static final String MESSAGE_DESCRIPTION = "message";
	public static final String S = "S";
	public static final String N = "N";
	public static final String SCAPE_PIPE = "\\|";
	public static final String PT = "pt";
	public static final String BR = "BR";
	public static final String ON = "on";
	public static final String OFF = "off";
	
	//Pacotes
	public static final String PACKAGE_JOB = "com.manager.systems";
	
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
	
	//Job Tasks 
	public static final String TASK_SINC_MOVEMENT_DOCUMENT = "Sinc Documents";
	public static final String TASK_SINC_PRODUCT_MOVEMENT = "Sinc Product Movement";
	public static final String TASK_SEND_EMAIL_MOVEMENT = "Send Email Movement";
	//Job Bank Account Statement Tasks 
	//Create Date 13/12/2021 
	public static final String TASK_SEND_EMAIL_BANK_ACCOUNT_STATEMENT = "Send Email Banck Account Statement";

	//Creation date 14/05/2024
	public static final String TASK_SINC_CASHING_CLOSING_MOVEMENT = "Generate Cashing Closing Movement";
	
	//Job Erro Product
	//Create Date 24/08/2022 
	public static final String TASK_SEND_EMAIL_ERROR = "Send Email Error";
	
	//Global Static Messages
	public static final String GLOBAL_MSG_CONNECTION_ERROR = "Erro ao criar conex\u00e3o com banco de dados.\nFavor entrar em contato com o Administrador do Sistema.";
	public static final String GLOBAL_MSG_INITIAL_SINC = "Initial Sinc";
	public static final String GLOBAL_MSG_ITENS_TO_SINC = "Itens to Sinc";
	public static final String GLOBAL_MSG_SAVE_ITENS = "Save itens";
	public static final String GLOBAL_MSG_SAVE_JOB = "Save JOB";
	public static final String GLOBAL_MSG_SINC_FINISH = "Sinc Finish";
	public static final String GLOBAL_MSG_PROCESS_MOVEMENT_INIT = "Process Movement Init";
	public static final String GLOBAL_MSG_PROCESS_MOVEMENT_FINISH = "Process Movement Finish";
	public static final String GLOBAL_MSG_INITIALIZING_JOBS = "Initializing Jobs";
	public static final String GLOBAL_MSG_FIND_JOBS = "Find %s jobs to scheduller";
	public static final String GLOBAL_MSG_SCHEDULLER = "Scheduller";
	public static final String GLOBAL_MSG_FILTERED_MOVEMENT = "Filtered movement";
	public static final String GLOBAL_MSG_HAS_NO_MOVEMENT_TO_PROCESS = "Has no movement to processing";
	public static final String GLOBAL_MSG_SEND_EMAILS = "Send Emails itens";
	
	//Messages
	public static final String MSG_PROCESS_MOVMENT_ERROR = "Erro ao processar movimento.\nFavor entrar em contato com o Administrador do Sistema.";
	
	//Job process next week movement
	//Create Date 02/09/2023 
	public static final String TASK_PROCESS_NEXT_WEEK_MOVEMENT = "Process next week movement";
}