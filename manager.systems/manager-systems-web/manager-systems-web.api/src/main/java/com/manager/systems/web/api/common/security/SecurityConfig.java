/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.manager.systems.web.api.common.constants.ControllerConstants;
import com.manager.systems.web.api.common.security.jwt.JwtAuthenticationFilter;
import com.manager.systems.web.api.common.security.jwt.JwtAuthorizationFilter;
import com.manager.systems.web.api.common.security.jwt.handler.AccessDeniedHandler;
import com.manager.systems.web.api.common.security.jwt.handler.UnauthorizedHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private UnauthorizedHandler unauthorizedHandler;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final AuthenticationManager authManager = authenticationManager();

        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, ControllerConstants.URL_API+ControllerConstants.URL_API_VERSION+ControllerConstants.URL_API_LOGIN).permitAll()
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
                .permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .addFilter(new JwtAuthenticationFilter(authManager))
                .addFilter(new JwtAuthorizationFilter(authManager, this.userDetailsService))
                .exceptionHandling()
                .accessDeniedHandler(this.accessDeniedHandler)
                .authenticationEntryPoint(this.unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        auth.userDetailsService(this.userDetailsService).passwordEncoder(encoder);
    }

}