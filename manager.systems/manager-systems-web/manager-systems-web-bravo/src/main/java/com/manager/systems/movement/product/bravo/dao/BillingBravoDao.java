/*
 * Date create 22/09/2023.
 */
package com.manager.systems.movement.product.bravo.dao;

import com.manager.systems.movement.product.bravo.dto.BillingBravoGamesRoomItemDTO;
import com.manager.systems.movement.product.bravo.dto.BravoMachineDTO;

public interface BillingBravoDao {
	
	boolean saveRoom(BillingBravoGamesRoomItemDTO room) throws Exception;
	boolean saveRoomItem(BravoMachineDTO roomItem) throws Exception;
}