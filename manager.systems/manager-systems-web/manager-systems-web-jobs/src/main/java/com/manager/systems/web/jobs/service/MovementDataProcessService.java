/**
 * Date create 03/04/2020.
 */
package com.manager.systems.web.jobs.service;

import com.manager.systems.web.jobs.dao.impl.MovementDataProccessDTO;

public interface MovementDataProcessService 
{
	void getNextMovementProcess(MovementDataProccessDTO movementData) throws Exception;
	boolean processMovementData(MovementDataProccessDTO movementData) throws Exception;
}