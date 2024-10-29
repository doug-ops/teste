
/*
 * Date creatye 30/06/2020.
 */
package com.manager.systems.web.api.common.exception;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.manager.systems.web.api.common.constants.MessageConstants;

@RestControllerAdvice
public class ExceptionConfig extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ EmptyResultDataAccessException.class })
	public ResponseEntity<?> errorNotFound() {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler({ IllegalArgumentException.class })
	public ResponseEntity<?> errorBadRequest() {
		return ResponseEntity.badRequest().build();
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status,
			final WebRequest request) {
		return buildResponseEntity(new ApiError(HttpStatus.METHOD_NOT_ALLOWED, MessageConstants.MSG_MEHOD_NOT_ALLOWED));
	}

	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(final EntityNotFoundException ex) {
		return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex.getMessage()));
	}

	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<Object> handleEntityNotFound(final AccessDeniedException ex) {
		return buildResponseEntity(new ApiError(HttpStatus.FORBIDDEN, MessageConstants.MSG_FORBIDDEN));
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(final UsernameNotFoundException ex) {
		return buildResponseEntity(new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage()));
	}

	@ExceptionHandler(BadCredentialsException.class)
	protected ResponseEntity<Object> handleEntityNotFound(final BadCredentialsException ex) {
		return buildResponseEntity(new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage()));
	}

	private ResponseEntity<Object> buildResponseEntity(final ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
}