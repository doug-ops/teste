/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.common.domain.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BaseObject {
	@Column(nullable = false)
	private Boolean inactive;
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date createdAt;
	@Column(length = 50, nullable = false)
	private Long createdUser;
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date updatedAt;
	@Column(length = 50, nullable = false)
	private Long updatedUser;
}