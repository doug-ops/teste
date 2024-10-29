package com.manager.systems.web.common.utils;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

@Component
public class Messages
{
	@Autowired
    private MessageSource messageSource;

    private MessageSourceAccessor accessor;

    @PostConstruct
    private void init() 
    {
        this.accessor = new MessageSourceAccessor(this.messageSource, new Locale(ConstantDataManager.PT, ConstantDataManager.BR));
    }

    public String get(final String code, final Object[] args) 
    {	
    	String result = ConstantDataManager.BLANK;
    	
    	if(args!=null)
    	{
    		result = this.accessor.getMessage(code, args);
    	}
    	else
    	{
    		result = this.accessor.getMessage(code);
    	}  	
    	return result;
    }
}