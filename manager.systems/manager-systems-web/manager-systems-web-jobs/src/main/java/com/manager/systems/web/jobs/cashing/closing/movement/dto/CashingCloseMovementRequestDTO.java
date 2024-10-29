/*
 * Date create 14/05/2024.
 */
package com.manager.systems.web.jobs.cashing.closing.movement.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CashingCloseMovementRequestDTO implements Serializable {

	private static final long serialVersionUID = 2978128774298222705L;
	
	private int operation;
	private long userId;
	private String usersChildrenParent;
	private int weekYear;
	private long userChange;

}
