/*
 * Date create 22/09/2023.
 */
package com.manager.systems.movement.product.bravo.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;

import com.manager.systems.common.vo.OperationType;
import com.manager.systems.movement.product.bravo.dao.BillingBravoDao;
import com.manager.systems.movement.product.bravo.dto.BillingBravoGamesRoomItemDTO;
import com.manager.systems.movement.product.bravo.dto.BravoMachineDTO;

public class BillingBravoDaoImpl implements BillingBravoDao {
	
	private Connection connection;

	public BillingBravoDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public boolean saveRoom(final BillingBravoGamesRoomItemDTO room) throws Exception {
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_sincronize_bravo_room");
		query.append("(");
		query.append("?,"); // 01 - @operation INT
		query.append("?,"); // 02 - IN _room_id INT
		query.append("?,"); // 03 - IN _room_description VARCHAR(100)
		query.append("?,"); // 04 - IN _server_id INT
		query.append("?,"); // 05 - IN _quantity_machines INT
		query.append("? "); // 06 - IN _inactive BIT
		query.append(")");
		query.append("}");

		boolean result = false;

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); // 01 - @operation INT
			statement.setLong(2, room.getRoomId()); // 02 - IN _room_id BIGINT
			statement.setString(3, room.getRoomName()); // 03 - IN _room_description VARCHAR(100)
			statement.setInt(4, room.getServerId()); // 04 - IN _server_id INT
			statement.setInt(5, room.getQuantityMachine()); // 05 - IN _quantity_machines INT
			statement.setBoolean(6, room.isInactive()); // 06 - IN _inactive BIT
			result = (statement.executeUpdate()>0);  
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		
		return result;
	}

	@Override
	public boolean saveRoomItem(final BravoMachineDTO roomItem) throws Exception {
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_sincronize_bravo_room_item");
		query.append("(");
		query.append("?,"); // 01 - @operation INT
		query.append("?,"); // 02 - IN _room_id INT
		query.append("?,"); // 03 - IN _machine_id BIGINT
		query.append("?,"); // 04 - IN _machine_description VARCHAR(100)
		query.append("?,"); // 05 - IN _machine_contract VARCHAR(50)
		query.append("?,"); // 06 - IN _input_movement BIGINT
		query.append("?,"); // 07 - IN _output_movement BIGINT
		query.append("? "); // 08 - IN _inactive BIT
		query.append(")");
		query.append("}");

		boolean result = false;

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); // 01 - @operation INT
			statement.setLong(2, roomItem.getRoomId()); // 02 - IN _room_id BIGINT
			statement.setLong(3, roomItem.getMachineId()); // 03 - IN _machine_id BIGINT
			statement.setString(4, roomItem.getMachineDescription()); // 04 - IN _machine_description VARCHAR(100)
			statement.setString(5, roomItem.getContract()); // 05 - IN _machine_contract VARCHAR(50)
			statement.setLong(6, roomItem.getInputMovement()); // 06 - IN _input_movement BIGINT
			statement.setLong(7, roomItem.getOutputMovement()); // 07 - IN _output_movement BIGINT
			statement.setBoolean(8, roomItem.isInactive()); // 06 - IN _inactive BIT
			result = (statement.executeUpdate()>0);  
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		
		return result;
	}
}
