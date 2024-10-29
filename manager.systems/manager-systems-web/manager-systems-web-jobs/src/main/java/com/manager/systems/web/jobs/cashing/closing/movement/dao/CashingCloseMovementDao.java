/*
 * Date create 14/05/2024.
 */
package com.manager.systems.web.jobs.cashing.closing.movement.dao;

import com.manager.systems.web.jobs.cashing.closing.movement.dto.CashingCloseMovementRequestDTO;

public interface CashingCloseMovementDao {

	void generateMovement(CashingCloseMovementRequestDTO request) throws Exception;

}