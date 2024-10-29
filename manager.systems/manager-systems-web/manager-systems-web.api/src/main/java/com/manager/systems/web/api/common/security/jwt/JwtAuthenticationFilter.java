/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.common.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.systems.web.api.common.constants.ControllerConstants;
import com.manager.systems.web.api.common.constants.MessageConstants;
import com.manager.systems.web.api.common.constants.ParameterConstants;
import com.manager.systems.web.api.login.domain.dto.UserDTO;
import com.manager.systems.web.api.login.domain.entity.User;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	public static final String AUTH_URL = ControllerConstants.URL_API + ControllerConstants.URL_API_VERSION
			+ ControllerConstants.URL_API_LOGIN;

	private final AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(final AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		setFilterProcessesUrl(AUTH_URL);
	}

	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) {

		try {
			final JwtLoginInput login = new ObjectMapper().readValue(request.getInputStream(), JwtLoginInput.class);
			final String username = login.getUsername();
			final String password = login.getPassword();

			if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
				throw new BadCredentialsException(MessageConstants.MSG_USER_OR_PASSWORD_INVALID);
			}

			final Authentication auth = new UsernamePasswordAuthenticationToken(username, password);

			return this.authenticationManager.authenticate(auth);
		} catch (final IOException e) {
			throw new BadCredentialsException(e.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain, final Authentication authentication) throws IOException {
		
		final User user = (User) authentication.getPrincipal();
		final String jwtToken = JwtUtil.createToken(user, user.getId());
		
//        String json = ServletUtil.getJson("token", jwtToken);
		final String json = UserDTO.create(user, jwtToken).toJson();
		ServletUtil.write(response, HttpStatus.OK, json);
	}

	@Override
	protected void unsuccessfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException error) throws IOException, ServletException {
		
		final String json = ServletUtil.getJson(ParameterConstants.PARAMETER_ERROR,
				MessageConstants.MSG_AUTHENTICATION_INVALID);
		ServletUtil.write(response, HttpStatus.UNAUTHORIZED, json);
	}
}
