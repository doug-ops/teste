package com.manager.systems.web.jobs.utils;

public class DatabaseConstants 
{
	//Global Tables
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_PARENT_ID = "parent_id";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_INTEGRATION_ID = "integration_id";
	public static final String COLUMN_INACTIVE = "inactive"; 
	public static final String COLUMN_USER_CREATION = "user_creation";
	public static final String COLUMN_USER_CREATION_NAME = "user_creation_name";
	public static final String COLUMN_CREATION_DATE = "creation_date";
	public static final String COLUMN_USER_CHANGE = "user_change";
	public static final String COLUMN_USER_CHANGE_NAME = "user_change_name";
	public static final String COLUMN_CHANGE_DATE = "change_date";
	public static final String COLUMN_INACTIVED = "inactive";
	public static final String COLUMN_VERSION_REGISTER = "version_register";
	public static final String COLUMN_EMAIL = "email";

	//jobs
	public static final String COLUMN_SYNC_TIMER = "sync_timer";
	public static final String COLUMN_RECORDS_PROCESSED = "records_processed";
	public static final String COLUMN_INITIAL_RECORD = "initial_record";
	public static final String COLUMN_FINAL_RECORD = "final_record";
	public static final String COLUMN_INITIAL_VERSION_RECORD = "initial_version_record";
	public static final String COLUMN_FINAL_VERSION_RECORD = "final_version_record";
	public static final String COLUMN_PROCESSING_STATUS = "processing_status";
	public static final String COLUMN_PROCESSING_MESSAGE = "processing_message";
	public static final String COLUMN_PROCESSING_DATA  = "processing_data";	
	public static final String COLUMN_ITEM_NAME  = "item_name";	
	
	//Document Movement
	public static final String COLUMN_CREDIT = "credit";	
	public static final String COLUMN_COMPANY_ID = "company_id";	
	public static final String COLUMN_PROVIDER_ID = "provider_id";
	public static final String COLUMN_BANK_ACCOUNT_ID = "bank_account_id";
	public static final String COLUMN_DOCUMENT_TYPE = "document_type";
	public static final String COLUMN_DOCUMENT_NUMBER = "document_number";
	public static final String COLUMN_DOCUMENT_VALUE = "document_value";
	public static final String COLUMN_DOCUMENT_NOTE = "document_note";
	public static final String COLUMN_DOCUMENT_STATUS = "document_status";
	public static final String COLUMN_PAYMENT_VALUE = "payment_value";
	public static final String COLUMN_PAYMENT_DISCOUNT = "payment_discount";
	public static final String COLUMN_PAYMENT_EXTRA = "payment_extra";
	public static final String COLUMN_PAYMENT_RESIDUE = "payment_residue";
	public static final String COLUMN_PAYMENT_DATA = "payment_data";
	public static final String COLUMN_PAYMENT_EXPIRY_DATA = "payment_expiry_data";
	public static final String COLUMN_PAYMENT_USER = "payment_user";
	public static final String COLUMN_NFE_NUMBER = "nfe_number";
	public static final String COLUMN_NFE_SERIE_NUMBER = "nfe_serie_number";
	public static final String COLUMN_GENERATE_BILLET = "generate_billet";
	public static final String COLUMN_FINANCIAL_GROUP_ID = "financial_group_id";
	public static final String COLUMN_FINANCIAL_SUB_GROUP_ID = "financial_sub_group_id";
	public static final String COLUMN_DOCUMENT_PARENT_ID = "document_parent_id";
	
	//Movement Data Product Synchronize
	public static final String COLUMN_MOVEMENT_ID = "movement_id";
	public static final String COLUMN_PRODUCT_ID = "product_id";
	public static final String COLUMN_COMPANY_DESCRIPTION = "company_description";
	public static final String COLUMN_CREDIT_IN = "credit_in";
	public static final String COLUMN_CREDIT_OUT = "credit_out";
	public static final String COLUMN_READING_DATE = "reading_date";
	public static final String COLUMN_INITIAL_DATE = "initial_date";
	public static final String COLUMN_MOVEMENT_TYPE = "movement_type";
	public static final String COLUMN_PROCESSING = "processing";	
	public static final String COLUMN_TERMINAL_TYPE = "terminal_type";
	public static final String COLUMN_COMPANY_NAME = "company_name";
	public static final String COLUMN_REGISTER_ENABLED = "register_enabled";
	
	//Send Email Document
	public static final String COLUMN_IS_SEND = "is_send";
	
	//Send Email Erros
	//23/08/2022
	public static final String COLUMN_PROCESS_DESCRIPTION = "process_description";
	public static final String COLUMN_ERROR_NOTE = "error_note";
	public static final String COLUMN_HAS_SEND = "has_send";
	public static final String COLUMN_SEND_DATE = "send_date";
}