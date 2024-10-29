/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.common.security.jwt;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.manager.systems.web.api.common.constants.MessageConstants;
import com.manager.systems.web.api.common.constants.ParameterConstants;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	private static Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

	private UserDetailsService userDetailsService;

	public JwtAuthorizationFilter(final AuthenticationManager authenticationManager, final UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		final String token = request.getHeader(ParameterConstants.PARAMETER_AUTHORIZATION);

		if (StringUtils.isEmpty(token) || !token.startsWith(ParameterConstants.PARAMETER_BEARER)) {
			// Não informou o authorization
			filterChain.doFilter(request, response);
			return;
		}

		try {

			if (!JwtUtil.isTokenValid(token)) {
				throw new AccessDeniedException(MessageConstants.MSG_ACCESS_DENIED);
			}

			final String login = JwtUtil.getLogin(token);
			final UserDetails userDetails = this.userDetailsService.loadUserByUsername(login);
			final List<GrantedAuthority> authorities = JwtUtil.getRoles(token);
			
			// var authorities = ((UserDetails) userDetails).getAuthorities();
			final Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

			// Salva o Authentication no contexto do Spring
			SecurityContextHolder.getContext().setAuthentication(auth);
			filterChain.doFilter(request, response);
		} catch (final RuntimeException ex) {
			logger.error(MessageConstants.MSG_ACCESS_DENIED + ex.getMessage(), ex);
			throw ex;
		}
	}
}
