/*
 * Date create 30/03/2021.
 */
package com.manager.systems.web.jobs.cashing.closing.movement.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;

import com.manager.systems.web.jobs.cashing.closing.movement.dao.CashingCloseMovementDao;
import com.manager.systems.web.jobs.cashing.closing.movement.dto.CashingCloseMovementRequestDTO;

public class CashingCloseMovementDaoImpl implements CashingCloseMovementDao {
	
	private Connection connection;
	
	public CashingCloseMovementDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public void generateMovement(final CashingCloseMovementRequestDTO request) throws Exception {
		
		CallableStatement statement = null;
		
		final StringBuilder query = new StringBuilder();		
		query.append("{");
		query.append("call pr_cashier_closing_generate_movement");
		query.append("(");
		query.append("?,");  //01 - IN _operation TINYINT 
		query.append("?,");  //02 - IN _user_id BIGINT
		query.append("?,");  //03 - IN _users_children_parent VARCHAR(5000)
		query.append("?,");  //04 - IN _week_year INT
		query.append("? ");  //05 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
				
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, request.getOperation());
			statement.setLong(2, request.getUserId());
			statement.setString(3, request.getUsersChildrenParent());
			statement.setInt(4, request.getWeekYear());
			statement.setLong(5, request.getUserChange());
			statement.executeUpdate();  			
		}
		catch(final Exception ex)
		{
			throw ex;
		}
		finally
		{
			if(statement!=null)
			{
				statement.close();
			}
		}
	}
}