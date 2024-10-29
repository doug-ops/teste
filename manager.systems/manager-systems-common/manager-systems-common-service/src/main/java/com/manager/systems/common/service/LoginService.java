package com.manager.systems.common.service;

import com.manager.systems.model.admin.User;

public interface LoginService 
{
	void validate(User user) throws Exception;
}