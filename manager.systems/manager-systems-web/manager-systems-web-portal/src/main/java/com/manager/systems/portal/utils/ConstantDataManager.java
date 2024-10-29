package com.manager.systems.portal.utils;

public final class ConstantDataManager {
	// Home
	public static final String PORTAL_CONTROLLER_HOME = "/home";

	// Methods
	public static final String METHOD_BASE = "base";
	public static final String METHOD_INDEX = "index";
	public static final String METHOD_LOGIN = "login";

	// Geral

	// Usuario
	public static final String PARAMETER_USERNAME = "username";
	public static final String PARAMETER_PASSWORD = "password";

	// Constroller
	public static final String RESULT_LOGIN = "login";
	public static final String RESULT_HOME = "home-base";
	public static final String RESULT_REDIRECT_HOME = "redirect:/home";

	// Login
	public static final String PORTAL_CONTROLLER = "/*";
	public static final String PORTAL_CONTROLLER_BASE = "/";
	public static final String PORTAL_CONTROLLER_INDEX = "/login.html";
	public static final String PORTAL_CONTROLLER_LOGIN = "/login";
	public static final String PORTAL_CONTROLLER_LOGOUT = "/logout";
	public static final String PORTAL_CONTROLLER_ACESSO_EXPIRADO = "/acessoExpirado";

	// Dashboard
	public static final String DASHBOARD_CONTROLLER = "/dashboard";
	public static final String DASHBOARD_PENDING_ITENS = "/pendingItens";
	
	//14/03/2022
	// Dashboard Company Execution week
	public static final String DASHBOARD_CONTROLLER_OPEN_METHOD = "/open";
	public static final String DASHBOARD_COMPANY_EXECUTION_WEEK_CONTROLLER_OPEN_METHOD_RESULT = "/dashboard/dashboard-company-execution-week-base";
	//dashboard/dashboard-company-execution-week-base
	public static final String FILTER_DASHBOARD_COMPANY_EXECUTION_WEEK_CONTROLLER = "/filterDashboardCompanyExecutionWeek";
	public static final String PARAMETER_DATE = "date";
	public static final String PARAMETER_FILTER_DATE = "filterDate";	
	public static final String PARAMETER_TYPE = "type";
	
	public static final String FILTER_DASHBOARD_MOVEMENT_CONTROLLER = "/filterDashboardMovement";
	public static final String CHART_TYPE_MOVEMENT_GROUPS = "MovementGroups";	
	public static final String CHART_TYPE_MOVEMENT_SUBGROUPS = "MovementSubgroups";
	public static final String CHART_TYPE_TOP_COMPANY = "MovementTopCompany";
	public static final String CHART_TYPE_AVERAGE_TOP_PRODUCT_COMPANY = "MovementAverageTopProductCompany";
	public static final String CHART_TYPE_AVERAGE_TOP_COMPANY = "MovementAverageTopCompany";
	public static final String CHART_TYPE_COMPANY_EXECUTION = "MovementCompanyExecution";
	public static final String CHART_TYPE_PRODUCT_EXECUTION = "MovementProductExecution";
	
	public static final String PARAMETER_USER_CHIDRENS_PARENT = "userChidrensParent";
	public static final String PARAMETER_USER_CHIDRENS_PARENT_COMBOBOX = "userChildrensParentCombobox";	
}