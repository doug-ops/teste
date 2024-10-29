/*
 * Date create 14/05/2024.
 */
package com.manager.systems.web.jobs.cashing.closing.movement.service;

import com.manager.systems.web.jobs.cashing.closing.movement.dto.CashingCloseMovementRequestDTO;

public interface CashingCloseMovementService {

	void generateMovement(CashingCloseMovementRequestDTO request) throws Exception;

}