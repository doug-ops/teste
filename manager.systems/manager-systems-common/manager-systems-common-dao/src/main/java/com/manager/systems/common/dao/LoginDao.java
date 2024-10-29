package com.manager.systems.common.dao;

import com.manager.systems.model.admin.User;

public interface LoginDao 
{
	void validate(User user) throws Exception;
}