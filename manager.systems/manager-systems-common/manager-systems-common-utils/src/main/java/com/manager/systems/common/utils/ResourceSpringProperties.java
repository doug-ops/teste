package com.manager.systems.common.utils;

import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ResourceSpringProperties 
{
    public static final int LOG4J_PROPERTIES = 1;
    
    private static ResourceSpringProperties instance;
    private Properties log4jProperties;
    
    private ResourceSpringProperties() throws Exception 
    {
        this.log4jProperties = new Properties();
        this.log4jProperties.load(getClass().getClassLoader().getResourceAsStream("log4j.properties"));
    }
    
    public static ResourceSpringProperties getInstance() throws Exception
    {
        if(instance == null)
            instance = new ResourceSpringProperties();
        return instance;
    }
    
    public String getProperty(final int type, final String key) throws Exception 
    {
        if(getProperties(type).containsKey(key))
            return getProperties(type).getProperty(key);
        throw new Exception("Propertie not found!");
    }
    
    public Properties getProperties(final int type) 
    {
        switch(type) 
        {
            case 1: return this.log4jProperties;
		default:
			break;
        }
        throw new NullPointerException("Invalid ResourceType " + type);
    }  
    
    public static String getPropertyWEB(final String propriedade) 
    {
    	final PropertyResourceBundle props = (PropertyResourceBundle)ResourceBundle.getBundle("application");
        return props.getString(propriedade);
    }

    public static String getPropertyWEB(final String propriedade, final String arg) 
    {
    	final PropertyResourceBundle props = (PropertyResourceBundle)ResourceBundle.getBundle("application");
        return props.getString(propriedade).replaceAll("\\{0\\}", arg);
    }
    
    public static String getProperty(final String propriedade) 
    {
    	final PropertyResourceBundle props =  (PropertyResourceBundle)ResourceBundle.getBundle("version");
        return props.getString(propriedade);
    }
    
    public static String getPropertyConfig(final String propriedade) 
    {
    	final PropertyResourceBundle props =  (PropertyResourceBundle)ResourceBundle.getBundle("config");
        return props.getString(propriedade);
    }
}