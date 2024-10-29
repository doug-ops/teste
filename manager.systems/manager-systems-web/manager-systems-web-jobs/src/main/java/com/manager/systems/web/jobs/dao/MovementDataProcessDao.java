/**
 * Date create 03/04/2020.
 */
package com.manager.systems.web.jobs.dao;

import com.manager.systems.web.jobs.dao.impl.MovementDataProccessDTO;

public interface MovementDataProcessDao 
{
	void getNextMovementProcess(MovementDataProccessDTO movementData) throws Exception;
	boolean processMovementData(MovementDataProccessDTO movementData) throws Exception;
}