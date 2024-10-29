package com.manager.systems.portal.configuration;

import java.util.Locale;

import javax.sql.DataSource;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.manager.systems.web.common.factory.ConnectionFactory;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer
{	
	@Bean
    public static LocaleResolver localeResolver() 
	{
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(new Locale(com.manager.systems.web.common.utils.ConstantDataManager.PT, com.manager.systems.web.common.utils.ConstantDataManager.BR));
        return slr;
    }
	
	@Primary
	@Bean(name=com.manager.systems.web.common.utils.ConstantDataManager.APPLICATION_PORTAL)
    public  DataSource portalDataSource() 
	{
        final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
        final DataSource dataSource = dsLookup.getDataSource(com.manager.systems.web.common.utils.ConstantDataManager.DATA_SOURCE_PORTAL);
        return dataSource;
    }
	
	/**
	@Bean(name=com.manager.systems.web.common.utils.ConstantDataManager.APPLICATION_RETAGUARDA)
    public  DataSource retaguardaDataSource() 
	{
        final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
        final DataSource dataSource = dsLookup.getDataSource(com.manager.systems.web.common.utils.ConstantDataManager.DATA_SOURCE_RETAGUARDA);
        return dataSource;
    }
    */
	
	@Bean
	public ConnectionFactory  connectionFactory()
	{
		return new ConnectionFactory();
	}
	
	@Bean
    public RestTemplate restTemplate(final RestTemplateBuilder builder) 
	{
       return builder.build();
    }
	
	@Bean
    public WebMvcConfigurer corsConfigurer() 
    {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
}