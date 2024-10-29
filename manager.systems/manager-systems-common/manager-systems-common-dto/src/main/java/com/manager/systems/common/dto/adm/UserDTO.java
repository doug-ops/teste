package com.manager.systems.common.dto.adm;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO implements Serializable 
{
	private static final long serialVersionUID = 7927565459010117025L;
	
	private long id;
	private long userOperation;
	private long companyId;
}