package com.manager.systems.movement.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages= {com.manager.systems.web.common.utils.ConstantDataManager.PACKAGE_PORTAL}, exclude= {SecurityAutoConfiguration.class})
public class ManagerSystemsMovementProductApplication extends SpringBootServletInitializer
{
	public static void main(final String[] args) 
	{
		SpringApplication.run(ManagerSystemsMovementProductApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder builder) 
	{
		return builder.sources(ManagerSystemsMovementProductApplication.class);
	}	
}