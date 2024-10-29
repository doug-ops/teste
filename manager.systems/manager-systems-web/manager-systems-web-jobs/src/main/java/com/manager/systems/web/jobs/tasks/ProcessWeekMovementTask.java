/*
 * Date create 02/09/2023.
 */
package com.manager.systems.web.jobs.tasks;

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.manager.systems.web.jobs.dao.FactoryConnection;
import com.manager.systems.web.jobs.dto.JobDTO;
import com.manager.systems.web.jobs.dto.RomaneioFilterDTO;
import com.manager.systems.web.jobs.service.RomaneioService;
import com.manager.systems.web.jobs.service.impl.RomaneioServiceImpl;
import com.manager.systems.web.jobs.utils.ConstantDataManager;

public class ProcessWeekMovementTask implements Runnable 
{
	private static final Log log = LogFactory.getLog(ProcessWeekMovementTask.class);

	private JobDTO job;
	
	public ProcessWeekMovementTask(final JobDTO job) 
	{
		super();
		this.job = job;
	}
	
	@Override
	public void run() 
	{
		try
		{
			this.handleJob();	
		}
		catch (final Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	private void handleJob() throws Exception {
		Connection connection = null;

		try
		{
			if(this.job!=null && this.job.getParentId()>0)
			{
				if(ConstantDataManager.TASK_PROCESS_NEXT_WEEK_MOVEMENT.equalsIgnoreCase(this.job.getDescription().trim()))
				{
					log.info(ConstantDataManager.GLOBAL_MSG_INITIAL_SINC + ConstantDataManager.TASK_PROCESS_NEXT_WEEK_MOVEMENT);
					
					connection = FactoryConnection.getConnectionPortal();
					connection.setAutoCommit(false);	
					
					final RomaneioService romaneioService = new RomaneioServiceImpl(connection);
					
					final RomaneioFilterDTO filter = RomaneioFilterDTO.builder().build();
					filter.setOperation(4);
					filter.setCompanyId(0L);
					filter.setDocumentParentId(0L);
					filter.setUserId(1L);
					filter.setDocumentNote(ConstantDataManager.BLANK);
					filter.setCountWeekProcess(0);
					
					romaneioService.generateNextWeekMovement(filter);	
					
					connection.commit();
				}
			}
		}
		catch (final Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage());

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
	}
}