package com.manager.systems.web.jobs.tasks;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.manager.systems.web.jobs.dao.FactoryConnection;
import com.manager.systems.web.jobs.dto.DocumentSincDTO;
import com.manager.systems.web.jobs.dto.JobDTO;
import com.manager.systems.web.jobs.service.DocumentSincService;
import com.manager.systems.web.jobs.service.JobService;
import com.manager.systems.web.jobs.service.impl.DocumentSincServiceImpl;
import com.manager.systems.web.jobs.service.impl.JobServiceImpl;
import com.manager.systems.web.jobs.utils.ConstantDataManager;

public class SincronizeDocumentTask implements Runnable 
{
	private static final Log log = LogFactory.getLog(SincronizeDocumentTask.class);

	private JobDTO job;
	
	public SincronizeDocumentTask(final JobDTO job) 
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
				if(ConstantDataManager.TASK_SINC_MOVEMENT_DOCUMENT.equalsIgnoreCase(this.job.getDescription()))
				{
					log.info(ConstantDataManager.GLOBAL_MSG_INITIAL_SINC + ConstantDataManager.TASK_SINC_MOVEMENT_DOCUMENT);

					final List<DocumentSincDTO> itens = this.getDocumentsMovement(this.job);
					log.info(ConstantDataManager.GLOBAL_MSG_ITENS_TO_SINC + itens.size() + ConstantDataManager.SPACE + ConstantDataManager.TASK_SINC_MOVEMENT_DOCUMENT);

					if(itens.size()>0)
					{
						log.info(ConstantDataManager.GLOBAL_MSG_SAVE_ITENS + itens.size() + ConstantDataManager.SPACE + ConstantDataManager.TASK_SINC_MOVEMENT_DOCUMENT);
						this.saveDocumentsMovement(itens);

						job.setInitialRecord(String.valueOf(itens.get(0).getId()));
						job.setVersionInitialRecord(itens.get(0).getVersionRegister());
						job.setFinalRecord(String.valueOf(itens.get(itens.size()-1).getId()));
						job.setVersionFinalRecord(itens.get(itens.size()-1).getVersionRegister());						
					}
					log.info(ConstantDataManager.GLOBAL_MSG_SAVE_JOB + itens.size() + ConstantDataManager.SPACE + ConstantDataManager.TASK_SINC_MOVEMENT_DOCUMENT);
					saveJob(job);					
					log.info(ConstantDataManager.GLOBAL_MSG_SINC_FINISH + ConstantDataManager.SPACE + itens.size() + ConstantDataManager.SPACE + ConstantDataManager.TASK_SINC_MOVEMENT_DOCUMENT);
				}
			}
		}
		catch (final Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	private List<DocumentSincDTO> getDocumentsMovement(final JobDTO job) throws Exception
	{
		final List<DocumentSincDTO> itens = new ArrayList<DocumentSincDTO>();
		
		Connection connection = null;
		try
		{
			connection = FactoryConnection.getConnectionRetaguarda();
			final DocumentSincService documentSincService = new DocumentSincServiceImpl();
			itens.addAll(documentSincService.getAllMajorVersionRecord(job, connection));
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
		return itens;
	}
	
	private void saveDocumentsMovement(final List<DocumentSincDTO> itens) throws Exception
	{		
		Connection connection = null;
		try
		{
			connection = FactoryConnection.getConnectionPortal();
			final DocumentSincService documentSincService = new DocumentSincServiceImpl();
			for (final DocumentSincDTO documentSinc : itens) 
			{
				documentSincService.save(documentSinc, connection);				
			}
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
	
	private void saveJob(final JobDTO job) throws Exception
	{		
		Connection connection = null;
		try
		{
			connection = FactoryConnection.getConnectionPortal();
			final JobService jobService = new JobServiceImpl();
			jobService.save(job, connection);
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