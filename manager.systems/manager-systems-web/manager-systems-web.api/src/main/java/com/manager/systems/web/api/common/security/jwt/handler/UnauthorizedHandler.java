/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.common.security.jwt.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.manager.systems.web.api.common.constants.CommonConstants;
import com.manager.systems.web.api.common.constants.MessageConstants;
import com.manager.systems.web.api.common.constants.ParameterConstants;
import com.manager.systems.web.api.common.security.jwt.ServletUtil;

@Component
public class UnauthorizedHandler implements AuthenticationEntryPoint {
	private static Logger logger = LoggerFactory.getLogger(UnauthorizedHandler.class);

	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException authException) throws IOException {
		logger.warn(MessageConstants.MSG_ACCESS_DENIED + CommonConstants.SPACE + authException);
		// Chamado se token errado ou ausente
		final String json = ServletUtil.getJson(ParameterConstants.PARAMETER_ERROR, MessageConstants.MSG_ACCESS_DENIED);
		ServletUtil.write(response, HttpStatus.FORBIDDEN, json);
	}
}
