package com.manager.systems.web.jobs.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.manager.systems.web.jobs.utils.ConstantDataManager;

@Configuration
@EnableScheduling
public class JobMvcConfiguration implements WebMvcConfigurer
{		
	@Primary
	@Bean(name=ConstantDataManager.APPLICATION_JOBS)
    public  DataSource portalDataSource() 
	{
        final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
        final DataSource dataSource = dsLookup.getDataSource(ConstantDataManager.DATA_SOURCE_PORTAL);
        return dataSource;
    }
	
	@Bean
    public TaskScheduler threadPoolTaskScheduler() 
	{
        return new ThreadPoolTaskScheduler();
    }
}