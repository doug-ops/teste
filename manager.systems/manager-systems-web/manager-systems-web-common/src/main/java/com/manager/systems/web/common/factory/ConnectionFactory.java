package com.manager.systems.web.common.factory;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import com.manager.systems.common.vo.ApplicationTypes;

public class ConnectionFactory 
{
	@Autowired(required = false)
	DataSource portalDataSource;

	public final Connection getConnection(final ApplicationTypes applicationTypes) throws Exception
	{
		Connection connection = null;
		switch (applicationTypes) 
		{
			case PORTAL:
				connection = this.portalDataSource.getConnection();
				break;
			default:
				break;
		}
		return connection;
	}
}