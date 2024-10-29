package com.manager.systems.web.common.controller;

import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.manager.systems.web.common.factory.ConnectionFactory;
import com.manager.systems.web.common.utils.ConstantDataManager;
import com.manager.systems.web.common.utils.Messages;

public abstract class BaseController 
{
	public final static TimeZone tz = TimeZone.getTimeZone(ConstantDataManager.TIMEZONE_SAO_PAULO);
	
	@Autowired
    public Messages messages;
	
	@Autowired(required = false)
	public ConnectionFactory connectionFactory;
	
	public void processLabelsPage(final int objectType, final HttpServletRequest request)
	{
		//
	}
}