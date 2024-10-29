package com.manager.systems.movement.product.bravo.service;

import java.util.List;

import com.manager.systems.movement.product.bravo.dto.BillingBravoGamesJsonDTO;
import com.manager.systems.movement.product.bravo.dto.BillingBravoGamesRoomItemDTO;
import com.manager.systems.movement.product.bravo.dto.BravoDTO;
import com.manager.systems.movement.product.bravo.dto.BravoMachineDTO;
import com.manager.systems.movement.product.bravo.utils.http.HttpURLConnection;

public interface BillingBravoService {
	void login(BravoDTO bravo, HttpURLConnection http) throws Exception;
	BillingBravoGamesJsonDTO getMachines(BravoDTO bravo, HttpURLConnection http) throws Exception;
	List<BillingBravoGamesRoomItemDTO> getRooms(BravoDTO bravo, HttpURLConnection http) throws Exception;
	BillingBravoGamesJsonDTO getMovements(BillingBravoGamesRoomItemDTO room, BravoDTO bravo, HttpURLConnection http) throws Exception;
	boolean saveRoom(BillingBravoGamesRoomItemDTO room) throws Exception;
	boolean saveRoomItem(BravoMachineDTO roomItem) throws Exception;
}