package com.manager.systems.common.dao.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.manager.systems.common.utils.ResourceSpringProperties;

public class ConnectionUtils {
	
	private static String JDBC_DRIVER = "spring.datasource.driver-class-name";
	
	private static String JDBC_URL = "spring.datasource.url";
	
	private static String JDBC_USERNAME = "spring.datasource.username";
	
	private static String JDBC_PASSWORD = "spring.datasource.password";

    private static Connection openConnection()
    {
    	Connection connection = null;
        try 
        {
            Class.forName(getDataBaseDriver());
            final String url = getDataBaseURL();                
            final Properties properties = new Properties();
            properties.put("user", getDataBaseUser());
            properties.put("password", getDataBaseUserPassword());
            connection = DriverManager.getConnection(url, properties);     
        } 
        catch (final SQLException e) 
        {
        	e.printStackTrace();
        } 
        catch (final ClassNotFoundException e) 
        {
        	e.printStackTrace();
        } 
        catch (final Exception e) 
        {
        	e.printStackTrace();
        }
        return connection;
    }

    public static Connection getConnection() throws Exception
    {
    		return openConnection();
    }
    
    /**
     * Get database driver.
     * 
     * @return database driver.
     */
    private static String getDataBaseDriver()
    {
    	return ResourceSpringProperties.getPropertyWEB(JDBC_DRIVER);
    }
    
    /**
     * Get database path.
     * 
     * @return database path.
     */
    private static String getDataBaseURL()
    {
    	return ResourceSpringProperties.getPropertyWEB(JDBC_URL);
    }

    /**
     * Get database user.
     * 
     * @return database user.
     */
    private static String getDataBaseUser() 
    {
        return ResourceSpringProperties.getPropertyWEB(JDBC_USERNAME);
    }

    /**
     * Get database password.
     * 
     * @return database password.
     */
    private static String getDataBaseUserPassword() 
    {
        return ResourceSpringProperties.getPropertyWEB(JDBC_PASSWORD);
    }

	public static void closeConnection(final Connection connection) 
	{
        if (connection != null)
        {
            try
        	{
            	connection.close();
            } 
            catch (final SQLException e) 
            {
            	e.printStackTrace();
            }
        }
    }
}