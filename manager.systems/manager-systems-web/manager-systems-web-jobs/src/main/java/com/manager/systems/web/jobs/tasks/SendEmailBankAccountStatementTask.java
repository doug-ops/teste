/*
 * Date create 13/12/2021.
 */
package com.manager.systems.web.jobs.tasks;

import java.sql.Connection;
import java.time.LocalDateTime;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.manager.systems.common.service.bankAccount.BankAccountStatementService;
import com.manager.systems.common.service.bankAccount.impl.BankAccountStatementServiceImpl;
import com.manager.systems.web.jobs.dao.FactoryConnection;
import com.manager.systems.web.jobs.dto.JobDTO;
import com.manager.systems.web.jobs.service.JobService;
import com.manager.systems.web.jobs.service.impl.JobServiceImpl;
import com.manager.systems.web.jobs.utils.ConstantDataManager;

public class SendEmailBankAccountStatementTask implements Runnable {
	private static final Log log = LogFactory.getLog(SendEmailBankAccountStatementTask.class);

	private JobDTO job;
	
	public SendEmailBankAccountStatementTask(final JobDTO job) 
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
				this.refreshJob(this.job);
				if(ConstantDataManager.TASK_SEND_EMAIL_BANK_ACCOUNT_STATEMENT.equalsIgnoreCase(this.job.getDescription()) && this.job.canProcess())
				{
					log.info(ConstantDataManager.GLOBAL_MSG_INITIAL_SINC + ConstantDataManager.TASK_SEND_EMAIL_BANK_ACCOUNT_STATEMENT);
					
					connection = FactoryConnection.getConnectionPortal();
					final BankAccountStatementService bankAccountService = new BankAccountStatementServiceImpl(connection);
					bankAccountService.sendEmailBankAccountStatement();
					log.info(ConstantDataManager.GLOBAL_MSG_SAVE_JOB + ConstantDataManager.SPACE + ConstantDataManager.TASK_SEND_EMAIL_BANK_ACCOUNT_STATEMENT);
					this.job.setProcessingStatus(true);
					this.job.setProcessingData(LocalDateTime.now());
					this.saveJob(job);	
				}
			}	
		}
		catch (final Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage());
		}
		finally
		{
			if(connection!=null)
			{
				connection.close();
			}
		}
		
	}
	
	private void saveJob(final JobDTO job) throws Exception
	{		
		Connection connection = null;
		try
		{
			connection = FactoryConnection.getConnectionPortal();
			connection.setAutoCommit(false);
			final JobService jobService = new JobServiceImpl();
			jobService.save(job, connection);
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
	}
	
	private void refreshJob(final JobDTO job) throws Exception
	{		
		Connection connection = null;
		try
		{
			connection = FactoryConnection.getConnectionPortal();
			final JobService jobService = new JobServiceImpl();
			jobService.get(job, connection);
		}
		catch (final Exception e) 
		{
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
