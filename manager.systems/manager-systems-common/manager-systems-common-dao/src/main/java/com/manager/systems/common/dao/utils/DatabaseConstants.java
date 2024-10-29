package com.manager.systems.common.dao.utils;

public abstract class DatabaseConstants
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
	public static final String COLUMN_REGISTER_TYPE = "register_type"; 
	public static final String COLUMN_NEXT_ID = "next_id";
	public static final String COLUMN_ROWNUM = "rownum";
	public static final String COLUMN_INITIAL_DATE_LONG = "initial_date_long";
	public static final String COLUMN_FINAL_DATE_LONG = "final_date_long";
	
	//Person
	public static final String COLUMN_PERSON_ID = "person_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_ALIAS_NAME = "alias_name";
	public static final String COLUMN_PERSON_TYPE = "person_type";
	public static final String COLUMN_OBJECT_TYPE = "object_type";
	public static final String COLUMN_CPF_CNPJ = "cpf_cnpj";
	public static final String COLUMN_RG = "rg";
	public static final String COLUMN_IE = "ie";
	public static final String COLUMN_ADDRESS_ZIP_CODE = "address_zip_code";
	public static final String COLUMN_ADDRESS_ZIP_CODE_SIDE = "address_zip_code_side";
	public static final String COLUMN_ADDRESS_STREET_NUMBER = "address_street_number";
	public static final String COLUMN_ADDRESS_STREET_COMPLEMENT = "address_street_complement";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_SITE = "site";
	public static final String COLUMN_ACCESS_DATA_USER = "access_data_user";
	public static final String COLUMN_ACCESS_DATA_PASSWORD = "access_data_password";
	public static final String COLUMN_EXPIRATION_DATE = "expiration_date";
	public static final String COLUMN_LAST_ACCESS_DATE = "last_access_date";
	public static final String COLUMN_ADDRESS_STREET = "address_street";
	public static final String COLUMN_ADDRESS_DISTRICT = "address_district";
	public static final String COLUMN_ADDRESS_CITY_IBGE = "address_city_ibge";
	public static final String COLUMN_ADDRESS_STATE_IBGE = "address_state_ibge";
	public static final String COLUMN_ADDRESS_COUNTRY_IBGE = "address_country_ibge";
	public static final String COLUMN_ADDRESS_GIA = "address_gia";
	public static final String COLUMN_ADDRESS_CITY_DESCRIPTION = "description_city";
	public static final String COLUMN_ADDRESS_STATE_DESCRIPTION = "description_state";
	public static final String COLUMN_ADDRESS_COUNTRY_DESCRIPTION = " description_country";	
	public static final String COLUMN_CREDIT_PROVIDER_ID = "credit_provider_id";
	public static final String COLUMN_DEBIT_PROVIDER_ID = "debit_provider_id";
	public static final String COLUMN_DEBIT_PROVIDER_DESCRIPTION = "debit_provider_description";
	public static final String COLUMN_CREDIT_PROVIDER_DESCRIPTION = "credit_provider_description";
	public static final String COLUMN_COUNT_COMPANY = "count_company";
	public static final String COLUMN_ATTACHED_USERS = "attached_users";
	
	//User 05/11/2023
	public static final String COLUMN_CLIENT_ID = "client_id";
	
	//Create date 02/10/2021
	// Last access finish User
	public static final String COLUMN_LAST_ACCESS_FINISH_DATE = "exit_access_date";
	
	//Integration Systems
	public static final String COLUMN_LEGACY_ID = "legacy_id";
	public static final String COLUMN_INTEGRATION_SYSTEM_ID = "integration_system_id";
	
	//Product Group
	public static final String COLUMN_PRODUCT_GROUP_ID = "product_group_id";	
	public static final String COLUMN_PRODUCT_GROUP_DESCRIPTION = "description_product_group";
	
	//Product SubGroup
	public static final String COLUMN_PRODUCT_SUB_GROUP_ID = "product_sub_group_id";	
	public static final String COLUMN_PRODUCT_SUB_GROUP_DESCRIPTION = "description_product_sub_group";
	
	//Product
	public static final String COLUMN_PRODUCT_ID = "product_id";
	public static final String COLUMN_PRODUCT_DESCRIPTION = "product_description";
	public static final String COLUMN_SALE_PRICE = "sale_price";
	public static final String COLUMN_COST_PRICE = "cost_price";
	public static final String COLUMN_CONVERSION_FACTOR = "conversion_factor";
	public static final String COLUMN_INPUT_MOVEMENT = "input_movement";
	public static final String COLUMN_OUTPUT_MOVEMENT = "output_movement";
	public static final String COLUMN_CLOCK_MOVEMENT = "clock_movement";
	public static final String COLUMN_ENABLE_CLOCK_MOVEMENT = "enable_clock_movement";
	
	public static final String COLUMN_PRODUCTS = "products";
	public static final String COLUMN_PRODUCT_PRODUCT_DESCRIPTION = "product_description";
	
	//Company
	public static final String COLUMN_COMPANY_ID = "company_id";
	public static final String COLUMN_COMPANY_DESCRIPTION = "company_description";
	public static final String COLUMN_COMPANY_NEGATIVE_CLOSE = "negative_close";
	public static final String COLUMN_COMPANY_PROCESS_MOVEMENT_AUTOMATIC = "process_movement_automatic";

	//Create Date 02/10/2022
	public static final String COLUMN_BLOCKED = "blocked";

	//Create Date 20/04/2022
	public static final String COLUMN_BACKGROUND_COLOR = "background_color";
	
	//Create Date 19/12/2021
	public static final String COLUMN_SEND_EMAIL = "email";

	//Financial Group
	public static final String COLUMN_FINANCIAL_GROUP_ID = "financial_group_id";
	public static final String COLUMN_FINANCIAL_GROUP = "financial_group";
	public static final String COLUMN_FINANCIAL_GROUP_DESCRIPTION = "description_financial_group";
	public static final String COLUMN_FINANCIAL_GROUP_DESCRIPTION_REPORT = "financial_group_description";

	
	//Financial SubGroup
	public static final String COLUMN_FINANCIAL_SUB_GROUP_ID = "financial_sub_group_id";
	public static final String COLUMN_FINANCIAL_SUB_GROUP = "financial_sub_group";
	public static final String COLUMN_FINANCIAL_SUB_GROUP_DESCRIPTION = "description_financial_sub_group";
	public static final String COLUMN_FINANCIAL_SUB_GROUP_DESCRIPTION_REPORT = "financial_sub_group_description";
	public static final String COLUMN_FINANCIAL_INACTIVE_SUB_GROUP_DESCRIPTION = "inactive_financial_sub_group";
	public static final String COLUMN_FINANCIAL_REVENUE_SOURCE_ID = "revenue_source_id";
	public static final String COLUMN_FINANCIAL_REVENUE_SOURCE = "revenue_source";
	public static final String COLUMN_FINANCIAL_REVENUE_TYPE_ID = "revenue_type_id";
	public static final String COLUMN_FINANCIAL_REVENUE_TYPE = "revenue_type";
	
	//Bank Account
	public static final String COLUMN_BANK_ACCOUNT_ID = "bank_account_id";
	public static final String COLUMN_BANK_ACCOUNT_BALANCE = "bank_balance_available";
	public static final String COLUMN_BANK_ACCOUNT_LIMIT = "bank_limit_available";
	public static final String COLUMN_BANK_ACCOUNT_CODE = "bank_code";
	public static final String COLUMN_BANK_ACCOUNT_ANGENCY = "bank_angency";
	public static final String COLUMN_BANK_ACCOUNT_ORIGIN_ID ="bank_account_origin_id";
	public static final String COLUMN_BANK_ACCOUNT_DESTINY_ID ="bank_account_destiny_id";
	public static final String COLUMN_BANK_ACCOUNT_AUTOMATIC_TRANSFER_ID ="bank_account_automatic_transfer_id";	
	public static final String COLUMN_BANK_ACCOUNT_AUTOMATIC_TRANSFER_DESCRIPTION ="bank_account_automatic_transfer_description";
	public static final String COLUMN_BANK_ACCOUNT_ORIGIN_DESCRIPTION = "bank_account_origin_description";
	public static final String COLUMN_BANK_ACCOUNT_DESTINY_DESCRIPTION = "bank_account_destiny_description";
	public static final String COLUMN_BANK_ACCOUNT_DESCRIPTION = "bank_account_description";
	public static final String COLUMN_BANK_ACCOUNT_ACCUMULATE_BALANCE = "accumulate_balance";
	
	//Provider
	public static final String COLUMN_PROVIDER_ID="provider_id";
	public static final String COLUMN_PROVIDER_DESCRIPTION="provider_description";
	public static final String COLUMN_PROVIDER_NAME="provider_name";
	
	//Financial Transfer
	public static final String COLUMN_FINANCIAL_TRANSFER_GROUP_ID="group_id";
	public static final String COLUMN_FINANCIAL_DOCUMENT_GROUP_TRANSFER_ID="group_transfer_id";
	public static final String COLUMN_FINANCIAL_TRANSFER_DESCRIPTION_GROUP="description_group";
	public static final String COLUMN_FINANCIAL_TRANSFER_GROUP_ITEM_ID="group_item_id";
	public static final String COLUMN_FINANCIAL_TRANSFER_GROUP_DESCRIPTION="group_description";
	public static final String COLUMN_FINANCIAL_TRANSFER_SUBGROUP_DESCRIPTION="sub_group_description";
	public static final String COLUMN_FINANCIAL_TRANSFER_GROUP_EXECUTION_ORDER="group_execution_order";
	public static final String COLUMN_FINANCIAL_TRANSFER_GROUP_ITEM_EXECUTION_ORDER="group_item_execution_order";
	public static final String COLUMN_FINANCIAL_TRANSFER_PERCENTAGE_TRANSFER="percentage_transfer";
	public static final String COLUMN_FINANCIAL_TRANSFER_VALUE_TRANSFER="value_transfer";
	public static final String COLUMN_FINANCIAL_TRANSFER_CREDIT_DEBIT="credit_debit";
	public static final String COLUMN_FINANCIAL_TRANSFER_STATE="transfer_state";
	public static final String COLUMN_FINANCIAL_TRANSFER_IS_OVER_TOTAL="is_over_total";
	public static final String COLUMN_FINANCIAL_TRANSFER_IS_MONTHLY="is_monthly";
	public static final String COLUMN_FINANCIAL_TRANSFER_IS_VARIABLE_EXPENSE="is_variable_expense";
	public static final String COLUMN_FINANCIAL_TRANSFER_IS_DAILY_EXPENSE="is_daily_expense";
	public static final String COLUMN_FINANCIAL_TRANSFER_IS_CLOSE="is_close";
	public static final String COLUMN_FINANCIAL_TRANSFER_IS_USE_REMAINING_BALANCE="is_use_remaining_balance";
	public static final String COLUMN_INACTIVE_GROUP = "inactive_group";
	public static final String COLUMN_INACTIVE_TRANSFER = "inactive_transfer";
	public static final String COLUMN_FINANCIAL_TRANSFER_DESCRIPTION_TRANFER="description_transfer";
	public static final String COLUMN_FINANCIAL_TRANSFER_TRANSFER_TYPE="transfer_type";
	public static final String COLUMN_FINANCIAL_TRANSFER_EXPENSE="expense";
	public static final String COLUMN_FINANCIAL_TRANSFER_EXECUTION_ORDER="execution_order";
	public static final String COLUMN_FINANCIAL_TRANSFER_IS_PRODUCT_MOVEMENT="is_product_movement";
	public static final String COLUMN_FINANCIAL_TRANSFER_EXECUTION_PERIOD="execution_period";
	public static final String COLUMN_FINANCIAL_TRANSFER_EXECUTION_INITIAL_PERIOD="execution_initial_period"; 
	public static final String COLUMN_FINANCIAL_IS_DOCUMENT_TRANSFER="is_document_transfer";
	public static final String COLUMN_FINANCIAL_IS_AUTOMATIC_TRANSFER="is_automatic_transfer";
	public static final String COLUMN_IS_PRODUCT_MOVEMENT="is_product_movement";
	public static final String COLUMN_FINANCIAL_TRANSFER_FIXED_DAY = "fixed_day";
	public static final String COLUMN_FINANCIAL_TRANSFER_FIXED_DAY_MONTH = "fixed_day_month";
	public static final String COLUMN_FINANCIAL_TRANSFER_WEEK_DAY = "week_day";
	public static final String COLUMN_FINANCIAL_TRANSFER_AUTOMATIC_PROCESSING = "automatic_processing";
	public static final String COLUMN_FINANCIAL_TRANSFER_MOVE_LAUNCH_TYPE = "move_launch_type";
	public static final String COLUMN_FINANCIAL_TRANSFER_EXECUTION_DAYS = "execution_days";
	public static final String COLUMN_FINANCIAL_TRANSFER_EXECUTION_TIME = "execution_time";
	public static final String COLUMN_FINANCIAL_GROUP_PRODUCT_ID = "group_product_id";
	public static final String COLUMN_FINANCIAL_GROUP_PRODUCT_DESCRIPTION = "group_product_description";
	public static final String COLUMN_FINANCIAL_SUB_GROUP_PRODUCT_ID = "sub_group_product_id";
	public static final String COLUMN_FINANCIAL_SUB_GROUP_PRODUCT_DESCRIPTION = "sub_group_product_description";
	
	//Document Movement
	public static final String COLUMN_DOCUMENT_ID = "document_id";
	public static final String COLUMN_DOCUMENT_PARENT_ID = "document_parent_id";
	public static final String COLUMN_DOCUMENT_TRANSACTION_ID = "document_transaction_id";
	public static final String COLUMN_CREDIT = "credit";	
	public static final String COLUMN_DOCUMENT_TYPE = "document_type";
	public static final String COLUMN_DOCUMENT_TYPE_DESCRIPTION = "document_type_description";
	public static final String COLUMN_MOVEMENT_DESCRIPTION = "movement_description";	
	public static final String COLUMN_DOCUMENT_NUMBER = "document_number";
	public static final String COLUMN_DOCUMENT_VALUE = "document_value";
	public static final String COLUMN_DOCUMENT_NOTE = "document_note";
	public static final String COLUMN_DOCUMENT_STATUS = "document_status";
	public static final String COLUMN_PAYMENT_VALUE = "payment_value";
	public static final String COLUMN_PAYMENT_DISCOUNT = "payment_discount";
	public static final String COLUMN_PAYMENT_EXTRA = "payment_extra";
	public static final String COLUMN_PAYMENT_RESIDUE = "payment_residue";
	public static final String COLUMN_PAYMENT_DATA = "payment_data";
	public static final String COLUMN_PAYMENT_DATA_LONG = "payment_data_long";
	public static final String COLUMN_CASHIER_CLOSING_DATE = "cashier_closing_date";
	
	public static final String COLUMN_PAYMENT_EXPIRY_DATA = "payment_expiry_data";
	public static final String COLUMN_PAYMENT_USER = "payment_user";
	public static final String COLUMN_PAYMENT_STATUS = "payment_status";
	public static final String COLUMN_NFE_NUMBER = "nfe_number";
	public static final String COLUMN_NFE_SERIE_NUMBER = "nfe_serie_number";
	public static final String COLUMN_GENERATE_BILLET = "generate_billet";
	public static final String COLUMN_VERSION_REGISTER = "version_register";
	public static final String COLUMN_TYPE_WATCH = "type_watch";
	public static final String COLUMN_INITIAL_ENTRY = "initial_entry";
	public static final String COLUMN_FINAL_ENTRY = "final_entry";
	public static final String COLUMN_INITIAL_OUTPUT = "initial_output";
	public static final String COLUMN_FINAL_OUTPUT = "final_output";
	public static final String COLUMN_INITIAL_INPUT = "initial_input";
	public static final String COLUMN_FINAL_INPUT = "final_input";
	public static final String COLUMN_DOCUMENT_NUMBER_RESULT = "document_number_result";
	
	//Access Profile
	public static final String COLUMN_ACCESS_PROFILE_ID = "access_profile_id";
	public static final String COLUMN_ACCESS_PROFILE_DESCRIPTION = "access_profile_description";
	public static final String COLUMN_ROLE = "role";
	public static final String COLUMN_CASH_CLOSING_MAX_DISCOUNT = "cash_closing_max_discount";
	
	//Permission
	public static final String COLUMN_PERMISSION_ID = "permission_id";
	public static final String COLUMN_PERMISSION_PARENT_ID = "permission_parent_id";
	public static final String COLUMN_PERMISSION_DESCRIPTION = "permission_description";
	public static final String COLUMN_PERMISSION_ROLE = "permission_role";
	public static final String COLUMN_PERMISSION = "permission";
	
	//Movement Product
	public static final String COLUMN_MOVEMENT_ID = "movement_id";
	public static final String COLUMN_CREDIT_IN_INITIAL = "credit_in_initial";
	public static final String COLUMN_CREDIT_IN_FINAL = "credit_in_final";	
	public static final String COLUMN_CREDIT_OUT_INITIAL = "credit_out_initial";
	public static final String COLUMN_CREDIT_OUT_FINAL = "credit_out_final";	
	public static final String COLUMN_CREDIT_CLOCK_INITIAL = "credit_clock_initial";
	public static final String COLUMN_CREDIT_CLOCK_FINAL = "credit_clock_final";
	public static final String COLUMN_CREDIT_INITIAL = "credit_initial";	
	public static final String COLUMN_CREDIT_FINAL = "credit_final";	
	public static final String COLUMN_CUSTOMER = "customer";
	public static final String COLUMN_REGISTER_DATE = "register_date";
	public static final String COLUMN_REGISTER_ONLINE = "register_online";
	public static final String COLUMN_COUNT_PRODUCT = "count_products";
	public static final String COLUMN_EXECUTION_ORDER = "execution_order";
	public static final String COLUMN_GROUP_EXECUTION_ORDER = "group_execution_order";
	public static final String COLUMN_GROUP_ITEM_EXECUTION_ORDER = "group_item_execution_order";
	
	public static final String COLUMN_PROCESSING = "processing";
	public static final String COLUMN_LAST_PROCESSING = "last_processing";
	public static final String COLUMN_TOTAL = "total";
	public static final String COLUMN_READING_DATE = "reading_date";
	public static final String COLUMN_INITIAL_DATE = "initial_date";
	public static final String COLUMN_IS_OFFLINE = "is_offline";
	
	//Bank  Account Statement
	public static final String COLUMN_BANK_ACCOUNT_AVAILABLE_INITIAL = "bank_balance_available_initial";
	public static final String COLUMN_BANK_ACCOUNT_AVAILABLE_FINAL = "bank_balance_available_final";
	public static final String COLUMN_BANK_ACCOUNT_AVAILABLE = "bank_balance_available";
	public static final String COLUMN_MAIN_BANK_ACCOUNT = "main_bank_account";
	public static final String COLUMN_DOCUMENT_DATE = "document_date";
	public static final String COLUMN_IS_GROUP_MOVEMENT = "is_group_movement";
	
	// Product Group
	public static final String COLUMN_TRANSFER_HAB = "transfer_hab";
	
	//User
	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_USER_NAME = "user_name";
	
	//03/02/2022
	// Has No Movement Product 
	public static final String COLUMN_HAS_NO_MOVEMENT = "has_no_movement";
	
	//04/03/2022
	// Dashboard
	public static final String COLUMN_EXPIRY_DATA = "expiry_data";
	public static final String COLUMN_EXECUTION_DATE = "execution_date";
	public static final String COLUMN_MOTIVE = "motive";
	public static final String COLUMN_EXPIRY_DATA_STRING = "expiry_data_string";
	public static final String COLUMN_REGISTER_ORDER = "register_order";
	
	//15/03/2022
	// Dashboard Company Execution Week
	public static final String COLUMN_COUNT_MOVEMENET = "count_movement";
	public static final String COLUMN_COUNT_EXECUTED = "count_executed";
	public static final String COLUMN_COUNT_NOT_EXECUTED = "count_not_executed";
	public static final String COLUMN_COUNT_NEGATIVE = "count_negative";
	public static final String COLUMN_COUNT_LOW_MOVEMENT = "count_low_movement";
	public static final String COLUMN_COUNT_OTHERS = "count_others";
	public static final String COLUMN_COUNT_NOT_INFORMED = "count_not_informed";
	public static final String COLUMN_COUNT_WEEK_YEAR = "week_year";
	public static final String COLUMN_INITIAL_DATE_WEEK = "initial_date_week";
	public static final String COLUMN_FINAL_DATE_WEEK = "final_date_week";
	public static final String COLUMN_COUNT_DISABLE = "count_disable";
	public static final String COLUMN_COUNT_COMPANY_NOT_EXPIRY = "count_company_not_expiry";
	public static final String COLUMN_COUNT_DISABLE_EXECUTED = "count_disable_executed";
	public static final String COLUMN_COUNT_NEGATIVE_EXECUTED = "count_negative_executed";
	
	public static final String COLUMN_PERSON_TYPE_ID = "person_type_id";
	public static final String COLUMN_PERSON_TYPE_DESCRIPTION = "person_type_description";
	public static final String COLUMN_ESTABLISHMENT_TYPE_ID = "establishment_type_id";
	public static final String COLUMN_ESTABLISHMENT_TYPE_DESCRIPTION = "establishment_type_description";
	public static final String COLUMN_PARCENT_SALES = "percent_sales";
	public static final String COLUMN_FINANCIAL_GROUP_TYPE_ID = "financial_group_type_id";
	public static final String COLUMN_ORDER_TYPE = "order_type";
	public static final String COLUMN_GROUP_TYPE_ID = "group_type_id";
	public static final String COLUMN_GROUP_TYPE_DESCRIPTION = "group_type_description";
	public static final String COLUMN_ORDER = "order";
	public static final String COLUMN_DATA_ORDER = "data_order";
	
	public static final String PARAMETER_ZERO = "0"; 
	
	//25/09/2022
	// Log System
	public static final String COLUMN_CHANGE_DATE_STRING = "change_date_string";
	public static final String COLUMN_JSON_DATA_BEFORE = "json_data_before";
	public static final String COLUMN_JSON_DATA_AFTER = "json_data_after";
	
	public static final String PARAMETER_MOVEMENT_PRODUCT = "p;";
	public static final String PARAMETER_MOVEMENT_TRANSFER = "t;";
	public static final String PARAMETER_DOCUMENT_TRANSFER_ID = "document_transfer_id";
	
	//19/11/2022
	// Financial Cost Center
	public static final String COLUMN_FINANCIAL_COST_CENTER_ID = "financial_cost_center_id";
	public static final String COLUMN_CHANGE_USER = "change_user";
	public static final String COLUMN_CREATION_USER = "creation_user";

	public static final String COLUMN_MAIN_COMPANY_ID = "main_company_id";
	public static final String PARAMETER_TOTAL_DOCUMENT_RESIDUE_VALUE = "total_document_residue_value";
	
	//Cash Statement
	public static final String COLUMN_MOVEMENT_TYPE = "movement_type";
	public static final String COLUMN_WEEK_YEAR = "week_year";
	public static final String COLUMN_MONTH_YEAR = "month_year";
	public static final String COLUMN_DOCUMENT_TRANSFER_ID = "document_transfer_id";
	public static final String COLUMN_DATE_FROM = "date_from";
	public static final String COLUMN_DATE_TO = "date_to";
	public static final String COLUMN_MOVEMENT_TYPE_ID = "movement_type_id";
	public static final String COLUMN_ORDER_ITEM = "order_item";
	public static final String COLUMN_DOCUMENT_DESCRIPTION = "document_description";
	public static final String COLUMN_TRANSACTION_SAME_COMPANY = "transaction_same_company";
	public static final String COLUMN_PROFIT_DISTRIBUTION = "profit_distribution";
	
	//Cash Closing
	public static final String COLUMN_CASHIER_CLOSING_ID = "cashier_closing_id";
	public static final String COLUMN_CASHIER_CLOSING_STATUS = "cashier_closing_status";
	public static final String COLUMN_CASHIER_CLOSING_COMPANY_STATUS = "cashier_closing_status_company";
	public static final String COLUMN_MOVEMENT_VALUE = "movement_value";
	public static final String COLUMN_PENDING_MOVEMENT_VALUE = "pending_movement_value";
	public static final String COLUMN_PENDING_MOVEMENT_AFTER_VALUE = "pending_movement_after_value";
	public static final String COLUMN_TOTAL_COMPANY = "total_company";
	public static final String COLUMN_DISCOUNT_TOTAL = "discount_total";
	public static final String COLUMN_PAYMENT_TOTAL = "payment_total";
	public static final String COLUMN_DOCUMENT_MOVEMENT_ID = "document_movement_id";
	public static final String COLUMN_RESIDUE = "residue";
	public static final String COLUMN_LAUNCH_DATE = "launch_date";
	public static final String COLUMN_LAUNCH_USER  = "launch_user";
	public static final String COLUMN_CLOSE_DATE  = "close_date";
	public static final String COLUMN_CLOSE_USER  = "close_user";
	public static final String COLUMN_TYPE  = "type";
	public static final String COLUMN_USER_OPERATOR = "user_operator";
	public static final String COLUMN_EXPENSE = "expense";
	
	//Alias Product
	public static final String COLUMN_ALIAS_PRODUCT = "alias_product";
	
	//Game Type
	public static final String COLUMN_GAME_TYPE_ID = "game_type_id";
	public static final String COLUMN_GAME_TYPE = "game_type";
	
	//Machine Type
    public static final String COLUMN_MACHINE_TYPE_ID = "machine_type_id";
    public static final String COLUMN_MACHINE_TYPE = "machine_type";
}