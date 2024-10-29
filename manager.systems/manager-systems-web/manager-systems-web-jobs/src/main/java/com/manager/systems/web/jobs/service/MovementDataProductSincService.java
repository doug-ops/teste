package com.manager.systems.web.jobs.service;

import java.sql.Connection;

import com.manager.systems.web.jobs.dto.MovementDataProductSincDTO;
import com.manager.systems.web.jobs.dto.MovementDataProductSincItemDTO;

public interface MovementDataProductSincService 
{
	void getProductMovementsPendingSinc(MovementDataProductSincDTO movement, Connection connection) throws Exception;
	boolean saveProductMovementPendingSinc(MovementDataProductSincItemDTO item, Connection connection) throws Exception;
	boolean changeSincStateProductMovementsPendingSinc(MovementDataProductSincItemDTO item, Connection connection) throws Exception;
}