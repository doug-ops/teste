package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonBankAccountPermissionDTO implements Serializable {

	private static final long serialVersionUID = -7685722658498806682L;
	
	private long userId;
	private int bankAccountId;
	private boolean inactive;
	private long userChange;
	private Calendar changeDate;
}