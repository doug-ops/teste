/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.common.domain.dto;

import java.time.ZonedDateTime;
import java.util.Date;

import com.manager.systems.web.api.common.enums.OperationsType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseDTO {
	private Boolean inactive;
	private Date createdAt;
	private Long createdUser;
	private Date updatedAt;
	private Long updatedUser;
	private Long userId;
	private ZonedDateTime actualDate;
	private OperationsType operationsType;
}