/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.common.security.jwt;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.google.gson.JsonObject;
import com.manager.systems.web.api.common.constants.CommonConstants;

public class ServletUtil {

	public static void write(final HttpServletResponse response, final HttpStatus status, final String json)
			throws IOException {
		response.setStatus(status.value());
		response.setCharacterEncoding(CommonConstants.UTF_8);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(json);
	}

	public static String getJson(final String key, final String value) {
		final JsonObject json = new JsonObject();
		json.addProperty(key, value);
		return json.toString();
	}
}