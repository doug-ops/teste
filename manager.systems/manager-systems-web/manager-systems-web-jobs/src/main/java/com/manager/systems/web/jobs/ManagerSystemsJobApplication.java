package com.manager.systems.web.jobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.manager.systems.web.jobs.utils.ConstantDataManager;

@SpringBootApplication(scanBasePackages= {ConstantDataManager.PACKAGE_JOB}, exclude= {SecurityAutoConfiguration.class})
public class ManagerSystemsJobApplication extends SpringBootServletInitializer
{
	public static void main(final String[] args) 
	{
		SpringApplication.run(ManagerSystemsJobApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder builder) 
	{
		return builder.sources(ManagerSystemsJobApplication.class);
	}	
}