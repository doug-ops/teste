/*
 * Date create 30/06/2020. 
 */
package com.manager.systems.web.api.common.controller;

import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.manager.systems.web.api.common.constants.CommonConstants;
import com.manager.systems.web.api.common.constants.ControllerConstants;
import com.manager.systems.web.api.common.domain.dto.BaseDTO;
import com.manager.systems.web.api.common.security.jwt.JwtUtil;

public abstract class BaseController {
	protected URI getUri(final Object id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path(ControllerConstants.URL_API_PARAMETER_ID)
				.buildAndExpand(id).toUri();
	}
	
	protected void populateDataDTO(final BaseDTO dto) {
		dto.setUserId(JwtUtil.getAuthId());
		dto.setActualDate(ZonedDateTime.now(ZoneId.of(CommonConstants.TIMEZONE_SAO_PAULO)));
	}
}