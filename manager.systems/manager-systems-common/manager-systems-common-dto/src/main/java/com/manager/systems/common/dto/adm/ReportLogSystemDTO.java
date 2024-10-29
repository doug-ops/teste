package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportLogSystemDTO implements Serializable
{
	private static final long serialVersionUID = -3987913989528926680L;
	
	private int id;
	private String changeDate;
	private int systemId;
	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;
	private int countLogSystem;
	private long objectId;
	private long userId;
}