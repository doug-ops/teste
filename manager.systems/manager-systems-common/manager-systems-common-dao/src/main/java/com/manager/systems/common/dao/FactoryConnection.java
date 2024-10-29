package com.manager.systems.common.dao;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class FactoryConnection 
{
	public static Connection getConnectionPortal() throws Exception
	{
		final Context initContext = new InitialContext();
		final Context envContext  = (Context) initContext.lookup("java:/comp/env");
		final DataSource dataSource = (DataSource) envContext.lookup("jdbc/portal");
		return dataSource.getConnection();
	}
	
	public static Connection getConnectionRetaguarda() throws Exception
	{
		final Context initContext = new InitialContext();
		final Context envContext  = (Context) initContext.lookup("java:/comp/env");
		final DataSource dataSource = (DataSource) envContext.lookup("jdbc/retaguarda");
		return dataSource.getConnection();
	}
	
	public static Connection getConnectionQuarterHorse() throws Exception
	{
		final Context initContext = new InitialContext();
		final Context envContext  = (Context) initContext.lookup("java:/comp/env");
		final DataSource dataSource = (DataSource) envContext.lookup("jdbc/quarterhorse");
		return dataSource.getConnection();
	}
}