package com.manager.systems.common.dao.movement;

import java.sql.Connection;

import com.manager.systems.common.dto.adm.ReportDocumentMovementDTO;

public interface DocumentMovementDao 
{
	void getAllMovement(ReportDocumentMovementDTO report, Connection connection) throws Exception;
}