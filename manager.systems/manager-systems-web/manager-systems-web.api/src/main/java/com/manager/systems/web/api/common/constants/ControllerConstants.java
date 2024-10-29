/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.common.constants;

public abstract class ControllerConstants {
	
	//Common
	public static final String URL_API = "/api";
	public static final String URL_API_VERSION = "/v1";
	public static final String URL_API_PARAMETER_ID="/{id}";
	public static final String URL_API_INSERT = "/insert";
	public static final String URL_API_INACTIVE = "/inactive";
	public static final String URL_API_SAVE = "/save";
	public static final String URL_API_FIND_ALL = "/findAll";
	public static final String URL_API_FIND_BY_ID = "/findById";
	public static final String URL_API_ONLINE = "/online";
	public static final String URL_API_GET_ALL = "/getAll";
	
	//Login
	public static final String URL_API_LOGIN = "/login";
	
	//Client
	public static final String URL_API_CLIENT = "/client";
	
	//User
	public static final String URL_API_USERS = "/users";
	
	//User Roles
	public static final String URL_API_ROLES = "/roles";
	
	//UserDetails
	public static final String URL_API_USER_DETAILS = "/security";
	public static final String URL_API_USER_DETAILS_USER_INFO = "/userInfo";
}