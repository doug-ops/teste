/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.common.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manager.systems.web.api.common.constants.CommonConstants;
import com.manager.systems.web.api.common.constants.MessageConstants;

import lombok.Data;

@Data
public class ApiError {
	private HttpStatus status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstants.DD_MM_YYYY_HH_MM_SS)
	private LocalDateTime timestamp;
	private String message;
	private String debugMessage;
	private List<ApiSubError> subErrors;

	private ApiError() {
		timestamp = LocalDateTime.now();
	}

	ApiError(final HttpStatus status, final String message) {
		this();
		this.status = status;
	}

	ApiError(final HttpStatus status) {
		this();
		this.status = status;
	}

	ApiError(final HttpStatus status, final Throwable ex) {
		this();
		this.status = status;
		this.message = MessageConstants.MSG_UNEXPECTED_ERROR;
		this.debugMessage = ex.getLocalizedMessage();
	}

	ApiError(final HttpStatus status, final String message, final Throwable ex) {
		this();
		this.status = status;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
	}
}
