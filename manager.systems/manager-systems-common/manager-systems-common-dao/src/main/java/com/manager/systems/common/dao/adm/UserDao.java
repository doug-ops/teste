/*
 * Date create 30/11/2021
 */
package com.manager.systems.common.dao.adm;

import com.manager.systems.model.admin.User;

public interface UserDao 
{
	boolean save(User user) throws Exception;
	boolean updatelastAcessUser(User user) throws Exception;
	boolean updateLastAccessFinishUser(User user) throws Exception;
	boolean saveConfigUser(User user) throws Exception;
}