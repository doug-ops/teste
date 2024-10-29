/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.common.security.jwt.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.manager.systems.web.api.common.constants.MessageConstants;
import com.manager.systems.web.api.common.constants.ParameterConstants;
import com.manager.systems.web.api.common.security.jwt.ServletUtil;

@Component
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
	@Override
	public void handle(final HttpServletRequest request, final HttpServletResponse response,
			final AccessDeniedException exc) throws IOException {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			final String json = ServletUtil.getJson(ParameterConstants.PARAMETER_ERROR, MessageConstants.MSG_ACCESS_DENIED);
			ServletUtil.write(response, HttpStatus.FORBIDDEN, json);
		}
	}
}