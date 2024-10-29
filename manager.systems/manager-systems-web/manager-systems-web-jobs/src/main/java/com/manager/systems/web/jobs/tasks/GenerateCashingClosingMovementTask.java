package com.manager.systems.web.jobs.tasks;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.temporal.IsoFields;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.web.jobs.cashing.closing.movement.dto.CashingCloseMovementRequestDTO;
import com.manager.systems.web.jobs.cashing.closing.movement.service.CashingCloseMovementService;
import com.manager.systems.web.jobs.cashing.closing.movement.service.impl.CashingCloseMovementServiceImpl;
import com.manager.systems.web.jobs.dao.FactoryConnection;
import com.manager.systems.web.jobs.dto.JobDTO;
import com.manager.systems.web.jobs.utils.ConstantDataManager;
import com.manager.systems.web.jobs.vo.OperationType;

public class GenerateCashingClosingMovementTask implements Runnable 
{
	private static final Log log = LogFactory.getLog(GenerateCashingClosingMovementTask.class);

	private JobDTO job;
	
	public GenerateCashingClosingMovementTask(final JobDTO job) 
	{
		super();
		this.job = job;
	}
	
	@Override
	public void run() 
	{
		try
		{
			if(this.job!=null && this.job.getParentId()>0)
			{
				if(ConstantDataManager.TASK_SINC_CASHING_CLOSING_MOVEMENT.equalsIgnoreCase(this.job.getDescription()))
				{
					log.info(ConstantDataManager.GLOBAL_MSG_INITIAL_SINC + ConstantDataManager.TASK_SINC_CASHING_CLOSING_MOVEMENT);
					this.generateCashingClosingMovement();
					log.info(ConstantDataManager.GLOBAL_MSG_PROCESS_MOVEMENT_FINISH);
				}
			}
		}
		catch (final Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	private boolean generateCashingClosingMovement() throws Exception
	{		
		boolean result = false;
				
		Connection connection = null;
		try
		{
			connection = FactoryConnection.getConnectionPortal();
			connection.setAutoCommit(false);			
			
			final int weekYear = Integer.parseInt(String.valueOf(LocalDate.now().getYear()) + StringUtils.padLeft(String.valueOf(LocalDate.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)), 2, ConstantDataManager.ZERO_STRING));
			
			final CashingCloseMovementRequestDTO request = CashingCloseMovementRequestDTO.builder()
					.operation(OperationType.SAVE.getType())
					.userId(8)
					.usersChildrenParent(null)
					.weekYear(weekYear)
					.userChange(8)
					.build();
			final CashingCloseMovementService cashingCloseMovementService = new CashingCloseMovementServiceImpl(connection);
			
			cashingCloseMovementService.generateMovement(request);
			
			request.setUserId(7);
			request.setUserChange(7);
			
			cashingCloseMovementService.generateMovement(request);
			
			connection.commit();
		}
		catch (final Exception e) 
		{
			if(connection!=null)
			{
				connection.rollback();
			}
			throw e;
		}
		finally
		{
			if(connection!=null)
			{
				connection.close();
			}
		}
		
		return result;
	}
}