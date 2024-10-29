/*
 * Date create 30/06/2020
 */
package com.manager.systems.web.api.common.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.ant("/api/**")).build()
				.globalOperationParameters(Collections
						.singletonList(new ParameterBuilder().name("Authorization").description("Bearer token")
								.modelRef(new ModelRef("string")).parameterType("header").required(false).build()))
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.contact(new Contact("Manager API", "www.managersystems.com", "admin@manager.com"))
				.title("Datamaxi API").description("Documentation API").license("Apache Licence Version 2.0")
				.licenseUrl("https://apache.org").version("1.0").build();
	}
}